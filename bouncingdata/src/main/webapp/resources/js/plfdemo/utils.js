function Utils() {
  
}

Utils.prototype.countLines = function(str) {
  
}

Workspace.prototype.getConsoleCaret = function(language) {
  if (language == "python") return ">>>";
  else if (language == "r") return ">";
  else return null;
}

plfdemo.Utils = new Utils();