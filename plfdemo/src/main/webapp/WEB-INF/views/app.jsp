<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/default.css" />" />
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/app.css" />" />
  <script type="text/javascript" src="<c:url value="/resources/js/jquery-1.6.4.min.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/json2.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/jqconsole-2.7.min.js" />" charset="utf-8"></script>
  
  <script type="text/javascript">
    var ctx = "${pageContext.request.contextPath}"
    var jqconsole; 
    
    function startPrompt() {
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
    
    function executeApp() {
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
    }
  	
    function refresh() {
      $.ajax({
        url : "${app.name}/data",
        success : function(jsStr) {
          var json = JSON.parse(jsStr);
          var tables = json["tables"];
          var visualizations = json["visualizations"];
          var tableContainer = $("#table-data-container");
          tableContainer.empty();
          // for each table in dataset, render the data table
          var index = 0;
          for (index in tables) {
            $(document).ready(function() {
              renderTableData(tables[index]["name"], JSON.stringify(tables[index]["data"]));
            });
          }
          $("#table-count").text(tables.length);

          var vsContainer = $("#visualization-container");
          vsContainer.empty();
          // for each visualization in visualizations, append to the visualization panel
          for (index in visualizations) {
            renderVisualization(visualizations[index]);
          }
          $("#visualization-count").text(visualizations.length);
        },
        error : function(json) {
          console.debug("Error when trying to refresh data");
        }
      });
    }

    function showConsole() {
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

    function clearConsole() {
      jqconsole.Reset();
    }

    function saveCode() {
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

    function renderVisualization(name) {
      var vsContainer = $("#visualization-container");
      vsContainer.append("<div>" + name + "&nbsp; <input type=\"submit\" value=\"Delete\" onclick=\"deleteVisualization('" + name + "')\"" + "/></div>");
      $('<iframe class="visualization-frame" id="visualize-' + name + '" src="' + ctx + '/visualize/' + '${app.name}' + '/' + name + '">').load().appendTo(vsContainer);
    }

    function renderTableData(tableName, tableData) {
      var tableContainer = $("#table-data-container");
      tableContainer.append("<div><span>" + tableName + "</span> &nbsp; <input type=\"submit\" value=\"Delete\" onclick=\"deleteTable('" + tableName + "')\"" + "/> Data:<div> <span id=\"" + tableName + "-data\" style=\"font-size: 0.75em; color: Gray;\"></span></div></div>");
      $("#" + tableName + "-data").text(tableData);
    }

    function deleteVisualization(name) {
      $.ajax({
        url : "${app.name}" + "/visualize/" + name + "/delete",
        type : "post",
        success : function(result) {
          console.info("Delete visualization " + name + ": " + result);
        },
        error : function(result) {
          console.info("Delete visualization " + name + ": " + result);
        }
      });
      refresh();
    }

    function deleteTable(name) {
      $.ajax({
        url : "${app.name}" + "/data/" + name + "/delete",
        type : "post",
        success : function(result) {
          console.info("Delete table " + name + ": " + JSON.stringify(result));
        },
        error : function(result) {
          console.info("Delete table " + name + ": " + JSON.stringify(result));
        }
      });
      refresh();
    }
    
    function getConsoleCaret() {
      if ("${app.language}" == "python") return ">>>";
      else if ("${app.language}" == "r") return ">";
      else return null;
    }
  </script>
</head>
<body>
  <h2>This is the application page</h2>
  <div class="main-container">
    <div class="top-content">
      <h3>Summary info</h3>
      <table>
      <tr>
        <td><span class="info-label">Application name: </span></td>
        <td><span id="appname">${app.name}</span></td>
      </tr>
      <tr>
        <td><span class="info-label">Language: </span></td>
        <td><span>${app.language }</span></td>
      </tr>
      <tr>
        <td><span class="info-label">Create date: </span></td>
        <td></td>
      </tr>
      <tr>
        <td><span class="info-label">Description: </span></td>
        <td><span>${app.description }</span></td>
      </tr>
      
      </table>
    </div>
    <div class="main-content-container">
      <div class="left-content">
        <div style="clear:both;">
        
          <div style="width: 550px; display: block;">
            <div>  
              <span>Language: </span>
              <select id="language">
                <option value="python">Python</option>
                <option value="r">R</option>
              </select>
              <script type="text/javascript">
                $("#language").val("${app.language}");
                $("#language").attr('disabled', true);
              </script>
            </div>
              <textarea rows="20" cols="80" id="code" >${appcode}</textarea>
              <input id="execute" type="button" value="Execute" onclick="executeApp();">
              <input id="show-console" type="button" value="Show console" onclick="showConsole();" />
              <input id="clear-console" type="button" value="Clear console" style="display:none;" onclick="clearConsole();" />
              <input id="save" type="submit" value="Save code" onclick="saveCode();" />
              <img id="ajax-loading" width="20px" height="20px" src="<c:url value="/resources/images/ajax-loading.gif" />" style="display:none;"  />
              <span id="ajax-message" style="color: Green; font-style: italic;"></span>
            <div>
              <div id="console" class="prompt" style="display: none;"></div>
              <script type="text/javascript">
              	jqconsole = $("#console").jqconsole('Welcome to our console\n', getConsoleCaret());
              	startPrompt();
              </script>
              
            </div>
          </div>
          
        <!-- /form-->
        </div>
        
      </div>
    
      <div class="right-content">
        <div class="righ-content-upper">
          <h3 style="margin-top: 0px;">Datasets</h3> <span id="table-count"><c:out value="${fn:length(tables)}" /></span> tables:&nbsp; 
          <div id="table-data-container">
            <c:forEach items="${tables}" var="table">
              <script type="text/javascript">
                var tablename = '${table.name}'
              	$(function() {
              	  $.ajax({
              	    url: "${app.name}" + "/data/" + "${table.name}",
              	    success: function(json) { 
              	      $(document).ready(function() { renderTableData("${table.name}", json); });           	      
              	    }, 
              	    error: function() { $("#${table.name}-data").text("Failed to load data"); }
              	  });
              	});
              </script>
              
            </c:forEach>
          </div>
        </div>
        
        <div class="right-content-lower">
          <h3>Visualizations</h3> <span id="visualization-count"><c:out value="${fn:length(visualizations)}" /></span> visualizations
          <div id="visualization-container">
            <c:forEach items="${visualizations }" var="visualization">
              <script type="text/javascript">
                $(document).ready(function() {renderVisualization("${visualization.name}");});
              </script>
            </c:forEach>
          </div>
        </div>
      </div>
    </div>
  </div>
</body>
</html>