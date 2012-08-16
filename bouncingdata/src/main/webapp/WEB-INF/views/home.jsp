<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div id="main-content">
  <div class="right-content">
  </div>
  <div class="center-content">
    <div class="center-content-wrapper">
      <div class="stream-container">
        <h3>Activity stream</h3>
        <div class="stream" id="stream">
          <c:forEach items="${activities }" var="activity">
            <div class="event">
              <div class="event-actor">
                <div class="avatar"></div>
              </div>
              <div class="event-content">
                <div class="info">
                  <strong>${activity.user.username }</strong>&nbsp;
                  <span class="action">${activity.action }</span>
                  <div class="time">${activity.time}</div> 
                </div>
                <div class="thumbnail">
                  <a target="_blank" href="<c:url value="/anls/${activity.object.guid}" />">
                    <img class="thumb-img" src="#" />
                  </a>
                </div>
                <p class="title">
                  <a target="_blank" href="<c:url value="/anls/${activity.object.guid}" />">${activity.object.name}</a>
                </p>
                <p class="description">
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
                    <strong class="event-score event-score-negative">-${activity.object.score }</strong>    
                  </c:if>
                  &nbsp;<a target="_blank" href="<c:url value="/anls/${activity.object.guid}#comments" />"><strong>${activity.object.commentCount }</strong>&nbsp;comments</a>
                </div>
              </div>
              <div class="clear"></div>
            </div>
          </c:forEach>      
          <div class="stream-footer">
            <a href="#">More</a>
          </div>    
        </div>
      </div>
      <div class="trends">
        
      </div>
    </div>
  </div>
</div>