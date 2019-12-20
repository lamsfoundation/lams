<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.forum.ForumConstants"%>    

<lams:html>
	<lams:head>
		<title><fmt:message key="activity.title" /></title>
		<%@ include file="/common/tabbedheader.jsp"%>

		<script type="text/javascript">
		     //<![CDATA[
			function init(){
				var tag = document.getElementById("currentTab");
				if(tag == null || tag.value != "")
					selectTab(tag.value);
		        else
					selectTab(1);
			}  
		        
			function doSelectTab(tabId) {
				var tag = document.getElementById("currentTab");
				tag.value = tabId;
				selectTab(tabId);
			}
		
			function doSubmit(method) {
				var authorForm = document.getElementById("forumForm");
				authorForm.action="<c:url value='/authoring/'/>"+method+".do";
				authorForm.submit();
			}
			
			//]]>        
		</script>
		
	</lams:head>

	<body class="stripes" onLoad="init()">
	
		<c:set var="csrfToken"><csrf:token/></c:set>
		<form:form action="update.do?${csrfToken}" method="post" id="forumForm" modelAttribute="forumForm" enctype="multipart/form-data">
			<form:hidden path="toolContentID" />
			<form:hidden path="sessionMapID" />
			<form:hidden path="contentFolderID" />
			<form:hidden path="currentTab" id="currentTab" />
			<input type="hidden" name="mode" value="${mode}">
			
			<c:set var="title"><fmt:message key="activity.title" /></c:set>
			<lams:Page title="${title}" type="navbar" formID="forumForm">
				
				<lams:Tabs control="true" title="${title}" helpToolSignature="<%= ForumConstants.TOOL_SIGNATURE %>" helpModule="authoring">
					<lams:Tab id="1" key="authoring.tab.basic" />
					<lams:Tab id="2" key="authoring.tab.advanced" />
					<lams:Tab id="3" key="authoring.tab.conditions" />
				</lams:Tabs> 
			
				<lams:TabBodyArea>
					<lams:errors/>
				   
				    <!--  Set up tabs  -->
				     <lams:TabBodys>
						<lams:TabBody id="1" titleKey="authoring.tab.basic" page="basic.jsp" />
						<lams:TabBody id="2" titleKey="authoring.tab.advanced" page="advance.jsp" />
						<lams:TabBody id="3" titleKey="authoring.tab.conditions" page="conditions.jsp" />
				    </lams:TabBodys>
				
					<!-- Button Row -->
					<lams:AuthoringButton formID="forumForm"
						clearSessionActionUrl="/clearsession.do"
						toolSignature="<%=ForumConstants.TOOL_SIGNATURE%>"
						toolContentID="${forumForm.toolContentID}"
						customiseSessionID="${forumForm.sessionMapID}"
						contentFolderID="${forumForm.contentFolderID}" 
						accessMode="${mode}" 
						defineLater="${mode=='teacher'}"/>
				</lams:TabBodyArea>
			           		
			    <div id="footer"></div>
			    
			</lams:Page>
		</form:form>

		
	</body>
</lams:html>




