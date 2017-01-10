<%--
    Original Version: 2007 LAMS Foundation
    Updated for Blackboard 9.1 SP6 (including new bbNG tag library) 2011
    Richard Stals (www.stals.com.au)
    Edith Cowan University, Western Australia
--%>
<%--
    Configuration Form for the Building Block System Administration
    View and set the configuration items
--%>
<%@ page import="java.util.Properties"%>
<%@ page import="blackboard.platform.BbServiceManager"%>
<%@ page import="blackboard.platform.plugin.PlugInUtil"%>
<%@ page import="blackboard.platform.plugin.PlugInException"%>
<%@ page import="org.lamsfoundation.ld.integration.util.LamsPluginUtil"%>
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
    
    // Get the LAMS2 Building Block properties from Blackboard (if set)
    Properties p = LamsPluginUtil.getProperties();
    String lamsServerUrl = p.getProperty("LAMS_SERVER_URL", "http://");
    String lamsServerId = p.getProperty("LAMS_SERVER_ID", "");
    String SecretKey = p.getProperty("LAMS_SERVER_SKEY", "");
    String ReqSrc = p.getProperty("BB_REQ_SRC");
    String lamsServerTimeRefreshInterval = p.getProperty("LAMS_SERVER_TIME_REFRESH_INTERVAL");
    String lamsAltServerUrl = p.getProperty("LAMS_ALT_SERVER_URL", "https://");
    //*NTU* added for i-NTULearn project
    String idpServer = p.getProperty("IDP_SERVER", "");

    //Add port to the url if the port is in the blackboard url
    int bbport = request.getServerPort();
    String bbportstr = bbport != 0 ? ":" + bbport : "";
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
    
    <%-- Properties Form --%>
    <form action="config_proc.jsp">
    	<bbNG:dataCollection>
            <bbNG:step title="Select Plug-in">
                <bbNG:dataElement label="LAMS SERVER URL" isRequired="true" labelFor="lams_server_url">
                    <input type="text" name="lams_server_url" size="70" value="<%=lamsServerUrl%>">
                </bbNG:dataElement>
                <bbNG:dataElement label="LAMS SERVER ID" isRequired="true" labelFor="lams_server_id">
                    <input type="text" name="lams_server_id" size="70" value="<%=lamsServerId%>">
                </bbNG:dataElement>
                <bbNG:dataElement label="LAMS SERVER SECRET KEY" isRequired="true" labelFor="lams_server_skey">
                    <input type="text" name="lams_server_skey" size="70" value="<%=SecretKey%>">
                </bbNG:dataElement>
                <bbNG:dataElement label="BLACKBOARD REQUEST SRC" isRequired="true" labelFor="bb_req_src">
                    <input type="text" name="bb_req_src" size="70" value="<%=ReqSrc%>">
                </bbNG:dataElement>
                <bbNG:dataElement label="LAMS SERVER TIME REFRESH INTERVAL (HOURS)" isRequired="true" labelFor="lams_server_time_refresh_interval">
                    <input type="text" name="lams_server_time_refresh_interval" size="20" value="<%=lamsServerTimeRefreshInterval%>">
                </bbNG:dataElement>                
                <bbNG:dataElement label="LAMS ALTERNATIVE SERVER URL" isRequired="false" labelFor="lams_alt_server_url">
                    <input type="text" name="lams_alt_server_url"  size="70" value="<%=lamsAltServerUrl%>">
                </bbNG:dataElement>
                <%--*NTU* added for i-NTULearn project--%>
                <bbNG:dataElement label="IdP SERVER" isRequired="false" labelFor="idp_server">
                    <input type="text" name="idp_server" size="70" value="<%=idpServer%>">
                </bbNG:dataElement>             
				<p>For further information on how to configure these settings, see <a target="_blank" href="http://wiki.lamsfoundation.org/display/lamsdocs/Blackboard+9">this tutorial</a>.</p>
            </bbNG:step>  
            <bbNG:stepSubmit title="Submit" />
        </bbNG:dataCollection>    
    </form>
            
</bbNG:genericPage>
