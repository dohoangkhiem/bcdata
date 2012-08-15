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
                  <span class="action">${activity.action }ed</span>
                  <div class="time">${activity.time}</div> 
                </div>
                <div class="thumbnail">
                  <img src="#" />
                </div>
                <p class="title">
                  ${activity.object.guid}
                </p>
                <p class="description">
                  analysis ${activity.object.guid}
                </p>
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