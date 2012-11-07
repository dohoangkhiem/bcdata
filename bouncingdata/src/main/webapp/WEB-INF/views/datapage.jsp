<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<script>
	$(function() {
	  $('#dataset-content').tabs();
	  com.bouncingdata.Nav.setSelected('data', '${dataset.guid}');
	});
</script>
<div id="main-content" class="datapage-container">
  <div class="data-info right-content">
    <div class="dataset-summary summary">
      <div class="data-info-header info-header">
        <div class="data-info-title info-title">Dataset Info</div>
        <div class="data-info-title-line info-title-line"></div>
      </div>
      <p class="line-item">
        <strong>Dataset: </strong><span>${dataset.name }</span>
      </p>
      <p class="line-item">
        <strong>Author: </strong><span>${dataset.user.username }</span>
      </p>
      <p class="line-item">
        <strong>Description: </strong><span>${dataset.description }</span>
      </p>
      <p class="line-item">
        <strong>Create at: </strong><span>${dataset.createAt }</span>
      </p>
      <p class="line-item">
        <strong>Tags: </strong><span>${dataset.tags }</span>
      </p>
    </div>
    <div class="related-info dataset-related-info">
      <div class="data-info-header info-header">
        <div class="data-info-title info-title">Related Info</div>
        <div class="data-info-title-line info-title-line"></div>
      </div>
      <c:if test="${not empty relatedAnls }">
        <p class="related-analyses">
          <strong>Related analyses: </strong>
          <c:forEach items="${relatedAnls }" var="anls">
            <a class="related-anls-link" href="<c:url value="/anls" />/${anls.guid}">${anls.name }</a>&nbsp;
          </c:forEach>
        </p>
        <script>
          $(function() {
            $('.related-analyses a.related-anls-link').each(function() {
              $(this).click(function() {
                com.bouncingdata.Nav.fireAjaxLoad($(this).prop('href'), false);
                return false;
              });
            });
          });
      	</script>
      </c:if>
    </div>  
  </div>
  <div class="center-content">
    <div class="center-content-wrapper">
      <div class="top-rule"></div>
      <div class="dataset-header header">
        <div class="dataset-title main-title"><h2>${dataset.name}</h2></div>
        <!-- div class="dataset-vote">
          <h3 class="datset-score">${anls.score}</h3>&nbsp;
          <a href="#" class="anls-vote-up">Vote up</a>&nbsp;
          <a href="#" class="anls-vote-down">Vote down</a>
        </div-->
        <div class="dataset-actions" style="margin-top: 4px;">
          <a href="<c:url value="/dataset/dl/csv/${dataset.guid}"/>" style="color: block; text-decoration: none;">Download CSV</a>&nbsp;&nbsp;
          <a href="<c:url value="/dataset/dl/json/${dataset.guid}"/>" style="color: block; text-decoration: none;">Download JSON</a>
        </div>
      </div>
      <div class="header-rule"></div>
      <div class="data-content data-tab-container" id="dataset-content">
        <ul>
          <li><a href="#data">Data</a></li>
          <li><a href="#schema">Schema</a></li>
          <li><a href="#description">Description</a></li>
        </ul>
        <div class="clear"></div>
        <div class="dataset-content-wrapper">
          <div id="data">
            <table class="data-table" id="data-table">
            </table>
            <c:choose>
              <c:when test="${not empty data }">
                <script>
                  var data = ${data};
                  var $table = $('#data-table');
                  com.bouncingdata.Workbench.renderDatatable(data, $table);
                </script>
              </c:when>
              <c:otherwise>
                <script>
                $(function() {
                  console.debug("Load datatable by Ajax...");
                  var guid = '${guid}';
                  var columns = ${columns};
                  var $table = $('#data-table');
                  com.bouncingdata.Workbench.loadDatatableByAjax(guid, columns, $table);               
                });
                </script>  
              </c:otherwise>
            </c:choose>
          </div>
        </div>
        <div id="schema">
          <pre style="white-space: normal; word-wrap: break-word;">${dataset.schema }</pre>
        </div>
        <div id="description">
        <c:choose>
          <c:when test="${not empty dataset.description }">
            <span>${dataset.description }</span>
          </c:when>
          <c:otherwise>
            <span>No description</span>
          </c:otherwise>
        </c:choose>
        </div>
        
      </div>
      <div class="clear"></div>
      <!-- div class="comments-container">
        <h3 class="comments-title">
          <a href="javascript:void(0);" onclick="$('#comment-form').toggle('slow');">Comment</a>
        </h3>
        <div class="comment-form" id="comment-form">
          <form>
            <fieldset>
              <p>
                <textarea rows="5" id="message"></textarea>
              </p>
              <p>
                <input type="button" class="comment-submit" id="comment-submit" value="Post comment">
              </p>
            </fieldset>
          </form>
        </div>
        <div class="clear"></div>
        <label id="comments"></label>
        <div class="comments">
          <h3 class="comments-count">Comments</h3>
          <ul id="comment-list" class="comment-list">
          </ul>
        </div>
      </div-->
    </div>
  </div>
</div>