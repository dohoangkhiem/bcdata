<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script>
	$(function() {
	  com.bouncingdata.ActivityStream.init();
	});
</script>
<div id="main-content">
  <div class="right-content">
  </div>
  <div class="center-content">
    <div class="center-content-wrapper">
      <div class="trends-container center-content-side">
        <h3>Trends</h3>
      </div>
      <div class="stream-container center-content-main">
        <h3>Activity stream</h3>
        <div class="stream" id="stream">
          <c:forEach items="${activities }" var="activity">
            <c:if test="${not empty activity.object }">
              <div class="event">
                <div class="event-avatar">
                  <img class="avatar no-avatar" src="<c:url value="/resources/images/no-avatar.jpg" />">
                </div>
                <div class="event-content">
                  <div class="info" aid="${activity.id }">
                    <a href="#" class="user"><strong>${activity.user.username }</strong></a>&nbsp;
                    <span class="action">${activity.action }</span>
                    <div class="time">${activity.time}</div> 
                  </div>
                  <div class="thumbnail">
                    <a id="evt-thumb-${activity.id }" href="<c:url value="/anls/${activity.object.guid}" />">
                      <c:choose>
                        <c:when test="${not empty activity.object.thumbnail }">
                          <img class="thumb-img" src="<c:url value="/thumbnails/${activity.object.thumbnail}.jpg" />" onerror="this.src='<c:url value="/thumbnails/no-image.jpg" />'; this.onerror=null;" />
                        </c:when>
                        <c:otherwise>
                          <img class="thumb-img" src="<c:url value="/thumbnails/no-image.jpg" />" />
                        </c:otherwise>
                      </c:choose>
                    </a>
                  </div>
                  <p class="title">
                    <a id="evt-title-${activity.id }" href="<c:url value="/anls/${activity.object.guid}" />"><strong>${activity.object.name}</strong></a>
                  </p>
                  <p class="description">
                    <span>${activity.object.description }</span>
                  </p>
                  <div class="clear"></div>
                  <div class="event-footer">
                    <c:if test="${activity.object.score > 0}">
                      <strong class="event-score event-score-positive">+${activity.object.score }</strong>    
                    </c:if>
                    <c:if test="${activity.object.score == 0}">
                      <strong class="event-score">0</strong>    
                    </c:if>
                    <c:if test="${activity.object.score < 0}">
                      <strong class="event-score event-score-negative">${activity.object.score }</strong>    
                    </c:if>
                    &nbsp;<a id="evt-comment-${activity.id }" class="comments-link" href="<c:url value="/anls/${activity.object.guid}#comments" />"><strong>${activity.object.commentCount }</strong>&nbsp;comments</a>
                  </div>
                </div>
                <div class="clear"></div>
              </div>
            </c:if>
          </c:forEach>      
          <div class="stream-footer">
            <a href="#">More</a>
          </div>    
        </div>
      </div>
    </div>
  </div>
</div>