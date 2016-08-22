<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>

<%@ page import="java.util.LinkedHashSet" %>
<%@ page import="java.util.Set" %>
<%@ page import="org.lamsfoundation.lams.tool.vote.VoteAppConstants"%>

<lams:html>
	<lams:head>
	
	<title> <fmt:message key="label.monitoring"/> </title>
	<%@ include file="/common/monitorheader.jsp"%>

	<script type="text/javascript">
	
	   	var imgRoot="${lams}images/";
	    var themeName="aqua";
	
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

	    	//for statistic page change:
    		if(tabId == 3) doStatistic();
        } 
        
		function doStatistic(){
			var url = '<c:url value="monitoring.do"/>?dispatch=statistics&toolContentID=${toolContentID}';
			$.ajaxSetup({ cache: true });
			$("#statisticArea").load(url);
		}

        function doSubmit(method) {
        	document.VoteMonitoringForm.dispatch.value=method;
        	document.VoteMonitoringForm.submit();
        }

		function submitMonitoringMethod(actionMethod) {
			document.VoteMonitoringForm.dispatch.value=actionMethod; 
			document.VoteMonitoringForm.submit();
		}
		
		function submitAuthoringMethod(actionMethod) {
			document.VoteAuthoringForm.dispatch.value=actionMethod; 
			document.VoteAuthoringForm.submit();
		}
		
		function submitModifyQuestion(questionIndexValue, actionMethod) {
			document.VoteMonitoringForm.questionIndex.value=questionIndexValue; 
			submitMethod(actionMethod);
		}
		
		function submitModifyMonitoringNomination(questionIndexValue, actionMethod) {
			document.VoteMonitoringForm.questionIndex.value=questionIndexValue; 
			submitMethod(actionMethod);
		}
		
		
		function submitEditResponse(responseId, actionMethod) {
			document.VoteMonitoringForm.responseId.value=responseId; 
			submitMethod(actionMethod);
		}
		
		function submitMethod(actionMethod) {
			submitMonitoringMethod(actionMethod);
		}
		
		function deleteOption(optIndex, actionMethod) {
			document.VoteMonitoringForm.optIndex.value=optIndex; 
			submitMethod(actionMethod);
		}
		
		function submitModifyOption(optionIndexValue, actionMethod) {
			document.VoteMonitoringForm.optIndex.value=optionIndexValue; 
			submitMethod(actionMethod);
		}

		function submitModifyNomination(optionIndexValue, actionMethod) {
			document.VoteMonitoringForm.optIndex.value=optionIndexValue; 
			submitMethod(actionMethod);
		}
		
	</script>
	
</lams:head>
<body class="stripes" onLoad="init();">

    <html:form  action="/monitoring?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
		<html:hidden property="dispatch"/>
		<html:hidden property="toolContentID"/>
		<html:hidden property="httpSessionID"/>		
		<html:hidden property="currentTab" styleId="currentTab" />
		<html:hidden property="contentFolderID"/>
		<html:hidden property="responseId"/>	 
		<html:hidden property="currentUid"/>

        <c:set var="title"><fmt:message key="activity.title" /></c:set>
        <lams:Page title="${title}" type="navbar">

         <lams:Tabs control="true" title="${title}" helpToolSignature="<%= VoteAppConstants.MY_SIGNATURE %>" helpModule="monitoring">
            <lams:Tab id="1" key="label.summary" />
            <lams:Tab id="2" key="label.editActivity" />
            <lams:Tab id="3" key="label.stats" />
         </lams:Tabs>
   
        <lams:TabBodyArea>
        <lams:TabBodys>
            <lams:TabBody id="1" titleKey="label.summary" page="SummaryContent.jsp"/>
	 		<lams:TabBody id="2" titleKey="label.editActivity" page="Edit.jsp" />
			<lams:TabBody id="3" titleKey="label.stats" page="Stats.jsp" />
        </lams:TabBodys>
        </lams:TabBodyArea>
       
        <div id="footer"></div>
       
        </lams:Page>
            		
	</html:form>
	
</body>
</lams:html>
