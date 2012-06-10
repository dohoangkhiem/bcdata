<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script type="text/javascript" src="<c:url value="/resources/js/jqconsole-2.7.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/js/jquery.bxSlider.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/js/plfdemo/workspace.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/js/plfdemo/ide.js" />"></script>

<div class="workspace-container">
  <div class="workspace-layout" id="workspace-layout">
    <div class="workspace-layout-center workspace-main-panel">
      <div class="app-editor-tabs" id="app-editor-tabs">
        <ul class="editor-tabs-bar">
          <!-- li class="tab-header editor-tab-header"><a href="#untitled1">Untitled1</a></li-->
        </ul>     
        <div class="app-actions">
          <img id="ajax-loading" width="20px" height="20px" src="<c:url value="/resources/images/ajax-loading.gif" />" style="display:none;"  />
          <span id="ajax-message" style="color: Green; font-style: italic;"></span>
          <input type="button" value="Clone" class="app-action" id="copy-app" />
          <input type="button" value="Save" class="app-action" id="save-app" />
          <input type="button" value="Run" class="app-action" id="run-app"  />
        </div>
        <!-- div class="app-code-editor">
            <textarea rows="15" id="code-editor" class="code-editor" spellcheck='false'></textarea>
        </div-->
        <!-- div id="untitled1" class="app-editor-item">
          <div class="app-info">
            <div class="app-title">
              <span>New application</span>
            </div>  
            <div class="language-select">  
              <span>Language: </span>
              <select id="app-language">
                <option value="python">Python</option>
                <option value="r">R</option>
              </select>
            </div>
          </div>
          <div class="app-code-editor">
            <textarea rows="15" id="code-editor" class="code-editor" spellcheck='false'></textarea>
          </div>        
        </div-->
      </div>
      
      <div class="workspace-info-tabs" id="workspace-info-tabs">
        <ul>
          <li><a href="#workspace-output">Output</a></li>
          <li><a href="#workspace-variables">Variables</a></li>
        </ul>  
        <div id="workspace-output">
          <div id="console" class="prompt" style="display: block;"></div>
          <div class="console-actions">    
            <input id="clear-console" type="button" value="Clear console" onclick="plfdemo.Workspace.clearConsole();" />
          </div>
        </div>
        <div id="workspace-variables">
          Workspace variables
        </div>
      </div>
    </div>
    <div class="workspace-layout-east workspace-side-panel">
      <div class="app-output-tabs" id="app-output-tabs">
        <ul>
          <li><a href="#app-visualizations">Visualizations</a></li>
          <li><a href="#app-datasets">Datasets</a></li>
        </ul>
        <div id="app-visualizations">
          Visualizations here
          <div id="visualization-slider"></div>
        </div>
        <div id="app-datasets">
          Datasets here
        </div>
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
          <input type="text" id="new-app-tags"></input><br>
        </fieldset>
      </form>
    </div>
  </div>
</div>