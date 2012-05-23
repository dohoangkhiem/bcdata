<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<html>
<head>
  <title><tiles:insertAttribute name="title" ignore="true" /></title>
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/default.css" />" />
  <link type="text/css" href="<c:url value="/resources/css/jquery-ui/smoothness/jquery-ui-1.8.20.custom.css" />" rel="stylesheet" />
  <script type="text/javascript" src="<c:url value="/resources/js/jquery/jquery-1.7.2.min.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/jquery/jquery-ui-1.8.20.custom.min.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/plfdemo/main.js" />"></script>
  
  <script>
    var ctx = '${pageContext.request.contextPath}';
    plfdemo.Main = new Main(ctx);
    $(function() {
      plfdemo.Main.initUI();
    });
  </script>
</head>
<body>
<div id="page">
  <div class="main-container">
    <div class="header-container">
      <div class="header">
        <tiles:insertAttribute name="header-content" />
      </div>
    </div>
    <div class="content-container">
      <div class="content">
        <div class="main-content"><tiles:insertAttribute name="main-content" /></div>
        <div class="right-content">
          <div class="search-box">
            <form id="search-form" method="get" action="<c:url value="/search" />">
              <input type="text" class="search-input" id="query" name="query" /> 
              <input type="submit" value="Search" />
            </form>
            <div style="clear: both;"></div>
          </div>
          <div class="browser-panel">
            <tiles:insertAttribute name="right-content" />
          </div>
          <div style="clear:both;"></div>
          </div>
        <div style="clear: both;"></div>
      </div>
    </div>
    <div class="footer-container">
      <div class="footer">
        <tiles:insertAttribute name="footer-content" />
      </div>
    </div>
  </div>
</div>
</body>
</html>
