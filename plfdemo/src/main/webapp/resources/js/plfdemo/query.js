function Query() {
  
}

Query.prototype.init = function() {
  $(function() {
    var searchFunc = function() {
      var query = $.trim($('#search-form #query').val()); 
      if (query.length > 0) {
        plfdemo.Query.execute(query); 
      }
    }
    $('#search-form #query').keypress(function(e) {
      var code = (e.keyCode ? e.keyCode : e.which);
      if(code == 13) {
        searchFunc();
        return false;
      } else return true;
    });
    $('#search-form #search-submit').click(function() {
      searchFunc();
      return false;
    });
  });
}

/**
 * 
 */
Query.prototype.execute = function(query) {
  $(function() {
    $.ajax({
      url: plfdemo.Main.ctx + '/main/search',
      data: {
        query: query
      },
      success: function(json) {
        var datastores = json['datastores'];
        var apps = json['applications'];
        var tables = json['datasets'];
        plfdemo.Browser.setMode("search");
        plfdemo.Browser.loadItems(tables, "dataset");
        plfdemo.Browser.loadItems(datastores, "datastore");
        plfdemo.Browser.loadItems(apps, "application");
      }, 
      error: function() {
        console.debug("Failed to execute search request.");
      }
    });
  });
}

plfdemo.Query = new Query();
plfdemo.Query.init();