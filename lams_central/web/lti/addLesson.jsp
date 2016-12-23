<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-function" prefix="fn"%>
<c:set var="lams" ><lams:LAMSURL/></c:set>

<lams:html>
<lams:head>
	<title><fmt:message key="title.author.window"/></title>
	
	<lams:css />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-redmond-theme.css" type="text/css" media="screen">
	<link type="text/css" rel="stylesheet" href="<lams:LAMSURL/>css/yui/treeview.css">
	<link type="text/css" rel="stylesheet" href="<lams:LAMSURL/>css/yui/folders.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/authoring.css" type="text/css" media="screen">
	<style>
		a, a:hover {
			border-bottom: none;
		}
		/*.ygtvfocus .ygtvlabel, .ygtvfocus .ygtvlabel:link, .ygtvfocus ygtvlabel:visited, .ygtvfocus .ygtvlabel:hover {
			background-color: white;
		}*/
		#title-table {
			padding-top: 10px;
			padding-bottom: 20px;
		}
		#preview-button {
			visibility: hidden;
		}
		#design-image {
			margin-top: 10px;
		}
	</style>
	
	<script type="text/javascript" src="includes/javascript/yui/yahoo-dom-event.js" ></script>
	<script type="text/javascript" src="includes/javascript/yui/animation-min.js"></script>
	<script type="text/javascript" src="includes/javascript/yui/json-min.js" ></script> 
	<script type="text/javascript" src="includes/javascript/yui/treeview-min.js" ></script>
	<script type="text/javascript" src="includes/javascript/getSysInfo.js"></script>
	<script type="text/javascript" src="loadVars.jsp"></script>
	<script type="text/javascript" src="includes/javascript/openUrls.js"></script>
	<script type="text/javascript" src="includes/javascript/addLesson.js"></script>	
	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery.dialogextend.js"></script>	
	<script type="text/javascript" src="includes/javascript/dialog.js"></script>
	<script type="text/javascript">
    	var isSelected = false;
		var tree;
		var userId = '<lams:user property="userID"/>',
			LAMS_URL = '<lams:LAMSURL/>',
			decoderDiv = $('<div />'),
			LABEL_RUN_SEQUENCES_FOLDER = '<fmt:message key="label.tab.lesson.sequence.folder" />',
			LABEL_NAME_INVALID_CHARACTERS = '<fmt:message key="error.lessonname.invalid.characters" />',
			LABELS = {
				<fmt:message key="authoring.fla.navigate.away.confirm" var="NAVIGATE_AWAY_CONFIRM_VAR"/>
				NAVIGATE_AWAY_CONFIRM : decoderDiv.html('<c:out value="${NAVIGATE_AWAY_CONFIRM_VAR}" />').text(),
				<fmt:message key="authoring.fla.page.title" var="AUTHORING_TITLE_VAR"/>
				AUTHORING_TITLE : '<c:out value="${AUTHORING_TITLE_VAR}" />'
			};
	
	$(document).ready(function (){

		// generate LD initial tree; folderContents is declared in newLesson.jsp
		var treeNodes = parseFolderContents(${folderContents});
		
		// there should be no focus, just highlight
		//YAHOO.widget.TreeView.FOCUS_CLASS_NAME = null;
		tree = new YAHOO.widget.TreeView('learningDesignTree', treeNodes);
		tree.setDynamicLoad(function(node, callback){
			// load subfolder contents
			$.ajax({
				url : LAMS_URL + 'home.do',
				data : {
					'method' : 'getFolderContents',
					'folderID' : node.data.folderID
				},
				cache : false,
				async: false,
				dataType : 'json',
				success : function(result) {
					var childNodeData = parseFolderContents(result);
					$.each(childNodeData, function(){
							new YAHOO.widget.TextNode(this, node);
						});
					}
				}
			);
			
			// required by YUI
			callback();
		});

		tree.singleNodeHighlight = true;
		tree.subscribe('clickEvent', function(event){
	         // if the selected object is a sequence (id!=0) then we assign the id to the hidden ldId
	         // also if the name is blank we just add the name of the sequence to the name too.
	         var obj = event.node.data.learningDesignId;
				
	         document.getElementsByName("ldId")[0].value = obj;

	         if ((typeof obj === "undefined") || (obj==0)) {
	         	isSelected = false;
	     		document.getElementById('preview-button').style.visibility='hidden';
	     		
	         } else {
	         	if (document.getElementsByName("title")[0].value == '') {
	         	    document.getElementsByName("title")[0].value = name;
	         	}
	         	isSelected = true;
	         	document.getElementById('preview-button').style.visibility='visible';
	         }
		});
		tree.subscribe('clickEvent',tree.onEventToggleHighlight);
		tree.render();
		
		// expand the first (user) folder
		tree.getRoot().children[0].expand();

	});

	function openPreview() {
    	var ldId = document.getElementsByName("ldId")[0].value;
        var title = document.lessonForm.title.value;
		
		// initialize, create and enter the preview lesson
		$.ajax({
			url : LAMS_URL + 'monitoring/monitoring.do',
			data : {
				'method' : 'initializeLesson',
				'learningDesignID' : ldId,
				'copyType' : 3,
				'lessonName' : "<fmt:message key='authoring.fla.preview.lesson.default.title' />"
			},
			cache : false,
			dataType : 'text',
			success : function(lessonID) {
				if (!lessonID) {
					alert("<fmt:message key='authoring.fla.preview.error' />");
					return;
				}
				
				$.ajax({
					url : LAMS_URL + 'monitoring/monitoring.do',
					data : {
						'method' : 'startPreviewLesson',
						'lessonID' : lessonID
					},
					cache : false,
					dataType : 'text',
					success : function() {
						// open preview pop up window
						window.open(LAMS_URL + 'home.do?method=learner&mode=preview&lessonID='+lessonID,'Preview',
									'width=920,height=700,resizable,scrollbars=yes,status=yes');
					}
				});

			}
		});
	}
     
     // Refresh the LAMS sequence list (tigra tree)
     function refreshSeqList() {
        document.getElementById("ldId").value="0";
        document.location.reload();
     }
     
     // Do form vaildation
     // Check that a title has been supplied
	function confirmSubmit() {
         
		var title = rettrim(document.lessonForm.title.value);
		if ((title == "")||(title == null)) {
			alert("<fmt:message key='authoring.fla.save.sequence.title.prompt' />");
			return false;
		}
         if(!isSelected) {
             //Error Message
             alert("<fmt:message key='label.select.sequence' />");
             return false;
         }
	}
		
     // Utility function to trim
     function rettrim(stringToTrim) {
         return stringToTrim.replace(/^\s+|\s+$/g,"");
     }
     
     function receiveMessage(event) {
     	var lamsServerUrl = "${lams}";
     	alert(""+lamsServerUrl.substring(0, event.origin.length) + "!" + event.origin +"!" +event.data);
     	
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

	</script>
	
</lams:head>

<body class="stripes">
<c:set var="titlePage"><fmt:message key="label.create.lesson"/></c:set>
<lams:Page type="learner" title="${titlePage}">
		
	<%-- Form to Collect ID of Selected LAMS Sequence --%>
    <form name="lessonForm" id="lesson-form" action="lti.do?method=startLesson" method="post" onSubmit="return confirmSubmit();">
    	<input type="hidden" name="oauth_consumer_key" value="${oauth_consumer_key}">
    	<input type="hidden" name="resource_link_id" value="${resource_link_id}">
        <input type="hidden" name="courseId" value="${courseId}">
        <input type="hidden" name="context_id" value="${context_id}">
    	<input type="hidden" name="ldId" id="ldId" value="0">
    	
    	<table class="table  table-no-border">
			<tr>
				<td>
					<label for="title">
						<fmt:message key="authoring.fla.page.dialog.ld.title" />
					</label>
				</td>
				<td>
					<input type="text" name="title" size="70" id="title" value="${title}" class="form-control"/>
				<td>
			</tr>
			
			<tr>
				<td>
					<label for="desc">
						<fmt:message key="authoring.fla.page.prop.description" />
					</label>
				</td>
				<td>
					<textarea name="desc" cols="70" rows="3" id="desc" class="form-control">${desc}</textarea>
				</td>
			</tr>
		</table>
            
		<lams:SimplePanel titleKey="label.choose.sequence">
		
			<%-- Preview and Author Buttons --%>			    
			<div class="pull-right">
				<a id="preview-button" class="btn btn-default" href="javascript:openPreview();" title="<fmt:message key="authoring.fla.preview.lesson.default.title" />">
					<fmt:message key="authoring.fla.preview.lesson.default.title" />
				</a>
				
				<a class="btn btn-default" href="javascript:showAuthoringDialog();" title="<fmt:message key="label.author.sequence" />">
					<fmt:message key="label.author.sequence" />
				</a>
				
				<a class="btn btn-default" href="javascript:window.location.reload();" title="<fmt:message key="authoring.fla.refresh.button" />">
					<fmt:message key="authoring.fla.refresh.button" />
				</a>
			</div>
	            
			<div id="learningDesignTree"></div>
			<div id="updatesequence"></div>
					
			<p>
				<input type="checkbox" name="enableLessonIntro" class="noBorder" id="design-image" value="true"/>
				<label for="design-image">
					<fmt:message key="label.display.design.image" />
				</label>
			</p>
		</lams:SimplePanel>
		
		<input type="submit" class="btn btn-primary pull-right" name="addNewLtiConsumer" value="<fmt:message key='authoring.fla.save.button' />" />
    </form>
	   
	<div id="footer"></div>
	
</lams:Page>
</body>
	
</lams:html>
 
