<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.forum.util.ForumConstants"%>

<script type="text/javascript">
	function verifyAllowRateMessagesCheckbox() {
		  var minRateDropDown = document.getElementById("minimumRate");
		   var minRatings = parseInt(minRateDropDown.value);
		   var maxRateDropDown = document.getElementById("maximumRate");
		   var maxRatings = parseInt(maxRateDropDown.value);
		
		if((minRatings == 0) && (maxRatings == 0)){
			var allowRateMessages = document.getElementById("allowRateMessages");
			allowRateMessages.checked = false;
		}
		
		return true;
	}
	
	
</script>

<html:form action="authoring/update" method="post"
	styleId="authoringForm" enctype="multipart/form-data" onsubmit="return verifyAllowRateMessagesCheckbox();">
	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<html:hidden property="toolContentID" />
	<html:hidden property="sessionMapID" />
	<html:hidden property="contentFolderID" />
	<html:hidden property="currentTab" styleId="currentTab" />
	<input type="hidden" name="mode" value="author">
	
<c:set var="title"><fmt:message key="activity.title" /></c:set>
<lams:Page title="${title}" type="navbar">
	
	<lams:Tabs control="true" title="${title}" helpToolSignature="<%= ForumConstants.TOOL_SIGNATURE %>" helpModule="authoring">
		<lams:Tab id="1" key="authoring.tab.basic" />
		<lams:Tab id="2" key="authoring.tab.advanced" />
		<lams:Tab id="3" key="authoring.tab.conditions" />
	</lams:Tabs>    

	<lams:TabBodyArea>
		<%@ include file="/common/messages.jsp"%>
	   
	    <!--  Set up tabs  -->
	     <lams:TabBodys>
			<lams:TabBody id="1" titleKey="authoring.tab.basic" page="basic.jsp" />
			<lams:TabBody id="2" titleKey="authoring.tab.advanced" page="advance.jsp" />
			<lams:TabBody id="3" titleKey="authoring.tab.conditions" page="conditions.jsp" />
	    </lams:TabBodys>
	
		<!-- Button Row -->
		<lams:AuthoringButton formID="authoringForm"
			clearSessionActionUrl="/clearsession.do"
			toolSignature="<%=ForumConstants.TOOL_SIGNATURE%>"
			toolContentID="${formBean.toolContentID}"
			customiseSessionID="${formBean.sessionMapID}"
			contentFolderID="${formBean.contentFolderID}" />
	</lams:TabBodyArea>
           		
    <div id="footer"></div>
    
</lams:Page>
</html:form>
