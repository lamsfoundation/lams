<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="fck-editor" prefix="fck"%>

<%@ attribute name="id" required="true" rtexprvalue="true"%>
<%@ attribute name="value" required="true" rtexprvalue="true"%>
<%@ attribute name="toolbarSet" required="false" rtexprvalue="true"%>
<%@ attribute name="contentFolderID" required="false" rtexprvalue="true"%>

<c:set var="language">
	<lams:user property="localeLanguage" />
</c:set>

<c:if test="${empty toolbarSet}">
	<c:set var="toolbarSet" value="Default" />
</c:if>

<c:set var="basePath"><lams:LAMSURL/>/fckeditor/</c:set>

<!-- 
<script type="text/javascript">
	var oFCKeditor=new FCKeditor('${id}');
	oFCKeditor.BasePath= "${basePath}";
	oFCKeditor.ToolbarSet= "${toolbarSet}";
	oFCKeditor.Value= '${value}';

	oFCKeditor.Config["ImageBrowserURL"]= "${basePath}editor/filemanager/browser/default/browser.html?Type=Image&amp;Connector=connectors/jsp/connector&amp;CurrentFolder=/${contentFolderID}/";
	oFCKeditor.Config["LinkBrowserURL"]=  "${basePath}editor/filemanager/browser/default/browser.html?Connector=connectors/jsp/connector&amp;CurrentFolder=/${contentFolderID}/";
	oFCKeditor.Config["FlashBrowserURL"]= "${basePath}editor/filemanager/browser/default/browser.html?Type=Flash&amp;Connector=connectors/jsp/connector&amp;CurrentFolder=/${contentFolderID}/";
	oFCKeditor.Config["ImageUploadURL"]=  "${basePath}editor/filemanager/upload/simpleuploader?Type=Image&CurrentFolder=/${contentFolderID}/";
	oFCKeditor.Config["LinkUploadURL"]=   "${basePath}editor/filemanager/upload/simpleuploader?Type=File&CurrentFolder=/${contentFolderID}/";
	oFCKeditor.Config["FlashUploadURL"]=  "${basePath}editor/filemanager/upload/simpleuploader?Type=Flash&CurrentFolder=/${contentFolderID}/";

	oFCKeditor.Config["AutoDetectLanguage"]= false ;
	oFCKeditor.Config["DefaultLanguage"]= "${language}" ;

	oFCKeditor.Create();
</script>
-->

<fck:editor id="${id}"
	basePath="${basePath}"
	toolbarSet="${toolbarSet}"
	imageBrowserURL="${basePath}editor/filemanager/browser/default/browser.html?Type=Image&Connector=connectors/jsp/connector&CurrentFolder=/${contentFolderID}/"
	imageUploadURL="${basePath}editor/filemanager/upload/simpleuploader?Type=Image&CurrentFolder=/${contentFolderID}/"
	linkBrowserURL="${basePath}editor/filemanager/browser/default/browser.html?Connector=connectors/jsp/connector&CurrentFolder=/${contentFolderID}/"
	linkUploadURL="${basePath}editor/filemanager/upload/simpleuploader?Type=File&CurrentFolder=/${contentFolderID}/"
	flashBrowserURL="${basePath}editor/filemanager/browser/default/browser.html?Type=Flash&Connector=connectors/jsp/connector&CurrentFolder=/${contentFolderID}/"
	flashUploadURL="${basePath}editor/filemanager/upload/simpleuploader?Type=Flash&CurrentFolder=/${contentFolderID}/"
	autoDetectLanguage="false"
	defaultLanguage="${language}">
		${value}
</fck:editor>




