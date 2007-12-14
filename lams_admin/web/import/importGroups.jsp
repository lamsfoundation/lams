<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.admin.service.IImportService" %>
<%@ page import="java.util.List" %>
<%@ page import="org.lamsfoundation.lams.usermanagement.OrganisationType" %>

<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/prototype.js"></script>
<script language="javascript" type="text/JavaScript">
function loading(){
	document.getElementById('loading').style.display="";
	document.getElementById('main-page').style.display="none";
}
</script>

<h4><a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a></h4>
<lams:help style="no-tabs" page="<%= IImportService.IMPORT_GROUPS_HELP_PAGE %>"/>
<h1><fmt:message key="sysadmin.import.groups.title" /></h1>

<div id="loading" style="display:none">
	<h3><fmt:message key="msg.please.wait"/></h3>
	<p align="center"><img src="<lams:LAMSURL/>/images/loading.gif" alt="loading..." /></p>
</div>

<div id="main-page">

<logic:notEmpty name="results">
	<h3><fmt:message key="heading.import.results"/></h3>
	<table cellspacing="5" cellpadding="5">
		<tr><th width="115" align="right"><fmt:message key="table.heading.organisation.id"/></th><th><fmt:message key="admin.organisation.name"/></th></tr>
		<%
			List results = (List)request.getAttribute("results");
			for (int i=0; i<results.size(); i++) {
				out.print("<tr>");
				List rowResult = (List)results.get(i);
				if (rowResult != null && rowResult.size() >= 4) {
					if (rowResult.get(3).equals(OrganisationType.COURSE_TYPE.toString())) {
						out.print("<th align=\"right\">"+rowResult.get(0)+"</th>");
						out.print("<th>"+rowResult.get(1)+"</th>");
					} else if (rowResult.get(3).equals(OrganisationType.CLASS_TYPE.toString())) {
						out.print("<td align=\"right\">"+rowResult.get(0)+"</td>");
						out.print("<td>"+rowResult.get(1)+"</td>");
					}
				} else { // it's an error message
					out.print("<td colspan=\"2\">");
					for (int j=0; j<rowResult.size(); j++) {
						out.println(rowResult.get(j)+"<br/>");
					}
					out.print("</td>");
				}
				out.println("</tr>");
			}
		%>
	</table>
	<p>&nbsp;</p>
	<hr />
</logic:notEmpty>

<p><fmt:message key="import.groups.intro"/></p>
<p>
<ul>
	<li>
		<fmt:message key="msg.import.1"/>
	</li>
	<li>
		<fmt:message key="import.groups.instructions"/>
	</li>
	<li>
		<fmt:message key="import.groups.download"/>
		<ul><li><p><a href="file/lams_groups_template.xls">lams_groups_template.xls</a></p></li></ul>
	</li>
</ul>
</p>
<p><fmt:message key="msg.import.conclusion"/></p>

<html:form action="/importgroups.do" method="post" enctype="multipart/form-data" onsubmit="loading();">
<html:hidden property="orgId" />

<table>
	<tr>
		<td align="right"><fmt:message key="label.excel.spreadsheet" />:&nbsp;</td>
		<td><html:file property="file" /></td>
	</tr>
</table>
<p align="center">
<html:submit styleClass="button"><fmt:message key="label.import"/></html:submit> &nbsp; 	
<html:cancel styleClass="button"><fmt:message key="admin.cancel"/></html:cancel>
</p>

</html:form>

</div>