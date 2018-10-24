 /*
  * LAMSLesson validation
  *
  * Date: 2012-01-07
  * LAMS Foundation (http://lamsfoundation.org)
  */	  

function PopupCenter(url, title, w, h) {
    // Credit: http://www.xtf.dk/2011/08/center-new-popup-window-even-on.html
    // Fixes dual-screen position                         Most browsers      Firefox
    var dualScreenLeft = window.screenLeft != undefined ? window.screenLeft : window.screenX;
    var dualScreenTop = window.screenTop != undefined ? window.screenTop : window.screenY;

    var width = window.innerWidth ? window.innerWidth : document.documentElement.clientWidth ? document.documentElement.clientWidth : screen.width;
    var height = window.innerHeight ? window.innerHeight : document.documentElement.clientHeight ? document.documentElement.clientHeight : screen.height;

    var left = ((width / 2) - (w / 2)) + dualScreenLeft;
    var top = ((height / 2) - (h / 2)) + dualScreenTop;
    var newWindow = window.open(url, title, 'scrollbars=yes, width=' + w + ', height=' + h + ', top=' + top + ', left=' + left);

    // Puts focus on the newWindow
    if (window.focus) {
        newWindow.focus();
    }
}

function openPreview(url,name, w, h) {
   url = url + "&ldId=" + document.getElementsByName("sequence_id")[0].value;
   url = url + "&course=" + course;
   PopupCenter(url,name, w, h);
    return false;
}

function selectSequence(obj, name){
    // if the selected object is a sequence (id!=0) then we assign the id to the hidden sequence_id
    // also if the name is blank we just add the name of the sequence to the name too.

    document.getElementsByName("sequence_id")[0].value = obj;

    if (obj!=0) {
	if (document.getElementsByName("name")[0].value == '') {
	    document.getElementsByName("name")[0].value = name;
	}
	document.getElementById('previewbutton').style.visibility='visible';
    } else {
	document.getElementById('previewbutton').style.visibility='hidden';
    }
}
