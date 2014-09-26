<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
		
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<title>
			<fmt:message key="label.monitoring.title" />
		</title>
		
		<%@ include file="/common/header.jsp"%>
	</lams:head>
	
	<body class="stripes">
	
		<div style="margin: 10% auto auto; width: 600px;">
		<div id="content" >
		
			<%@ include file="/common/messages.jsp"%>
			<html:form action="/monitoring/saveComment" method="post" styleId="imageCommentForm">
				<html:hidden property="sessionMapID" styleId="sessionMapID"/>
				<html:hidden property="commentUid" />
				<html:hidden property="createBy" />
				<html:hidden property="createDate" />
	
				<h2 class="no-space-left">
					<fmt:message key="label.monitoring.updatecomment.update.comment" />
				</h2>
				
				<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
				<c:set var="createBy"	value="${formBean.createBy}" />
				<c:set var="createDate"	value="${formBean.createDate}" />
				<div class="space-top" >
					<span class="field-name">
						<fmt:message key="label.monitoring.updatecomment.posted.by" />:
					</span>
					${createBy}					
				</div>					 
				
				<div class="space-top" >
					<span class="field-name">
						<fmt:message key="label.monitoring.updatecomment.posted.on" />:
					</span>
					${createDate}
				</div> 
	
				<div class="field-name space-top" >
					<fmt:message key="label.monitoring.updatecomment.comment" />
				</div>
				<lams:STRUTS-textarea property="comment" rows="3" cols="50" />
			</html:form>

			<br><br>
	
			<lams:ImgButtonWrapper>
				<a href="#" onclick="window.location.href = '<c:url value='/pages/monitoring/startimagesummary.jsp'/>?sessionMapID=${sessionMapID}'" class="button right-buttons space-left">
					<fmt:message key="label.cancel" /> 
				</a>
				<a href="#" onclick="document.imageCommentForm.submit();" class="button right-buttons space-left">
					<fmt:message key="label.monitoring.updatecomment.save" /> 
				</a>
			</lams:ImgButtonWrapper>
		
		</div>
		<!--closes content-->
	
		<div id="footer">
		</div>
		<!--closes footer-->

		</div>	
	</body>
</lams:html>
