<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>

<%@ page import="java.util.LinkedHashSet" %>
<%@ page import="java.util.Set" %>
<%@ page import="org.lamsfoundation.lams.tool.mc.McAppConstants"%>

<lams:html>
<lams:head>

	<title><fmt:message key="label.monitoring"/></title>
	
	<%@ include file="/common/monitoringheader.jsp"%>
	<script type="text/javascript">
	
        function init(){
            var tag = document.getElementById("currentTab");
	    	if(tag.value != "")
	    		selectTab(tag.value);
            else
                selectTab(1); //select the default tab;
        }     
        
        function doSelectTab(tabId) {
        	// start optional tab controller stuff
        	var tag = document.getElementById("currentTab");
	    	tag.value = tabId;
	    	// end optional tab controller stuff
	    	selectTab(tabId);
        } 
        
        function doSubmit(method) {
        	document.McMonitoringForm.dispatch.value=method;
        	document.McMonitoringForm.submit();
        }

		function submitMonitoringMethod(actionMethod) {
			document.McMonitoringForm.dispatch.value=actionMethod; 
			document.McMonitoringForm.submit();
		}
		
		function submitMethod(actionMethod) {
			submitMonitoringMethod(actionMethod);
		}
		
		function submitMonitoringMethod(actionMethod) {
			document.McMonitoringForm.dispatch.value=actionMethod; 
			document.McMonitoringForm.submit();
		}
		
		function submitChangeDisplayAnswers(displayAnswers, actionMethod) {
			document.McMonitoringForm.displayAnswers.value=displayAnswers; 
			submitMonitoringMethod(actionMethod);
		}
	
	</script>
	
</lams:head>
<body onLoad="init();" class="stripes">

<c:set var="title"><fmt:message key="activity.title" /></c:set>
<lams:Page title="${title}" type="navbar">

	<lams:Tabs title="${title}" control="true" helpToolSignature="<%= McAppConstants.MY_SIGNATURE %>" helpModule="monitoring">
		<lams:Tab id="1" key="label.summary" />
		<lams:Tab id="2" key="label.editActivity" />
		<lams:Tab id="3" key="label.stats" />
	</lams:Tabs>

    <html:form  action="/monitoring?validate=false" enctype="multipart/form-data" method="POST" target="_self" styleId="monitoringForm">		
		<html:hidden property="dispatch"/>
		<html:hidden property="toolContentID"/>
		<html:hidden property="httpSessionID"/>
		<html:hidden property="currentTab" styleId="currentTab" />
		<html:hidden property="contentFolderID"/>
		<html:hidden property="responseId"/>
	
		<lams:TabBodyArea>
			<lams:TabBodys>
				<lams:TabBody id="1" titleKey="label.summary" page="SummaryContent.jsp"/>
				<lams:TabBody id="2" titleKey="label.editActivity" page="Edit.jsp" />
				<lams:TabBody id="3" titleKey="label.stats" page="Stats.jsp" />
			</lams:TabBodys>
		</lams:TabBodyArea>

	</html:form>
	
	<div id="footer" />
	
</lams:Page>

</body>
</lams:html>
