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
      var $currentTab =  plfdemo.workspace.IDE.getSelectedTabContainer();
      var code = $('#code-editor', $currentTab).val();
      if (!code || $.trim(code).length == 0) return;
      var index = plfdemo.workspace.IDE.getSelectedIndex();
      var guid = plfdemo.workspace.IDE.tabsIndex[index];
      if (guid && guid.length > 0) {
        var authorName = plfdemo.workspace.IDE.tabsInfo[guid].app.authorName;
        if (authorName && (authorName != plfdemo.Main.username)) {
          console.debug('No permission to save this application.');
          return false;
        } else {
          me.saveCode(guid, code, '');
        }
      } else {
        me.saveCode(null, code, '');
      }
      
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
      height: 350,
      width: 460,
      modal: true,
      resizable: false,
      buttons: {
        "Save": function() {
          //
          console.info('Creating app.');
          me.createApp($('#new-app-name', $(this)).val(), $('#new-app-language', $(this)).val(), 
              $('#new-app-description', $(this)).val(), $('#code-editor').val(), 
              $('#new-app-public', $(this)).val(), $('#new-app-tags', $(this)).val());
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
  
  ide.sessions[tabIndex].status = "running";
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
          ide.sessions[tabIndex].output += ('\n' + result['output']);
          if (tabIndex == ide.getSelectedIndex) {
            me.jqconsole.Write(result['output'], 'jqconsole-output');
            me.startPrompt();
          }
          
          var visualizations = result['visualizations'];
          var index;
          for (index in visualizations) {
            me.renderBase64PNG('', visualizations[index]);
          }
          
          ide.sessions[tabIndex].status = "finished-running";
          if (tabIndex == ide.getSelectedIndex()) {
            this.setStatus("finished-running");
          }
          
        } else {
          console.debug(result);
          ide.sessions[tabIndex].status = "error";
          if (tabIndex == ide.getSelectedIndex()) {
            this.setStatus("error");
          }
        }     
        
        //refresh(); 
      },
      error: function() {
        ide.sessions[tabIndex].status = "error";
        if (tabIndex == ide.getSelectedIndex()) {
          this.setStatus("error");
        }
      }
    });
  });
};

/**
 * Sets session info to workspace info: console, status, variables
 */
Workspace.prototype.setSession = function(index) {
  var session = plfdemo.workspace.IDE.sessions[index];
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
      break;
    case "finished-running":
      $("#ajax-message").text("Finished running.");
      $("#ajax-loading").css("display", "none");
      break;
    case "updating":
      $("#ajax-message").text("Updating...");
      $("#ajax-loading").css("display", "inline");
      break;
    case "finished-save":
      $("#ajax-loading").css("display", "none");
      $("#ajax-message").text("Updated.");
      break;
    case "error":
      $("#ajax-loading").css("display", "none");
      $("#ajax-message").text("Error");
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
  ide.sessions[ide.getSelectedIndex()].output = '';
}

Workspace.prototype.getConsoleCaret = function(language) {
  if (language == "python") return ">>>";
  else if (language == "r") return ">";
  else return null;
}


/**
 * Saves the application code
 */
Workspace.prototype.saveCode = function(appGuid, code, language) {
  
  if (appGuid == null || appGuid == '') {   
    // open dialog
    var $dialog = $('#new-app-dialog'); 
    $dialog.dialog("open");
    $('#new-app-language', $dialog).val(language);
    
    return;
  }
  var ide = plfdemo.workspace.IDE;
  
  $("#ajax-loading").css("display", "inline");
  $("#ajax-message").text("Saving...");
  $.ajax({
    url : plfdemo.Main.ctx + "/app/" + appGuid + "/save",
    data : {
      
      code : code,
      language: language
    },
    success : function(json) {
      console.info("Update code: " + JSON.stringify(json));
      $("#ajax-loading").css("display", "none");
      $("#ajax-message").text("Updated!");
    },
    error : function(json) {
      console.info("Update code: " + JSON.stringify(json));
      $("#ajax-loading").css("display", "none");
      $("#ajax-message").text("Update code: Failed.");
    },
    type : "post"
  });
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
      dataType: "json", 
      success: function(json) {
        //window.location.href = plfdemo.Main.ctx + "/app/" + appname + "#app";
        window.location.reload(true);
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

plfdemo.Workspace = new Workspace();
plfdemo.Workspace.init();