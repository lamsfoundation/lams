<%@ include file="/common/taglibs.jsp" %>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>
	<title><fmt:message key="activity.title" /></title>
	<lams:headItems/>
	<!-- this is the custom CSS for the tool -->
	<link href="${tool}css/tool_custom.css" rel="stylesheet" type="text/css">

 	<!-- ******************** FCK Editor related javascript & HTML ********************** -->
	<script type="text/javascript">
		var imgRoot="${lams}images/";
	    var themeName="aqua";
	
		 function init(){
            initTabSize(5);
            
            var tag = document.getElementById("currentTab");
	    	if(tag.value != "")
	    		selectTab(tag.value);
            else
                selectTab(1); //select the default tab;
            
            initEditor("Title");
            initEditor("Instructions");
            initEditor("Comments");
            
        }   
        
		function doSelectTab(tabId) {
        	// start optional tab controller stuff
        	var tag = document.getElementById("currentTab");
	    	tag.value = tabId;
	    	// end optional tab controller stuff
	    	if(active(tabId))
	    		selectTab(tabId);
        } 
        
        function active(tabId) {
        	if(document.getElementById("tab" + tabId).className == "tab tabcentre_inactive")
        		return false;
        	return true;
        }
        
        function doSubmit(method, tabId) {
        	if(tabId != null)
        		document.SbmtMonitoringForm.currentTab.value=tabId;
        	document.SbmtMonitoringForm.method.value=method;
        	document.SbmtMonitoringForm.submit();
        }
	</script>
	<!-- ******************** END FCK Editor related javascript & HTML ********************** -->
    
  </head>
  
  <body onLoad="init()">
  
  	<html:form action="monitoring" method="post">
  		<html:hidden property="method"/>
		<html:hidden property="currentTab" styleId="currentTab" />
		
  	<h1><bean:message key="label.monitoring.heading" /></h1>
    <!-- start tabs -->
		<lams:Tabs control="true">
			<lams:Tab id="1" key="label.monitoring.heading.userlist"/>
			<lams:Tab id="2" key="label.monitoring.heading.instructions"/>
			<lams:Tab id="3" key="label.monitoring.heading.edit.activity"/>
			<lams:Tab id="4" key="label.monitoring.heading.stats"/>
			<lams:Tab id="5" key="label.monitoring.heading.marking" inactive="true"/>
		</lams:Tabs>
	<!-- end tab buttons -->
	<div class="tabbody">
		<html:errors/>	
		
		
		
		<!-- tab content 1 (Summary) -->
		<lams:TabBody id="1" titleKey="label.monitoring.heading.userlist.desc" page="alluserlist.jsp"/>
		<!-- end of content (Summary) -->
		
		<!-- tab content 2 (Instructions) -->
		<lams:TabBody id="2" titleKey="label.monitoring.heading.instructions.desc" page="instructions.jsp"/>
		<!-- end of content (Instructions) -->
	  	
	  	<!-- tab content 3 (Edit Activity) -->
	  	<c:set var="activity_page" value="showactivity.jsp"/>
		<c:if test="${SbmtMonitoringForm.map.method == 'editActivity'}">
			<c:set var="activity_page" value="editactivity.jsp"/>
		</c:if>
		<lams:TabBody id="3" titleKey="label.monitoring.heading.edit.activity.desc" page="${activity_page}"/>
		<!-- end of content (Edit Activity) -->
	  	
	  	<!-- tab content 4 (Stats) -->
		<lams:TabBody id="4" titleKey="label.monitoring.heading.stats.desc" page="statistic.jsp"/>
		<!-- end of content (Stats) -->
	  	
	  	<c:set var="marking_page" value="marking.jsp"/>
	  	<c:if test="${SbmtMonitoringForm.map.method == 'viewAllMarks'}">
	  		<c:set var="marking_page" value="viewallmarks.jsp"/>
	  	</c:if>
		<c:if test="${SbmtMonitoringForm.map.method == 'getFilesUploadedByUser' ||
					  SbmtMonitoringForm.map.method == 'updateMarks'}">
			<c:set var="marking_page" value="usermarkslist.jsp"/>
		</c:if>
		<c:if test="${SbmtMonitoringForm.map.method == 'markFile'}">
			<c:set var="marking_page" value="updatemarks.jsp"/>
		</c:if>

	  	<!-- tab content 5 (Marking) -->
		<lams:TabBody id="5" titleKey="label.monitoring.heading.marking.desc" page="${marking_page}"/>
		<!-- end of content (Marking) -->
	  	
	  	<!-- Button Row -->
		<p align="right">
			<html:link href="javascript:;" property="submit" onclick="window.close()" styleClass="button">
				<bean:message key="label.monitoring.done.button" />
			</html:link>
		</p>
	</div>	
	
	<lams:HTMLEditor/>		
	</html:form>
  </body>
</html:html>
