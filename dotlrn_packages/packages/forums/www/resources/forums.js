var forums_collapse_symbol = '<img src="/resources/forums/Collapse16.gif" width="16" height="16" ALT="-" border="0">';
var forums_expand_symbol = '<img src="/resources/forums/Expand16.gif" width="16" height="16" ALT="+" border="0">';
var forums_replies = new Array();

// toggle visibility

function forums_getid(id) {
    if(document.getElementById)
        return document.getElementById(id);
    else if(document.all)
        return document.all(id);
    return false;
}

function forums_toggle(targetId, state){
    var symbol;
    var content = forums_getid('content'+targetId);
    var link = forums_getid('toggle'+targetId);

    if (!link || !content) return;

    var s;
    if (state != null)
        s = state;
    else if (content.className == "dynexpanded") {
        s = 0;
    } else {
        s = 1;
    }
    if (s) {
        content.className = "dynexpanded";
        symbol = forums_collapse_symbol;
    } else {
        content.className = "dyncollapsed";
        symbol = forums_expand_symbol;
    }

    if (link.innerHTML)
        link.innerHTML = symbol;
    else if (link.appendChild) {
        while(link.hasChildNodes())
            link.removeChild(link.firstChild);
        link.appendChild(document.createTextNode(symbol));
    }
}

function forums_toggleList(a,state) {
    if (!a.length) return;
    for (var i = 0; i < a.length; ++i) {
        forums_toggle(a[i], state);
    }
}
 
