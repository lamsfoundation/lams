<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.user.management"/></c:set>
	<title>${title}</title>
		<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
</lams:head>
    
<body class="stripes">
	<lams:Page type="admin" title="${title}">
		<p><a href="<lams:LAMSURL/>admin/appadminstart.do" class="btn btn-default"><fmt:message key="appadmin.maintain" /></a></p>

		<p>${successful}</p>
		<p>
		<c:forEach items="${results}" var="messages" varStatus="index">
			<c:if test="${not empty messages}">
				Row <c:out value="${index.index+2}" />:
				<c:forEach items="${messages}" var="message">
					<c:out value="${message}" /><br />
				</c:forEach>
			</c:if>
		</c:forEach>
		</p>

	</lams:Page>
</body>
</lams:html>



