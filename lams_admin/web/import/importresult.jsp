<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.user.management"/></c:set>
	<title>${title}</title>

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>/admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-smoothness-theme.css" type="text/css" media="screen">
	<script language="JavaScript" type="text/JavaScript" src="<lams:LAMSURL/>/includes/javascript/changeStyle.js"></script>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
</lams:head>
    
<body class="stripes">
	<lams:Page type="admin" title="${title}">
		<p><a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>

		<p><c:out value="${successful}" /></p>
		<p>
		<c:forEach items="${results}" var="messages" indexId="index">
			<c:if test="${not empty messages}">
				Row <c:out value="${index+2}" />:
				<c:forEach items="${messages}" var="message">
					<c:out value="${message}" /><br />
				</c:forEach>
			</c:if>
		</c:forEach>
		</p>

	</lams:Page>
</body>
</lams:html>



