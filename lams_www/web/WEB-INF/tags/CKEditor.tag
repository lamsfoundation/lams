<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<%@ attribute name="id" required="true" rtexprvalue="true"%>
<%@ attribute name="value" required="true" rtexprvalue="true"%>
<%@ attribute name="toolbarSet" required="false" rtexprvalue="true"%>
<%@ attribute name="height" required="false" rtexprvalue="true"%>
<%@ attribute name="width" required="false" rtexprvalue="true"%>
<%@ attribute name="contentFolderID" required="false" rtexprvalue="true"%>
<%@ attribute name="displayExpanded" required="false" rtexprvalue="true"%>
<%@ attribute name="resizeParentFrameName" required="false" rtexprvalue="true"%>

<c:set var="language">
	<lams:user property="fckLanguageMapping" />
</c:set>

<c:if test="${empty toolbarSet}">
	<c:set var="toolbarSet" value="Default" />
</c:if>

<c:if test="${empty displayExpanded}">
	<c:set var="displayExpanded" value="true" />
</c:if>

<c:if test="${empty width}">
	<c:set var="width" value="100%" />
</c:if>

<c:if test="${empty height}">
	<c:set var="height" value="100px" />
</c:if>

<textarea id="${id}" name="${id}" style="display: none; visibility: hidden; height: 0px;">${value}</textarea>

<c:if test="${empty ckEditorBasePath}">
	<c:set scope="request" var="ckEditorBasePath"><lams:LAMSURL/>ckeditor/</c:set>
	<script type="text/javascript" src="${ckEditorBasePath}ckeditor.js"></script>
</c:if>

<script type="text/javascript">
	function initializeCKEditor(){
		
		if ("${resizeParentFrameName}" != ""){
			CKEDITOR.on('instanceReady', function(e){
				if (e.editor.element.$.id == "${id}"){
					var messageAreaFrame = window.parent.document.getElementById("${resizeParentFrameName}");
					if (messageAreaFrame != null){
						messageAreaFrame.style.height=messageAreaFrame.contentWindow.document.body.scrollHeight+'px';
					}
				}
			});
		}
		
		CKEDITOR.basePath = "${ckEditorBasePath}";
	    
		var instance = CKEDITOR.replace( "${id}", {
				width                         : "${width}",
				height                        : "${height}",
				toolbar                       : "${toolbarSet}",
				language                      : "${language}",
				defaultLangugage              : "en",
				toolbarStartupExpanded        : ${displayExpanded},
				filebrowserBrowseUrl          : "${ckEditorBasePath}filemanager/browser/default/browser.html?Type=File&Connector=connectors/jsp/connector&CurrentFolder=/${contentFolderID}/",
				filebrowserUploadUrl          : "${ckEditorBasePath}filemanager/upload/simpleuploader?Type=File&CurrentFolder=/${contentFolderID}/",
				filebrowserImageBrowseUrl     : "${ckEditorBasePath}filemanager/browser/default/browser.html?Type=Image&Connector=connectors/jsp/connector&CurrentFolder=/${contentFolderID}/",
				filebrowserImageUploadUrl     : "${ckEditorBasePath}filemanager/upload/simpleuploader?Type=Image&CurrentFolder=/${contentFolderID}/",
				filebrowserImageBrowseLinkUrl : "${ckEditorBasePath}filemanager/browser/default/browser.html?Connector=connectors/jsp/connector&CurrentFolder=/${contentFolderID}/",
				filebrowserFlashBrowseUrl     : "${ckEditorBasePath}filemanager/browser/default/browser.html?Type=Flash&Connector=connectors/jsp/connector&CurrentFolder=/${contentFolderID}/",
				filebrowserFlashUploadUrl     : "${ckEditorBasePath}filemanager/upload/simpleuploader?Type=Flash&CurrentFolder=/${contentFolderID}/",
				contentFolderID				  : "${contentFolderID}"
		});
		instance.initializeFunction = initializeCKEditor;
		return instance;
	}
	
	// run when page is loaded
	initializeCKEditor();

	function reinitializeCKEditorInstances(){
		for (var instanceId in CKEDITOR.instances){
			var instance = CKEDITOR.instances[instanceId];
			var initializeFunction = instance.initializeFunction;
			CKEDITOR.remove(instance);
			instance = initializeFunction();
			instance.initializeFunction = initializeFunction;
		}
	}
</script>
