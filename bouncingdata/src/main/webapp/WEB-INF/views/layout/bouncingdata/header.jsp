<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="header-content">
  <div style="float: right; margin-right: 10px; text-align: right;">
    <sec:authorize access="isAuthenticated()">
      <div>Welcome back <span style="font-weight: bold;"> 
          <sec:authentication property="principal.username" />
        </span>
        <a style="color: blue;" href="<c:url value="/auth/j_spring_security_logout" />"> Logout</a>
      </div>
    </sec:authorize>
  </div>
  
  <h2 style="margin: 0 0 0 10px; float: left; display: inline;">
    <a href="#">Bouncing Data</a>
  </h2>
  <div class="search-container">
    <div id="search-form" class="search-form">
      <input type="text" class="search-input" id="query" name="query" />
      <input type="submit" value="Search" id="search-submit" />
    </div>
    <div class="clear"></div>
  </div>
</div>
<jqtemplate>
  <!-- Comment templates -->
  <script id="comment-template" type="text/x-jquery-tmpl">
    <li class="comment-item" id="comment-\${id}" nodeid="\${id}">
      <div class="comment-item-body">
        <div class="comment-header">
          <div class="comment-author">\${username}</div>
        </div>
        <div class="comment-message">\${message}</div>
        <div class="comment-footer">
          <span class="comment-date">\${date}</span>&nbsp;
          <strong><span class="comment-score">\${upVote - downVote}</span>&nbsp;</strong>
          <!--span class="up-vote">\${upVote}</span>&nbsp;-->
          <a class="up-vote-link" href="#"><span class="up-vote-icon">Vote up</span></a>&nbsp;
          <!--span class="down-vote">\${downVote}</span>&nbsp;-->
          <a class="down-vote-link" href="#"><span class="down-vote-icon">Vote down</span></a>&nbsp;
          <a class="comment-reply" href="#">Reply</a>
        </div>
      </div>
      <ul class="children"></ul>
    </li>
	</script>
  <script id="comment-editor-template" type="text/x-jquery-tmpl">
    <div class="comment-editor inline-editor">
      <textarea class="reply-text" rows="\${rows}"></textarea>
      <input class="reply-button" type="button" value="Reply" />
      <div class="clear"></div>
    </div>
	</script>
  
  <!-- Dataset view templates  -->
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
          <li><a href="#app-output-data-\${tabId}">Data</a></li>
          <li><a href="#app-output-code-\${tabId}">Code</a></li>
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
            <div id="dashboard-wrapper-\${tabId}" class="dashboard-wrapper" style="width: 800px;position: absolute; visibility: hidden; height: 14000px;"></div>
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
</jqtemplate>