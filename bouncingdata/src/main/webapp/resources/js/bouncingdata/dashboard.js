function Dashboard() {
  
}

Dashboard.prototype.init = function() {
  
}

Dashboard.prototype.load = function(vizList, dashboardPos, $container, editMode) {
  $container.empty();
  var count = 0;
  for (v in vizList) {
    var viz = vizList[v];
    var pos = dashboardPos?dashboardPos[viz.guid]:null;
    if (pos) {
      this.addViz(pos.x, pos.y, pos.w, pos.h, viz, $container, editMode);
    } else {
      this.addViz((count%2) * 365, Math.floor(count/2) * 280, 0, 0, viz, $container, editMode)
    }
    count++;
  }
}

Dashboard.prototype.viewDashboard = function() {
  
}

Dashboard.prototype.addViz = function(x, y, w, h, viz, $container, editMode) {
  if (!viz || !viz.source) return;
  x = x || 0;
  y = y || 0;
  
  var type = viz.type.toLowerCase();
  var src = viz.source;
  
  var $vizContainer = $('<div class="viz-container"></div>');
  $vizContainer.attr('guid', viz.guid);
    
  var $vizHandle = $('<div class="viz-handle"><span class="perm-link viz-permalink"><a href="" target="_blank">permalink</a></span></div>');
  $vizContainer.append($vizHandle);
  
  var $inner;
  switch(type) {
  case "html":
    w = w || 360;
    h = h || 280;
    $inner = $('<iframe></iframe>');
    $('a', $vizHandle).attr('href', src);
    $inner.load().appendTo($vizContainer);
    $inner.attr('src', ctx + '/' + src)
      .css('height', (h - 10) + "px")
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
      h = h || ($(this).height() + 30);
      // adjust viz. container size
      $(this).css('width', (w - 10) + 'px').css('height', (h - 30) + 'px');
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
  
  $vizContainer.css('position', 'absolute').css('top', y + 'px').css('left', x + 'px');
  
  $container.append($vizContainer);
  
  if (editMode) {
    var _x = $container.offset().left;
    var _y = $container.offset().top;
    var _w = $container.width(); // should be fixed
    var _h = $container.height();
    $vizContainer.draggable({ 
      containment: [_x, _y, _x + _w - w, 140000], 
      handle: '.viz-handle', 
      iframeFix: true, 
      grid: [10, 10] 
    });
    
    $vizContainer.resizable({
      containment: '#' + $container.attr('id'),
      grid: 10,
      aspectRatio: type=="png"
    });
    
    $vizContainer.bind('drag', function(event, ui) {
      if (ui.position.top + $(this).height() + 25 >= $container.height()) {
        $container.css('height', ($container.height() + 25) + "px");
      }
    }).bind('dragstop', function(event, ui) {
      if (ui.position.top + $(this).height() >= $container.height()) {
        $container.css('heigth', (ui.position.top + $(this).height() + 10) + "px");
      }
      // post back
      com.bouncingdata.Dashboard.postback($container);
    }).bind('resize', function(event, ui) {
      if (ui.position.top + $(this).height() + 25 >= $container.height()) {
        $container.css('height', ($container.height() + 25) + "px");
      }
      var cw = $(this).width(); 
      $inner.css('height', ($(this).height() - 10) + "px").css('width', (cw - 10) + "px");
      $vizContainer.draggable("option", "containment", [_x, _y, _x + _w - cw, 140000]);
      
    }).bind('resizestop', function(event, ui) {
      if (ui.position.top + $(this).height() >= $container.height()) {
        $container.css('heigth', (ui.position.top + $(this).height() + 10) + "px");
      }
      // post back
      com.bouncingdata.Dashboard.postback($container);
    });
    
    $vizContainer.hover(function() {
      $vizHandle.addClass('viz-handle-hover');
      $(this).addClass('viz-container-hover');
    },
    function() {
      $vizHandle.removeClass('viz-handle-hover');
      $(this).removeClass('viz-container-hover');
    });
  }
}

Dashboard.prototype.refresh = function() {
  
}

Dashboard.prototype.postback = function($container) {
  //need: app guid, guid and position of all viz. in dashboard
  var status = ""; 
  var guid = $container.attr('guid');
  if (!guid) return;
  $('div.viz-container', $container).each(function() {
    var vGuid = $(this).attr('guid');
    var x = Math.round($(this).position().left);
    var y = Math.round($(this).position().top);
    var w = Math.round($(this).width());
    var h = Math.round($(this).height());
    status = status + vGuid + "," + x + "," + y + "," + w + "," + h + ",";
  });
  status = status.substring(0, status.length - 1);
  
  console.debug("Posting back dashboard..");
  console.debug("guid = " + guid, "status = " + status);
  
  $.ajax({
    url: ctx + "/app/v/d/update/" + guid,
    type: "post",
    data: {
      status: status
    },
    success: function(result) {
      console.debug("Successfully update dashboard " + guid);
    },
    error: function(result) {
      console.debug(result);
    }
  });
}

/**
 * Locks and unlocks dashboard
 */
Dashboard.prototype.lock = function($container, lock) {
  if (lock) {
    $('div.viz-container', $container).each(function() {
      $(this).draggable("option", "disabled", true);
      $(this).resizable("option", "disabled", true);
      $(this).hover(null, null);
    });
  } else {
    $('div.viz-container', $container).each(function() {
      $(this).draggable("option", "disabled", false);
      $(this).resizable("option", "disabled", false);
      $(this).hover(function() {
        $('viz-handle', $(this)).addClass('viz-handle-hover');
        $(this).addClass('viz-container-hover');
      },
      function() {
        $('viz-handle', $(this)).removeClass('viz-handle-hover');
        $(this).removeClass('viz-container-hover');
      });
    });
  }
}

com.bouncingdata.Dashboard = new Dashboard();