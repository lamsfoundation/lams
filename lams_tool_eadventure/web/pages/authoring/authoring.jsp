<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.eucm.lams.tool.eadventure.EadventureConstants"%>


<lams:html>
<lams:head>
	<title><fmt:message key="label.author.title" /></title>

	<%@ include file="/common/tabbedheader.jsp"%>

	<script>
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
	    	
	    	//if we're leaving Condition tab its addCondition area should be closed
	    	if (tabId != 4)	window.parent.hideConditionMessage();
	    	
	    	// end optional tab controller stuff
	    	selectTab(tabId);
	    	
	    	//for advanceTab
	    	if(tabId == 2)
	    		changeViewNum(-1);	    	
        } 
		
		function showGame(){
			var area=document.getElementById("addGame");
				if(area != null){
				area.style.display="block";
			}
		}
		
		function hideShowGame(){
			var obj = window.document.getElementById('addGame');
			if (!obj && window.parent) {
				 obj = window.parent.document.getElementById('addGame');
			}  
			if (!obj) {
				obj = window.top.document.getElementById('addGame');
			}
			if (obj){
				obj.style.display="none";
			}
		}

        function changeViewNum(initVal){
			var tb = document.getElementById("itemTable");
			var num = tb.getElementsByTagName("tr");
			var sel = document.getElementById("viewNumList");
			var newField = sel.options;
			var len = sel.length;
			
			//when first enter, it should get value from Eadventure
			var selIdx=initVal < 0?-1:initVal;
			//there is bug in Opera8.5: if add alert before this loop, it will work,weird.
			for (var idx=0;idx<len;idx++)
			{
				if(newField[0].selected && selIdx == -1 ){
					selIdx = newField[0].value;
				}
				sel.removeChild(newField[0]);
			}
		
			for(var i=0;i<=num.length;i++){
				var opt = document.createElement("option");
				var optT =document.createTextNode(i);
				opt.value=i;
				//get back user choosen value
				if(selIdx > 0 && selIdx==i){
					opt.selected = true;
				}else{
					opt.selected = false;
				}
				opt.appendChild(optT);
				sel.appendChild(opt);
			}
	}
	function showConditionsAddButton(){
		var conditionButton = document.getElementById("addConditionButton");
		var conditionMessage = document.getElementById("addConditionMessage");
		if(conditionButton != null){
				conditionButton.style.display="block";
				conditionMessage.style.display = "none";
			}
		
	}
	var emptyTable = "<table class='alternative-color' id='conditionTable' cellspacing='0'><tr><th align='left' style='width:1%;'>Nº</th><th align='left' colspan='10'>Condition Name</th></tr><tr><td colspan='15'>There are no conditions </td></tr>";
	
	function forceEmptyTable(){
		var area = document.getElementById("conditionsArea")
		if (area!=null){
			area.innerHTML = emptyTable;
		}
	}
	
	function hideConditionsAddButton(){
		var conditionButton = document.getElementById("addConditionButton");
		var conditionMessage = document.getElementById("addConditionMessage");
		if(conditionButton != null){
				conditionButton.style.display="none";
				conditionMessage.style.display = "block";
			}
		
	}
	
	function clearSession(){
		var url = "<c:url value="/clearsession.do"/>";
	    var myAjax = new Ajax.Updater(
		    	conditionListTargetDiv,
		    	url,
		    	{
		    		method:'get',
		    		evalScripts:true
		    	}
	    );
	}
        
    </script>
	<!-- ******************** END CK Editor related javascript & HTML ********************** -->

 
</lams:head>
<body class="stripes" onLoad="init()" onunload="clearSession()">
<div id="page">
		<h1>
			<fmt:message key="label.authoring.heading" />
		</h1>
<div id="header">
		<lams:Tabs useKey="true" control="true">
			<lams:Tab id="1" key="label.authoring.heading.basic" />
			<lams:Tab id="2" key="label.authoring.heading.advance" />
			<lams:Tab id="3" key="label.authoring.heading.instructions" />
			<lams:Tab id="4" key="label.authoring.heading.conditions" />
		</lams:Tabs></div>
		<!-- start tabs -->
<div id="content">
		<!-- end tab buttons -->
		
		<%@ include file="/common/messages.jsp"%>

		<html:form action="authoring/update" method="post" styleId="eadventureForm" enctype="multipart/form-data">
		<c:set var="formBean" value="<%= request
													 .getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
		<html:hidden property="eadventure.contentId" />
		<html:hidden property="sessionMapID" />
		<html:hidden property="contentFolderID" />

		<html:hidden property="currentTab" styleId="currentTab" />

		<lams:help toolSignature="<%= EadventureConstants.TOOL_SIGNATURE %>" module="authoring"/>

			<!-- tab content 1 (Basic) -->
			<lams:TabBody id="1" titleKey="label.authoring.heading.basic.desc" page="basic.jsp" />
			<!-- end of content (Basic) -->

			<!-- tab content 2 (Advanced) -->
			<lams:TabBody id="2" titleKey="label.authoring.heading.advance.desc" page="advance.jsp" />
			<!-- end of content (Advanced) -->

			<!-- tab content 3 (Instructions) -->
			<lams:TabBody id="3" titleKey="label.authoring.heading.instructions.desc" page="instructions.jsp" />
			<!-- end of content (Instructions) -->
			
			<!-- tab content 4 (Conditions) -->
			<lams:TabBody id="4" titleKey="label.authoring.heading.conditions.desc" page="conditions.jsp" />
			<!-- end of content (Conditions) -->
			
			<!-- Button Row -->
			<%--  Default value 
				cancelButtonLabelKey="label.authoring.cancel.button"
				saveButtonLabelKey="label.authoring.save.button"
				cancelConfirmMsgKey="authoring.msg.cancel.save"
				accessMode="author"
			--%>
			<lams:AuthoringButton formID="eadventureForm" clearSessionActionUrl="/clearsession.do" 
				toolSignature="<%=EadventureConstants.TOOL_SIGNATURE%>" toolContentID="${formBean.eadventure.contentId}" 
				 customiseSessionID="${formBean.sessionMapID}"
				 contentFolderID="${formBean.contentFolderID}" />
	</html:form>

</div>

<div id="footer"></div>
<!-- end page div -->
</div>
</body>
</lams:html>
