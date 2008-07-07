<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta content="noindex, nofollow" name="robots">
<style type="text/css">
select {
	width: 100%
}
</style>
<script type="text/javascript"
	src="../../../../includes/javascript/jquery-latest.pack.js"></script>
<script type="text/javascript">
<!--
var xmlDoc;
var wikiArray;
var oEditor = window.parent.InnerDialogLoaded() ;
var FCK = oEditor.FCK;
var FCKLang = oEditor.FCKLang ;
var FCKWikiLinks = oEditor.FCKWikiLinks ;

jQuery.ajax({
    url : "<lams:LAMSURL/>/tool/lawiki10/WikiLinkHandler",
    data : {toolSessionId: window.parent.InnerDialogLoaded().FCK.toolSessionId,
    		sessionMapID: window.parent.InnerDialogLoaded().FCK.sessionMapID,
    		wikiID: window.parent.InnerDialogLoaded().FCK.wikiID,
    		method: 'getWikis'},
    async : false, 
    success : function(xml) {
    	xmlDoc = xml;
    	wikiArray = xmlDoc.getElementsByTagName('Wiki');
    }
});

function init()
{
	// First of all, translate the dialog box texts
	oEditor.FCKLanguageManager.TranslatePage( document ) ;
	//alert('<fmt:message key="error.forgot.password.email" />');
	
	document.getElementById("linkAlias").value = getSelectedText();

	// Show the "Ok" button.
	window.parent.SetOkButton( true ) ;
}

function getSelectedText() 
{
    var selection = "";
    if( FCK.EditorDocument.selection != null ) {
      selection = FCK.EditorDocument.selection.createRange().text; // (Internet Explorer)
    } 
    else {
      selection = oEditor.FCK.EditorWindow.getSelection(); // (FireFox) after this, won't be a string 
      selection = "" + selection; // now a string again
    }
    return selection;
}

function addOption(dropDownMenu, wikiName, wikiURL)
{
	var option = document.createElement("Option");
	option.text = wikiName;
	option.value = wikiURL;
	dropDownMenu.options.add(option);
}

function Ok()
{
	var str = document.getElementById('linkAlias').value ;
	var linkAlias = str.replace(/^\s+|\s+$/g, '') ; //trim string of spaces
	
	var wikiUrl;

	if ( linkAlias.length == 0 )
	{
		alert(FCKLang.WikiLinkErrNoName ) ;
		return false ;
	}
	
	var existingWikiList = document.getElementById("existingWikiDropDownMenu");
	wikiUrl = existingWikiList.options[existingWikiList.selectedIndex].value;
	if (existingWikiList.selectedIndex == 0 )
	{
		alert( FCKLang.WikiLinkErrNoWiki ) ;
		return false;
	}
	
	FCKWikiLinks.InsertWikiLink( linkAlias, wikiUrl );
	return true ;
}

//-->
</script>
</head>
<body scroll="no" style="OVERFLOW: hidden" onload="init()">
<form name="wikiform" action="">
<table height="100%" cellSpacing="0" cellPadding="0" width="100%"
	border="0">
	<tr>
		<td>
		<table cellSpacing="3" cellPadding="3" align="center" border="0">
			<tr>
				<td colspan="2"><span fckLang="WikiLinkText"></span></td>
				<td><input id="linkAlias" type="text"></td>
			</tr>
			<tr>
				<td><span fckLang="WikiLinkExisting"></span></td>
				<td></td>
				<td><select name="existingWikiDropDownMenu"
					id="existingWikiDropDownMenu">
					<option value="noSelection">Please Select</option>
					<script type="text/javascript">
						<!--
							var i;
							for (i=0; i<wikiArray.length; ++i) 
							{	
								var wiki = wikiArray[i];
								var attr = wiki.attributes;
								if (attr!=null)
								{
	       							var wikiName = attr.getNamedItem('name').nodeValue;
	       							var wikiURL = attr.getNamedItem('url').nodeValue;
	       							if (wikiName.length < 18)
	       								addOption(document.getElementById('existingWikiDropDownMenu'), wikiName, wikiURL);
	       							else 
	          							addOption(document.getElementById('existingWikiDropDownMenu'), wikiName.substr(0,15) + "...", wikiURL);
	          					}
							}
						//-->		
					</script>
				</select>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>
