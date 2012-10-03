function Main() {
}

Main.prototype.setContext = function(ctx) {
  this.ctx = ctx;
}

Main.prototype.init = function() {
  this.workbenchSession = {};
  $(function() {
    
    $('input:button').button();
    $('input:submit').button();
    
    // initializes main navigation & ajax loading capabilities
    com.bouncingdata.Nav.init();    
    
    $('.search-container #search-form').submit(function(e) {
      e.preventDefault();
      var query = $('#query', $(this)).val();
      var criteria = $('#criteria', $(this)).val();
      if (!query || !criteria) return false;
      com.bouncingdata.Nav.fireAjaxLoad(ctx + '/main/search/?query=' + query + '&criteria=' + criteria, false);      
      return false;
    });
    
    /*// 
    Spring.addDecoration(new Spring.AjaxEventDecoration({
      elementId: 'search-form',
      formId: 'search-form',
      event: "onsubmit",
      params: {fragments: "main-content"}
    }));*/
    
    window.history.pushState({linkId: window.location.href}, null, window.location.href);
    
  });
}

Main.prototype.toggleAjaxLoading = function(display, message) {
  var $element = $('body > #ajaxLoadingMessage');
  if (display) $('span.ajaxLoadingMessage', $element).text(message?message:'Loading...')
  if (display) {
    $element.show();
  } else $element.hide();
}

function Utils() {
}

Utils.prototype.getConsoleCaret = function(language) {
  if (language == "python") return ">>>";
  else if (language == "r") return ">";
  else return null;
}


com = {};
com.bouncingdata = {};
com.bouncingdata.Main = new Main();
com.bouncingdata.Utils = new Utils();
Utils = com.bouncingdata.Utils;
com.bouncingdata.Main.init();

/**
 * Extra function for jQuery, to get html content of an object, including outer tag
 */
(function($) {
  $.fn.outerHtml = function() {
    return $(this).clone().wrap('<div></div>').parent().html();
  }
})(jQuery);
