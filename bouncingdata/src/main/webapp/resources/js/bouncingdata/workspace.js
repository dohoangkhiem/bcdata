function Workspace() {
  //the tabs counter, always increase, = the number of tabs which was created
  this.tabsCounter = 0;
  
  // current opening applications (only saved apps), key: app guid, value: tab info { tabId, app info, type }
  this.tabsInfo = {};
  
  // key: index (0, 1, 2, ...), value: app guid, jqconsole, if the tab belongs to new app., value = null 
  this.tabsIndex = [];
  
  // current working app 
  this.currentApp = {};
  
  this.$tabs = {};
  this.$tabTemplate = {};
  this.$dsTemplate = {};
  
  // use to name untitled tabs
  this.untitledCounter = 0;
}

Workspace.prototype.init = function() {
  var me = this;
  $(function() {
    
    //Init popup dialog
    $('.workspace-container #new-app-dialog').dialog({
      autoOpen: false,
      height: 345,
      width: 460,
      modal: true,
      resizable: false,
      buttons: {
        "Save": function() {
          //
          console.info('Creating app.');
          me.createApp($('#new-app-name', $(this)).val(), $('#new-app-language', $(this)).val(), 
              $('#new-app-description', $(this)).val(), me.getCode(me.getSelectedTabContainer()), 
              $('#new-app-public', $(this)).val(), $('#new-app-tags', $(this)).val());
          $(this).dialog("close");
        }, 
        "Cancel": function() {
          $(this).dialog("close");
        }
      },
      close: function() {     
      }
    });
    
    // get the tab template
    me.$tabTemplate = $('#workspace-content-template').template();
    me.$dsTemplate = $('#data-view-template').template();
    
    // initialize tabs
    me.$tabs = $('.workspace-container #workspace-main-tabs').tabs({
      tabTemplate: "<li class='tab-header workspace-tab-header'><a href='#{href}'>#{label}</a> <span class='ui-icon ui-icon-close' title='Close this tab'>Remove Tab</span></li>",
      
      add: function(event, ui) {
        // trick here: select before finish the adding, by this way the layout achieves the full width
        var index = ui.index;
        me.tabsIndex[index].loaded = false;
        me.$tabs.tabs('select', index);
            
        var guid = me.tabsIndex[index].guid;
        if (me.tabsIndex[index].type == 'app') {
          var app = null;
          if (guid) app = me.tabsInfo[guid].app;
          var $tabContent = me.makeTabContent(ui.panel.id, app, 'app');
          $(ui.panel).append($tabContent).addClass('workspace-tab-panel');
          
          // complete UI & functionalities for this tab
          me.processTab(index, $tabContent);
        } else if (me.tabsIndex[index].type == 'dataset') {
          var dataset = me.tabsInfo[guid].dataset;
          var $tabContent = me.makeTabContent(ui.panel.id, dataset, 'dataset');
          $(ui.panel).append($tabContent).addClass('dataset-tab-panel');
          me.processTab(index, $tabContent);
        }
        
      }, 
      
      show: function(event, ui) {
        // need this check as we have a trick in add() function above
        if (me.tabsIndex[ui.index].loaded) {
          var type = me.tabsIndex[ui.index].type;
          if (type == 'app') {
            var $layout = $('.workspace-content-layout', $(ui.panel)).layout();
            if ($layout) $layout.resizeAll();
          } else if (type == 'dataset') {
            var $layout = $('.dataset-view-layout', $(ui.panel)).layout();
            if ($layout) $layout.resizeAll();
          }
        }  
      }

    });
    
    // handles double click on tab bar
    $('#workspace-main-tabs .workspace-main-tabs-bar').dblclick(function(e) {
      var $target = $(e.target);
      if ($target[0].nodeName == 'a' || $target[0].nodeName == 'A') {
        $target = $target.parent();
      }
      if ($target.hasClass('workspace-tab-header')) return false;
      me.createTab(null);
      return false;
    });
    
    // handles tab closing
    $(".tab-header span.ui-icon-close", me.$tabs).live("click", function() {
      var index = $("li", me.$tabs).index($(this).parent());
      me.removeTab(index);
      return false;
    });
        
    // create empty tab
    me.createTab(null);
    
    // test
    $('.workspace-container').click(function(e) {     
      var $target = $(e.target);
      if (($target[0].nodeName == "input" || $target[0].nodeName == "INPUT") && $target.hasClass("app-action")) {       
        // determine tab
        var $tab = $target.parents('.workspace-tab-panel');
        // execute
        var tabId = $tab[0].id;
        var index = me.getTabIndex(tabId);
        if ($target[0].id == "run-app") {
          me.execute(index);
        } else if ($target[0].id == "save-app") {
          me.saveCode(index);
        }
      }
    });
             
  });
}

/**
 * Create a new tab to view application workspace or dataset
 */
Workspace.prototype.createTab = function(obj, tabName, type) {
  if (type == 'dataset') return this.openDataset(obj, tabName);
  else return this.openApp(obj, tabName);
} 

Workspace.prototype.openApp = function(app, tabName) {
  var index = this.getNumberOfTabs();
  this.tabsIndex[index] = {type: 'app'};
  if (!app) {
    this.tabsCounter++;
    this.untitledCounter++;
    this.currentApp = null;
    this.tabsIndex[index].guid = null;
    if (!tabName) tabName = 'Untitled' + this.untitledCounter;
    this.$tabs.tabs('add', '#tabs-' + this.tabsCounter, tabName);
  } else {
    this.currentApp = app;
    this.tabsCounter++;
    this.tabsIndex[index].guid = app.guid;
    this.tabsInfo[app.guid] = { tabId: 'tabs-' + this.tabsCounter, app: app};
    if (!tabName) tabName = app.name;
    this.$tabs.tabs('add', '#tabs-' + this.tabsCounter, tabName);
  }
  
  return index;
}

/**
 * 
 */
Workspace.prototype.openDataset = function(dataset, tabName) {
  var index = this.getNumberOfTabs();
  this.tabsIndex[index] = {type: 'dataset'};
  
  if (!dataset) {
    return null;
  }
  this.tabsCounter++;
  this.tabsIndex[index].guid = dataset.guid;
  this.tabsInfo[dataset.guid] = { tabId: 'tabs-' + this.tabsCounter, dataset: dataset};
  if (!tabName) tabName = dataset.name;
  this.$tabs.tabs('add', '#tabs-' + this.tabsCounter, tabName);
  return index;
}

/**
 * Removes a tab with given index (zero-based)
 */
Workspace.prototype.removeTab = function(index) {
  if (this.getNumberOfTabs() == 1) {
    return;
  }
  var guid = this.tabsIndex[index].guid;
  this.tabsIndex.splice(index, 1);
  //delete this.sessions_[this.getTabId(index)];
  if (guid) {
    delete this.tabsInfo[guid];
  }
  
  this.$tabs.tabs("remove", index);
}

/**
 * Callback function to be called after the main layout was resized.
 */
Workspace.prototype.resizeAll = function() {
  // do resize the current layout  
  var $tab = this.getSelectedTabContainer();
  var index = this.getSelectedIndex();
  var type = this.tabsIndex[index].type;
  if (type == 'app') {
    var $layout = $('.workspace-content-layout', $tab).layout();
    if ($layout) $layout.resizeAll();
  } else if (type == 'dataset') {
    var $layout = $('.dataset-view-layout', $tab).layout();
    if ($layout) $layout.resizeAll();
  }
}

/**
 * Builds tab content from the template for particular application or dataset 
 */
Workspace.prototype.makeTabContent = function(tabId, obj, type) {
  type = type || 'app';
  if (type == 'app') {
    var tabObj = {
        tabId: tabId,
        appName: obj?obj.name:'',
        appLang: obj?obj.language:'',
        appAuthor: obj?obj.authorName:'',
        guid: obj?obj.guid:''
    }
    return $.tmpl(this.$tabTemplate, tabObj);
  } else if (type == 'dataset') {
    var tabObj = {
        tabId: tabId,
        dsName: obj?obj.name:'',
        dsAuthor: obj?obj.authorName:'',
        dsSchema: obj?obj.schema:'',
        dsRowCount: obj?obj.rowCount:'',
        dsCreateDate: obj?new Date(obj.createAt):'',
        dsLastUpdate: obj?new Date(obj.lastUpdate):'',
        dsTags: obj?obj.tags:'',
        guid: obj?obj.guid:''
    };
    return $.tmpl(this.$dsTemplate, tabObj)
  } 
  
}

/**
 * Some magic works to make the tab content appears & functions correctly
 */
Workspace.prototype.processTab = function(tabIndex, $tabContent) {
  
  var type = this.tabsIndex[tabIndex].type;
  var tabId = $tabContent.parent()[0].id;
  var guid = this.tabsIndex[tabIndex].guid;
  var me = this;
  
  //buttons
  $('input:button', $tabContent).button();
  $('input:submit', $tabContent).button();
  
  if (type == 'dataset') {
    
    // initializes jQuery Layout
    var $layoutContainer = $('#dataset-view-layout-' + tabId, $tabContent);
    var $layout = $layoutContainer.layout({
      center__paneSelector: '#dataset-view-center-' + tabId,
      east__paneSelector: '#dataset-view-east-' + tabId,
      east__size: 300,
      applyDefaultStyles: true
    });
    
    var dataset = this.tabsInfo[guid].dataset;
    var $table = $('table.dataset-table', $tabContent)
    
    // retrieve dataset content
    me.setStatus($tabContent, "running");
    console.debug("Retrieving dataset content...");
    var $queryEditor = $('.query-editor', $tabContent);
    $queryEditor.val('SELECT * FROM `' + dataset.name + '`');
    $.ajax({
      url: ctx + '/dataset/' + guid,
      dataType: 'json',
      success: function(result) {
        me.renderDatatable(result, $table);
        me.setStatus($tabContent, "");
      }, 
      error: function(result) {
        console.debug(result);
        me.setStatus($tabContent, "error");
      }
    });
    
    // 
    var queryFunction = function() {
      var sql = $queryEditor.val();
      if (!sql) return;
      me.setStatus($tabContent, "running");
      $.ajax({
        url: ctx + '/dataset/query',
        dataType: 'json',
        data: {
          guid: guid,
          query: sql
        },
        type: 'post',
        success: function(result) {
          if (result['statusCode'] >= 0) {
            // show datatable
            me.setStatus($tabContent, "finished-running");
            var data = result['data'];
            me.renderDatatable(data, $table);
          } else {
            // print error message
            me.setStatus($tabContent, "error");
            console.debug(result);
          }
        },
        error: function(result) {
          me.setStatus($tabContent, "error");
          console.debug(result);
        }
      });
    }
    
    $('input.dataset-query-execute', $tabContent).click(function() {
      queryFunction();
      return false;
    });
    
    this.tabsIndex[tabIndex].loaded = true;
    return;
  }
  
  // init. tabs
  $('.app-execution-logs-tabs', $tabContent).tabs();
  $('.app-output-tabs', $tabContent).tabs();

  // init console
  var jqConsole = $('#app-execution-logs-' + tabId + ' .console', $tabContent).jqconsole('Welcome to our console\n', Utils.getConsoleCaret('python'));
  this.tabsIndex[tabIndex].jqConsole = jqConsole;
  this.startPrompt(jqConsole, 'python');
  
  // init app. actions
  var app;
  if (guid) {
    $('.app-info', $tabContent).show();
    $('.new-app-info', $tabContent).hide();
    app = this.tabsInfo[guid].app;
    
    if (app && (app.authorName != com.bouncingdata.Main.username)) {
      $('.app-actions #save-app', $tabContent).hide();
      $('.app-actions #run-app', $tabContent).hide();
      $('.app-actions #copy-app', $tabContent).show();
    }
  } else {
    $('.app-info', $tabContent).hide();
    $('.new-app-info', $tabContent).show();
    $('.app-actions #copy-app', $tabContent).hide();
  }
  
  // initializes resizable layout
  var $layoutContainer = $('#workspace-content-layout-' + tabId, $tabContent);
  var $layout = $layoutContainer.layout({
    center__paneSelector: '#workspace-content-center-' + tabId,
    east__paneSelector: '#workspace-content-east-' + tabId,
    east__size: 480,
    applyDefaultStyles: true
  });
  
  
  // Retrieve app. details
  if (app) {
    $(function() {
      me.setStatus($tabContent.parent(), "loading");
      console.info('Retrieving application details...');
      $.ajax({
        url: ctx + "/app/" + app['guid'],
        success: function(result) {
          console.debug(result);
          me.setCode(result.code, $tabContent)
          me.renderOutput(result['datasets'], result['visualizations'], $tabContent, app);        
          me.setStatus($tabContent.parent(), "");
        },
        error: function(msg) {
          console.debug(msg);
          me.setStatus($tabContent.parent(), "error");
        }
      });
    });
  }
  
  /*// Action handlers
  $('.app-actions #run-app', $tabContent).click(function() {
    var index = me.getSelectedIndex();
    me.execute(index);
  }); 
  
  $('.app-actions #save-app', $tabContent).click(function() {
    var index = me.getSelectedIndex();
    me.saveCode(index);
  });*/
  
  $('.app-actions #copy-app', $tabContent).click(function() {
    var code = me.getCode($tabContent);
    var lang;
    var tabName;
    if (app) {
      tabName = app.name + "-clone";
      lang = app.language;
    } 
    var $newTab = me.getTabContainer(me.createTab(null, tabName));
    me.setCode(code, $newTab);
    $('.new-app-info .language-select', $newTab).val(lang?lang:'python');
    return false;
  });
  
  $('.console-actions .clear-console', $tabContent).click(function() {
    var index = me.getSelectedIndex();
    var jqconsole = me.tabsIndex[index].jqConsole;
    jqconsole.Reset();
    me.startPrompt(jqconsole, 'python');
    return false;
  });
  
  this.tabsIndex[tabIndex].loaded = true;
}


/**
 * 
 */
Workspace.prototype.execute = function(tabIndex) {
  var me = this;
  var guid = me.tabsIndex[tabIndex].guid;
  var tabId = me.getTabId(tabIndex);
  var $tab = me.getTabContainer(tabIndex);
  var code = $('#code-editor-' + tabId, $tab).val();
  
  if (!code || $.trim(code).length == 0) return;
  
  var url;
  var language;
  if (guid) {
    url = ctx + "/app/" + guid + "/execute";
    language = me.tabsInfo[guid].app.language;
  } else {
    url = ctx + "/main/execute"; 
    language = $('.new-app-info select.language-select', $tab).val();
  }
  
  this.setStatus($tab, "running");
  
  $(function() {
    $.ajax({
      url: url,
      type: 'post',
      data: {
        code: code,
        language: language
      },
      success: function(result) {
        if (result['statusCode'] >= 0) {
          me.setStatus($tab, "finished-running");
          var jqConsole = me.tabsIndex[tabIndex].jqConsole;
          //var jqConsole = me.getJqConsole($tab);
          jqConsole.Write(result['output'], 'jqconsole-output');
          //jqConsole.Write('Run finished.', 'jqconsole-output');
          me.startPrompt(jqConsole, 'python');
          
          // render datasets & visualizations
          var datasets = result['datasets'];
          var visuals = result['visualizations'];
          me.renderOutput(datasets, visuals, $tab, null);
          
        } else {
          console.debug(result);
          me.setStatus($tab, "error");
        }
      },
      error: function(msg) {
        console.debug(msg);
        me.setStatus($tab, "error");
      }
    });
  });
  
}

/**
 * Saves the application code at the tab tabIndex
 */
Workspace.prototype.saveCode = function(tabIndex) {
  var me = this;
  var $tab = this.getTabContainer(tabIndex);
  
  var code = this.getCode($tab);
  
  if (!code || $.trim(code).length == 0) {
    console.debug("No code to save.");
    return;
  }
  
  var guid = this.tabsIndex[tabIndex].guid;
  
  if (guid) {
    var app = this.tabsInfo[guid].app;
    var authorName = app.authorName;
    if (authorName && (authorName != com.bouncingdata.Main.username)) {
      console.debug('No permission to save this application.');
      return false;
    } else {
      // save app. code
      
      this.setStatus($tab, "updating");
      
      $.ajax({
        url : ctx + "/app/" + guid + "/save",
        data : {         
          code : code,
          language: app.language
        },
        success : function(json) {
          console.info("Update code: " + JSON.stringify(json));
          me.setStatus($tab, "updated");
        },
        error : function(json) {
          console.info("Update code: " + JSON.stringify(json));
          me.setStatus($tab, "error");
        },
        type : "post"
      });
    }
  } else {
    // create new app.
    var $dialog = $('#new-app-dialog');
    $dialog.dialog("open");
    // reset form
    $('form', $dialog).each(function() {
      this.reset();
    });
    var language = $('.new-app-info .language-select', $tab).val();
    $('#new-app-language', $dialog).val(language?language:'python');
    $('#new-app-name', $dialog).focus();
    return;
  }
  
}


/**
 * Creates new application
 */
Workspace.prototype.createApp = function(appname, language, description, code, isPublic, tags) {
  var data = { 
    appname: appname,
    language: language,
    description: description,
    code: code,
    isPublic: isPublic,
    tags: tags
  };
  var me = this;
  $(function() {
    $.ajax({ 
      url: ctx + "/main/createapp", 
      data: data, 
      type: "post", 
      success: function(json) {
        // returned json as guid
        if (json) {
          //refresh browser
          com.bouncingdata.Browser.refreshMyStuff();
          var app = {name: appname, description: description, language: language, tags: tags, authorName: com.bouncingdata.Main.username, guid: json};
          me.bindAppToTab(me.getSelectedIndex(), app);
        }
        
      }, 
      error: function() { alert("Failed to create new application!"); } });  
  });
}

/**
 * 
 */
Workspace.prototype.bindAppToTab = function(index, app) {
  if (!app) return;
  var tabId = this.getTabId(index);
  this.tabsIndex[index].guid = app.guid;
  this.tabsInfo[app.guid] = { tabId: tabId, app: app};
  
  // change tab's title
  $($('li.tab-header a', this.$tabs)[index]).text(app.name);
  
  // set app. info
  var $tab = this.getTabContainer(index);
  
  $('.new-app-info', $tab).hide();
  var $appInfo = $('.app-info', $tab).show();
  $('div.app-title', $appInfo).append(app.name);
  $('div.app-language', $appInfo).append(app.language);
  $('div.app-author', $appInfo).append(app.authorName);
  
}

Workspace.prototype.setCode = function(code, $tab) {
  $('.app-code-editor .code-editor', $tab).val(code);
}

Workspace.prototype.getCode = function($tab) {
  return $('.app-code-editor .code-editor', $tab).val();
}

Workspace.prototype.renderOutput = function(datasets, visuals, $tab, app) {
  var $dsContainer = $(".app-output-datasets .dataset-container", $tab);
  $dsContainer.empty();
  // render datasets
  // IMPORTANT: this loop may decrease performance, need to be improved.
  for (d in datasets) {
    this.renderDataset(d, JSON.parse(datasets[d]), $dsContainer);
  }
  
  var $vsSlider = $(".app-output-visuals .visuals-container", $tab);
  $vsSlider.empty();
  for (v in visuals) {
    var type = visuals[v].type;
    if (type == "png" || type == "PNG") {
      this.renderBase64PNG(v, visuals[v].source, $vsSlider);
    } else if (type == "html" || type == "HTML") {
      //
      this.renderVisualization(v, visuals[v].source, $vsSlider, app);
      console.info("Render HTML visualization:" + v);
    }
  }
}

Workspace.prototype.renderVisualization = function(name, source, $visualsContainer, app) {
  var $vsItem = $('<div class="visualization-item"></div>');
  $vsItem.load().appendTo($visualsContainer);
  var $frame = $('<iframe class="visualization-item-frame"></iframe>'); 
  $frame.load().appendTo($vsItem);
  if (app) {
    var url = ctx + '/visualize/' + app['guid'] + '/' + source + '/html';
    $frame.attr('src', url);
    $('<span class="visualization-item-title" style="font-weight: bold; display: block;"><a href="'  + url 
        + '" alt="Open in new window">' + name + '</a></span>').load().appendTo($vsItem);
  } else {
    setTimeout(function() { $frame.contents().find('html').html(source); }, 2000);
    $('<span class="visualization-item-title" style="font-weight: bold; display: block;">' + name + '</span>').load().appendTo($vsItem);
  }
}

Workspace.prototype.renderBase64PNG = function(name, source, $visualsContainer) {
  var $vsItem = $('<div class="visualization-item" id="visualization-item-' + name + '"></div>');
  $vsItem.load().appendTo($visualsContainer);
  $('<img src="data:image/png;base64,' + source + '" />').load().appendTo($vsItem);
  $('<span class="visualization-item-title">' + name + '</span>').load().appendTo($vsItem);
}

Workspace.prototype.renderDataset = function(name, data, $dsContainer) {
  if (data.length <= 0) return;
  var $dsItem = $('<div class="dataset-item" style="margin-top: 2em;"></div>');
  $dsItem.load().appendTo($dsContainer);
  $dsItem.append('<span class="dataset-item-title"><strong>' + name + '</strong></span>');
  var $table = $('<table class="dataset-item-table"></table>');
  $dsItem.append($table);
  this.renderDatatable(data, $table);
}

Workspace.prototype.renderDatatable = function(data, $table) {
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
  //if (isNew) {
    $table.dataTable({
      "aaData": aaData, "aoColumns": aoColumns
    });
  //} else {
  //  var table = $table.dataTable();
  //  table.fnClearTable(0);
  //  table.fnAddData(aaData);
  //}
}


/**
 * 
 */
Workspace.prototype.setStatus = function($tab, status) {
  if (status) {
    switch(status) {
    case "running":
      $("#ajax-message", $tab).text("Running...");
      $("#ajax-loading", $tab).css("display", "inline");
      $("#ajax-message", $tab).css("color", "green");
      break;
    case "loading":
      $("#ajax-message", $tab).text("Loading...");
      $("#ajax-loading", $tab).css("display", "inline");
      $("#ajax-message", $tab).css("color", "green");
      break;
    case "finished-running":
      $("#ajax-message", $tab).text("Finished running.");
      $("#ajax-loading", $tab).css("display", "none");
      $("#ajax-message", $tab).css("color", "green");
      break;
    case "updating":
      $("#ajax-message", $tab).text("Updating...");
      $("#ajax-loading", $tab).css("display", "inline");
      $("#ajax-message", $tab).css("color", "green");
      break;
    case "updated":
      $("#ajax-loading", $tab).css("display", "none");
      $("#ajax-message", $tab).text("Updated.");
      $("#ajax-message", $tab).css("color", "green");
      break;
    case "error":
      $("#ajax-loading", $tab).css("display", "none");
      $("#ajax-message", $tab).text("Error");
      $("#ajax-message", $tab).css("color", "red");
    }
  } else {
    $("#ajax-message", $tab).text("");
    $("#ajax-loading", $tab).css("display", "none");
  }
}

/**
 * Starts console prompt and handles console action
 */
Workspace.prototype.startPrompt = function(jqconsole, language) {
  var me = this;
  jqconsole.Prompt(true, function(input) {
    $.ajax({
      url: ctx + "/shell/execute",
      type: "get",
      data: {
        code: input,
        language: language
      },
      success: function(result) {
        jqconsole.Write(result + '\n', 'jqconsole-output');
        me.startPrompt(jqconsole, language);
      },
      error: function(result) {
        console.info(result);
        me.startPrompt(jqconsole, language);
      }
    });
  });
}

/**
 * 
 */
/*Workspace.prototype.getJqConsole = function(tabIndex) {
  var $tab = this.getTabContainer(tabIndex);
  return $('.app-execution-logs .console', $tab).jqconsole(); 
}*/

/*Workspace.prototype.getJqConsole = function($tab) {
  return $('.app-execution-logs .console', $tab).jqconsole(); 
}*/


/**
 * Gets the current number of tabs, it differs from tab counter
 */
Workspace.prototype.getNumberOfTabs = function() {
  return $('li.tab-header a', this.$tabs).length;
}

/**
 * Gets the current selected tab index (zero-based: 0, 1, 2, ...)
 */
Workspace.prototype.getSelectedIndex = function() {
  return this.$tabs.tabs('option', 'selected');
}

/**
 * Gets the current selected tab id (i.e: tabs-1, tabs-2, ...)
 */
Workspace.prototype.getSelectedTabId = function() {
  var index = this.$tabs.tabs('option', 'selected');
  var href = $('li.tab-header a', this.$tabs)[index].href; 
  return href.substring(href.indexOf('#') + 1);
}

/**
 * 
 */
Workspace.prototype.getSelectedTabContainer = function() {
  return $('#' + this.getSelectedTabId(), this.$tabs);
}

/**
 * Get id of tab content's container
 * @param index: zero-based tab index
 * @return id of the div element which contains the content of the corresponding tab, i.e: tabs-1 at index 0
 */
Workspace.prototype.getTabId = function(index) {
  var href = $('li.tab-header a', this.$tabs)[index].href;
  return href.substring(href.indexOf('#') + 1)
}

/**
 * Gets the div element contains the tab at index (0, 1, 2, ...) 
 */
Workspace.prototype.getTabContainer = function(index) {
  var tabId = this.getTabId(index);
  return $('#' + tabId, this.$tabs);
}

/**
 * Retrieves the index of the tab with given tabId 
 */
Workspace.prototype.getTabIndex = function(tabId) {
  return $("li.tab-header", this.$tabs).index($("li:has(a[href='#" + tabId +"'])"));
}

com.bouncingdata.Workspace = new Workspace();
com.bouncingdata.Workspace.init();