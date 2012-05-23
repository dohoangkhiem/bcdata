<div class="browser-container">
  <div class="browser-tabs" id="browser-tabs">
    <ul>
      <li><a href="#browser-all">All</a></li>
      <li><a href="#browser-mystuff">My Stuff</a></li>
    </ul>
    <div id="browser-all">
      <div class="dataset-list-panel">
        <h4>Datastores</h4>
        <ul id="dataset-list-all"></ul>
      </div>
  
      <div class="application-list-panel">
        <h4>Applications</h4>
        <ul id="application-list-all"></ul>
      </div>
    </div>
    
    <div id="browser-mystuff">
      Your stuff here
    </div>
  </div>
  
</div>

<script type="text/javascript">
	$(function() {
		$("#browser-tabs" ).tabs();
		plfdemo.Main.getDatasetList();
		plfdemo.Main.getApplicationList();
	});
</script>