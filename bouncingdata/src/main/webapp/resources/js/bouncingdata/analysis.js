function Analysis() {
  
}

Analysis.prototype.init = function(guid) {
  $('#anls-content').easytabs();
  
  $('#comment-form #comment-submit').click(function() {
    // validate
    if (!$('#comment-title').val() || !!$('#comment-content').val()) return;
    
    // post comment
    $.ajax({
      url: ctx + '/anls/commentpost/' + guid,
      type: 'post',
      data: {
        title: $('#comment-title').val(),
        content: $('#comment-content').val(),
        parentId: -1
      }, 
      success: function(result) {
        
      }, 
      error: function(result) {
        
      }
    });
  });
  
  this.loadCommentList(guid);
}


/**
 * Loads the comment list for specific analysis
 * @param guid the analysis guid
 */
Analysis.prototype.loadCommentList = function(guid) {
  // fetch comment list from server
  $.ajax({
    url: ctx + '/anls/commentlist/' + guid,
    type: 'get',
    dataType: 'json',
    success: function(result) {
      console.debug("Received comment list:");
      console.debug(result);
    },
    error: function(result) {
      console.debug(result);
    }
  });
  
  // render comments
  
}

Analysis.prototype.addComment = function(guid, commentObj) {
  
}

Analysis.prototype.reload = function() {
  
}

com.bouncingdata.Analysis = new Analysis();
