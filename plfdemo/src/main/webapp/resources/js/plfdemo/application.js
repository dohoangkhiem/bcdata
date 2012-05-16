function Application(appname, language, description, code) {
}

Application.prototype.execute = function() {
  $("#ajax-loading").css("display", "inline");
  $("#ajax-message").text("Running...");
  if (!$("#console").is(":visible")) {
    $("#console").show();
    $("#clear-console").show();
    $("#show-console").val("Hide console");
  }
  $(function() {
    $.post($("#appname").text() + "/execute", 
        { code: $("#code").val(), language: $("#language").val() }, 
        function(output) { 
          $("#console").show(); 
          //$("#console").val($("#console").val() + output + "\n");
          jqconsole.Write(output + '\n', 'jqconsole-output');
          startPrompt();
          $("#ajax-loading").css("display", "none");
          $("#ajax-message").text("Finished running."); refresh(); 
        }
     );
  });
};


Application.prototype.startPrompt = function() {
  jqconsole.Prompt(true, function(input) {
    $.ajax({
      url: "${app.name}/execute",
      type: "get",
      data: {
        code: input,
        language: $("#language").val()
      },
      success: function(result) {
        jqconsole.Write(result + '\n', 'jqconsole-output');
        startPrompt();
      },
      error: function(result) {
        console.info(output);
        startPrompt();
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
  jqconsole.Reset();
  startPrompt();
}

Application.prototype.saveCode = function() {
  $("#ajax-loading").css("display", "inline");
  $("#ajax-message").text("Saving...");
  $.ajax({
    url : "${app.name}" + "/save",
    data : {
      code : $("#code").val()
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

Application.prototype.getConsoleCaret = function() {
  if ("${app.language}" == "python") return ">>>";
  else if ("${app.language}" == "r") return ">";
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
    $.ajax({ url: "main/createApp", data: data, type: "post", dataType: "json", success: function(json) {
      alert("Success");
      window.location.href=ctx = "/app/" + appname;
    }, error: function() { alert("Failed"); } });  
  });
}
