function Main() {
}

Main.prototype.setContext = function(ctx) {
  this.ctx = ctx;
}

Main.prototype.initUI = function() {
  this.workbenchSession = {};
  $(function() {
    
    $('input:button').button();
    $('input:submit').button();
    
    // initializes main navigation
    com.bouncingdata.Nav.init();
   
    /*var searchF = function(query, criteria) {
      if (!query || !criteria) return;
      window.location = ctx + "/main/search?query=" + query + "&criteria=" + criteria;
    }
    
    $('#search-form #search-submit').click(function() {
      searchF();
    });*/
    
    
  });
}

Main.prototype.toggleAjaxLoading = function(display, message) {
  var $element = $('body > #ajaxLoadingMessage');
  if (display) $('span.ajaxLoadingMessage', $element).text(message?message:'Loading...')
  if (display) {
    $element.show();
  } else $element.hide();
}

Main.prototype.setWorkbenchSession = function() {
  
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
com.bouncingdata.Main.initUI();

/**
 * Extra function for jQuery, to get html content of an object, including outer tag
 */
(function($) {
  $.fn.outerHtml = function() {
    return $(this).clone().wrap('<div></div>').parent().html();
  }
})(jQuery);
