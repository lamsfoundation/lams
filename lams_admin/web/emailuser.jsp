<%@ include file="/taglibs.jsp"%>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/common.js"></script>

<h4><a href="orgmanage.do?org=1"><fmt:message key="admin.course.manage" /></a> </h4>
<h1><fmt:message key="admin.email.compose.mail"/></h1>

<div align="center">
	<html:errors />
	<html:messages id="results" message="true">
		<bean:write name="results" />
	</html:messages>
</div>

<logic:notEqual name="isSysadmin" value="false">
<html:form action="/emailUser.do?method=send" method="post" styleId="emailUserForm">
	<html:hidden property="userId" value="${user.userId}"/>

	<table class="tablesorter-admin space-top">
	
		<tr>
			<td style="width: 70px;">
				<fmt:message key="admin.email.to" /> 
			</td>
			<td style="text-align: left;">
				${user.firstName} ${user.lastName} &lt;${user.email}&gt;
			</td>		
		</tr>
		
		<tr>
			<td>
				<fmt:message key="admin.email.subject" /> 
			</td>
			<td style="text-align: left;">
				<input type="text" name="subject" value="" style="width:95%;">
			</td>		
		</tr>
		
		<tr>
			<td colspan="2">
				<lams:STRUTS-textarea property="body" rows="16" value="" style="width:95%;" />
			</td>
		</tr>
	
	</table>
	
	<p align="right">
		<a href="javascript:;" onclick="document.getElementById('emailUserForm').submit();" class="button">
			<fmt:message key="admin.email.send" /> 
		</a>
		<a href="javascript:;" onclick="document.location.href ='<c:url value="/usersearch.do"/>';" class="button space-left">
			<fmt:message key="admin.cancel" />
		</a>
	</p>
	
</html:form>

<br/>

</logic:notEqual>
