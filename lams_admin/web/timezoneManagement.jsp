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
	<link rel="stylesheet" href="<lams:LAMSURL/>/css/thickbox.css" type="text/css" media="screen">

	<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/thickbox.js"></script>
	<script type="text/javascript">	
		//this script lets user select all available timezones just by enabling the checkbox in header label
		$(document).ready(function(){
			if ($('.timezoneCheckBox:checked').length == $('.timezoneCheckBox').length) {
					$('#currentSelectAllTZ').prop('checked', true);			
				}
			$("#currentSelectAllTZ").click(function(){
				if ($("#currentSelectAllTZ").is(':checked')) {
					//$('#ttl-login-request').prop("value", 80);
				    $('input:checkbox').not(this).prop('checked', this.checked);
				}else{
				    $('input:checkbox').not(this).prop('checked', false);	
				}
			});
		});
	</script>
</lams:head>
    
<body class="stripes">
	<c:set var="help"><fmt:message key="LAMS+Configuration"/></c:set>
	<c:set var="help"><lams:help style="small" page="${help}" /></c:set>
	<lams:Page type="admin" title="${title}" titleHelpURL="${help}" formID="timezoneForm">
		
			<p><a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>
			<fmt:message key="admin.timezone.select.timezones.you.want.users.choose" >
			<fmt:param>
				<a class="thickbox" href="<lams:LAMSURL/>admin/timezonemanagement/serverTimezoneManagement.do?KeepThis=true&TB_iframe=true&height=188&width=820" title="<fmt:message key='admin.servertimezone.server.timezone.management'/>">
					${serverTimezone}
				</a>
			</fmt:param>
			</fmt:message>
			
			<lams:errors/>
		<c:set var="csrfToken"><csrf:token/></c:set>	
        <form:form action="save.do?${csrfToken}" id="timezoneForm" modelAttribute="timezoneForm" method="post">
				
				<table class="table table-striped table-condensed">
					<tr>
						<th width="10%" align="center"><fmt:message key="admin.timezone.select" />
						<!-- added this check box for the user to enable all available time zones -->
						  <form:checkbox path="selected" value="currentSelectAllTZ" id="currentSelectAllTZ"/>
						</th>
						<th width="22%"><fmt:message key="admin.timezone.time.zone.id" /></th>
						<th width="24%" align="center"><fmt:message key="admin.timezone.raw.offset" /></th>
						<th width="12%" align="center"><fmt:message key="admin.timezone.dst.offset" /></th>
						<th width="32%"><fmt:message key="admin.timezone.display.name" /></th>
					</tr>	
				
					<c:forEach items="${timezoneDtos}" var="timezoneDto">
						<tr>
							<td align="center">
								<input type="checkbox" class="timezoneCheckBox" name="selected" ${timezoneDto.selected ? 'checked="checked"' : ''} value="${timezoneDto.timeZoneId}"/>
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
					</c:forEach>
					
				</table>
				
				<div class="pull-right">
					<a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-default">
						<fmt:message key="admin.cancel"/>
					</a>
					<input type="submit" class="btn btn-primary loffset5" value="<fmt:message key="admin.save" />" />
				</p>
				
			</form:form>
		</lams:Page>
</body>
</lams:html>
