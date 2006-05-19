<%@include file="/common/taglibs.jsp"%>
<%@ taglib uri="fck-editor" prefix="FCK"%>
<%@ page import="java.util.HashSet"%>
<%@ page import="java.util.Set"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<%Set tabs = new HashSet();
			tabs.add("label.authoring.heading.basic");
			tabs.add("label.authoring.heading.advance");
			tabs.add("label.authoring.heading.instructions");
			pageContext.setAttribute("tabs", tabs);

		%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
	<lams:headItems />
	<title><fmt:message key="activity.title" /></title>
	<!-- this is the custom CSS for the tool -->
	<link href="${tool}css/tool_custom.css" rel="stylesheet" type="text/css">

	<script>
    
    	var imgRoot="${lams}images/";
	    var themeName="aqua";
        
        function init(){
        
            initTabSize(3);
            
            var tag = document.getElementById("currentTab");
	    	if(tag.value != "")
	    		selectTab(tag.value);
            else
                selectTab(1); //select the default tab;
            
            initEditor("Title");
            initEditor("Instructions");
            initEditor("OnlineInstruction");
            initEditor("OfflineInstruction");
            
        }     
        
        function doSelectTab(tabId) {
        	// start optional tab controller stuff
        	var tag = document.getElementById("currentTab");
	    	tag.value = tabId;
	    	// end optional tab controller stuff
	    	selectTab(tabId);
        } 
        
        function doSubmit(method) {
        	document.getElementById("authoringForm").dispatch.value=method;
        	document.getElementById("authoringForm").submit();
        }
        
    </script>
	<!-- ******************** END FCK Editor related javascript & HTML ********************** -->


</head>
<body onLoad="init()">

	<html:form action="authoring" method="post" focus="title" styleId="authoringForm" enctype="multipart/form-data">
		<html:hidden property="toolContentID" />
		<html:hidden property="currentTab" styleId="currentTab" />
		<html:hidden property="dispatch" value="updateContent" />

		<h1>
			<fmt:message key="label.authoring.heading" />
		</h1>

		<!-- start tabs -->
		<lams:Tabs collection="${tabs}" useKey="true" control="true" />
		<!-- end tab buttons -->
		<div class="tabbody">
			<table align=center>
				<tr>
					<td NOWRAP>
						<%@ include file="/common/messages.jsp"%>
					</td>
				</tr>
			</table>

			<!-- tab content 1 (Basic) -->
			<lams:TabBody id="1" titleKey="label.authoring.heading.basic.desc" page="basic.jsp" />
			<!-- end of content (Basic) -->

			<!-- tab content 2 (Advanced) -->
			<lams:TabBody id="2" titleKey="label.authoring.heading.advance.desc" page="advance.jsp" />
			<!-- end of content (Advanced) -->

			<!-- tab content 3 (Instructions) -->
			<lams:TabBody id="3" titleKey="label.authoring.heading.instructions.desc" page="instructions.jsp" />
			<!-- end of content (Instructions) -->


			<!-- Button Row -->
			<%--  Default value 
		cancelButtonLabelKey="label.authoring.cancel.button"
		saveButtonLabelKey="label.authoring.save.button"
		cancelConfirmMsgKey="authoring.msg.cancel.save"
		accessMode="author"
	--%>
			<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" toolSignature="lasbmt11" toolContentID="${toolContentID}" />

		</div>
		<lams:HTMLEditor />


	</html:form>
</body>
</html:html>
