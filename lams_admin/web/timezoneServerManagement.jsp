<%@ include file="/taglibs.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<lams:html>
<lams:head>
	<title><fmt:message key="admin.servertimezone.server.timezone"/></title>
	<link rel="icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />
	
	<lams:css style="main" />
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

	<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery-latest.pack.js"></script>
	<script type="text/javascript">
		function changeServerTimezone() {
			parent.document.location.href ='<c:url value="/timezonemanagement.do?method=changeServerTimezone&timeZoneId="/>' + $("#timeZoneId").val();
		}
	</script>	
</lams:head>

<body>

<h1 style="padding: 20px 10px 0px;"><fmt:message key="admin.servertimezone.select.server.timezone" /></h1>
	<div style="padding: 13px 20px 20px;">

		<html:select styleId="timeZoneId" property="timeZoneId" value="${serverTimezone}" style="margin-bottom:40px;">
			<logic:iterate name="timezoneDtos" id="timezoneDto">
				<html:option value="${timezoneDto.timeZoneId}">
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
		
		<lams:ImgButtonWrapper>
			<a href="#" onclick="self.parent.tb_remove();" class="button right-buttons space-left">
				<fmt:message key="admin.cancel" /> 
			</a>
			<a href="#" onclick="changeServerTimezone();" class="button right-buttons">
				<fmt:message key="admin.servertimezone.select" /> 
			</a>
		</lams:ImgButtonWrapper>
	</div>
	
</body>
</lams:html>

