<%@ include file="/taglibs.jsp"%>

<c:set var="useInternalSMTPServer"><lams:Configuration key="InternalSMTPServer"/></c:set>
<c:set var="smtpServer"><lams:Configuration key="SMTPServer"/></c:set>

<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery-1.1.4.pack.js"></script>
<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.tablesorter.pack.js"></script>
<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.tablesorter.pager.js"></script>
<script language="JavaScript" type="text/javascript">
	<!--
	jQuery(document).ready(function() {
		jQuery("table.tablesorter-admin").tablesorter({widthFixed:true, sortList:[[0,0]], headers:{0:{sorter:'integer'},5:{sorter:false}}})
			.tablesorterPager({container: jQuery("#pager")});
	});
	//-->
</script>

<h4><a href="orgmanage.do?org=1"><fmt:message key="admin.course.manage" /></a> </h4>
<h1><fmt:message key="admin.user.find"/></h1>

<logic:notEqual name="isSysadmin" value="false">
<html:form action="/usersearch.do" method="post">
<html:hidden property="searched" />

<div align="center">
	<html:errors />
	<html:messages id="results" message="true">
		<bean:write name="results" />
	</html:messages>
</div>

<p align="right">
	<input class="button" type="button" value="<fmt:message key="admin.user.create"/>" onclick=javascript:document.location='user.do?method=edit' />
</p>

<p align="right">
	<fmt:message key="admin.search" />:&nbsp;<html:text property="term" size="20" /> 
	 <fmt:message key="label.or" /> <a onclick="document.UserSearchForm.term.value='';document.UserSearchForm.submit();"><fmt:message key="label.show.all.users"/></a>
</p>

<logic:notEmpty name="userList">
<p><c:out value="${numUsers}" /></p>
</logic:notEmpty>

<table class="tablesorter-admin">
<thead>
<tr>
	<th><fmt:message key="admin.user.userid"/></th>
	<th><fmt:message key="admin.user.login"/></th>
	<th><fmt:message key="admin.user.first_name"/></th>
	<th><fmt:message key="admin.user.last_name"/></th>
	<th><fmt:message key="admin.user.email"/></th>
	<th><fmt:message key="admin.user.actions"/></th>
</tr>
</thead>
<tbody>
<script language="JavaScript" type="text/javascript">
<!--
if (jQuery.browser.msie) {
	document.write('<tr><td/><td/><td/><td/><td/><td/></tr>');
}
//-->
</script>
<logic:notEmpty name="userList">
	<c:forEach var="user" items="${userList}">
		<tr>
			<td>
				<c:out value="${user.userId}"/>
			</td>
			<td>
				<c:out value="${user.login}"/>
			</td>
			<td>
				<c:out value="${user.firstName}"/>
			</td>
			<td>
				<c:out value="${user.lastName}"/>
			</td>
			<td>
				<c:out value="${user.email}"/>
			</td>
			<td>
				[<a href="user.do?method=edit&userId=<c:out value="${user.userId}"/>"><fmt:message key="admin.edit"/></a>]
				&nbsp;
				[<a href="user.do?method=remove&userId=<c:out value="${user.userId}"/>"><fmt:message key="admin.user.delete"/></a>]
				&nbsp;
				[<a href="<lams:LAMSURL/>/loginas.do?login=<c:out value="${user.login}"/>"><fmt:message key="label.login.as"/></a>]
				
				<c:if test="${(not empty user.email) && (useInternalSMTPServer || not empty smtpServer)}">
					[<a href="emailUser.do?method=composeMail&userId=<c:out value="${user.userId}"/>"><fmt:message key="label.email"/></a>]
				</c:if>
			</td>
		</tr>
	</c:forEach>
</logic:notEmpty>
</tbody>

</table>
</html:form>

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

</logic:notEqual>