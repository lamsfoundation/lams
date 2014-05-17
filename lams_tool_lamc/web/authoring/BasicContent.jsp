<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	
	function removeQuestion(questionIndex) {
		document.McAuthoringForm.questionIndex.value=questionIndex;
		document.McAuthoringForm.dispatch.value='removeQuestion'; 
		
		$('#authoringForm').ajaxSubmit({ 
    		target:  $('#resourceListArea'),
    		iframe: true,
    		success:    function() { 
    			document.McAuthoringForm.dispatch.value="submitAllContent";
    			refreshThickbox();
    	    }
	    });
	}
	
	function removeMonitoringQuestion(questionIndex) {
		document.McMonitoringForm.questionIndex.value=questionIndex;
		document.McMonitoringForm.dispatch.value='removeQuestion'; 
		
		$('#monitoringForm').ajaxSubmit({ 
    		target:  $('#resourceListArea'),
    		iframe: true,
    		success:    function() { 
    			document.McMonitoringForm.dispatch.value="submitAllContent";
    			refreshThickbox();
    	    }
	    });
	}

	function resizeIframe() {
		if (document.getElementById('TB_iframeContent') != null) {
		    var height = top.window.innerHeight;
		    if ( height == undefined || height == 0 ) {
		    	// IE doesn't use window.innerHeight.
		    	height = document.documentElement.clientHeight;
		    	// alert("using clientHeight");
		    }
			// alert("doc height "+height);
		    height -= document.getElementById('TB_iframeContent').offsetTop + 60;
		    document.getElementById('TB_iframeContent').style.height = height +"px";
	
			TB_HEIGHT = height + 28;
			tb_position();
		}
	};
	window.onresize = resizeIframe;

	function refreshThickbox(){
		tb_init('a.thickbox, area.thickbox, input.thickbox');//pass where to apply thickbox
	};
        
    function importQTI(){
    	window.open('<lams:LAMSURL/>questions/questionFile.jsp?limitType=mc',
    			    'QuestionFile','width=500,height=200,scrollbars=yes');
    }
	
    function saveQTI(formHTML, formName) {
    	document.body.innerHTML += formHTML;
    	var form = document.getElementById(formName);
    	form.action = '<html:rewrite page="/authoring.do?dispatch=saveQTI&contentFolderID=${mcGeneralAuthoringDTO.contentFolderID}&httpSessionID=${mcGeneralAuthoringDTO.httpSessionID}&toolContentID=${mcGeneralAuthoringDTO.toolContentID}"/>';
    	form.submit();
    }
    
    function exportQTI(){
    	var frame = document.getElementById("downloadFileDummyIframe"),
    		title = encodeURIComponent(document.getElementsByName("title")[0].value);
    	frame.src = '<html:rewrite page="/authoring.do?dispatch=exportQTI&httpSessionID=${mcGeneralAuthoringDTO.httpSessionID}" />'
    			+ '&title=' + title;
    }

</script>

<html:hidden property="questionIndex" />
<table cellpadding="0">
	<tr>
		<td colspan="2">
			<div class="field-name">
				<fmt:message key="label.authoring.title.col"></fmt:message>
			</div>
			<html:text property="title" style="width: 99%;"></html:text>
		</td>
	</tr>

	<tr>
		<td colspan="2">
			<div class="field-name">
				<fmt:message key="label.authoring.instructions.col"></fmt:message>
			</div>
			<lams:CKEditor id="instructions"
				value="${mcGeneralAuthoringDTO.activityInstructions}"
				contentFolderID="${mcGeneralAuthoringDTO.contentFolderID}"></lams:CKEditor>
		</td>
	</tr>
</table>

<div id="resourceListArea">
	<%@ include file="/authoring/itemlist.jsp"%>
</div>

<p>
	<a href="<html:rewrite page="/authoring.do"/>?dispatch=newQuestionBox&contentFolderID=${mcGeneralAuthoringDTO.contentFolderID}&httpSessionID=${mcGeneralAuthoringDTO.httpSessionID}&toolContentID=${mcGeneralAuthoringDTO.toolContentID}&sln=${mcGeneralAuthoringDTO.sln}&showMarks=${mcGeneralAuthoringDTO.showMarks}&randomize=${mcGeneralAuthoringDTO.randomize}&questionsSequenced=${mcGeneralAuthoringDTO.questionsSequenced}&retries=${mcGeneralAuthoringDTO.retries}&KeepThis=true&TB_iframe=true&height=640&width=950&modal=true"
		class="button-add-item thickbox"> 
		<fmt:message key="label.save.question" /> 
	</a>
	
	<a href="#" onClick="javascript:importQTI()" style="margin-left: 40px">
		<fmt:message key="label.authoring.import.qti" />
	</a>
	<a href="#" onClick="javascript:exportQTI()" style="margin-left: 40px">
		<fmt:message key="label.authoring.export.qti" />
	</a>
</p>

<!-- For exporting QTI packages -->
<iframe id="downloadFileDummyIframe" style="display: none;"></iframe>
