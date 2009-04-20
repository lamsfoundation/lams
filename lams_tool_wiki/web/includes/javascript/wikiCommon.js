function changeDiv(divId)
{
	document.getElementById("edit").style.display = "none";
	document.getElementById("view").style.display = "none";
	document.getElementById("add").style.display = "none";
	document.getElementById("history").style.display = "none";
	document.getElementById(divId).style.display = "block";
	
	if (divId=="view")
	{
		//document.getElementById("viewFrame").style.display = "block";
		document.getElementById("finishButtonDiv").style.display = "block";
	}
	else
	{
		//document.getElementById(divId).style.display = "block";
		document.getElementById("finishButtonDiv").style.display = "none";
		//document.getElementById("viewFrame").style.display = "none";
	}

}

function cancelAdd(){
	document.getElementById("newPageTitle").value="";
	var fckEditor = FCKeditorAPI.GetInstance("newPageWikiBody");
	fckEditor.EditorDocument.body.innerHTML = "";
}

function changeWikiPage(pageName){
	document.getElementById("newPageName").value = pageName;
	submitWiki("changePage");
}

function doRevert(id)
{
	document.getElementById("historyPageContentId").value=id;
	submitWiki("revertPage");
}

function trim(str)
{
	var regX1 = /\n/gi;
	var regX2 = /\t/gi;
	str = new String(str);
	str = str.replace(/^\s+|\s+$/g, '').replace(regX1, "").replace(regX2, "");
	return str;
}

function doRemove(confirmMessage)
{
	var remove = confirm(confirmMessage);
	if (remove)
	{
		submitWiki('removePage');
	}
}

var compareWindow = null;
function doCompareOrView(webAppUrl, historyId, currentPageId, dispatch)
{
	var url = webAppUrl + "/authoring.do?";
	url += "&historyPageContentId=" + historyId;
	url += "&currentWikiPage=" + currentPageId;
	url += "&dispatch=" + dispatch;
	
	if(compareWindow && compareWindow.open && !compareWindow.closed){
		compareWindow.close();
	}
	compareWindow = window.open(url,'compareWindow','resizable,width=796,height=570,scrollbars');
	compareWindow.window.focus();
}

function toggleWikiList(webUrl)
{
	
	var wikiListDiv = document.getElementById("wikiList");
	var wikiListImage = document.getElementById("wikiListImage");
	
	if (wikiListDiv.style.display=="block")
	{
		wikiListDiv.style.display = "none";
		wikiListImage.src = webUrl +"/images/tree_closed.gif";
	}
	else
	{
		wikiListDiv.style.display = "block";
		wikiListImage.src = webUrl + "/images/tree_open.gif";
	}
}
