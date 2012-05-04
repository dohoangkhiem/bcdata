<html>
<head>
  <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
</head>
<body>
  
  <script type="text/javascript">
  	function execute() {
  	  $(function() {
  	    $.post($("#appname").text() + "/execute", {code: $("#code").val()}, function(output) { $("#console").show(); $("#console").val(output); });
  	  });
  	}
  </script>
  
  <h2>This is a application page</h2>
  <div>
    <div>
      Summary info
      <table>
      <tr>
        <td><span>Application name: </span></td>
        <td><span id="appname">${appname}</span></td>
      </tr>
      <tr>
        <td><span>Owner: </span></td>
        <td></td>
      </tr>
      <tr>
        <td><span>Create date: </span></td>
        <td></td>
      </tr>
      </table>
    </div>
    <div>
      <div style="clear:both;">
      <!-- form action="execute" method="post"-->
        <textarea rows="20" cols="80" id="code"></textarea>
        <input type="submit" value="Execute" onclick="execute();">
      <!-- /form-->
      </div>
      <textarea rows="20" cols="80" id="console" style="display:none; background-color: #000000; color: #FFFFFF;"></textarea>
    </div>
  </div>
  <div>
    Datasets
  </div>
  <div>
    Visualizations
  </div>
  
  
</body>
</html>