<!DOCTYPE html>

<%@include file="/common/taglibs.jsp"%>

<%@ page import="org.lamsfoundation.lams.tool.sbmt.util.SbmtConstants"%>

<lams:html>
<lams:head>
	<title><fmt:message key="activity.title" /></title>
	<lams:headItems />
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
    		selectTab(tabId);
	    		
	    	//for statistic page change:
	    	if(tabId == 3)
	    		doStatistic();	  
        } 
        
        
        function doSubmit(method) {
        	document.SbmtMonitoringForm.method.value=method;
        	document.SbmtMonitoringForm.submit();
        }
        
		function doStatistic(){
			var url = "<c:url value="/monitoring.do"/>";
			
			$("#statisticArea_Busy").show();
			$("#statisticArea").load(
				url,
				{
					method: "doStatistic",
					toolContentID: "${param.toolContentID}", 
					reqID: (new Date()).getTime()
				},
				function() {
					$("#statisticArea_Busy").hide();
				}
			);
		}
		
	</script>
</lams:head>

<body class="stripes" onLoad="init()">
	<div id="page">
		<h1>
			<fmt:message key="label.monitoring.heading" />
		</h1>
		<div id="header">
			<lams:Tabs control="true">
				<lams:Tab id="1" key="label.monitoring.heading.userlist" />
				<lams:Tab id="2" key="label.monitoring.heading.edit.activity" />
				<lams:Tab id="3" key="label.monitoring.heading.stats" />
			</lams:Tabs>
		</div>

		<div id="content">
			<html:form action="monitoring" method="post" styleId="monitoringForm">
				<html:hidden property="method" />
				<html:hidden property="currentTab" styleId="currentTab" />
				<html:errors />

				<lams:help toolSignature="<%= SbmtConstants.TOOL_SIGNATURE %>" module="monitoring"/>

				<!-- tab content 1 (Summary) -->
				<lams:TabBody id="1" titleKey="label.monitoring.heading.userlist.desc" page="parts/summary.jsp" />
				<!-- end of content (Summary) -->

				<!-- tab content 2 (Edit Activity) -->
				<lams:TabBody id="2" titleKey="label.monitoring.heading.edit.activity.desc" page="parts/activity.jsp" />
				<!-- end of content (Edit Activity) -->

				<!-- tab content 3 (Stats) -->
				<lams:TabBody id="3" titleKey="label.monitoring.heading.stats.desc" page="parts/statistic.jsp" />
				<!-- end of content (Stats) -->
			</html:form>
		</div>
		<div id="footer"></div>
	</div>
</body>
</lams:html>
