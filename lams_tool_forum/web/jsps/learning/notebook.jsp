<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE)%></c:set>
<c:set var="EXE_FILE_TYPES"><%=Configuration.get(ConfigurationKeys.EXE_EXTENSIONS)%></c:set>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="tool"><lams:WebAppURL /></c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
		
<lams:html>
	<lams:head>
		<title><fmt:message key="activity.title" /></title>

		<!-- ********************  CSS ********************** -->

		<lams:css />
		<style media="screen,projection" type="text/css">
			.info {
				margin: 10px;
			}
		</style>
		
		<!-- ********************  javascript ********************** -->
		<lams:JSImport src="includes/javascript/common.js" />
		<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.treetable.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>		
		<script type="text/javascript" src="${lams}includes/javascript/jquery.jscroll.js"></script>
		<lams:JSImport src="includes/javascript/upload.js" />
		<lams:JSImport src="learning/includes/javascript/gate-check.js" />
	
		<script type="text/javascript">
			var removeItemAttachmentUrl = "<lams:WebAppURL />learning/deleteAttachment.do";
			var warning = '<fmt:message key="warn.minimum.number.characters" />';
			var LABEL_MAX_FILE_SIZE = '<fmt:message key="errors.maxfilesize"><param>{0}</param></fmt:message>';
			var LABEL_NOT_ALLOWED_FORMAT = '<fmt:message key="error.attachment.executable"/>';	
			var EXE_FILE_TYPES = '${EXE_FILE_TYPES}';
			var UPLOAD_FILE_MAX_SIZE = '${UPLOAD_FILE_MAX_SIZE}';

			checkNextGateActivity('finishButton', '${sessionMap.toolSessionID}', '', submitForm);

			function disableFinishButton() {
				document.getElementById("finishButton").disabled = true;
			}
			function submitForm(){
		    	var f = document.getElementById('reflectionForm');
				f.submit();
		    }
		</script>
		<script type="text/javascript" src="${tool}includes/javascript/learner.js"></script>	
		
	</lams:head>
	
	<body class="stripes">
		<form:form action="submitReflection.do" method="post"
			onsubmit="disableFinishButton();" modelAttribute="reflectionForm" id="reflectionForm">
			<form:hidden path="userID" />
			<form:hidden path="sessionMapID" />
		
			<lams:Page type="learner" title="${sessionMap.title}" formID="reflectionForm">
		
				<lams:errors/>
		
				<p>
					<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
				</p>
		
				<textarea id="focused" rows="5" name="entryText" class="form-control">${reflectionForm.entryText}</textarea>
		
				<div class="activity-bottom-buttons">
					<a  href="#nogo" class="btn btn-primary na" id="finishButton">
						<span class="nextActivity">
							<c:choose>
			 					<c:when test="${sessionMap.isLastActivity}">
			 						<fmt:message key="label.submit" />
			 					</c:when>
			 					<c:otherwise>
			 		 				<fmt:message key="label.finish" />
			 					</c:otherwise>
			 				</c:choose>
			 			</span>
					</a>
				</div>
		
			</lams:Page>
		</form:form>
		
		<script type="text/javascript">
			window.onload = function() {
				document.getElementById("focused").focus();
			}
		</script>
	</body>
</lams:html>