<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  
  <!-- link rel="stylesheet" type="text/css" href=""-->
  <!-- script type="text/javascript" src="<script type="text/javascript" src="<c:url value="/resources/js/jquery-1.6.4.min.js" />"></script>"></script-->
  <script type="text/javascript" src="<c:url value="/resources/js/jquery-1.6.4.min.js" />"></script>
  
  <title>Platform Demo</title>
</head>
<body style="margin: 0 auto; width: 1000px;">  
  <div class="main-content">
    <h3>List of Dataset:</h3>
    <div>
      <ul id="datasetList"></ul>
    </div>
  </div>
  
  <script type="text/javascript">
    ctx = "${pageContext.request.contextPath}"
  	function getDataset() {
  	  $(function() {
  	    $.ajax({ url: "main/dataset", dataType: "json", success: function(json) {
  	      //$("#datasets").replaceWith('<span id="datasets">' + json[0]['name'] + '</span>');
  	      var i;
  	      for (i = 0; i < json.length; i++) {
	      	  var dataset = json[i];
	      	  $("#datasetList").append('<li><a href="' + ctx + '/dataset/gui"><span>' + dataset['name'] + '</span></a></li>');
	      	} 
  	    }, error: function() { alert("error"); }   
  	    }); 
  	  });
  	}
  	getDataset();
  	
  	function createApp() {
  	  var data = { 
  	  	appname: $("#appname").val(), 
  	    description: $("#description").val(),
  	   	code: $("#code").val()
  	  };
  	  $(function() {
  	  	$.ajax({ url: "main/createApp", data: data, type: "post", dataType: "json", success: function(json) {
  	  	  alert("Success");
  	  	}, error: function() { alert("Failed"); } });  
  	  });
  	}
  	
  	function getApplicationList() {
  	  $(function() {
  	    $.ajax({
  	      url: "<c:url value='main/application' />", dataType: "json", success: function(json) {
  	        var i = 0;
  	      	for (i = 0; i < json.length; i++) {
  	      	  var app = json[i];
  	      	  $("#appList").append('<li><a href="app/' + app['name'] + '"><span>' + app['name'] + '</span></a></li>');
  	      	}  
  	      }, error: function() {}
  	    });
  	  });
  	}
  	getApplicationList();
  </script>
  <div>
    <h3>List of application</h3>
    <ul id="appList"></ul>
  </div>
  <div id="createForm">
    <h3>Create new application</h3>
    <table>
      <tr>
        <td><span>Application name</span></td>
        <td><input type="text" id="appname" /></td>
      </tr>
      <tr>
        <td><span>Description</span></td>
        <td><textarea id="description" cols="60" rows="5"></textarea></td>
      </tr>
      <tr>
        <td><span>Code</span></td>
        <td><textarea id="code" cols="60" rows="15"></textarea></td>
      </tr>
      <tr>
        <td colspan="2">
          <input type="submit" value="Submit" onclick="createApp();" /> 
        </td>
      </tr>
    </table>
  </div>
</body>
</html>
