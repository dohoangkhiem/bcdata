<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="header-content">
  <div style="float: right; margin-right: 10px; text-align: right;">
    <sec:authorize access="isAuthenticated()">
      <div>Welcome back <span style="font-weight: bold;"> 
          <sec:authentication property="principal.username" />
        </span>
        <a style="color: blue;" href="<c:url value="/auth/j_spring_security_logout" />"> Logout</a>
      </div>
    </sec:authorize>
  </div>
  
  <h2 style="margin: 0 0 0 10px; float: left; display: inline;">
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