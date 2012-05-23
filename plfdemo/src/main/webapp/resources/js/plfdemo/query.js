function Query() {
  
}

Query.prototype.init = function() {
  $(function() {
    $('#query-input').keypress(function(event) {
      var keycode = (event.keyCode ? event.keyCode : event.which);
      if(keycode == '13') {
        plfdemo.Query.execute($('#query-input').val());
        return false;
      } else return true;
    });
  });
}

/**
 * 
 */
Query.prototype.execute = function(query) {
  if (query) {
    $.ajax({
      url: plfdemo.Main.ctx + "/dataset/query",
      data: { query: query },
      success: function(result) {
        console.info("Query OK, result: " + result);
        // render result
        $("#data-raw-view").text(result);
      },
      error: function(result) {
        // notify about error
        $("#data-raw-view").text("Error occurs: " + result);
      }
    });
  }
}

plfdemo.Query = new Query();
plfdemo.Query.init();