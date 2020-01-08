<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.scratchie.ScratchieConstants"%>

<lams:html>
<lams:head>
	<title><fmt:message key="label.author.title" /></title>

	<%@ include file="/common/tabbedheader.jsp"%>
	<link href="<lams:LAMSURL/>css/thickbox.css" rel="stylesheet" type="text/css" media="screen">
	<style type="text/css">
		#confidence-levels-activity {
			display: inline-block;
			width: 200px;
		}
		#display-confidence-levels-activities {
			margin-top: 8px;
		}
	</style>
	
	<script>
        function init(){
            var tag = document.getElementById("currentTab");
		    	if (tag.value != "") {
		    		selectTab(tag.value);
		    	} else {
	            selectTab(1); //select the default tab;
		    	}
        }     
        
        function doSelectTab(tabId) {
        		// start optional tab controller stuff
        		var tag = document.getElementById("currentTab");
	    		tag.value = tabId;
	    		// end optional tab controller stuff
	    		selectTab(tabId);	    	
        }
    </script>
 
</lams:head>
<body class="stripes" onLoad="init()">
	<form:form action="/lams/tool/lascrt11/authoring/update.do" modelAttribute="authoringForm" method="post" id="authoringForm" >
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
		<form:hidden path="scratchie.contentId" />
		<form:hidden path="sessionMapID" />
		<form:hidden path="contentFolderID" />
		<input type="hidden" name="mode" value="${mode}"/>
		<form:hidden path="currentTab" id="currentTab" />
		<input type="hidden" name="itemList" id="itemList" />

		<c:set var="title"><fmt:message key="activity.title" /></c:set>
		<lams:Page title="${title}" type="navbar">
		
			<lams:Tabs control="true" title="${title}" helpToolSignature="<%= ScratchieConstants.TOOL_SIGNATURE %>" helpModule="authoring">
				<lams:Tab id="1" key="label.authoring.heading.basic" />
				<lams:Tab id="2" key="label.authoring.heading.advance" />
			</lams:Tabs>
	
			<lams:TabBodyArea>
				<lams:errors/>
		
				 <lams:TabBodys>
					 <!-- tab content 1 (Basic) -->
					<lams:TabBody id="1" titleKey="label.authoring.heading.basic.desc" page="basic.jsp" />
					<!-- end of content (Basic) -->
		
					<!-- tab content 2 (Advanced) -->
					<lams:TabBody id="2" titleKey="label.authoring.heading.advance.desc" page="advance.jsp" />
					<!-- end of content (Advanced) -->
				</lams:TabBodys>	
	
				<!-- Button Row -->
				<%--  Default value 
					cancelButtonLabelKey="label.authoring.cancel.button"
					saveButtonLabelKey="label.authoring.save.button"
					cancelConfirmMsgKey="authoring.msg.cancel.save"
					accessMode="author"
				--%>
				<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" toolSignature="<%=ScratchieConstants.TOOL_SIGNATURE%>" 
					toolContentID="${authoringForm.scratchie.contentId}" accessMode="${mode}" defineLater="${mode=='teacher'}"
					customiseSessionID="${authoringForm.sessionMapID}" contentFolderID="${authoringForm.contentFolderID}" />
			</lams:TabBodyArea>

			<div id="footer"></div>

		</lams:Page>
		
	</form:form>

</body>
</lams:html>
