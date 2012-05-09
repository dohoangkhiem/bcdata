<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/default.css" />" />
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/app.css" />" />
  <script type="text/javascript" src="<c:url value="/resources/js/jquery-1.6.4.min.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/json2.js" />"></script>
  
  <script type="text/javascript">
  	ctx = "${pageContext.request.contextPath}"

    function executeApp() {
  	  $("#ajax-loading").css("display", "inline");
  	  $("#ajax-message").text("Running...");
      if (!$("#console").is(":visible")) {
        $("#console").show();
        $("#clear-console").show();
        $("#show-console").val("Hide console");
      }
      $(function() {
        $.post($("#appname").text() + "/execute", {code: $("#code").val()}, function(output) { $("#console").show(); $("#console").append(output); $("#ajax-loading").css("display", "none");
    	  $("#ajax-message").text("Finished running.");});
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
      $("#console").text("");
    }
    
    function saveCode() {
      $("#ajax-loading").css("display", "inline");
  	  $("#ajax-message").text("Saving...");
    	$.ajax({
    	  url: "${app.name}" + "/save",
    	  data: { code: $("#code").val() },
    	  success: function(json) {
    	    console.info("Update code: " + JSON.stringify(json));
    	    $("#ajax-loading").css("display", "none");
      	  $("#ajax-message").text("Updated!");
    	  }, 
    	  error: function(json) {
    	    console.info("Update code: " + JSON.stringify(json));
    	    $("#ajax-loading").css("display", "none");
      	  $("#ajax-message").text("Update code: Failed.");
    	  }, 
    	  type: "post"
    	});  
    }
    
    function renderVisualization(name) {
      var vsContainer = $("#visualization-container");
      // get visualization content
      $.ajax({
        url: "${app.name}" + "/visualize/" + name,
        success: function(content) {
          vsContainer.append("<div>" + name + "&nbsp; <input type=\"submit\" value=\"Delete\" onclick=\"deleteVisualization('" + "${app.name}" + "','" + name + "')\"" + "/></div>");
          $('<iframe class="visualization-frame" id="visualize-' + name + '" src="' + ctx + '/visualize/' + '${app.name}' + '/' + name + '">').load().appendTo(vsContainer);
        },
        error: function(json) {
          $('<span>Failed to load visualization ' + name + '</span>').appendTo(vsContainer);
        }
      });
    }
    
    function deleteVisualization(appname, name) {
      $.ajax({
        url: "${app.name}" + "/visualize/" + name + "/delete",
        type: "post",
        success: function(result) {
          console.info("Delete visualization " + name + ": " + result);
        },
        error: function(result) {
          console.info("Delete visualization " + name + ": " + result);
        }         
      });
      location.reload();
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
              <textarea rows="20" cols="80" id="code" >${appcode}</textarea>
              <input id="execute" type="button" value="Execute" onclick="executeApp();">
              <input id="show-console" type="button" value="Show console" onclick="showConsole();" />
              <input id="save" type="submit" value="Save code" onclick="saveCode();" />
              <img id="ajax-loading" width="20px" height="20px" src="<c:url value="/resources/images/ajax-loading.gif" />" style="display:none;"  />
              <span id="ajax-message" style="color: Green; font-style: italic;"></span>
            <div>
              <textarea rows="15" cols="80" id="console" style="display:none; background-color: #000000; color: #FFFFFF;"></textarea>
              <input id="clear-console" type="button" value="Clear console" style="display:none;" onclick="clearConsole();" />
            </div>
          </div>
          
        <!-- /form-->
        </div>
        
      </div>
    
      <div class="right-content">
        <div class="righ-content-upper">
          <h3 style="margin-top: 0px;">Datasets</h3> <c:out value="${fn:length(tables)}" /> tables:&nbsp; 
          <c:forEach items="${tables}" var="table">
            <div>
              <span>${table.name }</span> &nbsp; Data:
              <div> 
                <span id="${table.name}-data" style="font-size: 0.75em; color: Gray;"></span>
              </div>
            </div>
            <script type="text/javascript">
              var tablename = '${table.name}'
            	$(function() {
            	  $.ajax({
            	    url: "${app.name}" + "/data/" + "${table.name}",
            	    success: function(json) { 
            	      $("#${table.name}-data").text(json)
            	     // $("#${table.name}-data").replaceWith('<span id="${table.name}-data">' + JSON.stringify(json) + '</span>');
            	    }, 
            	    error: function() { $("#${table.name}-data").text("Failed to load data"); }
            	  });
            	});
            </script>
            
          </c:forEach>
        </div>
        
        <div class="right-content-lower">
          <h3>Visualizations</h3> <c:out value="${fn:length(visualizations)}" /> visualizations
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