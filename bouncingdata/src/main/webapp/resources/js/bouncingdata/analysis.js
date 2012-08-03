function Analysis() {
  
}

Analysis.prototype.init = function() {
  
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
