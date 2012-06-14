/**
 * This class represents multiple tabs IDE component.  
 */
function IDE() {
  
  // the tabs counter, always increase, = the number of tabs which was created
  this.tabsCounter = 0;
  
  // current opening applications (only saved apps), key: app guid, value: tab info { tabId, app info }
  this.tabsInfo = {};
  
  // key: index (0, 1, 2, ...), value: app guid, if the tab belongs to new app., value = null 
  this.tabsIndex = [];
  
  // current working app 
  this.currentApp = {};
  
  this.$tabs = {};
  
  // use to name untitled tabs
  this.untitledCounter = 0;
  
  // Updated: change key to tabId (tabs-1, tabs-3, ...) as the tab index is too transiently & not consistent.
  // From tabId we can easily find the index and vice-versa
  this.sessions_ = {};
}

/**
 * Initializes the IDE with multiple tabs
 */
IDE.prototype.init = function() {
  var me = this;
  $(function() {
    // initializes multiple tabs editor
    me.$tabs = $('#app-editor-tabs').tabs({
      tabTemplate: "<li class='tab-header editor-tab-header'><a href='#{href}'>#{label}</a> <span class='ui-icon ui-icon-close' title='Close this tab'>Remove Tab</span></li>",
      
      add: function(event, ui) {
        var $tabContentContainer = $("<div class='editor-tab-content' id='editor-tab-content-" + me.tabsCounter + "'></div>");
        var $appInfo = $("<div class='app-info'></div>");
        var $appEditor = $("<div class='app-code-editor'><textarea rows='15' id='code-editor' " +
        		"class='code-editor' spellcheck='false'></textarea></div>");
        $tabContentContainer.append($appInfo);
        $tabContentContainer.append($appEditor);
        var guid = me.tabsIndex[ui.index];
        if (guid) {
          var app = me.tabsInfo[guid].app;
          if (app) {
            $appInfo.append("<div class='app-title'><label style='font-weight: bold;'>Application name: </label>" + app.name + "</div>"); 
            $appInfo.append("<div class='app-language' id='app-language'><label style='font-weight: bold;'>Language: </label>" + app.language + "</div>");
            $appInfo.append("<div class='app-author'><label style='font-weight: bold;'>Author: </label>" + app.authorName + "</div>");
            $('#code-editor', $appEditor).val(app.code);
          }
        } else {
          $appInfo.append("<div class='language-select'><span>Language: </span><select id='app-language' class='app-language-select'><option value='python'>Python</option><option value='r'>R</option></select></div>");
        }
        
        //var $workspaceInfo = $(workspaceInfoTemplate.replace('{tab_id}', 'tabs-' + me.tabsCounter));
        //$tabContentContainer.append($workspaceInfo);
        $(ui.panel).append($tabContentContainer);   
        
        //$workspaceInfo.tabs();
      },
      
      show: function(event, ui) {
        
      },
      spinner: "Loading application..."
    });
    
    // handles double click on tab bar
    $('#app-editor-tabs .editor-tabs-bar').dblclick(function(e) {
      var $target = $(e.target);
      if ($target[0].nodeName == 'a' || $target[0].nodeName == 'A') {
        $target = $target.parent();
      }
      if ($target.hasClass('editor-tab-header')) return false;
      me.createTab(null);
      //alert('I will create new tab for u. Thanks!');
      return false;
    });
    
    // bind corresponding session when select a tab
    me.$tabs.bind("tabsselect", function(event, ui) {
      // the ui.index or 'selected' option does not work in the case this tab is auto-selected after delete the 
      // previous tab, as the ui.index value still is the old index of current tab (it should be ui.index - 1)
      var index = $("li", me.$tabs).index($(ui.tab).parent());
      console.debug("Selected tab index: " + ui.index);
      plfdemo.Workspace.setSession(me.getTabId(index));
      $('#code-editor', me.getTabContainer(index)).focus();
      
    });
    
    // when a tab is showed
    me.$tabs.bind("tabsshow", function(event, ui) {
      var index = ui.index; 
      console.debug("Show tab index: " + index);
      
      $('.app-actions #save-app').show();
      $('.app-actions #run-app').show();
      $('.app-actions #copy-app').hide();
      
      // in case of open app. of other user, disable the 'Save' button 
      var guid = me.tabsIndex[index];
      if (guid) {
        var app = me.tabsInfo[guid].app;
        if (app && (app.authorName != plfdemo.Main.username)) {
          $('.app-actions #save-app').hide();
          $('.app-actions #run-app').hide();
          $('.app-actions #copy-app').show();
        }
      }
    });
    
    // handles tab closing
    $(".tab-header span.ui-icon-close", me.$tabs).live("click", function() {
      var index = $("li", me.$tabs).index($(this).parent());
      me.removeTab(index);
    });
    
    me.createTab(null);
  });
  
}

/**
 * Creates a new tab
 * @param app object
 */
IDE.prototype.createTab = function(app) {

  // index (zero-based) of the newly created tab
  var index = this.getNumberOfTabs();
  
  // new tab
  if (!app) {
    this.tabsCounter++;
    this.untitledCounter++;
    this.currentApp = null;
    this.tabsIndex[index] = null;
    this.$tabs.tabs('add', '#tabs-' + this.tabsCounter, 'Untitled' + this.untitledCounter);
  } else {
    // 
    this.currentApp = app;
    this.tabsCounter++;
    this.tabsIndex[index] = app.guid
    this.tabsInfo[app.guid] = { tabId: 'tabs-' + this.tabsCounter, app: app };
    this.$tabs.tabs('add', '#tabs-' + this.tabsCounter, app.name);
  }
  
  this.sessions_['tabs-' + this.tabsCounter] = {};
  this.$tabs.tabs('select', '#tabs-' + this.tabsCounter);
 
}

IDE.prototype.removeTab = function(index) {
  if (this.getNumberOfTabs() == 1) {
    return;
  }
  var guid = this.tabsIndex[index];
  this.tabsIndex.splice(index, 1);
  delete this.sessions_[this.getTabId(index)];
  if (guid) {
    delete this.tabsInfo[guid];
  }
  
  this.$tabs.tabs("remove", index);
}

/**
 * 
 */
IDE.prototype.bindAppToTab = function(index, app) {
  if (!app) return;
  var tabId = this.getTabId(index);
  this.tabsIndex[index] = app.guid;
  this.tabsInfo[app.guid] = { tabId: tabId, app: app};
  
  // change tab's title
  $($('li.tab-header a', this.$tabs)[index]).text(app.name);
  
  // append app. info
  var $tabContainer = this.getTabContainer(index);
  var $tabContentContainer = $(".editor-tab-content", $tabContainer);
  if ($('.app-info', $tabContentContainer)) {
    $('.app-info', $tabContentContainer).remove();
  }
  var $appInfo = $("<div class='app-info'></div>");
  var $appEditor = $(".app-code-editor", $tabContentContainer);
   
  $appInfo.append("<div class='app-title'><label style='font-weight: bold;'>Application name: </label>" + app.name + "</div>"); 
  $appInfo.append("<div class='app-language' id='app-language'><label style='font-weight: bold;'>Language: </label>" + app.language + "</div>");
  $appInfo.append("<div class='app-author'><label style='font-weight: bold;'>Author: </label>" + app.authorName + "</div>");
  $appInfo.insertBefore($appEditor);
}

/**
 * Gets the current selected tab index (zero-based: 0, 1, 2, ...)
 */
IDE.prototype.getSelectedIndex = function() {
  return this.$tabs.tabs('option', 'selected');
}

/**
 * Gets the current selected tab id (i.e: tabs-1, tabs-2, ...)
 */
IDE.prototype.getSelectedTabId = function() {
  var index = this.$tabs.tabs('option', 'selected');
  var href = $('li.tab-header a', this.$tabs)[index].href; 
  return href.substring(href.indexOf('#') + 1);
}

/**
 * 
 */
IDE.prototype.getSelectedTabContainer = function() {
  return $('#' + this.getSelectedTabId(), this.$tabs);
}

/**
 * Get id of tab content's container
 * @param index: zero-based tab index
 * @return id of the div element which contains the content of the corresponding tab, i.e: tabs-1 at index 0
 */
IDE.prototype.getTabId = function(index) {
  var href = $('li.tab-header a', this.$tabs)[index].href;
  return href.substring(href.indexOf('#') + 1)
}

/**
 * Gets the div element contains the tab at index (0, 1, 2, ...) 
 */
IDE.prototype.getTabContainer = function(index) {
  var tabId = this.getTabId(index);
  return $('#' + tabId, this.$tabs);
}

/**
 * Gets the index of a tab with specific id
 */
IDE.prototype.getTabIndex = function(tabId) {
  //var a = $("a")
  //var index = $("li", me.$tabs).index($(ui.tab).parent());  
}

/**
 * Gets the current number of tabs, it differs from tab counter
 */
IDE.prototype.getNumberOfTabs = function() {
  return $('li.tab-header a', this.$tabs).length;
}

plfdemo.workspace = {};
plfdemo.workspace.IDE = new IDE(); 
plfdemo.workspace.IDE.init();