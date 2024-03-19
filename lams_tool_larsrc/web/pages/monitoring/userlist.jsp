<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="uniqueId"><%=java.util.UUID.randomUUID()%></c:set>
<c:set var="pagerClass">ts-pager</c:set>
<c:set var="numColumns">4</c:set>

<c:set var="title"><fmt:message key="label.monitoring.heading.access" /></c:set>
<lams:PageMonitor title="${title}" hideHeader="true">

 	<!-- ********************  CSS ********************** -->
	<link type="text/css" href="${lams}css/jquery-ui-bootstrap-theme5.css" rel="stylesheet">
	<link type="text/css" href="${lams}css/jquery.tablesorter.theme.bootstrap5.css" rel="stylesheet">
	<link type="text/css" href="${lams}css/jquery.tablesorter.pager5.css" rel="stylesheet">

 	<!-- ********************  javascript ********************** -->
	<script type="text/javascript" src="${tool}includes/javascript/rsrccommon.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script> 
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script> 
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script> 
<%-- 	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.tablesorter.pack.js"></script> --%>
	<script language="JavaScript" type="text/javascript">
		jQuery(document).ready(function() {
			jQuery("table.tablesorter").tablesorter({
				theme : "bootstrap",
				widthFixed:true,
			    headerTemplate : '{content} {icon}', 
			    widgets : [ "uitheme"]
			})
			.tablesorterPager({
				savePages: false,
	            container: $(this).find(".ts-pager"),
			    output: '{startRow} - {endRow} / {filteredRows} ({totalRows})',
	            cssPageDisplay: '.pagedisplay',
	            cssPageSize: '.pagesize',
	            cssDisabled: 'disabled'
			 });
			
			//sort table only in case there is a data inside (it's a tablesorter bug)
			if (jQuery("table.tablesorter tbody tr").length > 0) {
				jQuery("table.tablesorter").tablesorterPager({container: jQuery("#pager")});
			}
		});
	</script>

	<div class="container-main">
		<h1 class="mb-4">
			${title}
		</h1>
	
		<table class="tablesorter shadow rounded-4">
			<thead>
				<tr>
					<th>
						<fmt:message key="monitoring.label.user.name" />
					</th>		
					<th>
						<fmt:message key="monitoring.label.access.time" />
					</th>
					<th>
						<fmt:message key="monitoring.label.complete.time" />
					</th>
					<th>
						<fmt:message key="monitoring.label.time.taken" />
					</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="user" items="${userList}">
					<tr>
						<td>
							<c:out value="${user.firstName},${user.lastName} (${user.loginName})" escapeXml="true"/>
						</td>			
						<td>
							<lams:Date value="${user.accessDate}"/>
						</td>
						<td>
							<lams:Date value="${user.completeDate}"/>
						</td>
						<td>
							<fmt:formatDate value="${user.timeTaken}" pattern="HH:mm:ss" timeZone="GMT" />
						</td>
					</tr>
				</c:forEach>
			</tbody>
			<tfoot>
				<tr>
					<th colspan="${numColumns}" class="${pagerClass} text-center">
						<button type="button" class="btn btn-secondary btn-sm rounded first" title="First">
							<i class="fa fa-step-backward"></i>
						</button>
						<button type="button" class="btn btn-secondary btn-sm rounded prev" title="Previous">
							<i class="fa fa-backward"></i>
						</button> 
						<span class="pagedisplay"></span> <!-- this can be any element, including an input -->
						<button type="button" class="btn btn-secondary btn-sm rounded next" title="Next">
							<i class="fa fa-forward"></i>
						</button>
						<button type="button" class="btn btn-secondary btn-sm rounded last" title="Last">
							<i class="fa fa-step-forward"></i>
						</button>
						
						<label for="${uniqueId}" class="visually-hidden">Select page size</label>
						<select class="pagesize form-select" title="Select page size" id="${uniqueId}">
							<option selected="selected" value="10">10</option>
							<option value="20">20</option>
							<option value="30">30</option>
							<option value="40">40</option>
							<option value="50">50</option>
							<option value="100">100</option>
						</select>
					</th>
				</tr>
			</tfoot>
		</table>
	
		<div class="activity-bottom-buttons">
			<button type="button" onclick="window.close()" class="btn btn-primary">
				<i class="fa-regular fa-circle-xmark me-1"></i>
				<fmt:message key="label.close" />
			</button>
		</div>
	</div>
</lams:PageMonitor>
