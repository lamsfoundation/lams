 /*
  * LAMSLesson validation
  *
  * Date: 2012-01-07
  * LAMS Foundation (http://lamsfoundation.org)
  */	  

function openAuthor(url,name,fullscreen) {
   authorWin = window.open(url,name,options);
   if (fullscreen) {
	authorWin.moveTo(0,0);
	authorWin.resizeTo(screen.availWidth,screen.availHeight);
   }
   authorWin.focus();
  return false;
}

function openPreview(url,name,fullscreen) {
   url = url + "&ldId=" + document.getElementsByName("sequence_id")[0].value;
   url = url + "&course=" + course;
   previewWin = window.open(url,name,options);
    if (fullscreen) {
      previewWin.moveTo(0,0);
      previewWin.resizeTo(screen.availWidth,screen.availHeight);
    }
    previewWin.focus();
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
