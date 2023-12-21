<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.user.management"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />

	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
</lams:head>
    
<body class="component pb-4 pt-2 px-2 px-sm-4">
	<lams:Page5 type="admin" title="${title}">
		<p><a href="<lams:LAMSURL/>admin/appadminstart.do" class="btn btn-secondary"><fmt:message key="appadmin.maintain" /></a></p>

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

	</lams:Page5>
</body>
</lams:html>
