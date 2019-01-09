<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.servertimezone.title"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link rel="icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
	<style media="screen,projection" type="text/css">
		html {
			margin: 1;
			overflow:auto;
			background: none;
		}
		body {
		margin: 1;
			background: none;
			min-height: 100%;
			width: 100%;
		}	
	</style>	

	<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.js"></script>
	<script type="text/javascript">
		function changeServerTimezone() {
			var timeZoneId = encodeURIComponent($("#timeZoneId").val());
			parent.document.location.href ='<c:url value="/timezonemanagement/changeServerTimezone.do?timeZoneId="/>' + $("#timeZoneId").val();
		}
	</script>
</lams:head>
    
<body class="stripes">
	<lams:Page type="admin" title="${title}">
		<h4 class="loffset10">
			<fmt:message key="admin.servertimezone.select.server.timezone" />
		</h4>
		
		<div style="padding: 13px 20px 20px;">
			
					<form:select id="timeZoneId" path="serverTimezone" style="margin-bottom:40px;">
						<c:forEach items="${timezoneDtos}" var="timezoneDto">
							<form:option value="${timezoneDto.timeZoneId}" cssClass="form-control">
								${timezoneDto.timeZoneId}
								&nbsp;&nbsp;-&nbsp;&nbsp;
								<fmt:message key="admin.servertimezone.raw.offset" ><fmt:param><fmt:formatDate value="${timezoneDto.rawOffset}" pattern="H:mm" timeZone="GMT" /></fmt:param></fmt:message>
								&nbsp;&nbsp;-&nbsp;&nbsp;
								<fmt:message key="admin.servertimezone.dst.offset"><fmt:param>${timezoneDto.dstOffset}</fmt:param></fmt:message>
								&nbsp;&nbsp;-&nbsp;&nbsp;
								<fmt:message key="admin.servertimezone.name"><fmt:param>${timezoneDto.displayName}</fmt:param></fmt:message>
							</form:option>
						</c:forEach>
					</form:select>
					
					<div class="pull-right">
						<a href="#" onclick="self.parent.tb_remove();" class="btn btn-default btn-sm">
							<fmt:message key="admin.cancel" /> 
						</a>
						<a href="#" onclick="changeServerTimezone();" class="btn btn-primary btn-sm">
							<fmt:message key="admin.servertimezone.select" /> 
						</a>
					</div>
		</div>
	</lams:Page>
</body>
</lams:html>


