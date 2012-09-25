<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="nav-item" id="nav-home">
  <form action="<c:url value='/home'/>" method="GET" id="nav-home-form" style="padding: 0px;margin: 0px;"></form>
  <a href="<c:url value='/home'/>" class="nav-item-link" id="nav-home-link">
    <div class="nav-item-icon"></div>
    <div class="nav-item-text">Home</div>
  </a>
</div>
<div class="nav-item" id="nav-profile">
  <form action="<c:url value='/profile'/>" method="GET" id="nav-profile-form" style="padding: 0px;margin: 0px;"></form>
  <a href="<c:url value='/profile'/>" class="nav-item-link" id="nav-profile-link">
    <div class="nav-item-icon"></div>
    <div class="nav-item-text">Profile</div>
  </a>
</div>
<div class="nav-item" id="nav-create">
  <form action="<c:url value='/create'/>" method="GET" id="nav-create-form" style="padding: 0px;margin: 0px;"></form>
  <a href="<c:url value='/create'/>" class="nav-item-link" id="nav-create-link">
    <div class="nav-item-icon"></div>
    <div class="nav-item-text">Create</div>
  </a>
</div>
<div class="nav-item" id="nav-connect">
  <form action="<c:url value='/connect'/>" method="GET" id="nav-connect-form" style="padding: 0px;margin: 0px;"></form>
  <a href="<c:url value='/connect'/>" class="nav-item-link" id="nav-connect-link">
    <div class="nav-item-icon"></div>
    <div class="nav-item-text">Connect</div>
  </a>
</div>
<!-- div class="nav-item" id="nav-search">
  <form action="<c:url value='/main/search'/>" method="GET" id="nav-search-form" style="padding: 0px;margin: 0px;"></form>
  <a href="<c:url value='/main/search'/>" class="nav-item-link" id="nav-search-link">
    <div class="nav-item-icon"></div>
    <div class="nav-item-text">Search</div>
  </a>
</div-->
  