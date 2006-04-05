<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.lamsfoundation.lams.tool.noticeboard.web.NbMonitoringAction" %>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD hTML 4.01 Transitional//EN">
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title><fmt:message key="tool.display.name"/></title>

	<lams:css/>
    
	<!-- this is the custom CSS for the tool -->
	<link href="${tool}css/tool_custom.css" rel="stylesheet" type="text/css">

 	<!-- ******************** FCK Editor related javascript & HTML ********************** -->
    <script type="text/javascript" src="${lams}fckeditor/fckeditor.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/fckcontroller.js"></script>
    <link href="${lams}css/fckeditor_style.css" rel="stylesheet" type="text/css">
	
	<script type="text/javascript">
		var imgRoot="${lams}images/";
	    var themeName="aqua";
	
		 function init(){
            initTabSize(4);
            
            var tag = document.getElementById("currentTab");
	    	if(tag.value != "")
	    		selectTab(tag.value);
            else
                selectTab(1); //select the default tab;
        }   

		// this is used when the page is initialised/reloaded, to show the correct tab
		function doSelectTab(tabId) {
        	// start optional tab controller stuff
        	var tag = document.getElementById("currentTab");
	    	tag.value = tabId;
	    	// end optional tab controller stuff
        } 
        
        // The following methods control the user switching tabs.
        // Needed as it goes back to the server to get the data (so the page is reloaded)
        function doSwitchSummary(tabId) {
        	doSwitchTab(tabId,"summary");
        }
        function doSwitchInstructions(tabId) {
        	doSwitchTab(tabId,"instructions");
        }
        function doSwitchEditActivity(tabId) {
        	doSwitchTab(tabId,"editActivity");
        }
        function doSwitchStatistics(tabId) {
        	doSwitchTab(tabId,"statistics");
        }
        
		function doSwitchTab(tabId, method) {
        	if(tabId != null) {
        		document.NbMonitoringForm.currentTab.value=tabId;
        	}
        	document.NbMonitoringForm.method.value=method;
        	document.NbMonitoringForm.submit();
        }

	</script>
	<!-- ******************** END FCK Editor related javascript & HTML ********************** -->
    
	<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>    
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
  </head>
  
<body onLoad='init()'>

<html:form action="/monitoring" target="_self">
<html:hidden property="method"/>
<html:hidden property="currentTab" styleId="currentTab" />

<c:set var="monitoringURL">
	<html:rewrite page="/monitoring.do" />
</c:set>

<!-- start tabs -->
<lams:Tabs control="true">
	<lams:Tab id="<%=NbMonitoringAction.SUMMARY_TABID%>" key="titleHeading.summary" methodCall="doSwitchSummary"/>
	<lams:Tab id="<%=NbMonitoringAction.INSTRUCTIONS_TABID%>" key="titleHeading.instructions" methodCall="doSwitchInstructions"/>
	<lams:Tab id="<%=NbMonitoringAction.EDITACTIVITY_TABID%>" key="titleHeading.editActivity" methodCall="doSwitchEditActivity"/>
	<lams:Tab id="<%=NbMonitoringAction.STATISTICS_TABID%>" key="titleHeading.statistics" methodCall="doSwitchStatistics"/>
</lams:Tabs>
<!-- end tab buttons -->

<div class="tabbody">

<!-- tab content 1 (Summary) -->
<lams:TabBody id="1" titleKey="titleHeading.summary" page="m_Summary.jsp"/>
<!-- end of content (Basic) -->
      
<!-- tab content 2 (Advanced) -->
<lams:TabBody id="2" titleKey="titleHeading.instructions" page="m_Instructions.jsp" />
<!-- end of content (Advanced) -->

<!-- tab content 3 (Instructions) -->
<lams:TabBody id="3" titleKey="titleHeading.editActivity" page="m_EditActivity.jsp" />
<!-- end of content (Instructions) -->

<!-- tab content 4 (Statistics) -->
<lams:TabBody id="4" titleKey="titleHeading.statistics" page="m_Statistics.jsp" />
<!-- end of content (Instructions) -->

<!-- Button Row -->
<hr>
<p align="right">
	<html:link href="javascript:;" property="submit" onclick="window.close()" styleClass="button">
		<fmt:message key="button.done" />
	</html:link>
</p>
</div>
    
</html:form>

</body>


</html:html>

