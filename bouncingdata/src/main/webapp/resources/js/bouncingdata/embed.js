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
  //$vizContainer.append($vizHandle);
  
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

function loadDatasetByAjax(dataPanelId) {
  var $dataPanel = $(dataPanelId);
  if ($dataPanel.length < 1) {
    $('bcdata-embedded-wrapper').html('<span>UI error. Cannot render data panel</span>');
    return;
  }
  var dsguids = '';
  $('.anls-dataset', $dataPanel).each(function() {
    dsguids += $(this).attr('dsguid') + ',';
  });
  dsguids = dsguids.substring(0, dsguids.length - 1);
  if (dsguids.length > 0) {
    setOverlay($dataPanel, true);
    $.ajax({
      url: ctx + '/public/data/m/' + dsguids,
      type: 'get',
      dataType: 'json',
      success: function(result) {
        setOverlay($dataPanel, false);
        $('.anls-dataset', $dataPanel).each(function() {
          var dsguid = $(this).attr('dsguid');
          var $table = $('table', $(this));
          var data = result[dsguid].data;
          if (data) {
            renderDatatable($.parseJSON(data), $table);
          } else if (result[dsguid].size > 0) {
            console.debug("Load datatable by Ajax...");
            var columns = result[dsguid].columns;
            var aoColumns = [];
            for (idx in columns) {
              aoColumns.push({ "mDataProp": columns[idx], "sTitle": columns[idx] });
            }
            var datatable = $table.dataTable({
              "bServerSide": true,
              "bProcessing": true,
              "sAjaxSource": ctx + "/public/data/ajax/" + dsguid,
              "aoColumns": aoColumns,
              "bJQueryUI": true,
              "sPaginationType": "full_numbers"
            });
            
            var keys = new KeyTable( {
              "table": $table[0],
              "datatable": datatable
            });
          }
        });
      },
      error: function(result) {
        setOverlay($dataPanel, false);
        console.debug('Failed to load datasets.');
        console.debug(result);
        $dataPanel.text('Failed to load datasets.');
      }
    });
  }
}

function renderDatatable(data, $table) {
  if (!data || data.length <= 0) return;
  
  //prepare data
  var first = data[0];
  var aoColumns = [];
  for (key in first) {
    aoColumns.push({ "sTitle": key});
  }
  
  var aaData = [];
  for (index in data) {
    var item = data[index];
    var arr = [];
    for (key in first) {
      arr.push(item[key]);
    }
    aaData.push(arr);
  }
  var datatable = $table.dataTable({
    "aaData": aaData, 
    "aoColumns": aoColumns, 
    "bJQueryUI": true,
    "sPaginationType": "full_numbers"
  });
  var keys = new KeyTable( {
    "table": $table[0],
    "datatable": datatable
  });
}

function setOverlay($panel, isActive) {
  if (isActive) {
    var $overlay = $('<div class="overlay-panel" style="position: absolute; top: 0; bottom: 0; left: 0; right: 0;"></div>');
    $overlay.css('background', 'url("' + ctx + '/resources/images/ajax-loader.gif") no-repeat 50% 10% #eee')
      .css('z-index', 10).css('background-size', '30px 30px').css('opacity', '0.8');
    if (!$panel.css('position')) {
      $panel.css('position', 'relative');
    }
    $panel.append($overlay);
  } else {
    $('div.overlay-panel', $panel).remove();
  }
}