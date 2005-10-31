<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="tags-tiles" prefix="tiles"%>
<html>
<tiles:insert attribute="header" />
<body>

	<tiles:useAttribute name="pageTitleKey" scope="request" />
	<bean:define name="pageTitleKey" id="pTitleKey" type="String" />
	<logic:notEmpty name="pTitleKey">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td><b class="subTitle"><bean:message key="<%=pTitleKey %>" /></b></td>
			</tr>
		</table>
	</logic:notEmpty>

	<tiles:insert attribute="body" />
	<tiles:insert attribute="footer" />
</body>
</html>
