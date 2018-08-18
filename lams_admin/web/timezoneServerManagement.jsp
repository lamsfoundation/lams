<%@ include file="/taglibs.jsp"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<title><fmt:message key="admin.servertimezone.server.timezone"/></title>
	<link rel="icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />
	
	<lams:css />
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
			parent.document.location.href ='<c:url value="/timezonemanagement.do?method=changeServerTimezone&timeZoneId="/>' + timeZoneId;
		}
	</script>	
</lams:head>

<body>

<h4 class="loffset10"><fmt:message key="admin.servertimezone.select.server.timezone" /></h4>
	<div style="padding: 13px 20px 20px;">

		<html:select styleId="timeZoneId" property="timeZoneId" value="${serverTimezone}" style="margin-bottom:40px;">
			<logic:iterate name="timezoneDtos" id="timezoneDto">
				<html:option value="${timezoneDto.timeZoneId}" styleClass="form-control">
					${timezoneDto.timeZoneId}
					&nbsp;&nbsp;-&nbsp;&nbsp;
					<fmt:message key="admin.servertimezone.raw.offset" ><fmt:param><fmt:formatDate value="${timezoneDto.rawOffset}" pattern="H:mm" timeZone="GMT" /></fmt:param></fmt:message>
					&nbsp;&nbsp;-&nbsp;&nbsp;
					<fmt:message key="admin.servertimezone.dst.offset"><fmt:param>${timezoneDto.dstOffset}</fmt:param></fmt:message>
					&nbsp;&nbsp;-&nbsp;&nbsp;
					<fmt:message key="admin.servertimezone.name"><fmt:param>${timezoneDto.displayName}</fmt:param></fmt:message>
				</html:option>
			</logic:iterate>
		</html:select>
		
		<div class="pull-right">
			<a href="#" onclick="self.parent.tb_remove();" class="btn btn-default btn-sm">
				<fmt:message key="admin.cancel" /> 
			</a>
			<a href="#" onclick="changeServerTimezone();" class="btn btn-primary btn-sm">
				<fmt:message key="admin.servertimezone.select" /> 
			</a>
		</div>
	</div>
	
</body>
</lams:html>

