<!DOCTYPE html>

<%@ page import="org.lamsfoundation.lams.timezone.Timezone" %>
<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.timezone.title"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
	
	<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
</lams:head>
    
<body class="component pb-4 pt-2 px-2 px-sm-4">
	<c:set var="help"><fmt:message key="LAMS+Configuration"/></c:set>
	<c:set var="help"><lams:help style="small" page="${help}" /></c:set>
	
	<%-- Build breadcrumb --%>
	<c:set var="breadcrumbItems"><lams:LAMSURL/>admin/appadminstart.do | <fmt:message key="appadmin.maintain" /></c:set>
	<c:set var="breadcrumbItems">${breadcrumbItems}, . | <fmt:message key="admin.timezone.title"/></c:set>
	
	<lams:Page5 type="admin" title="${title}" titleHelpURL="${help}" breadcrumbItems="${breadcrumbItems}" formID="timezoneForm">
		<form action="changeServerTimezone.do">
			<div class="row">
				<div class="col-6 offset-3">
					<h4>
						<fmt:message key="admin.servertimezone.select.server.timezone" />
					</h4>
					<select name="serverTimezone" class="form-select">
						<c:forEach items="${timezoneDtos}" var="timezoneDto">
							<option value="${timezoneDto.timeZoneId}" class="form-control" ${timezoneDto.timeZoneId eq serverTimezone ? "selected" : ""}>
								${timezoneDto.timeZoneId}
								&nbsp;&nbsp;-&nbsp;&nbsp;
								<fmt:message key="admin.servertimezone.raw.offset" ><fmt:param><fmt:formatDate value="${timezoneDto.rawOffset}" pattern="H:mm" timeZone="GMT" /></fmt:param></fmt:message>
								&nbsp;&nbsp;-&nbsp;&nbsp;
								<fmt:message key="admin.servertimezone.dst.offset"><fmt:param>${timezoneDto.dstOffset}</fmt:param></fmt:message>
								&nbsp;&nbsp;-&nbsp;&nbsp;
								<fmt:message key="admin.servertimezone.name"><fmt:param>${timezoneDto.displayName}</fmt:param></fmt:message>
							</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="row mt-3">
				<div class="col-6 offset-3 text-end">
					<a href="<lams:LAMSURL/>admin/appadminstart.do" class="btn btn-secondary"><fmt:message key="admin.cancel"/></a>
					<button class="btn btn-primary">
						<c:if test="${param.saved eq 'true'}">
							<i class="fa fa-check"></i>
						</c:if>
						<fmt:message key="admin.servertimezone.select" /> 
					</button>
				</div>
			</div>	
		</form>
	</lams:Page5>
</body>
</lams:html>
