function Browser() {
  this.mode = "all";
  this.myDatasets = {};
  this.myApplications = {};
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
    
    $('#browser-tabs .dataset-list-panel h4').click(function() {
      if (!$(this).next().is(':empty')) {
        $(this).next().toggle('fast');
      }
      return false;
    });
    
    $('#browser-tabs .application-list-panel h4').click(function() {
      if (!$(this).next().is(':empty')) {
        $(this).next().toggle('fast');
      }
      return false;
    })
    
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

Browser.prototype.setMyDatasets = function(datasetList) {
  //this.myDatasets = datasetList;
  for (index in datasetList) {
    var item = datasetList[index];
    this.myDatasets[item.guid] = item;
  }
}

Browser.prototype.setMyApplications = function(applicationList) {
  //this.myApplications = applicationList;
  for (index in applicationList) {
    var item = applicationList[index];
    this.myApplications[item.guid] = item;
  }
}


Browser.prototype.getDatasetList = function() {
  var me = this;
  $(function() {
    $.ajax({
      url : ctx + "/main/dataset",
      dataType : "json",
      success : function(json) {
        me.setMyDatasets(json);
        me.loadItems(me.myDatasets, "dataset");
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
        me.setMyApplications(json);
        me.loadItems(me.myApplications, "application");
      }, 
      error: function() {
        console.debug('Failed to load list of application');
      }
    });
  });
}

Browser.prototype.loadItems = function(itemMap, type) {
  var $container;
  if (type == "application") $container = $('#application-list');
  else if (type == 'dataset') $container = $('#dataset-list');
  else return;
  
  $container.empty();
  for (key in itemMap) {
    var itemObj = itemMap[key];
    var $item = $('<div class="browser-item"></div>');
    $item.addClass(type + "-item");
    
    var $itemHeader = $('<div class="browser-item-header"></div>');
    
    $itemHeader.append('<a href="#"><span class="browser-item-title"><strong>' + itemObj['name'] + '</strong></span></a>');
    var $itemFooter = $('<div class="browser-item-footer"></div>');
    var $expandLink = $('<a class="browser-item-footer-link expand-link" href="javascript:void(0);">Expand</a>');
    $itemFooter.append($expandLink);
    var $openLink = $('<a class="browser-item-footer-link browser-item-action" href="javascript:void(0)">Open</a>');
    $itemFooter.append($openLink);
    $itemHeader.append($itemFooter);
    $item.append($itemHeader);
    
    var $itemDetail = $('<div class="browser-item-detail"></div>');
    $itemDetail.addClass(type + "-item-detail");
    var $description = $('<div class="browser-item-description browser-item-info"><strong>Description: </strong></div>');
    $description.addClass(type + "-item-description");
    $description.append('<span>' + itemObj['description'] + '</span>');
    $itemDetail.append($description);
    
    if (type == "application") {
      $itemDetail.append('<div class="browser-item-info application-language"><strong>Language: </strong>' + itemObj['language'] + '</div>');
      $itemDetail.append('<div class="browser-item-info"><strong>Author: </strong>' + itemObj['authorName'] + '</div>');
      $itemDetail.append('<div class="browser-item-info"><strong>Line count: </strong>' + itemObj['lineCount'] + '</div>');
      $itemDetail.append('<div class="browser-item-info"><strong>Is public: </strong>' + itemObj['published'] + '</div>');
      $itemDetail.append('<div class="browser-item-info"><strong>Create date: </strong>' + new Date(itemObj['createAt']) + '</div>');
      $itemDetail.append('<div class="browser-item-info"><strong>Last update: </strong>' + new Date(itemObj['lastUpdate']) + '</div>');
      $itemDetail.append('<div class="browser-item-info browser-item-tags"><strong>Tags: </strong>' + itemObj['tags'] + '</div>');
    } else if (type == "dataset") {
      //$itemDetail.append('<div class="dataset-detail-datastore"><strong>Datastore: </strong>' + itemObj['datastore'] + '</div>');
      $itemDetail.append('<div class="browser-item-info dataset-detail-schema"><strong>Schema: </strong>' + itemObj['schema']  + '</div>');
      $itemDetail.append('<div class="browser-item-info dataset-detail-author"><strong>Author: </strong>' + itemObj['authorName'] + '</div>');
      $itemDetail.append('<div class="browser-item-info dataset-detail-rowcount"><strong>Row count: </strong>' + itemObj['rowCount'] + '</div>');
      $itemDetail.append('<div class="browser-item-info"><strong>Create date: </strong>' + new Date(itemObj['createAt']) + '</div>');
      $itemDetail.append('<div class="browser-item-info"><strong>Last update: </strong>' + new Date(itemObj['lastUpdate']) + '</div>');
      $itemDetail.append('<div class="browser-item-info browser-item-tags"><strong>Tags: </strong>' + itemObj['tags']?itemObj['tags']:'' + '</div>');    
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
    
    $openLink.click(function(itemObj, type) {
      return function() {
        var workspace = com.bouncingdata.Workspace;
        // check if this action about to open an existing app. or blank tab for new app.
        if (itemObj && (itemObj.guid in workspace.tabsInfo)) {
          workspace.$tabs.tabs('select', '#' + workspace.tabsInfo[itemObj.guid].tabId);
          return false;
        }
        if (type == "application") {
          workspace.createTab(itemObj);          
        } else if (type == "dataset") {
          workspace.createTab(itemObj, null, 'dataset');
        }
        return false;
      }
    }(itemObj, type));
  }
}

Browser.prototype.showAll = function() {
  this.setMode("all");
  this.loadItems(this.myDatasets, 'dataset');
  this.loadItems(this.myApplications, 'application');
}

Browser.prototype.refreshMyStuff = function() {
  this.getApplicationList();
  this.getDatasetList();
}

/**
 * 
 */
Browser.prototype.search = function(query) {
  var me = this;
  $(function() {
    $.ajax({
      url: ctx + '/main/search',
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

com.bouncingdata.Browser = new Browser();
com.bouncingdata.Browser.init();