<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	
	<lams:css/>
	<style type="text/css">
		a {
			margin: 10px 2px 0px 0px;
			float: right;
		}
		
		input.item {
			margin: 5px 0px 5px 10px;
			float: none;
		}
		
		body {
			width: ${param.plannerCKEditorLongWidth};
		}
		
		table#itemTable td {
			margin: 0px;
			padding: 5px 0px 5px 0px;
			border-bottom: thin inset;
		}
		
		img.clearEntry {
			maring: 0px;
			padding: 0px;
			cursor: pointer;
		}
	</style>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
  	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
  	<script type="text/javascript">
  		var url = "<c:url value='/authoring/createPedagogicalPlannerItem.do' />";
  		function createItem(itemType){
  			var currentUrl = url + "?addType=" + itemType;
  			$('#pedagogicalPlannerForm').ajaxSubmit({
  				url: currentUrl,
  				beforeSubmit: function(formData, jqForm, options){
  					for (elementIndex = 0;elementIndex<formData.length;elementIndex++){
  						var elementName = formData[elementIndex].name;
  						if (elementName.indexOf("file[")==0 && formData[elementIndex].value==""){
  							var openBracketIndex = elementName.indexOf("[")+1;
  							formData[elementIndex].name="fileDummy["+elementName.substr(openBracketIndex,elementName.length-openBracketIndex-1)+"]";
  						}
  					}
 				},
  				success: function(responseText){
 	  				var bodyTag = '<body';
 	  				var selectedBody = responseText.substring(responseText.search(bodyTag) + 1);
 	  				selectedBody = selectedBody.substring(selectedBody.search(bodyTag));
  					$('#body').html(selectedBody);
  				}
  			});
  		}
  		
	  	function prepareFormData(){
			//CKeditor content is not submitted when sending by jQuery; we need to do this
			var content = CKEDITOR.instances.instructions.getData();
			document.getElementById("instructions").value=content;
		}
		
		function clearEntry(entryIndex){
			document.getElementById("title"+entryIndex).value="";
			document.getElementById("entry"+entryIndex).value="";
		}
  	</script>
</lams:head>
<body id="body">
	<%@ include file="/common/messages.jsp"%>
	<html:form enctype="multipart/form-data" action="/authoring/saveOrUpdatePedagogicalPlannerForm.do" styleId="pedagogicalPlannerForm" method="post">
		<html:hidden property="toolContentID" styleId="toolContentID" />
		<html:hidden property="valid" styleId="valid" />
		<html:hidden property="callID" styleId="callID" />
		<html:hidden property="activityOrderNumber" styleId="activityOrderNumber" />
		
		<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
		<h4 class="space-left"><fmt:message key="label.authoring.basic.resource.instructions"/></h4>
		<lams:CKEditor id="instructions"
			value="${formBean.instructions}"
			contentFolderID="${formBean.contentFolderID}"
               toolbarSet="CustomPedplanner" height="150px"
               width="${param.plannerCKEditorLongWidth}" displayExpanded="false">
		</lams:CKEditor>
		
		<c:if test="${formBean.itemCount ne 0}">
			<h4 class="space-left space-top space-bottom"><fmt:message key="label.authoring.basic.resource.list.title"/></h4>
			<table id="itemTable" cellpadding="0" cellspacing="0">
				<c:forEach var="itemIndex"  begin="1" end="${formBean.itemCount}">
					<tr>
						<td>
							<c:set var="itemType" value="${formBean.typeList[itemIndex-1]}" />
							<c:set var="itemFileName" value="${formBean.fileNameList[itemIndex-1]}" />
							<html:hidden property="type[${itemIndex-1}]" />
							<html:hidden property="fileUuid[${itemIndex-1}]" />
							<html:hidden property="fileVersion[${itemIndex-1}]" />
							<html:hidden property="fileName[${itemIndex-1}]"></html:hidden>
							<h4 class="space-left" id="title${status.index}"><fmt:message key="label.authoring.basic.title"/></h4>
							<html:text styleId="title${itemIndex-1}" styleClass="item" size="90" property="title[${itemIndex-1}]"></html:text>
							<c:choose>
								<c:when test="${itemType eq 1}">
									<h4 class="space-left"><fmt:message key="label.authoring.basic.resource.url"/></h4>
									<html:text styleId="entry${itemIndex-1}" styleClass="item" size="90" property="url[${itemIndex-1}]"></html:text>
								</c:when>
								<c:when test="${itemType eq 2}">
									<html:hidden property="url[${itemIndex-1}]" />
									<h4 class="space-left"><fmt:message key="label.authoring.basic.resource.file"/>
										<c:if test="${not empty itemFileName}">: ${itemFileName}</c:if>
									</h4>
									<html:file styleId="entry${itemIndex-1}" size="78" styleClass="item" property="file[${itemIndex-1}]" />
								</c:when>
							</c:choose>
						</td>
						<td>
							<img class="clearEntry" src="<lams:LAMSURL/>images/icons/cross.png"
								title="<fmt:message key="label.authoring.basic.resource.delete" />"
								onclick="javascript:clearEntry(${itemIndex-1})" />
						</td>
					</tr>
				</c:forEach>
			</table>
		</c:if>
	</html:form>
	<a class="button" href="javascript:createItem(2);"><fmt:message key="label.authoring.basic.add.file" /></a>
	<a class="button" href="javascript:createItem(1);"><fmt:message key="label.authoring.basic.add.url" /></a>
</body>
</lams:html>