
// Toggles whether to display advanced options in monitor summary for tools
function toggleAdvancedOptionsVisibility(div, img, imageUrl)
{
	var treeClosedIcon = imageUrl + "/images/tree_closed.gif";
	var treeOpenIcon = imageUrl + "/images/tree_open.gif";
	if (div.style.display == "block")
	{
		div.style.display = "none";
		img.src = treeClosedIcon;
	}
	else if (div.style.display == "none")
	{
		div.style.display = "block";
		img.src = treeOpenIcon;
	}
}