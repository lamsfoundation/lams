<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css style="tabbed" />
		<script type="text/javascript"> 
			function callHideMessage() {
				if (window.parent && window.parent.hideMessage) {
					window.parent.hideMessage();
				} else if (window.parent && window.parent.hideMessage) {
					window.top.hideMessage();
				}
			}
		</script>
	</lams:head>
	<body class="tabpart">
		<!-- Basic Info Form-->
		<%@ include file="/common/messages.jsp"%>

		<html:form action="/monitoring/saveMark" method="post" styleId="markForm" focus="marks">
			<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
			<c:set var="sessionMapID" value="${formBean.sessionMapID}" />				
			<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
			<html:hidden property="sessionMapID" />
			<html:hidden property="userUid" />
			
			<h2 class="no-space-left">
				<fmt:message key="label.monitoring.summary.marking.marking" />
			</h2>

			<div class="field-name">
            	<fmt:message key="label.monitoring.summary.marking.marks" />
			</div>

			<div class="small-space-bottom">
         		<html:text property="marks" size="55"/>
			</div>

			<div class="field-name">
            	<fmt:message key="label.monitoring.summary.marking.comments" />
			</div>

			<div class="small-space-bottom" >
				<lams:CKEditor id="comments" value="${formBean.comments}" contentFolderID="${sessionMap.contentFolderID}" />
			</div>
			
		</html:form>

		<lams:ImgButtonWrapper>
			<a href="#" onclick="markForm.submit();" class="button space-left">
				<fmt:message key="label.monitoring.summary.marking.update.marks" /> 
			</a>
			<a href="javascript:;" onclick="javascript:callHideMessage" class="button space-left">
				<fmt:message key="label.cancel" /> 
			</a>
		</lams:ImgButtonWrapper>
	</body>
</lams:html>
