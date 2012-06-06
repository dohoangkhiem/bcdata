<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <title>Bouncing Data | User Login</title>
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/resizable/login.css" />" />
  <link type="text/css" href="<c:url value="/resources/css/jquery-ui/smoothness/jquery-ui-1.8.20.custom.css" />" rel="stylesheet" />
  <script type="text/javascript" src="<c:url value="/resources/js/jquery/jquery-1.7.2.min.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/jquery/jquery-ui-1.8.20.custom.min.js" />"></script>
  <style>
  .errorblock {
    color: #ff0000;
    background-color: #ffEEEE;
    border: 2px solid #ff0000;
    padding: 8px;
  }
  </style>
  <script type="text/javascript">
  	$(function() {
  	  $('#login-tabs').tabs();
  	  $('input:button').button();
      $('input:submit').button();
  	})
  </script>
</head>
<body onload='document.f.j_username.focus();'>
  <div class="page" id="page">
    <div class="top-content">
      <div class="logo">
      
      </div>
      <div class="title">
        <h1>Bouncing Data</h1>
      </div>
      <div class="top-horizontal-rule"></div>
    </div>
    <div class="login-main-container">
      <div class="login-tabs" id="login-tabs">
        <ul>
          <li><a href="#login">Login</a></li>
          <li><a href="#signup">Sign up</a></li>
        </ul>
        <div id="login">
          <h4 style="margin-bottom: 2px;">Login to your account</h4>
          <div class="message">
            <c:if test="${not empty error}">
              <div class="errorblock">
                Your login attempt was not successful, try again.
              </div>
            </c:if>
            Demo account: demo/demo
          </div>
          <div class="login-form">
            <form name='f' action="<c:url value='/auth/j_spring_security_check' />" method='post'> 
                <label>Username</label>
                <input type='text' name='j_username' id='username' maxlength="40"></input>
                <label>Password</label>
                <input type='password' name='j_password' id='password' maxlength="100"></input>
                <div class="login-actions">
                  <input type="submit" name="submit" value="Login" onclick="if ($('#username').val().length <= 0 || $('#password').val().length <= 0) return false; else return true;" />
                  <a href="#">Forgot your password?</a>
                </div>            
              <div class="clear"></div>
            </form>
          </div>
        </div>
        <div id="signup">
          <h4>New account</h4>
        </div>
      </div>
    </div>
    <div class="footer">
      <div class="footer-content">
        <a href="#" class="footer-link">Term of Service</a>
        <a href="#" class="footer-link">Privacy Policy</a>
        <a href="#" class="footer-link">Help</a>
        <div class="footer-right">
          <span>Copyright &copy;2012 bouncingdata.com. All rights reserved.</span>
        </div>
      </div>
    </div>
  </div>  
</body>
</html>