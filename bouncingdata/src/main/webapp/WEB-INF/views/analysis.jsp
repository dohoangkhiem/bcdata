<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<script type="text/javascript" src="<c:url value="/resources/js/bouncingdata/analysis.js" />"></script>
<script>
	$(function() {
	  com.bouncingdata.Analysis.init('${anls.guid}');
	  
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
      <c:if test="${not empty datasetDetailMap }">
        <p class="relatedDatasets">
          <strong>Related datasets: </strong>
          <script>
            $(function() {
              var datasetDetailMap = ${datasetDetailMap};
            	var $relatedDatasets = $('.related-info p.relatedDatasets');
            	for (guid in datasetDetailMap) {
            	  var dataset = datasetDetailMap[guid];
            	  $relatedDatasets.append('<a target="_blank" href="' + ctx + '/dataset/view/' + guid + '">' + dataset.name + '</a>&nbsp;');  
            	}
            });
          </script>
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
          <a href="#" class="anls-vote-up">Vote up</a>&nbsp;
          <a href="#" class="anls-vote-down">Vote down</a>&nbsp;
          <a href="#" class="anls-embed-button" id="anls-embed-button">Embed</a>
        </div>
        <div class="embedded-link" id="embedded-link">
          <textarea id="embedded-link-text" spellcheck='false'></textarea>
          <a class="embedded-link-hidden" href="<c:url value="/anls/embed/" />${anls.guid}" style="display: none;">embedded</a>
        </div>
        <div class="clear"></div>
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
          <div class="anls-data" id="anls-data">
            <c:choose>
              <c:when test="${not empty datasetDetailMap }">
                <script>
                	$(function() {
                	  var datasetDetailMap = ${datasetDetailMap};
                  	var $dsContainer = $('#anls-data');
                  	com.bouncingdata.Workbench.renderDatasets(datasetDetailMap, $dsContainer);  
                	});
                </script>
              </c:when>
              <c:otherwise>
                <script>
                	var $dsContainer = $('#anls-data');
                	$dsContainer.append('<p>No data for this analysis.</p>');
                </script>
              </c:otherwise>
            </c:choose>
            
          </div>
        </div>
        
        <ul class="anls-tabs">
          <li class="anls-tab"><a href="#anls-dashboard">Dashboard</a></li>
          <li class="anls-tab"><a href="#anls-code">Code</a></li>
          <li class="anls-tab"><a href="#anls-data">Data</a></li>
        </ul>
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