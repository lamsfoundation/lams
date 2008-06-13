<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<script type="text/javascript">
	function hideToolbarItem(editorInstance, itemName) {
		toolbarItem = editorInstance.EditorWindow.parent.FCKToolbarItems.LoadedItems[itemName]._UIButton.MainElement;
		toolbarItem.style.display = 'none';
	}
	
	function FCKeditor_OnComplete(editorInstance) 
	{ 	
		if (!${sessionMap.allowAttachImages}) {	
			hideToolbarItem(editorInstance, 'Image');
		}
		if (!${sessionMap.allowInsertWikiLinks}) {	
			hideToolbarItem(editorInstance, 'WikiLink');
		}
		if (!${sessionMap.allowInsertExternalLinks}) {	
			hideToolbarItem(editorInstance, 'Link');
		}
		document.getElementById("fckbox").style.visibility = 'visible';
	}
</script>