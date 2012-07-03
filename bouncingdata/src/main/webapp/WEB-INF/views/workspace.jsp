<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script type="text/javascript" src="<c:url value="/resources/js/bouncingdata/workspace.js" />"></script>
<script type="text/javascript">
	com.bouncingdata.Workspace.init();
</script>
<div id="main-content">
<div class="workspace-container">  
    <!-- Workspace main tabs layout -->
    <div class="workspace-main-tabs" id="workspace-main-tabs">
      <ul class="workspace-main-tabs-bar">
      </ul>
    </div> 
    
    <div class="popup new-app-dialog" id="new-app-dialog" title="Save your application">
      <form>
        <fieldset>
          <label for="new-app-name">Application name</label>
          <input type="text" id="new-app-name" maxlength="40"></input><br>
          <label for="new-app-language">Language</label>
          <select id="new-app-language">
            <option value="python">Python</option>
            <option value="r">R</option>
          </select> <br>
          <label for="new-app-description">Description</label>
          <textarea rows="3" id="new-app-description" style="resize: none;"></textarea><br>
          <label for="new-app-public">Published this application?</label>
          <select id="new-app-public">
            <option value="1">Public</option>
            <option value="0">Private</option>
          </select><br>
          <label for="new-app-tags">Tags</label>
          <input type="text" id="new-app-tags" title="Separate tags by comma"></input><br>
        </fieldset>
      </form>
    </div> 
</div>
</div>