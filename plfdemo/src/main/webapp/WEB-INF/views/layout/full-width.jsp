<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<html>
<head>
  <title><tiles:insertAttribute name="title" ignore="true" /></title>
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/full-width.css" />" />
  <link type="text/css" href="<c:url value="/resources/css/jquery-ui/smoothness/jquery-ui-1.8.20.custom.css" />" rel="stylesheet" />
  <script type="text/javascript" src="<c:url value="/resources/js/jquery/jquery-1.7.2.min.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/jquery/jquery-ui-1.8.20.custom.min.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/plfdemo/main.js" />"></script>
  
  <script>
    var ctx = '${pageContext.request.contextPath}';
    plfdemo.Main.setContext(ctx);
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
      <div class="left-panel">
        <div class="search-box">
          <div id="search-form">
            <input type="text" class="search-input" id="query" name="query" /> 
            <input type="submit" value="Search" id="search-submit" />
          </div>
          <div class="clear"></div>
        </div>
        <div class="left-content">
          <tiles:insertAttribute name="left-content" />
        </div>
        <div class="clear"></div>
      </div>
      <div class="main-content">
        <tiles:insertAttribute name="main-content" />
      </div>
      <div class="clear"></div>
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