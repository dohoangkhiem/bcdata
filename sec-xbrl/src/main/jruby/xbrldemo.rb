require 'rubygems'
require 'nokogiri'
require 'open-uri'
require 'xbrlware'
require 'edgar'

class XBRLDEMO
  
  def search(cik, type) 
    search_url = "http://www.sec.gov/cgi-bin/browse-edgar?action=getcompany&CIK=?cik&type=?type&dateb=&owner=exclude&count=100".sub("?cik", cik).sub("?type", type)
    puts "Navigating to " + search_url
    doc = Nokogiri::HTML(open(search_url))
    # for xbrl document
    tds = doc.xpath("//table[@class=\"tableFile2\"]//tr/td/a[@id=\"interactiveDataBtn\"]")
    urls = []
    tds.each do |td|
      link = td.parent.first_element_child()['href']
      urls[urls.length] = "http://www.sec.gov" + link
    end
    
    urls.each do |url|
      fetch_data(url)
    end
  end
  
  def count(cik, type) 
    search_url = "http://www.sec.gov/cgi-bin/browse-edgar?action=getcompany&CIK=?cik&type=?type&dateb=&owner=exclude&count=100".sub("?cik", cik).sub("?type", type)
    puts "Navigating to " + search_url
    doc = Nokogiri::HTML(open(search_url))
    # for xbrl document
    tds = doc.xpath("//table[@class=\"tableFile2\"]//tr/td/a[@id=\"interactiveDataBtn\"]")
    return tds.length
  end
  
  def count_company(cik, type)
    if (count(cik, type) > 0) 
      return 1
    end
    return 0
  end
  
  def fetch_data(url)
    puts "Fetching data from " + url
    dl = Edgar::HTMLFeedDownloader.new()
    cik = url.split("/")[-3]
    download_dir = url.split("/")[-2] # download dir is : 000135448809002030
    base_dir = "/media/MUSIC/xbrl/"
    download_dir = base_dir + cik + "/" + download_dir
    if !File.directory?(download_dir) or Dir[download_dir + "/*"].empty?
      dl.download(url, download_dir)
    else
      puts download_dir + "is already existed. Skip."
    end
    #parse_xbrl(download_dir)
  end
  
  def fetch_data_override(search_url)
    puts "Navigating to " + search_url
    doc = Nokogiri::HTML(open(search_url))
    # for xbrl document
    tds = doc.xpath("//table[@class=\"tableFile2\"]//tr/td/a[@id=\"interactiveDataBtn\"]")
    urls = []
    tds.each do |td|
      link = td.parent.first_element_child()['href']
      urls[urls.length] = "http://www.sec.gov" + link
    end
    
    urls.each do |url|
      puts "Fetching data from " + url
      dl = Edgar::HTMLFeedDownloader.new()
      cik = url.split("/")[-3]
      download_dir = url.split("/")[-2] # download dir is : 000135448809002030
      base_dir = "/media/MUSIC/xbrl/"
      download_dir = base_dir + cik + "/" + download_dir
      dl.download(url, download_dir)
    end
      
  end
  
  def parse_xbrl(data_dir) 
    instance_file = Xbrlware.file_grep(data_dir)["ins"] # use file_grep to filter xbrl files and get instance file
    instance = Xbrlware.ins(instance_file)
    items = instance.item_all
    
    data=[]
    items.each do |item|
      period = item.context.period
      start_date = nil
      end_date = nil
      if period.is_duration?
        start_date = period.value["start_date"]
        end_date = period.value["end_date"]
      elsif period.is_instant?
        end_date = period.value
      end
      
      measure = nil
      unit = item.unit
      if (!unit.nil?)
        if unit.measure.kind_of? Xbrlware::Unit::Divide
          numerator = unit.measure.numerator.to_s
          denominator = unit.measure.denominator.to_s
          numerator = numerator.split(":")[1]  if numerator.include? ":"    
          denominator = denominator.split(":")[1]  if denominator.include? ":" 
          measure = numerator + "/" + denominator
        else 
          measure = unit.measure.to_s
          measure = measure.split(":")[1]  if measure.include? ":"
        end
      end
      type = "monetary"
      type =  "text" if measure.nil?
      type = "dei" if item.nsp == "dei"
        
      data = data + [{"name" => item.name, "value" => item.value, "start_date" => start_date, "end_date" => end_date, "unit" => measure, "type" => type}]
    end
    data
  end
  
  def parse_10k_html(url) 
    puts "Parsing the HTML 10K document..."
    doc = Nokogiri::HTML(open(url))
    toc_table = doc.xpath("//a[@name=\"toc\"]//following::table").first
    #puts "Toc table", toc_table.to_s
    item6_td = toc_table.xpath("//td//a[text() = \"Selected Financial Data\"]").first
    item6_a_name = item6_td['href'].sub("#", "");
    #item6_anchor = doc.xpath("//a[@name='#{item6_a_name}']")
    data_table = doc.xpath("//a[@name='#{item6_a_name}']//following::table").first
    puts data_table.to_s
  end
end

XBRLDEMO.new()




