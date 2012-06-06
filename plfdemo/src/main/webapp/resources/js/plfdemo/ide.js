/**
 * This class represents multiple tabs IDE component.  
 */
function IDE() {
  
  // the tabs counter, always increase
  this.tabsCounter = 0;
  
  // current opening applications (only saved apps), key: app id, value: tab info { index, app info }
  this.tabsApp = {};
  
  // key: index (1, 2, 3, ...), value: app id, if the tab belongs to new app., value = null 
  this.tabsIndex = {};
  
  // current working app 
  this.currentApp = {};
  
  this.$tabs = {};
  
  // use to name untitled tabs
  this.untitledCounter = 0;
  
  // key: tab id (tabs-1, tabs-2, ...), value: session info {output, variables}
  this.sessions = {};
  
}

var workspaceInfoTemplate = "<div class=\"workspace-info-tabs\" id=\"{tab_id}-workspace-info-tabs\"><ul><li><a href=\"#{tab_id}-workspace-output\">Output</a></li>"
  + "<li><a href=\"#{tab_id}-workspace-variables\">Variables</a></li></ul><div id=\"{tab_id}-workspace-output\"><div id=\"{tab_id}-console\" class=\"prompt\" style=\"display: block;\"></div>" 
  + "<div class=\"console-actions\"><input class=\"clear-console\" type=\"button\" value=\"Clear console\" onclick=\"plfdemo.Workspace.clearConsole();\" />"
  + "</div></div><div id=\"{tab_id}-workspace-variables\">Workspace variables</div></div>";

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
        if (me.currentApp) {
          $appInfo.append("<div class='app-title'>" + me.currentApp.name + "</div>"); 
          $appInfo.append("<div class='app-language' id='app-language'>" + me.currentApp.language + "</div>");
          $appInfo.append("<div class='app-author'>" + me.currentApp.author + "</div>");
          $('#code-editor', $appEditor).val(me.currentApp.code);
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
      var index = ui.index + 1;
      plfdemo.Workspace.setSession("tabs-" + index);
      $('#code-editor', $('#tabs-' + index)).focus();
    });
    
    // handles tab closing
    $(".tab-header span.ui-icon-close", me.$tabs).live("click", function() {
      if (me.getNumberOfTabs() == 1) {
        return;
      }
      var index = $("li", me.$tabs).index($(this).parent());
      me.$tabs.tabs("remove", index);
      //me.tabsCounter;
      delete me.tabsIndex[index+1];
    });
    
    me.createTab(null);
  });
  
}

/**
 * Creates a new tab
 * @param app object
 */
IDE.prototype.createTab = function(app) {
  // check if this action about to open an existing app. or blank tab for new app.
  if (app && app.id in this.tabs_info) {
    this.$tabs.tabs('select', '#tabs-' + this.tabs_info[appid].index);
    this.currentApp = this.tabs_info[appid].app;
    return;
  }
  
  // new tab
  if (!app) {
    this.tabsCounter++;
    this.untitledCounter++;
    this.currentApp = null;
    this.tabsIndex[this.tabsCounter] = null;
    this.$tabs.tabs('add', '#tabs-' + this.tabsCounter, 'Untitled' + this.untitledCounter);
  } else {
    // 
    this.currentApp = app;
    this.tabsCounter++;
    this.tabsIndex[this.tabsCounter] = app.id
    this.tabsInfo[app.id] = { index: this.tabsCounter, app: app };
    this.$tabs.tabs('add', '#tabs-' + this.tabsCounter, 'Untitled' + this.untitledCounter);
  }
  
  this.sessions['tabs-' + this.tabsCounter] = {};
  this.$tabs.tabs('select', '#tabs-' + this.tabsCounter);
 
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
 * Gets the current number of tabs, it differs from tab counter
 */
IDE.prototype.getNumberOfTabs = function() {
  return $('li.tab-header a', this.$tabs).length;
}

plfdemo.workspace = {};
plfdemo.workspace.IDE = new IDE(); 
plfdemo.workspace.IDE.init();