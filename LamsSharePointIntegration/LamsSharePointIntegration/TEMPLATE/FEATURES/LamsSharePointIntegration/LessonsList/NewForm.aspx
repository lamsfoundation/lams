<%@ Page language="C#" Debug="true" MasterPageFile="~masterurl/default.master"    Inherits="Microsoft.SharePoint.WebPartPages.WebPartPage,Microsoft.SharePoint,Version=12.0.0.0,Culture=neutral,PublicKeyToken=71e9bce111e9429c" %> 
<%@ Assembly Name="Microsoft.SharePoint, Version=12.0.0.0, Culture=neutral, PublicKeyToken=71e9bce111e9429c"%> 
<%@ Assembly Name="LamsSharePointIntegration, Version=1.0.0.0, Culture=neutral, PublicKeyToken=2c5da6fd93fafb88" %>
<%@ Register Tagprefix="SharePoint" Namespace="Microsoft.SharePoint.WebControls" Assembly="Microsoft.SharePoint, Version=12.0.0.0, Culture=neutral, PublicKeyToken=71e9bce111e9429c" %> 
<%@ Register Tagprefix="Utilities" Namespace="Microsoft.SharePoint.Utilities" Assembly="Microsoft.SharePoint, Version=12.0.0.0, Culture=neutral, PublicKeyToken=71e9bce111e9429c" %> 
<%@ Import Namespace="Microsoft.SharePoint" %> 
<%@ Register Tagprefix="WebPartPages" Namespace="Microsoft.SharePoint.WebPartPages" Assembly="Microsoft.SharePoint, Version=12.0.0.0, Culture=neutral, PublicKeyToken=71e9bce111e9429c" %>
<%@ Import Namespace="LamsSharePointIntegration" %>
<asp:Content ID="Content1" ContentPlaceHolderId="PlaceHolderPageTitle" runat="server">
	<SharePoint:ListFormPageTitle ID="ListFormPageTitle1" runat="server"/>
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderId="PlaceHolderPageTitleInTitleArea" runat="server">
	<SharePoint:ListProperty Property="LinkTitle" runat="server" id="ID_LinkTitle"/>: <SharePoint:ListItemProperty id="ID_ItemProperty" MaxLength=40 runat="server"/>
</asp:Content>

<asp:Content ID="Content3" ContentPlaceHolderId="PlaceHolderPageImage" runat="server">
	<IMG SRC="/_layouts/images/blank.gif" width=1 height=1 alt="">
</asp:Content>

<asp:Content ID="Content4" ContentPlaceHolderId="PlaceHolderLeftNavBar" runat="server"/>

<asp:Content ID="Content5" ContentPlaceHolderId="PlaceHolderMain" runat="server">
    
    <script language="JavaScript" type="text/javascript" src="/_layouts/LamsSharePointIntegration/tigra/tree.js"></script>
    <script language="JavaScript" type="text/javascript">
    <!-- 
        var authorUrl = "";
	    var previewUrl = "";
	    var learningDesignRepositoryStr = ""; 
	    
	    // ajax object
	    var xmlHttp;
	    
	    // Get the author url
	    xmlHttp1 = getAjaxObject();
        xmlHttp1.onreadystatechange = function() 
        {
            if (xmlHttp1.readyState == 4) 
            { 
                authorUrl = xmlHttp1.responseText;
            }
        }
        xmlHttp1.open("GET", "/_layouts/LamsSharePointIntegration/LAMSAjaxServletRequester.ashx?&method=author&siteUrl=" + location.href, true);
        xmlHttp1.send(null); 
	    
	    
	    // Get the preview url
	    xmlHttp2 = getAjaxObject();
        xmlHttp2.onreadystatechange = function() 
        {
            if (xmlHttp2.readyState == 4) 
            { 
                previewUrl = xmlHttp2.responseText;
            }
        }
        xmlHttp2.open("GET", "/_layouts/LamsSharePointIntegration/LAMSAjaxServletRequester.ashx?&method=preview&siteUrl=" + location.href, true);
        xmlHttp2.send(null);
        
	    // Get the learning design repositroy javascript array string
        xmlHttp3 = getAjaxObject();
        xmlHttp3.onreadystatechange = function() 
        {
            if (xmlHttp3.readyState == 4) 
            { // readyState, see below
                learningDesignRepositoryStr = eval(xmlHttp3.responseText);
            }
        }
        xmlHttp3.open("GET", "/_layouts/LamsSharePointIntegration/LAMSAjaxServletRequester.ashx?&method=learningDesignRepository&siteUrl=" + location.href, false);
        xmlHttp3.send(null);
	    
 	    var authorWin = null;
	    var previewWin = null;
	    var sequenceInput = null;
	    
	    
 	    
 	    var TREE_TPL = {
	    'target'  : '_self',	// name of the frame links will be opened in
							    // other possible values are: _blank, _parent, _search, _self and _top

	    'icon_e'  : '/_layouts/images/LamsSharePointIntegration/treeicons/empty.gif', // empty image
	    'icon_l'  : '/_layouts/images/LamsSharePointIntegration/treeicons/line.gif',  // vertical line

        'icon_32' : '/_layouts/images/LamsSharePointIntegration/treeicons/base.gif',   // root leaf icon normal
        'icon_36' : '/_layouts/images/LamsSharePointIntegration/treeicons/base.gif',   // root leaf icon selected
    	
	    'icon_48' : '/_layouts/images/LamsSharePointIntegration/treeicons/base.gif',   // root icon normal
	    'icon_52' : '/_layouts/images/LamsSharePointIntegration/treeicons/base.gif',   // root icon selected
	    'icon_56' : '/_layouts/images/LamsSharePointIntegration/treeicons/base.gif',   // root icon opened
	    'icon_60' : '/_layouts/images/LamsSharePointIntegration/treeicons/base.gif',   // root icon selected
    	
	    'icon_16' : '/_layouts/images/LamsSharePointIntegration/treeicons/folder.gif', // node icon normal
	    'icon_20' : '/_layouts/images/LamsSharePointIntegration/treeicons/folderopen.gif', // node icon selected
	    'icon_24' : '/_layouts/images/LamsSharePointIntegration/treeicons/folderopen.gif', // node icon opened
	    'icon_28' : '/_layouts/images/LamsSharePointIntegration/treeicons/folderopen.gif', // node icon selected opened

	    'icon_0'  : '/_layouts/images/LamsSharePointIntegration/treeicons/page.gif', // leaf icon normal
	    'icon_4'  : '/_layouts/images/LamsSharePointIntegration/treeicons/page.gif', // leaf icon selected
    	
	    'icon_2'  : '/_layouts/images/LamsSharePointIntegration/treeicons/joinbottom.gif', // junction for leaf
	    'icon_3'  : '/_layouts/images/LamsSharePointIntegration/treeicons/join.gif',       // junction for last leaf
	    'icon_18' : '/_layouts/images/LamsSharePointIntegration/treeicons/plusbottom.gif', // junction for closed node
	    'icon_19' : '/_layouts/images/LamsSharePointIntegration/treeicons/plus.gif',       // junctioin for last closed node
	    'icon_26' : '/_layouts/images/LamsSharePointIntegration/treeicons/minusbottom.gif',// junction for opened node
	    'icon_27' : '/_layouts/images/LamsSharePointIntegration/treeicons/minus.gif'       // junctioin for last opended node
        };
        
        // returns the correct ajax object for the browser
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
        
     
        
        
        function initPage()
        {
            
            var pageInputs = document.getElementsByTagName("input");
	        for (var i=0; i<pageInputs.length; i++)
            {  
	            if (pageInputs[i].title == "Sequence ID")
	            {
	                if (pageInputs[i].value != "")
	                {
	                    document.getElementById("step1").style.display="none";
	                    document.getElementById("step2").style.display="block";
	                    sequenceInput = pageInputs[i];
	                    document.getElementById("sequence_id").value = pageInputs[i].value;
	                    break;
	                }
	            }
	        }
        }
        
        function nextStep()
        {
	        sequenceObj = document.getElementById("sequence_id");
	        if(sequenceObj.value  == ""){
	            alert('You have to select a sequence to proceed.');
	            return false;
	        }
	        document.getElementById("step1").style.display="none";
	        document.getElementById("step2").style.display="block";
	    }

	    function openAuthor()
        {
    	    var tempAuthorUrl = authorUrl + "&notifyCloseURL=";
    	    if(authorWin && authorWin.open && !authorWin.closed){
    		    try {
            	    authorWin.focus();
                }catch(e){
		            // popups blocked by a 3rd party
		   	    }
            }
            else{
                try {
            	    authorWin = window.open(tempAuthorUrl,'aWindow','width=800,height=600,resizable');
            	    authorWin.focus();
                }catch(e){
		            // popups blocked by a 3rd party
		        }
            }
        }
        
        function openPreview()
        {
            sequenceObj = document.getElementById("sequence_id");
	        var tempPreviewUrl = previewUrl;
	        if(sequenceObj.value == ""){
	            alert('Please select a sequence to preview.');
	            return false;
	        }
	        else
	        {
	            tempPreviewUrl += "&ldId=" + sequenceObj.value; 
	            if(previewWin && previewWin.open && !previewWin.closed)
	            {
    		        try {
            	        previewWin.focus();
                    }catch(e){
		                // popups blocked by a 3rd party
		   	        }
                }
                else{
                    try {
            	        previewWin = window.open(tempPreviewUrl, 'aWindow','width=800,height=600,resizable');
            	        previewWin.focus();
                    }catch(e){
		                // popups blocked by a 3rd party
		            }
                }
	        }
    	    
        }
	    
	    function prevStep()
	    {
			document.getElementById("step1").style.display="block";
			document.getElementById("step2").style.display="none";
	    }
	    
	    function selectSequence(obj)
	    {
		    inputs = document.getElementsByTagName("input");
		    document.getElementById("sequence_id").value = obj;
		    
		    for (var x=0; x<inputs.length; x++)
		    {
		        if (inputs[x].title == "Sequence ID")
		        {
		            //inputs[x].type="hidden";            
		            inputs[x].value = obj;
		            inputs[x].style.display = 'none';
	                //inputs[x].disabled = true;
		        }
		    }
	    }

	    function trim(str){
		    return str.replace(/^\s+|\s+$/, '');
	    }
	    
	    function refreshWorkspace()
	    {
	        inputs = document.getElementsByTagName("input");
		    for (var x=0; x<inputs.length; x++)
		    {
		        if (inputs[x].title == "Sequence ID")
		        {
		            inputs[x].value = "";
		            break;
		        }
		    }
		    document.location.reload(); 
	    }
	        	
    //-->
    </script>
    
    <input type="hidden" id="sequence_id" name="sequence_id" value=""  title="sequence_id_hidden"/>
    
    <div id="step1" align="left">
    
    <table cellpadding="2" cellspacing="0" id="Tablez">
       
        <tr><td>
            <table class="ms-formtable" style="margin-top: 8px;" border=0 cellpadding=0 cellspacing=0 width=250 >
                <tr> 
                    <td nowrap="true" valign="top" width="190px" class="ms-formlabel"><h3 class="ms-standardheader"><nobr>Select Sequence</nobr></h3></td>
                
                    <td valign="top" class="ms-formbody" width="400px">
                        <script language="JavaScript" type="text/javascript">
                           <!--            		
                                
                                if (learningDesignRepositoryStr != null && learningDesignRepositoryStr != "")
	                            {
	                                var tree = new tree(learningDesignRepositoryStr, TREE_TPL);
	                            }
	                            else
	                            {
	                                document.write("<font color=\"Red\">Problem accessing learning design repository, please contact your system administrator</Red>");
	                            }
	                            
                         //-->
                        </script>
                        
                        
                    </td>
                </tr>
            </table>
        </td></tr>

        <tr> <td align="left"><br />
        
        <input type="button" name="cancel" value="Cancel" id="btncancel" accesskey="C"  onclick="javascript: history.go(-1)"/>

        <input type="button" name="author" value="Open Author" onclick="javascript:openAuthor();" id="author"   target="_self" />
        
        <input type="button" name="refresh" value="Refresh Workspace" onclick="javascript:refreshWorkspace();" id="refresh"   target="_self" />
        
        <input type="button" name="preview" value="Preview Selected" onclick="javascript:openPreview();" id="preview"   target="_self" />
        
        <input type="button" name="Next" value="Next" onclick="javascript:nextStep();" id="Button1"  target="_self" />
        </td></tr>
    </table>

        
    </div>
    
    <!--<div id="step2" style="display:none" align="center"><table cellpadding="5">-->
    <div id="step2" style="display:none" align="left">
    <table cellpadding=0 cellspacing=0 id="onetIDListForm">
        <tr>
            <td>
            <WebPartPages:WebPartZone runat="server" FrameType="None" ID="Main" Title="loc:Main" />
            <img src="/_layouts/images/blank.gif" width=590 height=1 alt="">
            </td>
        </tr>
    </table>
    </div>
    
    <br />
    
    <script language="JavaScript" type="text/javascript">
        <!--
            initPage();
        //-->
    </script>
    <asp:Label ID="lbldebug" Text="" runat="server" />
    
</asp:Content>

<asp:Content ID="Content6" ContentPlaceHolderId="PlaceHolderTitleLeftBorder" runat="server">
<table cellpadding=0 height=100% width=100% cellspacing=0>
 <tr><td class="ms-areaseparatorleft"><IMG SRC="/_layouts/images/blank.gif" width=1 height=1 alt=""></td></tr>
</table>
</asp:Content>

<asp:Content ID="Content7" ContentPlaceHolderId="PlaceHolderTitleAreaClass" runat="server">
<script id="onetidPageTitleAreaFrameScript">
	document.getElementById("onetidPageTitleAreaFrame").className="ms-areaseparator";
</script>
</asp:Content>

<asp:Content ID="Content8" ContentPlaceHolderId="PlaceHolderBodyAreaClass" runat="server">
<style type="text/css">
.ms-bodyareaframe {
	padding: 8px;
	border: none;
}
</style>
</asp:Content>

<asp:Content ID="Content9" ContentPlaceHolderId="PlaceHolderBodyLeftBorder" runat="server">
<div class='ms-areaseparatorleft'><IMG SRC="/_layouts/images/blank.gif" width=8 height=100% alt=""></div>
</asp:Content>

<asp:Content ID="Content10" ContentPlaceHolderId="PlaceHolderTitleRightMargin" runat="server">
<div class='ms-areaseparatorright'><IMG SRC="/_layouts/images/blank.gif" width=8 height=100% alt=""></div>
</asp:Content>

<asp:Content ID="Content11" ContentPlaceHolderId="PlaceHolderBodyRightMargin" runat="server">
<div class='ms-areaseparatorright'><IMG SRC="/_layouts/images/blank.gif" width=8 height=100% alt=""></div>
</asp:Content>

<asp:Content ID="Content12" ContentPlaceHolderId="PlaceHolderTitleAreaSeparator" runat="server"/>
