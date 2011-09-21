<%--
    Original Version: 2007 LAMS Foundation
    Updated for Blackboard 9.1 SP6 (including new bbNG tag library) 2011
    Richard Stals (www.stals.com.au)
    Edith Cowan University, Western Australia
--%>
<%--
    Step 1 For Creating a New LAMS Lesson
    Allows the user to (optionally) author a new LAMS lesson
    Then the user must select a LAMS lesson before proceeding to Step 2.

    Step 1 - create.jsp
    Step 2 - start_lesson.jsp
    Step 3 - start_lesson_proc.jsp
--%>
<%@ page import="blackboard.platform.plugin.PlugInUtil"%>
<%@ page import="blackboard.platform.plugin.PlugInException"%>
<%@ page import="org.lamsfoundation.ld.integration.Constants"%>
<%@ page import="org.lamsfoundation.ld.integration.blackboard.LamsSecurityUtil"%>
<%@ page errorPage="/error.jsp"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>

<bbNG:genericPage title="Add New LAMS" ctxId="ctx">
    <bbNG:jsFile href="lib/tigra/tree.js" />
    <bbNG:jsFile href="lib/tigra/tree_tpl.js" />
<%
    // SECURITY!
    // Authorise current user for Course Control Panel (automatic redirect)
    try{
        if (!PlugInUtil.authorizeForCourseControlPanel(request, response))
            return;
    } catch(PlugInException e) {
        throw new RuntimeException(e);
    }

    // Get the Login Request URL for authoring LAMS Lessons
    String authorUrl = LamsSecurityUtil.generateRequestURL(ctx, "author");
    
    // Get the list of Learning Designs
    String learningDesigns = LamsSecurityUtil.getLearningDesigns(ctx, 2);
        // Error checking
        if (learningDesigns.equals("error")) {
            response.sendRedirect("lamsServerDown.jsp");
            return;
        }

%>
    <%-- Breadcrumbs --%>
    <bbNG:breadcrumbBar environment="COURSE" isContent="true">
        <bbNG:breadcrumb title="Add New LAMS" />
    </bbNG:breadcrumbBar>

    <%-- Page Header --%>
    <bbNG:pageHeader>    	
        <bbNG:pageTitleBar title="Add New LAMS"/>
    </bbNG:pageHeader>
    
    <%-- Action Control Bar --%>
    <bbNG:actionControlBar>
    	<bbNG:actionButton id="open_author" url="javascript:openAuthor();" title="Open Author" primary="true"/>     <%-- Open the LAMS Author Window --%>
        <bbNG:actionButton id="refresh" url="javascript:refreshSeqList();" title="Refresh" primary="true"/>         <%-- Refresh the list of LAMS sequences --%>
        <bbNG:actionButton id="next" url="javascript:openNext();" title="Next" primary="true"/>                     <%-- Go to Next Step --%>
    </bbNG:actionControlBar>
    
    <%-- Form to Collect ID of Selected LAMS Sequence --%>
    <form name="workspace_form" id="workspace_form" action="start_lesson.jsp" method="post">
    	<input type="hidden" name="content_id" value="<%=request.getParameter("content_id")%>">
        <input type="hidden" name="course_id" value="<%=request.getParameter("course_id")%>">
    	<input type="hidden" name="sequence_id" id="sequence_id" value="0">
        <%-- Display LAMS Sequence tree (Using tigra) --%>
        <script language="JavaScript" type="text/javascript">
            <!-- 
                var TREE_ITEMS = <%=learningDesigns%>;            		
                var tree = new tree(TREE_ITEMS, TREE_TPL);	
            //-->
        </script>
    </form>


    <bbNG:jsBlock>
        <script language="JavaScript" type="text/javascript">
        <!--
            var authorWin = null;
            var isSelected = false;
        
            // Open the LAMS Seuence Author Window
            function openAuthor() {
                authorUrl = '<%=authorUrl%>';
                authorUrl += "&notifyCloseURL=";
                
                if(authorWin && authorWin.open && !authorWin.closed){
                    try {
                        authorWin.focus();
                    }catch(e){
                        // popups blocked by a 3rd party
                        alert("Pop-up windows have been blocked by your browser.  Please allow pop-ups for this site and try again");
                    }
                }
                else{
                    try {
                        authorWin = window.open(authorUrl,'aWindow','width=800,height=600,resizable');
                        authorWin.focus();
                    }catch(e){
                        // popups blocked by a 3rd party
                        alert("Pop-up windows have been blocked by your browser.  Please allow pop-ups for this site and try again");
                    }
                }
            }
            
            // Refresh the LAMS sequence list (tigra tree)
            function refreshSeqList() {
                document.getElementById("sequence_id").value="0";
                document.location.reload();
            }
            
            // Go to Step 2
            function openNext() {
                if(isSelected) {
                    //Submit Form
                    document.getElementById("workspace_form").submit();
                } else {
                    //Error Message
                    alert("You must select a LAMS Sequence before continuing.");
                }
            }
            
            //Executed when a seuqence is selected
            //Set the flag and form element
            function selectSequence(id) {
                document.getElementById("sequence_id").value=id;
                isSelected = true;
            }

        //-->
        </script>
    </bbNG:jsBlock>
    
</bbNG:genericPage>