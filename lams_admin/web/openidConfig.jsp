<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ include file="/taglibs.jsp"%>
<html>
	<lams:head>
		<title><fmt:message key="admin.openid.title" /></title>
		<lams:css/>
	</lams:head>
	
	<body class="stripes">

			<div id="content">
            <h4><a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a></h4>
			<h1>
				<fmt:message key="admin.openid.title" />
			</h1>
			<p>
			<c:if test="${success}">
				<p class="info">
					<fmt:message key="admin.success" />
				<p>
			</c:if>
			
			<html:form action="/openIDConfig" styleId="openIDForm" method="post" enctype="multipart/form-data">
				<html:hidden property="method" value="save" />
				<table class="alternative-color">
					<tr>
						<td width="30%"><fmt:message key="admin.openid.enabled" /></td>
						<td width="70%">
						  <html:checkbox property="openIDEnabled"></html:checkbox>
                        </td>
					</tr>
					<tr>
                        <td width="30%"><fmt:message key="admin.openid.portalurl" /></td>
                        <td width="70%">
                          <html:text property="portalURL" size="50" maxlength="255"></html:text>
                        </td>
                    </tr>
                    <tr height="20"> 
                    </tr>
                    <tr>
                        <td colspan="2" ><fmt:message key="admin.openid.trustedidps.intructions" /></td>
                    </tr>
                    <tr>
                        <td width="30%"><fmt:message key="admin.openid.trustedidps" /></td>
                        <td width="70%">
                          <html:text property="trustedIDPs" size="50" maxlength="255"></html:text>
                        </td>
                    </tr>
				</table>
				
				<html:submit styleClass="button"><fmt:message key="admin.save" /></html:submit>
		        <html:reset styleClass="button"><fmt:message key="admin.reset" /></html:reset>
			
			</html:form>
			
			</div>
			<!--closes content-->

			<div id="footer">
			</div>
			<!--closes footer-->

	</body>

</head>