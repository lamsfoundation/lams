<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %>
<c:set var="UPLOAD_FILE_MAX_SIZE_AS_USER_STRING"><%=FileValidatorUtil.formatSize(Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE))%></c:set>

<html:form action="/authoring/saveMultipleImages" method="post" styleId="imageGalleryItemsForm" enctype="multipart/form-data">
	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="panel-title">
				<fmt:message key="label.authoring.basic.add.multiple.images" />
				<a href="javascript:showMessage('<html:rewrite page="/authoring/newImageInit.do?sessionMapID=${formBean.sessionMapID}"/>');" 
						class="btn btn-default btn-xs pull-right">
					<fmt:message key="label.authoring.basic.upload.single.image" />
				</a>
			</div>		
		</div>
			
		<div class="panel-body">
	
			<%@ include file="/common/messages.jsp"%>	
			<html:hidden property="sessionMapID" styleId="sessionMapID"/>
	
			<div class="form-group">
				<label>
					<fmt:message key="label.authoring.basic.resource.files.input"/>
				</label>

				<div class="help-block">
					<fmt:message key="label.upload.info">
					<fmt:param>${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}</fmt:param>
					</fmt:message>
				</div>
				
				<lams:FileUpload fileButtonBrowse="fileButtonBrowse1" fileFieldname="file1" errorMsgDiv="fileerror1" uploadInfoMessageKey="-"
					fileInputNameFieldname="fileInputName1" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"/>
	
				<div class="voffset5"></div>
				<lams:FileUpload fileButtonBrowse="fileButtonBrowse2" fileFieldname="file2" errorMsgDiv="fileerror2" uploadInfoMessageKey="-"
					fileInputNameFieldname="fileInputName2" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"/>

				<div class="voffset5"></div>
				<lams:FileUpload fileButtonBrowse="fileButtonBrowse3" fileFieldname="file3" errorMsgDiv="fileerror3" uploadInfoMessageKey="-"
					fileInputNameFieldname="fileInputName3" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"/>

				<div class="voffset5"></div>
				<lams:FileUpload fileButtonBrowse="fileButtonBrowse4" fileFieldname="file4" errorMsgDiv="fileerror4" uploadInfoMessageKey="-"
					fileInputNameFieldname="fileInputName4" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"/>

				<div class="voffset5"></div>
				<lams:FileUpload fileButtonBrowse="fileButtonBrowse5" fileFieldname="file5" errorMsgDiv="fileerror5" uploadInfoMessageKey="-"
					fileInputNameFieldname="fileInputName5" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"/>
									
			</div>
			
			<div class="panel-body text-center" style="display:none" id="itemAttachmentArea_Busy">
				<i class="fa fa-refresh fa-spin fa-2x fa-fw text-primary"></i>
			</div>
				
			<div id="uploadButtons" class="voffset10 pull-right">
			    <a href="#nogo" onclick="javascript:cancelImageGalleryItem()" class="btn btn-sm btn-default loffset5">
					<fmt:message key="label.cancel" /> 
				</a>
				<a href="#nogo" onclick="javascript:submitMultipleImageGalleryItems()" class="btn btn-sm btn-default button-add-item">
					<fmt:message key="label.authoring.basic.add.images" /> 
				</a>
			</div>
			
		</div>
	</div>
	
</html:form>
