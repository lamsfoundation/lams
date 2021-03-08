<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.taskList.TaskListConstants"%>

<lams:html>
<lams:head>
	<title><fmt:message key="label.authoring.title" /></title>

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
        	var tag = document.getElementById("currentTab");
	    	tag.value = tabId;
	    	
	    	selectTab(tabId);
	    	
	    	//for advanceTab
	    	if(tabId == 2)
	    		changeMinTasks(-1);
        }
        
        function changeMinTasks(initVal){
			var tb = document.getElementById("itemTable");
			var num = tb.getElementsByTagName("tr");
			var sel = document.getElementById("minimumNumberTasks");
			var newField = sel.options;
			var len = sel.length;
			
			//when first enter, it should get value from TaskList
			var selIdx=initVal < 0?-1:initVal;
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
    </script>
</lams:head>

<body class="stripes" onLoad="init()">
<form:form action="update.do" method="post" modelAttribute="taskListForm" id="taskListForm">
	<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
	<form:hidden path="taskList.contentId" />
	<input type="hidden" name="mode" value="${mode}"/>
	<form:hidden path="sessionMapID" />
	<form:hidden path="contentFolderID" />
	<form:hidden path="currentTab" id="currentTab" />

		<c:set var="title"><fmt:message key="activity.title" /></c:set>
		<lams:Page title="${title}" type="navbar" formID="taskListForm">
	

			 <lams:Tabs control="true" title="${title}" helpToolSignature="<%= TaskListConstants.TOOL_SIGNATURE %>" helpModule="authoring">
				<lams:Tab id="1" key="label.authoring.heading.basic" />
				<lams:Tab id="2" key="label.authoring.heading.advance" />
				<lams:Tab id="3" key="label.authoring.heading.conditions" />
			</lams:Tabs>
			
			<lams:TabBodyArea>
				<lams:errors/>
		
				<lams:TabBodys>
					<!-- tab content 1 (Basic) -->
					<lams:TabBody id="1" page="basic.jsp" />
					<!-- end of content (Basic) -->
		
					<!-- tab content 2 (Advanced) -->
					<lams:TabBody id="2" page="advance.jsp" />
					<!-- end of content (Advanced) -->
		
					<!-- tab content 3 (Conditions) -->
					<lams:TabBody id="3" page="conditions.jsp" />
					<!-- end of content (Instructions) -->
				</lams:TabBodys>
				
					<!-- Button Row -->
					<%--  Default value 
						cancelButtonLabelKey="label.authoring.cancel.button"
						saveButtonLabelKey="label.authoring.save.button"
						cancelConfirmMsgKey="authoring.msg.cancel.save"
						accessMode="author"
					--%>
		
		
					<lams:AuthoringButton formID="taskListForm" clearSessionActionUrl="/clearsession.do" 
						toolSignature="<%=TaskListConstants.TOOL_SIGNATURE%>" toolContentID="${taskListForm.taskList.contentId}" 
						 customiseSessionID="${taskListForm.sessionMapID}" accessMode="${mode}" defineLater="${mode=='teacher'}"
						 contentFolderID="${taskListForm.contentFolderID}" />
			</lams:TabBodyArea>
	
			<div id="footer"></div>

		</lams:Page>	
</form:form>

<script type="text/javascript">
	changeMinTasks(${taskListForm.taskList.minimumNumberTasks});
</script>

</body>
</lams:html>
