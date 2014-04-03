<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<c:set var="lams"><lams:LAMSURL/></c:set>
	<c:set var="tool"><lams:WebAppURL/></c:set>
	<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>

 	<!-- ********************  CSS ********************** -->
	<link href="<html:rewrite page='/includes/css/rsrc.css'/>" rel="stylesheet" type="text/css">
	<lams:css/>


 	<!-- ********************  javascript ********************** -->
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/rsrccommon.js'/>"></script>
	<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>    
	    
	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.tablesorter.pack.js"></script>
	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.tablesorter.pager.js"></script>
	<script language="JavaScript" type="text/javascript">
		<!--
		jQuery(document).ready(function() {
			jQuery("table.tablesorter-admin").tablesorter({widthFixed:true});
			//sort table only in case there is a data inside (it's a tablesorter bug)
			if (jQuery("table.tablesorter-admin tbody tr").length > 0) {
				jQuery("table.tablesorter-admin").tablesorterPager({container: jQuery("#pager")});
			}
		});
		//-->
	</script>
</lams:head>
<body class="stripes">

<div id="content">
		
	<h1>
		<fmt:message key="label.monitoring.heading.access"/>
	</h1>
	
	<br/>
		
	<table border="0" cellspacing="3" width="98%" class="tablesorter-admin">
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
			<script language="JavaScript" type="text/javascript">
				<!--
				if (jQuery.browser.msie) {
					document.write('<tr><td/><td/><td/><td/><td/></tr>');
				}
				//-->
			</script>
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
	</table>
	
	<br/>

	<div id="pager" class="pager">
		<form onsubmit="return false;">
			<img src="<lams:LAMSURL/>/images/first.png" class="first"/>
			<img src="<lams:LAMSURL/>/images/prev.png" class="prev">
			<input type="text" class="pagedisplay"/>
			<img src="<lams:LAMSURL/>/images/next.png" class="next">
			<img src="<lams:LAMSURL/>/images/last.png" class="last">
			<select class="pagesize">
				<option selected="selected"  value="10">10&nbsp;&nbsp;</option>
				<option value="20">20</option>
				<option value="30">30</option>
				<option value="40">40</option>
				<option value="50">50</option>
				<option value="100">100</option>
			</select>
		</form>
	</div>
	
	<div class="align-right small-space-top">
		<a href="javaqscript:;" onclick="window.close()" class="button">Close</a>
	</div>
</div>

<div id="footer"></div>
</body>
</lams:html>