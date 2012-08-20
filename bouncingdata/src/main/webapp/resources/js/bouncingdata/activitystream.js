function ActivityStream() {
  
}

ActivityStream.prototype.init = function() {
  
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
