<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">  
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/app.css" />" />
  <script type="text/javascript" src="<c:url value="/resources/js/jqconsole-2.7.min.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/plfdemo/application.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/plfdemo/query.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/jquery.bxSlider.min.js" />"></script>
  
  <title>Platform Demo</title>
</head>
<body>      
  <script type="text/javascript">
  $(function() {
  	plfdemo.Application.initConsole('python');
  	$("#main-tabs").tabs();
  	$("#main-tabs").tabs("select", window.location.hash);
  	$("#result-tabs").tabs();
  }); 
  </script>
  <div class="main-tabs" id="main-tabs">
    <ul>
      <li><a href="#data">Play</a></li>
      <li><a href="#app">Application</a></li>
    </ul>
    <div class="data-container" id="data">
      <span>Data query console here.</span>
      <div class="data-query-container">
        <div class="query-console">
          <textarea rows="3" id="query-input" class="query-input code-editor"></textarea>
        </div>
        <div class="data-viewer">
          <span>Output format </span>
          <select id="data-viewer-select">
            <option value="table">Table</option>
            <option value="json">Json</option>
          </select>
          <div class="data-view-json">
            <span id="data-raw-view"></span>
          </div>
        </div>
      </div>
    </div>
    <div class="application-container" id="app">
      <div class="application-info">
        <div class="new-application">
          <div class="application-title">
            <h3>
              <c:choose>
                <c:when test='${app == null}'>New application</c:when>
                <c:otherwise>${app.name } </c:otherwise>
              </c:choose>
            </h3>
          </div>
          <c:if test="${app != null }">
            <div class="create-new-app">
              <input type="button" id="create-app-button" value="New application" onclick="window.location.href=plfdemo.Main.ctx + '/#app';" />
            </div>
          </c:if>
          <div class="language-select">  
            <span>Language: </span>
            <select id="language">
              <option value="python">Python</option>
              <option value="r">R</option>
            </select>
            <c:if test="${app != null}">
              <script type="text/javascript">
              	$('#language').val('${app.language}');
              </script>
            </c:if>
          </div>
        </div>
      </div>
      <div class="application-detail">        
        <div style="width: 100%; display: block;">
          <div class="app-actions"> 
            <input id="execute" type="button" value="Execute" onclick="plfdemo.Application.execute($('#code-editor').val(), $('#language').val(), '${app.name}');">     
            <input id="save" type="submit" value="Save" onclick="plfdemo.Application.saveCode('${app.name}', $('#code-editor').val(), $('#language').val());" />
            <img id="ajax-loading" width="20px" height="20px" src="<c:url value="/resources/images/ajax-loading.gif" />" style="display:none;"  />
            <span id="ajax-message" style="color: Green; font-style: italic;"></span>
          </div>
          <textarea rows="15" id="code-editor" class="code-editor"><c:if test="${app != null }">${appcode }</c:if></textarea>    
          
          <div style="clear:both;"></div>
        </div>
      </div>
      
      <div class="result-tabs" id="result-tabs">
        <ul>
          <li><a href="#console-panel">Console</a></li>
          <li><a href="#visualization-panel">Visualization</a></li>
          <li><a href="#datastore-panel">Datastore</a></li>
        </ul>
        <div id="console-panel">      
          <div id="console" class="prompt" style="display: block;"></div>
          <div class="console-actions">    
            <input id="clear-console" type="button" value="Clear console" onclick="plfdemo.Application.clearConsole();" />
          </div>
        </div>
        <div class="visualization-panel" id="visualization-panel">
          <h3>Visualization</h3>
          <div id="visualization-slider" class="visualization-slider">
            <c:forEach items="${visualizations }" var="visualization">
              <script>
                plfdemo.Application.renderVisualization("${visualization.name}", "${app.name}");
              </script>
            </c:forEach>
          </div>
          <!-- script>
            $(function() {
              $('#visualization-slider').bxSlider();
            });
          </script-->
        </div>
      
        <div class="datastore-panel" id="datastore-panel">
          <h3>Datastore</h3>
        </div>
      </div>
      
      <div class="popup save-app-dialog" id="save-app-dialog" title="Save your application">
        <form>
          <fieldset>
            <label>Application name</label>
            <input type="text" id="new-app-name" maxlength="255"></input>
            <label>Description</label>
            <textarea rows="3" id="new-app-description" style="resize: none;"></textarea>
          </fieldset>
        </form>
      </div>
    </div>
  </div>
  
</body>
</html>
