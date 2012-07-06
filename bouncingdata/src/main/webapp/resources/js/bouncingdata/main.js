function Main() {
}

Main.prototype.setContext = function(ctx) {
  this.ctx = ctx;
}

Main.prototype.initUI = function() {
  $(function() {
    /*
    outerLayout = $('.content-container #main-layout').layout({
      center__paneSelector: ".main-layout-center",
      west__paneSelector:   ".main-layout-west",
      west__size:       240,
      applyDefaultStyles: true,
      resizable: false,
      center__onresize: "com.bouncingdata.Workspace.resizeAll()"
    });
    */
    /*innerLayout = $('.workspace-container #workspace-layout').layout({
      center__paneSelector: ".workspace-layout-center",
      east__paneSelector:   ".workspace-layout-east",
      east__size: 400,
      applyDefaultStyles: true
    });*/
    
    $('input:button').button();
    $('input:submit').button();
    
  });
}

function Utils() {
}

Utils.prototype.getConsoleCaret = function(language) {
  if (language == "python") return ">>>";
  else if (language == "r") return ">";
  else return null;
}


com = {};
com.bouncingdata = {};
com.bouncingdata.Main = new Main();
com.bouncingdata.Utils = new Utils();
Utils = com.bouncingdata.Utils;
com.bouncingdata.Main.initUI();
