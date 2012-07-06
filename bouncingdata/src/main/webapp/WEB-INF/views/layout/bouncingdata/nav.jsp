<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="nav-item" id="nav-home">
  <form action="<c:url value='/home'/>" method="GET" id="nav-home-form" style="padding: 0px;margin: 0px;"></form>
  <a href="#" class="nav-item-link" id="nav-home-link">
    <div class="nav-item-icon"></div>
    <div class="nav-item-text">Home</div>
  </a>
</div>
<div class="nav-item" id="nav-profile">
  <form action="<c:url value='/profile'/>" method="GET" id="nav-profile-form" style="padding: 0px;margin: 0px;"></form>
  <a href="#" class="nav-item-link" id="nav-profile-link">
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
<script type="text/javascript">
	$('.main-navigation .nav-item').each(function() {
	  var $form = $('form', this);
	  var $link = $('a.nav-item-link', this);
	  Spring.addDecoration(new Spring.AjaxEventDecoration({
      elementId: $link.attr('id'),
      formId: $form.attr('id'),
      event: "onclick",
      params: {fragments: "main-content"}
   	}));
	  
	  $link.bind('click', function(e) {
	    var $oldSelected = $('.main-navigation div.nav-item-selected');
	    if ($oldSelected) {
	      $oldSelected.removeClass('nav-item-selected');
	    }
	    $(this).parent().addClass('nav-item-selected');
	  })
	});
	
</script>
  