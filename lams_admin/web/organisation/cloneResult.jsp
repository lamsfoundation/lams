<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.usermanagement.OrganisationType" %>

<c:set var="classTypeId"><%= OrganisationType.CLASS_TYPE %></c:set>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="title.clone.lessons"/></c:set>
	<title>${title}</title>
		<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
</lams:head>
    
<body class="stripes">

	<lams:Page type="admin" title="${title}">
	
		<p><a href="<lams:LAMSURL/>admin/orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a> :
			<c:if test="${org.organisationType.organisationTypeId eq classTypeId}">
				<a href="<lams:LAMSURL/>admin/orgmanage.do?org=<c:out value="${org.parentOrganisation.organisationId}" />" class="btn btn-default">
				<c:out value="${org.parentOrganisation.name}" />
				</a> :
				</c:if>
				<a href="<lams:LAMSURL/>admin/orgmanage.do?org=<c:out value="${org.organisationId}" />" class="btn btn-default">
					<c:out value="${org.name}" />
				</a>
		</p>

		<h4><fmt:message key="title.clone.lessons.for"><fmt:param value="${org.name}" /></fmt:message></h4>
				
		<c:if test="${not empty errors}">
			<lams:Alert type="danger" id="errorKey" close="false">	
				<c:forEach items="${errors}" var="error">
					<c:out value="${error}" />
				</c:forEach>
			</lams:Alert>
		</c:if>
				
		<p>
			<fmt:message key="message.cloned.lessons"><fmt:param value="${result}" /></fmt:message>
		</p>
				
		<input type="button" class="btn btn-default pull-right" value="<fmt:message key="label.return.to.group" />" 
		onclick="document.location='<lams:LAMSURL/>admin/orgmanage.do?org=<c:out value="${org.organisationId}" />';"	>
		
	</lams:Page>
	
</body>
</lams:html>
