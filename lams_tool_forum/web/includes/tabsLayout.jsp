<%@ include file="/includes/taglibs.jsp" %>

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
		page='<%= "/images/" + pIcon %>' width="0" height="30" align="absmiddle" /><b class="subTitle"><bean:message key="<%=pTitleKey %>" /></b></td>
	</logic:notEmpty>
</tr>
</table>
</logic:notEmpty>
<%--
	Tabs Layout . 
	This layout allows to render several Tiles in a tabs fashion.
	@param tabList A list of available tabs. 
		We use MenuItem to carry data (name, body, icon, ...)
	@param selectedIndex Index of default selected tab
	@param parameterName Name of parameter carrying selected info in HTTP request.
--%> 
<%-- 
	Use Tiles attributes, and declare them as page variable. 
	These attribute must be passed to the Tile. 
--%>
<tiles:useAttribute name="actionName" classname="java.lang.String" />
<tiles:useAttribute name="parameterName" classname="java.lang.String" />
<tiles:useAttribute id="selectedIndexStr" name="selectedIndex" ignore="true" classname="java.lang.String" />
<%--
    <bean:parameter name="customerId" id="customerId"  />
--%>
<tiles:useAttribute name="tabList" classname="java.util.List" />
<% 
	String selectedTab = "selectedTab";
	String notSelectedTab = "tab";
    String qString = "";
	//String qString = "customerId=" + customerId;
	int index = 0;
	// Loop index
	int selectedIndex = 0;
	// Check if selected come from request parameter
	try {
		selectedIndex = Integer.parseInt(selectedIndexStr);
		selectedIndex = Integer.parseInt(request.getParameter(parameterName));
	} catch(java.lang.NumberFormatException ex ) {
		// do nothing 
	}
	// Check selectedIndex bounds
	if (selectedIndex < 0 || selectedIndex >= tabList.size()) selectedIndex = 0; 
	String selectedBody = 
		((org.apache.struts.tiles.beans.MenuItem)tabList.get(selectedIndex)).getLink();
	// Selected body
%>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
<%-- Draw tabs --%>
<tr>
	<td width="10">&nbsp;</td>
	<td>
	<table border="0" cellspacing="0" cellpadding="5">
		<tr>
		<logic:iterate id="tab" name="tabList" type="org.apache.struts.tiles.beans.MenuItem" >
		<% 
			// compute href
			String href = actionName + "?" + parameterName + "=" + index ; //+ "&" + qString ;
			String styleClass = notSelectedTab;
			if (index == selectedIndex) {
				selectedBody = tab.getLink() + "?" + qString;
				styleClass = selectedTab;
			}
			// enf if
			index++;
		%>
		<td class="<%=styleClass%>"><a href="<%=href%>" /><%=tab.getValue()%></a></td>
		<td width="1" ></td>
		</logic:iterate>
		</tr>
	</table>
	</td>
	<td width="10" >&nbsp;</td>
</tr>
<tr>
	<td height="5" class="<%=selectedTab%>" colspan="3" >&nbsp;</td>
</tr>
<%-- Draw body --%>
<tr>
	<td width="10" class="<%=selectedTab%>">&nbsp;</td>
	<td>
		<tiles:insert name="<%=selectedBody%>" flush="true" />
	</td>
	<td width="10" class="<%=selectedTab%>">&nbsp;</td>
</tr>
<tr>
	<td height="5" class="<%=selectedTab%>" colspan="3" >&nbsp;</td>
</tr>
</table>
<%--
<%= selectedBody %>
--%>
<tiles:insert attribute="footer"/>
</body>
</html>
