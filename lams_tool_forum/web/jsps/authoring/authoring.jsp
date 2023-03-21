<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %>
<%@ page import="org.lamsfoundation.lams.tool.forum.ForumConstants"%>    
<c:set var="UPLOAD_FILE_LARGE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set>
<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE)%></c:set>
<c:set var="EXE_FILE_TYPES"><%=Configuration.get(ConfigurationKeys.EXE_EXTENSIONS)%></c:set>
<c:set var="language"><lams:user property="localeLanguage"/></c:set>

<lams:html>
	<lams:head>
		<title><fmt:message key="activity.title" /></title>
		<%@ include file="/common/tabbedheader.jsp"%>
		<link href="${lams}css/uppy.min.css" rel="stylesheet" type="text/css" />
		<link href="${lams}css/uppy.custom.css" rel="stylesheet" type="text/css" />
		
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
		    var csrfTokenName = '<csrf:tokenname/>',
   				csrfTokenValue = '<csrf:tokenvalue/>';
   				
   			var LAMS_URL = '${lams}',
   		 		UPLOAD_FILE_MAX_SIZE = '<c:out value="${UPLOAD_FILE_MAX_SIZE}"/>',
   		 		UPLOAD_FILE_LARGE_MAX_SIZE = '<c:out value="${UPLOAD_FILE_LARGE_MAX_SIZE}"/>',
   				// convert Java syntax to JSON
   		       EXE_FILE_TYPES = JSON.parse("[" + "${EXE_FILE_TYPES}".replace(/\.\w+/g, '"$&"') + "]"),
   			   decoderDiv = $('<div />'),
   		       <fmt:message key="error.attachment.executable" var="EXE_FILE_ERROR_VAR" />
   		       EXE_FILE_ERROR = decoderDiv.html('<c:out value="${EXE_FILE_ERROR_VAR}" />').text();
   				
			function init(){
				var tag = document.getElementById("currentTab");
				if(tag == null || tag.value != "")
					selectTab(tag.value);
		        else
					selectTab(1);
			}  
		        
			function doSelectTab(tabId) {
				var tag = document.getElementById("currentTab");
				tag.value = tabId;
				selectTab(tabId);
			}
		
			function doSubmit(method) {
				var authorForm = document.getElementById("forumForm");
				authorForm.action="<c:url value='/authoring/'/>"+method+".do";
				authorForm.submit();
			}
			
		</script>
		
	</lams:head>

	<body class="stripes" onLoad="init()">
	
		<form:form action="update.do" method="post" id="forumForm" modelAttribute="forumForm">
			<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
			<form:hidden path="toolContentID" />
			<form:hidden path="sessionMapID" />
			<form:hidden path="contentFolderID" />
			<form:hidden path="currentTab" id="currentTab" />
			<input type="hidden" name="mode" value="${mode}">
			
			<c:set var="title"><fmt:message key="activity.title" /></c:set>
			<lams:Page title="${title}" type="navbar" formID="forumForm">
				
				<lams:Tabs control="true" title="${title}" helpToolSignature="<%= ForumConstants.TOOL_SIGNATURE %>" helpModule="authoring">
					<lams:Tab id="1" key="authoring.tab.basic" />
					<lams:Tab id="2" key="authoring.tab.advanced" />
					<lams:Tab id="3" key="authoring.tab.conditions" />
				</lams:Tabs> 
			
				<lams:TabBodyArea>
					<lams:errors/>
				   
				    <!--  Set up tabs  -->
				     <lams:TabBodys>
						<lams:TabBody id="1" page="basic.jsp" />
						<lams:TabBody id="2" page="advance.jsp" />
						<lams:TabBody id="3" page="conditions.jsp" />
				    </lams:TabBodys>
				
					<!-- Button Row -->
					<lams:AuthoringButton formID="forumForm"
						clearSessionActionUrl="/clearsession.do"
						toolSignature="<%=ForumConstants.TOOL_SIGNATURE%>"
						toolContentID="${forumForm.toolContentID}"
						customiseSessionID="${forumForm.sessionMapID}"
						contentFolderID="${forumForm.contentFolderID}" 
						accessMode="${mode}" 
						defineLater="${mode=='teacher'}"/>
				</lams:TabBodyArea>
			           		
			    <div id="footer"></div>
			    
			</lams:Page>
		</form:form>

		
	</body>
</lams:html>




