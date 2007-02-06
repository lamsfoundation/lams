<%@ include file="/taglibs.jsp"%>

<h2>
	<a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a>
	: <fmt:message key="admin.importv1.title" />
</h2>

<p>&nbsp;</p>

<html:form action="/importv1contentssave.do" method="post">

<h3><fmt:message key="heading.importv1.users" /></h3>
<c:out value="${msgNumUsers}"/>

<script language="javascript" type="text/JavaScript">
var numOrgs=<c:out value="${numOrgs}"/>;
function toggleOrgs(){
	for (var i=0; i<=numOrgs; i++) { 
		document.ImportV1ContentsForm.orgSids[i].checked=document.ImportV1ContentsForm.allOrgs.checked;
	}
}
function toggleSessions(){
	for (var i=0; i<=numOrgs; i++) { 
		document.ImportV1ContentsForm.sessSids[i].checked=document.ImportV1ContentsForm.allSessions.checked;
	}
}
</script>

<p>
<h3><fmt:message key="heading.importv1.account.organisations" /></h3>
<table class="alternative-color" width=100% cellspacing="0">
	<tr>
		<th><input type="checkbox" name="allOrgs" onclick="toggleOrgs();" onkeyup="toggleOrgs();" /></th>
		<th>sid</th><th><fmt:message key="admin.organisation.name" /></th><th><fmt:message key="admin.organisation.description" /></th>
	</tr>
	<logic:iterate name="orgs" id="org" indexId="index">
		<logic:equal name="org" property="accountOrganisation" value="1">
			<tr>
				<td><html:multibox name="ImportV1ContentsForm" property="orgSids" value="${org.sid}"/></td>
				<td><c:out value="${org.sid}" /></td>
				<td><c:out value="${org.name}" /></td>
				<td><c:out value="${org.description}" /></td>
			</tr>
		</logic:equal>
	</logic:iterate>
</table>

<h3><fmt:message key="heading.importv1.session.classes" /></h3>
<table class="alternative-color" width=100% cellspacing="0">
	<tr>
		<th><input type="checkbox" name="allSessions" onclick="toggleSessions();" onkeyup="toggleSessions();" /></th>
		<th>sid</th><th><fmt:message key="admin.organisation.name" /></th><th><fmt:message key="admin.organisation.description" /></th>
	</tr>
	<logic:iterate name="orgs" id="org">
		<logic:equal name="org" property="accountOrganisation" value="0">
			<tr>
				<td><html:multibox name="ImportV1ContentsForm" property="sessSids" value="${org.sid}"/></td>
				<td><c:out value="${org.sid}" /></td>
				<td><c:out value="${org.name}" /></td>
				<td><c:out value="${org.description}" /></td>
			</tr>
		</logic:equal>
	</logic:iterate>
</table>
</p>

<p>
<html:checkbox property="onlyMembers">&nbsp;Only import users that are members of organisations/session classes to be imported</html:checkbox>
<p>

<p>
	<!--<input type="submit" class="button" value="Ok"
		onclick="javascript:document.location='sysadminstart.do';" />
	-->	
	<html:submit styleClass="button"><fmt:message key="admin.save"/></html:submit>
	<html:reset styleClass="button"><fmt:message key="admin.reset"/></html:reset>
	<html:cancel styleClass="button"><fmt:message key="admin.cancel"/></html:cancel>
</p>

</html:form>