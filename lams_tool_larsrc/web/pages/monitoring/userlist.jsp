<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<c:set var="lams"><lams:LAMSURL/></c:set>
	<c:set var="tool"><lams:WebAppURL/></c:set>
	<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>

 	<!-- ********************  CSS ********************** -->
	<lams:css/>
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap.css">
	<link type="stylesheet" href="${lams}css/jquery.tablesorter.pager.css">

 	<!-- ********************  javascript ********************** -->
	<lams:JSImport src="includes/javascript/common.js" />
	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${tool}includes/javascript/rsrccommon.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script> 
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script> 
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script> 
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	    
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
</lams:head>
<body class="stripes">

<div id="content">
		
	<c:set var="title"><fmt:message key="label.monitoring.heading.access"/></c:set>
	<lams:Page type="learner" title="${title}">
	
	<table class="tablesorter">
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
						<c:out value="${user.getFullName()} (${user.loginName})" escapeXml="true"/>
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
		    	<th colspan="4" class="ts-pager form-horizontal">
		        <button type="button" class="btn btn-xs first"><i class="fa fa-step-backward"></i></button>
		        <button type="button" class="btn btn-xs prev"><i class="fa fa-backward"></i></button>
		        <span class="pagedisplay"></span> <!-- this can be any element, including an input -->
		        <button type="button" class="btn btn-xs next"><i class="fa fa-forward"></i></button>
		        <button type="button" class="btn btn-xs last"><i class="fa fa-step-forward"></i></button>
		        <select class="pagesize" title="Select page size">
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
	
	<div class="align-right small-space-top">
		<a href="javaqscript:;" onclick="window.close()" class="btn btn-default">Close</a>
	</div>

	<div id="footer"></div>

	</lams:Page>
</body>
</lams:html>