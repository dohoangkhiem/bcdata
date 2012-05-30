function Main() {
}

Main.prototype.setContext = function(ctx) {
  this.ctx = ctx;
}

Main.prototype.initUI = function() {
  $(function() {
    $('input:button').button();
    $('input:submit').button();
  });
}

Main.prototype.getDatastoreList = function() {
  $(function() {
    $.ajax({
      url : ctx + "/main/datastore",
      dataType : "json",
      success : function(json) {
        plfdemo.Browser.setDatastoreAll(json);
        plfdemo.Browser.loadItems(json, "datastore");
      },
      error : function() {
        console.debug('Failed to load list of datastore');
      }
    });
  });
}

Main.prototype.getApplicationList = function() {
  $(function() {
    $.ajax({
      url: ctx + '/main/application', 
      dataType: "json", 
      success: function(json) {
        plfdemo.Browser.setApplicationAll(json);
        plfdemo.Browser.loadItems(json, "application");
      }, 
      error: function() {
        console.debug('Failed to load list of application');
      }
    });
  });
} 

plfdemo = {};
plfdemo.Main = new Main();
