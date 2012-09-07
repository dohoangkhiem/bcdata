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
        <input type="text" class="search-input" id="query" name="query" placeholder="Search global stuff..." />
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
          <div class="analysis-list-panel">
            <h4 style="cursor: pointer;">Analyses</h4>
            <div id="analysis-list"></div>
          </div>
          <div class="browser-separator"></div>
          <div class="scraper-list-panel">
            <h4 style="cursor: pointer;">Scrapers</h4>
            <div id="scraper-list"></div>
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
      <div class="workbench-toolbar">
        <span id="app-actions-toolbar" class="app-actions ui-widget-header ui-corner-all">
          <button class="app-action" id="new-app">New</button>
          <button class="app-action" id="copy-app">Clone</button>
          <button class="app-action" id="save-app">Save</button>
          <button class="app-action" id="run-app">Run</button>
          <button class="app-action" id="publish-app">Publish</button>
          <button class="app-action" id="view-app">View</button>
          <button class="app-action" id="upload-data">Upload</button>
        </span>
      </div>
      <!-- workbench main tabs layout -->
      <div class="workbench-main-tabs" id="workbench-main-tabs">
        <ul class="workbench-main-tabs-bar">
        </ul>
      </div> 
      
      <div class="popup new-app-dialog" id="new-app-dialog" title="Save your application">
        <form>
          <fieldset>
            <label for="new-app-name">Analysis name</label>
            <input type="text" id="new-app-name" maxlength="40"></input><br>
            <label for="new-app-language">Language</label>
            <select id="new-app-language">
              <option value="python">Python</option>
              <option value="r">R</option>
            </select> <br>
            <label for="new-app-description">Description</label>
            <textarea rows="3" id="new-app-description" style="resize: none;"></textarea><br>
            <label for="new-app-public">Auto publish?</label>
            <select id="new-app-public">
              <option value="1">Yes</option>
              <option value="0">No</option>
            </select><br>
            <label for="new-app-tags">Tags</label>
            <input type="text" id="new-app-tags" title="Separate tags by comma"></input><br>
          </fieldset>
        </form>
      </div>
      <div class="popup publish-dialog" id="publish-dialog" title="Publish your analysis">
        <form>
          <fieldset>
            <label for="anls-name">Analysis</label>
            <span id="anls-name"></span><br>
            <label for="publish-msg">Message</label>
            <textarea id="publish-msg" rows="3"></textarea>
          </fieldset>
        </form>
      </div>
      <div class="popup upload-data-dialog" id="upload-data-dialog" title="Upload data">
        <form id="file-upload-form" method="POST" enctype="multipart/form-data">
          <fieldset>
            <label>Select your file</label> &nbsp;
            <input id="file" name="file" type="file" /> &nbsp;
          </fieldset>
          <img alt="Uploading" src="<c:url value="/resources/images/loader32.gif" />" class="upload-in-progress">
          <span class="upload-status"></span>
        </form>
      </div>
      
      <div class="popup new-dialog" id="new-dialog" title="Create new script">
        <ul class="select-type">
          <li class="script-type" script-type="analysis"><a href="#">Analysis</a></li>
          <li class="script-type" script-type="scraper"><a href="#">Scraper</a></li>
        </ul>
        <ul class="select-language">
          <li class="script-language" lang="python"><a href="#">Python</a></li>
          <li class="script-language" lang="r"><a href="#">R</a></li>
        </ul>
      </div>
    </div> 
  </div>
</div>