function Browser() {
  this.mode = "all";
  this.myDatasets = {};
  this.myApplications = {};
  this.myStuff = {};
}

Browser.prototype.init = function() {
  var me = this;
  $(function() {
    // init tabs
    me.$tabs = $("#browser-tabs" ).tabs();
    
    me.$itemTemplate = $('#browser-item-template').template();
    
    // load datasets, applications in "My stuff" tab
    //me.getDatasetList();
    //me.getApplicationList();
    me.getMyStuff();
    me.setMode("all");
    
    $('#browser-tabs .dataset-list-panel h4').click(function() {
      if (!$(this).next().is(':empty')) {
        $(this).next().toggle('fast');
      }
      return false;
    });
    
    $('#browser-tabs .analysis-list-panel h4').click(function() {
      if (!$(this).next().is(':empty')) {
        $(this).next().toggle('fast');
      }
      return false;
    })
    
    $('#browser-tabs .scraper-list-panel h4').click(function() {
      if (!$(this).next().is(':empty')) {
        $(this).next().toggle('fast');
      }
      return false;
    });
    
    // initializes search
    $('#workbench-browser #search-form .search-criteria').change(function() {
      var value = $(this).val();
      if (value == 'global') {
        $(this).prev().prop('placeholder', 'Search global stuff...');
      } else if (value == 'mystuff') {
        $(this).prev().prop('placeholder', 'Search my stuff...');
      }
    });
    
    var searchFunc = function() {
      var query = $.trim($('#workbench-browser #search-form #query').val());
      var criteria = $('#workbench-browser #search-form .search-criteria').val();
      if (query.length > 0) {
        me.search(query, criteria); 
      }
    }
    $('#workbench-browser #search-form #query').keypress(function(e) {
      var code = (e.keyCode ? e.keyCode : e.which);
      if(code == 13) {
        searchFunc();
        return false;
      } else return true;
    });
    $('#workbench-browser #search-form #search-submit').click(function() {
      searchFunc();
      return false;
    });
  });
}

Browser.prototype.setMode = function(mode) {
  var me = this;
  this.mode = mode;
  if (mode == "all") {
    $("#browser-tabs #browser-mystuff .show-all").hide();
  } else if (mode == "search") {
    $("#browser-tabs #browser-mystuff .show-all").show();
    $("#browser-tabs #browser-mystuff .show-all #show-all-button").click(function() {
      me.showAll();
      return false;
    });
  }
}

/**
 * @deprecated
 */
Browser.prototype.setMyDatasets = function(datasetList) {
  //this.myDatasets = datasetList;
  for (index in datasetList) {
    var item = datasetList[index];
    this.myDatasets[item.guid] = item;
  }
}
/**
 * @deprecated
 */
Browser.prototype.setMyApplications = function(applicationList) {
  //this.myApplications = applicationList;
  for (index in applicationList) {
    var item = applicationList[index];
    this.myApplications[item.guid] = item;
  }
}

/**
 * @deprecated
 */
Browser.prototype.getDatasetList = function() {
  var me = this;
  $(function() {
    $.ajax({
      url : ctx + "/main/dataset",
      dataType : "json",
      success : function(json) {
        me.setMyDatasets(json);
        me.loadMyStuff(me.myDatasets, "dataset");
      },
      error : function() {
        console.debug('Failed to load list of dataset');
      }
    });
  });
}

/**
 * @deprecated
 */
Browser.prototype.getApplicationList = function() {
  var me = this;
  $(function() {
    $.ajax({
      url: ctx + '/main/application', 
      dataType: "json", 
      success: function(json) {
        me.setMyApplications(json);
        me.loadMyStuff(me.myApplications, "application");
      }, 
      error: function() {
        console.debug('Failed to load list of application');
      }
    });
  });
}


Browser.prototype.getMyStuff = function() {
  var me = this;
  var $mystuffOverlay = $('<div class="mystuff-overlay" style="position: absolute; top: 0; bottom: 0; left: 0; right: 0;"></div>');
  $mystuffOverlay.css('background', 'url("' + ctx + '/resources/images/ajax-loader.gif") no-repeat 50% 10% #eee')
    .css('z-index', 10).css('background-size', '30px 30px').css('opacity', '0.8');
  $('#browser-mystuff').append($mystuffOverlay);
  $.ajax({
    url: ctx + '/main/mystuff',
    dataType: 'json',
    success: function(result) {
      $mystuffOverlay.remove();
      me.setMyStuff(result);
      me.loadMyStuff(result);
    },
    error: function() {
      console.debug('Failed to load my stuff.');
    }
  });
}

Browser.prototype.setMyStuff = function(stuffs) {  
  for (stuffName in stuffs) {
    this.myStuff[stuffName] = {};
    
    for (index in stuffs[stuffName]) {
      var item = stuffs[stuffName][index];
      this.myStuff[stuffName][item.guid] = item;
    }
  }
}

Browser.prototype.loadMyStuff = function(stuffs) {
  this.loadStuff(stuffs['analyses'], 'analysis', $('#browser-tabs #browser-mystuff #analysis-list'));
  this.loadStuff(stuffs['datasets'], 'dataset', $('#browser-tabs #browser-mystuff #dataset-list'));
  this.loadStuff(stuffs['scrapers'], 'scraper', $('#browser-tabs #browser-mystuff #scraper-list'));
}

Browser.prototype.loadStuff = function(stuffs, type, $container) {
  $container.empty();
  for (key in stuffs) {
    var itemObj = stuffs[key];
    var $item;
    if (type == "scraper" || type == "analysis") {
      $item = $.tmpl(this.$itemTemplate, {
        title: itemObj.name,
        description: itemObj.description,
        author: itemObj['user'].username,
        lineCount: itemObj.lineCount,
        'public': itemObj['published'],
        createDate: new Date(itemObj['createAt']),
        lastUpdate: new Date(itemObj['lastUpdate']),
        tags: itemObj['tags']?itemObj['tags']:'',
        language: itemObj['language']
      });
    } else if (type == "dataset") {
      $item = $.tmpl(this.$itemTemplate, {
        title: itemObj.name,
        description: itemObj.description,
        author: itemObj['user'].username,
        lineCount: null,
        'public': itemObj['public'],
        createDate: new Date(itemObj['createAt']),
        lastUpdate: new Date(itemObj['lastUpdate']),
        tags: itemObj['tags']?itemObj['tags']:''
      });
      
      $('.application-language', $item).remove();
      $('.line-count', $item).replaceWith('<div class="browser-item-info row-count"><strong>Row count: </strong>' + itemObj.rowCount + '</div>')
      $('.row-count', $item).after('<div class="browser-item-info dataset-schema"><strong>Schema: </strong>' + itemObj.schema + '</div>') ;
    
      if (itemObj.scraper) {
        $('.browser-item-header', $item).append('<div class="source-scraper">by <a href="#">' + itemObj.scraper.name + '</a></div>');
      }
      
      var $viewLink = $('<a class="browser-item-footer-link view-link" href="' + ctx + '/dataset/view/' + itemObj.guid + '">View</a>');
      $('.browser-item-footer', $item).append($viewLink);
      $viewLink.click(function(lnk) {
        return function() {
          com.bouncingdata.Nav.fireAjaxLoad(lnk, false);
          return false;
        }
      }($viewLink.prop('href')));
      
    } else return;
        
    $item.load().appendTo($container);
       
    // open new tab when clicking on 'open' link
    var $openLink = $('.browser-item-footer-link.open-link', $item);
    $openLink.click(function(itemObj, type) {
      return function() {
        var workbench = com.bouncingdata.Workbench;
        // check if this action about to open an existing app. or blank tab for new app.
        if (itemObj && (itemObj.guid in workbench.tabsInfo)) {
          workbench.$tabs.tabs('select', '#' + workbench.tabsInfo[itemObj.guid].tabId);
          return false;
        }
        if (type == "analysis" || type == "scraper") {
          workbench.createTab(itemObj, null, type);          
        } else if (type == "dataset") {
          workbench.createTab(itemObj, null, 'dataset');
        }
        return false;
      }
    }(itemObj, type));
  }
  
   
  var expandFunc = function($detail, $expand) {
    $detail.toggle('fast');
    if($expand.text() == 'Collapse') $expand.text('Expand');
    else if ($expand.text() == 'Expand') $expand.text('Collapse');
  };
  
  $('.browser-item-header', $container).click(function() {
    var $expandLink = $('.browser-item-footer-link.expand-link', $(this).next());
    var $itemDetail = $(this).next().next();
    expandFunc($itemDetail, $expandLink);
  });
  
  $('.browser-item-footer-link.expand-link', $container).click(function() {
    var $expandLink = $('.browser-item-footer-link.expand-link', $(this).next());
    var $itemDetail = $(this).next().next();
    expandFunc($itemDetail, $expandLink);
  });
  
  $('.browser-item-detail', $container).hide();
    
}

Browser.prototype.renderSearchResult = function(results, query) {
  var $resultTab = $('div#browser-search-result', this.$tabs);
  if ($resultTab.length == 0) {
    var size = this.$tabs.tabs("length");
    if (size > 1) {
      for (i = 1; i < size; i++) {
        this.$tabs.tabs("remove", 1);
      }
    }
    this.$tabs.tabs('add', '#browser-search-result', "Search result")
    $resultTab = $('div#browser-search-result', this.$tabs);
  }  
  
  this.$tabs.tabs('select', 1);
  
  // render search result
  $resultTab.empty();
  var $analyses = $('<div class="search-result-analyses-panel"><h4>Analyses</h4><div class="search-result-analyses"></div></div>');
  var $datasets = $('<div class="search-result-datasets-panel"><h4>Datasets</h4><div class="search-result-datasets"></div></div>');
  var $scrapers = $('<div class="search-result-scrapers-panel"><h4>Scrapers</h4><div class="search-result-scrapers"></div></div>');
  $resultTab.append('<div style="margin: 5px 0;">All results for &ldquo;<b>' + query + '</b>&rdquo;</div>')
    .append($datasets).append('<div class="browser-separator"></div>')
    .append($analyses).append('<div class="browser-separator"></div>')
    .append($scrapers);
  this.loadStuff(results['analyses'], 'analysis', $('.search-result-analyses', $analyses));
  this.loadStuff(results['datasets'], 'dataset', $('.search-result-datasets', $datasets));
  this.loadStuff(results['scrapers'], 'scraper', $('.search-result-scrapers', $scrapers)); 
}

Browser.prototype.getStuff = function(guid, type) {
  switch (type) {
  case "analysis":
    return this.myStuff['analyses'][guid];
  case "scraper":
    return this.myStuff['scrapers'][guid];
  case 'dataset':
    return this.myStuff['datasets'][guid];
  default:
    return null;
  }
}

Browser.prototype.showAll = function() {
  this.setMode("all");
  this.loadMyStuff(this.myStuff);
}

Browser.prototype.refreshMyStuff = function() {
  this.getMyStuff();
}

/**
 * 
 */
Browser.prototype.search = function(query, criteria) {
  var me = this;
  $(function() {
    $.ajax({
      url: ctx + '/main/browsersearch',
      data: {
        query: query,
        criteria: criteria
      },
      success: function(json) {
        if (criteria == "mystuff") {
          me.setMode("search");
          me.loadStuff(json['analyses'], 'analysis', $('#browser-mystuff #analysis-list', me.$tabs));
          me.loadStuff(json['datasets'], 'dataset', $('#browser-mystuff #dataset-list', me.$tabs));
          me.loadStuff(json['scrapers'], 'scraper', $('#browser-mystuff #scraper-list', me.$tabs));
        } else if (criteria == "global") {
          console.debug("Received global search results.");
          me.renderSearchResult(json, query);
        }
      }, 
      error: function() {
        console.debug("Failed to execute search request.");
      }
    });
  });
}

com.bouncingdata.Browser = new Browser();