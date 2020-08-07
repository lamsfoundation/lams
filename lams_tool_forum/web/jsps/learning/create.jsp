<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %>
<%@ page import="org.lamsfoundation.lams.tool.forum.ForumConstants"%>   
<c:set var="UPLOAD_FILE_LARGE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set>
<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE)%></c:set>
<c:set var="EXE_FILE_TYPES"><%=Configuration.get(ConfigurationKeys.EXE_EXTENSIONS)%></c:set>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="tool"><lams:WebAppURL /></c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="language"><lams:user property="localeLanguage"/></c:set>
<lams:html>
	<lams:head>
		<title><fmt:message key="activity.title" /></title>

		<!-- ********************  CSS ********************** -->

		<lams:css />
		<link href="${lams}css/uppy.min.css" rel="stylesheet" type="text/css" />
		
		<style media="screen,projection" type="text/css">
			.info {
				margin: 10px;
			}
		</style>
		
		<!-- ********************  javascript ********************** -->
		<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.treetable.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>		
		<script type="text/javascript" src="${lams}includes/javascript/jquery.jscroll.js"></script>
		
		<script type="text/javascript" src="${lams}includes/javascript/uppy/uppy.min.js"></script>
		<c:choose>
			<c:when test="${language eq 'es'}">
				<script type="text/javascript" src="${lams}includes/javascript/uppy/es_ES.min.js"></script>
			</c:when>
			<c:when test="${language eq 'fr'}">
				<script type="text/javascript" src="${lams}includes/javascript/uppy/fr_FR.min.js"></script>
			</c:when>
			<c:when test="${language eq 'el'}">
				<script type="text/javascript" src="${lams}includes/javascript/uppy/el_GR.min.js"></script>
			</c:when>
		</c:choose>
		
		<script type="text/javascript">
			var removeItemAttachmentUrl = "<lams:WebAppURL />learning/deleteAttachment.do";
			var warning = '<fmt:message key="warn.minimum.number.characters" />';
			var LABEL_MAX_FILE_SIZE = '<fmt:message key="errors.maxfilesize"><param>{0}</param></fmt:message>';
			var LABEL_NOT_ALLOWED_FORMAT = '<fmt:message key="error.attachment.executable"/>';	
				
   			var LAMS_URL = '${lams}',
   		 		UPLOAD_FILE_MAX_SIZE = '<c:out value="${UPLOAD_FILE_MAX_SIZE}"/>',
   		 		UPLOAD_FILE_LARGE_MAX_SIZE = '<c:out value="${UPLOAD_FILE_LARGE_MAX_SIZE}"/>';
   				// convert Java syntax to JSON
   		       EXE_FILE_TYPES = JSON.parse("[" + "${EXE_FILE_TYPES}".replace(/\.\w+/g, '"$&"') + "]"),
   		       EXE_FILE_ERROR = '<fmt:message key="error.attachment.executable"/>';
		</script>
		<script type="text/javascript" src="${tool}includes/javascript/learner.js"></script>	
		
	</lams:head>
	
	<body class="stripes">
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
		
		<form:form action="createTopic.do" id="messageForm" modelAttribute="messageForm" onsubmit="return doSubmit();" 
				method="post" focus="message.subject" enctype="multipart/form-data">
				
			<form:hidden path="sessionMapID" />
			<c:set var="sessionMapID" value="${messageForm.sessionMapID}" />
			<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
		
			<lams:Page type="learner" title="${sessionMap.title}" formID="messageForm">
		
				<div class="container-fluid">
				<div class="panel panel-default">
					<div class="panel-heading panel-title">
						<fmt:message key="title.message.edit" />
					</div>
					<div class="panel-body">
						<lams:errors/>
		 				<%@ include file="/jsps/learning/message/topicform.jsp"%>
					</div>
				</div>
				</div>		
			</lams:Page>
		
		</form:form>
		

	</body>
</lams:html>

