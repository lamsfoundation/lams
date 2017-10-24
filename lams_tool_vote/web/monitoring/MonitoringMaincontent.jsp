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
			var url = 'statistics.do?toolContentID=${toolContentID}';
			$.ajaxSetup({ cache: true });
			$("#statisticArea").load(url);
		}

        function doSubmit(method) {
        	document.forms.voteMonitoringForm.action=method + '.do';
        	document.forms.voteMonitoringForm.submit();
        }

		function submitMonitoringMethod(actionMethod) {
			document.forms.voteMonitoringForm.action=actionMethod + '.do';
			document.forms.voteMonitoringForm.submit();
		}
		
		function submitAuthoringMethod(actionMethod) {
			document.forms.voteAuthoringForm.action=actionMethod + '.do'; 
			document.forms.voteAuthoringForm.submit();
		}
		
		function submitModifyQuestion(questionIndexValue, actionMethod) {
			document.forms.voteMonitoringForm.questionIndex.value=questionIndexValue; 
			submitMethod(actionMethod);
		}
		
		function submitModifyMonitoringNomination(questionIndexValue, actionMethod) {
			document.forms.voteMonitoringForm.questionIndex.value=questionIndexValue; 
			submitMethod(actionMethod);
		}
		
		
		function submitEditResponse(responseId, actionMethod) {
			document.forms.voteMonitoringForm.responseId.value=responseId; 
			submitMethod(actionMethod);
		}
		
		function submitMethod(actionMethod) {
			submitMonitoringMethod(actionMethod);
		}
		
		function deleteOption(optIndex, actionMethod) {
			document.forms.voteMonitoringForm.optIndex.value=optIndex; 
			submitMethod(actionMethod);
		}
		
		function submitModifyOption(optionIndexValue, actionMethod) {
			document.forms.voteMonitoringForm.optIndex.value=optionIndexValue; 
			submitMethod(actionMethod);
		}

		function submitModifyNomination(optionIndexValue, actionMethod) {
			document.forms.voteMonitoringForm.optIndex.value=optionIndexValue; 
			submitMethod(actionMethod);
		}
		
	</script>
	
</lams:head>
<body class="stripes" onLoad="init();">

    <form:form modelAttribute="voteMonitoringForm" method="POST">		
		<form:hidden path="toolContentID"/>
		<form:hidden path="httpSessionID"/>		
		<form:hidden path="currentTab" />
		<form:hidden path="contentFolderID"/>
		<form:hidden path="responseId"/>	 
		<form:hidden path="currentUid"/>

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
            		
	</form:form>
	
</body>
</lams:html>
