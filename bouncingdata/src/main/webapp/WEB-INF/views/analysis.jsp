<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<script type="text/javascript" src="<c:url value="/resources/js/bouncingdata/analysis.js" />"></script>
<script>
	$(function() {
	  com.bouncingdata.Analysis.init('${anlsApp.guid}');
	  
	  var dbDetail = $.parseJSON('${dashboardDetail}');
	  com.bouncingdata.Dashboard.view(dbDetail.visualizations, dbDetail.dashboard, $('#main-content #anls-dashboard'));
	 
	});
</script>
<script id="comment-template" type="text/x-jquery-tmpl">
	<li class="comment-item" id="comment-\${id}" nodeid="\${id}">
    <div class="comment-item-body">
      <div class="comment-header">
        <div class="comment-author">\${username}</div>
      </div>
      <div class="comment-message">\${message}</div>
      <div class="comment-footer">
        <span class="comment-date">\${date}</span>&nbsp;
        <span class="up-vote">\${upVote}</span>&nbsp;
        <a class="up-vote-link" href="#"><span class="up-vote-icon">Vote up</span></a>&nbsp;
        <span class="down-vote">\${downVote}</span>&nbsp;
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
<div id="main-content" class="analysis-container">
  <div class="analysis-info right-content">
    <div class="anls-main-info">
      <div class="anls-info-header">
        <div class="anls-info-title">Analysis Info</div>
        <div class="anls-info-title-line"></div>
      </div>
      <p class="line-item">
        <strong>Analysis: </strong><span>${anlsApp.name }</span>
      </p>
      <p class="line-item">
        <strong>Author: </strong><span>${anlsApp.authorName }</span>
      </p>
      <p class="line-item">
        <strong>Description: </strong><span>${anlsApp.description }</span>
      </p>
      <p class="line-item">
        <strong>Create at: </strong><span>${anlsApp.createAt }</span>
      </p>
      <p class="line-item">
        <strong>Tags: </strong><span>${anlsApp.tags }</span>
      </p>  
    </div>
    <div class="anls-related-info">
      <div class="anls-info-header">
        <div class="anls-info-title">Related Info</div>
        <div class="anls-info-title-line"></div>
      </div>
    </div>
  </div>
    
  <div class="analysis-main center-content">
    <div class="center-content-wrapper">
      <div class="top-rule"></div>
      <div class="anls-header">
        <div class="anls-title"><h2>${anlsTitle}</h2></div>
        <div class="anls-vote">
  
        </div>
      </div>
      <div class="anls-header-rule"></div>
      <div class="anls-content anls-tab-container" id="anls-content">
        <div>
          <div class="anls-dashboard" id="anls-dashboard"></div>
          <div class="anls-code" id="anls-code">
            <div class="code-block" id="code-block">
              <pre></pre>
              <script>
                var code = "${anlsCode}";
                $('#code-block pre').text(code);
              </script>
            </div>
          </div>
          <div class="anls-data" id="anls-data"></div>
        </div>
        
        <ul class="anls-tabs">
          <li class="anls-tab"><a href="#anls-dashboard">Dashboard</a></li>
          <li class="anls-tab"><a href="#anls-code">Code</a></li>
          <li class="anls-tab"><a href="#anls-data">Data</a></li>
        </ul>
      </div>
        
      <div class="comments-container">
        <h3 class="comments-title"><a href="javascript:void(0);" onclick="$('#comment-form').toggle('slow');">Comments</a></h3>
        <div class="comment-form" id="comment-form">
          <form>
            <fieldset>
            <!-- p>
              <label for="comment-title">Title</label>
              <input type="text" id="comment-title" />
            </p-->
            <p>
              <!-- label for="comment-content">Content</label-->
              <textarea rows="5" id="message"></textarea>
            </p>  
            <p>
              <input type="button" class="comment-submit" id="comment-submit" value="Post comment">
            </p>              
            </fieldset>
          </form>
        </div>
        <div class="clear"></div>
        <div class="comments">
          <h3 class="comments-count">Comments</h3>
          <ul id="comment-list" class="comment-list">            
          </ul>
        </div>
      </div>
    </div>
    
  </div>
  
</div>