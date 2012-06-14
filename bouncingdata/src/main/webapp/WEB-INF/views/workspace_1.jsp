<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script type="text/javascript" src="<c:url value="/resources/js/jqconsole-2.7.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/js/jquery.bxSlider.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/js/plfdemo/workspace.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/js/plfdemo/ide.js" />"></script>

<div class="workspace-container">  
    <!-- Workspace main tabs layout -->
    <div class="workspace-main-tabs">
      <ul class="workspace-main-tabs-bar">
      </ul>
      
      <!-- Define the workspace templates -->
      
      <!-- Workspace tab header template -->
      <script id="workspace-tab-template" type="text/x-jquery-tmpl">
				<li class="tab-header workspace-tab-header"><a href="#${tabId}">${tabTitle}</a></li>
			</script>
    
      <!-- script id="workspace-content-template" type="text/x-jquery-tmpl"-->
      	<div id="${tabId}" class="workspace-content">
          <div class="workspace-content-layout">
  					<div class="workspace-content-center">
  						<div class="workspace-content-main">
  							<div class="app-info">
  					      <div class='app-title'><label style='font-weight: bold;'>Application name: </label>${appName}</div>
                  <div class='app-language' id='app-language'><label style='font-weight: bold;'>Language: </label>${appLang}</div>
                  <div class='app-author'><label style='font-weight: bold;'>Author: </label>${appAuthor}</div>
  							</div>
          			<div class="app-actions">
                  <img id="ajax-loading" width="20px" height="20px" src="<c:url value="/resources/images/ajax-loading.gif" />" style="display:none;"  />
                  <span id="ajax-message" style="color: Green; font-style: italic;"></span>
                  <input type="button" value="Clone" class="app-action" id="copy-app" />
                  <input type="button" value="Save" class="app-action" id="save-app" />
                  <input type="button" value="Run" class="app-action" id="run-app"  />
          			</div>
          			<div class="app-editor">
                  <div class="app-code-editor">
                    <textarea rows='15' id='code-editor' class='code-editor' spellcheck='false'>${appCode}</textarea>
                  </div>
  							</div>
  
   							<div class="app-output-logs-tabs">
                  <ul>
                    <li><a href="#app-output-logs-${guid}">Logs</a></li>
                    <li><a href="#app-output-variables-${guid}">Variables</a></li>
                  </ul>  
                  <div id="app-output-logs-${guid}">
                    <div class="console prompt" style="display: block;"></div>
                    <div class="console-actions">    
                      <input class="clear-console" type="button" value="Clear console" onclick="plfdemo.Workspace.clearConsole();" />
                    </div>
                  </div>
                  <div id="app-output-variables-${guid}">
                    Application variables.
                  </div>
                  
          			</div>
  						</div>
  					</div>
  					<div class="workspace-content-east">
  						<div class="workspace-content-side">
  							<div class="app-output-result-tabs">
  			          <ul>
                    <li><a href="#app-output-visuals-${guid}">Visualizations</a></li>
                    <li><a href="#app-output-datasets-${guid}">Datasets</a></li>
                  </ul> 
                  <div id="#app-output-visuals-${guid}">
                    <h4>Visualizations</h4>
                  </div>
                  <div id="#app-output-datasets-${guid}">
                    <h4>Datasets</h4>
                  </div>  
  							</div>
  						</div>
  					</div>
  				</div>
          
        </div>
        
      <!-- script-->
    </div>  
</div>