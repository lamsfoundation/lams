<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
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
    Step 2 - start_lesson_proc.jsp
--%>
<%@ page import="blackboard.platform.plugin.PlugInUtil"%>
<%@ page import="blackboard.platform.plugin.PlugInException"%>
<%@ page import="org.lamsfoundation.ld.integration.Constants"%>
<%@ page import="org.lamsfoundation.ld.integration.blackboard.LamsSecurityUtil"%>
<%@ page errorPage="/error.jsp"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>

<bbNG:genericPage title="Add New LAMS" ctxId="ctx">
<bbNG:jsFile href="lib/tree/yahoo-dom-event.js" />
<bbNG:jsFile href="lib/tree/treeview-min.js" />

<bbNG:cssFile href="css/treeview.css" />
<bbNG:cssFile href="css/folders.css" />
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
    String lamsServerUrl = LamsSecurityUtil.getServerAddress();

%>
    <%-- Breadcrumbs --%>
    <bbNG:breadcrumbBar environment="COURSE" isContent="true">
        <bbNG:breadcrumb title="Add New LAMS" />
    </bbNG:breadcrumbBar>

    <%-- Page Header --%>
    <bbNG:pageHeader>    	
        <bbNG:pageTitleBar title="Add New LAMS"/>
    </bbNG:pageHeader>
    
    <%-- Form to Collect ID of Selected LAMS Sequence --%>
    <form name="lesson_form" id="lesson_form" action="start_lesson_proc.jsp" method="post" onSubmit="return confirmSubmit();">
    	<input type="hidden" name="content_id" value="<%=request.getParameter("content_id")%>">
        <input type="hidden" name="course_id" value="<%=request.getParameter("course_id")%>">
    	<input type="hidden" name="sequence_id" id="sequence_id" value="0">
    	
    	<bbNG:dataCollection>
		
            <bbNG:step title="Name and describe the lesson">
                <bbNG:dataElement label="Name" isRequired="true" labelFor="title">
                    <input id="title" type="text" name="title" value="" size="70">
                </bbNG:dataElement>
                
                <bbNG:dataElement label="Description" labelFor="description">
                    <textarea name="description" rows="12" cols="35"></textarea>
                </bbNG:dataElement>
            </bbNG:step> 
            
            <bbNG:step title="Choose Lams sequence">
            
			    <%-- Preview and Author Buttons --%>
			    <div id="buttons" style="float:right;">
			    
			    	<span id="previewbutton" style="visibility:hidden;" class="yui-button yui-link-button">
			    		<span class="first-child">
			    			<button onclick="openPreview(&quot;http:\/\/moodle.lamscommunity.org\/moodle2\/mod\/lamslesson\/preview.php?&quot;, &quot;preview&quot;, 0); return false;">
			    				Preview this lesson
			    			</button>
			    		</span>
			    	</span>
			    	
			    	<span id="authorbutton" class="yui-button yui-link-button">
			    		<span class="first-child">
			    			<button onclick="openAuthor(); return false;" >
			    				Author new LAMS lessons
			    			</button>
			    		</span>
			    	</span>
			    	
			    	
			    	<span id="refresh-button" class="yui-button yui-link-button">
			    		<span class="first-child">
			    			<button onclick="window.location.reload(); return false;">
			    				Refresh
			    			</button>
			    		</span>
			    	</span>
			    </div> 
            
            	<div id="treeDiv"></div>
				<div id="updatesequence"></div>
				<div style="vertical-align: text-bottom; margin-top: 15px;">
					<input type="checkbox" name="isDisplayDesignImage" value="true">  Display image design?

				</div>
				
                <%-- Display LAMS Sequence tree (Using tigra) --%>
		        <script language="JavaScript" type="text/javascript">
		            <!-- 
		     		var tree = new YAHOO.widget.TreeView("treeDiv", <%=learningDesigns%>);
		        	tree.getNodeByIndex(1).expand(true);
		        	tree.getNodeByIndex(2).expand(true);
					tree.render();
					tree.subscribe('clickEvent',function(oArgs) {
					    selectSequence(oArgs.node.data.id, oArgs.node.label);
					});
		  
				//-->
		        </script>
            </bbNG:step>
                
            <bbNG:step title="Lesson options">
                <bbNG:dataElement label="Do you want to make this LAMS lesson visible?" labelFor="isAvailable">
                    <input type="Radio" name="isAvailable" value="true" checked>Yes 
                    <input type="Radio" name="isAvailable" value="false">No
                </bbNG:dataElement>

                <bbNG:dataElement label="Do you want to add a mark/completion column in Gradecenter?" labelFor="isGradecenter">
                    <input type="Radio" name="isGradecenter" value="true" checked>Yes
                    <input type="Radio" name="isGradecenter" value="false">No
                </bbNG:dataElement>


                <bbNG:dataElement label="Track number of views" labelFor="isTracked">
                    <input type="radio" name="isTracked" value="true">Yes
                    <input type="radio" name="isTracked" value="false" checked>No
                </bbNG:dataElement>
                <bbNG:dataElement label="Choose date restrictions">
                    <bbNG:dateRangePicker baseFieldName="lessonAvailability" showTime="true"/>
                </bbNG:dataElement>
            </bbNG:step>
            
            <bbNG:stepSubmit title="Start Lesson" cancelOnClick="back();" />
        
    	</bbNG:dataCollection>
    </form>

    <bbNG:jsBlock>
        <script language="JavaScript" type="text/javascript">
        <!--
        	
            var authorWin = null;
        	var previewWin = null;
            var isSelected = false;
        
            // Open the LAMS Seuence Author Window
            function openAuthor() {
                var authorUrl = '<%=authorUrl%>';
                authorUrl += "&isPostMessageToParent=true";
                
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
                        authorWin = window.open(authorUrl,'aWindow','width=1024,height=768,resizable');
                        authorWin.focus();
                    }catch(e){
                        // popups blocked by a 3rd party
                        alert("Pop-up windows have been blocked by your browser.  Please allow pop-ups for this site and try again");
                    }
                }
                return false;
            }
            
            function unloadHandler(m){
                
            }
            
            // Open the LAMS Seuence Preview Window
            function openPreview() {
            	
                var previewUrl = "preview.jsp?course_id=<%=request.getParameter("course_id")%>&ldId=" + document.getElementsByName("sequence_id")[0].value + "&title=" + document.lesson_form.title.value + "&description=" + document.lesson_form.description.value;
                
                //lams_central
            	//if (parentURL != "") {
            		//window.parent.opener.location.href = parentURL;
            	//}

                
                if(previewWin && previewWin.open && !previewWin.closed){
                    try {
                        previewWin.focus();
                    }catch(e){
                        // popups blocked by a 3rd party
                        alert("Pop-up windows have been blocked by your browser.  Please allow pop-ups for this site and try again");
                    }
                }
                else{
                    try {
                        previewWin = window.open(previewUrl,'pWindow','width=1024,height=768,resizable');
                        previewWin.focus();
                    }catch(e){
                        // popups blocked by a 3rd party
                        alert("Pop-up windows have been blocked by your browser.  Please allow pop-ups for this site and try again");
                    }
                }
                return false;
            }
            
            // Refresh the LAMS sequence list (tigra tree)
           function refreshSeqList() {
               document.getElementById("sequence_id").value="0";
               document.location.reload();
           }
            
            function selectSequence(obj, name){
                // if the selected object is a sequence (id!=0) then we assign the id to the hidden sequence_id
                // also if the name is blank we just add the name of the sequence to the name too.
				
                document.getElementsByName("sequence_id")[0].value = obj;

                if (obj!=0) {
	            	if (document.getElementsByName("title")[0].value == '') {
	            	    document.getElementsByName("title")[0].value = name;
	            	}
	            	isSelected = true;
	            	document.getElementById('previewbutton').style.visibility='visible';
	            	
                } else {
                	isSelected = false;
            		document.getElementById('previewbutton').style.visibility='hidden';
                }
            }
            
            // Do form vaildation
            // Check that a title has been supplied
            function confirmSubmit() {
                var title = rettrim(document.lesson_form.title.value);
				if ((title == "")||(title == null)) {
                    alert("The title is empty. Please enter a title for the LAMS sequence.");
                    return false;
                }
                if(!isSelected) {
                    //Error Message
                    alert("You must select a LAMS Sequence before continuing.");
                    return false;
                }
            }
			
            // Utility function to trim
            function rettrim(stringToTrim) {
                return stringToTrim.replace(/^\s+|\s+$/g,"");
            }
            
            // Go back one page if the user clicks the Cancel Button
            function back() {
                history.go(-1);
            }
            
            function receiveMessage(event) {
            	var lamsServerUrl = "<%=lamsServerUrl%>";
            	
            	// verify the sender of this message
            	if ((lamsServerUrl.substring(0, event.origin.length) === event.origin) && (event.data == "refresh")) {
            		window.location.reload();
            	}
            }
			if (window.addEventListener){
				window.addEventListener("message", receiveMessage, false);
			} else if (window.attachEvent){
				window.attachEvent("message", receiveMessage);
			}
            

        //-->
        </script>
    </bbNG:jsBlock>
    
</bbNG:genericPage>
