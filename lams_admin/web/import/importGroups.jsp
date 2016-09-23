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

<p><a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>

<div id="loading" style="display:none">
	<h4><fmt:message key="msg.please.wait"/></h4>
	<p class="text-center"><i class="fa fa-refresh fa-spin fa-2x fa-fw"></i></p>
</div>

<div id="main-page">

<logic:notEmpty name="results">
	<h4><fmt:message key="heading.import.results"/></h4>
	<table cellspacing="5" cellpadding="5">
		<tr><th width="115" align="right"><fmt:message key="table.heading.organisation.id"/></th><th><fmt:message key="admin.organisation.name"/></th></tr>
		<%
			List results = (List)request.getAttribute("results");
			for (int i=0; i<results.size(); i++) {
				out.print("<tr>");
				List rowResult = (List)results.get(i);
				if (rowResult != null && rowResult.size() >= 4) {
					if (rowResult.get(3).equals(OrganisationType.COURSE_TYPE.toString())) {
						out.print("<th>"+rowResult.get(0)+"</th>");
						out.print("<th>"+rowResult.get(1)+"</th>");
					} else if (rowResult.get(3).equals(OrganisationType.CLASS_TYPE.toString())) {
						out.print("<td>"+rowResult.get(0)+"</td>");
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
		<td><html:file property="file" styleClass="form-control"/></td>
	</tr>
</table>
<div class="pull-right">
<html:cancel styleId="cancelButton" styleClass="btn btn-default"><fmt:message key="admin.cancel"/></html:cancel>
<html:submit styleId="importButton" styleClass="btn btn-primary loffset5"><fmt:message key="label.import"/></html:submit> &nbsp; 	
</div>

</html:form>

</div>
