<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>

<c:set var="UPLOAD_FILE_LARGE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set>
<c:set var="UPLOAD_FILE_MAX_SIZE_AS_USER_STRING"><%=FileValidatorUtil.formatSize(Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE))%></c:set>
<c:set var="EXE_FILE_TYPES"><%=Configuration.get(ConfigurationKeys.EXE_EXTENSIONS)%></c:set>
<c:set var="language"><lams:user property="localeLanguage"/></c:set>

 	<!-- ********************  CSS ********************** -->
	<lams:css />
	<link href="${lams}css/uppy.min.css" rel="stylesheet" type="text/css" />
	<link href="${lams}css/uppy.custom.css" rel="stylesheet" type="text/css" />

 	<!-- ********************  javascript ********************** -->
	<lams:JSImport src="includes/javascript/common.js" />
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.validate.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.tabcontroller.js"></script>
	<lams:JSImport src="includes/javascript/upload.js" />
	<lams:JSImport src="includes/javascript/rsrccommon.js" relative="true" />

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
	   <%-- used by rsrcresourceitem.js --%>
       	var removeItemAttachmentUrl = "<c:url value='/authoring/removeItemAttachment.do'/>";
       	
		var LAMS_URL = '${lams}',
			UPLOAD_FILE_LARGE_MAX_SIZE = '<c:out value="${UPLOAD_FILE_LARGE_MAX_SIZE}"/>',
			// convert Java syntax to JSON
	       EXE_FILE_TYPES = JSON.parse("[" + "${EXE_FILE_TYPES}".replace(/\.\w+/g, '"$&"') + "]"),
		   decoderDiv = $('<div />'),
	       <fmt:message key="error.attachment.executable" var="EXE_FILE_ERROR_VAR" />
	       EXE_FILE_ERROR = decoderDiv.html('<c:out value="${EXE_FILE_ERROR_VAR}" />').text();
	</script>