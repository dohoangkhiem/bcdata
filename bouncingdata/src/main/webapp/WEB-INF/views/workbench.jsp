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
          <div class="application-list-panel">
            <h4 style="cursor: pointer;">Analyses</h4>
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
            <label for="new-app-name">Analysis name</label>
            <input type="text" id="new-app-name" maxlength="40"></input><br>
            <label for="new-app-language">Language</label>
            <select id="new-app-language">
              <option value="python">Python</option>
              <option value="r">R</option>
            </select> <br>
            <label for="new-app-description">Description</label>
            <textarea rows="3" id="new-app-description" style="resize: none;"></textarea><br>
            <label for="new-app-public">Publish this analysis?</label>
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