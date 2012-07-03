<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="header-content">
  <div style="float: right; margin-right: 5px; text-align: right;">
    <div>Welcome back <span style="font-weight: bold;"> ${username } </span>
      <a style="color: blue;" href="<c:url value="/auth/j_spring_security_logout" />" >Logout</a>
    </div>
  </div>
  
  <h2 style="margin: 0;">
    <a href="#">Bouncing Data</a>
  </h2>
  <div class="navi">
    <!-- a href="<c:url value="/main" />" >Main</a> &nbsp;&nbsp;
    <a href="<c:url value="/test" />" >Test</a-->
    
    <ul>
      <li>
        <form action="<c:url value='main'/>" method="GET" id="mainForm" style="padding: 0px;margin: 0px;"></form>
        <a id="mainLink" href="#">Main</a>
      </li>
      <li>
        <form action="<c:url value='/test'/>" method="GET" id="testForm" style="padding: 0px;margin: 0px;"></form>
        <a id="testLink" href="#">Test</a>
      </li>
    </ul>
    <script type="text/javascript">
      Spring.addDecoration(new Spring.AjaxEventDecoration({
         elementId: "mainLink",
         formId: "mainForm",
         event: "onclick",
         params: {fragments: "main-content"}
      }));
      Spring.addDecoration(new Spring.AjaxEventDecoration({
         elementId: "testLink",
         formId: "testForm",
         event: "onclick",
         params: {fragments: "main-content"}
      }));
    </script>
  </div>
  <div class="clear"></div>
</div>