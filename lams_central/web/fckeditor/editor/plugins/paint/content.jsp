<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<lams:html>
	<lams:head>
			<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
			<meta content="noindex, nofollow" name="robots">
			
			<script language="javascript" src="swfobject.js"></script>
			<script language="javascript">
				var oEditor = window.parent.InnerDialogLoaded() ;
				var Lang = oEditor.FCKLang.Paint ;
				var PaintCommand = oEditor.PaintCommand ;
				var dataSent = false;
				window.onload = function (){
					oEditor.FCKLanguageManager.TranslatePage(document);
					window.parent.SetOkButton(true);
				}
				function Ok(){
					if(!dataSent){
						document.getElementById('flashContent').sendData("<lams:LAMSURL/>/fckeditor/editor/filemanager/browser/default/connectors/jsp/connector?Command=PaintUpload&Type=/Image&DesignFolder=/" + oEditor.FCKConfig.ContentFolderID + "&CurrentFolder=");	// Target URL
						dataSent = true;
					}
					return false;
				}
				function imageReceived(source){
					PaintCommand.Add(source);
					dataSent = false;
					parent.Cancel();
				}
				function imageFailed(){
					dataSent = false;
				}
				var xmlDic="<xml><language>";
				for(var prop in Lang.Flash){
					xmlDic += "<entry key='local."+prop+"'><name>"+Lang.Flash[prop]+"</name></entry>";
				}
				xmlDic += "</language></xml>";
				var flashvars = {
					dictionary: xmlDic,
					startingImageURL: PaintCommand.startingImage,
					userId: "4",										// User ID
					contentFolder: oEditor.FCKConfig.ContentFolderID	// Content Folder Id
				}
				PaintCommand.startingImage = "";
				swfobject.embedSWF("FCK Paint.swf", "flashContent", "800", "500", "9.0.0", null, flashvars);
			</script>
	</lams:head>

	<body scroll="no" style="OVERFLOW: hidden">
		<div id="flashContent"/>
	</body>

</lams:html>
