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
	<link rel="stylesheet" href="<lams:LAMSURL/>/css/jquery.tablesorter.theme.bootstrap4.css">
	
	
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.timeago.js"></script>	
	<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.tablesorter.js"></script>
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
    
<body class="stripes">
	<lams:Page type="admin" title="${title}">

		<nav aria-label="breadcrumb" role="navigation">
		  <ol class="breadcrumb">
		    <li class="breadcrumb-item">
		    	<a href="<lams:LAMSURL/>admin/sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a>
		    </li>
		    <li class="breadcrumb-item active" aria-current="page"><fmt:message key="sysadmin.maintain.session"/></li>
		  </ol>
		</nav>		

		
		<div class="alert alert-info">${fn:length(sessions)}&nbsp;<fmt:message key="sysadmin.maintain.session.count" /></div>

		<table class="table table-striped table-hover sessionTable" id="sessionTable" data-sortlist="[[1]]">
			<thead class="thead-light">
			<tr>
				<th><fmt:message key="sysadmin.maintain.session.login" /></th>
				<th><fmt:message key="sysadmin.maintain.session.name" /></th>
				<th><fmt:message key="sysadmin.maintain.session.access" /></th>
				<th><fmt:message key="sysadmin.maintain.session.id" /></th>
				<th data-sorter="false"  class="text-center"><fmt:message key="admin.actions" /></th>
			</tr>
			</thead>
			<tbody>
			<c:forEach items="${sessions}" var="ses">
			<tr>
				<td><c:out value="${ses.key}" /></td>
				<td><c:out value="${ses.value[0]} ${ses.value[1]}" /></td>
				<td><lams:Date value="${ses.value[2]}" timeago="true"/></td>
				<td title="<fmt:message key="sysadmin.maintain.session.created" />&nbsp;<c:out value="${ses.value[3]}" />">
					<c:out value="${ses.value[4]}" />
				</td>
				<td class="text-center">
					<csrf:form style="display: inline-block;" id="delete_${ses.key}" method="post" action="/lams/admin/sessionmaintain/delete.do">
					<input type="hidden" name="login" value="${ses.key}"/>
					<button title="<fmt:message key="sysadmin.maintain.session.delete" />" type="submit" class="btn btn-outline-danger btn-sm">
						<i class="fa fa-trash"></i></button>
					</csrf:form>
				</td>
			</tr>
			</c:forEach>
			</tbody>
		</table>
		<hr>
		<div class="pull-right">
			<a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-outline-secondary btn-sm">
				<fmt:message key="admin.cancel"/>
			</a>
		</div>		
		
	</lams:Page>
</body>
</lams:html>


