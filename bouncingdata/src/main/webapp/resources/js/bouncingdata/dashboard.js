function Dashboard() {
  
}

Dashboard.prototype.init = function() {
  
}

/**
 * 
 */
Dashboard.prototype.addAllViz = function(vizList, appGuid, $container) {
  var count = 0;
  for (v in vizList) {
    var viz = vizList[v];
    if (viz.type == "png" || viz.type == "PNG") {
      this.addViz((count%2) * 365, Math.floor(count/2) * 280, 360, 280, viz.source, "png", $container)
    } else if (viz.type == "html" || viz.type == "HTML") {
      var src = ctx + '/visualize/' + appGuid + '/' + viz.source + '/html';
      this.addViz((count%2) * 365, Math.floor(count/2) * 280, 360, 280, src, "html", $container)
    }
    count++;
  }
}

Dashboard.prototype.addViz = function(x, y, w, h, src, type, $container) {
  if (!src) return;
  x = x || 0;
  y = y || 0;
  w = w || 360;
  h = h || 280;
  
  var $vizContainer = $('<div class="viz-container"></div>');
  var $vizHandle = $('<div class="viz-handle"></div>');
  $vizContainer.append($vizHandle);
  $vizContainer.css('position', 'absolute').css('top', y + 'px').css('left', x + 'px')
               .css('width', w + 'px').css('height', h + 'px').css('border', '1px solid #DDDDDD');
  var $inner;
  switch(type) {
  case "html":
    var $inner = $('<iframe></iframe>');
    $inner.load().appendTo($vizContainer);
    $inner.attr('src', src);
    break;
  case "png":
    var $inner = $('<img src="data:image/png;base64,' + src + '" />');
    $vizContainer.append($inner);
    break;
  default: 
    console.debug("Unknown visualization type!");
    return;
  }
  
  $inner.css('height', ($vizContainer.height() - 30) + "px").css('width', ($vizContainer.width() - 10) + "px");
  $container.append($vizContainer);
  $vizContainer.draggable({ containment: '#' + $container.attr('id'), handle: '.viz-handle', iframeFix: true, grid: [20, 20] });
  $vizContainer.resizable();
  $vizContainer.bind('resize', function() {
    $inner.css('height', ($(this).height() - 30) + "px").css('width', ($(this).width() - 10) + "px");
  });
  
  $vizContainer.hover(function() {
    $vizHandle.addClass('viz-handle-hover');
  },
  function() {
    $vizHandle.removeClass('viz-handle-hover');
  });
  
  if ($container.height() < (y + h)) {
    $container.css('height', (y + h + 5) + "px");
  }
}

Dashboard.prototype.refresh = function() {
  
}

Dashboard.prototype.lock = function() {
  
}

com.bouncingdata.Dashboard = new Dashboard();