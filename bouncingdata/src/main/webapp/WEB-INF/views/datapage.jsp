<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<div id="main-content" class="datapage-container">
  <div class="data-info right-content">
    Some information  
  </div>
  <div class="center-content">
    <div class="center-content-wrapper">
      <div class="top-rule"></div>
      <div class="dataset-header">
        <div class="dataset-title"><h2>${dataset.name}</h2></div>
        <!-- div class="dataset-vote">
          <h3 class="datset-score">${anls.score}</h3>&nbsp;
          <a href="#" class="anls-vote-up">Vote up</a>&nbsp;
          <a href="#" class="anls-vote-down">Vote down</a>
        </div-->
      </div>
      <div class="header-rule"></div>
      <div class="data-content">
        <table class="data-table" id="data-table">
          
        </table>
        <script>
        	var data = ${data}
        	console.debug("Data: ");
        	console.debug(data);
        	var $table = $('#data-table');
        	com.bouncingdata.Workbench.renderDatatable(data, $table);
        </script>
      </div>

      <div class="comments-container">
        <h3 class="comments-title">
          <a href="javascript:void(0);" onclick="$('#comment-form').toggle('slow');">Comments</a>
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
      </div>
    </div>
  </div>
</div>