<%@ page import="org.lamsfoundation.lams.timezone.Timezone" %>
<%@ include file="/taglibs.jsp"%>
<link rel="stylesheet" href="<lams:LAMSURL/>/css/thickbox.css" type="text/css" media="screen">
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery-latest.pack.js"></script>
<script type="text/javascript">
	var tb_pathToImage ='<lams:LAMSURL/>images/loadingAnimation.gif';
</script>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/thickbox-compressed.js"></script>

<h4><a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a></h4>
<lams:help style="no-tabs" page="<%= Timezone.TIMEZONE_HELP_PAGE %>"/>
<h1><fmt:message key="admin.timezone.available.timezones" /></h1>
<fmt:message key="admin.timezone.select.timezones.you.want.users.choose" />
	<a class="thickbox" href="timezonemanagement.do?method=serverTimezoneManagement&KeepThis=true&TB_iframe=true&height=188&width=820" title="<fmt:message key='admin.servertimezone.server.timezone.management'/>">
		${serverTimezone}
	</a>

<div align="center">
	<c:if test="${not empty error}">
		<p class='warning'><c:out value="${error}"/></p>
	</c:if>
</div>

<html:form action="/timezonemanagement" method="post">
	<html:hidden property="method" value="save"/>
	
	<table class="alternative-color" width=100% cellspacing="0" style="padding-top:20px;">
		<tr>
			<th width="8%"><fmt:message key="admin.timezone.select" /></th>
			<th width="22%"><fmt:message key="admin.timezone.time.zone.id" /></th>
			<th width="24%" align="center"><fmt:message key="admin.timezone.raw.offset" /></th>
			<th width="13%" align="center"><fmt:message key="admin.timezone.dst.offset" /></th>
			<th width="33%"><fmt:message key="admin.timezone.display.name" /></th>
		</tr>	
	
		<logic:iterate name="timezoneDtos" id="timezoneDto">
			<tr>
				<td align="center">
					<html:checkbox name="timezoneDto" property="selected" value="${timezoneDto.timeZoneId}"></html:checkbox>
				</td>
				<td>
					${timezoneDto.timeZoneId}
				</td>
				<td align="center">
					<c:if test="${timezoneDto.rawOffsetNegative}">-</c:if>
					<fmt:formatDate value="${timezoneDto.rawOffset}" pattern="H : mm" timeZone="GMT" /> 
				</td>
				<td align="center">
					<c:if test="${timezoneDto.dstOffset != 0}">
						${timezoneDto.dstOffset}
					</c:if>
				</td>												
				<td>
					${timezoneDto.displayName}
				</td>				
			</tr>
		</logic:iterate>
		
	</table>
	
	<p align="right" class="small-space-top" style="margin-right: 10px">
		<html:submit styleClass="button" style="margin-right: 15px"><fmt:message key="admin.save" /></html:submit>
		<html:cancel styleClass="button"><fmt:message key="admin.cancel" /></html:cancel>
	</p>
</html:form>