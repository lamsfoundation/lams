// global request and XML document objects
var req;

// retrieve XML document (reusable generic function);
// parameter is URL string (relative or complete) to
// an .xml file whose Content-Type is a valid XML
// type, such as text/xml; XML source must be from
// same domain as HTML file
function loadXMLDoc(url,target) {
    // branch for native XMLHttpRequest object
    if (window.XMLHttpRequest) {
        req = new XMLHttpRequest();
        req.onreadystatechange = function(){
				processReqChange(target);
		}
        req.open("GET", url, true);
        req.send(null);
    // branch for IE/Windows ActiveX version
    } else if (window.ActiveXObject) {
        req = new ActiveXObject("Microsoft.XMLHTTP");
        if (req) {
            req.onreadystatechange = function(){
				processReqChange(target);
			}
            req.open("GET", url, true);
            req.send();
        }
    }
}


// handle onreadystatechange event of req object
function processReqChange(target) {
    // only if req shows "loaded"
    if (req.readyState == 4) {
        // only if "OK"
        if (req.status == 200) {
			var select = document.getElementById(target);
			select.innerHTML = req.responseText;
         } else {
            alert("There was a problem retrieving the XML data:\n" +
                req.statusText);
         }
    }
}
//main function, will call loadXMLDoc() directly, but catch any exception and alert.
function loadDoc(url,target) {
	try {
			loadXMLDoc(url, target);
	}
	catch(e) {
		var msg = (typeof e == "string") ? e : ((e.message) ? e.message : "Unknown Error");
		alert("Unable to get XML data:\n" + msg);
		return;
	}
}