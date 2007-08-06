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
    String lamsServerUrl = request.getParameter("lams_server_url");
    String lamsServerSkey = request.getParameter("lams_server_skey");
    String lamsServerId = request.getParameter("lams_server_id");
    String bbReqSrc =  request.getParameter("bb_req_src");

    p.setProperty(LamsPluginUtil.PROP_LAMS_URL, lamsServerUrl);
    p.setProperty(LamsPluginUtil.PROP_LAMS_SECRET_KEY, lamsServerSkey);
    p.setProperty(LamsPluginUtil.PROP_LAMS_SERVER_ID, lamsServerId);
    p.setProperty("BB_REQ_SRC", bbReqSrc); 

	LamsPluginUtil.setProperties(p); //persist the properties file
%>
  <bbUI:docTemplate title="Configure Sample Plug-in">
    <bbUI:breadcrumbBar handle="admin_plugin_manage">
        <bbUI:breadcrumb>LAMS Configuration</bbUI:breadcrumb>
    </bbUI:breadcrumbBar>
    <bbUI:receipt type="SUCCESS" 
                  iconUrl="/images/ci/icons/tools_u.gif" 
                  title="Configure LAMS Plugin." 
                  recallUrl="/webapps/blackboard/admin/manage_plugins.jsp">
        <h4>Sample plugin configured</h4><p>
        LAMS_SERVER_URL: <%= lamsServerUrl %><br>
        LAMS_SERVER_SKEY: <%= lamsServerSkey %><br>
        LAMS_SERVER_ID: <%= lamsServerId %><br>
        BB_REQ_SRC: <%= bbReqSrc %>
    </bbUI:receipt>
    <br>

  </bbUI:docTemplate>
</bbData:context>