function Analysis() {
  
}

Analysis.prototype.init = function(guid) {
  $('#anls-content').easytabs();
  var me = this;
  
  $('#comment-form #comment-submit').click(function() {
    // validate
    var message = $('#comment-form #message').val();
    if (!message) return;
    me.postComment(guid, message, -1, function() { $('#comment-form #message').val(''); });
  });
  
  $('.comments h3.comments-count').click(function() {
    $(this).next().toggle('slow');
  }).css('cursor', 'pointer');
  
  this.loadCommentList(guid); 
  
  this.$commentTemplate = $('#comment-template').template();
  this.$commentEditor = $('#comment-editor-template').template();
  
  // callback function when click on 'reply'
  this.inlineReplyFunction = function() {
    // 
    var $commentBody = $(this).parent().parent();
    var $comment = $commentBody.parent();
    if ($commentBody.next().is('div.inline-editor')) {
      $commentBody.next().remove();
      return false;
    }
    var $inlineEditor = $.tmpl(me.$commentEditor, { rows: 3 });
    $commentBody.after($inlineEditor);
    $('input.reply-button', $inlineEditor).click(function() {
      var message = $(this).prev().val();
      if (!message) return false;
      me.postComment(guid, message, $comment.attr('nodeid'), function() {$commentBody.next().remove();});
    }).button();
    return false;
  }
  
  var $score = $('.anls-header .anls-score');
  var score = $score.text();
  if (score > 0) {
    $score.attr('class', 'anls-score anls-score-positive');
  } else {
    if (score == 0) $score.attr('class', 'anls-score'); 
    else $score.attr('class', 'anls-score anls-score-negative');
  }
  
  $('.anls-header a.anls-vote-up').click(function() {
    me.voteAnalysis(guid, 1);
    return false;
  });
  
  $('.anls-header a.anls-vote-down').click(function() {
    me.voteAnalysis(guid, -1);
    return false;
  });
  
  this.votingCache = {};
  
  // embedded
  var $embedded = $('#embedded-link').hide();
  $('.anls-action-links a#anls-embed-button').click(function() {
    $embedded.toggle('slow');
    // still not reversed the remote ip to hostname, temporarily hard code the host
    var host = "www.bouncingdata.com";
    var embedded = '<iframe src="http://' + host + ctx + '/public/embed/' + guid + '" style="border: 0" width="800" height="600" frameborder="0" scrolling="no"></iframe>';
    $('#embedded-link-text', $embedded).val(embedded).click(function() { $(this).select() });
  });
}

Analysis.prototype.postComment = function(guid, message, parentId, callback) {
  var me = this;
  if (!message) return;
  // post comment
  $.ajax({
    url: ctx + '/anls/commentpost/' + guid,
    type: 'post',
    data: {
      message: message,
      parentId: parentId
    }, 
    success: function(result) {
      if (!result) {
        console.debug("Comment post failed!");
        return;
      }
      console.debug("Comment post successfully!");
      me.addComment(guid, {
        id: result.id,
        message: message,
        parentId: parentId,
        lastUpdate: new Date(),
        user: { username: com.bouncingdata.Main.username }
      });
      
      if (callback) callback();
    }, 
    error: function(result) {
      console.debug("Comment post failed!");
    }
  });
}

/**
 * Loads the comment list for specific analysis
 * @param guid the analysis guid
 */
Analysis.prototype.loadCommentList = function(guid) {
  var me = this;
  
  // represents the hierachy, key: commentId, value: array of childId
  me.children = {};
  
  // array of root node
  me.roots = [];
  
  // maps the commentId with commentObj
  me.commentList = {};
  
  // fetch comment list from server
  $.ajax({
    url: ctx + '/anls/commentlist/' + guid,
    type: 'get',
    dataType: 'json',
    success: function(result) {      
      var $commentList = $('#comment-list');
      $commentList.css('background', '#fff');
      
      me.commentCount = result.length;
      
      me.updateCommentCounter();
      
      if (result.length==0) {
        return;
      }
      
      // build comment list hierachy     
      for (var i = 0; i < result.length; i++) {
        var id = result[i].id;
        var parentId = result[i].parentId;
        me.commentList[id] = result[i];   
        if (parentId > 0) {          
          if (!me.children[parentId]) me.children[parentId] = [ id ];
          else me.children[parentId].push(id);
        } else {
          me.roots.push(id);
        }   
      }
      
      // render comments
      var commentToInsert = [];
      for (var i = 0; i < me.roots.length; i++) {
        commentToInsert[i]= me.renderCommentNode(me.roots[i]);
      }
      
      $commentList.append(commentToInsert.join(''));
      
      $('a.comment-reply', $commentList).click(me.inlineReplyFunction);
      
      $('a.up-vote-link', $commentList).click(function() {
        var $comment = $(this).parent().parent().parent();
        me.voteComment(guid, $comment.attr('nodeid'), 1);
        return false;
      });
      
      $('a.down-vote-link', $commentList).click(function() {
        var $comment = $(this).parent().parent().parent();
        me.voteComment(guid, $comment.attr('nodeid'), -1);
        return false;
      });
      
      $('.comment-score', $commentList).each(function() {
        var $score = $(this);
        var score = $score.text();
        if (score > 0) {
          $score.attr('class', 'comment-score comment-score-positive');
        } else if (score == 0) $score.attr('class', 'comment-score');
        else $score.attr('class', 'comment-score comment-score-negative');
      });
    },
    error: function(result) {
      console.debug(result);
    }
  });
  
}

/**
 * Recursively generate the comment list html structure
 **/
Analysis.prototype.renderCommentNode = function(id) {
  var commentObj = this.commentList[id];
  if (!commentObj) return '';
  
  var $comment = $.tmpl(this.$commentTemplate, { id: id, 
    username: commentObj.user.username, 
    message: commentObj.message,
    date: new Date(commentObj.lastUpdate),
    upVote: commentObj.upVote,
    downVote: commentObj.downVote
  });
  // leaf node
  if (!this.children[id] || this.children[id].length <= 0) {    
    /** see main.js#jQuery.fn.outerHtml */ 
    return $comment.outerHtml();
  } else {    
    var $children = $comment.children('ul.children');
    // recursive
    for (childId in this.children[id]) {
      $children.append(this.renderCommentNode(this.children[id][childId]));
    }
    return $comment.outerHtml();
  }
}

Analysis.prototype.addComment = function(guid, commentObj) {
  var me = this;
  this.commentCount++;
  this.updateCommentCounter();
  var $commentList = $('#comment-list');
  var id = commentObj.id;
    
  var $comment = $.tmpl(this.$commentTemplate, { id: id, 
    username: commentObj.user.username, 
    message: commentObj.message,
    date: new Date(commentObj.lastUpdate),
    upVote: 0,
    downVote: 0
  });
  
  console.debug($comment);
  
  if (commentObj.parentId < 1) {
    $commentList.append($comment);
  } else {
    // find parent
    var $parent = $('#comment-' + commentObj.parentId , $commentList);
    if (!$parent) return false;
    $parent.children('ul.children').append($comment);
  }
  
  $('a.comment-reply', $comment).click(this.inlineReplyFunction);
  
  $('a.up-vote-link', $commentList).click(function() {
    var $comment = $(this).parent().parent().parent();
    me.voteComment(guid, $comment.attr('nodeid'), 1);
    return false;
  });
  
  $('a.down-vote-link', $commentList).click(function() {
    var $comment = $(this).parent().parent().parent();
    me.voteComment(guid, $comment.attr('nodeid'), -1);
    return false;
  });
}

Analysis.prototype.voteAnalysis = function(guid, vote) {
  var me = this;
  if (this.votingCache[guid] && this.votingCache[guid] * vote > 0) {
    console.debug("You have voted this analysis " + guid + "already");
    return;
  }
  
  if (!this.votingCache[guid]) this.votingCache[guid] = 0;
  
  $.ajax({
    url: ctx + "/anls/vote/" + guid,
    data: {
      vote: vote
    },
    type: 'post',
    success: function(result) {
      var $score = $('.anls-header .anls-score');
      if (vote >= 0) {
        me.votingCache[guid]++;          
        $score.text($score.text() - (-1));
      } else {
        me.votingCache[guid]--;
        $score.text($score.text() - 1);
      }
      var score = $score.text();
      if (score > 0) {
        $score.attr('class', 'anls-score anls-score-positive');
      } else {
        if (score == 0) $score.attr('class', 'anls-score'); 
        else $score.attr('class', 'anls-score anls-score-negative');
      }
    },
    error: function(result) {
      console.debug("Failed to vote analysis " + guid);
      console.debug(result);
    }
  });
}

Analysis.prototype.voteComment = function(guid, commentId, vote) {
  var me = this;
  if (this.votingCache[commentId] && (this.votingCache[commentId] * vote > 0)) {
    console.debug("You have voted this comment #" + commentId + " already");
    return;
  }
  
  if (!this.votingCache[commentId]) this.votingCache[commentId] = 0;
  
  $.ajax({
    url: ctx + '/anls/commentvote/' + guid,
    data: {
      commentId: commentId,
      vote: vote
    },
    type: 'post',
    success: function(result) {
      var $commentBody = $('li.comment-item#comment-' + commentId + ' > div.comment-item-body');
      var $score = $('span.comment-score', $commentBody);
      if (vote >= 0) {
        me.votingCache[commentId]++;          
        $score.text($score.text() - (-1));
      } else {
        me.votingCache[commentId]--;
        $score.text($score.text() - 1);
      }
      var score = $score.text();
      if (score > 0) {
        $score.attr('class', 'comment-score comment-score-positive');
      } else {
        if (score == 0) $score.attr('class', 'comment-score'); 
        else $score.attr('class', 'comment-score comment-score-negative');
      }
    },
    error: function(result) {
      console.debug("Failed to vote comment #" + commentId);
      console.debug(result);
    }
  });
}

Analysis.prototype.updateCommentCounter = function() {
  $('.comments .comments-count').text(this.commentCount + " comments");
}

Analysis.prototype.reload = function() {
  
}

com.bouncingdata.Analysis = new Analysis();
