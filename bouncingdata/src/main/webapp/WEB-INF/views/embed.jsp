<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/bouncingdata/embed.css" />" />
<script type="text/javascript" src="<c:url value="/resources/js/jquery/jquery-1.7.2.min.js" />"></script> 
<script type="text/javascript" src="<c:url value="/resources/js/bouncingdata/embed.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/js/jquery/jquery-ui-1.8.20.custom.min.js" />"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/jquery-ui/smoothness/jquery-ui-1.8.20.custom.css" />" rel="stylesheet" />
<script type="text/javascript" src="<c:url value="/resources/js/syntaxhighlighter/scripts/shCore.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/js/syntaxhighlighter/scripts/shBrushJScript.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/js/syntaxhighlighter/scripts/shBrushPython.js" />"></script>
<link href="<c:url value="/resources/js/syntaxhighlighter/styles/shCore.css" />" rel="stylesheet" type="text/css" />
<link href="<c:url value="/resources/js/syntaxhighlighter/styles/shThemeFadeToGrey.css" />" rel="stylesheet" type="text/css" />
  
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/datatable/css/jquery.dataTables.css" />" />
<script type="text/javascript" src="<c:url value="/resources/js/jquery.dataTables.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/js/KeyTable.min.js" />"></script>
  
<div id="bcdata-embedded-wrapper" class="bcdata-embedded-wrapper"></div>
<script>
  var ctx = '${pageContext.request.contextPath}';
	var $wrapper = $('#bcdata-embedded-wrapper');
</script>
<c:choose>
  <c:when test="${not empty errorMsg }">
    <script>
    	$wrapper.css('height', '10px').html('<span>${errorMsg}</span>');
    </script>
  </c:when>
  <c:when test='${mode eq "default"}'>
    <script>
      var dbDetail = JSON.parse('${dashboardDetail}');
      var $dashboard = $('<div class="bcdata-dashboard"></div>');
      $dashboard.height('600px').width('800px').css('position', 'relative');
      $wrapper.append($dashboard);
      view(dbDetail.visualizations, dbDetail.dashboard, $dashboard);
    </script>
  </c:when>
  <c:when test='${mode eq "single"}'>
    <c:choose>
      <c:when test="${tabs[0] eq 'v'}">
        <script>
        	var dbDetail = JSON.parse('${dashboardDetail}');
        	var $dashboard = $('<div class="bcdata-dashboard"></div>');
        	$dashboard.height('600px').width('800px').css('position', 'relative');
        	$wrapper.append($dashboard);
        	view(dbDetail.visualizations, dbDetail.dashboard, $dashboard);
        </script>
      </c:when>
      <c:when test="${tabs[0] eq 'c'}">
        <script>
        	var code = '${code}';
        	var $code = $('<div class="code"><pre class="brush: py"></pre></div>');
   				$('pre', $code).text(code);
   				$wrapper.append($code);
   				SyntaxHighlighter.highlight();
        </script>
      </c:when>
      <c:when test="${tabs[0] eq 'd'}">
        <c:if test="${empty datasetList and empty attachments }">
          <span>No data</span>
        </c:if>
        <c:if test="${not empty datasetList}">
        <div class="bcdata-datasets" id="bcdata-datasets">
          <c:forEach items="${datasetList }" var="entry">
            <div class="anls-dataset" style="margin: 1em 0 2.5em 0;" dsguid="${entry.key }">
              <span class="dataset-item-title"> <strong>
                <a target="_blank" href="<c:url value="/dataset/view/${entry.key }" />">${entry.value}</a>
              </strong>
              &nbsp;
              <a href="<c:url value="/dataset/dl/csv/${entry.key }" />" style="color: blue; text-decoration: none;">Download CSV</a>&nbsp;&nbsp;
              <a href="<c:url value="/dataset/dl/json/${entry.key }" />" style="color: blue; text-decoration: none;">Download JSON</a>
              </span>
              <table dsguid="${entry.key }" class="dataset-table"></table>
            </div>
          </c:forEach>
        </div>
        <script>
        	loadDatasetByAjax('#bcdata-datasets');
        </script>
        </c:if>
        <c:if test="${not empty attachments }">
          <c:forEach items="${attachments }" var="attachment">
            <script>
              $(function() {
                var $attachment = $('<div class="anls-attachment" style="margin: 1em 0 2.5em 0;"><span class="dataset-item-title"><strong>'
                    + '<a href="">${attachment.name}</a></strong><a href="<c:url value="/dataset/att/csv/${guid}/${attachment.name}" />" ' 
                    + 'style="color: blue; text-decoration: none;">Download CSV</a>&nbsp;&nbsp;<a href="<c:url value="/dataset/att/json/${guid}/${attachment.name}" />">'
                    + 'Download JSON</a></span><table class="attachment-table"></table></div>');
                if ($('#bcdata-datasets').length < 1) {
                  $wrapper.append('<div class="bcdata-datasets" id="bcdata-datasets"></div>');
                }
                $attachment.appendTo($('#bcdata-datasets'));
                var $table = $('table', $attachment);
                var data = '${attachment.data}';
                renderDatatable($.parseJSON(data), $table);
              });
            </script>
          </c:forEach>
        </c:if>
      </c:when>
    </c:choose>
  </c:when>
  <c:when test='${mode eq "tabs" }'>
    <script>
    	var $tabs = $('<div class="tabs"><ul class="tabs-bar"></ul></div>');
    	$wrapper.append($tabs);
    	$tabs.tabs();
    	$tabs.dsloaded = false;
   	</script>
    <c:forEach items="${tabs }" var="tab">
      <c:if test="${tab eq 'v'}">
        <script>
          $('.tabs', $wrapper).tabs('add', '#v', 'Dashboard');
          var $panel = $('.tabs #v', $wrapper);
          var $dashboard = $('<div class="bcdata-dashboard"></div>');
          $dashboard.height('600px').width('800px').css('position', 'relative');
          $panel.append($dashboard);
          var dbDetail = JSON.parse('${dashboardDetail}');
          view(dbDetail.visualizations, dbDetail.dashboard, $dashboard);
        </script>
      </c:if>
      <c:if test="${tab eq 'c'}">
        <script>
        	$('.tabs', $wrapper).tabs('add', '#c', 'Code');
        	var $panel = $('.tabs #c', $wrapper);
        	var code = '${code}';
        	var $code = $('<div class="code"><pre class="brush: py"></pre></div>');
   				$('pre', $code).text(code);
   				$panel.append($code);
   				SyntaxHighlighter.highlight();
        </script>
      </c:if>
      <c:if test="${tab eq 'd'}">
        <script>
        	$('.tabs', $wrapper).tabs('add', '#d', 'Data');
        	var $dataTab = $('.tabs #d', $wrapper);
        </script>
        
        <c:if test="${empty datasetList and empty attachments }">
          <script>
          	$dataTab.append('<span>No related data.</span>');
          </script>
        </c:if>
        <c:if test="${not empty datasetList}">
          <c:forEach items="${datasetList }" var="entry">
            <script>
            $dataTab.append('<div class="anls-dataset" style="margin: 1em 0 2.5em 0;" dsguid="${entry.key }">'
            	+ '<span class="dataset-item-title"> <strong><a target="_blank" href="<c:url value="/dataset/view/${entry.key }" />">${entry.value}</a>'
            	+ '</strong>&nbsp;&nbsp;<a href="<c:url value="/dataset/dl/csv/${entry.key }" />" style="color: blue; text-decoration: none;">Download CSV</a>'
            	+ '&nbsp;&nbsp;<a href="<c:url value="/dataset/dl/json/${entry.key }" />" style="color: blue; text-decoration: none;">Download JSON</a></span>' 
            	+ '<table dsguid="${entry.key }" class="dataset-table"></table></div>');
            </script>
          </c:forEach>
          <script>
            // load dataset
            $tabs.bind('tabsselect', function(event, ui) {
              if ($(ui.panel).prop('id') == 'd' && !$tabs.dsloaded) {
                loadDatasetByAjax('#bcdata-embedded-wrapper .tabs #d');
                $tabs.dsloaded = true;
              }
            });
            
          </script>
        </c:if>
        <c:if test="${not empty attachments }">
          <c:forEach items="${attachments }" var="attachment">
            <script>
              $(function() {
                var $attachment = $('<div class="anls-attachment" style="margin: 1em 0 2.5em 0;"><span class="dataset-item-title"><strong>'
                    + '<a href="">${attachment.name}</a></strong>&nbsp;<a href="<c:url value="/dataset/att/csv/${guid}/${attachment.name}" />" ' 
                    + 'style="color: blue; text-decoration: none;">Download CSV</a>&nbsp;&nbsp;<a href="<c:url value="/dataset/att/json/${guid}/${attachment.name}" />">' 
                    + 'Download JSON</a></span><table class="attachment-table"></table></div>');
                $attachment.appendTo($dataTab);
                var $table = $('table', $attachment);
                var data = '${attachment.data}';
                renderDatatable($.parseJSON(data), $table);
              });
            </script>
          </c:forEach>
        </c:if>
      </c:if>
    </c:forEach>
  </c:when>
  <c:otherwise></c:otherwise>
</c:choose>

<div class="footer" style="clear: both; float: right; font-size: 12px;">
  Power by <a href="http://www.bouncingdata.com" target="_blank" style="font-weight: bold; color: #000; text-decoration: none;">Bouncing Data</a>
</div>