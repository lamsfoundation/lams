// JavaScript used to manage dynamic-mode comments
// (c) 2002 Joe Groff. Use this code under whatever terms you wish, as long
// as you give me some credit.

var nIframes = 0;

// Block data which should be set from Scoop
var collapse_symbol;
var expand_symbol;
var loading_symbol;
var loading_message;
var rootdir;
var sid;

// Internet Explorer on Windows prior to 5.5 doesn't support Array.push()
if(!Array.prototype.push) {
	Array.prototype.push = function() {
		var l = this.length;
		for(var i = 0; i < arguments.length; ++i,++l)
			this[l] = arguments[i];
	}
}

// Arrays of cid indices for comment replies and new comments
var replies = new Array();
var newcomments = new Array();

// Get an element by ID in a portable manner
function getid(id) {
       if(document.getElementById)
		return document.getElementById(id);
       else if(document.all)
		return document.all(id);
	return false;
}

// Get the document object inside an iframe in a portable manner
function getIframeDocument(id) {
	if(document.frames)
		return document.frames[id].document;
	var ifrm = getid(id);
	if(ifrm.contentDocument)
		return ifrm.contentDocument;
	else if(ifrm.contentWindow)
		return ifrm.contentWindow.document;
	else if(ifrm.document)
		return ifrm.document;
}

// Set the state of a comment.
// n is the cid of the comment to expand.
// move, if true, will cause the page to jump to that comment's header before
//   changing its state.
// state, if set, will change the comment to that specific state (0 = collapsed
//   1 = expanded). If not set or set to null, the state will be toggled.
// rate, if set, will cause the comment to be given that specific rating.
function toggle(n,move,state,rate) {
    var ifrmdoc = getIframeDocument('dynamic');
    var link = getid('toggle'+n);
    var content = getid('content'+n);
    if(!link || !content || !ifrmdoc)
        return;

    // Initialise the link's expandStatus if it hasn't yet been set
    if(link.className == '') {
        if (content.className == 'dynexpanded') { 
            link.className = 'dynexpanded';
        } else {
            link.className = 'dyncollapsed';
        }
    }
    if(link.className == 'dynexpanded') {
        link.expandStatus = 1;
    } else { 
        link.expandStatus = 0;
    }

    var s;
    if(state != null)
        s = state;

    else if(link.className == 'dyncollapsed')
        s = 1;	// set to expand
    else
        s = 0;	// set to collapse

    // Immediately return if we aren't changing state at all
    if((s == link.expandStatus) && (rate == null))
        return;

    if(move) window.location.hash = 'toggle'+n;

    if(rate == null
       && (((s == 1) && content.expandedContent)
           || ((s == 0) && content.collapsedContent))) {
        // If we've already grabbed the desired state for this comment,
        // set it right here
        setSavedState(n, s);
    }
    else {
        // Otherwise grab it from the server using the iframe
        setLoading(n);
        var src = rootdir+"?cid="+n+"&sid="+sid+"&dynamicmode="+s;
        if(rate != null)
            src += ";rate=Rate;rating_"+n+"="+rate;
        ifrmdoc.location.replace(src);
        // The content will be set by the iframe's onLoad property
    }
}

// Set the state of all the comments listed in an array.
function toggleList(a,state) {
	var fetch_cids = new Array();	// cids we need to ask the server for
	var contentprop = (state? 'expandedContent' : 'collapsedContent');
	var content;
	var ifrmdoc = getIframeDocument('dynamic');

	if(!a.length) return;
	for(var i = 0; i < a.length; ++i) {
		content = getid('content'+a[i]);
		if(!content) continue;
		// Skip this one if it's already in the proper state
		if((state && (content.className == 'dynexpanded'))
		   || (!state && (content.className == 'dyncollapsed')))
			continue;
		// Check if we already have the desired content for this cid
		if(content[contentprop]) 
			setSavedState(a[i], state);
		else {
			fetch_cids.push(a[i]);
			setLoading(a[i]);
		}
	}
	// Grab the remaining comments from the server
	if(fetch_cids.length > 0)
		ifrmdoc.location.replace(rootdir+"?cid="+fetch_cids.join('&cid=')+"&sid="+sid+"&dynamicmode="+state);
}

// Function to set comment content from already-saved data
function setSavedState(n,s) {
	var content = getid('content'+n);
	// Fill in the comment
	if(content.innerHTML) {
		if((s == 1) && content.expandedContent)
			content.innerHTML = content.expandedContent;
		else if((s == 0) && content.collapsedContent)
			content.innerHTML = content.collapsedContent;
	}
	else if(content.appendChild) {
		var nodes;
		if((s == 1) && content.expandedContent)
			nodes = content.expandedContent;
		else if((s == 0) && content.collapsedContent)
			nodes = content.collapsedContent;
		if(nodes) {
			while(content.hasChildNodes())
				content.removeChild(content.firstChild);
			for(var i = 0; i < nodes.length; ++i)
				content.appendChild(nodes[i]);
		}
	}
	// Set the link state
	setState(n,s);
}

// Set a link into waiting state
function setLoading(n) {
	var link = getid('toggle'+n);
	var content = getid('content'+n);
	if(!link || !content) return;

	if(content.innerHTML) {
		content.innerHTML = loading_message;
		link.innerHTML = loading_symbol;
	}
	else if(content.appendChild) {
		while(content.hasChildNodes())
			content.removeChild(content.firstChild);
		content.appendChild(document.createTextNode(loading_message));

		while(link.hasChildNodes())
			link.removeChild(link.firstChild);
		link.appendChild(document.createTextNode(loading_symbol));
	}
}

// Set a link into collapsed or expanded state
function setState(n,s) {
	var link = getid('toggle'+n);
	var content = getid('content'+n);
	if(!link || !content) return;

	var symbol;
	if (s) symbol = collapse_symbol;
	else  symbol = expand_symbol;

	if(link.innerHTML)
		link.innerHTML = symbol;
	else if(link.appendChild) {
		while(link.hasChildNodes())
			link.removeChild(link.firstChild);
		link.appendChild(document.createTextNode(symbol));
	}

	link.expandStatus = s;

	content.className = (s? 'dynexpanded' : 'dyncollapsed');
	link.className = (s? 'dynexpanded' : 'dyncollapsed');
}

// Called from the iframe's onload event to ship the content into the main
// page
function copyContent(no,s) {
    if(window == top || !parent.setState) return true;

    var content;
    var body = document.body;
    var divs;
    var n;

    // Get the set of comments out of the body
    if(body.children)
        divs = body.children.tags('DIV');
    else if(body.childNodes) {
        divs = new Array();
        for(var i = 0; i < body.childNodes.length; ++i)
            if(body.childNodes[i].tagName == 'DIV')
                divs.push(body.childNodes[i]);
    }

    for (var i = 0; i < divs.length; ++i) {
        n = divs[i].getAttribute('id');
        content = parent.getid('content'+n);
        if(!content) continue;

        if (content.innerHTML) {
            content.innerHTML = divs[i].innerHTML;
            if(s == 1) content.expandedContent = content.innerHTML;
            else if(s == 0) content.collapsedContent = content.innerHTML;
        }
        else if(content.appendChild) {
            while(content.hasChildNodes())
                content.removeChild(content.firstChild);
            for(var i = 0; i < divs[i].childNodes.length; ++i) {
                var nod = divs[i].childNodes[i].cloneNode(true);
                content.appendChild(nod);
            }
            if(s == 1) content.expandedContent = content.childNodes;
            else if(s == 0) content.collapsedContent = content.childNodes;
        }
        parent.setState(n,s);
    }
}

