<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="sysadmin.maintain.session"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
	
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.timeago.js"></script>	
	<script type="text/javascript">
	    jQuery(document).ready(function() {
	    	jQuery("time.timeago").timeago();
	    });
	</script>
</lams:head>
    
<body class="stripes">
	<lams:Page type="admin" title="${title}">
		<p>
			<a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a>
		</p>
		
		<div class="alert alert-info">${fn:length(sessions)}&nbsp;<fmt:message key="sysadmin.maintain.session.count" /></div>

		<table class="table table-striped table-hover">
			<tr>
				<th><fmt:message key="sysadmin.maintain.session.login" /></th>
				<th><fmt:message key="sysadmin.maintain.session.name" /></th>
				<th><fmt:message key="sysadmin.maintain.session.access" /></th>
				<th><fmt:message key="sysadmin.maintain.session.id" /></th>
				<th></th>
			</tr>
			<c:forEach items="${sessions}" var="ses">
			<tr>
				<td><c:out value="${ses.key}" /></td>
				<td><c:out value="${ses.value[0]} ${ses.value[1]}" /></td>
				<td><lams:Date value="${ses.value[2]}" timeago="true"/></td>
				<td title="<fmt:message key="sysadmin.maintain.session.created" />&nbsp;<c:out value="${ses.value[3]}" />">
					<c:out value="${ses.value[4]}" />
				</td>
				<td>
					<csrf:form style="display: inline-block;" id="delete_${ses.key}" method="post" action="/lams/admin/sessionmaintain/delete.do"><input type="hidden" name="login" value="${ses.key}"/><button type="submit" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> <fmt:message key="sysadmin.maintain.session.delete" /></button></csrf:form>
				</td>
			</tr>
			</c:forEach>
		</table>
	</lams:Page>
</body>
</lams:html>


