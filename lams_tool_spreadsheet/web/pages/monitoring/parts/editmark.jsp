<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css/>
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
	</lams:head>
	<body class="stripes">
	
	<c:set var="title"><fmt:message key="label.monitoring.summary.marking.marking" /></c:set>

	<html:form action="/monitoring/saveMark" method="post" styleId="markForm" focus="marks">
	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" /> 

	<lams:Page type="learner" title="${title}">
	
		<!-- Basic Info Form-->
 		<%@ include file="/common/messages.jsp"%>

		<p><fmt:message key="label.reviewitem.spreadsheet.sent.by" />&nbsp;<strong>${formBean.userName}</strong></p>
		
		<c:set var="sessionMapID" value="${formBean.sessionMapID}" />				
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
		<html:hidden property="sessionMapID" />
		<html:hidden property="userUid" />
		<html:hidden property="code" />
		<html:hidden property="userName" />
		
		<div class="form-group">
			<label for="marks"><fmt:message key="label.monitoring.summary.marking.marks" /></label>
        		<html:text property="marks" size="10" style="form-control form-control-inline"/>
		</div>

		<div class="form-group">
           	<label for="comments"><fmt:message key="label.monitoring.summary.marking.comments" /></label>
           	<lams:CKEditor id="comments" value="${formBean.comments}" contentFolderID="${sessionMap.contentFolderID}"></lams:CKEditor>
		</div>
	
		<c:choose>
			<c:when test="${empty formBean.code}">
				<lams:Alert type="info" id="no-spreadsheet" close="false">
					<fmt:message key="label.reviewitem.user.hasnot.sent.spreadsheet" />
				</lams:Alert>
			</c:when>
			<c:otherwise>
			    <html:hidden property="spreadsheetCode" styleId="spreadsheet-code" value="${formBean.code}"/>	
				<iframe
					id="externalSpreadsheet" name="externalSpreadsheet" src="${spreadsheetURL}"
					style="width:99%" frameborder="no" height="385px"
					scrolling="no">
				</iframe> 
			</c:otherwise>
		</c:choose>	

		<p>
			<a href="#" onclick="markForm.submit();" class="btn btn-default btn-small loffset5">
				<fmt:message key="label.monitoring.summary.marking.update.marks" /> 
			</a>
			<a href="#" onclick="javascript:window.close()" class="btn btn-default btn-small loffset5">
				<fmt:message key="label.cancel" /> 
			</a>
		</p> 
		
		<div id="footer"></div>
		
	</lams:Page>
	
	</html:form> 

	
	</body>
</lams:html>
