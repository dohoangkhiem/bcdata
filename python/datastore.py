import MySQLdb as mysqldb

#conn = mysqldb.connect (host='localhost', user='root', passwd='a', db='worldbank')

#cursor = conn.cursor()

#cursor.execute("select count(*) from Indicators")
#row = cursor.fetchone()
#print row[0]
#cursor.close()
#conn.close()

def persist_data(appname, tablename, data):
  if data == None or len(data) == 0:
    return
  # check if the dataset for this app was created
  conn = mysqldb.connect (host='localhost', user='root', passwd='a', db='plfdemo')
  cursor = conn.cursor()
  query = "select * from Datasets where name = '" + appname + "'"
  cursor.execute(query)
  row = cursor.fetchone()
  if row == None:
    __create_dataset(appname, "This dataset belongs to application " + appname)
  # create dataset if neccessary  

  # create table info if neccessary
  query = "select * from Tables where name = '" + tablename + "' and dataset='" + appname + "'"
  cursor.execute(query)
  row = cursor.fetchone()
  if row == None:
    fields = []
    for key in data[0]:
      fields.append(key)
    __create_table_info(appname, tablename, "table info", fields)      
  # create table if neccessary
  
  # insert data to table	
  __persist_json("userdata_" + appname + "_" + tablename, data)
  print "Successfully persist data for ", appname, "dataset, table ", tablename
  cursor.close()
  conn.close()

def __create_dataset(dataset_name, description):
  conn = mysqldb.connect (host='localhost', user='root', passwd='a', db='plfdemo')
  cursor = conn.cursor()
  try:
    query = "insert into Datasets(name, description) values('" + dataset_name + "', '" + description + "')"
    print "Creating dataset ", dataset_name, ": ", query
    cursor.execute(query)
    conn.commit()
  except:
    print "something wrong while creating dataset " + dataset_name
    conn.rollback()
  cursor.close()
  conn.close()

def __create_table_info(dataset_name, table_name, description, fieldlist):
  conn = mysqldb.connect (host='localhost', user='root', passwd='a', db='plfdemo')
  cursor = conn.cursor()
  fields_str = ""
  for field in fieldlist:
    fields_str = fields_str + field + ","
  fields_str = fields_str[:-1]
  try:
    query = "insert into Tables(name, description, dataset, field_list) values('" + table_name + "', '" + description + "', '" + dataset_name + "', '" + fields_str + "')"
    print "Creating table info for table ", table_name, ": ", query
    cursor.execute(query)
    conn.commit()
  except:
    print "something wrong while creating table " + table_name
    conn.rollback()
  cursor.close()
  conn.close()

def __persist_json(table_name, jsonlist):
  conn = mysqldb.connect (host='localhost', user='root', passwd='a', db='plfdemo')
  cursor = conn.cursor()
  try:
    cursor.execute("drop table if exists " + table_name)
    fields = []
    for key in jsonlist[0]:
      fields.append(key)

    # create table
    create_table_str = "create table " + table_name + "("
    for i in range(len(fields) - 1):
      create_table_str = create_table_str + "`" + fields[i] + "` text, "
    create_table_str = create_table_str + "`" + fields[len(fields) - 1] + "` text)"
    print "Creating table: ", create_table_str
    cursor.execute(create_table_str)
    
    # insert data
    insert_data_str = "insert into " + table_name + "("
    for i in range(len(fields) - 1):
      insert_data_str = insert_data_str + "`" + fields[i] + "`, "
    insert_data_str = insert_data_str + "`" + fields[len(fields) - 1] + "`)"
    insert_data_str = insert_data_str + " values"
    
    # for each row
    for i in range(len(jsonlist) - 1):
      insert_data_str = insert_data_str + "("
      obj = jsonlist[i]
      for j in range(len(fields) - 1):
        if type(obj[fields[j]]) is dict:
          insert_data_str = insert_data_str + "'" + obj[fields[j]]['id'] + "',"
        else:
          insert_data_str = insert_data_str + "'" + obj[fields[j]] + "',"
      if type(obj[fields[len(fields) - 1]]) is dict:
        insert_data_str = insert_data_str + "'" + obj[fields[len(fields) - 1]]['id'] + "')"
      else:
        insert_data_str = insert_data_str + "'" + obj[fields[len(fields) - 1]] + "')"
      insert_data_str = insert_data_str + ","
    
    insert_data_str = insert_data_str + "("
    obj = jsonlist[len(jsonlist) - 1]
    for j in range(len(fields) - 1):
      if type(obj[fields[j]]) is dict:
        insert_data_str = insert_data_str + "'" + obj[fields[j]]['id'] + "',"
      else:
        insert_data_str = insert_data_str + "'" + obj[fields[j]] + "'," 
    insert_data_str = insert_data_str + "'" + obj[fields[len(fields) - 1]] + "')"   
    
    print "Inserting data: ", insert_data_str
    cursor.execute(insert_data_str)
    conn.commit()
  except:
    print "something wrong"
    conn.rollback()
  cursor.close()
  conn.close()

#test
def test():
	json_str = [{"page":1,"pages":1,"per_page":"50","total":11},[{"indicator":{"id":"NY.GDP.MKTP.CD","value":"GDP (current US$)"},"country":{"id":"BR","value":"Brazil"},"value":"2087889553821.68","decimal":"0","date":"2010"},{"indicator":{"id":"NY.GDP.MKTP.CD","value":"GDP (current US$)"},"country":{"id":"BR","value":"Brazil"},"value":"1594489675023.99","decimal":"0","date":"2009"},{"indicator":{"id":"NY.GDP.MKTP.CD","value":"GDP (current US$)"},"country":{"id":"BR","value":"Brazil"},"value":"1652632229227.61","decimal":"0","date":"2008"},{"indicator":{"id":"NY.GDP.MKTP.CD","value":"GDP (current US$)"},"country":{"id":"BR","value":"Brazil"},"value":"1365982651542.37","decimal":"0","date":"2007"},{"indicator":{"id":"NY.GDP.MKTP.CD","value":"GDP (current US$)"},"country":{"id":"BR","value":"Brazil"},"value":"1088917279411.76","decimal":"0","date":"2006"},{"indicator":{"id":"NY.GDP.MKTP.CD","value":"GDP (current US$)"},"country":{"id":"BR","value":"Brazil"},"value":"882185291700.904","decimal":"0","date":"2005"},{"indicator":{"id":"NY.GDP.MKTP.CD","value":"GDP (current US$)"},"country":{"id":"BR","value":"Brazil"},"value":"663760000000","decimal":"0","date":"2004"},{"indicator":{"id":"NY.GDP.MKTP.CD","value":"GDP (current US$)"},"country":{"id":"BR","value":"Brazil"},"value":"552469288267.793","decimal":"0","date":"2003"},{"indicator":{"id":"NY.GDP.MKTP.CD","value":"GDP (current US$)"},"country":{"id":"BR","value":"Brazil"},"value":"504221228974.035","decimal":"0","date":"2002"},{"indicator":{"id":"NY.GDP.MKTP.CD","value":"GDP (current US$)"},"country":{"id":"BR","value":"Brazil"},"value":"553582178386.192","decimal":"0","date":"2001"},{"indicator":{"id":"NY.GDP.MKTP.CD","value":"GDP (current US$)"},"country":{"id":"BR","value":"Brazil"},"value":"644701831101.394","decimal":"0","date":"2000"}]]

	datalist = json_str[1]
	#persist_json("test_table", datalist)
	#create_dataset("test", "a test")
	#fields = ["id", "name", "age"]
	#create_table_info("test", "test_table", "a test table", fields)
        persist_data("newapp", "newtable", datalist)
