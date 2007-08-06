<%@page import="java.util.Properties,
                blackboard.platform.BbServiceManager,
                blackboard.platform.plugin.PlugInUtil,
                blackboard.platform.security.AccessManagerService,
                org.lamsfoundation.ld.integration.blackboard.LamsPluginUtil" 
		errorPage="/error.jsp"
%>

<%@ taglib uri="/bbUI" prefix="bbUI"%>
<%@ taglib uri="/bbData" prefix="bbData"%>
<bbData:context id="ctx">

<%
    // SECURITY!
    AccessManagerService accessManager = (AccessManagerService) BbServiceManager.lookupService(AccessManagerService.class);
    if (!PlugInUtil.authorizeForSystemAdmin(request,response)){
        accessManager.sendAccessDeniedRedirect(request,response);
        return;
    }

    Properties p = LamsPluginUtil.getProperties();
	String lamsServerUrl = p.getProperty("LAMS_SERVER_URL", "http://");
	String lamsServerId = p.getProperty("LAMS_SERVER_ID", "");
	String SecretKey = p.getProperty("LAMS_SERVER_SKEY", "");
	String ReqSrc = p.getProperty("BB_REQ_SRC");
%>

<bbUI:docTemplate title="Configure LAMS">
<bbUI:breadcrumbBar handle="admin_plugin_manage">
    <bbUI:breadcrumb>LAMS Configuration</bbUI:breadcrumb>
</bbUI:breadcrumbBar>
<bbUI:titleBar iconUrl="/images/ci/icons/tools_u.gif">Configure Sample Plugin.</bbUI:titleBar>
<form action="config_proc.jsp">
  <bbUI:step title="Select Plug-in" number="1">
    <bbUI:dataElement label="LAMS SERVER URL">
      <input type="text" name="lams_server_url" size="70" value="<%=lamsServerUrl%>">
	</bbUI:dataElement>
    <bbUI:dataElement label="LAMS SERVER ID">
      <input type="text" name="lams_server_id" size="70" value="<%=lamsServerId%>">
	</bbUI:dataElement>
    <bbUI:dataElement label="LAMS SERVER SECRET KEY">
      <input type="text" name="lams_server_skey" size="70" value="<%=SecretKey%>">
	</bbUI:dataElement>
	<bbUI:dataElement label="BLACKBOARD REQUEST SRC">
      <input type="text" name="bb_req_src" size="70" value="<%=ReqSrc%>">
	</bbUI:dataElement>
  </bbUI:step>
  <bbUI:step title="Callback URL" number="2">
  	<bbUI:instructions>
		Copy and paste the user data callback URL into LAMS <BR>
		<B>USER DATA CALLBACK URL</B>: http://<%=request.getServerName()%><%=request.getContextPath()%>/UserData?uid=%username%&ts=%timestamp%&hash=%hash%
	</bbUI:instructions>
  </bbUI:step>
  <bbUI:stepSubmit title="Submit" number="3" />
  
</form>

</bbUI:docTemplate>
</bbData:context>