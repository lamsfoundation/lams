<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.admin.service.IImportService" %>

<script language="javascript" type="text/JavaScript">
function toggleOrgs(){
	for (var i=0; i<<c:out value="${numOrgs}"/>; i++) { 
		document.ImportV1ContentsForm.orgSids[i].checked=document.ImportV1ContentsForm.allOrgs.checked;
	}
}
function toggleSessions(){
	for (var i=0; i<<c:out value="${numSessionOrgs}"/>; i++) { 
		document.ImportV1ContentsForm.sessSids[i].checked=document.ImportV1ContentsForm.allSessions.checked;
	}
}
function loading(){
	document.getElementById('loading').style.display="";
	document.getElementById('list').style.display="none";
}
</script>

<h4><a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a></h4>
<lams:help style="no-tabs" page="<%= IImportService.IMPORTV1_HELP_PAGE %>"/>
<h1><fmt:message key="admin.importv1.title" /></h1>

<div id="loading" style="display:none">
	<h3><fmt:message key="msg.importv1.please.wait"/></h3>
	<p align="center"><img src="<lams:LAMSURL/>/images/loading.gif" alt="loading..." /></p>
</div>

<div id="list">

<html:form action="/importv1contentssave.do" method="post">

<h3><fmt:message key="heading.importv1.users" /></h3>
<c:out value="${msgNumUsers}"/>

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

<p>
<html:checkbox property="onlyMembers">&nbsp;<fmt:message key="msg.importv1.only.members"/></html:checkbox>
</p>

<p>
	<!--<input type="submit" class="button" value="Ok"
		onclick="javascript:document.location='sysadminstart.do';" />
	-->	
	<html:submit styleClass="button" onclick="loading();" onkeydown="loading();"><fmt:message key="label.import"/></html:submit>
	<html:reset styleClass="button"><fmt:message key="admin.reset"/></html:reset>
	<html:cancel styleClass="button"><fmt:message key="admin.cancel"/></html:cancel>
</p>

</html:form>

</div>