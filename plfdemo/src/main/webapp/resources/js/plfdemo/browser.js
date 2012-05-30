function Browser() {
  this.mode = "all";
}

Browser.prototype.setMode = function(mode) {
  this.mode = mode;
  if (mode == "all") {
    $(".browser-tabs #browser-all .show-all").hide();
    $(".dataset-list-panel").hide();
  } else if (mode == "search") {
    $(".dataset-list-panel").show();
    $(".browser-tabs #browser-all .show-all").show();
    $(".browser-tabs #browser-all .show-all #show-all-button").click(function() {
      plfdemo.Browser.showAll();
      return false;
    });
  }
}

Browser.prototype.setDatastoreAll = function(datastoreList) {
  this.datastoreAll = datastoreList;
}

Browser.prototype.setApplicationAll = function(applicationList) {
  this.applicationAll = applicationList;
}

Browser.prototype.loadItems = function(itemList, type) {
  var i;
  var json = itemList;
  var container;
  if (type == "application") container = $('#application-list');
  else if (type == "datastore") container = $('#datastore-list');
  else if (type == 'dataset') container = $('#dataset-list');
  else return;
  
  container.empty();
  for (i = 0; i < json.length; i++) {
    var itemObj = json[i];
    var item = $('<div class="browser-item"></div>');
    item.addClass(type + "-item");
    
    var itemHeader = $('<div class="browser-item-header"></div>');
    var link;
    if (type == "application") { 
      link = plfdemo.Main.ctx + "/app/" + encodeURI(itemObj['name']) + "/#app" 
    } else link = "#";
    
    itemHeader.append('<a href="#"><span class="browser-item-title"><strong>' + itemObj['name'] + '</strong></span></a>');
    var itemFooter = $('<div class="browser-item-footer"></div>');
    var expandLink = $('<a class="browser-item-footer-link expand-link" href="javascript:void(0);">Expand</a>');
    itemFooter.append(expandLink);
    itemFooter.append('<a class="browser-item-footer-link browser-item-action" href="' + link + '">Open</a>');
    itemHeader.append(itemFooter);
    item.append(itemHeader);
    
    var itemDetail = $('<div class="browser-item-detail"></div>');
    itemDetail.addClass(type + "-item-detail");
    var description = $('<div class="browser-item-description"><strong>Description: </strong></div>');
    description.addClass(type + "-item-description");
    description.append('<span>' + itemObj['description'] + '</span>');
    itemDetail.append(description);
    
    if (type == "datastore") {
      var datasetList = $('<div class="datastore-item-datasets"><strong>Datasets: </strong></div>');
      var dsList = itemObj['datasets'];
      var j;
      for (j in dsList) {
        var ds = dsList[j];
        var dsItem = $('<a href="#"><span class="datastore-dataset-title">' + ds['name'] + '</span></a>');
        datasetList.append(dsItem);
      }
      itemDetail.append(datasetList);
    } else if (type == "application") {
      itemDetail.append('<div class="application-language"><strong>Language: </strong>' + itemObj['language'] + '</div>');
    } else if (type == "dataset") {
      itemDetail.append('<div class="dataset-detail-datastore"><strong>Datastore: </strong>' + itemObj['datastore'] + '</div>');
      itemDetail.append('<div class="dataset-detail-schema"><strong>Field list: </strong>' + itemObj['fieldList']  + '</div>');   
    }
    item.append(itemDetail);
    
    item.load().appendTo(container);
    
    var expandFunc = function(detail, expand) {
      return function(e) {
        if ((e.target.nodeName == 'a' || e.target.nodeName == 'A') && !$(e.target).hasClass('expand-link')) return;
        detail.toggle('fast');
        if(expand.text() == 'Collapse') expand.text('Expand');
        else if (expand.text() == 'Expand') expand.text('Collapse');
        return false;
      }
    };
    itemHeader.click(expandFunc(itemDetail, expandLink));
    expandLink.click(expandFunc(itemDetail, expandLink));
    itemDetail.hide();
  }
}

Browser.prototype.showAll = function() {
  this.setMode("all");
  this.loadItems(this.datastoreAll, 'datastore');
  this.loadItems(this.applicationAll, 'application');
}

plfdemo.Browser = new Browser();