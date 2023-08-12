<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE)%></c:set>
<c:set var="EXE_FILE_TYPES"><%=Configuration.get(ConfigurationKeys.EXE_EXTENSIONS)%></c:set>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="tool"><lams:WebAppURL /></c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />

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
		
		<script type="text/javascript">
			var removeItemAttachmentUrl = "<lams:WebAppURL />learning/deleteAttachment.do";
			var warning = '<fmt:message key="warn.minimum.number.characters" />';
			var LABEL_MAX_FILE_SIZE = '<fmt:message key="errors.maxfilesize"><param>{0}</param></fmt:message>';
			var LABEL_NOT_ALLOWED_FORMAT = '<fmt:message key="error.attachment.executable"/>';	
			var EXE_FILE_TYPES = '${EXE_FILE_TYPES}';
			var UPLOAD_FILE_MAX_SIZE = '${UPLOAD_FILE_MAX_SIZE}';
		</script>
		<script type="text/javascript" src="${tool}includes/javascript/learner.js"></script>	
		
	</lams:head>
	
	<body class="stripes">
		
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
		
		<c:set var="title"><fmt:message key="activity.title" /></c:set>
		<lams:Page type="learner" title="${title}">
		
			<lams:Alert type="danger" id="submission-deadline" close="false">
				<fmt:message key="authoring.info.teacher.set.restriction" >
					<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
				</fmt:message>
			</lams:Alert>
		
			<div class="activity-bottom-buttons">
				<c:set var="continue">
					<lams:WebAppURL />learning/newReflection.do?sessionMapID=${sessionMapID}
				</c:set>
				<c:set var="finish">
					<lams:WebAppURL />learning/finish.do?sessionMapID=${sessionMapID}
				</c:set>
				
				<script type="text/javascript">
					function submitFinish() {
						document.getElementById("finish").disabled = true;
						location.href = '${finish}';
					}		
				</script>
				
				<c:choose>
					<c:when
						test="${sessionMap.reflectOn && (not sessionMap.finishedLock)}">
						<button name="continue"
							onclick="javascript:location.href='${continue}';"
							class="btn btn-primary">
							<fmt:message key="label.continue" />
						</button>
					</c:when>
					<c:otherwise>
						<a href="#nogo"  name="finish" id="finish"
							onclick="submitFinish();" class="btn btn-primary na">
								<c:choose>
				 					<c:when test="${sessionMap.isLastActivity}">
				 						<fmt:message key="label.submit" />
				 					</c:when>
				 					<c:otherwise>
				 		 				<fmt:message key="label.finish" />
				 					</c:otherwise>
				 				</c:choose>
						</a>
					</c:otherwise>
				</c:choose>
		
			</div>
		</lams:Page>
	</body>
</lams:html>