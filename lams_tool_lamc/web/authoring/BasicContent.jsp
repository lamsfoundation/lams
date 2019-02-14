<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapId]}" />
<c:set var="isAuthoringRestricted" value="${sessionMap.isAuthoringRestricted}" />

<script type="text/javascript">
	function removeQuestion(questionIndex) {
		document.forms.mcAuthoringForm.questionIndex.value=questionIndex;
		document.forms.mcAuthoringForm.action='removeQuestion.do'; 
		
		$('#mcAuthoringForm').ajaxSubmit({ 
    		target:  $('#resourceListArea'),
    		data: { 
				sessionMapId: '${sessionMapId}'
			},
    		iframe: true,
    		success:    function() { 
    			document.forms.mcAuthoringForm.action="submitAllContent.do";
    			refreshThickbox();
    	    }
	    });
	}

	function refreshThickbox(){
		tb_init('a.thickbox, area.thickbox, input.thickbox');//pass where to apply thickbox
	};
        
    function importQTI(){
    	window.open('<lams:LAMSURL />questions/questionFile.jsp?limitType=mc',
    			    'QuestionFile','width=500,height=240,scrollbars=yes');
    }
	
    function saveQTI(formHTML, formName) {
    	var form = $(formHTML);
    	form.prop("action", '<lams:WebAppURL />authoring/saveQTI.do?sessionMapId=${sessionMapId}').appendTo(document.body);
    	form.ajaxSubmit({ 
    		target:  $('#resourceListArea'),
    		iframe: true,
    		success:    function() { 
    			document.forms.mcAuthoringForm.action="submitAllContent.do";
    			refreshThickbox();
    	    	form.remove();
    	    }
	    });
    }
    
    function exportQTI() {
    	var frame = document.getElementById("downloadFileDummyIframe"),
    		title = encodeURIComponent(document.getElementsByName("title")[0].value);
		
    	frame.src = '<lams:WebAppURL />authoring/exportQTI.do?sessionMapId=${sessionMapId}&title=' + title;
    }
</script>

<c:if test="${isAuthoringRestricted}">
	<lams:Alert id="edit-in-monitor-while-assessment-already-attempted" type="error" close="true">
		<fmt:message key="label.edit.in.monitor.warning"/>
	</lams:Alert>
</c:if>

<input type="hidden" name="questionIndex" />

<div class="form-group">
    <label for="title"><fmt:message key="label.authoring.title.col"/></label>
    <input type="text" name="title" value="${mcAuthoringForm.title}" class="form-control"/>
</div>

<div class="form-group">
    <label for="instructions"><fmt:message key="label.authoring.instructions.col" /></label>
    <lams:CKEditor id="instructions" value="${mcAuthoringForm.instructions}" contentFolderID="${sessionMap.contentFolderID}"/>
</div>

<div id="resourceListArea">
	<%@ include file="/authoring/itemlist.jsp"%>
</div>
 
<c:if test="${!isAuthoringRestricted}">
	<p>
		<a href="<lams:WebAppURL />authoring/editQuestionBox.do?sessionMapId=${sessionMapId}&KeepThis=true&TB_iframe=true&modal=true"
			class="btn btn-default btn-sm thickbox"> 
			<i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.save.question" /> 
		</a>
	</p>
</c:if>
	
<!-- For exporting QTI packages -->
<iframe id="downloadFileDummyIframe" style="display: none;"></iframe>
