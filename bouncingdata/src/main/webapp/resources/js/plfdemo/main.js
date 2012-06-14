function Main() {
}

Main.prototype.setContext = function(ctx) {
  this.ctx = ctx;
}

Main.prototype.initUI = function() {
  $(function() {
    outerLayout = $('.content-container #main-layout').layout({
      center__paneSelector: ".main-layout-center",
      west__paneSelector:   ".main-layout-west",
      west__size:       240,
      applyDefaultStyles: true,
      resizable: false,
      center__onresize: "innerLayout.resizeAll"
    });
    
    innerLayout = $('.workspace-container #workspace-layout').layout({
      center__paneSelector: ".workspace-layout-center",
      east__paneSelector:   ".workspace-layout-east",
      east__size: 400,
      applyDefaultStyles: true
    });
    
    $('input:button').button();
    $('input:submit').button();
  });
}
 
plfdemo = {};
plfdemo.Main = new Main();
plfdemo.Main.initUI();
