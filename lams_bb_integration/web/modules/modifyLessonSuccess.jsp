<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ page isELIgnored="false" %>

<!DOCTYPE HTML>
<%--
    Step 2 For Modifing an existing LAMS Lesson
    Process the data and persist to Blackboard

    Step 1 - modify.jsp
    Step 2 - modify_proc.jsp
--%>
<%@ page errorPage="/error.jsp"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<%@ taglib uri="/tags-core" prefix="c"%>
<bbNG:genericPage title="Modify A LAMS Lesson" ctxId="ctx">

    <%-- Breadcrumbs --%>
    <bbNG:breadcrumbBar environment="CTRL_PANEL" isContent="true">
        <bbNG:breadcrumb title="Modify A LAMS Lesson" />
    </bbNG:breadcrumbBar>
    
    <%-- Page Header --%>    
    <bbNG:pageHeader>    	
        <bbNG:pageTitleBar title="Modify A LAMS Lesson"/>
    </bbNG:pageHeader>

    <%-- Receipt --%>
    <bbNG:receipt type="SUCCESS" 
        iconUrl="/images/ci/icons/receiptsuccess_u.gif" 
        title="Content Modified" 
        recallUrl="${strReturnUrl}">
            Content successfully modified.
    </bbNG:receipt>

</bbNG:genericPage>