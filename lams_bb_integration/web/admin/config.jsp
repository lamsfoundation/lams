<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ page isELIgnored="false" %>

<!DOCTYPE HTML>
<%--
    Configuration Form for the Building Block System Administration
    View and set the configuration items
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
    
    <%-- Properties Form --%>
    <form action="../configPlugin?method=saveConfigSettings" method="post">
    	<bbNG:dataCollection>
            <bbNG:step title="Select Plug-in">
                <bbNG:dataElement label="LAMS SERVER URL" isRequired="true" labelFor="lamsServerUrl">
                    <input type="text" name="lamsServerUrl" size="70" value="${lamsServerUrl}">
                </bbNG:dataElement>
                <bbNG:dataElement label="LAMS SERVER ID" isRequired="true" labelFor="lamsServerId">
                    <input type="text" name="lamsServerId" size="70" value="${lamsServerId}">
                </bbNG:dataElement>
                <bbNG:dataElement label="LAMS SERVER SECRET KEY" isRequired="true" labelFor="lamsSecretKey">
                    <input type="text" name="lamsSecretKey" size="70" value="${secretKey}">
                </bbNG:dataElement>
                <bbNG:dataElement label="LAMS SERVER TIME REFRESH INTERVAL (HOURS)" isRequired="true" labelFor="lamsServerTimeRefreshInterval">
                    <input type="text" name="lamsServerTimeRefreshInterval" size="20" value="${lamsServerTimeRefreshInterval}">
                </bbNG:dataElement>
				<p>For further information on how to configure these settings, see <a target="_blank" href="http://wiki.lamsfoundation.org/display/lamsdocs/Blackboard+9">this tutorial</a>.</p>
            </bbNG:step>  
            <bbNG:stepSubmit title="Submit" />
        </bbNG:dataCollection>    
    </form>
            
</bbNG:genericPage>
