<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@include file="/common/taglibs.jsp"%>
<%@ taglib uri="fck-editor" prefix="FCK"%>

<%@ page import="org.lamsfoundation.lams.tool.sbmt.util.SbmtConstants"%>
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
			pageContext.setAttribute("tabs", tabs);

			%>
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
        
            initTabSize(1);
            
            var tag = document.getElementById("currentTab");
            selectTab(1); //select the default tab;
            
            initEditor("Title");
            initEditor("Instructions");
        }     
        
        function doSelectTab(tabId) {
        	// start optional tab controller stuff
        	var tag = document.getElementById("currentTab");
	    	tag.value = tabId;
	    	// end optional tab controller stuff
	    	selectTab(tabId);
        } 
        
    </script>
	<!-- ******************** END FCK Editor related javascript & HTML ********************** -->


</head>
<body onLoad="init()">
	<div id="page">

		<h1>
			<fmt:message key="label.authoring.heading" />
		</h1>
		<div id="header">
			<!-- start tabs -->
			<lams:Tabs collection="${tabs}" useKey="true" control="true" />
			<!-- end tab buttons -->
		</div>
		<div id="content">
			<table align=center>
				<tr>
					<td NOWRAP>
						<%@ include file="/common/messages.jsp"%>
					</td>
				</tr>
			</table>
			<html:form action="definelater" method="post" focus="title" styleId="authoringForm" enctype="multipart/form-data">
				<html:hidden property="mode" value="teacher" />
				<html:hidden property="toolContentID" />
				<html:hidden property="currentTab" styleId="currentTab" />
				<html:hidden property="dispatch" value="updateContent" />

				<!-- tab content 1 (Basic) -->
				<lams:TabBody id="1" titleKey="label.authoring.heading.basic.desc" page="basic.jsp" />

				<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" accessMode="teacher" toolSignature="<%=SbmtConstants.TOOL_SIGNATURE%>" toolContentID="${toolContentID}" defineLater="true" />

				<lams:HTMLEditor />

			</html:form>
		</div>
		<div id="footer"></div>
	</div>
</body>
</html:html>
