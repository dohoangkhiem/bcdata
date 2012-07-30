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
	});
</script>

<div id="main-content" class="analysis-container">
  <div class="analysis-info right-content">
    <div class="anls-main-info">The analysis info here.</div>
    <div class="anls-related-info">The analysis related info here.</div>
  </div>
    
  <div class="analysis-main center-content">
    <div class="anls-header">
      <div class="anls-title"><h2>${anlsTitle}</h2></div>
      <div class="anls-vote">

      </div>
    </div>
    <div class="anls-header-rule"></div>
    <div class="anls-content">
      <ul>
        <li><a href="#anls-dashboard">Dashboard</a></li>
        <li><a href="#anls-code">Code</a></li>
        <li><a href="#anls-data">Data</a></li>
      </ul>
      <div class="anls-dashboard" id="anls-dashboard"></div>
      <div class="anls-code" id="anls-code"></div>
      <div class="anls-data" id="anls-data"></div>
    </div>
      
    <div class="comments-container">
      <h3>Comments</h3>
      <div>All comments here.</div>
    </div>
  </div>
  
</div>