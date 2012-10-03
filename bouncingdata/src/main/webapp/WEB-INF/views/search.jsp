<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script>
	$(function() {
	  $('.result-analysis ul.analysis-list li').each(function() {
	    var $link = $('a.anls-item', $(this));
	    $link.click(function() {
	      com.bouncingdata.Nav.fireAjaxLoad($link.prop('href'), false);
	      return false;
	    });
	  });
	  
	  $('.result-dataset ul.dataset-list li').each(function() {
	    var $link = $('a.dataset-item', $(this));
	    $link.click(function() {
	      com.bouncingdata.Nav.fireAjaxLoad($link.prop('href'), false);
	      return false;
	    });
	  });
	  
	  com.bouncingdata.Nav.setSelected('search', '${query}');
	});
</script>
<div id="main-content" class="main-content search-page" style="min-height: 480px; padding: 10px; background-color: #fff;">
  <div class="result-container">
    <div class="result-people" style="float: left; width: 48%;">
      <h3>
        <span class="result-title">People</span> &nbsp;&nbsp;
        <small style="font-weight: normal; font-size: 12px;">
          <a href="#" class="result-view-all">View all</a> &nbsp;|&nbsp;
          <a href="#" class="result-collapse">Hide</a>
        </small>
      </h3>
      <ul class="people-list">
        <c:choose>
          <c:when test="${empty searchResult.users }">No people matched.</c:when>
          <c:otherwise>
            <c:forEach items="${searchResult.users }" var="user">
              <li><a class="people-item" href="#" title="View profile">${user.username }</a></li>
            </c:forEach>
          </c:otherwise>
        </c:choose>  
      </ul>      
    </div>
    
    <div class="result-dataset" style="float: left; width: 48%;">
      <h3>
        <span class="result-title">Dataset</span> &nbsp;&nbsp;
        <small style="font-weight: normal; font-size: 12px;">
          <a href="#" class="result-view-all">View all</a> &nbsp;|&nbsp;
          <a href="#" class="result-collapse">Hide</a>
        </small>
      </h3>
      <ul class="dataset-list">
        <c:choose>
          <c:when test="${empty searchResult.datasets }">No dataset matched.</c:when>
          <c:otherwise>
            <c:forEach items="${searchResult.datasets }" var="dataset">
              <li><a class="dataset-item" target="_blank" href="<c:url value="/dataset/view/${dataset.guid}" />" title="View dataset">${dataset.name }</a></li>
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </ul>
    </div>
    
    <div class="result-analysis" style="float: left; width: 48%;">
      <h3>
        <span class="result-title">Analysis</span> &nbsp;&nbsp;
        <small style="font-weight: normal; font-size: 12px;">
          <a href="#" class="result-view-all">View all</a> &nbsp;|&nbsp;
          <a href="#" class="result-collapse">Hide</a>
        </small>
      </h3>
      <ul class="analysis-list">
        <c:choose>
          <c:when test="${empty searchResult.analyses }">No analysis matched.</c:when>
          <c:otherwise>
            <c:forEach items="${searchResult.analyses }" var="anls">
              <li><a class="anls-item" href="<c:url value="/anls/${anls.guid}" />" title="View analysis">${anls.name }</a></li>
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </ul>
    </div>
    
    <div class="result-scraper" style="float: left; width: 48%;">
      <h3>
        <span class="result-title">Scraper</span> &nbsp;&nbsp;
        <small style="font-weight: normal; font-size: 12px;">
          <a href="#" class="result-view-all">View all</a> &nbsp;|&nbsp;
          <a href="#" class="result-collapse">Hide</a>
        </small>
      </h3>
      <ul class="scraper-list">
        <c:choose>
          <c:when test="${empty searchResult.scrapers }">No scraper matched.</c:when>
          <c:otherwise>
            <c:forEach items="${searchResult.scrapers }" var="scraper">
              <li><a class="scraper-item" href="#" title="View scraper">${scraper.name }</a></li>
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </ul>
    </div>
    
  </div>
</div>