function Main(ctx) {
  this.ctx = ctx;
}

Main.prototype.initUI = function() {
  $('input:button').button();
  $('input:submit').button();
}

Main.prototype.getDatasetList = function() {
  $(function() {
    $.ajax({
      url : ctx + "/main/dataset",
      dataType : "json",
      success : function(json) {
        var i;
        for (i = 0; i < json.length; i++) {
          var dataset = json[i];
          $("#dataset-list-all").append('<li><a href="' + ctx + '/dataset/gui"><span>' + dataset['name']
                  + '</span></a></li>');
        }
      },
      error : function() {
        alert("error");
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
        var i = 0;
        for (i = 0; i < json.length; i++) {
          var app = json[i];
          $("#application-list-all").append('<li><a href="' + ctx + "/app/" + encodeURI(app['name']) + '#app"><span>' + app['name'] + '</span></a></li>');
        }  
      }, 
      error: function() {}
    });
  });
}

plfdemo = {};
