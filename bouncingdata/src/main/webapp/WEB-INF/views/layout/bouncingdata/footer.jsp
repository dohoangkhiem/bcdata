<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="footer-horizontal-rule"></div>
<div class="footer-content">
  <span style="font-weight: bold; text-align: center;">Copyright &copy;2012 bouncingdata.com. All rights reserved.</span>
</div>
<!-- Dataset view template  -->
      <script id="data-view-template" type="text/x-jquery-tmpl">
        <div class="dataset-view-container">
          <div class="dataset-view-layout" id="dataset-view-layout-\${tabId}">
            <div class="dataset-view-center" id="dataset-view-center-\${tabId}">
              <div class="dataset-view-main">
                <div class="dataset-view">
                  <table class="dataset-table"></table>
                </div>
                <div class="dataset-query">
                  <div class="dataset-query-editor">
                    <textarea rows="5" class="code-editor query-editor" spellcheck="false"></textarea>
                  </div>
                  <div class="dataset-query-actions">
                    <img id="ajax-loading" width="20px" height="20px" src="<c:url value="/resources/images/ajax-loading.gif" />" style="display:none;"  />
                    <span id="ajax-message" style="color: Green; font-style: italic;"></span>
                    <input class="dataset-query-execute" type="button" value="Execute" />
                  </div>
                </div>
              </div>
            </div> 
            <div class="dataset-view-east" id="dataset-view-east-\${tabId}">
              <div class="dataset-view-side dataset-view-info">
                <p>
                  <strong>Dataset: </strong>
                  <span>\${dsName }</span>
                </p>
                <p>
                  <strong>Author: </strong>
                  <span>\${dsAuthor }</span>
                </p>
                <p>
                  <strong>Schema: </strong>
                  <span>\${dsSchema }</span>
                </p>
                <p>
                  <strong>Row count: </strong>
                  <span>\${dsRowCount }</span>
                </p>
                <p>
                  <strong>Create date: </strong>
                  <span>\${dsCreateDate }</span>
                </p>
                <p>
                  <strong>Last update: </strong>
                  <span>\${dsLastUpdate }</span>
                </p>
                <p>
                  <strong>Tags: </strong>
                  <span>\${dsTags }</span>
                </p>
              </div>
            </div>
          </div>
          
        </div>
      </script>
      
      <!-- Define the workspace templates -->
      <script id="workspace-content-template" type="text/x-jquery-tmpl">
        <div class="workspace-content-container">
          <div class="workspace-content-layout" id="workspace-content-layout-\${tabId}">
            <div class="workspace-content-center" id="workspace-content-center-\${tabId}">
              <div class="workspace-content-main">
                <div class="app-actions">
                  <img id="ajax-loading" width="20px" height="20px" src="<c:url value="/resources/images/ajax-loading.gif" />" style="display:none;"  />
                  <span id="ajax-message" style="color: Green; font-style: italic;"></span>
                  <input type="button" value="Clone" class="app-action" id="copy-app" />
                  <input type="button" value="Save" class="app-action" id="save-app" />
                  <input type="button" value="Run" class="app-action" id="run-app"  />
                </div>
                <div class="app-info">
                  <div class='app-title'><label style='font-weight: bold;'>Application name: </label>\${appName}</div>
                  <div class='app-language' id='app-language'><label style='font-weight: bold;'>Language: </label>\${appLang}</div>
                  <div class='app-author'><label style='font-weight: bold;'>Author: </label>\${appAuthor}</div>
                </div>
                <div class="new-app-info">
                  <strong>Language: </strong>
                  <select class="language-select">
                    <option value='python'>Python</option>
                    <option value='r'>R</option>
                  </select>
                </div>
                <div class="clear"></div>
                <div class="app-code-editor">
                  <textarea rows='15' id='code-editor-\${tabId}' class='code-editor' spellcheck='false'></textarea>
                </div>
  
                <div class="app-execution-logs-tabs">
                  <ul>
                    <li><a href="#app-execution-logs-\${tabId}">Logs</a></li>
                    <li><a href="#app-execution-variables-\${tabId}">Variables</a></li>
                  </ul>  
                  <div id="app-execution-logs-\${tabId}" class="app-execution-logs">
                    <div class="console prompt" style="display: block;"></div>
                    <div class="console-actions">    
                      <input class="clear-console" type="button" value="Clear console" />
                    </div>
                  </div>
                  <div id="app-execution-variables-\${tabId}" class="app-execution-variables">
                    Application variables.
                  </div>
                  
                </div>
              </div>
            </div>
            <div class="workspace-content-east" id="workspace-content-east-\${tabId}">
              <div class="workspace-content-side">
                <div class="app-output-tabs">
                  <ul>
                    <li><a href="#app-output-visuals-\${tabId}">Visualizations</a></li>
                    <li><a href="#app-output-datasets-\${tabId}">Datasets</a></li>
                  </ul> 
                  <div id="app-output-visuals-\${tabId}" class="app-output-visuals">
                    <h4>Visualizations</h4>
                    <div class="visuals-container"></div>
                  </div>
                  <div id="app-output-datasets-\${tabId}" class="app-output-datasets">
                    <h4>Datasets</h4>
                    <div class="dataset-container"></div>
                  </div>  
                </div>
              </div>
            </div>
          </div>
          
        </div>
        
      </script>