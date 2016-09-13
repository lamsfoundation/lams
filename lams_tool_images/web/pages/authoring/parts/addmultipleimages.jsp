<%@ include file="/common/taglibs.jsp"%>
	
<div class="panel panel-default">
	<div class="panel-heading">
		<div class="panel-title">
			<fmt:message key="label.authoring.basic.add.multiple.images" />
		</div>
	</div>
		
	<div class="panel-body">

		<%@ include file="/common/messages.jsp"%>
		<html:form action="/authoring/saveMultipleImages" method="post" styleId="imageGalleryItemsForm" enctype="multipart/form-data">
			<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />	
			<html:hidden property="sessionMapID" styleId="sessionMapID"/>
	
			<div class="form-group">
				<label>
					<fmt:message key="label.authoring.basic.resource.files.input"/>
				</label>
					
				<div>
					<input type="file" name="file1" id="file1"/>
				</div>
				<div class="voffset5">
					<input type="file" name="file2" id="file2"/>
				</div>
				<div class="voffset5">
					<input type="file" name="file3" id="file3"/>
				</div>
				<div class="voffset5">
					<input type="file" name="file4" id="file4"/>
				</div>
				<div class="voffset5">
					<input type="file" name="file5" id="file5"/>
				</div>
			</div>
	
			<a href="javascript:showMessage('<html:rewrite page="/authoring/newImageInit.do?sessionMapID=${formBean.sessionMapID}"/>');" 
					class="btn btn-default btn-xs">
				<fmt:message key="label.authoring.basic.upload.single.image" />
			</a>			
	
		</html:form>
			
		<div class="voffset10 pull-right">
		    <a href="#nogo" onclick="javascript:cancelImageGalleryItem()" class="btn btn-sm btn-default loffset5">
				<fmt:message key="label.cancel" /> 
			</a>
			<a href="#nogo" onclick="javascript:submitMultipleImageGalleryItems()" class="btn btn-sm btn-default button-add-item">
				<fmt:message key="label.authoring.basic.add.images" /> 
			</a>
		</div>
		
	</div>
</div>
