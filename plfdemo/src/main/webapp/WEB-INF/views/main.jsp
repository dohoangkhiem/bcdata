<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  
  <!-- script type="text/javascript" src="<script type="text/javascript" src="<c:url value="/resources/js/jquery-1.6.4.min.js" />"></script>"></script-->
  <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
  
  <title>Platform Demo</title>
</head>
<body>
  <h2>Message: ${message}</h2>
  
  <div class="main-content">
    List of Dataset: <span id="datasets"></span>
  </div>
  
  <script type="text/javascript">
  	function getDataset() {
  	  $(function() {
  	    $.ajax({ url: "main/dataset", dataType: "json", success: function(json) {
  	      $("#datasets").replaceWith('<span id="datasets">' + json[0]['name'] + '</span>');  
  	    }, error: function() { alert("error"); }   
  	    }); 
  	  });
  	}
  	getDataset();
  </script>
</body>
</html>
