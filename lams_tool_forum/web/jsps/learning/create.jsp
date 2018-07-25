<%@ include file="/common/taglibs.jsp"%>

<script>
	function doSubmit() {
		disableSubmitButton(); 
		if (validateForm()) {
			showBusy("itemAttachmentArea");
			return true;
		}
		enableSubmitButton();
		return false;
	}
</script>

<html:form action="/learning/createTopic.do" onsubmit="return doSubmit();" 
		method="post" focus="message.subject" enctype="multipart/form-data">
		
	<html:hidden property="sessionMapID" />
	<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
	<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

	<lams:Page type="learner" title="${sessionMap.title}">

		<div class="container-fluid">
		<div class="panel panel-default">
			<div class="panel-heading panel-title">
				<fmt:message key="title.message.edit" />
			</div>
			<div class="panel-body">
				<html:errors property="error" />
 				<%@ include file="/jsps/learning/message/topicform.jsp"%>
			</div>
		</div>
		</div>		
	</lams:Page>

</html:form>

