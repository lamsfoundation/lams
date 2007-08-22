<%@ page import="blackboard.platform.plugin.PlugInUtil"%>
<%@ page import="org.lamsfoundation.ld.integration.Constants"%>
<%@ page import="org.lamsfoundation.ld.integration.blackboard.LamsSecurityUtil"%>
<%@ page errorPage="/error.jsp"%>

<%@ taglib uri="/bbData" prefix="bbData"%>
<%@ taglib uri="/bbUI" prefix="bbUI"%>

<bbData:context  id="ctx">
<bbUI:docTemplate title = "Add new LAMS">
<head>
	<link type="text/css" rel="stylesheet" href="css/bb.css" />
</head>
<%
	// SECURITY!
    //AccessManagerService accessManager = (AccessManagerService) BbServiceManager.lookupService(AccessManagerService.class);
	if (!PlugInUtil.authorizeForCourseControlPanel(request,response)){
		//accessManager.sendAccessDeniedRedirect(request,response);
		//TODO: redirect user to login page, since sendAccessDeniedRedirect is deprecated another way is needed
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		return;
	}
	String authorUrl = LamsSecurityUtil.generateRequestURL(ctx, "author");
	String learningDesigns = LamsSecurityUtil.getLearningDesigns(ctx, 2);
	
	// Error checking
	if (learningDesigns.equals("error"))
	{
		response.sendRedirect("lamsServerDown.jsp");
	}

%>

<bbUI:breadcrumbBar handle="control_panel" isContent="true" >
    <bbUI:breadcrumb>Add new LAMS</bbUI:breadcrumb>
</bbUI:breadcrumbBar>

<bbUI:titleBar iconUrl ="/images/ci/icons/bookopen_u.gif">Add new LAMS</bbUI:titleBar>





<form name="workspace_form" id="workspace_form" action="start_lesson.jsp" method="post">
    
    
    <input type="hidden" name="content_id" value="<%=request.getParameter("content_id")%>">
    <input type="hidden" name="course_id" value="<%=request.getParameter("course_id")%>">
    <input type="hidden" name="sequence_id" id="sequence_id" value="0">
    
    
    <bbUI:step title="Select an existing sequence from the LAMS workspace.">
        <bbUI:dataElement> 
        <br>                    
            <script language="JavaScript" type="text/javascript" src="../lib/tigra/tree.js"></script>
			<script language="JavaScript" type="text/javascript" src="../lib/tigra/tree_tpl.js"></script>
            <script language="JavaScript" type="text/javascript">
            	<!-- 
            	 	var TREE_ITEMS = <%=learningDesigns%>;            		
					var tree = new tree(TREE_ITEMS, TREE_TPL);	
					
				//-->
			</script>
		<br>
		</bbUI:dataElement>
	 </bbUI:step>
	 <bbUI:step title="Open author, refresh the workspace or start the chosen lesson.">		
        <bbUI:dataElement> 
           	<input type="button" class="button"name="author" onClick="openAuthor();" value="Open Author">
            <input type="button" class="button"name="action" onClick="refreshSeqList();" value="Refresh">
            <input id="disabledNextButton" class="disabled" type="button" name="disabledNextButton" value="Next" disabled="true">
            <input id="nextButton" class="button" type="hidden" name="nextButton" onClick="openNext();" value="Next">
     	</bbUI:dataElement> 
     	<br>
     </bbUI:step>
    
</form>


<script language="JavaScript" type="text/javascript">
<!--
    var authorWin = null;

    function openAuthor()
    {
    	authorUrl = '<%=authorUrl%>'; 
    	if(authorWin && authorWin.open && !authorWin.closed){
    	
            authorWin.focus();
        }
        else{
            authorWin = window.open(authorUrl,'aWindow','width=800,height=600,resizable');
            authorWin.focus();
        }
    }
    
    function refreshSeqList()
    {
    	document.getElementById("sequence_id").value="0";
    	document.location.reload();
    }
    
    function openNext()
    {
    }
    
    function selectSequence(id) 
    {
		//document.workspace_form.nextButton.class= "button"; 
		//document.getElementById("nextButton").disabled = false;
		document.getElementById("nextButton").type = "submit";  
		document.getElementById("disabledNextButton").type = "hidden";
		
		//document.getElementById("nextButton").class = "button"; 	
    	document.getElementById("sequence_id").value=id;
	}
//-->
</script>



</bbUI:docTemplate>
</bbData:context>