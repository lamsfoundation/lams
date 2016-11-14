<%@ include file="/common/taglibs.jsp"%>
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

				<div class="input-group">
				    <span class="input-group-btn">
							<button id="fileButtonBrowse1" type="button" class="btn btn-sm btn-default">
							<i class="fa fa-upload"></i> <fmt:message key="label.authoring.basic.resource.file.input"/>
						</button>
					</span>
					<input type="file" id="file1" name="file1" style="display:none"> 
					<input type="text" id="fileInputName1" style="display:none" disabled="disabled" class="form-control input-sm">
				</div>
						
				<script type="text/javascript">
					document.getElementById('fileButtonBrowse1').addEventListener('click', function() {
						document.getElementById('file1').click();
					});
					
					document.getElementById('file1').addEventListener('change', function() {
						$('#fileInputName1').show();
						document.getElementById('fileInputName1').value = this.value.replace(/^.*\\/, "");
						
					});
				</script>    
		
				<div class="input-group voffset5">
				    <span class="input-group-btn">
							<button id="fileButtonBrowse2" type="button" class="btn btn-sm btn-default">
							<i class="fa fa-upload"></i> <fmt:message key="label.authoring.basic.resource.file.input"/>
						</button>
					</span>
					<input type="file" id="file2" name="file2" style="display:none"> 
					<input type="text" id="fileInputName2" style="display:none" disabled="disabled" class="form-control input-sm">
				</div>
						
				<script type="text/javascript">
					document.getElementById('fileButtonBrowse2').addEventListener('click', function() {
						document.getElementById('file2').click();
					});
					
					document.getElementById('file2').addEventListener('change', function() {
						$('#fileInputName2').show();
						document.getElementById('fileInputName2').value = this.value.replace(/^.*\\/, "");
						
					});
				</script>    
							
				<div class="input-group voffset5">
				    <span class="input-group-btn">
							<button id="fileButtonBrowse3" type="button" class="btn btn-sm btn-default">
							<i class="fa fa-upload"></i> <fmt:message key="label.authoring.basic.resource.file.input"/>
						</button>
					</span>
					<input type="file" id="file3" name="file3" style="display:none"> 
					<input type="text" id="fileInputName3" style="display:none" disabled="disabled" class="form-control input-sm">
				</div>
						
				<script type="text/javascript">
					document.getElementById('fileButtonBrowse3').addEventListener('click', function() {
						document.getElementById('file3').click();
					});
					
					document.getElementById('file3').addEventListener('change', function() {
						$('#fileInputName3').show();
						document.getElementById('fileInputName3').value = this.value.replace(/^.*\\/, "");
						
					});
				</script>    
				
				<div class="input-group voffset5">
				    <span class="input-group-btn">
							<button id="fileButtonBrowse4" type="button" class="btn btn-sm btn-default">
							<i class="fa fa-upload"></i> <fmt:message key="label.authoring.basic.resource.file.input"/>
						</button>
					</span>
					<input type="file" id="file4" name="file4" style="display:none"> 
					<input type="text" id="fileInputName4" style="display:none" disabled="disabled" class="form-control input-sm">
				</div>
						
				<script type="text/javascript">
					document.getElementById('fileButtonBrowse4').addEventListener('click', function() {
						document.getElementById('file4').click();
					});
					
					document.getElementById('file4').addEventListener('change', function() {
						$('#fileInputName4').show();
						document.getElementById('fileInputName4').value = this.value.replace(/^.*\\/, "");
						
					});
				</script>    
				
				<div class="input-group voffset5">
				    <span class="input-group-btn">
							<button id="fileButtonBrowse5" type="button" class="btn btn-sm btn-default">
							<i class="fa fa-upload"></i> <fmt:message key="label.authoring.basic.resource.file.input"/>
						</button>
					</span>
					<input type="file" id="file5" name="file5" style="display:none"> 
					<input type="text" id="fileInputName5" style="display:none" disabled="disabled" class="form-control input-sm">
				</div>
						
				<script type="text/javascript">
					document.getElementById('fileButtonBrowse5').addEventListener('click', function() {
						document.getElementById('file5').click();
					});
					
					document.getElementById('file5').addEventListener('change', function() {
						$('#fileInputName5').show();
						document.getElementById('fileInputName5').value = this.value.replace(/^.*\\/, "");
						
					});
				</script>
									
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
