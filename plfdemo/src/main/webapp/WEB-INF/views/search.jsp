<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/default.css" />" />
  <script type="text/javascript" src="<c:url value="/resources/js/jquery-1.6.4.min.js" />"></script>
</head>
<body>
<div>
  <div>
    <h3>Dataset result</h3>
    <span><c:out value="${fn:length(appList)}" /></span> items
    <ul id="appList">
      <c:forEach items="${appList }" var="app">
        <li><a href="<c:url value="/app/${app.name }" />">${app.name }</a></li>
      </c:forEach>
    </ul>
  </div>
  
  <div>
    <h3>Application result</h3>
    <span><c:out value="${fn:length(datasetList)}" /></span> items
    <ul id="datasetList">
      <c:forEach items="${datasetList }" var="dataset">
        <li><a href="<c:url value="/dataset/gui" />">${dataset.name }</a></li>
      </c:forEach>
    </ul>
  </div>
</div>
  
</body>

</html>