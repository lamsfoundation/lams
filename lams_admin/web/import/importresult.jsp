<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<h2><fmt:message key="admin.user.import" /></h2>

<p>
<c:out value="${successful}" /><br />
<logic:iterate name="results" id="messages" indexId="index">
	<logic:notEmpty name="messages">
		Row <c:out value="${index+2}" />:
		<logic:iterate name="messages" id="message">
			<bean:write name="message" /><br />
		</logic:iterate>
	</logic:notEmpty>
</logic:iterate>
</p>

<p>
	<input type="submit" class="button" value="Ok"
		onclick="javascript:document.location='usersearch.do';" />
</p>