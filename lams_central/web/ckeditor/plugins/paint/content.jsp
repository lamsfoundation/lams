<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<lams:html>
	<lams:head>
			<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
			<meta content="noindex, nofollow" name="robots">
			
			<script language="javascript" src="<lams:LAMSURL/>includes/javascript/swfobject.js"></script>
			<script language="javascript">
				
				function triggerOK(){
					if(!dataSent){
						document.getElementById('flashContent').sendData("<lams:LAMSURL/>ckeditor/filemanager/browser/default/connectors/jsp/connector?Command=PaintUpload&Type=/Image&DesignFolder=/" + CK.config.contentFolderID + "&CurrentFolder=");	// Target URL
						dataSent = true;
					}
					return false;
				}
				
				function imageReceived(source){
					var trimmedSource = source.replace(/^\s+|\s+$/g, '');
					CK.insertHtml('<img class="ckeditor_paint" src="' + trimmedSource + '" style="border: none;"/>');
					CK.focus();
					dataSent = false;
					thisDialog.getButton('cancel').click();
				}
				
				function imageFailed(){
					dataSent = false;
				}

				var CKGlobal = window.parent.CKEDITOR;
				var thisDialog = CKGlobal.dialog.getCurrent();
				var CK = thisDialog.getParentEditor();
				var dataSent = false;
				
				// remove our previously registered listeners and reregister a new one
				var okButton = thisDialog.getButton('ok');
				okButton._.events.click.listeners = [];
				okButton.on('click', triggerOK);
				
				var xmlDic="<xml><language>";
				for(var prop in CK.lang.paint.Flash){
					xmlDic += "<entry key='local." + prop + "'><name>" + CK.lang.paint.Flash[prop] + "</name></entry>";
				}
				xmlDic += "</language></xml>";
				var flashvars = {
					dictionary: xmlDic,
					startingImageURL: CK.paintStartingImage,
					userId: "4",								// User ID
					contentFolder: CK.config.contentFolderID	// Content Folder Id
				}
				CK.paintStartingImage = "";
				swfobject.embedSWF("CK Paint.swf", "flashContent", "800", "500", "9.0.0", null, flashvars);
			</script>
	</lams:head>

	<body scroll="no" style="OVERFLOW: hidden">
		<div id="flashContent"/>
	</body>

</lams:html>
