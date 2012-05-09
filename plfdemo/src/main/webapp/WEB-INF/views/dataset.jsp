<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
<head>
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/default.css" />" />
  <script type="text/javascript" src="<c:url value="/resources/js/jquery-1.6.4.min.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/json2.js" />"></script>
  
  <script>
    
    function query() {
      if ($("#query-text").val()) {
        $.ajax({
          url: "query",
          data: { query: $("#query-text").val() },
          type: "get",
          success: function(json) {
            $("#query-result").text(json);
          },
          error: function(json) {
            $("#query-result").text("Error occurred while execute query");
          }
        });
      }
    }
    
    function showSchema(link) {
      console.info(link.title);
      $("#schema").text("Field list: " + link.title);
    }
 	</script>
</head>
<body>

  <div class="main-container">
    <div class="top-panel">
      <div class="top-left-panel">
        <!-- div class="table-list-container">
          <h3></h3>
          <div id="table-list">
            <ul>
            <c:if test="${currentDataset != null}">
              <c:forEach items="${tables }" var="table">
                <li>
                  <a href="#" onclick="">${table.name }</a>
                </li>
              </c:forEach>
            </c:if>
            </ul>
          </div>
        </div-->
        <div class="dataset-list-container">
          <h3>Dataset and table list</h3>
          <div id="dataset-list">
            <ul>
            <!-- c:if test="${currentDataset == null}"-->
              <c:forEach items="${datasets }" var="ds">
                <li>
                  <a href="<c:url value="/dataset/gui/${ds.name }" />">${ds.name }</a>
                  <span>Tables: </span>
                  <span id="${ds.name }-tables">
                    <c:forEach items="${datamap[ds.name] }" var="table">
                      <a href="#" onclick="showSchema(this);" title="${table.fieldList }"><c:out value="${table.name }"></c:out></a> &nbsp;
                    </c:forEach>
                  </span>
                </li>
              </c:forEach>
            <!-- /c:if-->
            </ul>
          </div>
        </div>
        <!-- div class="links">
          <a href="#" id="back" onclick="">Back</a>
        </div-->
        <div class="top-right-panel">
          <div id="schema" style="font-family: Courier; font-size: 0.85em;">Click to table show schema</div>
        </div>
      </div>
    </div>
    <div class="bottom-panel">
      <div>
        <h3>Query data</h3>
        <textarea rows="8" cols="80" id="query-text" style="display: block;"></textarea>
        <input type="submit" value="Execute" onclick="query();">
        <div style="margin-top: 10px;"><span id="query-result" style="font-family: Courier; font-size: 0.85em;"></span></div>
      </div>
    </div>
  </div>
  
</body>
</html>