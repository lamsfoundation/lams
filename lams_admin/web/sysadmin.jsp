<%@ include file="/taglibs.jsp"%>

<div class="list-group">
	<logic:iterate name="links" id="linkBean">
	<span class="list-group-item">
		<a href="<bean:write name="linkBean" property="link"/>">
				<fmt:message><bean:write name="linkBean" property="name"/></fmt:message>
			</a>
	</span>
	</logic:iterate>
</div>

