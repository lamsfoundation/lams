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

<script type="text/javascript">
<!--
var xmlDoc;
var wikiArray;
var oEditor = window.parent.InnerDialogLoaded() ;
var FCKLang = oEditor.FCKLang ;
var FCKWikiLinks = oEditor.FCKWikiLinks ;

var xmlHttp = getAjaxObject();
xmlHttp.onreadystatechange = function() 
{ 
    if(xmlHttp.readyState == 4) 
    {
    	var xmlDoc = xmlHttp.responseXML;
    	wikiArray = xmlDoc.getElementsByTagName('Wiki');
    }
 }

var url="<lams:LAMSURL/>/tool/lawiki10/WikiLinkHandler?"
	+ "&toolSessionId="+window.parent.InnerDialogLoaded().FCK.toolSessionId
	+ "&sessionMapID="+window.parent.InnerDialogLoaded().FCK.sessionMapID
	+ "&wikiID="+window.parent.InnerDialogLoaded().FCK.wikiID
	+ "&method="+"getWikis";

try{
	xmlHttp.open("GET",url,false);
	xmlHttp.send(null);
}
catch(e)
{
	alert("An error occurred: " + e);
}
function addOption(dropDownMenu, wikiName, wikiURL) 
{
	var option = document.createElement("Option");
	option.text = wikiName;
	option.value = wikiURL;
	dropDownMenu.options.add(option);
}

function init()
{
	// First of all, translate the dialog box texts
	oEditor.FCKLanguageManager.TranslatePage( document ) ;

	document.getElementById("newWikiLink").disabled = true;

	//alert('<fmt:message key="error.forgot.password.email" />');
	
	// Show the "Ok" button.
	window.parent.SetOkButton( true ) ;
}

function Ok()
{
	var linkText = document.getElementById('linkName').value ;
	var existingWikiSelected = document.getElementById('existingWikiRadioButton').checked ;
	var newWikiSelected = document.getElementById('newWikiRadioButton').checked ;
	
	var wikiUrl;

	if ( linkText.length == 0 )
	{
		alert(FCKLang.WikiLinkErrNoName ) ;
		return false ;
	}
	
	if ( existingWikiSelected ) {
		var existingWikiList = document.getElementById("existingWikiDropDownMenu");
		wikiUrl = existingWikiList.options[existingWikiList.selectedIndex].value;

		if ( wikiUrl.length == 0 )
		{
			alert( FCKLang.WikiLinkErrNoWiki ) ;
			return false;
		}
	}
	else if ( newWikiSelected ) 
	{
		var wikiNameStr = document.getElementById("newWikiLink").value;
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
	
	FCKWikiLinks.InsertWikiLink( linkText, wikiUrl );
	return true ;
}

function enableSelection()
{
	var existingWikiSelected = document.getElementById('existingWikiRadioButton').checked ;
	var newWikiSelected = document.getElementById('newWikiRadioButton').checked ;
	
	if ( existingWikiSelected ) {
		document.getElementById("newWikiLink").disabled = true;
		document.getElementById("existingWikiDropDownMenu").disabled = false;
	}
	else if ( newWikiSelected ) {
		document.getElementById("existingWikiDropDownMenu").disabled = true;
		document.getElementById("newWikiLink").disabled = false;
	}
}

function getAjaxObject()
{
    var ajavObj;
    try
    {
        // Firefox, Opera 8.0+, Safari
        ajaxObj=new XMLHttpRequest();
    }
    catch (e)
    {
        // Internet Explorer
        try
        {
            ajaxObj=new ActiveXObject("Msxml2.XMLHTTP");
        }
        catch (e)
        {
            try
            {
                ajaxObj=new ActiveXObject("Microsoft.XMLHTTP");
            }
            catch (e)
            {
                alert("Your browser does not support AJAX!");
            }
        }
    }
    return ajaxObj;
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
				<td><input id="linkName" type="text"></td>
			</tr>
			<tr>
				<td><span fckLang="WikiLinkExisting"></span></td>
				<td><input type="radio" id="existingWikiRadioButton"
					name="wikilinkgroup" selected="true" checked
					onchange="enableSelection();"></td>
				<td><select name="existingWikiDropDownMenu" id="existingWikiDropDownMenu">
						<option value="noSelection">Please Select</option>
						<script type='text/javascript'>
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
	       							addOption(document.getElementById('existingWikiDropDownMenu'), wikiName, wikiURL);
	          					}						
							}
						//-->		
						</script>
				</select></td>
			</tr>
			<tr>
				<td><span fckLang="WikiLinkNew"></span></td>
				<td><input type="radio" id="newWikiRadioButton"
					name="wikilinkgroup" onchange="enableSelection();"></td>
				<td><input id="newWikiLink" name="newWikiLink" type="text">
				</td>
		</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>
