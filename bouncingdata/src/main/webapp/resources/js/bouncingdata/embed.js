function view(vizList, dashboardPos, $container) {
  var count = 0, defaultSize = 380;
  $container.empty();
  for (v in vizList) {
    var viz = vizList[v];
    viz.name = v;
    var pos = dashboardPos[viz.guid];
    if (pos) {
      addViz(pos.x, pos.y, pos.w, pos.h, viz, $container);
    }
    else addViz((count%2 + 1)*10 + ((count%2) * defaultSize), (Math.floor(count/2)+1)*10 + (Math.floor(count/2) * defaultSize), defaultSize, defaultSize, viz, $container);
    count++;
  }
}

function addViz(x, y, w, h, viz, $container) {
  if (!viz || !viz.source) return;

  var type = viz.type.toLowerCase();
  var src = viz.source;
  
  var $vizContainer = $('<div class="viz-container"></div>');
  $vizContainer.attr('guid', viz.guid).attr('n', viz.name);
    
  var $vizHandle = $('<div class="viz-handle"><span class="permalink viz-permalink"><a href="" target="_blank">permalink</a></span></div>');
  $vizContainer.append($vizHandle);
  
  var $inner;
  switch(type) {
  case "html":
    $inner = $('<iframe></iframe>');
    $('a', $vizHandle).attr('href', src);
    $inner.load().appendTo($vizContainer);
    $inner.attr('src', ctx + '/' + src)
      .css('height', (h - 15) + "px")
      .css('width', (w - 10) + "px");
    $vizContainer.css('width', w + 'px').css('height', h + 'px');
    if ($container.height() < (y + h)) {
      $container.css('height', (y + h + 10) + "px");
    }
    break;
  case "png":
    $inner = $('<img src="data:image/png;base64,' + src + '" />');
    $('a', $vizHandle).attr('href', 'data:image/png;base64,' + src);
    $inner.bind('load', function() {
      w = w || ($(this).width() + 10);
      h = h || ($(this).height() + 15);
      // adjust viz. container size
      $(this).css('width', (w - 10) + 'px').css('height', (h - 15) + 'px');
      $vizContainer.css('width', w + 'px').css('height', h + 'px');
      if ($container.height() < (y + h)) {
        $container.css('height', (y + h + 10) + "px");
      }
    }).appendTo($vizContainer);
    break;
  default: 
    console.debug("Unknown visualization type!");
    return;
  }
  
  $vizContainer.css('position', 'absolute')
    .css('top', y + 'px')
    .css('left', x + 'px')
     
  $container.append($vizContainer);

}