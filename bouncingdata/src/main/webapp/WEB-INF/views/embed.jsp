<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/bouncingdata/dashboard.css" />" />
<script type="text/javascript" src="<c:url value="/resources/js/jquery/jquery-1.7.2.min.js" />"></script> 
<script type="text/javascript" src="<c:url value="/resources/js/bouncingdata/embed.js" />"></script>
<script>
	var ctx = '${pageContext.request.contextPath}';
</script>
<div class="bcdata-dashboard" id="bcdata-dashboard" style="width: 800px; height: 600px; position: relative;">  
</div>
<c:choose>
  <c:when test="${not empty errorMsg }">
    <script>
    	$('#bcdata-dashboard').css('height', '10px').before('<span>' + ${errorMsg} + '</span>');
    </script>  
  </c:when>
  <c:otherwise>
    <script>
    	var dbDetail = JSON.parse('${dashboardDetail}');
	  	view(dbDetail.visualizations, dbDetail.dashboard, $('#bcdata-dashboard'));
    </script>
  </c:otherwise>
</c:choose>