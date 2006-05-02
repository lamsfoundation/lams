<%@ include file="/common/taglibs.jsp"%>

<html>
	<head>
		 <%@ include file="/common/header.jsp" %>
	 <script>
	    
	    	var imgRoot="${lams}images/";
		    var themeName="aqua";
	        
	        function init(){
	        
	            initTabSize(2);
	            
                selectTab(1); //select the default tab;
	        }     
	        
	        function doSelectTab(tabId) {
		    	// end optional tab controller stuff
		    	selectTab(tabId);
	        } 
	        
	        
	    </script>		 
	</head>
	<body onLoad="init()">
		<lams:Tabs>
			<lams:Tab id="1" key="monitoring.tab.summary" />
			<lams:Tab id="2" key="monitoring.tab.statistics" />
		</lams:Tabs>


		<div class="tabbody">
			<lams:TabBody id="1" titleKey="monitoring.tab.summary" page="summary.jsp" />
			<lams:TabBody id="2" titleKey="monitoring.tab.statistics" page="statistic.jsp" />
		</div>
	</body>
</html>
