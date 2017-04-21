<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ page isELIgnored="false" %>

<!DOCTYPE HTML>
<%--
    Original Version: 2007 LAMS Foundation
    Updated for Blackboard 9.1 SP6 (including new bbNG tag library) 2011
    Richard Stals (www.stals.com.au)
    Edith Cowan University, Western Australia
--%>
<%@ page errorPage="/error.jsp"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<%@ taglib uri="/tags-core" prefix="c"%>
<bbNG:genericPage title="LAMS Learning Activity Management System" ctxId="ctx">

    <%-- Breadcrumbs --%>
    <bbNG:breadcrumbBar environment="COURSE" isContent="true">
        <bbNG:breadcrumb title="Start A LAMS Lesson" />
    </bbNG:breadcrumbBar>

    <%-- Page Header --%>
    <bbNG:pageHeader>
        <bbNG:pageTitleBar title="Start A LAMS Lesson"/>
    </bbNG:pageHeader>

    <%-- Receipt --%>
    <bbNG:receipt type="SUCCESS"
        iconUrl="/images/ci/icons/receiptsuccess_u.gif"
        title="Start A LAMS Lesson"
        recallUrl="${returnUrl}">
        	<!-- LESSON START SUCCESS (DO NOT REMOVE: String is used to check for completion) --> 
            Content successfully added.
    </bbNG:receipt>

</bbNG:genericPage>
