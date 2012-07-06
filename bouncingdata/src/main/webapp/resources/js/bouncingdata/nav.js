function Nav() {
  selected = null;
}

Nav.prototype.init = function() {
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
}

/**
 * Set selected item
 */
Nav.prototype.setSelected = function() {
  this.selected = {};
}

com.bouncingdata.Nav = new Nav();