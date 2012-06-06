function Browser() {
  this.mode = "all";
}

Browser.prototype.init = function() {
  var me = this;
  $(function() {
    // init tabs
    $("#browser-tabs" ).tabs();
    
    // load datasets, applications in "My stuff" tab
    me.getDatasetList();
    me.getApplicationList();
    me.setMode("all");
    
    // initializes search
    var searchFunc = function() {
      var query = $.trim($('#search-form #query').val()); 
      if (query.length > 0) {
        me.search(query); 
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

Browser.prototype.setMode = function(mode) {
  var me = this;
  this.mode = mode;
  if (mode == "all") {
    $(".browser-tabs #browser-mystuff .show-all").hide();
  } else if (mode == "search") {
    $(".browser-tabs #browser-mystuff .show-all").show();
    $(".browser-tabs #browser-mystuff .show-all #show-all-button").click(function() {
      me.showAll();
      return false;
    });
  }
}

Browser.prototype.setDatasetAll = function(datasetList) {
  this.datasetAll = datasetList;
}

Browser.prototype.setApplicationAll = function(applicationList) {
  this.applicationAll = applicationList;
}


Browser.prototype.getDatasetList = function() {
  var me = this;
  $(function() {
    $.ajax({
      url : ctx + "/main/datastore",
      dataType : "json",
      success : function(json) {
        me.setDatasetAll(json);
        me.loadItems(json, "dataset");
      },
      error : function() {
        console.debug('Failed to load list of dataset');
      }
    });
  });
}

Browser.prototype.getApplicationList = function() {
  var me = this;
  $(function() {
    $.ajax({
      url: ctx + '/main/application', 
      dataType: "json", 
      success: function(json) {
        me.setApplicationAll(json);
        me.loadItems(json, "application");
      }, 
      error: function() {
        console.debug('Failed to load list of application');
      }
    });
  });
}

Browser.prototype.loadItems = function(itemList, type) {
  var i;
  var json = itemList;
  var $container;
  if (type == "application") $container = $('#application-list');
  else if (type == 'dataset') $container = $('#dataset-list');
  else return;
  
  $container.empty();
  for (i = 0; i < json.length; i++) {
    var itemObj = json[i];
    var $item = $('<div class="browser-item"></div>');
    $item.addClass(type + "-item");
    
    var $itemHeader = $('<div class="browser-item-header"></div>');
    var link;
    if (type == "application") { 
      link = plfdemo.Main.ctx + "/app/" + encodeURI(itemObj['name']) + "/#app" 
    } else link = "#";
    
    $itemHeader.append('<a href="#"><span class="browser-item-title"><strong>' + itemObj['name'] + '</strong></span></a>');
    var $itemFooter = $('<div class="browser-item-footer"></div>');
    var $expandLink = $('<a class="browser-item-footer-link expand-link" href="javascript:void(0);">Expand</a>');
    $itemFooter.append($expandLink);
    $itemFooter.append('<a class="browser-item-footer-link browser-item-action" href="' + link + '">Open</a>');
    $itemHeader.append($itemFooter);
    $item.append($itemHeader);
    
    var $itemDetail = $('<div class="browser-item-detail"></div>');
    $itemDetail.addClass(type + "-item-detail");
    var $description = $('<div class="browser-item-description"><strong>Description: </strong></div>');
    $description.addClass(type + "-item-description");
    $description.append('<span>' + itemObj['description'] + '</span>');
    $itemDetail.append($description);
    
    /*if (type == "datastore") {
      var $datasetList = $('<div class="datastore-item-datasets"><strong>Datasets: </strong></div>');
      var dsList = itemObj['datasets'];
      var j;
      for (j in dsList) {
        var ds = dsList[j];
        var $dsItem = $('<a href="#"><span class="datastore-dataset-title">' + ds['name'] + '</span></a>');
        $datasetList.append($dsItem);
      }
      $itemDetail.append($datasetList);
    } else */
    if (type == "application") {
      $itemDetail.append('<div class="application-language"><strong>Language: </strong>' + itemObj['language'] + '</div>');
    } else if (type == "dataset") {
      //$itemDetail.append('<div class="dataset-detail-datastore"><strong>Datastore: </strong>' + itemObj['datastore'] + '</div>');
      $itemDetail.append('<div class="dataset-detail-schema"><strong>Schema: </strong>' + itemObj['schema']  + '</div>');   
    }
    $item.append($itemDetail);
    
    $item.load().appendTo($container);
    
    var expandFunc = function($detail, $expand) {
      return function(e) {
        if ((e.target.nodeName == 'a' || e.target.nodeName == 'A') && !$(e.target).hasClass('expand-link')) return;
        $detail.toggle('fast');
        if($expand.text() == 'Collapse') $expand.text('Expand');
        else if ($expand.text() == 'Expand') $expand.text('Collapse');
        return false;
      }
    };
    $itemHeader.click(expandFunc($itemDetail, $expandLink));
    $expandLink.click(expandFunc($itemDetail, $expandLink));
    $itemDetail.hide();
  }
}

Browser.prototype.showAll = function() {
  this.setMode("all");
  this.loadItems(this.datasetAll, 'dataset');
  this.loadItems(this.applicationAll, 'application');
}

/**
 * 
 */
Browser.prototype.search = function(query) {
  var me = this;
  $(function() {
    $.ajax({
      url: plfdemo.Main.ctx + '/main/search',
      data: {
        query: query
      },
      success: function(json) {
        var apps = json['applications'];
        var datasets = json['datasets'];
        me.setMode("search");
        me.loadItems(datasets, "dataset");
        me.loadItems(apps, "application");
      }, 
      error: function() {
        console.debug("Failed to execute search request.");
      }
    });
  });
}

plfdemo.Browser = new Browser();
plfdemo.Browser.init();