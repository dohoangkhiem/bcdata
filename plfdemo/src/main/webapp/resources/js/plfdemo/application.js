function Application() {
  this.jqconsole = {}
}

Application.prototype.initConsole = function(language) {
  this.jqconsole = $("#console").jqconsole('Welcome to our console\n', this.getConsoleCaret(language));
  this.startPrompt();
}

Application.prototype.initPopup = function() {
  $(function() {
    $('#save-app-dialog').dialog({
      autoOpen: false,
      height: 300,
      width: 350,
      modal: true,
      buttons: {
        "Save": function() {
          //
          //alert("Save the app");
          plfdemo.Application.createApp($('#new-app-name', $(this)).val(), $('#language').val() ,$('#new-app-description', $(this)).val(), $('#code-editor').val());
        }, 
        "Cancel": function() {
          $(this).dialog("close");
        }
      },
      close: function() {
        
      }
    });
    
    // open popup event
    //$('#save').button().click(function() {
      //$('#save-app-dialog').dialog("open");
    //});
  });
}

Application.prototype.execute = function(code, language, appname) {
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
        plfdemo.Application.jqconsole.Write(result['output'] + '\n', 'jqconsole-output');
        plfdemo.Application.startPrompt();
        var visualizations = result['visualizations'];
        var index;
        for (index in visualizations) {
          plfdemo.Application.renderBase64PNG('', visualizations[index]);
        }
        $("#ajax-loading").css("display", "none");
        $("#ajax-message").text("Finished running."); 
        //refresh(); 
      }
    });
  });
};


Application.prototype.startPrompt = function(language) {
  this.jqconsole.Prompt(true, function(input) {
    $.ajax({
      url: plfdemo.Main.ctx + "/shell/execute",
      type: "get",
      data: {
        code: input,
        language: language
      },
      success: function(result) {
        plfdemo.Application.jqconsole.Write(result + '\n', 'jqconsole-output');
        plfdemo.Application.startPrompt();
      },
      error: function(result) {
        console.info(result);
        plfdemo.Application.startPrompt();
      }
    });
  });
}

Application.prototype.showConsole = function() {
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

Application.prototype.clearConsole = function() {
  this.jqconsole.Reset();
  this.startPrompt();
}

Application.prototype.saveCode = function(appname, code, language) {
  
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

Application.prototype.getConsoleCaret = function(language) {
  if (language == "python") return ">>>";
  else if (language == "r") return ">";
  else return null;
}

Application.prototype.createApp = function(appname, language, description, code) {
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

Application.prototype.renderVisualization = function(name, appname) {
  $(function() {
    var vsSlider = $("#visualization-slider");
    var vsItem = $('<div class="visualization-item" id="visualization-item-' + name + '"></div>');
    vsItem.load().appendTo(vsSlider);
    $('<iframe class="visualization-item-frame" id="visualize-' + name + '" src="' + plfdemo.Main.ctx + '/visualize/' + appname + '/' + name + '">').load().appendTo(vsItem);
    $('<span class="visualization-item-title">' + name + '</span>').load().appendTo(vsItem);
  });
}

Application.prototype.renderBase64PNG = function(name, source) {
  $(function() {
    var vsSlider = $("#visualization-slider");
    var vsItem = $('<div class="visualization-item" id="visualization-item-' + name + '"></div>');
    vsItem.load().appendTo(vsSlider);
    $('<img src="data:image/png;base64,' + source + '" />').load().appendTo(vsItem);
    $('<span class="visualization-item-title">' + name + '</span>').load().appendTo(vsItem);
  });
}

plfdemo.Application = new Application();
plfdemo.Application.initPopup();