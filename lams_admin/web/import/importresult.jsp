<%@ include file="/taglibs.jsp"%>

<h4><a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a></h4>
<h1><fmt:message key="admin.user.import" /></h1>

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

<p>&nbsp;</p>

<p>
	<input type="submit" class="button" value="Ok"
		onclick="javascript:document.location='sysadminstart.do';" />
</p>