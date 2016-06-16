<%@ page import="org.lamsfoundation.lams.timezone.Timezone" %>
<%@ include file="/taglibs.jsp"%>
<link rel="stylesheet" href="<lams:LAMSURL/>/css/thickbox.css" type="text/css" media="screen">
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/thickbox.js"></script>

<p><a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>
<fmt:message key="admin.timezone.select.timezones.you.want.users.choose" >
<fmt:param>
	<a class="thickbox" href="timezonemanagement.do?method=serverTimezoneManagement&KeepThis=true&TB_iframe=true&height=188&width=820" title="<fmt:message key='admin.servertimezone.server.timezone.management'/>">
		${serverTimezone}
	</a>
</fmt:param>
</fmt:message>

<c:if test="${not empty error}">
	<lams:Alert type="danger" id="errorKey" close="false">				
		<c:out value="${error}"/></p>
	</lams:Alert>
</c:if>

<html:form action="/timezonemanagement" method="post">
	<html:hidden property="method" value="save"/>
	
	<table class="table table-striped table-condensed">
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
	
	<div class="pull-right">
		<html:cancel styleClass="btn btn-default"><fmt:message key="admin.cancel" /></html:cancel>
		<html:submit styleClass="btn btn-primary loffset5"><fmt:message key="admin.save" /></html:submit>
	</p>
</html:form>