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
	var ckEditor = CKEDITOR.instances["newPageWikiBody"];
	// fckEditor.EditorDocument.body.innerHTML = "";
	ckEditor.setData("");
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
	var regX1 = /\r?\n/g;
	var regX2 = /\t/g;
	str = new String(str);
	str = str.replace(/^\s+|\s+$/g, '').replace(regX1, "").replace(regX2, "");
	return str;
}

function doRemove(confirmMessage)
{
	var remove = confirm(confirmMessage);
	if (remove)
	{
		submitWiki("removePage");
	}
}

var compareWindow = null;

function doCompareOrView(webAppUrl, historyId, currentPageId, actionMethod)
{
	var url = webAppUrl + "/learning/"+ actionMethod +".do?";
	url += "&historyPageContentId=" + historyId;
	url += "&currentWikiPage=" + currentPageId;
	
	if(compareWindow && compareWindow.open && !compareWindow.closed){
		compareWindow.close();
	}
	compareWindow = window.open(url,'compareWindow','resizable,width=1152,height=648,scrollbars');
	compareWindow.window.focus();
}

function toggleWikiList(webUrl)
{
	var wikiListDiv = document.getElementById("wikiList");
	
	$('#iconToggle').toggleClass('fa-plus-square-o fa-minus-square-o');
	
	if (wikiListDiv.style.display=="block")
	{
		wikiListDiv.style.display = "none";
	}
	else
	{
		wikiListDiv.style.display = "block";
	}
}

//LDEV-2824 Replace "javascript" with another word before posting so browser does not detect it as XSS attack
function replaceJavascriptTokenAndSubmit(formName) {
	// updating CKEditor instance is asynchronous, but we need to make sure it's completed
	// before submitting the form, thus sophisticated synchronization :/
	var instanceUpdateStarted = 0;
	var instanceUpdateCompleted = 0;
	
	for (var instanceId in CKEDITOR.instances){
		var instance = CKEDITOR.instances[instanceId];
		var data = instance.getData();
		var encodedData = data.replace(/javascript/g,"JAVASCRIPTREPLACE");
		instanceUpdateStarted++;
		instance.setData(encodedData, function() {
			instance.updateElement();
			instanceUpdateCompleted++;
		});
	}
	
	var synchro = setInterval(function() {
		if (instanceUpdateCompleted >= instanceUpdateStarted){
			 clearInterval(synchro);
			 document.getElementById(formName).submit();
			}
	}, 500);
}
