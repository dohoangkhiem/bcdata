<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <title><tiles:insertAttribute name="title" ignore="true" /></title>
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/resizable/main.css" />" />
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/resizable/browser.css" />" />
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/resizable/workspace.css" />" />
  <link type="text/css" href="<c:url value="/resources/css/jquery-ui/smoothness/jquery-ui-1.8.20.custom.css" />" rel="stylesheet" />
  <!-- link type="text/css" href="<c:url value="/resources/css/jquery-ui/redmond/jquery-ui-1.8.21.custom.css" />" rel="stylesheet" /-->
  <!-- script src="//ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
  <script>window.jQuery || document.write('<script src="resources/js/jquery/jquery-1.7.2.min.js"><\/script>')</script-->
  <script type="text/javascript" src="<c:url value="/resources/js/jquery/jquery-1.7.2.min.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/jquery/jquery-ui-1.8.20.custom.min.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/jquery.layout-1.3.0.rc30.4.min.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/plfdemo/main.js" />"></script>
  
  <script>
    var ctx = '${pageContext.request.contextPath}';
    plfdemo.Main.setContext(ctx);
    plfdemo.Main.username = '${username}';
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
        <div id="main-layout">
          <div class="left-container main-layout-west">
            <tiles:insertAttribute name="left-content" />
          </div>
          <div class="main-content-container main-layout-center">
            <tiles:insertAttribute name="main-content" />
          </div>
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