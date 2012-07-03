<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="header-content">
  <div style="float: right; margin-right: 5px; text-align: right;">
    <div>Welcome back <span style="font-weight: bold;"> ${username } </span>
      <a style="color: blue;" href="<c:url value="/auth/j_spring_security_logout" />"> Logout</a>
    </div>
  </div>
  
  <h2 style="margin: 0;">
    <a href="#">Bouncing Data</a>
  </h2>
  <div class="search-container">
    <div id="search-form" class="search-form">
      <input type="text" class="search-input" id="query" name="query" />
      <input type="submit" value="Search" id="search-submit" />
    </div>
    <div class="clear"></div>
  </div>

</div>