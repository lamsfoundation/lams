<%--
    Original Version: 2007 LAMS Foundation
    Updated for Blackboard 9.1 SP6 (including new bbNG tag library) 2011
    Richard Stals (www.stals.com.au)
    Edith Cowan University, Western Australia
--%>
<%--
    Process the Configuration Form for the Building Block System Administration
    Save the configuration items and display a success receipt
--%>
<%@ page import="java.util.Properties"%>
<%@ page import="blackboard.platform.BbServiceManager"%>
<%@ page import="blackboard.platform.plugin.PlugInUtil"%>
<%@ page import="blackboard.platform.plugin.PlugInException"%>
<%@ page import="org.lamsfoundation.ld.integration.blackboard.LamsPluginUtil"%>
<%@ page errorPage="/error.jsp"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<bbNG:genericPage title="LAMS Configuration" ctxId="ctx">
<%
    // SECURITY!
    // Authorise current user for System Admin (automatic redirect)
    try{
        if (!PlugInUtil.authorizeForSystemAdmin(request, response))
            return;
    } catch(PlugInException e) {
        throw new RuntimeException(e);
    }
		
    // Save the Properties
    
    // Getthe properties object
    Properties p = LamsPluginUtil.getProperties();
    
    // Get the LAMS2 Building Block properties from the request
    String lamsServerUrl = request.getParameter("lams_server_url");
    String lamsServerSkey = request.getParameter("lams_server_skey");
    String lamsServerId = request.getParameter("lams_server_id");
    String bbReqSrc =  request.getParameter("bb_req_src");
    String lamsServerTimeRefreshInterval = request.getParameter("lams_server_time_refresh_interval");

    // Save the properties to Blackboard
    p.setProperty(LamsPluginUtil.PROP_LAMS_URL, lamsServerUrl);
    p.setProperty(LamsPluginUtil.PROP_LAMS_SECRET_KEY, lamsServerSkey);
    p.setProperty(LamsPluginUtil.PROP_LAMS_SERVER_ID, lamsServerId);
    p.setProperty(LamsPluginUtil.PROP_REQ_SRC, bbReqSrc); 
    p.setProperty(LamsPluginUtil.PROP_LAMS_SERVER_TIME_REFRESH_INTERVAL, lamsServerTimeRefreshInterval); 
    
    // Persist the properties object
    LamsPluginUtil.setProperties(p); 
%>

    <%-- Breadcrumbs  It seems we need to build the full trail manually --%>
    <bbNG:breadcrumbBar environment="SYS_ADMIN">
    	<bbNG:breadcrumb href="/webapps/portal/execute/tabs/tabAction?tabType=admin" title="Administrator Panel" />
        <bbNG:breadcrumb href="/webapps/portal/admin/pa_ext_caret.jsp" title="Building Blocks" />
       	<bbNG:breadcrumb href="/webapps/blackboard/admin/manage_plugins.jsp" title="Installed Tools" />
       	<bbNG:breadcrumb title="LAMS Configuration" />
    </bbNG:breadcrumbBar>
    
    <%-- Page Header --%>
    <bbNG:pageHeader>    	
        <bbNG:pageTitleBar title="LAMS Configuration"/>
    </bbNG:pageHeader>

    <%-- Receipt --%>
    <bbNG:receipt type="SUCCESS" 
        iconUrl="/images/ci/icons/receiptsuccess_u.gif" 
        title="LAMS Configuration" 
        recallUrl="/webapps/blackboard/admin/manage_plugins.jsp">
            <h4>Sample plugin configured</h4><p>
            LAMS_SERVER_URL: <%= lamsServerUrl %><br>
            LAMS_SERVER_SKEY: <%= lamsServerSkey %><br>
            LAMS_SERVER_ID: <%= lamsServerId %><br>
            BB_REQ_SRC: <%= bbReqSrc %>
            LAMS_SERVER_TIME_REFRESH_INTERVAL: <%= lamsServerTimeRefreshInterval %>
    </bbNG:receipt>
    
</bbNG:genericPage>