<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<html>
<tiles:insert attribute="header"/>
<body>
<tiles:useAttribute name="pageTitleKey" scope="request"/>
<bean:define name="pageTitleKey" id="pTitleKey" type="String" />
<tiles:useAttribute name="pageIcon" scope="request"/>
<bean:define name="pageIcon" id="pIcon" type="String" />
<logic:notEmpty name="pTitleKey">
<table border="0" cellpadding="0" cellspacing="0">
<tr>
	<logic:notEmpty name="pIcon">
	<td colspan="2" bgcolor="#F9F9F5" background="<html:rewrite page="/images/back_subSection.gif" />"><html:img 
		page='<%= "/images/" + pIcon %>' width="25" height="25" align="absmiddle" /><b class="subTitle"><bean:message key="<%=pTitleKey %>" /></b></td>
	</logic:notEmpty>
</tr>
</table>
</logic:notEmpty>
<tiles:insert attribute="body"/>
<tiles:insert attribute="footer"/>
</body>
</html>
