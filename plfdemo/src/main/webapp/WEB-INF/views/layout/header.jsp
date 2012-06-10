<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="header-content">
  <div style="float: right; margin-right: 5px; text-align: right;">
    <span style="color: red; clear: both;">This version maybe incomplete and in continuous development. The old version <a style="color: blue" href="<c:url value="/plfdemo" />">here</a></span>
    <div>Welcome back <span style="font-weight: bold;"> ${username } </span>
      <a style="color: blue;" href="<c:url value="/auth/j_spring_security_logout" />" >Logout</a>
    </div>
  </div>
  
  <h2 style="margin: 0;">
    <a href="#">Bouncing Data</a>
  </h2>
  <div class="clear"></div>
</div>