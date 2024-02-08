<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>
<%-- Build breadcrumb --%>
<c:set var="breadcrumbItems"><lams:LAMSURL/>admin/appadminstart.do | <fmt:message key="appadmin.maintain" /></c:set>
<c:set var="breadcrumbItems">${breadcrumbItems}, . | ${title}</c:set>
	
<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="appadmin.maintain.session"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	
	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme5.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>/css/jquery.tablesorter.theme.bootstrap5.css">

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.timeago.js"></script>	
	<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.tablesorter.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
	<script type="text/javascript">
	    jQuery(document).ready(function() {
	    	jQuery("time.timeago").timeago();
	    	
	    	// initial sort set using data-sortlist attribute 
			$(".sessionTable").tablesorter({
				theme : 'bootstrap'
			});
	    });
	</script>
</lams:head>

	<lams:PageAdmin title="${title}" breadcrumbItems="${breadcrumbItems}">
		<div class="alert alert-info">${fn:length(sessions)}&nbsp;<fmt:message key="appadmin.maintain.session.count" /></div>

		<table class="table table-striped table-hover bg-white sessionTable align-middle" id="sessionTable" data-sortlist="[[1]]">
			<thead class="thead-light">
			<tr>
				<th><fmt:message key="appadmin.maintain.session.login" /></th>
				<th><fmt:message key="appadmin.maintain.session.name" /></th>
				<th><fmt:message key="appadmin.maintain.session.access" /></th>
				<th><fmt:message key="appadmin.maintain.session.id" /></th>
				<th data-sorter="false"  class="text-center"><fmt:message key="admin.actions" /></th>
			</tr>
			</thead>
			<tbody>
			<c:forEach items="${sessions}" var="ses">
			<tr>
				<td><c:out value="${ses.key}" /></td>
				<td><c:out value="${ses.value[0]} ${ses.value[1]}" /></td>
				<td><lams:Date value="${ses.value[2]}" timeago="true"/></td>
				<td title="<fmt:message key="appadmin.maintain.session.created" />&nbsp;<c:out value="${ses.value[3]}" />">
					<c:out value="${ses.value[4]}" />
				</td>
				<td class="text-center">
					<csrf:form style="display: inline-block;" id="delete_${ses.key}" method="post" action="/lams/admin/sessionmaintain/delete.do">
					<input type="hidden" name="login" value="${ses.key}"/>
					<button title="<fmt:message key="sysadmin.maintain.session.delete" />" type="submit" class="btn btn-danger">
						<i class="fa fa-trash"></i></button>
					</csrf:form>
				</td>
			</tr>
			</c:forEach>
			</tbody>
		</table>
		<hr>
		
		<div class="text-end">
			<a href="<lams:LAMSURL/>admin/appadminstart.do" class="btn btn-secondary">
				<fmt:message key="admin.cancel"/>
			</a>
		</div>
	</lams:PageAdmin>
</lams:html>
