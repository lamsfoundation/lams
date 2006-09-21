<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@include file="/common/taglibs.jsp"%>

<%@ page import="org.lamsfoundation.lams.tool.sbmt.util.SbmtConstants"%>

<html:html>
<head>
	<title><fmt:message key="activity.title" /></title>
	<lams:headItems />
	<!-- ********************  CSS ********************** -->
	<link href="<html:rewrite page='/includes/css/tool_custom.css'/>" rel="stylesheet" type="text/css">
	<script type="text/javascript">
		 function init(){
            initTabSize(4);
            
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
	    	if(tabId == 4)
	    		doStatistic();	  
        } 
        
        
        function doSubmit(method) {
        	document.SbmtMonitoringForm.method.value=method;
        	document.SbmtMonitoringForm.submit();
        }
        
        
	var statisticTargetDiv = "statisticArea";
	function doStatistic(){
		var url = "<c:url value="/monitoring.do"/>";
	    var reqIDVar = new Date();
		var param = "method=doStatistic&toolContentID=" + ${param.toolContentID} +"&reqID="+reqIDVar.getTime();
		messageLoading();
	    var myAjax = new Ajax.Updater(
		    	statisticTargetDiv,
		    	url,
		    	{
		    		method:'get',
		    		parameters:param,
		    		onComplete:messageComplete,
		    		evalScripts:true
		    	}
	    );
		
	}
	
	function showBusy(targetDiv){
		if($(targetDiv+"_Busy") != null){
			Element.show(targetDiv+"_Busy");
		}
	}
	function hideBusy(targetDiv){
		if($(targetDiv+"_Busy") != null){
			Element.hide(targetDiv+"_Busy");
		}				
	}
	function messageLoading(){
		showBusy(statisticTargetDiv);
	}
	function messageComplete(){
		hideBusy(statisticTargetDiv);
	}        
	</script>
</head>

<body onLoad="init()">
	<div id="page">
		<h1>
			<bean:message key="label.monitoring.heading" />
		</h1>
		<div id="header">
			<lams:Tabs control="true">
				<lams:Tab id="1" key="label.monitoring.heading.userlist" />
				<lams:Tab id="2" key="label.monitoring.heading.instructions" />
				<lams:Tab id="3" key="label.monitoring.heading.edit.activity" />
				<lams:Tab id="4" key="label.monitoring.heading.stats" />
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

				<!-- tab content 2 (Instructions) -->
				<lams:TabBody id="2" titleKey="label.monitoring.heading.instructions.desc" page="parts/instructions.jsp" />
				<!-- end of content (Instructions) -->

				<!-- tab content 3 (Edit Activity) -->
				<lams:TabBody id="3" titleKey="label.monitoring.heading.edit.activity.desc" page="parts/activity.jsp" />
				<!-- end of content (Edit Activity) -->

				<!-- tab content 4 (Stats) -->
				<lams:TabBody id="4" titleKey="label.monitoring.heading.stats.desc" page="parts/statistic.jsp" />
				<!-- end of content (Stats) -->
			</html:form>
		</div>
		<div id="footer"></div>
	</div>
</body>
</html:html>
