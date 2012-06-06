<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" src="<c:url value="/resources/js/plfdemo/browser.js" />"></script>
<script type="text/javascript">
  $(function() {
    $("#browser-tabs" ).tabs();
    plfdemo.Main.getDatastoreList();
    plfdemo.Main.getApplicationList();
    plfdemo.Browser.setMode("all");
  });
</script>
<div class="browser-container">
  <div class="browser-tabs" id="browser-tabs">
    <ul>
      <li><a href="#browser-all">All</a></li>
      <li><a href="#browser-mystuff">My Stuff</a></li>
    </ul>
    <div id="browser-all">
      <div class="dataset-list-panel">
        <h4>Datasets</h4>
        <div id="dataset-list">
        </div>
      </div>
      <div class="datastore-list-panel">
        <h4>Datastores</h4>
        <div id="datastore-list">         
        </div>
      </div>
      <div class="browser-separator"></div>
      <div class="application-list-panel">
        <h4>Applications</h4>
        <div id="application-list"></div>
      </div>
      <div class="show-all"><a id="show-all-button" href="javascript:void(0)">Back</a></div>
      <div class="clear"></div>
    </div>
    
    <div id="browser-mystuff">
      Your stuff here
    </div>
  </div>
</div>