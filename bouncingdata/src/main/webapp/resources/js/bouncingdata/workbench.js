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

/**
 * Initializes workbench: init. variables, bind handlers, load last session
 */
Workbench.prototype.init = function(callback) {
  console.info('Initializing Workbench..');
  var me = this;
  
  //the tabs counter, always increase, = the number of tabs which was created
  this.tabsCounter = 0;
  
  // current opening applications (only saved apps), key: app guid, value: tab info { tabId, app info, type }
  this.tabsInfo = {};
  
  // key: index (0, 1, 2, ...), value: guid = app guid, type = ?, jqconsole, if the tab belongs to new app., value = null 
  this.tabsIndex = [];
      
  // use to name untitled tabs
  this.untitledCounter = 0;
  
  $(function() {
    $('#main-content input:button').button();
    $('#main-content input:submit').button();
    
    $('#app-actions-toolbar .app-action').button();
    
    /**
     * Initilizes popup dialogs
     */
    me.$saveAnlsDialog = $('.workbench-container #save-app-dialog').dialog({
      autoOpen: false,
      height: 345,
      width: 460,
      modal: true,
      resizable: false,
      buttons: {
        "Save": function() {
          //
          if (!$('.app-name', $(this)).val()) {
            return;
          }
          var index = me.getSelectedIndex();
          var type = me.tabsIndex[index].type;
          var isPublic = $('.app-privacy-public', $(this)).prop('checked');
          
          me.createApp($('.app-name', $(this)).val(), $('.app-language', $(this)).val(), 
              $('.app-description', $(this)).val(), me.getCode(me.getSelectedTabContainer()), 
              isPublic, $('.app-tags', $(this)).val(), type);
          $(this).dialog("close");
        }, 
        "Cancel": function() {
          $(this).dialog("close");
        }
      },
      open: function() {
        $('.app-privacy-public', me.$saveAnlsDialog).attr('checked', true);
        $('.ui-widget-overlay').bind('click', function(){ me.$saveAnlsDialog.dialog('close'); });
      },
      close: function() {     
      }
    });
    
    me.$publishDialog = $('.workbench-container #publish-dialog').dialog({
      autoOpen: false,
      height: 250,
      width: 400,
      modal: true,
      resizable: false,
      open: function(event, ui) {
        $('.ui-widget-overlay').bind('click', function(){ $(this).dialog('close'); });
      }
    });
    
    me.$uploadDataDialog = $('.workbench-container #upload-data-dialog').dialog({
      autoOpen: false,
      height: 180,
      width: 400,
      modal: true,
      resizable: false,
      buttons: {
        "Upload": function() {
          console.debug("Upload dataset file...");
          var $form = $('form#file-upload-form', $(this));
          //var file = $form.prop('value');
          var file = $('#file', $form).val();
          if (!file) {
            return;
          }
          // determine file type
          if (file.indexOf('/') > -1) file = file.substring(file.lastIndexOf('/') + 1);
          else if (file.indexOf('\\') > -1) file = file.substring(file.lastIndexOf('\\') + 1);
          
          if (file.indexOf('.') < 0) {
            $('.upload-status', $form).text('This file could not be imported. Supported formats: .xls, .xlsx, .csv, .txt').show();
            return;
          }
          
          var extension = file.substring(file.lastIndexOf('.') + 1);
          if ($.inArray(extension, ['xls', 'xlsx', 'csv', 'txt']) < 0) {
            $('.upload-status', $form).text('This file could not be imported. Supported formats: .xls, .xlsx, .csv, .txt').show();
            return;
          }
          
          $('.upload-in-progress', $form).show();
          $('.upload-status', $form).text('Uploading in progress').show();
          $form.ajaxSubmit({
            url: ctx + '/dataset/up',
            type: 'post',
            data: {
              type: extension
            },
            clearForm: true,
            resetForm: true,
            success: function(res) {
              $('.upload-in-progress', $form).hide();
              if (res < 0) {
                $('.upload-status', $form).text('Upload failed! Your file may not valid.');
                return;
              }
              console.debug("Uploaded successfully!");
              $('.upload-status', $form).text(res +  ' bytes uploaded successfully');
            }
          });
        },
        "Cancel": function() {
          $(this).dialog('close');
        }
      },
      open: function(event, ui) {
        $('.upload-status', me.$uploadDataDialog).text('Maximum file size is 10MB').show();
        $('.ui-widget-overlay').bind('click', function(){ me.$uploadDataDialog.dialog('close'); })
      }
    });
    
    me.$newDialog = $('.workbench-container #new-dialog').dialog({
      autoOpen: false,
      width: 300,
      modal: true,
      resizable: false,
      buttons: {},
      open: function(event, ui) {        
        $('.ui-widget-overlay').bind('click', function(){ me.$newDialog.dialog('close'); });
      }
    });
    $('li.script-type', me.$newDialog).click(function() {
      me.$newDialog.dialog('close');
      me.createTab(null, null, $(this).attr('script-type'), null);
    });
    
    // get the tab template
    me.$tabTemplate = $('#workbench-content-template').template();
    me.$dsTemplate = $('#data-view-template').template();
    
    /**
     * Initializes workbench tabs
     */
    me.$tabs = $('.workbench-container #workbench-main-tabs').tabs({
      tabTemplate: "<li class='tab-header workbench-tab-header'><a href='#{href}'>#{label}</a> <span class='ui-icon ui-icon-close' title='Close this tab'>Remove Tab</span></li>",
      
      add: function(event, ui) {
        // trick here: select before finish the adding, by this way the layout achieves the full width
        var index = ui.index;
        me.tabsIndex[index].loaded = false;
        //me.$tabs.tabs('select', index);
            
        var guid = me.tabsIndex[index].guid;
        var type = me.tabsIndex[index].type;
        if (type == 'analysis' || type == 'scraper') {
          var app = null;
          if (guid) app = me.tabsInfo[guid].app;
          var $tabContent = me.makeTabContent(ui.panel.id, app, type);
          $(ui.panel).append($tabContent).addClass('workbench-tab-panel');
          
          // complete UI & functionalities for this tab
          me.processTab(index, $tabContent);
        } else if (type == 'dataset') {
          var dataset = me.tabsInfo[guid].dataset;
          var $tabContent = me.makeTabContent(ui.panel.id, dataset, 'dataset');
          $(ui.panel).append($tabContent).addClass('dataset-tab-panel');
          me.processTab(index, $tabContent);
        }
        
        me.$tabs.tabs('select', index);
      }, 
      
      // each time show the tab
      show: function(event, ui) {       
        var guid = me.tabsIndex[ui.index].guid;
        var app = guid?me.tabsInfo[guid].app:null;
        //me.updateActionStatus(guid, !guid?null:(me.tabsInfo[guid].app.user.username == com.bouncingdata.Main.username));
        me.updateActionStatus(app);
      }

    });
    
    // handles double click on tab bar
    /*$('#workbench-main-tabs .workbench-main-tabs-bar').dblclick(function(e) {
      var $target = $(e.target);
      var $p = $target.parents('li.workbench-tab-header');
      if ($p && $p.length > 0) return false;
      me.createTab(null);
      return false;
    });*/
    
    // handles tab closing
    $(".tab-header span.ui-icon-close", me.$tabs).live("click", function() {
      var index = $("li", me.$tabs).index($(this).parent());
      me.removeTab(index);
      return false;
    });
    
    /**
     * Toolbar actions
     */
    $('.workbench-ide .app-actions').click(function(e) {
      var $target = $(e.target);
      // for Chrome
      if ($target[0].nodeName == "span" || $target[0].nodeName == "SPAN") {
        $target = $target.parents('button.app-action');
      }
      if (($target[0].nodeName == "button" || $target[0].nodeName == "BUTTON" || $target[0].nodeName == "a") && $target.hasClass("app-action")) {       
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
        } else if (actionId == 'copy-app') {
          me.cloneApp(index);
        } else if (actionId == 'new-app') {
          // create empty tab
          //me.createTab(null, null, 'analysis');
          me.$newDialog.dialog('open');
        } else if (actionId == 'upload-data') {
          me.$uploadDataDialog.dialog('open');
        } else if (actionId == 'view-app') {
          var guid = me.tabsIndex[index].guid;
          if (guid && me.tabsIndex[index].type == 'analysis') {
            com.bouncingdata.Nav.fireAjaxLoad(ctx + '/anls/' + guid, false);
          }         
        } else if (actionId == 'publish-app') {
          // WHY IT'S SO COMPLICATED???
          var guid = me.tabsIndex[index].guid;
          if (guid) {
            var app = me.tabsInfo[guid].app;
            if (!app.published && !app.executed) {
              window.alert("Your application has not been executed yet!");
              return;
            }
            
            var value = !app.published;
            var publishFunc = function() {
              me.publish(guid, value, function() {
                console.debug("Successfully" + value?"publish":"un-publish" + " analysis.");
                app.published = value;
                if (value) {
                  if (window.confirm("Your analysis has published! View your analysis now?")) {
                    window.open(ctx + "/anls/" + guid);
                  }
                } else {
                  window.alert("Your analysis has become private");
                }
                $('span',$target).text(value?"Make private":"Publish");
              });
            };
              
            if (value) {
              me.$publishDialog.dialog("option", "buttons", {
                "Save": function() {
                  publishFunc();
                  $(this).dialog("close");          
                }, 
                "Cancel": function() {
                  $(this).dialog("close");
                } 
              });
              me.$publishDialog.dialog("open");
              me.$publishDialog.css('z-index', 10000).css('position', 'relative');
              $('#anls-name', me.$publishDialog).text(app.name);
            } else {
              publishFunc();
            }
            
          } else {
            return;
          }
        }
      }
    });
    
    //
    me.loadLastSession();
    
    com.bouncingdata.Nav.setSelected('page', 'create');
    if(callback) callback();
    
  });
}

/**
 * Create a new tab to view application workbench or dataset
 */
Workbench.prototype.createTab = function(obj, tabName, type, lang) {
  if (type == 'dataset') return this.openDataset(obj, tabName);
  else return this.openApp(obj, tabName, type, lang);
} 

Workbench.prototype.openApp = function(app, tabName, type, lang) {
  var index = this.getNumberOfTabs();
  this.tabsIndex[index] = {type: type};
  if (!app) {
    this.tabsCounter++;
    this.untitledCounter++;
    if (!lang) lang = 'python';
    if (!tabName) tabName = 'Untitled' + this.untitledCounter + '.' + (lang=="python"?"py":(lang=="r"?"r":""));
    this.tabsIndex[index].guid = null;
    this.tabsIndex[index].lang = lang;
    //this.tabsIndex[index].name = tabName;
    this.$tabs.tabs('add', '#tabs-' + this.tabsCounter, tabName);
  } else {
    this.tabsCounter++;
    lang = app.language;
    if (!tabName) tabName = app.name + '.' + (lang=="python"?"py":(lang=="r"?"r":""));
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
 * @deprecated
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
 * Builds tab content from the template for given application or dataset 
 */
Workbench.prototype.makeTabContent = function(tabId, obj, type) {
  if (type == 'analysis' || type == 'scraper') {
    var tabObj = {
        tabId: tabId,
        appName: obj?obj.name:'',
        appLang: obj?obj.language:'',
        appAuthor: obj?obj.user.username:'',
        guid: obj?obj.guid:'',
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
 * Some magic works to render tab content & function correctly
 */
Workbench.prototype.processTab = function(tabIndex, $tabContent) {
  
  var type = this.tabsIndex[tabIndex].type;
  var lang = this.tabsIndex[tabIndex].lang;
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
    
  
  // initializes the inner tabs: code, visualizations, data
  var $tabs = $('.app-editor-tabs', $tab).tabs();
  if (type == "scraper") {   
    $tabs.tabs("select", 0);
    $tabs.tabs("disable", 1);
  }

  this.tabsIndex[tabIndex].dsloaded = false;
  
  // init console
  //var jqConsole = $('#app-execution-logs-' + tabId + ' .console', $tab).jqconsole('Welcome to our console\n', Utils.getConsoleCaret('python'));
  var jqConsole = $('#execution-logs-' + tabId + ' .console', $tab).jqconsole('Welcome to our console\n', Utils.getConsoleCaret('python'));
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
    
  /*var $appCodeLayoutContainer = $('.app-code-layout', $tab);
  $appCodeLayoutContainer.layout({
    center__paneSelector: '#app-code-layout-center-' + tabId,
    east__paneSelector: '#app-code-layout-east-' + tabId,
    east__size: 350,
    east__initClosed: true,
    applyDefaultStyles: true,
    center__onresize: function() {
      com.bouncingdata.Workbench.resizeEditor($tab);
    }
  });*/
  
  // initialize ace editor
  var editorDom = $('.app-code-editor .code-editor', $tab)[0];
  var editor = ace.edit(editorDom);
  editor.getSession().setMode('ace/mode/python');
  
  var url = type=="analysis"?(ctx + "/app/a/" + guid):type=="scraper"?(ctx + "/app/scr/" + guid):null;
  // Retrieve app. details
  if (app) {
    $(function() {
      me.setStatus($tab, "loading");
      console.info('Retrieving application details...');
      $.ajax({
        url: url,
        success: function(result) {
          console.debug(result);
          /*var $codeText = $("<pre></pre>");
          $codeText.text(result.code).appendTo($('.app-output-code .code-block', $tab));*/
          
          
          me.setCode(result.code, $tab);
          if (type == "analysis") {
            //if ($tabs.tabs("option", "select") == 1 && !$tabs['dbloaded']) {
              me.loadDashboard(result['visualizations'], result['dashboard'], $tab, app);
            //} else {
            //  $tab['dbloaded'] = false;
            //}       
              
            if ((result['datasets'] && result['datasets'].length > 0) || (result['attachments'] && result['attachments'].length > 0)) {
              $('#' + $tab.prop('id') + '-data .no-data', $tab).remove();
            }
            $tabs.bind('tabsselect', function(event, ui) {
              //var tabIndex = me.getTabIndex($tab.prop('id'));
              //if (ui.index == 2 && !me.tabsIndex[tabIndex].dsloaded) {
              if (ui.index == 2 && !$tab.dsloaded) {
                me.loadDatasets(result['datasets'], $tab);
                //me.renderAttachments(result['attachments'], $('#' + $tab.attr('id') + '-data', $tab));
              }
            });
            
            me.renderAttachments(result['attachments'], $('#' + $tab.attr('id') + '-data', $tab));
            
          } else if (type == "scraper") {
            // 
            $tabs.bind('tabsselect', function(event, ui) {
              if (ui.index == 2 && !$tab.dsloaded) {
                me.loadDatasets(result['datasets'], $tab);
              }
            });  
          }
          
          me.setStatus($tab, "");
        },
        error: function(msg) {
          console.debug(msg);
          me.setStatus($tab, "error");
        }
      });
    });
  } else {
    me.setLanguage(lang, $tab);
    me.setCode(lang=='python'?'import datastore\n':'', $tab);
  }
  
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
 * Loads the last saved session
 */
Workbench.prototype.loadLastSession = function() {
  var session = com.bouncingdata.Main.workbenchSession;
  if ($.isEmptyObject(session) || session["tabsIndex"].length == 0) {
    // create an empty tab
    this.createTab(null, null, 'analysis', 'python');
    return;
  }
    
  this.tabsCounter = session["tabsCounter"];
  this.tabsInfo = session["tabsInfo"];
  this.tabsIndex = session["tabsIndex"];
  
  // process to open old tabs
  for (i in this.tabsIndex) {
    var tabInfo = this.tabsIndex[i];
    var lang = tabInfo.lang;
    var guid = tabInfo.guid;
    if (guid) {
      var app = this.tabsInfo[guid].app;
      tabName = app.name + '.' + (lang=="python"?"py":(lang=="r"?"r":""));
      this.tabsInfo[guid].tabId = 'tabs-' + (i*1+1);
      this.$tabs.tabs('add', '#tabs-' + (i*1+1), tabName);
    } else {
      var tabName = 'Untitled' + ++this.untitledCounter + '.' + (lang=="python"?"py":(lang=="r"?"r":""));
      this.$tabs.tabs('add', '#tabs-' + (i*1+1), tabName);
      // set code
      this.setCode(tabInfo.code, this.getTabContainer(i)); 
    }
  }
  
  if (session["currentSelected"]) {
    var guid = session["currentSelected"].guid;
    if (guid) {
      this.$tabs.tabs("select", '#' + this.tabsInfo[guid].tabId);
    } else {
      this.$tabs.tabs("select", session["currentSelected"].index);      
    }
    if (session["currentSelected"].tab) {
      var $tab = this.getSelectedTabContainer();
      $('.app-editor-tabs', $tab).tabs("select", session["currentSelected"].tab);
    }
  }
  
}

/**
 * Saves current session
 */
Workbench.prototype.saveSession = function() {
  // save to workbenchSession object of com.bouncingdata.Main
  for (i in this.tabsIndex) {
    if (!this.tabsIndex[i].guid) {
      this.tabsIndex[i].code = this.getCode(this.getTabContainer(i));
    }
  }
  com.bouncingdata.Main.workbenchSession = {
      'tabsCounter': this.tabsCounter,
      'tabsInfo': this.tabsInfo,
      'tabsIndex': this.tabsIndex,
      'currentSelected': {
        'guid': this.tabsIndex[this.getSelectedIndex()].guid,
        'index': this.getSelectedIndex(),
        'tab': $('.app-editor-tabs', this.getSelectedTabContainer()).tabs("option", "select")
      }
  }
}

/**
 * Saves current session then dispose
 */
Workbench.prototype.dispose = function() {
  this.saveSession();
  this.tabsCounter = null;
  this.tabsInfo = null;
  this.tabsIndex = null;
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
  var type = this.tabsIndex[tabIndex].type;
  
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
  //$('.app-code-layout', $tab).layout().open("east");
  
  $(function() {
    $.ajax({
      url: url,
      type: 'post',
      data: {
        code: code,
        language: language,
        type: type
      },
      success: function(result) {
        if (result['statusCode'] >= 0) {
          me.setStatus($tab, "finished-running");
          var jqConsole = me.tabsIndex[tabIndex].jqConsole;
          jqConsole.Write(result['output'], 'jqconsole-output');
          //jqConsole.Write('Run finished.', 'jqconsole-output');
          me.startPrompt(jqConsole, 'python');
          
          if (!app) {
            if (type == 'analysis') {
              // render datasets & visualizations
              var datasets = result['datasets'];
              var visuals = result['visualizations'];
              me.renderOutput(datasets, visuals, $tab, app);
            }
          } else {
            // reload datasets & viz.
            if (type == 'analysis') {
              me.reloadDashboard(app, $tab);
              app.executed = true;
              var datasets = result['datasets'];
              var $dsContainer = $('#' + tabId + '-data', $tab);
              $dsContainer.empty();
              me.renderDatasets(datasets, $dsContainer);
            } else if (type == 'scraper') {
              var datasets = result['datasets'];
              var $dsContainer = $('#' + tabId + '-data', $tab);
              //for (name in datasets) {
              //  var data = datasets[name];
              //  me.renderDataset(name, data, $dsContainer);
              //}
              
              me.renderDatasets(datasets, $dsContainer);
            }
          }
          
        } else {
          console.debug(result);
          me.setStatus($tab, "error");
          var jqConsole = me.tabsIndex[tabIndex].jqConsole;
          jqConsole.Write(result['output'], 'jqconsole-output');
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
  var type = this.tabsIndex[tabIndex].type;
  
  if (!code || $.trim(code).length == 0) {
    console.debug("No code to save.");
    return;
  }
  
  var guid = this.tabsIndex[tabIndex].guid;
  var type = this.tabsIndex[tabIndex].type;
  
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
          language: app.language,
          type: type
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
    var $dialog = me.$saveAnlsDialog;
    $dialog.dialog("option", "title", "Save " + type);
    $dialog.dialog("open");
    // reset form
    $('form', $dialog).each(function() {
      this.reset();
    });
    var language = $('.new-app-info .language-select', $tab).val();
    $('.app-language', $dialog).val(language?language:'python');
    $('.app-name', $dialog).focus();
    return;
  }
}

/**
 * Publish an analysis
 * @param guid the guid of analysis
 * @param value boolean value indicates publish or un-publish action
 * @param callback the callback function
 */
Workbench.prototype.publish = function(guid, value, callback) {
  $.ajax({
    url: ctx + '/app/a/publish/' + guid,
    type: 'post',
    data: {
      value: value
    },
    success: function(result) {
      if (callback) callback();
    },
    error: function(result) {
      console.debug("Failed to publis analysis.");
    }
  });
}

/**
 * Clone content from given tab to new tab
 */
Workbench.prototype.cloneApp = function(tabIndex) {
  var $tab = this.getTabContainer(tabIndex);
  var guid = this.tabsIndex[tabIndex].guid; 
  if (guid) {
    var type = this.tabsIndex[tabIndex].type;
    var app = this.tabsInfo[guid].app;
    var lang = app.language;
    var code = this.getCode($tab);
    var $newTab = this.getTabContainer(this.createTab(null, app.name + "-clone", type, lang));
    this.setCode(code, $newTab);
    $('.new-app-info .language-select', $newTab).val(lang?lang:'python');
  }
}

/**
 * Creates new application
 */
Workbench.prototype.createApp = function(appname, language, description, code, isPublic, tags, type) {
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
    tags: tags,
    type: type
  };
  var me = this;
  $(function() {
    $.ajax({ 
      url: ctx + "/main/createapp", 
      data: data, 
      type: "post", 
      success: function(json) {
        // returned json as guid
        // return app obj
        if (json) {
          //refresh browser
          com.bouncingdata.Browser.refreshMyStuff();
          //var app = {name: appname, description: description, language: language, tags: tags, authorName: com.bouncingdata.Main.username, guid: json};
          //var app = com.bouncingdata.Browser.getStuff(json);
          var app = json;
          me.bindAppToTab(me.getSelectedIndex(), app);
        }
        
      }, 
      error: function() { alert("Failed to create new application!"); } });  
  });
}

/**
 * Converts current anonymous tab to an application's tab
 * @param index the current tab index
 * @param app the application to bind to tab
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
  $('div.app-author', $appInfo).append(app.user.username);
  this.updateActionStatus(app);
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

Workbench.prototype.setLanguage = function(lang, $tab) {
  $('.new-app-info .language-select', $tab).val(lang);
}

Workbench.prototype.getLanguage = function($tab) {
  
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
  $tab['dbloaded'] = true;
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
      com.bouncingdata.Dashboard.postback($dashboard, app.executed?"execute":"");
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

/**
 * Loads data from list of dataset info
 * @param dsList list of data info object
 * @param $tab the container tab in workbench
 */
Workbench.prototype.loadDatasets = function(dsList, $tab) {
  var me = this;
  var guids = '';
  if (!dsList || dsList.length == 0) return;
  for (index in dsList) {
    var ds = dsList[index];
    guids += ds.guid + ',';
  }
  guids = guids.substring(0, guids.length - 1);
  var $dsContainer = $('#' + $tab.attr('id') + '-data', $tab);
  com.bouncingdata.Utils.setOverlay($dsContainer, true);
  $.ajax({
    url: ctx + '/dataset/m/' + guids,
    type: 'get',
    dataType: 'json',
    success: function(result) {
      com.bouncingdata.Utils.setOverlay($dsContainer, false);
      me.renderDatasets(result, $dsContainer);
      //me.tabsIndex[me.getTabIndex($tab.prop('id'))].dsloaded = true;
      $tab.dsloaded = true;
    },
    error: function(result) {
      console.debug('Failed to load datasets.');
      console.debug(result);
    }
  });
}

/**
 * @param datasetDetailMap = { guid: {guid, name, data} }
 */
Workbench.prototype.renderDatasets = function(datasetDetailMap, $dsContainer) {
  var me = this;
  var htmlToAppend = [];
  var i = 0;
  for (guid in datasetDetailMap) {
    var name = datasetDetailMap[guid].name;
    htmlToAppend[i++] = '<div class="dataset-item" style="margin-top: 2em;" dsguid="' + guid + '"><div class="dataset-item-title">'
      + '<strong><a target="_blank" href="' + ctx + '/dataset/view/' + guid + '">' + name + '</a></strong></div><table class="dataset-item-table"></table></div>';
  }
  
  //$dsContainer.empty();
  $dsContainer.append(htmlToAppend.join());
  
  $('.dataset-item', $dsContainer).each(function() {
    var $dsItem = $(this);
    var guid = $dsItem.attr('dsguid');
    var $table = $('table.dataset-item-table', $dsItem);
    var size = datasetDetailMap[guid].size;
    var data = datasetDetailMap[guid].data;
    if (data) {
      me.renderDatatable($.parseJSON(datasetDetailMap[guid].data), $table);
    } else if (size > 0) {
      console.debug("Load datatable by Ajax...");
      var columns = datasetDetailMap[guid].columns;
      me.loadDatatableByAjax(guid, columns, $table);
    }    
  });
}

Workbench.prototype.renderAttachments = function(attachmentList, $dsContainer) {
  var me = this;
  var htmlToAppend = [];
  var i = 0;
  for (index in attachmentList) {
    var attachment = attachmentList[index];
    var name = attachment.name;
    htmlToAppend[i++] = '<div attIdx="' + index + '" class="attachment-item" style="margin-top: 2em;"><span class="attachment-item-title">'
      + '<strong><a href="javascript:void(0)">' + name + '</a></strong></span><table class="attachment-item-table"></table></div>';
  }
  
  $dsContainer.append(htmlToAppend.join());
  
  $('.attachment-item', $dsContainer).each(function() {
    var $attItem = $(this);
    var index = $attItem.attr('attIdx');
    var $table = $('table.attachment-item-table', $attItem);
    me.renderDatatable($.parseJSON(attachmentList[index].data), $table);
  });
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
  com.bouncingdata.Utils.renderDatatable(data, $table);
}

Workbench.prototype.loadDatatableByAjax = function(dsGuid, columns, $table) {
  var aoColumns = [];
  for (idx in columns) {
    aoColumns.push({ "mDataProp": columns[idx], "sTitle": columns[idx] });
  }
  var datatable = $table.dataTable({
    "bServerSide": true,
    "bProcessing": true,
    "sAjaxSource": ctx + "/dataset/ajax/" + dsGuid,
    "aoColumns": aoColumns,
    "bJQueryUI": true,
    "sPaginationType": "full_numbers"
  });
  
  var keys = new KeyTable( {
    "table": $table[0],
    "datatable": datatable
  });
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

Workbench.prototype.updateActionStatus = function(app) {
  var isSaved = app!=null;
  var isOwner = app && (app.user.username == com.bouncingdata.Main.username);
  if (isSaved) {
    if (isOwner) {
      $('.app-actions #save-app').show();
      $('.app-actions #run-app').show();
      $('.app-actions #copy-app').show();
      $('.app-actions #publish-app').show().children().text(app.published?'Make private':'Publish');
    } else {
      $('.app-actions #save-app').hide();
      $('.app-actions #run-app').hide();
      $('.app-actions #copy-app').show();
      $('.app-actions #publish-app').hide();
    }
    
  } else {
    $('.app-actions #save-app').show();
    $('.app-actions #run-app').show();
    $('.app-actions #copy-app').hide();
    $('.app-actions #publish-app').hide();
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
