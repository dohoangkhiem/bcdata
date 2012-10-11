require(RMySQL)
library(RMySQL)
library(rjson)

query = function(query) {
  drv = MySQL()
  con = dbConnect(drv, user="root", password="a",
                dbname="bcdatastore", host="localhost")
  rs = dbSendQuery(con, query)
  data = fetch(rs, n=-1)
  dbDisconnect(con)
  return(data)
} 

load = function(dataset) {
  args <- commandArgs(trailingOnly = TRUE)
  execution_id = args[1]
  
  drv = MySQL()
  con = dbConnect(drv, user="root", password="a",
                  dbname="bcdatastore", host="localhost")
  statement = paste('select * from `', dataset, '`', sep='')
  rs = dbSendQuery(con, statement)
  
  data = fetch(rs, n=-1)
  dbDisconnect(con)
  
  # write to log file
  #data_list <- vector("list", nrow(data))
  #for (i in 1:nrow(data)) {
  #  data_list[[i]] <- data[i,]
  #}
  #json = toJSON(data_list)
  
  exec_dir = paste("/tmp/bouncingdata/", execution_id, sep='')
  if (!file.exists(exec_dir)) {
    dir.create(exec_dir)
  }
  
  json_file <- paste("/tmp/bouncingdata/", execution_id, "/dataset.out", sep='')
  if (file.exists(json_file)) {
    ds_log <- fromJSON(paste(readLines(json_file), collapse=""))
  } else {
    ds_log <- list()
  }
  
  ds_log[[length(ds_log) + 1]] = list("name"=dataset)
  output_file = file(json_file)
  write(toJSON(ds_log), file=json_file)
  close(output_file)
  
  return(data)
}

attach = function(attname, dataframe) {
  args <- commandArgs(trailingOnly = TRUE)
  execution_id = args[1]
  
  exec_dir = paste("/tmp/bouncingdata/", execution_id, sep='')
  if (!file.exists(exec_dir)) {
    dir.create(exec_dir)
  }
  
  json_file <- paste("/tmp/bouncingdata/", execution_id, "/", attname, ".att", sep='')
  
  data_list = list()
  for (i in 1:nrow(dataframe)) {
    data_list[[i]] <- dataframe[i,]
  }
  json = toJSON(data_list)
  
  #att_log_str = paste("{\"name\":", attname, ",\"data\":", json, "}", sep='')
  att_log = list("name"=attname,"data"=data_list)
  
  output_file = file(json_file)
  #write(att_log_str, file=json_file)
  write(toJSON(att_log), file=json_file)
  close(output_file)
}

