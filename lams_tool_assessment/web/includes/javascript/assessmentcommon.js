// toggles whether to display advanced options
function toggleVisibility(divId) {
	var divToHide = $("#" + divId);
	
	//change img's src
	var img = $(divToHide).parent().children().first();
	var oldSrc = img.attr("src");
	var newSrc = (oldSrc.indexOf("/images/tree_closed.gif") > -1) ? 
			oldSrc.replace("/images/tree_closed.gif", "/images/tree_open.gif") : 
			oldSrc.replace("/images/tree_open.gif", "/images/tree_closed.gif");
	img.attr("src", newSrc);

	divToHide.toggle("slow");
}

