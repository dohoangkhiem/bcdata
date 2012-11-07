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

Utils.prototype.renderDatatable = function(data, $table) {
  if (!data || data.length <= 0) return;
  
  //prepare data
  var first = data[0];
  var aoColumns = [];
  for (key in first) {
    aoColumns.push({ "sTitle": key});
  }
  
  var aaData = [];
  for (index in data) {
    var item = data[index];
    var arr = [];
    for (key in first) {
      arr.push(item[key]);
    }
    aaData.push(arr);
  }
  var datatable = $table.dataTable({
    "aaData": aaData, 
    "aoColumns": aoColumns, 
    "bJQueryUI": true,
    "sPaginationType": "full_numbers"
  });
  var keys = new KeyTable( {
    "table": $table[0],
    "datatable": datatable
  });
}

Utils.prototype.setOverlay = function($panel, isActive) {
  if (isActive) {
    var $overlay = $('<div class="overlay-panel" style="position: absolute; top: 0; bottom: 0; left: 0; right: 0;"></div>');
    $overlay.css('background', 'url("' + ctx + '/resources/images/ajax-loader.gif") no-repeat 50% 10% #eee')
      .css('z-index', 10).css('background-size', '30px 30px').css('opacity', '0.8');
    if (!$panel.css('position')) {
      $panel.css('position', 'relative');
    }
    $panel.append($overlay);
  } else {
    $('div.overlay-panel', $panel).remove();
  }
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
