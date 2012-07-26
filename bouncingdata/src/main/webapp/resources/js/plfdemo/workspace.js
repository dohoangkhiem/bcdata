/**
 * This class represents the development workspace.
 * It contains 3 components: multiple tabs editor (code editor), workspace info area (logs, variables), output area (visuals, datasets) 
 */
function Workspace() {
  this.jqconsole = {}
}

/**
 * Initializes the workspace environment: app. actions, tabs layout, pop-up windows, output console
 */
Workspace.prototype.init = function() {
  var me = this;
  $(function() {
    // App. actions
    $('.app-actions #save-app').click(function() {
      var index = plfdemo.workspace.IDE.getSelectedIndex();
      me.saveCode(index);      
      return false;
    });
    
    $('.app-actions #run-app').click(function() {
      // get current selected tab from IDE
      var index = plfdemo.workspace.IDE.getSelectedIndex();
      me.execute(index);
      return false;
    });
    
    // Init tabs layout
    $('#workspace-info-tabs').tabs();
    $('#app-output-tabs').tabs();
    
    // Init popup dialog
    $('#new-app-dialog').dialog({
      autoOpen: false,
      height: 345,
      width: 460,
      modal: true,
      resizable: false,
      buttons: {
        "Save": function() {
          //
          console.info('Creating app.');
          me.createApp($('#new-app-name', $(this)).val(), $('#new-app-language', $(this)).val(), 
              $('#new-app-description', $(this)).val(), $('#code-editor', plfdemo.workspace.IDE.getSelectedTabContainer()).val(), 
              $('#new-app-public', $(this)).val(), $('#new-app-tags', $(this)).val());
          $(this).dialog("close");
        }, 
        "Cancel": function() {
          $(this).dialog("close");
        }
      },
      close: function() {     
      }
    });
    
    // jqConsole
    me.initConsole('python');
  });
}

/**
 * Executes an application or code
 */
Workspace.prototype.execute = function(tabIndex) {
  var me = this;
  var ide = plfdemo.workspace.IDE;
  
  var appGuid = ide.tabsIndex[tabIndex];
  var $tab =  ide.getTabContainer(tabIndex);
  var code = $('#code-editor', $tab).val();
  if (!code || $.trim(code).length == 0) return;
  
  var language = $('#app-language', $tab).val();
  
  //ide.sessions[tabIndex].status = "running";
  var tabId = ide.getTabId(tabIndex);
  if (tabId) {
    ide.sessions_[tabId].status = "running";
  } else {
    console.debug('The tab has been closed, skip executing');
    return;
  }
  
  if (tabIndex == ide.getSelectedIndex()) {
    this.setStatus("running");
  }
    
  var url;
  if (appGuid) {
    url = plfdemo.Main.ctx + "/app/" + appGuid + "/execute";
  } else {
    url = plfdemo.Main.ctx + "/main/execute"; 
  }
  $(function() {
    $.ajax({
      url: url,
      type: "post",
      data: {
        code: code,
        language: language
      },
      success: function(result) {
        $("#console").show(); 
        // if this app. has ran (not sure successful or not)
        if (result['statusCode'] >= 0) {
          //if (!ide.sessions[tabIndex].output) ide.sessions[tabIndex].output = "";
          //ide.sessions[tabIndex].output += (result['output']);
          //ide.sessions[tabIndex].status = "finished-running";
          if (!ide.sessions_[tabId].output) ide.sessions_[tabId].output = "";
          ide.sessions_[tabId].output += (result['output']);
          ide.sessions_[tabId].status = "finished-running";
          if (tabIndex == ide.getSelectedIndex()) {
            me.jqconsole.Write(result['output'], 'jqconsole-output');
            me.startPrompt();
            me.setStatus("finished-running");
          }
          
          var datasets = result['datasets'];
          console.debug(datasets);
          var $dsContainer = $(".app-output-tabs #app-datasets");
          $dsContainer.empty();
          // render datasets
          for (d in datasets) {
            me.renderDataset(d, JSON.parse(datasets[d]));
          }
          
          var visuals = result['visualizations'];
          console.debug(visuals);
          var $vsSlider = $("#visualization-slider");
          $vsSlider.empty();
          for (v in visuals) {
            var type = visuals[v].type;
            if (type == "png" || type == "PNG") {
              me.renderBase64PNG('', visuals[v].source);
            } else if (type == "html" || type == "HTML") {
              //
              console.info("Render HTML visualization:" + v);
            }
          }
                    
        } else {
          console.debug(result);
          ide.sessions_[tabId].status = "error";
          if (tabIndex == ide.getSelectedIndex()) {
            me.setStatus("error");
          }
        }     
        
        //refresh(); 
      },
      error: function() {
        //ide.sessions_[tabIndex].status = "error";
        ide.sessions_[tabId].status = "error";
        if (tabIndex == ide.getSelectedIndex()) {
          me.setStatus("error");
        }
      }
    });
  });
};

/**
 * Sets session info to workspace info: console, status, variables
 */
Workspace.prototype.setSession = function(tabId) {
  //var session = plfdemo.workspace.IDE.sessions[index];
  //var tabId = plfdemo.workspace.IDE.getTabId(index);
  if (!tabId) return;
  var session = plfdemo.workspace.IDE.sessions_[tabId];
  if (!session) return;
  
  // set status
  var status = session.status;
  this.setStatus(status);
  
  var console = this.jqconsole;
  console.Reset();
  console.Write(session.output, 'jqconsole-output');
  this.startPrompt();
  
  // set variables from session
  
}

Workspace.prototype.setStatus = function(status) {
  if (status) {
    switch(status) {
    case "running":
      $("#ajax-message").text("Running...");
      $("#ajax-loading").css("display", "inline");
      $("#ajax-message").css("color", "green");
      break;
    case "finished-running":
      $("#ajax-message").text("Finished running.");
      $("#ajax-loading").css("display", "none");
      $("#ajax-message").css("color", "green");
      break;
    case "updating":
      $("#ajax-message").text("Updating...");
      $("#ajax-loading").css("display", "inline");
      $("#ajax-message").css("color", "green");
      break;
    case "finished-save":
      $("#ajax-loading").css("display", "none");
      $("#ajax-message").text("Updated.");
      $("#ajax-message").css("color", "green");
      break;
    case "error":
      $("#ajax-loading").css("display", "none");
      $("#ajax-message").text("Error");
      $("#ajax-message").css("color", "red");
    }
  } else {
    $("#ajax-message").text("");
    $("#ajax-loading").css("display", "none");
  }
}


/**
 * Initializes the console
 */
Workspace.prototype.initConsole = function(language) {
  var me = this;
  this.jqconsole = $("#console").jqconsole('Welcome to our console\n', this.getConsoleCaret(language));
  this.startPrompt();
  //this.jqconsole.Disable();
  $("#clear-console").click(function() {
    me.clearConsole();
  });
}

/**
 * Starts console prompt and handles console action
 */
Workspace.prototype.startPrompt = function(language) {
  var me = this;
  this.jqconsole.Prompt(true, function(input) {
    $.ajax({
      url: plfdemo.Main.ctx + "/shell/execute",
      type: "get",
      data: {
        code: input,
        language: language
      },
      success: function(result) {
        me.jqconsole.Write(result + '\n', 'jqconsole-output');
        me.startPrompt();
      },
      error: function(result) {
        console.info(result);
        me.startPrompt();
      }
    });
  });
}

/**
 * Show/hide console
 */
Workspace.prototype.showConsole = function() {
  if (!$("#console").is(":visible")) {
    $("#console").show();
    $("#clear-console").show();
    $("#show-console").val("Hide console");
  } else {
    $("#console").hide();
    $("#clear-console").hide();
    $("#show-console").val("Show console");
  }
}

/**
 * Clear the console output
 */
Workspace.prototype.clearConsole = function() {
  this.jqconsole.Reset();
  this.startPrompt();
  var ide = plfdemo.workspace.IDE;
  //ide.sessions[ide.getSelectedIndex()].output = '';
  ide.sessions_[ide.getSelectedTabId()].output = '';
}

Workspace.prototype.getConsoleCaret = function(language) {
  if (language == "python") return ">>>";
  else if (language == "r") return ">";
  else return null;
}


/**
 * Saves the application code at the tab tabIndex
 */
Workspace.prototype.saveCode = function(tabIndex) {
  var me = this;
  var ide = plfdemo.workspace.IDE;
  var $currentTab =  ide.getTabContainer(tabIndex);
  var tabId = ide.getTabId(tabIndex);
  
  if(!tabId) {
    return;
  }
  
  var code = $('#code-editor', $currentTab).val();
  if (!code || $.trim(code).length == 0) {
    console.debug("No code to save.");
    return;
  }
  
  var guid = ide.tabsIndex[tabIndex];
  if (guid && guid.length > 0) {
    var authorName = ide.tabsInfo[guid].app.authorName;
    if (authorName && (authorName != plfdemo.Main.username)) {
      console.debug('No permission to save this application.');
      return false;
    } else {
      // save app. code
      
      //ide.sessions[tabIndex].status = "running";
      ide.sessions_[tabId].status = "running";
      if (tabIndex == ide.getSelectedIndex()) {
        this.setStatus("running");
      }
      
      $.ajax({
        url : plfdemo.Main.ctx + "/app/" + guid + "/save",
        data : {         
          code : code,
          language: language
        },
        success : function(json) {
          console.info("Update code: " + JSON.stringify(json));
          //ide.sessions[tabIndex].status = "finished-save";
          ide.sessions_[tabId].status = "finished-save";
          if (tabIndex == ide.getSelectedIndex()) {
            me.setStatus("finished-save");
          }
        },
        error : function(json) {
          console.info("Update code: " + JSON.stringify(json));
          //ide.sessions[tabIndex].status = "error";
          ide.sessions_[tabId].status = "error";
          if (tabIndex == ide.getSelectedIndex()) {
            me.setStatus("error");
          }
        },
        type : "post"
      });
    }
  } else {
    // create new app.
    var $dialog = $('#new-app-dialog');
    $dialog.dialog("open");
    // reset form
    $('form', $dialog).each(function() {
      this.reset();
    });
    var language = $('.app-info .language-select #app-language', $currentTab).val();
    $('#new-app-language', $dialog).val(language?language:'python');
    $('#new-app-name', $dialog).focus();
    return;
  }
  
}

/**
 * Creates new application
 */
Workspace.prototype.createApp = function(appname, language, description, code, isPublic, tags) {
  var data = { 
    appname: appname,
    language: language,
    description: description,
    code: code,
    isPublic: isPublic,
    tags: tags
  };
  $(function() {
    $.ajax({ 
      url: plfdemo.Main.ctx + "/main/createapp", 
      data: data, 
      type: "post", 
      success: function(json) {
        // returned json as guid
        if (json) {
          //refresh browser
          plfdemo.Browser.refreshMyStuff();
          var app = {name: appname, description: description, language: language, tags: tags, authorName: plfdemo.Main.username, guid: json};
          plfdemo.workspace.IDE.bindAppToTab(plfdemo.workspace.IDE.getSelectedIndex(), app);
        }
        
      }, 
      error: function() { alert("Failed to create new application!"); } });  
  });
}

Workspace.prototype.renderVisualization = function(name, appname) {
  $(function() {
    var $vsSlider = $("#visualization-slider");
    var $vsItem = $('<div class="visualization-item" id="visualization-item-' + name + '"></div>');
    $vsItem.load().appendTo($vsSlider);
    $('<iframe class="visualization-item-frame" id="visualize-' + name + '" src="' + plfdemo.Main.ctx + '/visualize/' + appname + '/' + name + '">').load().appendTo($vsItem);
    $('<span class="visualization-item-title">' + name + '</span>').load().appendTo($vsItem);
  });
}

Workspace.prototype.renderBase64PNG = function(name, source) {
  $(function() {
    var $vsSlider = $("#visualization-slider");
    var $vsItem = $('<div class="visualization-item" id="visualization-item-' + name + '"></div>');
    $vsItem.load().appendTo($vsSlider);
    $('<img src="data:image/png;base64,' + source + '" />').load().appendTo($vsItem);
    $('<span class="visualization-item-title">' + name + '</span>').load().appendTo($vsItem);
  });
}

Workspace.prototype.renderDataset = function(name, data) {
  if (data.length <= 0) return;
  $(function() {
    var $dsContainer = $(".app-output-tabs #app-datasets");
    var $dsItem = $('<div class="dataset-item" style="margin-top: 2em;"></div>');
    $dsItem.load().appendTo($dsContainer);
    $dsItem.append('<span class="dataset-item-title"><strong>' + name + '</strong></span>');
    var $table = $('<table class="dataset-item-table"></table>');
    $dsItem.append($table);
    
    // prepare data
    var first = data[0];
    var aoColumns = [];
    for (key in first) {
      aoColumns.push({ "sTitle": key});
    }
    
    var aaData = [];
    for (index in data) {
      var item = data[index];
      var arr = [];
      for (key in first) {
        arr.push(item[key]);
      }
      aaData.push(arr);
    }
    
    $table.dataTable({
      "aaData": aaData, "aoColumns": aoColumns
    });
  });
}

plfdemo.Workspace = new Workspace();
plfdemo.Workspace.init();