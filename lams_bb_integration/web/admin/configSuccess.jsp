<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ page isELIgnored="false" %>

<!DOCTYPE HTML>
<%--
    Process the Configuration Form for the Building Block System Administration
    Save the configuration items and display a success receipt
--%>
<%@ page errorPage="/error.jsp"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<%@ taglib uri="/tags-core" prefix="c"%>
<bbNG:genericPage title="LAMS Configuration" ctxId="ctx">

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
        iconUrl="/includes/images/ci/icons/receiptsuccess_u.gif" 
        title="LAMS Configuration" 
        recallUrl="/webapps/blackboard/admin/manage_plugins.jsp">
            <h4>Sample plugin configured</h4><p>
            LAMS_SERVER_URL: ${param.lamsServerUrl}<br>
            LAMS_SERVER_SKEY: ${param.lamsSecretKey}<br>
            LAMS_SERVER_ID: ${param.lamsServerId}<br>
            LAMS_SERVER_TIME_REFRESH_INTERVAL: ${param.lamsServerTimeRefreshInterval}
    </bbNG:receipt>
    
</bbNG:genericPage>