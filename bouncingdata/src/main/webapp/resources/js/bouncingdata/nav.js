function Nav() {
  this.selected = null;
}

Nav.prototype.init = function() {
  $('#page>.main-container>.main-navigation .nav-item').each(function() {
    var $form = $('form', this);
    var $link = $('a.nav-item-link', this);
    
    $link.bind('click', function(e) {
      com.bouncingdata.Main.toggleAjaxLoading(true);
      /*var $oldSelected = $('#page>.main-container>.main-navigation div.nav-item-selected');
      if ($oldSelected) {
        $oldSelected.removeClass('nav-item-selected');
        var oldPageId = $('a.nav-item-link', $oldSelected).prop('id');
        if (oldPageId == "nav-create-link") {
          //
          com.bouncingdata.Workbench.dispose();
        }      
      }
      $(this).parent().addClass('nav-item-selected');*/
            
      if (!e.originalEvent["isBackAction"]) {
        window.history.pushState({linkId: $(this).attr('id'), type:'page'}, $('.nav-item-text', $(this)).text(), $form.attr('action'));
      }
    });
    
    // load page
    Spring.addDecoration(new Spring.AjaxEventDecoration({
      elementId: $link.attr('id'),
      formId: $form.attr('id'),
      event: "onclick",
      params: {fragments: "main-content"}
    }));
    
  });
  
  // register ajax loading handler
  Spring.addDecoration(new Spring.AjaxEventDecoration({
    elementId: 'hiddenLinkForAjax',
    event: "onclick",
    params: {fragments: "main-content"}
  }));
  
  $('#hiddenLinkForAjax').click(function(e) {
    com.bouncingdata.Main.toggleAjaxLoading(true);
    
    /*var $oldSelected = $('#page>.main-container>.main-navigation div.nav-item-selected');
    if (($oldSelected.length > 0) && ($oldSelected.prop('id') != 'nav-home')) {
      $oldSelected.removeClass('nav-item-selected');
      var oldPageId = $('a.nav-item-link', $oldSelected).prop('id');
      if (oldPageId == "nav-create-link") {
        //
        com.bouncingdata.Workbench.dispose();
      }      
    }
    
    $('#page>.main-container>.main-navigation div.nav-item#nav-home').addClass('nav-item-selected');*/
    
    
    
    if (!e.originalEvent["isBackAction"]) {
      window.history.pushState({linkId: $(this).prop('href')}, null, $(this).prop('href'));
    }
    return false;
  });
  
  // hide the ajax loading status when received response
  dojo.connect(Spring.RemotingHandler.prototype, 'handleResponse', null, function() {
    com.bouncingdata.Main.toggleAjaxLoading(false);
  });
  
  // manage history
  window.onpopstate = function(e) {
    if(e.state) {
      var popped = ('state' in window.history && window.history.state !== null);
      var linkId = e.state.linkId;
      var type = e.state.type;
      console.debug('Pop state: linkId = ' + linkId + ', type = ' + type);
      var event;
      if (type == 'page') {
        var $link = $('a#' + linkId);
        if (!linkId || $link.length < 1) {
          location.reload();
          return;
        }
        // create a native event on nav. link
        if (document.createEvent) {
          event = document.createEvent("HTMLEvents");
          event.initEvent("click", false, true);
          event["isBackAction"] = true;
          $link[0].dispatchEvent(event);
        } else { // IE
          event = document.createEventObject();
          event.eventType = "click";
          event["isBackAction"] = true;
          $link[0].fireEvent("on" + event.eventType, event);
        }
      } else {
        com.bouncingdata.Nav.fireAjaxLoad(linkId, true);
      }
      
    } else {
      // TODO: have problem with Chrome, which always fire onpopstate when loading page   
      //location.reload();
      console.debug("Onpopstate fired with no state.");
    }
  }
    
}

/**
 * Sets current selected item
 * @param type one of 'page', 'anls', 'data', 'search'
 * @param ref the reference string, it's the page name ('create', 'stream', ...) in case of type 'page',</br> 
 * guid in case of type 'anls' or 'data', query string if type is 'search'
 */
Nav.prototype.setSelected = function(type, ref) {
  var $oldSelected = $('#page>.main-container>.main-navigation div.nav-item-selected');
  if ($oldSelected) {
    $oldSelected.removeClass('nav-item-selected');
    var oldPageId = $('a.nav-item-link', $oldSelected).prop('id');
    if (oldPageId == "nav-create-link" && this.selected.type == 'page' && this.selected.ref == 'create') {
      com.bouncingdata.Workbench.dispose();
    }      
  }
  
  this.selected = {
    'type': type,
    'ref': ref
  };
  
  if (type == 'page') {
    $('#page>.main-container>.main-navigation div.nav-item#nav-' + ref).addClass('nav-item-selected');
  } else {
    $('#page>.main-container>.main-navigation div.nav-item#nav-stream').addClass('nav-item-selected');
  }
}

/**
 * 
 */
Nav.prototype.fireAjaxLoad = function(link, isBack) {
  var $hiddenLink = $('#hiddenLinkForAjax');
  $hiddenLink.prop('href', link);
  var event;
  if (document.createEvent) {
    event = document.createEvent("HTMLEvents");
    event.initEvent("click", false, true);
    event["isBackAction"] = isBack;
    $hiddenLink[0].dispatchEvent(event);
  } else { //IE?
    event = document.createEventObject();
    event.eventType = "click";
    event["isBackAction"] = isBack;
    $hiddenLink[0].fireEvent("on" + event.eventType, event);
  }
}

Nav.prototype.openWorkbench = function() {
  //create a native event on workbench navigation link
  var $link = $('a#nav-create-link');
  if (document.createEvent) {
    event = document.createEvent("HTMLEvents");
    event.initEvent("click", false, true);
    event["isBackAction"] = false;
    $link[0].dispatchEvent(event);
  } else { // IE
    event = document.createEventObject();
    event.eventType = "click";
    event["isBackAction"] = false;
    $link[0].fireEvent("on" + event.eventType, event);
  }
}

com.bouncingdata.Nav = new Nav();