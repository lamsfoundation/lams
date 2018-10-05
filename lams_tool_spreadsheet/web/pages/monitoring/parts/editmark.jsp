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

	<form:form action="saveMark.do" method="post" modelAttribute="markForm" id="markForm" focus="marks">

	<lams:Page type="learner" title="${title}" formID="markForm">
	
		<!-- Basic Info Form-->
 		<lams:errors/>

		<p><fmt:message key="label.reviewitem.spreadsheet.sent.by" />&nbsp;<strong>${markForm.userName}</strong></p>
		
		<c:set var="sessionMapID" value="${markForm.sessionMapID}" />				
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
		<form:hidden path="sessionMapID" />
		<form:hidden path="userUid" />
		<form:hidden path="code" />
		<form:hidden path="userName" />
		
		<div class="form-group">
			<label for="marks"><fmt:message key="label.monitoring.summary.marking.marks" /></label>
        		<input type="text" name="marks" size="10" value="${markForm.marks}" style="form-control form-control-inline"/>
		</div>

		<div class="form-group">
           	<label for="comments"><fmt:message key="label.monitoring.summary.marking.comments" /></label>
           	<lams:CKEditor id="comments" value="${markForm.comments}" contentFolderID="${sessionMap.contentFolderID}"></lams:CKEditor>
		</div>
	
		<c:choose>
			<c:when test="${empty markForm.code}">
				<lams:Alert type="info" id="no-spreadsheet" close="false">
					<fmt:message key="label.reviewitem.user.hasnot.sent.spreadsheet" />
				</lams:Alert>
			</c:when>
			<c:otherwise>
			    <input type="hidden" name="spreadsheetCode" id="spreadsheet-code" value="<c:out value='${markForm.code}' />" />	
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
	
	</form:form> 

	
	</body>
</lams:html>
