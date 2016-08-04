<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	window.resizeTo(550,300)
	window.opener.refresh();
</script>

<lams:html>

	<lams:head>  
		<lams:css/>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	</lams:head>
	
	<body class="stripes">
	
		<c:choose>
			<c:when test="${success}">
				<c:set var="title"><fmt:message key="message.imageEdited" /></c:set>
			</c:when>
			<c:otherwise>
				<c:set var="title"><fmt:message key="error.retreiving.image" /></c:set>
			</c:otherwise>
		</c:choose>
	
		<lams:Page type="learner" title="${title}" hideProgressBar="true">
			
			<html:submit styleClass="btn btn-primary pull-right" onclick="javascript:window.close();">
				<fmt:message key="button.close" />
			</html:submit>
			
			<div class="footer"></div>
			
		</lams:Page>
	</body>
</lams:html>


