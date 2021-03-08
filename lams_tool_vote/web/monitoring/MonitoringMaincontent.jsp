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
            var tag = "${voteGeneralMonitoringDTO.currentTab}";
	    	if(tag.value != "")
	    		selectTab(tag.value);
            else
                selectTab(1); //select the default tab;
        }     
        
        function doSelectTab(tabId) {
        	// start optional tab controller stuff
        	var tag = "${voteGeneralMonitoringDTO.currentTab}";
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
	</script>
</lams:head>
<body class="stripes" onLoad="init();">
	<c:set var="title"><fmt:message key="activity.title" /></c:set>
	<lams:Page title="${title}" type="navbar">

         <lams:Tabs control="true" title="${title}" helpToolSignature="<%= VoteAppConstants.MY_SIGNATURE %>" helpModule="monitoring">
            <lams:Tab id="1" key="label.summary" />
            <lams:Tab id="2" key="label.editActivity" />
            <lams:Tab id="3" key="label.stats" />
         </lams:Tabs>
   
        <lams:TabBodyArea>
        <lams:TabBodys>
            <lams:TabBody id="1" page="SummaryContent.jsp"/>
	 		<lams:TabBody id="2" page="Edit.jsp" />
			<lams:TabBody id="3" page="Stats.jsp" />
        </lams:TabBodys>
        </lams:TabBodyArea>
       
        <div id="footer"></div>
	</lams:Page>
	
</body>
</lams:html>
