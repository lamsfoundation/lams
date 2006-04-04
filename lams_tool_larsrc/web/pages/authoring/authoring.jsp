<%@ include file="/common/taglibs.jsp" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.Set" %>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>
    <% 
    	
		Set tabs = new HashSet();
		tabs.add("label.authoring.heading.basic");
		tabs.add("label.authoring.heading.advance");
		tabs.add("label.authoring.heading.instructions");
		pageContext.setAttribute("tabs", tabs);
		
	%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
	<title><fmt:message key="label.author.title"/></title>
	
	<!-- depending on user / site preference this will get changed probbably use passed in variable from flash to select which one to use-->
    <link href="<html:rewrite page='/includes/css/aqua.css'/>" rel="stylesheet" type="text/css">
	<!-- this is the custom CSS for hte tool -->
	<link href="<html:rewrite page='/includes/css/tool_custom.css'/>" rel="stylesheet" type="text/css">
    <link href="<html:rewrite page='/includes/css/fckeditor_style.css'/>" rel="stylesheet" type="text/css">

 	<!-- ******************** FCK Editor related javascript & HTML ********************** -->
    <script type="text/javascript" src="${lams}fckeditor/fckeditor.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	
    <script type="text/javascript" src="<html:rewrite page='/includes/javascript/fckcontroller.js'/>"></script>
    <script type="text/javascript" src="<html:rewrite page='/includes/javascript/tabcontroller.js'/>"></script>    
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/prototype.js'/>"></script>

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
        	$("authoringForm").submit();
        }
        
    </script>
    <!-- ******************** END FCK Editor related javascript & HTML ********************** -->
    
    
</head>
<body onLoad="init()">

<html:form action="authoring/update" method="post" styleId="authoringForm"
	focus="title"  enctype="multipart/form-data">
		<html:hidden property="toolContentID"/>
		<html:hidden property="currentTab" styleId="currentTab" />

<h1><fmt:message key="label.authoring.heading" /></h1>

<!-- start tabs -->
<lams:Tabs collection="${tabs}" useKey="true" control="true"/>
<!-- end tab buttons -->
<div class="tabbody">
<table align=center> 	  
				<tr>   
				<td NOWRAP>
						<%@ include file="/common/messages.jsp" %>
				</td>
				</tr> 
</table>

<!-- tab content 1 (Basic) -->
<lams:TabBody id="1" titleKey="label.authoring.heading.basic.desc" page="basic.jsp"/>
<!-- end of content (Basic) -->
      
<!-- tab content 2 (Advanced) -->
<lams:TabBody id="2" titleKey="label.authoring.heading.advance.desc" page="advance.jsp" />
<!-- end of content (Advanced) -->

<!-- tab content 3 (Instructions) -->
<lams:TabBody id="3" titleKey="label.authoring.heading.instructions.desc" page="instructions.jsp" />
<!-- end of content (Instructions) -->


<!-- Button Row -->
		<p align="right">
			<html:link href="javascript:doSubmit('updateContent');" property="submit" styleClass="button">
				<fmt:message key="label.authoring.save.button" />
			</html:link>
			<html:link href="javascript:;" property="cancel"
				onclick="window.close()" styleClass="button">
				<fmt:message key="label.authoring.cancel.button" />
			</html:link>
		</p>
</div>
<lams:HTMLEditor/>
	

</html:form>
</body>
</html:html>
