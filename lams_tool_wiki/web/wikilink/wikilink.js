var xmlDoc;
var wikiArray;
var CKGlobal = window.parent.CKEDITOR;
var thisDialog = CKGlobal.dialog.getCurrent();
var CK = thisDialog.getParentEditor();
// Get the array of possible wiki links from the opening window
wikiArray = window.parent.wikiLinkArray;

function init()
{
	// No need or possibility to do that in CKEditor 3
	// oEditor.FCKLanguageManager.TranslatePage( document ) ;
	// but we need to do this instead
	
	document.getElementById("linkAliasLabel").firstChild.data = CK.lang.wikilink.WikiLinkText;
	document.getElementById("existingLinkMenuLabel").firstChild.data = CK.lang.wikilink.WikiLinkExisting;

	document.getElementById("linkAlias").value = CK.getSelection().getSelectedText();
	
	// remove our previously registered listeners and reregister a new one
	var okButton = thisDialog.getButton('ok');
	var newListeners = [];
	for (var i = 0; i < okButton._.events.click.listeners.length; i++){
		if (okButton._.events.click.listeners[i].priority != 1) {
			newListeners.push(okButton._.events.click.listeners[i]);
		}
	}
	okButton._.events.click.listeners = newListeners;
	okButton.on('click', triggerOK, null, null, 1);
		
	// Show the "Ok" button - doesn't work in CKEditor 3, but shouldn't cause problems
	// window.parent.SetOkButton( true ) ;
}



function addOption(dropDownMenu, wikiName, wikiURL)
{
	var option = document.createElement("Option");
	option.text = wikiName;
	option.value = wikiURL;
	dropDownMenu.options.add(option);
}

function prettyWikiLink(wikiLink)
{
	return wikiLink.replace("`", "'").replace("&quot;", '"');
}

function triggerOK(ev)
{
	var str = document.getElementById('linkAlias').value ;
	var linkAlias = str.replace(/^\s+|\s+$/g, '') ; // trim string of spaces
	
	var wikiUrl;

	if ( linkAlias.length == 0 )
	{
		alert(CK.lang.wikilink.WikiLinkErrNoName ) ;
		ev.stop();
		return;
	}
	
	var existingWikiList = document.getElementById("existingWikiDropDownMenu");
	wikiUrl = existingWikiList.options[existingWikiList.selectedIndex].value;
	if (existingWikiList.selectedIndex == 0 )
	{
		alert( CK.lang.wikilink.WikiLinkErrNoWiki ) ;
		ev.stop();
		return;
	}
	
	// workaround for FF "security feature" clearing malicious code
	// to be inserted in contenteditable secitions
	var escapedWikiUrl = 'mediaembedInsertData|---' + escape(wikiUrl) + '---|mediaembedInsertData';
	CK.insertHtml( '<a href="' + escapedWikiUrl + '" class="skip-auto-target">' + linkAlias + '</a>' );
	var updatedEditorData = CK.getData();
	var cleanEditorData = updatedEditorData.replace(escapedWikiUrl, wikiUrl);
	CK.setData(cleanEditorData);
}