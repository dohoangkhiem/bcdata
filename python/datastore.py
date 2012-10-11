 # -*- coding: utf-8 -*-
import MySQLdb as mysqldb
import MySQLdb.converters
import os
import json
import sys
import uuid
from collections import OrderedDict
from types import *
import re
from string import Template

_host = 'localhost'
_user = 'root'
_passwd = 'a'
_db = 'bcdatastore'
_log_dir = '/tmp/bouncingdata'


def _initialize():
  global _execution_id
  global _username
  global _mode
  global _initialized
  global _types_map
  global _counter
  try:
    _execution_id = sys.argv[1]
    _username = sys.argv[2]
    _mode = sys.argv[3]
    _types_map = {int: 'integer', list: 'text', dict: 'text', str: 'text', unicode: 'text', bool: 'boolean', long: 'bigint', float: 'double'}
    _counter = {'value': 0}
    _initialized = True
  except:
    print "Failed to initialize datastore. Reason: Invalid parameters"
    _initialized = False
  
def store(data, dataset="", description=""):
  if not _initialized:
    return
  
  if data == None or len(data) == 0:
    print "No data to persist";
    return
  
  if not dataset:
    dataset = "bcdata"
   
  dataset = _username + '.' + dataset
  
  # build dataset schema
  #print json.dumps(data[0])
  fields = {}
  for key in data[0]:
    fields[key] = type(data[0][key])
    
  create_str = "CREATE TABLE IF NOT EXISTS `%s` ("
  li = iter(fields)
  field = next(li)
  while True:
    try:
      create_str += "`" + field.strip() + "` " + _types_map[fields[field]]
      field = next(li)
      create_str += ","
    except StopIteration:
      create_str += ")"
      break
  
  schema_str = (create_str[:13] + create_str[27:]) % dataset

  print schema_str
    
  # write log to log directory  
  try:
    # write data to file under /tmp/{execution_id}/{dataset}.dat
    filename = _log_dir + "/" + _execution_id + "/dataset.inp"
    dirname = os.path.dirname(filename)
    if not os.path.exists(dirname):
      os.makedirs(dirname)
    
    output_file = None;
    content = None;
    try:
      output_file = open(filename, "r")
      content = str(output_file.read())
      content = json.loads(content)    
    except IOError as io_err:
      content = []
    output_file = open(filename, "w")
    dataObj = {"name": dataset, "description": description, "schema": schema_str, "size": len(data)}
    content.append(dataObj)
    #output_file.write(json.dumps(dataObj));
    output_file.write(json.dumps(content));
  except Exception as e:
    print "Failed to log dataset."
    print e
  if _mode == "not-persistent":
    return
  
  if _mode != "persistent":
    print "Unknown execution mode. Terminated."
    return
  
  # drop old table
  drop_str = "DROP TABLE IF EXISTS `%`"
      
  conn = mysqldb.connect (_host, _user, _passwd, _db)
  cursor = conn.cursor()

  # create the real data table  
  try:
    cursor.execute(drop_str % dataset)
    cursor.execute(create_str % dataset)
    conn.commit()
  except Exception as ex:
    print "Something wrong while creating table " + dataset
    print ex
    conn.rollback()
  
  columns_count = len(fields)
  
  params = []
  
  # insert data into data table
  query = "INSERT INTO `%`("
  li = iter(fields)
  field = next(li)
  while True:
    try:
      query += "`" + field.strip() + "` "
      field = next(li)
      query += ","
    except StopIteration:
      query += ")"
      break
  
  query += " VALUES("
  
  items = iter(data)
  item = next(items)
  
  for i in range(0, columns_count):
    query += "%s,"
  
  query = query[:-1]
  query += ")"
  
  while True:
    try:
      row = []
      li = iter(fields)
      field = next(li)
      while True:
        try:
          value = item[field]
          value_t = type(value)
          #if value_t is IntType or value_t is LongType or value_t is FloatType:
          #  row.append(item[field])
          #else:
          row.append(unicode(item[field]).encode('utf-8'))
          
          field = next(li)
        except StopIteration:
          break
      params.append(tuple(row))
      item = next(items)
    except StopIteration:
      break
  
  query = query % dataset
  print query
  print params
  disableKeys = False
  try:
    if (len(data) > 1000):
      cursor.execute("ALTER TABLE `" + dataset + "` DISABLE KEYS")
      disableKeys = True
    cursor.executemany(query, params)
    if disableKeys:
      cursor.execute("ALTER TABLE `" + dataset + "` ENABLE KEYS")
    conn.commit()
  except Exception as e:
    print "Exception occurs when persist data to dataset table."
    print e
    conn.rollback()
  cursor.close()
  conn.close()
  
  
def load(identifier):
  if not _initialized:
    return
  li = identifier.split('.', 1)
  u = li[0]
  d = li[1]
  query = "SELECT * FROM `" + identifier + "`"
  conn = mysqldb.connect (_host, _user, _passwd, _db)
  cursor = conn.cursor(mysqldb.cursors.DictCursor)
  try:
    cursor.execute(query)
    rs = cursor.fetchall()
    
    # write log to log directory  
    try:
      # write data to file under /tmp/{execution_id}/{dataset}.dat
      filename = _log_dir + "/" + _execution_id + "/dataset.out"
      dirname = os.path.dirname(filename)
      if not os.path.exists(dirname):
        os.makedirs(dirname)
      
      output_file = None;
      content = None;
      try:
        output_file = open(filename, "r")
        content = str(output_file.read())
        content = json.loads(content)    
      except IOError as io_err:
        content = []
      output_file = open(filename, "w")        
      dataObj = {"name": identifier}
      content.append(dataObj)
      output_file.write(json.dumps(content));      
    except Exception as e:
      print "Failed to log dataset."
      print e
    
    return rs
  except Exception as e:
    print e
    return None
  finally:
    cursor.close()
    conn.close()
    
def query(query):
  if not _initialized:
    return
  conn = mysqldb.connect (_host, _user, _passwd, _db)
  cursor = conn.cursor(mysqldb.cursors.DictCursor)
  try:
    cursor.execute(query)
    rs = cursor.fetchall()
    return rs
  except Exception as e:
    print e
    return None
  finally:
    cursor.close()
    conn.close()
  
def attach(data, dataset="", description=""):
  if not _initialized:
    return
  
  if data == None or len(data) == 0:
    print "No data to attach. Skip.";
    return
  if not dataset:
    dataset = "attachment_data_" + str(_counter['value'])
    _counter['value'] = _counter['value'] + 1
     
  # build dataset schema
  fields = {}
  for key in data[0]:
    fields[key] = type(data[0][key])
  
  create_str = "CREATE TABLE `" + dataset + "` ("
  li = iter(fields)
  field = next(li)
  while True:
    try:
      create_str += "`" + field + "` " + _types_map[fields[field]]
      field = next(li)
      create_str += ","
    except StopIteration:
      create_str += ")"
      break
  
  schema_str = create_str
  
  # write log to log directory  
  try:
    # write data to file under /tmp/{execution_id}/{dataset}.dat
    filename = _log_dir + "/" + _execution_id + "/" + dataset + ".att"
    dirname = os.path.dirname(filename)
    if not os.path.exists(dirname):
      os.makedirs(dirname)
    
    #output_file = None;
    #content = None;
    ##try:
    #  output_file = open(filename, "r")
    #  content = str(output_file.read())
    #  content = json.loads(content)    
    #except IOError as io_err:
    #  content = []
        
    output_file = open(filename, "w")
    
    dataObj = {"name": dataset, "description": description, "schema": schema_str, "data": data}
    #content.append(dataObj)
    output_file.write(json.dumps(dataObj));
    print "Successfully attached dataset name " + dataset 
  except Exception as e:
    print "Failed to attach dataset " + dataset 
    
def attach_dataset(identifier):
  load(identifier)
  print "Dataset " + identifier + " has been attached."
  
# run main() function from command-line with 3 arguments: execution_id, username, mode (not-persistent|persistent) 
def main():
    _initialize()
    
    dataObj = [{"page":1,"pages":1,"per_page":"50","total":11},[{"indicator":{"id":"NY.GDP.MKTP.CD","value":"GDP (current US$)"},"country":{"id":"BR","value":"Brazil"},"value":"2087889553821.68","decimal":"0","date":"2010"},{"indicator":{"id":"NY.GDP.MKTP.CD","value":"GDP (current US$)"},"country":{"id":"BR","value":"Brazil"},"value":"1594489675023.99","decimal":"0","date":"2009"},{"indicator":{"id":"NY.GDP.MKTP.CD","value":"GDP (current US$)"},"country":{"id":"BR","value":"Brazil"},"value":"1652632229227.61","decimal":"0","date":"2008"},{"indicator":{"id":"NY.GDP.MKTP.CD","value":"GDP (current US$)"},"country":{"id":"BR","value":"Brazil"},"value":"1365982651542.37","decimal":"0","date":"2007"},{"indicator":{"id":"NY.GDP.MKTP.CD","value":"GDP (current US$)"},"country":{"id":"BR","value":"Brazil"},"value":"1088917279411.76","decimal":"0","date":"2006"},{"indicator":{"id":"NY.GDP.MKTP.CD","value":"GDP (current US$)"},"country":{"id":"BR","value":"Brazil"},"value":"882185291700.904","decimal":"0","date":"2005"},{"indicator":{"id":"NY.GDP.MKTP.CD","value":"GDP (current US$)"},"country":{"id":"BR","value":"Brazil"},"value":"663760000000","decimal":"0","date":"2004"},{"indicator":{"id":"NY.GDP.MKTP.CD","value":"GDP (current US$)"},"country":{"id":"BR","value":"Brazil"},"value":"552469288267.793","decimal":"0","date":"2003"},{"indicator":{"id":"NY.GDP.MKTP.CD","value":"GDP (current US$)"},"country":{"id":"BR","value":"Brazil"},"value":"504221228974.035","decimal":"0","date":"2002"},{"indicator":{"id":"NY.GDP.MKTP.CD","value":"GDP (current US$)"},"country":{"id":"BR","value":"Brazil"},"value":"553582178386.192","decimal":"0","date":"2001"},{"indicator":{"id":"NY.GDP.MKTP.CD","value":"GDP (current US$)"},"country":{"id":"BR","value":"Brazil"},"value":"644701831101.394","decimal":"0","date":"2000"}]]

    datalist = dataObj[1]
    
    #store(datalist, "test_dataset", "Test dataset")
    #store(datalist, "new_dataset", "New dataset")
    attach(datalist, "", "")
    attach(datalist, "", "")
    #print type(query("select * from `demo.Rankings 1998-2010_uploaded` limit 0,10"))
    #all_matches = list(load('demo.Premier League 2011-12 Match by Match_uploaded'))
    
if __name__ == "__main__":
  main()
elif __name__ == "datastore":
  _initialize()