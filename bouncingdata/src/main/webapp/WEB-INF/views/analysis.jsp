<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<script>
	$(function() {
	  if (!com.bouncingdata.Analysis) {
	    $.getScript(ctx + "/resources/js/bouncingdata/analysis.js", function() {
	    	console.debug("analysis.js async. loaded!");
	    	
	    	var anls = {
	    	    guid: '${anls.guid}',
	    	    name: '${anls.name}',
	    	    description: '${anls.description}',
	    	    user: {username: '${anls.user.username}'},
	    	    language: '${anls.language}',
	    	    code: '${anlsCode}'
	    	}	
	  	  	
	    	com.bouncingdata.Analysis.init(anls);
	    });  
	  } else {
	    var anls = {
	  	    guid: '${anls.guid}',
	  	    name: '${anls.name}',
	  	    description: '${anls.description}',
	  	    user: {username: '${anls.user.username}'},
	  	    language: '${anls.language}',
	  	    code: '${anlsCode}'
	  	}
	    com.bouncingdata.Analysis.init(anls);
	  }
	 
	  var dbDetail = $.parseJSON('${dashboardDetail}');
	  com.bouncingdata.Dashboard.view(dbDetail.visualizations, dbDetail.dashboard, $('#main-content #anls-dashboard'));
	
	});
</script>
<div id="main-content" class="analysis-container">
  <div class="analysis-info right-content">
    <div class="anls-summary summary">
      <div class="anls-info-header info-header">
        <div class="anls-info-title info-title">Analysis Info</div>
        <div class="anls-info-title-line info-title-line"></div>
      </div>
      <p class="line-item">
        <strong>Analysis: </strong><span>${anls.name }</span>
      </p>
      <p class="line-item">
        <strong>Author: </strong><span>${anls.user.username }</span>
      </p>
      <p class="line-item">
        <strong>Description: </strong><span>${anls.description }</span>
      </p>
      <p class="line-item">
        <strong>Create at: </strong><span>${anls.createAt }</span>
      </p>
      <p class="line-item">
        <strong>Tags: </strong><span>${anls.tags }</span>
      </p>  
    </div>
    <div class="anls-related-info related-info">
      <div class="anls-info-header info-header">
        <div class="anls-info-title">Related Info</div>
        <div class="anls-info-title-line"></div>
      </div>
      
      <c:if test="${not empty datasetList }">
        <p class="relatedDatasets">
          <strong>Related datasets: </strong>
          
          <c:forEach items="${datasetList }" var="entry">
            <a class="related-dataset-link" href="<c:url value="/dataset/view/${entry.key }" />"${entry.value }></a>&nbsp;
          </c:forEach>
        </p>
      </c:if>
    </div>
  </div>
    
  <div class="analysis-main center-content">
    <div class="center-content-wrapper">
      <div class="top-rule"></div>
      <div class="anls-header">
        <div class="anls-title main-title"><h2>${anls.name}</h2></div>
        <div class="anls-action-links">
          <h3 class="anls-score">${anls.score}</h3>&nbsp;
          <a href="javascript:void(0)" class="anls-vote-up">Vote up</a>&nbsp;&nbsp;
          <a href="javascript:void(0)" class="anls-vote-down">Vote down</a>&nbsp;&nbsp;
          <a href="javascript:void(0)" class="anls-embed-button" id="anls-embed-button">Embed</a>&nbsp;&nbsp;
          <c:if test="${isOwner }">
            <a href="javascript:void(0)" class="anls-edit-button" id="anls-edit-button" title="Edit this analysis in your workbench">Edit</a>
          </c:if>
        </div>
        <div class="embedded-link" id="embedded-link" style="display: none;">
          <textarea id="embedded-link-text" spellcheck='false' style="float: left;"></textarea>
          <!-- a class="embedded-link-hidden" href="<c:url value="/anls/embed/" />${anls.guid}" style="display: none;">embedded</a-->
          <div class="embedded-options" style="float: left; margin-left: 15px;">     
            <div>  
              <strong>Include tab</strong><br />
              <input id="include-viz" type="checkbox" checked />Dashboard<br />
              <input id="include-code" type="checkbox"/>Code<br />
              <input id="include-data" type="checkbox"/>Data<br />
            </div><br />
            <div>
              <strong>Width</strong><br />
              <input id="embedded-width" type="text" value="800" />&nbsp; pixels<br/><br/>
              <strong>Height</strong><br />
              <input id="embedded-height" type="text" value="600" />&nbsp; pixels<br/>
              <input type="checkbox" id="embedded-border" />&nbsp; Border?
            </div>
          </div>
        </div>
        <div class="clear"></div>
      </div>
      <div class="anls-header-rule"></div>
      <div class="anls-content anls-tab-container" id="anls-content">
        <ul class="anls-tabs">
          <li class="anls-tab"><a href="#anls-dashboard">Dashboard</a></li>
          <li class="anls-tab"><a href="#anls-code">Code</a></li>
          <li class="anls-tab"><a href="#anls-data">Data</a></li>
        </ul>
        <div class="clear"></div>
        <div class="anls-tabs-content-wrapper">
          <div class="anls-dashboard" id="anls-dashboard"></div>
          <div class="anls-code" id="anls-code">
            <div class="code-block" id="code-block">
              <pre class="brush: py"></pre>
            </div>
            <div class="raw-source-link" style="float: right;">
              <a target="_blank" href="view-source:<c:url value="/public/source/${anls.guid }" />" style=" color: #999; text-decoration: none; font-size: 11px;">Full view</a>
            </div>
            <div class="clear"></div>
          </div>
          <div class="anls-data" id="anls-data">
            <c:if test="${empty datasetList and empty attachments }">
              <span>No dataset</span>
            </c:if>
            <c:if test="${not empty datasetList}">
              <c:forEach items="${datasetList }" var="entry">
                <div class="anls-dataset" style="margin: 1em 0 2.5em 0;" dsguid="${entry.key }">
                  <div class="dataset-item-title">
                    <strong>
                      <a href="<c:url value="/dataset/view/${entry.key }" />">${entry.value }</a>
                      &nbsp;
                    </strong>
                    <a href="<c:url value="/dataset/dl/csv/${entry.key }" />" style="color: blue; text-decoration: none;">Download CSV</a>&nbsp;&nbsp;
                    <a href="<c:url value="/dataset/dl/json/${entry.key }" />" style="color: blue; text-decoration: none;">Download JSON</a>
                  </div>
                  <table dsguid="${entry.key }" class="dataset-table"></table>
                </div>
              </c:forEach>
            </c:if>
            <c:if test="${not empty attachments }">
              <c:forEach items="${attachments }" var="attachment">
                <script>
                	$(function() {
                	  var $attachment = $('<div class="anls-attachment" style="margin: 1em 0 2.5em 0;"><div class="dataset-item-title"><strong>'
                	  + '<a href="">${attachment.name}</a></strong>&nbsp;<a href="<c:url value="/dataset/att/csv/${anls.guid}/${attachment.name}" />" style="color: blue; text-decoration: none;">Download CSV</a>'
                	  + '&nbsp;&nbsp;<a href="<c:url value="/dataset/att/json/${anls.guid}/${attachment.name}" />" style="color: blue; text-decoration: none;">Download JSON</a></div>' 
                	  + '<table class="attachment-table"></table></div>');
                	  $attachment.appendTo($('#anls-data'));
                	  var $table = $('table', $attachment);
                	  var data = '${attachment.data}';
                	  com.bouncingdata.Workbench.renderDatatable($.parseJSON(data), $table);
                	});
              	</script>
              </c:forEach>  
            </c:if>
          </div>
        </div>
      </div>
        
      <div class="comments-container">
        <h3 class="comments-title"><a href="javascript:void(0);" onclick="$('#comment-form').toggle('slow');">Comment</a></h3>
        <div class="comment-form" id="comment-form">
          <form>
            <fieldset>
            <p>
              <textarea rows="5" id="message" spellcheck='false'></textarea>
            </p>  
            <p>
              <input type="button" class="comment-submit" id="comment-submit" value="Post comment">
            </p>              
            </fieldset>
          </form>
        </div>
        <div class="clear"></div>
        <label id="comments"></label>
        <div class="comments">
          <h3 class="comments-count">Comments</h3>
          <ul id="comment-list" class="comment-list">            
          </ul>
        </div>
      </div>
    </div>
    
  </div>
  
</div>