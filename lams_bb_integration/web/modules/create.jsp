<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ page isELIgnored="false" %>

<!DOCTYPE HTML>
<%--
    Step 1 For Creating a New LAMS Lesson
    Allows the user to (optionally) author a new LAMS lesson
    Then the user must select a LAMS lesson before proceeding to Step 2.

    Step 1 - create.jsp
    Step 2 - /LessonManager?method=start
--%>
<%@ page errorPage="/error.jsp"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<%@ taglib uri="/tags-core" prefix="c"%>

<bbNG:genericPage title="Add New LAMS" ctxId="ctx">
<bbNG:cssFile href="../includes/css/treeview.css" />
<bbNG:cssFile href="../includes/css/folders.css" />

<bbNG:jsFile href="includes/javascript/yahoo-dom-event.js" />
<bbNG:jsFile href="includes/javascript/treeview-min.js" />
<bbNG:jsFile href="includes/javascript/jquery.js" />
<bbNG:jsFile href="includes/javascript/openLamsPage.js" />
<bbNG:jsBlock>
	<script type="text/javascript">
	var $j = jQuery.noConflict();
	
	$j(document).ready(function(){
		
		// generate LD initial tree; folderContents is declared in newLesson.jsp
		var treeNodes = parseFolderContents(${learningDesigns});
		
		tree = new YAHOO.widget.TreeView('learningDesignTree', treeNodes);
		tree.setDynamicLoad(function(node, callback){
			
			var folderId = node.data.folderID;
			
			// load subfolder contents
			$j.ajax({
				url : '../LamsLearningDesign',
				data : 'course_id=${param.course_id}&folderId=' + folderId,
				cache : false,
				async: false,
				dataType : 'json',
				success : function(result) {
					var childNodeData = parseFolderContents(result);
					$j.each(childNodeData, function(){
						new YAHOO.widget.TextNode(this, node);
					});
				}
			});
			
			// required by YUI
			callback();
		});
		
		tree.singleNodeHighlight = true;
		tree.subscribe('clickEvent',function(event) {
		    selectSequence(event.node.data.learningDesignId, event.node.label);
		});
		tree.subscribe('clickEvent',tree.onEventToggleHighlight);
		tree.render();
		
		// expand the first (user) folder
		tree.getRoot().children[0].expand();

	});	
	
	/**
	 * Parses response in JSON format into readable for YUI.
	 */
	 function parseFolderContents(nodeJSON) {
			var result = [];

			if (nodeJSON.folders) {
				$j.each(nodeJSON.folders, function(){
					result.push({
						'type'            : 'text',
						'label'           : this.isRunSequencesFolder ? "Run sequences" : this.name,
						'folderID'		   : this.folderID
					});
				});
			}
			if (nodeJSON.learningDesigns) {
				$j.each(nodeJSON.learningDesigns, function(){
					result.push({
						'type'             : 'text',
					  	'label'            : this.name,
					  	'isLeaf'           : true,
					  	'learningDesignId' : this.learningDesignId
					});
				});
			}
			
			return result;
		}

	</script>
</bbNG:jsBlock>

    <%-- Breadcrumbs --%>
    <bbNG:breadcrumbBar environment="COURSE" isContent="true">
        <bbNG:breadcrumb title="Add New LAMS" />
    </bbNG:breadcrumbBar>

    <%-- Page Header --%>
    <bbNG:pageHeader>    	
        <bbNG:pageTitleBar title="Add New LAMS"/>
    </bbNG:pageHeader>
    
    <%-- Form to Collect ID of Selected LAMS Sequence --%>
    <form name="lesson_form" id="lesson_form" action="../LessonManager?method=start" method="post" onSubmit="return confirmSubmit();">
    		<input type="hidden" name="content_id" value="${param.content_id}">
        <input type="hidden" name="course_id" value="${param.course_id}">
    		<input type="hidden" name="sequence_id" id="sequence_id" value="0">
    	
    		<bbNG:dataCollection>
		
            <bbNG:step title="Name and describe the lesson">
                <bbNG:dataElement label="Name" isRequired="true" labelFor="title">
                    <input id="title" type="text" name="title" value="" size="70">
                </bbNG:dataElement>
                
                <bbNG:dataElement label="Description" labelFor="descriptiontext">
                    <bbNG:textbox name="description" text="" isContentLinking="true"/>
                </bbNG:dataElement>
            </bbNG:step> 
            
            <bbNG:step title="Choose Lams sequence">
            
			    <%-- Preview and Author Buttons --%>
			    <div id="buttons" style="float:right;">
			    
			    	<span id="previewbutton" style="visibility:hidden;" class="yui-button yui-link-button">
			    		<span class="first-child">
			    			<button onclick="openPreview(); return false;">
			    				Preview this lesson
			    			</button>
			    		</span>
			    	</span>
			    	
			    	<span id="authorbutton" class="yui-button yui-link-button">
			    		<span class="first-child">
			    			<button onclick="openAuthor('${param.course_id}'); return false;" >
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
            
            	<div id="learningDesignTree"></div>
				<div id="updatesequence"></div>
				<div style="vertical-align: text-bottom; margin-top: 15px;">
					<input type="checkbox" name="isDisplayDesignImage" value="true">  Display image design?
				</div>
            </bbNG:step>
                
            <bbNG:step title="Lesson options">
                <bbNG:dataElement label="Do you want to make this LAMS lesson visible?" labelFor="isAvailable">
                    <input type="Radio" name="isAvailable" value="true" checked>Yes 
                    <input type="Radio" name="isAvailable" value="false">No
                </bbNG:dataElement>

                <bbNG:dataElement label="Do you want to add a mark/completion column in Gradecenter?" labelFor="isGradecenter">
                    <input type="Radio" name="isGradecenter" value="true">Yes
                    <input type="Radio" name="isGradecenter" value="false" checked>No
                </bbNG:dataElement>

                <bbNG:dataElement label="Track number of views" labelFor="isTracked">
                    <input type="Radio" name="isTracked" value="true">Yes
                    <input type="Radio" name="isTracked" value="false" checked>No
                </bbNG:dataElement>
                
				<bbNG:dataElement label="Learners can restart the lesson?" labelFor="isAllowLearnerRestart">
                    <input type="Radio" name="isAllowLearnerRestart" value="true">Yes
                    <input type="Radio" name="isAllowLearnerRestart" value="false" checked>No
                </bbNG:dataElement>
                
                <bbNG:dataElement label="Choose date restrictions">
                    <bbNG:dateRangePicker baseFieldName="lessonAvailability" showTime="true"/>
                </bbNG:dataElement>
            </bbNG:step>
            
            <bbNG:stepSubmit title="Start Lesson" 
            			cancelUrl="/webapps/blackboard/content/listContentEditable.jsp?content_id=${param.content_id}&course_id=${param.course_id}" />
        
    		</bbNG:dataCollection>
    </form>

    <bbNG:jsBlock>
        <script type="text/javascript">
            var authorWin = null;
        		var previewWin = null;
            var isSelected = false;
            
            // Open the LAMS Seuence Preview Window
            function openPreview() {
            	
                var previewUrl = "../openLamsPage?method=openPreview&course_id=${param.course_id}&ldId=" + document.getElementsByName("sequence_id")[0].value + "&title=" + document.lesson_form.title.value;
                
               if (previewWin && !previewWin.closed) {
                    try {
                        previewWin.focus();
                    } catch(e) {
                        // popups blocked by a 3rd party
                        alert("Pop-up windows have been blocked by your browser.  Please allow pop-ups for this site and try again");
                    }
                    
                } else {
                    try {
                        previewWin = window.open(previewUrl,'pWindow','width=1280,height=720,resizable,scrollbars=yes');
                        previewWin.focus();
                    } catch(e) {
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

                if ((typeof obj === "undefined") || (obj==0)) {
                	isSelected = false;
            		document.getElementById('previewbutton').style.visibility='hidden';
            		
                } else {
	            	if (document.getElementsByName("title")[0].value == '') {
	            	    document.getElementsByName("title")[0].value = name;
	            	}
	            	isSelected = true;
	            	document.getElementById('previewbutton').style.visibility='visible';
                }
            }
            
            // Do form vaildation. Check that a title has been supplied
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
        </script>
    </bbNG:jsBlock>
    
</bbNG:genericPage>
