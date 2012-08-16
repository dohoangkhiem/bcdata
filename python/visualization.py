import MySQLdb as mysqldb
import os
import sys
import uuid

_log_dir = '/tmp/bouncingdata'
_app_store = '/home/khiem/java/temp/apps'

def _initialize():
  global _execution_id
  global _app_id
  global _app_guid
  global _user_id
  global _username
  global _mode
  global _initialized
  try:    
    _execution_id = sys.argv[1]
    _app_id = sys.argv[2]
    _app_guid = sys.argv[3]
    _user_id = sys.argv[4]
    _username = sys.argv[5]
    _mode = sys.argv[6] 
    _initialized = True
  except:
    print "Failed to initialize datastore. Reason: Invalid parameters"
  

def save_html(v_name, code, description):
  if not _initialized:
    print 'Visualization module was not initialized properly.'
    return
  
  try:
    # store html file
    filename = _log_dir + "/" + _execution_id + "/visualization/" + v_name + ".html"
    dirname = os.path.dirname(filename)
    if not os.path.exists(dirname):
      os.makedirs(dirname)
    output_file = open(filename, "w")
    output_file.write(code)
    output_file.close()
    
    if _mode == "not-persistent":
      return
  
    if _mode != "persistent":
      print "Unknown execution mode. Terminated."
      return
  
    # update to database
    conn = mysqldb.connect (host='localhost', user='root', passwd='a', db='bouncingdata')
    cursor = conn.cursor(mysqldb.cursors.DictCursor)
    
    print 'Saving visualization..'
    # save visualization
    guid = str(uuid.uuid4())
    filename = _app_store + "/" + _app_guid + "/v/" + guid + ".html"
    dirname = os.path.dirname(filename)
    if not os.path.exists(dirname):
      os.makedirs(dirname)
    output_file = open(filename, "w")
    output_file.write(code)
    output_file.close()
    
    # invalidate old viz.
    query = "update `visualizations` set `is_active` = false where `app_id` = " + _app_id + " and `is_active` = true"
    try:
      cursor.execute(query)
      conn.commit()
    except:
      print "Failed to invalidate old visualizations"
      conn.rollback()
    
    # update db
    print 'Updating database..'
    query = "insert into `visualizations`(`name`, `app_id`, `type`, `description`, `author`, `guid`, `is_active`) values('" + v_name + "'," + _app_id + ", 'html', '" +  description + "'," + _user_id + ", '" + guid + "', true)" 
    try:
      cursor.execute(query)
      conn.commit() 
    except:
      print "Failed to update database"
      conn.rollback()
    cursor.close()
    conn.close()
    
    print "Successfully save visualization"
  except Exception as e:
    print "There're something wrong when save visualization"
    print e

def main():
  #do something
  x = 1

if __name__ == "__main__":
  main()
elif __name__ == "visualization":
  _initialize()
