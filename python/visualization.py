import MySQLdb as mysqldb
import os

def save_html(appname, visualization_name, code, description): 
  try:
    # store html file
    filename = "/home/khiem/java/temp/apps/" + appname + "/visualization/" + visualization_name + ".html"
    dirname = os.path.dirname(filename)
    if not os.path.exists(dirname):
      os.makedirs(dirname)
    output_file = open(filename, "w")
    output_file.write(code)
    output_file.close()
  
    
    # update to database
    conn = mysqldb.connect (host='localhost', user='root', passwd='a', db='plfdemo')
    cursor = conn.cursor()
    query = "insert into Visualizations(name, appname, type, description) values('" + visualization_name + "','" + appname + "', 'html', '" +  description + "')" 
    try:
      cursor.execute(query)
      conn.commit() 
    except:
      print "Failed to update database"
      conn.rollback
    cursor.close()
    conn.close()
    
    print "Successfully save visualization"
  except:
    print "There're something wrong when save visualization"

