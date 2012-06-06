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
      console.info('Saving app.');
      me.saveCode(null, $('.app-code-editor #code-editor').val(), $('.language-select #app-language').val());
      return false;
    });
    
    $('.app-actions #run-app').click(function() {
      // get current selected tab from IDE
      var $currentTab =  plfdemo.workspace.IDE.getSelectedTabContainer();
      var code = $('#code-editor', $currentTab).val();
      if (!code || $.trim(code).length == 0) return;
      me.execute($('#code-editor', $currentTab).val(), $('#app-language', $currentTab).val(), null);
      return false;
    });
    
    // Init tabs layout
    $('#workspace-info-tabs').tabs();
    $('#app-output-tabs').tabs();
    
    // Init popup dialog
    $('#save-app-dialog').dialog({
      autoOpen: false,
      height: 300,
      width: 350,
      modal: true,
      buttons: {
        "Save": function() {
          //
          //alert("Save the app");
          me.createApp($('#new-app-name', $(this)).val(), $('#language').val() ,$('#new-app-description', $(this)).val(), $('#code-editor').val());
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
Workspace.prototype.execute = function(code, language, appname) {
  var me = this;
  var ide = plfdemo.workspace.IDE;
  $("#ajax-loading").css("display", "inline");
  $("#ajax-message").text("Running...");
  if (!$("#console").is(":visible")) {
    $("#console").show();
    $("#clear-console").show();
    $("#show-console").val("Hide console");
  }
  var url;
  if (appname && (appname != null)) {
    url = plfdemo.Main.ctx + "/app/" + appname + "/execute";
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
        //$("#console").val($("#console").val() + output + "\n");
        me.jqconsole.Write(result['output'] + '\n', 'jqconsole-output');
        me.startPrompt();
        
        ide.sessions['tabs-' + (ide.getSelectedIndex() + 1)].output = me.jqconsole.Dump();
        
        var visualizations = result['visualizations'];
        var index;
        for (index in visualizations) {
          me.renderBase64PNG('', visualizations[index]);
        }
        $("#ajax-loading").css("display", "none");
        $("#ajax-message").text("Finished running."); 
        //refresh(); 
      }
    });
  });
};

/**
 * Sets session info to workspace info
 */
Workspace.prototype.setSession = function(sessionId) {
  var session = plfdemo.workspace.IDE.sessions[sessionId];
  if (!session) return;
  //$('#code-editor').val(session.code);
  var console = this.jqconsole;
  console.Reset();
  console.Write(session.output, 'jqconsole-output');
  this.startPrompt();
  // set variables from session
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
  ide.sessions['tabs-' + (ide.getSelectedIndex() + 1)].output = '';
}

Workspace.prototype.getConsoleCaret = function(language) {
  if (language == "python") return ">>>";
  else if (language == "r") return ">";
  else return null;
}


/**
 * Saves the application code
 */
Workspace.prototype.saveCode = function(appname, code, language) {
  
  if (appname == null || appname == '') {   
    // open dialog
    $('#save-app-dialog').dialog("open");
    return;
  }
  
  $("#ajax-loading").css("display", "inline");
  $("#ajax-message").text("Saving...");
  $.ajax({
    url : plfdemo.Main.ctx + "/app/" + appname + "/save",
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
Workspace.prototype.createApp = function(appname, language, description, code) {
  var data = { 
    appname: appname,
    language: language,
    description: description,
    code: code
  };
  $(function() {
    $.ajax({ 
      url: plfdemo.Main.ctx + "/main/createApp", 
      data: data, 
      type: "post", 
      dataType: "json", 
      success: function(json) {
        window.location.href = plfdemo.Main.ctx + "/app/" + appname + "#app";
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