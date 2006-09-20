<%--
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as
  published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<%@ page import="java.util.LinkedHashSet" %>
<%@ page import="java.util.Set" %>
<%@ page import="org.lamsfoundation.lams.tool.mc.McAppConstants"%>

    <% 
		Set tabs = new LinkedHashSet();
		tabs.add("label.summary");
		tabs.add("label.instructions");
		tabs.add("label.editActivity");
		tabs.add("label.stats");
		pageContext.setAttribute("tabs", tabs);
	%>

<html:html locale="true">
<head>
	<title><fmt:message key="activity.title" /></title>

	<%@ include file="/common/header.jsp"%>
	<script type="text/javascript" src="${lams}includes/javascript/prototype.js"></script>
	<script type="text/javascript" src="${tool}includes/javascript/common.js"></script>
	
	 	<!-- ******************** FCK Editor related javascript & HTML ********************** -->
	<script language="JavaScript" type="text/JavaScript">
		function submitMonitoringMethod(actionMethod) 
		{
			document.McMonitoringForm.method.value=actionMethod; 
			document.McMonitoringForm.submit();
		}
		
		function submitModifyQuestion(questionIndexValue, actionMethod) 
		{
			document.McMonitoringForm.questionIndex.value=questionIndexValue; 
			submitMethod(actionMethod);
		}
		
		function submitMethod(actionMethod) 
		{
			submitMonitoringMethod(actionMethod);
		}
		
		function deleteOption(deletableOptionIndex, actionMethod) {
			document.McMonitoringForm.deletableOptionIndex.value=deletableOptionIndex; 
			submitMethod(actionMethod);
		}
		
		
		function submitSession(selectedToolSessionId, actionMethod) {
			document.McMonitoringForm.selectedToolSessionId.value=selectedToolSessionId; 
			submitMonitoringMethod(actionMethod);
		}
		
		function MM_reloadPage(init) {  //reloads the window if Nav4 resized
		  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
		    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
		  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
		}
		
    	var imgRoot="${lams}images/";
	    var themeName="aqua";
        
        
        function doSelectTab(tabId) {
        	// start optional tab controller stuff
        	var tag = document.getElementById("currentTab");
	    	tag.value = tabId;
	    	// end optional tab controller stuff
	    	selectTab(tabId);
        } 
        
        function doSubmit(method) {
        	document.McMonitoringForm.method.value=method;
        	document.McMonitoringForm.submit();
        }

	</script>	

	<script language="JavaScript" type="text/JavaScript">

		<c:if test="${sessionScope.editOptionsMode != 1 }"> 		
		        function init(){
		        
		            initTabSize(4);
		            
		            var tag = document.getElementById("currentTab");
			    	if(tag.value != "")
			    		selectTab(tag.value);
		            else
		                selectTab(1); //select the default tab;
		
		            //initEditor("richTextTitle");
		            //initEditor("richTextInstructions");
		        }     
		</c:if> 

		<c:if test="${sessionScope.editOptionsMode == 1 }"> 		
		        function init(){
		        
		            initTabSize(4);
		            
		            var tag = document.getElementById("currentTab");
			    	if(tag.value != "")
			    		selectTab(tag.value);
		            else
		                selectTab(1); //select the default tab;
		
		            //initEditor("richTextIncorrectFeedback");                                                
		            //initEditor("richTextCorrectFeedback");                                                            		            
		        }     
		</c:if> 

	
	</script>	
	
</head>

<%-- chooses which tab to highlight --%>

<body onLoad="init();">
<div id="page">
	<h1> <bean:message key="label.monitoring"/>  </h1>
	
		<c:set var="monitoringURL">
			<html:rewrite page="/monitoring.do" />
		</c:set>

	<div id="header">
		<lams:Tabs collection="${tabs}" useKey="true" control="true"/>
	</div>	


	<div id="content">		
	  <html:form  action="/monitoring?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
		<html:hidden property="method"/>	 
		<html:hidden property="currentTab" styleId="currentTab" />
		
		<lams:TabBody id="1" titleKey="label.summary" page="Summary.jsp"/>

		<lams:TabBody id="2" titleKey="label.instructions" page="Instructions.jsp" />

		<lams:TabBody id="3" titleKey="label.editActivity" page="Edit.jsp" />
		
		<lams:TabBody id="4" titleKey="label.stats" page="Stats.jsp" />
 		</html:form>
	</div>		

	<div id="footer"></div>
		<lams:HTMLEditor />
	</div>


</body>
</html:html>







