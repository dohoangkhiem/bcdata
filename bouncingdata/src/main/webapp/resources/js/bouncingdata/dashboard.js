function Dashboard() {
  
}

Dashboard.prototype.Layout = {
    ONE_COLUMN: 1,
    TWO_COLUMN: 2,
    THREE_COLUMN: 3
}

Dashboard.prototype.init = function() {
  this.zCounter = {};
  this.dashboardCache = {};
}

Dashboard.prototype.prepareLayout = function($container, layout) {
  
}

Dashboard.prototype.loadAll = function(vizList, dashboardPos, $container) {
  
}

Dashboard.prototype.load = function(vizList, dashboardPos, $container, editMode) {
  var dbCache;
  if (editMode) {
    this.zCounter[$container.attr('tabid')] = 50;
    dbCache = this.dashboardCache[$container.attr('guid')];
  }
  $container.empty();
  var count = 0, defaultSize = 380; //Math.floor($container.width()/2);
  
  if (!dashboardPos) {
    // the vizList will be positioned automatically
    for (v in vizList) {
      var viz = vizList[v];
      viz.name = v;
      useCache = false;
      var pos = dbCache && useCache ?dbCache[v]:null;
      if (pos) {
        this.addViz(pos.x, pos.y, pos.w, pos.h, viz, $container, editMode);
      } else {
        this.addViz((count%2 + 1)*10 + ((count%2) * defaultSize), (Math.floor(count/2)+1)*10 + (Math.floor(count/2) * defaultSize), defaultSize, defaultSize, viz, $container, editMode);
      }
      count++;
    }
  } else {
    for (v in vizList) {
      var viz = vizList[v];
      viz.name = v;
      var pos = dashboardPos[viz.guid] || (dbCache?dbCache[v]:null);
      if (pos) {
        this.addViz(pos.x, pos.y, pos.w, pos.h, viz, $container, editMode);
        if (editMode) {
          if (!dbCache) {
            this.dashboardCache[$container.attr('guid')] = {};
            dbCache = this.dashboardCache[$container.attr('guid')];
          }
          dbCache[v] = {x: pos.x, y: pos.y, w: pos.w, h: pos.h}
        }
      }
      else this.addViz((count%2 + 1)*10 + ((count%2) * defaultSize), (Math.floor(count/2)+1)*10 + (Math.floor(count/2) * defaultSize), defaultSize, defaultSize, viz, $container, editMode);
      count++;
    }
  }
  
}


Dashboard.prototype.viewDashboard = function() {
  
}

Dashboard.prototype.addViz = function(x, y, w, h, viz, $container, editMode) {
  if (!viz || !viz.source) return;

  var me = this;
  var type = viz.type.toLowerCase();
  var src = viz.source;
  
  var $vizContainer = $('<div class="viz-container"></div>');
  $vizContainer.attr('guid', viz.guid).attr('n', viz.name);
  $vizContainer.css('width', w + 'px').css('height', h + 'px');  
  //var $vizHandle = $('<div class="viz-handle"><span class="permalink viz-permalink"><a href="" target="_blank">permalink</a></span></div>');
  var $vizHandle = $('<div class="viz-handle"></div>');
  $vizContainer.append($vizHandle);
  
  var $inner;
  switch(type) {
  case "html":
    $inner = $('<iframe style="width:' + (w-10) + 'px; height:' + (h-15) + 'px;"></iframe>');
    $('a', $vizHandle).attr('href', src);
    $inner.load().appendTo($vizContainer);
    
    $inner.attr('src', ctx + '/' + src);
      
    if ($container.height() < (y + h)) {
      $container.css('height', (y + h + 10) + "px");
      if (editMode) me.updateRuler($container);
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
        if (editMode) me.updateRuler($container);
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
   
  if (editMode) $vizContainer.css('z-index', this.zCounter[$container.attr('tabid')]++);
  
  $container.append($vizContainer);
  
  if (editMode) {
    
    // add info tooltip
    var $info = $('<div class="viz-dimension-info"></div>');
    $info.css('position', 'absolute').css('top', 0).css('left', 0)
      .css('border', '1px solid #555555').css('background-color', 'yellow').css('z-index', 9999);
    $info.css('padding', '2px').css('opacity', 0.8).css('font-size', '10px');
    
    $vizContainer.append($info);
    $info.hide();
    
    var _x = $container.offset().left;
    var _y = $container.offset().top;
    var _w = $container.width(); // should be fixed
    var _h = $container.height();
    
    $vizContainer.draggable({ 
      //containment: [_x, _y, _x + _w - w, 140000],
      containment: '#dashboard-wrapper-' + $container.attr('tabid'), 
      handle: '.viz-handle', 
      iframeFix: true, 
      grid: [10, 10] 
    });
    
    $vizContainer.resizable({
      //containment: '#' + $container.attr('id'),
      containment: '#dashboard-wrapper-' + $container.attr('tabid'),
      //containment: [_x, _y, _x + w, 140000],
      grid: 10,
      //aspectRatio: type=="png"
      aspectRatio: false
    });
      
    $vizContainer.bind('click', function(event, ui) {
      $(this).css('z-index', me.zCounter[$container.attr('tabid')]++);
    })
    .bind('dragstart', function(event, ui) {
      $(this).css('z-index', me.zCounter[$container.attr('tabid')]++);
      $info.show();
    })
    .bind('resizestart', function(event, ui) {
      $(this).css('z-index', me.zCounter[$container.attr('tabid')]++);
      var $currentViz = this;
      $info.show();
      
      // iframe fix for resizing
      $("iframe", $container).each(function() {
        var $frame = $(this);
        var $resizeIframeFix = $('<div class="resizable-iframe-fix" style="position: absolute; opacity: 0.001; z-index: 9999;"></div>');
        if ($frame.is($currentViz.children)) {
          $resizeIframeFix.addClass("resizable-iframe-fix-inner");
        }
        $resizeIframeFix.css('background', 'none repeat scroll left top #FFFFFF')
          .css('top', $frame.offset().top)
          .css('left', $frame.offset().left)
          .css('width', $frame.width())
          .css('height', $frame.height());
        $("body").append($resizeIframeFix);
      });
      
    });
    
    $vizContainer.bind('drag', function(event, ui) {
      me.showSnapLines($(this), $container, false);
      
      if (ui.position.top + $(this).height() + 15 >= $container.height()) {
        $container.css('height', ($container.height() + 15) + "px");
        me.updateRuler($container);
      }
      $info.text('left: ' + Math.round(ui.position.left) + ', top: ' + Math.round(ui.position.top));
      
    })
    
    .bind('dragstop', function(event, ui) {  
      me.hideSnapLines($container);
      $info.hide();
      
      if (ui.position.top + $(this).height() >= $container.height()) {
        $container.css('heigth', (ui.position.top + $(this).height() + 10) + "px");
        me.updateRuler($container);
      }
      // post back
      me.postback($container, "rearrange");
    })
    
    .bind('resize', function(event, ui) {
      me.showSnapLines($(this), $container, true);
      
      if (ui.position.top + $(this).height() + 15 >= $container.height()) {
        $container.css('height', ($container.height() + 15) + "px");
        me.updateRuler($container);
      }
      
      var cw = $(this).width(), ch = $(this).height(); 
      $inner.css('height', (ch - 15) + "px").css('width', (cw - 10) + "px");
      //$vizContainer.draggable("option", "containment", [_x, _y, _x + _w - cw, 140000]);
      
      // update inner iframe-fix position
      if ($inner[0].tagName.toLowerCase() == "iframe") {
        var $innerIframeFix = $(".resizable-iframe-fix.resizable-iframe-fix-inner");
        $innerIframeFix.css('top', $inner.offset().top)
          .css('left', $inner.offset().left)
          .css('width', $inner.width())
          .css('height', $inner.height());
      }
      
      $info.text('width: ' + Math.round($(this).width()) + ', height: ' + Math.round($(this).height()));
    })
    
    .bind('resizestop', function(event, ui) {
      me.hideSnapLines($container);
      $info.hide();
      
      if (ui.position.top + $(this).height() >= $container.height()) {
        $container.css('heigth', (ui.position.top + $(this).height() + 10) + "px");
        me.updateRuler($container);
      }
      
      //remove resizable-iframe-fix
      $('.resizable-iframe-fix').remove();
      
      // post back
      me.postback($container, "rearrange");
      
      // request re-plot viz.
      if (type == "png") {
        var anlsGuid = $container.attr('guid');
        var vizGuid = $(this).attr('guid');
        if (!anlsGuid || !vizGuid) return;
        console.debug("Request for replay plot..")
        $.ajax({
          url: ctx + "/visualize/replot/" + anlsGuid + "/" + vizGuid + "/png",
          type: "get",
          data: {
            w: $vizContainer.width() - 10,
            h: $vizContainer.height() - 15
          },
          success: function(res) {
            console.debug("Successfully replay plot.");
          }, 
          error: function(res) {
            console.debug("Failed to replot viz.");
            console.debug(res);
          }
        });
      }
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

/**
 * Post dashboard status to server
 */
Dashboard.prototype.postback = function($container, cause) {
  //need: app guid, guid and position of all viz. in dashboard
  var status = ""; 
  var guid = $container.attr('guid');
  var me = this;
  if (!guid) return;
  
  $('div.viz-container', $container).each(function() {
    var vGuid = $(this).attr('guid');
    var x = Math.round($(this).position().left);
    var y = Math.round($(this).position().top);
    var w = Math.round($(this).width());
    var h = Math.round($(this).height());
    status = status + vGuid + "," + x + "," + y + "," + w + "," + h + ",";
    
    if (!me.dashboardCache[guid]) me.dashboardCache[guid] = {};
    me.dashboardCache[guid][$(this).attr('n')] = {x: x, y: y, w: w, h: h};
  });
  status = status.substring(0, status.length - 1);
  
  console.debug("Posting back dashboard..");
  console.debug("guid = " + guid, "status = " + status);
  
  $.ajax({
    url: ctx + "/app/v/d/update/" + guid,
    type: "post",
    data: {
      status: status,
      cause: cause
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
 * 
 */
Dashboard.prototype.view = function(vizList, dashboardPos, $container) {
  var count = 0, defaultSize = 380;
  $container.empty();
  for (v in vizList) {
    var viz = vizList[v];
    viz.name = v;
    var pos = dashboardPos[viz.guid];
    if (pos) {
      this.addViz(pos.x, pos.y, pos.w, pos.h, viz, $container, false);
    }
    else this.addViz((count%2 + 1)*10 + ((count%2) * defaultSize), (Math.floor(count/2)+1)*10 + (Math.floor(count/2) * defaultSize), defaultSize, defaultSize, viz, $container, false);
    count++;
  }
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
      $('viz-handle', this).hide();
      $(this).css('border-style', 'none');
      var $ruler = $('.dashboard-ruler', $container.parent());
      if (!$ruler || !$ruler.hasClass("dashboard-ruler")) {
        console.debug("Can't find ruler for the dashboard " + $container);
        return; 
      }
      $ruler.hide();
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
      $(this).css('border-style', 'solid');
      var $ruler = $('.dashboard-ruler', $container.parent());
      if (!$ruler || !$ruler.hasClass("dashboard-ruler")) {
        console.debug("Can't find ruler for the dashboard " + $container);
        return; 
      }
      $ruler.show();
    });
  }
}

/**
 * Update dashboard ruler to match the height
 */
Dashboard.prototype.updateRuler = function($container) {
  var $ruler = $('.dashboard-ruler', $container.parent());
  if (!$ruler) {
    console.debug("Can't find ruler for the dashboard " + $container);
    return; 
  }
  
  $ruler.height($container.height());
}

/**
 * Display the snap lines for resizing/dragging alignment
 */
Dashboard.prototype.showSnapLines = function($viz, $container, isResized) {
  var $ruler = $('.dashboard-ruler', $container.parent());
  var $topLine = $('.snap-line-top', $ruler), $leftLine = $('.snap-line-left', $ruler),
    $rightLine = $('.snap-line-right', $ruler), $bottomLine = $('.snap-line-bottom', $ruler);
  
  var pos = $viz.position();
  if (!isResized) {
    $topLine.css('top', pos.top).css('left', 0).css('opacity', 1).width($container.width());
    $leftLine.css('top', 0).css('left', pos.left).css('opacity', 1).height($container.height());
  }
  $bottomLine.css('top', pos.top + $viz.height() + 1).css('left', 0).css('opacity', 1).width($container.width());
  $rightLine.css('top', 0).css('left', pos.left + $viz.width() + 1).css('opacity', 1).height($container.height());
}

Dashboard.prototype.hideSnapLines = function($container) {
  var $ruler = $('.dashboard-ruler', $container.parent());
  $('.snap-line', $ruler).css('opacity', 0);
}

com.bouncingdata.Dashboard = new Dashboard();
com.bouncingdata.Dashboard.init();