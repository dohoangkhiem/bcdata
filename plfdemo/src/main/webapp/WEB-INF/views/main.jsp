<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">  
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/main.css" />" />
  <script type="text/javascript" src="<c:url value="/resources/js/plfdemo/application.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/jqconsole-2.7.min.js" />"></script>
  
  <title>Platform Demo</title>
</head>
<body>      
  <script type="text/javascript">
  $(function() {
  	jqconsole = $("#console").jqconsole('Welcome to our console\n', '>>>');
  	//plfdemo.Application = new Application();
  }); 
  </script>
  <div class="application-container">
    <div class="application-info">
      <div class="new-application">
        <h3>New Application</h3>
        <div class="language-select">  
          <span>Language: </span>
          <select id="language">
            <option value="python">Python</option>
            <option value="r">R</option>
          </select>
        </div>
      </div>
      <!-- div class="application-summary">
        <h3>Application summary</h3>
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
            <td><span class="info-label">Description: </span></td>
            <td><span>${app.description }</span></td>
          </tr>   
        </table>
      </div-->
    </div>
    <div class="application-detail">        
      <div style="width: 550px; display: block;"> 
        <input id="execute" type="button" value="Execute" onclick="executeApp();">     
        <input id="save" type="submit" value="Save code" onclick="saveCode();" />
        <img id="ajax-loading" width="20px" height="20px" src="<c:url value="/resources/images/ajax-loading.gif" />" style="display:none;"  />
        <span id="ajax-message" style="color: Green; font-style: italic;"></span>
        <textarea rows="20" cols="80" id="code-editor" >${appcode}</textarea>        
        <input id="show-console" type="button" value="Show console" onclick="showConsole();" />
        <input id="clear-console" type="button" value="Clear console" style="display:none;" onclick="clearConsole();" />      
        <div>
          <div id="console" class="prompt" style="display: none;"></div>
        </div>        
      </div>
    </div>
  </div>
  
</body>
</html>
