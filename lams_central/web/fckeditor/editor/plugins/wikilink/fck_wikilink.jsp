<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta content="noindex, nofollow" name="robots">
		<style type="text/css">
			select {width: 100%}
		</style>
		
<script language="JavaScript" type="text/javascript" src="../../../../includes/javascript/jquery-latest.pack.js"></script>
<script language="JavaScript" type="text/javascript">
<!--
		
var xmlDoc;
var wikiArray;
var oEditor = window.parent.InnerDialogLoaded() ;
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


window.onload = function ()
{
	// First of all, translate the dialog box texts
	oEditor.FCKLanguageManager.TranslatePage( document ) ;

	document.wikiform.newWikiLink.disabled = true;

	// Show the "Ok" button.
	window.parent.SetOkButton( true ) ;
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
	
	var linkText = document.getElementById('linkName').value ;
	var existingWikiSelected = document.getElementById('existingWikiRadioButton').checked ;
	var newWikiSelected = document.getElementById('newWikiRadioButton').checked ;

	var oEditor2 = window.parent.InnerDialogLoaded() ;
	var FCKWikiLinks2 = oEditor2.FCKWikiLinks ;
		

	var wikiUrl;
	if ( linkText.length == 0 )
	{
		alert(FCKLang.WikiLinkErrNoName ) ;
		return false ;
	}
	
	if ( existingWikiSelected ) {

		var existingWikiList = document.wikiform.existingWikiDropDownMenu;
		wikiUrl = existingWikiList.options[existingWikiList.selectedIndex].value;

		if ( wikiUrl.length == 0 )
		{
			alert( FCKLang.WikiLinkErrNoWiki ) ;
			return false;
		}
	}
	else if ( newWikiSelected ) 
	{
		var wikiNameStr = document.wikiform.newWikiLink.value ;
		
		if ( wikiNameStr.length == 0 )
		{
			alert( FCKLang.WikiLinkErrNoWiki ) ;
			return false;
		}
		window.parent.jQuery.ajax({
            url : "<lams:LAMSURL/>/tool/lawiki10/WikiLinkHandler",
            data : {toolSessionId: window.parent.InnerDialogLoaded().FCK.toolSessionId,
            		sessionMapID: window.parent.InnerDialogLoaded().FCK.sessionMapID,
            		wikiID: window.parent.InnerDialogLoaded().FCK.wikiID,
            		wikiName: wikiNameStr,
            		method: 'createWiki'},	
           	async : false, 
            success : function(result) 
            {
          	
            	wikiUrl = result;
            }
        });
	}
	
	FCKWikiLinks2.InsertWikiLink( linkText, wikiUrl );
	return true ;
}

function enableSelection()
{
	var existingWikiSelected = document.getElementById('existingWikiRadioButton').checked ;
	var newWikiSelected = document.getElementById('newWikiRadioButton').checked ;
	
	if ( existingWikiSelected ) {
		document.wikiform.newWikiLink.disabled = true;
		document.wikiform.existingWikiDropDownMenu.disabled = false;
	}
	else if ( newWikiSelected ) {
		document.wikiform.existingWikiDropDownMenu.disabled = true;
		document.wikiform.newWikiLink.disabled = false;
	}
}

//-->
</script>
	</head>
	<body scroll="no" style="OVERFLOW: hidden">
	<form name="wikiform" action="">
		<table height="100%" cellSpacing="0" cellPadding="0" width="100%" border="0">
			<tr>
				<td>
					<table cellSpacing="3" cellPadding="3" align="center" border="0">
						<tr>
							<td colspan="2">
								<span fckLang="WikiLinkText"></span>
							</td>
							<td>
								<input id="linkName" type="text">
							</td>
						</tr>
						<tr>
							<td>
								<span fckLang="WikiLinkExisting"></span>
							</td>
							<td>
								<input type="radio" id="existingWikiRadioButton" name="wikilinkgroup" selected="true" checked 		onchange="enableSelection();">
							</td>
							<td>
								<select name="existingWikiDropDownMenu" id="existingWikiDropDownMenu">
									<option value="noSelection">Please Select</option>
									<script type='text/javascript'>
									<!--
										for (var wikiPage in wikiArray) 
										{	
											var wiki = wikiArray[wikiPage].attributes;
											var attr = wikiArray[wikiPage].attributes;
											if (attr!=null)
											{
												var attr = wikiArray[wikiPage].attributes;
	                							var wikiName = attr.getNamedItem('name').nodeValue;
	                							var wikiURL = attr.getNamedItem('url').nodeValue;
	                							addOption(document.getElementById('existingWikiDropDownMenu'), wikiName, wikiURL);
                							}
                							
										}
										
										
									//-->		
									</script>	
								</select>
							</td>
						</tr>
						<tr>
							<td>
								<span fckLang="WikiLinkNew"></span>
							</td>
							<td>
								<input type="radio" id="newWikiRadioButton" name="wikilinkgroup" onchange="enableSelection();">
							</td>
							<td>
								<input id="newWikiLink" name="newWikiLink" type="text">
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
	</body>
</html>
