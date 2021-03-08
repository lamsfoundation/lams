<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.rsrc.ResourceConstants"%>

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
	    	// end optional tab controller stuff
	    	selectTab(tabId);
	    	
	    	//for advanceTab
	    	if(tabId == 2)
	    		changeViewNum(-1);	    	
        }
        
        function changeViewNum(initVal){
			var tb = document.getElementById("itemTable");
			var num = tb.getElementsByTagName("tr");
			var sel = document.getElementById("viewNumList");
			var newField = sel.options;
			var len = sel.length;
			
			//when first enter, it should get value from Resource
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
    </script> 
</lams:head>
<body class="stripes" onLoad="init()">
	<form:form action="update.do" method="post" modelAttribute="authoringForm" id="authoringForm">
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
		<form:hidden path="resource.contentId" />
		<form:hidden path="mode" />
		<form:hidden path="sessionMapID" />
		<form:hidden path="contentFolderID" />
		<form:hidden path="currentTab" styleId="currentTab" />
	
		<c:set var="title"><fmt:message key="activity.title" /></c:set>
		<lams:Page title="${title}" type="navbar">
	
			<lams:Tabs control="true" title="${title}" helpToolSignature="<%= ResourceConstants.TOOL_SIGNATURE %>" helpModule="authoring">
				<lams:Tab id="1" key="label.authoring.heading.basic" />
				<lams:Tab id="2" key="label.authoring.heading.advance" />
			</lams:Tabs>    

			<lams:TabBodyArea>
				<lams:errors/>
			   
			    <!--  Set up tabs  -->
			    <lams:TabBodys>
					<!-- tab content 1 (Basic) -->
					<lams:TabBody id="1" page="basic.jsp" />
					<!-- end of content (Basic) -->
		
					<!-- tab content 2 (Advanced) -->
					<lams:TabBody id="2" page="advance.jsp" />
					<!-- end of content (Advanced) -->
				</lams:TabBodys>
					
				<!-- Button Row -->
				<%--  Default value 
					cancelButtonLabelKey="label.authoring.cancel.button"
					saveButtonLabelKey="label.authoring.save.button"
					cancelConfirmMsgKey="authoring.msg.cancel.save"
					accessMode="author"
				--%>
				<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" 
					toolSignature="<%=ResourceConstants.TOOL_SIGNATURE%>" toolContentID="${authoringForm.resource.contentId}" 
					 customiseSessionID="${authoringForm.sessionMapID}" accessMode="${mode}" defineLater="${mode=='teacher'}"
					 contentFolderID="${authoringForm.contentFolderID}" />		 
			</lams:TabBodyArea>
		
			<div id="footer"></div>

		</lams:Page>
		
	</form:form>

	<script type="text/javascript">
		changeViewNum(${authoringForm.resource.miniViewResourceNumber});
	</script>
</body>
</lams:html>
