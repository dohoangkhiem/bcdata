function ActivityStream() {
  
}

ActivityStream.prototype.init = function() {
  $('#stream .event').each(function() {
    var $title = $('.title a', $(this));
    var $thumb = $('.thumbnail a', $(this));
    
    var clickF = function($ele, evt) {
      Main.prototype.toggleAjaxLoading(true);
      //
      if (!evt.originalEvent["isBackAction"]) {
        window.history.pushState({linkId: $ele.prop('href'), type: 'anls'}, $ele.prop('id'), $ele.prop('href'));
      }
      evt.preventDefault();
    }
    
    $title.click(function(e) {
      clickF($(this), e);
    });
    
    $thumb.click(function(e) {
      clickF($(this), e);
    });
    
    Spring.addDecoration(new Spring.AjaxEventDecoration({
      elementId: $title.prop('id'),
      event: "onclick",
      params: {fragments: "main-content"}
    }));
    
    Spring.addDecoration(new Spring.AjaxEventDecoration({
      elementId: $thumb.prop('id'),
      event: "onclick",
      params: {fragments: "main-content"}
    }));
    
  });
  
}

/**
 * Loads more recent activity stream.
 * @param lastId the last (oldest) activity id currently in activity stream.
 */
ActivityStream.prototype.loadMore = function(lastId) {
  $.ajax({
    url: '',
    
  });
}


com.bouncingdata.ActivityStream = new ActivityStream();
