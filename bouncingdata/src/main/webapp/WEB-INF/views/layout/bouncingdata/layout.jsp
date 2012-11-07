<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="content-type" content="text/html;charset=utf-8" />
  
  <title><tiles:insertAttribute name="title" ignore="true" /></title>
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/bouncingdata/default.css" />" />
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/bouncingdata/browser.css" />" />
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/bouncingdata/workbench.css" />" />
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/bouncingdata/analysis.css" />" />
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/bouncingdata/home.css" />" />
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/bouncingdata/connect.css" />" />
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/bouncingdata/datatable.css" />" />
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/jquery-ui/smoothness/jquery-ui-1.8.20.custom.css" />" rel="stylesheet" />
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/datatable/css/demo_page.css" />" />
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/datatable/css/jquery.dataTables.css" />" />
  <!-- link type="text/css" href="<c:url value="/resources/css/jquery-ui/redmond/jquery-ui-1.8.21.custom.css" />" rel="stylesheet" /-->
  <!-- script src="//ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
  <script>window.jQuery || document.write('<script src="resources/js/jquery/jquery-1.7.2.min.js"><\/script>')</script-->
  <script type="text/javascript" src="<c:url value="/resources/js/jquery/jquery-1.7.2.min.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/jquery/jquery-ui-1.8.20.custom.min.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/jquery.layout-1.3.0.rc30.4.min.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/jquery.dataTables.min.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/KeyTable.min.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/jquery.tmpl.min.js" />"></script>
  
  <script type="text/javascript" src="<c:url value="/resources/dojo/dojo.js" />"> </script>
  <script type="text/javascript" src="<c:url value="/resources/spring/Spring.js" />"> </script>
  <script type="text/javascript" src="<c:url value="/resources/spring/Spring-Dojo.js" />"> </script>
  
  <script type="text/javascript" src="<c:url value="/resources/js/jqconsole-2.7.min.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/jquery.bxSlider.min.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/jquery.hashchange.min.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/jquery.easytabs.min.js" />"></script>
  
  <!-- Some external scripts like this need to be loaded in defer way -->
  <script type="text/javascript" src="<c:url value="/resources/js/ace-min/ace.js" />" charset="utf-8"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/jquery.form.js" />"></script>
  
  <!-- Loads at start page for dev. mode -->
  <script type="text/javascript" src="<c:url value="/resources/js/bouncingdata/main.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/bouncingdata/dashboard.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/bouncingdata/activitystream.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/bouncingdata/workbench.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/bouncingdata/browser.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/bouncingdata/nav.js" />"></script>
  
  <script type="text/javascript" src="<c:url value="/resources/js/syntaxhighlighter/scripts/shCore.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/syntaxhighlighter/scripts/shBrushJScript.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/syntaxhighlighter/scripts/shBrushPython.js" />"></script>
  <link href="<c:url value="/resources/js/syntaxhighlighter/styles/shCore.css" />" rel="stylesheet" type="text/css" />
  <link href="<c:url value="/resources/js/syntaxhighlighter/styles/shThemeFadeToGrey.css" />" rel="stylesheet" type="text/css" />  
    
  <script>
    var ctx = '${pageContext.request.contextPath}';
    var host = '${pageContext.request.remoteHost}';
    console.debug('Host name: ' + host);
    com.bouncingdata.Main.setContext(ctx);
    com.bouncingdata.Main.username = '${pageContext.request.userPrincipal.name}';
    console.debug('Username: ' + com.bouncingdata.Main.username);
  </script>
</head>
<body>
  <div id="page">
    <div class="header-container">
      <div class="header">
        <tiles:insertAttribute name="header-content" />
      </div>
    </div>
    <div style="clear: both; height: 60px;"></div>
    <div class="main-container">
      <div class="main-navigation">
        <tiles:insertAttribute name="navigation" />
      </div>
      <div class="main-content-container">
        <tiles:insertAttribute name="main-content" />
      </div>
    </div>
    <div class="footer-container">
      <div class="footer">
        <tiles:insertAttribute name="footer-content" />
      </div>
    </div>
  </div>
  <div id="ajaxLoadingMessage">
    <span class="ajaxLoadingMessage">Loading...</span>
  </div>
  <a href="#" id="hiddenLinkForAjax" style="display: none;"></a> 
</body>
</html>