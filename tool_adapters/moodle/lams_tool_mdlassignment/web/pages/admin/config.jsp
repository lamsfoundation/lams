<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<html>
	<lams:head>
		<title><fmt:message key="pageTitle.admin" /></title>
		<lams:css/>
	</lams:head>
	
	<body class="stripes">

			<div id="content">

			<h1>
				<fmt:message key="pageTitle.admin" />
			</h1>
			
			<a href="<lams:LAMSURL/>/admin/sysadminstart.do"><fmt:message key="admin.return" /></a>

			<p>
			<c:choose>
			<c:when test="${error}">
				<p class="warning">
					<fmt:message key="admin.formError" />
				</p>
			</c:when>
			</c:choose>
			<html:form action="/mdasgm10admin" styleId="mdasgm10AdminForm" method="post" enctype="multipart/form-data">
				
				<html:hidden property="dispatch" value="saveContent" />
				
				<h3>
                        <fmt:message key="admin.enableForIntegratedServers" />
                </h3>
                <logic:iterate name="mdasgm10AdminForm" property="mappableServers" id="mappableServer">
	                <html:multibox property="mappedServers">
	                	<bean:write name="mappableServer"/>
	                </html:multibox>
	                <bean:write name="mappableServer"/><br/>
                </logic:iterate>

                <br />
				
				<html:submit><fmt:message key="button.save" /></html:submit>
			
			</html:form>
			
			
				
			</div>
			<!--closes content-->

			<div id="footer">
			</div>
			<!--closes footer-->

	</body>

</head>
