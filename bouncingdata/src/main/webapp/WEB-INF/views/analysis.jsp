<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script>
	$(function() {
	  var dbDetail = $.parseJSON('${dashboardDetail}');
	  var vizMap = dbDetail.visualizations;
	  var dashboardStatus = dbDetail.dashboard;
	  console.debug("Visualization map: " + vizMap);
	  console.debug("Dashboard status: " + dashboardStatus);
	  com.bouncingdata.Dashboard.load(vizMap, dashboardStatus, $('#main-content #anls-dashboard'), false);
	});
</script>

<div id="main-content" class="analysis-container">
  <div class="analysis-info right-content">
    <div class="anls-main-info">The analysis info here.</div>
    <div class="anls-related-info">The analysis related info here.</div>
  </div>
    
  <div class="analysis-main center-content">
    <div class="anls-content">
      <div class="anls-dashboard" id="anls-dashboard" style="width: 800px; height: 600px; border: 1px solid #999999;"></div>
    </div>
      
    <div class="comments-container">
      All comments here.
    </div>
  </div>
  
</div>