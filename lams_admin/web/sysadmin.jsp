<%@ include file="/taglibs.jsp"%>

<h1 class=no-tabs-below><fmt:message key="sysadmin.headline"/></h1>
<br />

<logic:iterate name="links" id="linkBean">
	<p>
		<a href="<bean:write name="linkBean" property="link"/>">
			<fmt:message><bean:write name="linkBean" property="name"/></fmt:message>
		</a>
	</p>
</logic:iterate>


