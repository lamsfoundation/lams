<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/includes/taglibs.jsp"%>

<%@ page import="java.util.HashMap"%>
<%@ page import="org.lamsfoundation.lams.tool.noticeboard.web.NbMonitoringAction"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<html:html>
<head>
	<lams:css/>
	<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<title><fmt:message key="activity.title" /></title>

	<script type="text/javascript">

		var imgRoot="${lams}images/";
	    var themeName="aqua";
	
		 function init(){
            initTabSize(4);
            var tag = document.getElementById("currentTab");
	    	if(tag != null && tag.value != "") {
	    		selectTab(tag.value);
	    	}
            else {
                selectTab(1); //select the default tab;
            }
        }   

	</script>
</head>

<body onLoad='init()'>

	<div id="page">
			<h1>
				<fmt:message key="activity.title" />
			</h1>
			
			<div id="header">
				<lams:Tabs>
					<lams:Tab id="<%=NbMonitoringAction.SUMMARY_TABID%>" key="titleHeading.summary" />
					<lams:Tab id="<%=NbMonitoringAction.INSTRUCTIONS_TABID%>" key="titleHeading.instructions" />
					<lams:Tab id="<%=NbMonitoringAction.EDITACTIVITY_TABID%>" key="titleHeading.editActivity" />
					<lams:Tab id="<%=NbMonitoringAction.STATISTICS_TABID%>" key="titleHeading.statistics" />
				</lams:Tabs>
			</div>

			<div id="content">
			
				<html:form action="/monitoring" target="_self">

				<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" scope="request"/>
				<html:hidden property="method" />
	
				<c:set var="monitoringURL">
					<html:rewrite page="/monitoring.do" />
				</c:set>

				<lams:TabBody id="1" titleKey="titleHeading.summary" page="m_Summary.jsp" />
				<lams:TabBody id="2" titleKey="titleHeading.instructions" page="m_Instructions.jsp" />
				<lams:TabBody id="3" titleKey="titleHeading.editActivity" page="m_EditActivity.jsp" />
				<lams:TabBody id="4" titleKey="titleHeading.statistics" page="m_Statistics.jsp" />
			</div>
			
			<div id="footer" />
		</html:form>
	</div>

</body>


</html:html>

