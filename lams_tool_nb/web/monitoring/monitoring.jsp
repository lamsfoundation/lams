<!DOCTYPE html>
        

<%@ include file="/includes/taglibs.jsp"%>

<%@ page import="java.util.HashMap"%>
<%@ page import="org.lamsfoundation.lams.tool.noticeboard.web.NbMonitoringAction"%>
<%@ page import="org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<lams:html>
<lams:head>
	<lams:css/>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.tabcontroller.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<title><fmt:message key="activity.title" /></title>

	<script type="text/javascript">

		 function init(){
            var tag = document.getElementById("currentTab");
	    	if(tag != null && tag.value != "") {
	    		selectTab(tag.value);
	    	}
            else {
                selectTab(1); //select the default tab;
            }
        }   

	</script>
</lams:head>

<body class="stripes" onLoad='init()'>

		<html:form action="/monitoring" target="_self">

			<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" scope="request"/>
			<html:hidden property="method" />
			<html:hidden property="currentTab" styleId="currentTab" />

			<c:set var="monitoringURL">
				<html:rewrite page="/monitoring.do" />
			</c:set>
			
			<c:set var="title"><fmt:message key="activity.title" /></c:set>
			<lams:Page title="${title}" type="navbar">

		 	<lams:Tabs title="${title}" helpToolSignature="<%= NoticeboardConstants.TOOL_SIGNATURE %>" helpModule="monitoring">
				<lams:Tab id="1" key="titleHeading.summary" />
				<lams:Tab id="2" key="titleHeading.editActivity" />
				<lams:Tab id="3" key="titleHeading.statistics" />
			</lams:Tabs>
		
			<lams:TabBodyArea>
			<lams:TabBodys>
				<lams:TabBody id="1" titleKey="titleHeading.summary" page="m_Summary.jsp" /> 
				<lams:TabBody id="2" titleKey="titleHeading.editActivity" page="m_EditActivity.jsp" />
  				<lams:TabBody id="3" titleKey="titleHeading.statistics" page="m_Statistics.jsp" />
 			</lams:TabBodys>
			</lams:TabBodyArea>
			
			<div id="footer" />
			
			</lams:Page>

		</html:form>

</body>


</lams:html>

