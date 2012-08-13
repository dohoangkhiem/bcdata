<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript">
  if (!com.bouncingdata.Workbench) {
    $.getScript(ctx + "/resources/js/bouncingdata/workbench.js", function() {
    	console.debug("workbench.js async. loaded!");
    	com.bouncingdata.Workbench.init();
    });  
  } else {
    com.bouncingdata.Workbench.init();
  }
  
  if (!com.bouncingdata.Browser) {
    $.getScript(ctx + "/resources/js/bouncingdata/browser.js", function() {
    	console.debug("browser.js async. loaded!");
    	com.bouncingdata.Browser.init();
    });  
  } else {
    com.bouncingdata.Browser.init();
  }
  
</script>

<div id="main-content" class="workbench-container">
  <div class="workbench-browser right-content" id="workbench-browser">
    <div class="search-container">
      <div id="search-form" class="search-form">
        <input type="text" class="search-input" id="query" name="query" placeholder="Search your stuff..." />
        <!-- input type="submit" value="Search" id="search-submit" /-->
      </div>
      <div class="clear"></div>
    </div>
    <div class="browser-container">
      <div class="browser-tabs" id="browser-tabs">
        <ul>
          <li><a href="#browser-mystuff">My Stuff</a></li>
        </ul>
        <div id="browser-mystuff">
          <div class="dataset-list-panel">
            <h4 style="cursor: pointer;">Datasets</h4>
            <div id="dataset-list">
            </div>
          </div>
          <div class="browser-separator"></div>
          <div class="application-list-panel">
            <h4 style="cursor: pointer;">Applications</h4>
            <div id="application-list"></div>
          </div>
          <div class="show-all"><a id="show-all-button" href="javascript:void(0)">Back</a></div>
          <div class="clear"></div>
        </div>
        
      </div>
    </div>
  </div>
  <div class="workbench-ide center-content">  
    <div class="center-content-wrapper">
      <div class="top-rule"></div>
      <!-- workbench main tabs layout -->
      <div class="workbench-main-tabs" id="workbench-main-tabs">
        <ul class="workbench-main-tabs-bar">
        </ul>
        <div class="app-actions">
          <input type="button" value="Clone" class="app-action" id="copy-app" />
          <input type="button" value="Save" class="app-action" id="save-app" />
          <input type="button" value="Run" class="app-action" id="run-app"  />
        </div>
      </div> 
      
      <div class="popup new-app-dialog" id="new-app-dialog" title="Save your application">
        <form>
          <fieldset>
            <label for="new-app-name">Application name</label>
            <input type="text" id="new-app-name" maxlength="40"></input><br>
            <label for="new-app-language">Language</label>
            <select id="new-app-language">
              <option value="python">Python</option>
              <option value="r">R</option>
            </select> <br>
            <label for="new-app-description">Description</label>
            <textarea rows="3" id="new-app-description" style="resize: none;"></textarea><br>
            <label for="new-app-public">Published this application?</label>
            <select id="new-app-public">
              <option value="1">Public</option>
              <option value="0">Private</option>
            </select><br>
            <label for="new-app-tags">Tags</label>
            <input type="text" id="new-app-tags" title="Separate tags by comma"></input><br>
          </fieldset>
        </form>
      </div>
    </div> 
  </div>
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
      
<!-- workbench templates -->
<script id="workbench-content-template" type="text/x-jquery-tmpl">
  <div class="workbench-ide-content">
    <div class="app-status">
      <img id="ajax-loading" width="20px" height="20px" src="<c:url value="/resources/images/ajax-loader.gif" />" style="opacity: 0;"  />
      <span id="ajax-message" style="color: Green; font-style: italic;"></span>
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
    <div class="app-code-container">
      <div class="app-code-layout">
        <div class="app-code-layout-center app-code-editor" id="app-code-layout-center-\${tabId }">
          <!-- textarea rows='15' id='code-editor-\${tabId}' class='code-editor' spellcheck='false'></textarea-->
          <div>
            <div id="code-editor-\${tabId}" class="code-editor"></div>
          </div>    
        </div>
        <div class="app-code-layout-east app-execution-logs" id="app-code-layout-east-\${tabId }">
          <div id="app-execution-logs-\${tabId}">
            <div class="console prompt" style="display: block;"></div>
            <div class="console-actions">
              <input class="clear-console" type="button" value="Clear console" />
            </div>
          </div>
        </div>
      </div>            
    </div>
    
    <div class="app-output-tabs">
      <ul>
        <li><a href="#app-output-viz-\${tabId}">Visualization</a></li>
        <li><a href="#app-output-code-\${tabId}">Code</a></li>
        <li><a href="#app-output-data-\${tabId}">Data</a></li>
      </ul>
      <div id="app-output-viz-\${tabId}" class="app-output-viz">
        <div><strong>Visualization Dashboard.</strong></div><br />
        <div class="app-output-actions">
          <input class="dashboard-preview" type="button" value="Preview" />
          <input class="dashboard-publish" type="button" value="Publish" />
        </div>
        <div class="app-output-viz-dashboard">
          <div class="dashboard-ruler">
            <div class="dashboard-ruler-left ruler"></div>
            <div class="dashboard-ruler-top ruler"></div>
            <div class="dashboard-ruler-right ruler"></div>
            <div class="dashboard-ruler-bottom ruler"></div>
            <div class="snap-line-left snap-line"></div>
            <div class="snap-line-top snap-line"></div>
            <div class="snap-line-right snap-line"></div>
            <div class="snap-line-bottom snap-line"></div>
          </div>
          <div class="viz-dashboard" id="viz-dashboard-\${tabId}"></div>
        </div>
      </div>
      <div id="app-output-code-\${tabId}" class="app-output-code">
        <div class="code-block"></div>
      </div>
      <div id="app-output-data-\${tabId}" class="app-output-data">
        <div>App. Data</div>
      </div>
    </div>
  </div>
</script>