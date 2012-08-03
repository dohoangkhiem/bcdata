<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script>
	$(function() {
	  var dbDetail = $.parseJSON('${dashboardDetail}');
	  var vizMap = dbDetail.visualizations;
	  var dashboardStatus = dbDetail.dashboard;
	  console.debug("Visualization map: " + vizMap);
	  console.debug("Dashboard status: " + dashboardStatus);
	  com.bouncingdata.Dashboard.view(vizMap, dashboardStatus, $('#main-content #anls-dashboard'));
	  
	  $('#anls-content').easytabs();
	});
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
                var code = '${anlsCode}';
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
        <h3>Comments</h3>
        <div>All comments here.</div>
      </div>
    </div>
    
  </div>
  
</div>