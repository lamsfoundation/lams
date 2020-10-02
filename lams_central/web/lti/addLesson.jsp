<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams" ><lams:LAMSURL/></c:set>

<lams:html>
<lams:head>
	<title><fmt:message key="title.author.window"/></title>
	
	<lams:css />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap-treeview.css" type="text/css" media="screen" />
	<lams:css suffix="authoring"/>
	<style>
		#title-table {
			padding-top: 10px;
			padding-bottom: 20px;
		}
		#learningDesignTree {
			clear: both;
			padding-top: 10px;
		}
		#preview-button {
			visibility: hidden;
		}
		#design-image {
			margin-top: 10px;
		}
	</style>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/getSysInfo.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>loadVars.jsp"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/openUrls.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/addLesson.js"></script>	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap-treeview.js" ></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/learning-design-treeview.js" ></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.dialogextend.js"></script>	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/dialog.js"></script>
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
		tree = $('#learningDesignTree');
		
		// customise treeview label
		ldTreeview.LABEL_RUN_SEQUENCES_FOLDER = LABEL_RUN_SEQUENCES_FOLDER;
		
		ldTreeview.init(
			'#learningDesignTree', 
			function(event, node) {
	        	// if the selected object is a sequence then we assign the id to the hidden ldId
	         	// also if the name is blank we just add the name of the sequence to the name too.
	         	document.getElementsByName("ldId")[0].value = node.learningDesignId;
	         
				// if a LD is selected
				if (node.state.selected && node.learningDesignId) {
		         	if (document.getElementsByName("title")[0].value == '') {
		         	    document.getElementsByName("title")[0].value = node.label;
		         	}
		         	isSelected = true;
		         	document.getElementById('preview-button').style.visibility='visible';
				} else {
		         	isSelected = false;
		     		document.getElementById('preview-button').style.visibility='hidden';
				}
			},
			function(event, node) {
				if (!node.learningDesignId){
					if (!node.state.expanded) {
						ldTreeview.refresh(tree, node);
					}
					return;
				}
			}
		);
	});

	function openAuthor() {
		window.open(LAMS_URL + 'authoring/openAuthoring.do','Author',
                                    'width=1366,height=768,resizable,scrollbars=yes,status=yes');
	}

	function openPreview() {
    	var ldId = document.getElementsByName("ldId")[0].value;
        var title = document.forms.lessonForm.title.value;
		
		// initialize, create and enter the preview lesson
		$.ajax({
			url : LAMS_URL + 'monitoring/monitoring/initializeLesson.do',
			data : {
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
					url : LAMS_URL + 'monitoring/monitoring/startPreviewLesson.do',
					data : {
						'lessonID' : lessonID
					},
					cache : false,
					dataType : 'text',
					success : function() {
						// open preview pop up window
						window.open(LAMS_URL + 'home/learner.do?mode=preview&lessonID='+lessonID,'Preview',
									'width=1366,height=768,resizable,scrollbars=yes,status=yes');
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
         
		var title = rettrim(document.forms.lessonForm.title.value);
		if ((title == "")||(title == null)) {
			alert("<fmt:message key='authoring.fla.save.sequence.title.prompt' />");
			return false;
		}
         if(!isSelected) {
             //Error Message
             alert("<fmt:message key='label.select.sequence' />");
             return false;
         }
         
         $('#addNewLtiConsumer').button('loading');
	}
		
     // Utility function to trim
     function rettrim(stringToTrim) {
         return stringToTrim.replace(/^\s+|\s+$/g,"");
     }

	</script>
	
</lams:head>

<body class="stripes">
<c:set var="titlePage"><fmt:message key="label.create.lesson"/></c:set>
<lams:Page type="learner" title="${titlePage}">
		
	<%-- Form to Collect ID of Selected LAMS Sequence --%>
    <form name="lessonForm" id="lesson-form" action="lti/startLesson.do" method="post" onSubmit="return confirmSubmit();">
    	<input type="hidden" name="oauth_consumer_key" value="${oauth_consumer_key}">
    	<input type="hidden" name="resource_link_id" value="${resource_link_id}">
        <input type="hidden" name="courseId" value="${courseId}">
        <input type="hidden" name="context_id" value="${context_id}">
        <input type="hidden" name="custom_context_memberships_url" value="${custom_context_memberships_url}">
    	<input type="hidden" name="ldId" id="ldId" value="0">
    	
    	<%-- ContentItemSelectionRequest items --%>
    	<input type="hidden" name="lti_message_type" value="${lti_message_type}">
    	<input type="hidden" name="content_item_return_url" value="${content_item_return_url}">
    	<input type="hidden" name="data" value="${data}">
    	
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
				
				<a class="btn btn-default" href="javascript:openAuthor();"  title="<fmt:message key="label.author.sequence" />">
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
		
		<button type="submit" class="btn btn-primary pull-right" name="addNewLtiConsumer" id="addNewLtiConsumer"
			    data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i> <fmt:message key='authoring.fla.save.button' />">
			<fmt:message key='authoring.fla.save.button' />
		</button>
    </form>
	   
	<div id="footer"></div>
	
</lams:Page>
</body>
	
</lams:html>
 
