function Workbench() {
  /*the tabs counter, always increase, = the number of tabs which was created
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
  this.untitledCounter = 0;*/
}

Workbench.prototype.init = function() {
  console.info('Initializing Workbench..');
  var me = this;
  
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
  
  $(function() {
    $('#main-content input:button').button();
    $('#main-content input:submit').button();
    
    //Init popup dialog
    $('.workbench-container #new-app-dialog').dialog({
      autoOpen: false,
      height: 345,
      width: 460,
      modal: true,
      resizable: false,
      buttons: {
        "Save": function() {
          //
          if (!$('#new-app-name', $(this)).val()) {
            return;
          }
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
    me.$tabTemplate = $('#workbench-content-template').template();
    me.$dsTemplate = $('#data-view-template').template();
    
    // initialize tabs
    me.$tabs = $('.workbench-container #workbench-main-tabs').tabs({
      tabTemplate: "<li class='tab-header workbench-tab-header'><a href='#{href}'>#{label}</a> <span class='ui-icon ui-icon-close' title='Close this tab'>Remove Tab</span></li>",
      
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
          $(ui.panel).append($tabContent).addClass('workbench-tab-panel');
          
          // complete UI & functionalities for this tab
          me.processTab(index, $tabContent);
        } else if (me.tabsIndex[index].type == 'dataset') {
          var dataset = me.tabsInfo[guid].dataset;
          var $tabContent = me.makeTabContent(ui.panel.id, dataset, 'dataset');
          $(ui.panel).append($tabContent).addClass('dataset-tab-panel');
          me.processTab(index, $tabContent);
        }
        
      }, 
      
      // each time show the tab
      show: function(event, ui) {       
        var guid = me.tabsIndex[ui.index].guid;
        me.updateActionStatus(guid, !guid?null:(me.tabsInfo[guid].app.user.username == com.bouncingdata.Main.username));
      }

    });
    
    // handles double click on tab bar
    $('#workbench-main-tabs .workbench-main-tabs-bar').dblclick(function(e) {
      var $target = $(e.target);
      var $p = $target.parents('li.workbench-tab-header');
      if ($p && $p.length > 0) return false;
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
    
    // 
    $('.workbench-ide .app-actions').click(function(e) {     
      var $target = $(e.target);
      if (($target[0].nodeName == "input" || $target[0].nodeName == "INPUT") && $target.hasClass("app-action")) {       
        // determine tab
        //var $tab = me.getSelectedTabId(); 
          //$target.parents('.workbench-tab-panel');
        // execute
        var tabId = me.getSelectedTabId();
        var index = me.getTabIndex(tabId);
        var actionId = $target.attr('id');
        if (actionId == "run-app") {
          me.execute(index);
        } else if (actionId == "save-app") {
          me.saveCode(index);
        } else if (actionId = 'copy-app') {
          me.cloneApp(index);
        }
      }
    });
             
  });
}

/**
 * Create a new tab to view application workbench or dataset
 */
Workbench.prototype.createTab = function(obj, tabName, type) {
  if (type == 'dataset') return this.openDataset(obj, tabName);
  else return this.openApp(obj, tabName);
} 

Workbench.prototype.openApp = function(app, tabName) {
  var index = this.getNumberOfTabs();
  this.tabsIndex[index] = {type: 'app'};
  if (!app) {
    this.tabsCounter++;
    this.untitledCounter++;
    this.currentApp = null;
    if (!tabName) tabName = 'Untitled' + this.untitledCounter;
    this.tabsIndex[index].guid = null;
    //this.tabsIndex[index].name = tabName;
    this.$tabs.tabs('add', '#tabs-' + this.tabsCounter, tabName);
  } else {
    this.currentApp = app;
    this.tabsCounter++;
    if (!tabName) tabName = app.name;
    this.tabsIndex[index].guid = app.guid;
    //this.tabsIndex[index].name = tabName;
    this.tabsInfo[app.guid] = { tabId: 'tabs-' + this.tabsCounter, app: app};
    this.$tabs.tabs('add', '#tabs-' + this.tabsCounter, tabName);
  }
  
  return index;
}

/**
 * 
 */
Workbench.prototype.openDataset = function(dataset, tabName) {
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
Workbench.prototype.removeTab = function(index) {
  if (this.getNumberOfTabs() == 1) {
    return;
  }
  if (!this.tabsIndex[index]) return;
  
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
Workbench.prototype.resizeAll = function() {
  // do resize the current layout  
  var $tab = this.getSelectedTabContainer();
  var index = this.getSelectedIndex();
  var type = this.tabsIndex[index].type;
  if (type == 'app') {
    var $layout = $('.workbench-content-layout', $tab).layout();
    if ($layout) $layout.resizeAll();
  } else if (type == 'dataset') {
    var $layout = $('.dataset-view-layout', $tab).layout();
    if ($layout) $layout.resizeAll();
  }
}

Workbench.prototype.resizeEditor = function($tab) {
  var editorDom = $('.app-code-editor .code-editor', $tab)[0];
  var editor = ace.edit(editorDom);
  editor.resize();
}

/**
 * Builds tab content from the template for particular application or dataset 
 */
Workbench.prototype.makeTabContent = function(tabId, obj, type) {
  type = type || 'app';
  if (type == 'app') {
    var tabObj = {
        tabId: tabId,
        appName: obj?obj.name:'',
        appLang: obj?obj.language:'',
        appAuthor: obj?obj.user.username:'',
        guid: obj?obj.guid:''
    }
    return $.tmpl(this.$tabTemplate, tabObj);
  } else if (type == 'dataset') {
    var tabObj = {
        tabId: tabId,
        dsName: obj?obj.name:'',
        dsAuthor: obj?obj.user.username:'',
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
Workbench.prototype.processTab = function(tabIndex, $tabContent) {
  
  var type = this.tabsIndex[tabIndex].type;
  var $tab = $tabContent.parent();
  var tabId = $tab.attr('id');
  var guid = this.tabsIndex[tabIndex].guid;
  var me = this;
  
  //buttons
  $('input:button', $tabContent).button();
  $('input:submit', $tabContent).button();
  
  if (type == 'dataset') {
    
    // initializes jQuery Layout
    var $layoutContainer = $('#dataset-view-layout-' + tabId, $tab);
    var $layout = $layoutContainer.layout({
      center__paneSelector: '#dataset-view-center-' + tabId,
      east__paneSelector: '#dataset-view-east-' + tabId,
      east__size: 300,
      applyDefaultStyles: true
    });
    
    if (!guid) {
      this.tabsIndex[tabIndex].loaded = true;
      return;
    }
    
    var dataset = this.tabsInfo[guid].dataset;
    var $table = $('table.dataset-table', $tab)
    
    // retrieve dataset content
    me.setStatus($tabContent, "running");
    console.debug("Retrieving dataset content...");
    var $queryEditor = $('.query-editor', $tab);
    $queryEditor.val('SELECT * FROM `' + dataset.name + '`');
    $.ajax({
      url: ctx + '/dataset/' + guid,
      dataType: 'json',
      success: function(result) {
        me.renderDatatable(result, $table);
        me.setStatus($tab, "");
      }, 
      error: function(result) {
        console.debug(result);
        me.setStatus($tab, "error");
      }
    });
    
    // 
    var queryFunction = function() {
      var sql = $queryEditor.val();
      if (!sql) return;
      me.setStatus($tab, "running");
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
            me.setStatus($tab, "finished-running");
            var data = result['data'];
            me.renderDatatable(data, $table);
          } else {
            // print error message
            me.setStatus($tab, "error");
            console.debug(result);
          }
        },
        error: function(result) {
          me.setStatus($tab, "error");
          console.debug(result);
        }
      });
    }
    
    $('input.dataset-query-execute', $tab).click(function() {
      queryFunction();
      return false;
    });
    
    this.tabsIndex[tabIndex].loaded = true;
    return;
  }
  
  // init. tabs
  $('.app-execution-logs-tabs', $tab).tabs();
  $('.app-output-tabs', $tab).tabs();

  // init console
  var jqConsole = $('#app-execution-logs-' + tabId + ' .console', $tab).jqconsole('Welcome to our console\n', Utils.getConsoleCaret('python'));
  this.tabsIndex[tabIndex].jqConsole = jqConsole;
  this.startPrompt(jqConsole, 'python');
  
  // init app. actions
  var app;
  if (guid) {
    $('.app-info', $tab).show();
    $('.new-app-info', $tab).hide();
    app = this.tabsInfo[guid].app;    
  } else {
    $('.app-info', $tab).hide();
    $('.new-app-info', $tab).show();
  }
    
  var $appCodeLayoutContainer = $('.app-code-layout', $tab);
  $appCodeLayoutContainer.layout({
    center__paneSelector: '#app-code-layout-center-' + tabId,
    east__paneSelector: '#app-code-layout-east-' + tabId,
    east__size: 350,
    east__initClosed: true,
    applyDefaultStyles: true,
    center__onresize: function() {
      com.bouncingdata.Workbench.resizeEditor($tab);
    }
  });
  
  // initialize ace editor
  var editorDom = $('.app-code-editor .code-editor', $tab)[0];
  var editor = ace.edit(editorDom);
  editor.getSession().setMode('ace/mode/python');
  
  // Retrieve app. details
  if (app) {
    $(function() {
      me.setStatus($tab.parent(), "loading");
      console.info('Retrieving application details...');
      $.ajax({
        url: ctx + "/app/a/" + guid,
        success: function(result) {
          console.debug(result);
          me.setCode(result.code, $tab);
          me.loadDashboard(result['visualizations'], result['dashboard'], $tab, app);
          var $codeText = $("<pre></pre>");
          $codeText.text(result.code).appendTo($('.app-output-code .code-block', $tab));
          me.setStatus($tab, "");
        },
        error: function(msg) {
          console.debug(msg);
          me.setStatus($tab, "error");
        }
      });
    });
  }

  $(".dashboard-preview", $tab).click(function() {
    window.open(ctx + "/anls/" + guid);
  });

  $(".dashboard-publish", $tab).click(function() {
    $.ajax({
      url: ctx + '/app/a/publish/' + guid,
      type: 'post',
      data: {},
      success: function(result) {
        console.debug("Successfully published analysis.");
        if (window.confirm("Your analysis has published! View your analysis now?")) {
          window.open(ctx + "/anls/" + guid);
        }
      },
      error: function(result) {
        console.debug("Failed to publis analysis.");
      }
    });
    return false;
  });
  
  /*
  $(function() {
    // just for demo
    var $iframe = $('<iframe style="position: absolute; width: 100%; height: 100%; border: 0 none;"></iframe>');
    var $dashboard = $('#viz-dashboard-' + tabId, $tab);
    $iframe.load().appendTo($dashboard);
    $dashboard.append('<script type="text/javascript"> $("#viz-dashboard-' + tabId + ' iframe").attr("src", "http://bouncingdata.com/cdn/dashboard.html") </script>');
    //$iframe.attr('src', "http://bouncingdata.com/cdn/dashboard.html");
  });
  */
  
  $('.console-actions .clear-console', $tab).click(function() {
    var index = me.getSelectedIndex();
    var jqconsole = me.tabsIndex[index].jqConsole;
    jqconsole.Reset();
    me.startPrompt(jqconsole, 'python');
    return false;
  });
  
  this.tabsIndex[tabIndex].loaded = true;
}


/**
 * Executes code from the tab with given index
 */
Workbench.prototype.execute = function(tabIndex) {
  var me = this;
  var guid = this.tabsIndex[tabIndex].guid;
  var tabId = this.getTabId(tabIndex);
  var $tab = this.getTabContainer(tabIndex);
  var code = this.getCode($tab);
  
  if (!code || $.trim(code).length == 0) return;
  
  var url, language, app;
  if (guid) {
    app = this.tabsInfo[guid].app;
    if (app.user.username && (app.user.username != com.bouncingdata.Main.username)) {
      console.debug('No permission to execute this application.');
      return false;
    }
    url = ctx + "/app/e/" + guid;
    language = this.tabsInfo[guid].app.language;
  } else {
    url = ctx + "/main/execute"; 
    language = $('.new-app-info select.language-select', $tab).val();
    app = null;
  }
  
  this.setStatus($tab, "running");
  $('.app-code-layout', $tab).layout().open("east");
  
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
          jqConsole.Write(result['output'], 'jqconsole-output');
          //jqConsole.Write('Run finished.', 'jqconsole-output');
          me.startPrompt(jqConsole, 'python');
          
          if (!app) {
            // render datasets & visualizations
            var datasets = result['datasets'];
            var visuals = result['visualizations'];
            me.renderOutput(datasets, visuals, $tab, app);
          } else {
            // reload datasets & viz.
            me.reloadDashboard(app, $tab);
          }
          
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
Workbench.prototype.saveCode = function(tabIndex) {
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
    var authorName = app.user.username;
    if (authorName && (authorName != com.bouncingdata.Main.username)) {
      console.debug('No permission to save this application.');
      return false;
    } else {
      // save app. code     
      this.setStatus($tab, "updating");
      
      $.ajax({
        url : ctx + "/app/s/" + guid,
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
 * Clone content from given tab to new tab
 */
Workbench.prototype.cloneApp = function(tabIndex) {
  var $tab = this.getTabContainer(tabIndex);
  var guid = this.tabsIndex[tabIndex].guid; 
  if (guid) {
    var app = this.tabsInfo[guid].app;
    var lang = app.language;
    var code = this.getCode($tab);
    var $newTab = this.getTabContainer(this.createTab(null, app.name + "-clone"));
    this.setCode(code, $newTab);
    $('.new-app-info .language-select', $newTab).val(lang?lang:'python');
  }
}

/**
 * Creates new application
 */
Workbench.prototype.createApp = function(appname, language, description, code, isPublic, tags) {
  if (!appname) {
    console.debug("Application name must be not empty");
    return;
  }
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
Workbench.prototype.bindAppToTab = function(index, app) {
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
  this.updateActionStatus(true, true);
}

Workbench.prototype.setCode = function(code, $tab) {
  var editorDom = $('.app-code-editor .code-editor', $tab)[0];
  var editor = ace.edit(editorDom);
  editor.getSession().getDocument().setValue(code);
}

Workbench.prototype.getCode = function($tab) {
  var editorDom = $('.app-code-editor .code-editor', $tab)[0];
  var editor = ace.edit(editorDom);
  return editor.getSession().getDocument().getValue();
}

Workbench.prototype.renderOutput = function(datasets, visuals, $tab, app) {
  /*var $dsContainer = $(".app-output-datasets .dataset-container", $tab);
  $dsContainer.empty();
  // render datasets
  // IMPORTANT: this loop may decrease performance, need to be improved.
  for (d in datasets) {
    this.renderDataset(d, JSON.parse(datasets[d]), $dsContainer);
  }*/
  
  var $dashboard = $("#viz-dashboard-" + $tab.attr('id'), $tab);
  $dashboard.attr('tabid', $tab.attr('id'));
  if (app) {
    $dashboard.attr('guid', app['guid']);
  }
  com.bouncingdata.Dashboard.load(visuals, null, $dashboard, true);
}

Workbench.prototype.loadDashboard = function(visuals, dashboard, $tab, app) {
  var $dashboard = $("#viz-dashboard-" + $tab.attr('id'), $tab);
  $dashboard.attr('guid', app['guid']).attr('tabid', $tab.attr('id')); 
  com.bouncingdata.Dashboard.load(visuals, dashboard, $dashboard, app.user.username==com.bouncingdata.Main.username);
}

/**
 * Reloads after execute
 */
Workbench.prototype.reloadDashboard = function(app, $tab) {
  var $dashboard = $("#viz-dashboard-" + $tab.attr('id'), $tab);
  // retrieves application visualizations
  $.ajax({
    url: ctx + '/app/v/' + app['guid'],
    dataType: 'json',
    type: 'get',
    success: function(result) {
      com.bouncingdata.Dashboard.load(result['visualizations'], null, $dashboard, true);
      console.debug("Finish reload dashboard after execution, now post it back again...");
      com.bouncingdata.Dashboard.postback($dashboard);
    },
    error: function(result) {
      console.debug(result);
    }
  });
}

Workbench.prototype.renderVisualization = function(name, source, $visualsContainer, app) {
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

Workbench.prototype.renderBase64PNG = function(name, source, $visualsContainer) {
  var $vsItem = $('<div class="visualization-item" id="visualization-item-' + name + '"></div>');
  $vsItem.load().appendTo($visualsContainer);
  $('<img src="data:image/png;base64,' + source + '" />').load().appendTo($vsItem);
  $('<span class="visualization-item-title">' + name + '</span>').load().appendTo($vsItem);
}

Workbench.prototype.renderDataset = function(name, data, $dsContainer) {
  if (data.length <= 0) return;
  var $dsItem = $('<div class="dataset-item" style="margin-top: 2em;"></div>');
  $dsItem.load().appendTo($dsContainer);
  $dsItem.append('<span class="dataset-item-title"><strong>' + name + '</strong></span>');
  var $table = $('<table class="dataset-item-table"></table>');
  $dsItem.append($table);
  this.renderDatatable(data, $table);
}

Workbench.prototype.renderDatatable = function(data, $table) {
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
Workbench.prototype.setStatus = function($tab, status) {
  var $message = $("#ajax-message", $tab);
  var $loading = $("#ajax-loading", $tab);
  if (status) {
    switch(status) {
    case "running":
      $message.text("Running...").css("color", "green");
      $loading.css("opacity", 1);
      break;
    case "loading":
      $message.text("Loading...").css("color", "green");
      $loading.css("opacity", 1);
      break;
    case "finished-running":
      $message.text("Finished running.").css("color", "green");;
      $loading.css("opacity", 0);
      break;
    case "updating":
      $message.text("Updating...").css("color", "green");
      $loading.css("opacity", 1);
      break;
    case "updated":
      $loading.css("opacity", 0)
      $message.text("Updated.").css("color", "green");;
      break;
    case "error":
      $loading.css("opacity", 0);
      $message.text("Error").css("color", "red");
      break;
    default:
      $message.text("");
    $loading.css("opacity", 0);
    }
  } else {
    $message.text("");
    $loading.css("opacity", 0);
  }
}

Workbench.prototype.updateActionStatus = function(isSaved, isOwner) {
  if (isSaved) {
    if (isOwner) {
      $('.app-actions #save-app').show();
      $('.app-actions #run-app').show();
      $('.app-actions #copy-app').show();
    } else {
      $('.app-actions #save-app').hide();
      $('.app-actions #run-app').hide();
      $('.app-actions #copy-app').show();
    }
  } else {
    $('.app-actions #save-app').show();
    $('.app-actions #run-app').show();
    $('.app-actions #copy-app').hide();
  }
}

/**
 * Starts console prompt and handles console action
 */
Workbench.prototype.startPrompt = function(jqconsole, language) {
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
/*Workbench.prototype.getJqConsole = function(tabIndex) {
  var $tab = this.getTabContainer(tabIndex);
  return $('.app-execution-logs .console', $tab).jqconsole(); 
}*/

/*Workbench.prototype.getJqConsole = function($tab) {
  return $('.app-execution-logs .console', $tab).jqconsole(); 
}*/


/**
 * Gets the current number of tabs, it differs from tab counter
 */
Workbench.prototype.getNumberOfTabs = function() {
  return $('li.tab-header a', this.$tabs).length;
}

/**
 * Gets the current selected tab index (zero-based: 0, 1, 2, ...)
 */
Workbench.prototype.getSelectedIndex = function() {
  return this.$tabs.tabs('option', 'selected');
}

/**
 * Gets the current selected tab id (i.e: tabs-1, tabs-2, ...)
 */
Workbench.prototype.getSelectedTabId = function() {
  var index = this.$tabs.tabs('option', 'selected');
  var href = $('li.tab-header a', this.$tabs)[index].href; 
  return href.substring(href.indexOf('#') + 1);
}

/**
 * 
 */
Workbench.prototype.getSelectedTabContainer = function() {
  return $('#' + this.getSelectedTabId(), this.$tabs);
}

/**
 * Get id of tab content's container
 * @param index: zero-based tab index
 * @return id of the div element which contains the content of the corresponding tab, i.e: tabs-1 at index 0
 */
Workbench.prototype.getTabId = function(index) {
  var href = $('li.tab-header a', this.$tabs)[index].href;
  return href.substring(href.indexOf('#') + 1)
}

/**
 * Gets the div element contains the tab at index (0, 1, 2, ...) 
 */
Workbench.prototype.getTabContainer = function(index) {
  var tabId = this.getTabId(index);
  return $('#' + tabId, this.$tabs);
}

/**
 * Retrieves the index of the tab with given tabId 
 */
Workbench.prototype.getTabIndex = function(tabId) {
  return $("li.tab-header", this.$tabs).index($("li:has(a[href='#" + tabId +"'])"));
}

com.bouncingdata.Workbench = new Workbench();