<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>

 <!-- ********************  CSS ********************** -->
<lams:css/>

<!-- ********************  javascript ********************** -->
<lams:JSImport src="includes/javascript/common.js" />
<lams:JSImport src="includes/javascript/spreadsheetcommon.js" relative="true" />
<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/bootstrap.tabcontroller.js"></script>

<title><fmt:message key="activity.title" /></title>	