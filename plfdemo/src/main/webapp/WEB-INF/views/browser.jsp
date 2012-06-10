<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" src="<c:url value="/resources/js/plfdemo/browser.js" />"></script>
<div class="search-container">
  <div id="search-form" class="search-form">
    <input type="text" class="search-input" id="query" name="query" />
    <input type="submit" value="Search" id="search-submit" />
  </div>
  <div class="clear"></div>
</div>
<div class="browser-container">
  <div class="browser-tabs" id="browser-tabs">
    <ul>
      <li><a href="#browser-mystuff">My Stuff</a></li>
    </ul>
    <div id="browser-mystuff">
      <div class="dataset-list-panel">
        <h4 style="cursor: pointer;">Datasets</h4>
        <div id="dataset-list">
        </div>
      </div>
      <div class="browser-separator"></div>
      <div class="application-list-panel">
        <h4 style="cursor: pointer;">Applications</h4>
        <div id="application-list"></div>
      </div>
      <div class="show-all"><a id="show-all-button" href="javascript:void(0)">Back</a></div>
      <div class="clear"></div>
    </div>
    
  </div>
</div>