import httplib
import json
import urllib2
import datastore

def get_data(username):
    # list of user info
    info_list = []
    # list of tweets
    tweet_list = []
    
    info = __get_info(username)
    info_list.append(info)
    tweets = __get_tweets(username)
    tweet_list.append(tweets)
    
    friend_ids = __get_friends(username)
    #print json.dumps(ids)
    print 'Found ' + str(len(friend_ids)) + ' users are followed by ' + username
    for id in friend_ids:
        print 'Getting info about user with id ' + str(id) + '...'
        # get info
        info = __get_info_by_id(id)
        info_list.append(info)
        
        # get tweets
        tweets = __get_tweets_by_id(id)
        
        tweet_list.append(tweets)    
        # get list of friends
        friends_ids_of_friend = __get_friends_by_id(id)
        
        for fid in friends_ids_of_friend:
            # get info, tweets
            info = __get_info_by_id(id)
            info_list.append(info)
            tweets = __get_tweets_by_id(fid)
            tweet_list.append(tweets)
        
    print 'Info list size ' + str(len(info_list))
    print 'Tweets list: number of element ' + str(len(tweet_list))
    print 'A tweet ' + str(tweet_list[0])  
    
    # persist to 'users' table: id, name, description  
    datastore.persist_data('twitter', 'users', info_list, 'id', 'name', 'description')
    
    # persist to 'tweets' table: id, text, user_id
    datastore.persist_data('twitter', 'tweets', tweeets, 'id', 'text', 'user_id')
    
def __get_friends(username):
    f = urllib2.urlopen('https://api.twitter.com/1/friends/ids.json?screen_name=' + username)
    result = f.read()
    obj = json.loads(result)
    ids = obj['ids']
    return ids

def __get_friends_by_id(user_id):
    url = 'https://api.twitter.com/1/friends/ids.json?user_id=' + str(user_id)
    f = urllib2.urlopen(url)
    result = f.read()
    obj = json.loads(result)
    ids = obj['ids']
    return ids

def __get_info(screen_name):
    try:
      url = 'https://api.twitter.com/1/users/show.json?screen_name=' + screen_name
      f = urllib2.urlopen(url)
      result = f.read()
      obj = json.loads(result)
      return obj
    except:
      print 'Failed to get info of user ' + screen_name
      print url

def __get_info_by_id(user_id):
    url = 'https://api.twitter.com/1/users/show.json?user_id=' + str(user_id)
    f = urllib2.urlopen(url)
    result = f.read()
    obj = json.loads(result)
    return obj

def __get_tweets_by_id(user_id):
    print 'Getting tweets of user with id ' + str(user_id)
    url = 'http://api.twitter.com/1/statuses/user_timeline.json?user_id=' + str(user_id)
    f = urllib2.urlopen(url)
    result = f.read()
    obj = json.loads(result)
    return obj

def __get_tweets(screen_name):
    url = 'http://api.twitter.com/1/statuses/user_timeline.json?screen_name=' + screen_name
    f = urllib2.urlopen(url)
    result = f.read()
    obj = json.loads(result)
    return obj
    
