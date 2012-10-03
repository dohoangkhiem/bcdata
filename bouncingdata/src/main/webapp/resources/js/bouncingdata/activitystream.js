function ActivityStream() {
  
}

ActivityStream.prototype.init = function() {
  $('#stream .event').each(function() {
    var $title = $('.title a', $(this));
    var $thumb = $('.thumbnail a', $(this));
    var $comment = $('.event-footer a.comments-link', $(this))
    var name = $title.text();
    var href = $title.prop('href');
    
    $title.click(function(e) {
      Main.prototype.toggleAjaxLoading(true);
      window.history.pushState({linkId: href, type: 'anls'}, name, href);
      e.preventDefault();
    });
    
    $thumb.click(function(e) {
      Main.prototype.toggleAjaxLoading(true);
      window.history.pushState({linkId: href, type: 'anls'}, name, href);
      e.preventDefault();
    });
    
    $comment.click(function(e) {
      Main.prototype.toggleAjaxLoading(true);
      window.history.pushState({linkId: href, type: 'anls'}, name, href);
      e.preventDefault();
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
    
    Spring.addDecoration(new Spring.AjaxEventDecoration({
      elementId: $comment.prop('id'),
      event: "onclick",
      params: {fragments: "main-content"}
    }));
    
  });
  
  com.bouncingdata.Nav.setSelected('page', 'stream');
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
