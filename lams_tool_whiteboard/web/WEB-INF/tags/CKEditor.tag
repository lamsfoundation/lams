<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-function" prefix="fn" %>

<%@ attribute name="id" required="true" rtexprvalue="true"%>
<%@ attribute name="placeholder" required="false" rtexprvalue="true"%>
<%@ attribute name="value" required="true" rtexprvalue="true"%>
<%@ attribute name="toolbarSet" required="false" rtexprvalue="true"%>
<%@ attribute name="classes" required="false" rtexprvalue="true"%>
<%@ attribute name="height" required="false" rtexprvalue="true"%>
<%@ attribute name="width" required="false" rtexprvalue="true"%>
<%@ attribute name="contentFolderID" required="false" rtexprvalue="true"%>
<%@ attribute name="displayExpanded" required="false" rtexprvalue="true"%>
<%@ attribute name="resizeParentFrameName" required="false" rtexprvalue="true"%>
<%@ attribute name="method" required="false" rtexprvalue="true"%>
<%@ attribute name="maxWords" required="false" rtexprvalue="true"%>

<c:if test="${empty method}">
	<c:set var="method" value="inline" />
</c:if>

<c:set var="language">
	<lams:user property="fckLanguageMapping" />
</c:set>

<c:if test="${empty toolbarSet}">
	<c:set var="toolbarSet" value="Default" />
</c:if>

<c:if test="${fn:containsIgnoreCase(method,'Inline')}">
	<c:set var="toolbarSet">${toolbarSet}Inline</c:set>
</c:if>

<c:if test="${empty displayExpanded}">
	<c:set var="displayExpanded" value="true" />
</c:if>

<c:if test="${empty width}">
	<c:set var="width" value="100%" />
</c:if>

<c:set var="fixedValue" value="${fn:replace(value,'&lt','&amp;lt')}"/>
<c:set var="fixedValue" value="${fn:replace(fixedValue,'&gt','&amp;gt')}"/>

<c:if test="${not empty placeholder}">
	<c:set var="classes" value="placeholder-shown ${classes}" />
	<div class="cke-div">
</c:if>

<textarea id="${id}" name="${id}" style="display: none; visibility: hidden; height: 0px;">${fixedValue}</textarea>

<c:if test="${not empty placeholder}">
		<label for="${id}" class="cke-label-floating">${placeholder}</label>
	</div>
</c:if>

<c:if test="${empty ckEditorBasePath}">
	<c:set scope="request" var="ckEditorBasePath">/lams/ckeditor/</c:set>
	<script type="text/javascript" src="${ckEditorBasePath}ckeditor.js"></script>
</c:if>

<script type="text/javascript">
	function initializeCKEditor(id, method, resizeParentFrameName, width, height, toolbarSet, classes, language, displayExpanded, contentFolderID, maxWords){
		
		if (resizeParentFrameName != ""){
			CKEDITOR.on('instanceReady', function(e){
				if (e.editor.element.$.id == id){
					var messageAreaFrame = window.parent.document.getElementById(resizeParentFrameName);
					if (messageAreaFrame != null){
						messageAreaFrame.style.height=messageAreaFrame.contentWindow.document.body.scrollHeight+'px';
					}
				}
			});
		}
		
		CKEDITOR.basePath = "${ckEditorBasePath}";
	    
	    var editor = CKEDITOR.instances[id];
	    if (editor) { editor.destroy(true); }
	    
		var configuration = {
				width                         : width,
				height                        : height,
				toolbar                       : toolbarSet,
				classes                       : classes,
				language                      : language,
				defaultLangugage              : "en",
				toolbarStartupExpanded        : displayExpanded,
				filebrowserBrowseUrl          : "${ckEditorBasePath}filemanager/browser/default/browser.html?Type=File&Connector=connectors/jsp/connector&CurrentFolder=/" + contentFolderID + "/",
				filebrowserUploadUrl          : "${ckEditorBasePath}filemanager/upload/simpleuploader?Type=File&CurrentFolder=/" + contentFolderID + "/",
				filebrowserImageBrowseUrl     : "${ckEditorBasePath}filemanager/browser/default/browser.html?Type=Image&Connector=connectors/jsp/connector&CurrentFolder=/" + contentFolderID + "/",
				filebrowserImageUploadUrl     : "${ckEditorBasePath}filemanager/upload/simpleuploader?Type=Image&CurrentFolder=/" + contentFolderID + "/",
				filebrowserImageBrowseLinkUrl : "${ckEditorBasePath}filemanager/browser/default/browser.html?Connector=connectors/jsp/connector&CurrentFolder=/" + contentFolderID + "/",
				filebrowserFlashBrowseUrl     : "${ckEditorBasePath}filemanager/browser/default/browser.html?Type=Flash&Connector=connectors/jsp/connector&CurrentFolder=/" + contentFolderID + "/",
				filebrowserFlashUploadUrl     : "${ckEditorBasePath}filemanager/upload/simpleuploader?Type=Flash&CurrentFolder=/" + contentFolderID + "/",
				contentFolderID				  : contentFolderID
		};
		
		if (maxWords > 0) {
			$.extend(configuration, {
				wordcount					  : {
					maxWordCount			  : maxWords,
					showParagraphs			  : false,
					showRemaining			  : true,
					pasteWarningDuration      : 5000
				}
			});
		}

		

		instance = CKEDITOR[method](id, configuration);
		instance.initializeFunction = function(){
			 initializeCKEditor(id, method, resizeParentFrameName, width, height, toolbarSet, classes, language, displayExpanded, contentFolderID);
		};
		return instance;
	}
	
	// run initialisation code
	initializeCKEditor("${id}", "${method}", "${resizeParentFrameName}", "${width}", "${height}", "${toolbarSet}", "${classes}", "${language}", ${displayExpanded}, "${contentFolderID}", ${empty maxWords or maxWords <= 0 ? 0 : maxWords});
	

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
