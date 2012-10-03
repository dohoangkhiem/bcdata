function Connect() {
  
}

/**
 * Initializes UI events & handles for connect page
 */
Connect.prototype.init = function() {
  var me = this;
  $('#main-content input:button').button();
  $('#main-content input:submit').button();
  
  $('#main-content>.connect-page-tabs').tabs({
    // each time show the tab
    show: function(event, ui) {       
      if (ui.index == 1) {
        me.loadConnection();
      }
    }
  });
  
  
  $('#find-friends .search-form').submit(function() {
    var query = $('#find-friends #friend-name').val();
    if (!query) return false;
    me.findFriends(query, function(results) {
      var $result = $('#find-friends #find-friends-result');
      if (!results) return;
      $result.empty();
      for(i in results) {
        var user = results[i];
        var $user = $('<div userid="' + user.id + '" class="user-info"><strong>' + user.username + '</strong></div>');
        var $follow;
        if (user.friend) {
          $follow = $('<a href="#" class="unfollow-link">Unfollow</a>');
        } else {
          $follow = $('<a href="#" class="follow-link">Follow</a>');
        }
        $user.append('&nbsp;').append($follow);
        
        $follow.click(function(usr, $this) {
          return function () {
            var follow;
            if ($this.hasClass('unfollow-link')) follow = false;
            else follow = true;
          
            me.follow(usr.id, follow, function() {
              if ($this.hasClass('unfollow-link')) {
                $this.removeClass('unfollow-link');
                $this.addClass('follow-link');
                $this.text('Follow')
              } else {
                $this.removeClass('follow-link');
                $this.addClass('unfollow-link');
                $this.text('Unfollow');
              }
            });
            return false;
          }
        }(user, $follow));
        
        $result.append($user);
      }
    });
    return false;
  });
  
  com.bouncingdata.Nav.setSelected('page', 'connect');
}

/**
 * Finds users based on query string
 * @param username username of finder
 */
Connect.prototype.findFriends = function(query, callback) {
  $.ajax({
    url: ctx + '/connect/find/' + query,
    dataType: 'json',
    success: function(results) {
      if (callback) callback(results);
    }, 
    error: function(results) {
      console.debug(results);
    }
  });
}

Connect.prototype.getFollowers = function(callback) {
  $.ajax({
    url: ctx + '/connect/followers',
    dataType: 'json',
    success: function(results) {
      if(callback) callback(results);
    }, 
    error: function(results) {
      console.debug(results);
    }
  });
}

Connect.prototype.getFollowingUsers = function(callback) {
  $.ajax({
    url: ctx + '/connect/followings',
    dataType: 'json',
    success: function(results) {
      if(callback) callback(results);
    }, 
    error: function(results) {
      console.debug(results);
    }
  });
}

Connect.prototype.getConnections = function(callback) {
  $.ajax({
    url: ctx + '/connect/connections',
    dataType: 'json',
    success: function(results) {
      if(callback) callback(results);
    }, 
    error: function(results) {
      console.debug(results);
    }
  });
}

Connect.prototype.loadConnection = function() {
  var me = this;
  me.getConnections(function(results) {
    var $followers = $('#connections #follower-list');
    var $followings = $('#connections #following-list');
    var followers = results['followers'];
    var followings = results['followings'];
    if (followers && followers.length > 0) {
      $followers.empty();
      for (i in followers) {
        var user = followers[i];
        var $user = $('<div userid="' + user.id + '" class="user-info"><span>' + user.username + '</span></div>');
        var $follow;
        if (user.friend) {
          $follow = $('<a href="#" class="unfollow-link">Unfollow</a>');
        } else {
          $follow = $('<a href="#" class="follow-link">Follow</a>');
        }
        $user.append('&nbsp;').append($follow);
        $follow.click(function(usr, $this) {
          return function() {
            var follow;
            if ($this.hasClass('unfollow-link')) follow = false;
            else follow = true;
            me.follow(usr.id, follow, function() {
              me.loadConnection();
            });
            return false;
          }
        }(user, $follow));
        $followers.append($user);
      }
    } else {
      $followers.text('You have 0 follower.');
    }
    if (followings && followings.length > 0) {
      $followings.empty();
      for (i in followings) {
        var user = followings[i];
        var $user = $('<div userid="' + user.id + '" class="user-info"><span>' 
          + user.username + '</span>&nbsp;<a href="#" class="unfollow-link">Unfollow</a></div>');
        $('a.unfollow-link', $user).click(function(usr) {
          return function() {
            me.follow(usr.id, false, function() {
              me.loadConnection();
            });
            return false;
          };
          
        }(user));
        $followings.append($user);
      }
    } else {
      $followings.text('You are not following anyone.');
    }        
  });
}

/**
 * Follows or unfollows a friend
 * @param targetId id of friend
 * @param follow true/false to follow/unfollow
 * @param successCallback
 * @param errorCallback
 */
Connect.prototype.follow = function(targetId, follow, successCallback, errorCallback) {
  $.ajax({
    url: ctx + '/connect/friendship',
    dataType: 'json',
    type: 'post',
    data: {
      'target': targetId,
      'follow': follow
    },
    success: function(results) {
      if (successCallback) successCallback(results);
    }, 
    error: function(results) {
      console.debug(results);
      if (errorCallback) errorCallback(results);
    }
  });
}

com.bouncingdata.Connect = new Connect();