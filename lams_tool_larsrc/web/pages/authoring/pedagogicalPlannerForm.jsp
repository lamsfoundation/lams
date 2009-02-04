<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	
	
	<lams:css style="core" />
	<style type="text/css">
		a {
			margin: 10px 2px 0px 0px;
			float: right;
		}
		
		input.item {
			margin: 5px 0px 5px 10px;
			float: none;
		}
	</style>
	
	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-latest.pack.js"></script>
  	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
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
  					$('#body').html(responseText.substring(responseText.search(/<body/i)));
  				}
  			});
  		}
  	</script>
</lams:head>
<body id="body">
	<%@ include file="/common/messages.jsp"%>
	<html:form style="width: 555px" enctype="multipart/form-data" action="/authoring/saveOrUpdatePedagogicalPlannerForm.do" styleId="pedagogicalPlannerForm" method="post">
		<html:hidden property="toolContentID" />
		<html:hidden property="valid" styleId="valid" />
		<html:hidden property="callID" styleId="callID" />
		<html:hidden property="activityOrderNumber" styleId="activityOrderNumber" />
		<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
		<c:forEach var="itemIndex"  begin="1" end="${formBean.itemCount}" >
			<c:set var="itemType" value="${formBean.typeList[itemIndex-1]}" />
			<c:set var="itemFileName" value="${formBean.fileNameList[itemIndex-1]}" />
			<html:hidden property="type[${itemIndex-1}]" />
			<html:hidden property="fileUuid[${itemIndex-1}]" />
			<html:hidden property="fileVersion[${itemIndex-1}]" />
			<html:hidden property="fileName[${itemIndex-1}]"></html:hidden>
			<h4 class="space-left"><fmt:message key="label.authoring.basic.title"/></h4>
			<html:text styleClass="item" size="80" property="title[${itemIndex-1}]"></html:text>
			<c:choose>
				<c:when test="${itemType eq 1}">
					<h4 class="space-left"><fmt:message key="label.authoring.basic.resource.url"/></h4>
					<html:text styleClass="item" size="80" property="url[${itemIndex-1}]"></html:text>
				</c:when>
				<c:when test="${itemType eq 2}">
					<html:hidden property="url[${itemIndex-1}]" />
					<h4 class="space-left"><fmt:message key="label.authoring.basic.resource.file"/>
						<c:if test="${not empty itemFileName}">: ${itemFileName}</c:if>
					</h4>
					<html:file size="68" styleClass="item" property="file[${itemIndex-1}]" />
				</c:when>
			</c:choose>
			<hr style="margin-left: auto; margin-right: auto; margin-top: 5px; margin-bottom: 5px; width: 450px" />
		</c:forEach>
	</html:form>
	<a class="button" href="javascript:createItem(2);"><fmt:message key="label.authoring.basic.add.file" /></a>
	<a class="button" href="javascript:createItem(1);"><fmt:message key="label.authoring.basic.add.url" /></a>
</body>
</lams:html>