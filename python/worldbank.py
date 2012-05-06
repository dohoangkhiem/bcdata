import httplib
import json
import visualization
import datastore

conn = httplib.HTTPConnection("api.worldbank.org")
conn.request("GET", "/countries/br/indicators/NY.GDP.MKTP.CD?format=json&date=2000:2010")
res = conn.getresponse()
data = res.read()

# parse the json data
obj = json.loads(data)
page = obj[0]
datalist = obj[1]

print json.dumps(datalist)
datastore.persist_data("worldbank", "bra_gdp", datalist)

template = """
<html>
  <head>
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
      google.load("visualization", "1", {packages:["corechart"]});
      google.setOnLoadCallback(drawChart);
      function drawChart() {
        var json = %(json_str)s;
        var wbdata = new google.visualization.DataTable;
        wbdata.addColumn('date', 'Year');
        wbdata.addColumn('number', 'GDP');
        var i;
        for (i = 10; i > 0; i--) {
          var value = parseFloat(json[i].value);
          wbdata.addRow([new Date(parseInt(json[i].date), 1, 1), value]);
        }
	console.debug(wbdata);
        var options = {
          title: 'Brasil GDP', vAxis: {logScale: true}, hAxis: {showTextEvery: 1}
        };

        var chart = new google.visualization.LineChart(document.getElementById('chart_div'));
        chart.draw(wbdata, options);
      }
    </script>
  </head>
  <body>
    <div id="chart_div" style="width: 600px; height: 400px;"></div>
  </body>
</html>
"""
json_str = json.dumps(datalist)
print json_str
visualization.save_html("worldbank", "bra_gdp_linechart", template % vars(), This is a line chart to visualize GDP of Brasil")
