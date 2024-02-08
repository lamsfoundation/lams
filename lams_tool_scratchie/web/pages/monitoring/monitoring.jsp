<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.scratchie.ScratchieConstants"%>

<c:set var="lams"><lams:LAMSURL /></c:set>

<lams:html>
	<lams:head>
	
	<lams:css/>
	<link type="text/css" href="${lams}css/jquery-ui-bootstrap-theme5.css" rel="stylesheet">
	<link type="text/css" href="${lams}css/thickbox.css" rel="stylesheet"  media="screen">
	<link href="${lams}css/jquery-ui.timepicker.css" rel="stylesheet" type="text/css" >
	<link href="${lams}css/free.ui.jqgrid.min.css" rel="stylesheet" type="text/css">
	<lams:css suffix="chart"/>
		
	<lams:JSImport src="includes/javascript/common.js" />
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.plugin.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.countdown.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/free.jquery.jqgrid.min.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/thickbox.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.tabcontroller.js"></script>
 
 	<!--  File Download -->
 	<script type="text/javascript" src="${lams}includes/javascript/jquery.cookie.js"></script>
 	<script type="text/javascript" src="${lams}includes/javascript/download.js"></script>
 
 	<!--  Marks Chart -->
 	<script type="text/javascript" src="${lams}includes/javascript/d3.js"></script>
 	<script type="text/javascript" src="${lams}includes/javascript/chart.js"></script>

 	<lams:JSImport src="includes/javascript/portrait5.js" />
 
	 <script>
			var initialTabId = "${initialTabId}";

		   function init(){
			 	if (initialTabId) {
					selectTab(initialTabId);
				} else {
					selectTab(1);
				}
	        }     
	        
	        function doSelectTab(tabId) {
		    	if ( tabId == 3 )
		    		doStatistic();
		    	selectTab(tabId);
	        } 
	        
			function doStatistic(){
				var url = '<c:url value="/monitoring/statistic.do?sessionMapID=${sessionMapID}"/>';
				$.ajaxSetup({ cache: true });
				$("#statisticArea").load(
					url,
					{
						toolContentID: <c:out value="${param.toolContentID}" />, 
						reqID: (new Date()).getTime()
					}
				);
				
			}
		</script>	  

	</lams:head>
	<body class="stripes" onLoad="init()">

	<c:set var="title"><fmt:message key="activity.title" /></c:set>
	<lams:Page title="${title}" type="navbar">
	
		<lams:Tabs title="${title}" control="true" helpToolSignature="<%= ScratchieConstants.TOOL_SIGNATURE %>" helpModule="monitoring" refreshOnClickAction="javascript:location.reload();">
 			<lams:Tab id="1" key="monitoring.tab.summary" />
			<lams:Tab id="2" key="monitoring.tab.edit.activity" />			
 			<lams:Tab id="3" key="monitoring.tab.statistics" />
		</lams:Tabs>
		
		<lams:TabBodyArea>
		<lams:TabBodys>
 			<lams:TabBody id="1" page="summary.jsp" />
			<lams:TabBody id="2" page="editactivity.jsp" />			
 			<lams:TabBody id="3" page="statistic.jsp" />
		</lams:TabBodys>
		</lams:TabBodyArea>
		
		<div id="footer" />
	</lams:Page>
	
	</body>
</lams:html>