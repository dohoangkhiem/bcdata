function Nav() {
  selected = null;
}

Nav.prototype.init = function() {
  $(function() {
    $('#page>.main-container>.main-navigation .nav-item').each(function() {
      var $form = $('form', this);
      var $link = $('a.nav-item-link', this);
      
      $link.bind('click', function(e) {
        com.bouncingdata.Main.toggleAjaxLoading(true);
        var $oldSelected = $('#page>.main-container>.main-navigation div.nav-item-selected');
        if ($oldSelected) {
          $oldSelected.removeClass('nav-item-selected');
          var oldPageId = $('a.nav-item-link', $oldSelected).prop('id');
          if (oldPageId == "nav-create-link") {
            //
            com.bouncingdata.Workbench.dispose();
          }      
        }
        $(this).parent().addClass('nav-item-selected');
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
    
    // register load analysis ajax handler
    Spring.addDecoration(new Spring.AjaxEventDecoration({
      elementId: 'hiddenLinkForAjax',
      event: "onclick",
      params: {fragments: "main-content"}
    }));
    
    $('#hiddenLinkForAjax').click(function(e) {
      com.bouncingdata.Main.toggleAjaxLoading(true);
      $(this).prop('href', '');
      return false;
    });
    
    // hide the ajax loading status when receive response
    dojo.connect(Spring.RemotingHandler.prototype, 'handleResponse', null, function() {
      com.bouncingdata.Main.toggleAjaxLoading(false);
    });
    
    // manage history
    window.onpopstate = function(e) {
      if(e.state) {
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
          // create a native event
          if (document.createEvent) {
            event = document.createEvent("HTMLEvents");
            event.initEvent("click", false, false);
            event["isBackAction"] = true;
            $link[0].dispatchEvent(event);
          } else { // IE
            event = document.createEventObject();
            event.eventType = "click";
            event["isBackAction"] = true;
            $link[0].fireEvent("on" + event.eventType, event);
          }
        } else if (type == 'anls') {
          if (document.createEvent) {
            event = document.createEvent("HTMLEvents");
            event.initEvent("click", false, false);
            $('#hiddenLinkForAjax').prop('href', linkId);
            $('#hiddenLinkForAjax')[0].dispatchEvent(event);
          } else {
            event = document.createEventObject();
            event.eventType = "click";
            $('#hiddenLinkForAjax')[0].fireEvent("on" + event.eventType, event);
          }
        }
        
      } else {
        // TODO: have problem with Chrome, which always fire onpopstate when loading page   
        //location.reload();
        console.debug("Onpopstate fired with no state.");
      }
    }
    
  });
}

/**
 * Set selected item
 */
Nav.prototype.setSelected = function() {
  this.selected = {};
}

com.bouncingdata.Nav = new Nav();