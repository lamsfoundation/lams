<!DOCTYPE html>

<%@ page import="org.lamsfoundation.lams.timezone.Timezone" %>
<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.timezone.title"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">

	<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.js"></script>
</lams:head>
    
<body class="stripes">
	<lams:Page type="admin" title="${title}" formID="timezoneForm">
		<p><a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>
			
		<h4>
			<fmt:message key="admin.servertimezone.select.server.timezone" />
		</h4>
		
		<form action="changeServerTimezone.do">
			<select name="serverTimezone" style="display: block">
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
			
			<button class="btn btn-primary voffset20">
				<c:if test="${param.saved eq 'true'}">
					<i class="fa fa-check"></i>
				</c:if>
				<fmt:message key="admin.servertimezone.select" /> 
			</button>
		</form>
	</lams:Page>
</body>
</lams:html>
