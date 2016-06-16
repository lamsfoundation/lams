<%@ include file="/taglibs.jsp"%>

<p><a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>

<p><c:out value="${successful}" /></p>
<p>
<logic:iterate name="results" id="messages" indexId="index">
	<logic:notEmpty name="messages">
		Row <c:out value="${index+2}" />:
		<logic:iterate name="messages" id="message">
			<bean:write name="message" /><br />
		</logic:iterate>
	</logic:notEmpty>
</logic:iterate>
</p>
