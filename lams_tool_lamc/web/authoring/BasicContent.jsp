<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
<c:set var="sessionMap" value="${sessionScope[sessionMapId]}" />

<script type="text/javascript">
	function removeQuestion(questionIndex) {
		document.McAuthoringForm.questionIndex.value=questionIndex;
		document.McAuthoringForm.dispatch.value='removeQuestion'; 
		
		$('#authoringForm').ajaxSubmit({ 
    		target:  $('#resourceListArea'),
    		data: { 
				sessionMapId: '${sessionMapId}'
			},
    		iframe: true,
    		success:    function() { 
    			document.McAuthoringForm.dispatch.value="submitAllContent";
    			refreshThickbox();
    	    }
	    });
	}

	function refreshThickbox(){
		tb_init('a.thickbox, area.thickbox, input.thickbox');//pass where to apply thickbox
	};
        
    function importQTI(){
    	window.open('<lams:LAMSURL/>questions/questionFile.jsp?limitType=mc',
    			    'QuestionFile','width=500,height=240,scrollbars=yes');
    }
	
    function saveQTI(formHTML, formName) {
    	document.body.innerHTML += formHTML;
    	var form = document.getElementById(formName);
    	form.action = '<html:rewrite page="/authoring.do?dispatch=saveQTI&sessionMapId=${sessionMapId}"/>';
    	form.submit();
    }
    
    function exportQTI() {
    	var frame = document.getElementById("downloadFileDummyIframe"),
    		title = encodeURIComponent(document.getElementsByName("title")[0].value);
    	frame.src = '<html:rewrite page="/authoring.do?dispatch=exportQTI&sessionMapId=${sessionMapId}" />'
    			+ '&title=' + title;
    }
</script>

<html:hidden property="questionIndex" />

<div class="form-group">
    <label for="title"><fmt:message key="label.authoring.title.col"/></label>
    <html:text property="title" styleClass="form-control"/>
</div>

<div class="form-group">
    <label for="instructions"><fmt:message key="label.authoring.instructions.col" /></label>
    <lams:CKEditor id="instructions" value="${formBean.instructions}" contentFolderID="${sessionMap.contentFolderID}"/>
</div>

<div id="resourceListArea">
	<%@ include file="/authoring/itemlist.jsp"%>
</div>
  
<p>
	<a href="<html:rewrite page="/authoring.do"/>?dispatch=newQuestionBox&sessionMapId=${sessionMapId}&sln=${mcGeneralAuthoringDTO.sln}&showMarks=${mcGeneralAuthoringDTO.showMarks}&randomize=${mcGeneralAuthoringDTO.randomize}&questionsSequenced=${mcGeneralAuthoringDTO.questionsSequenced}&retries=${mcGeneralAuthoringDTO.retries}&KeepThis=true&TB_iframe=true&modal=true"
		class="btn btn-default btn-sm thickbox"> 
		<i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.save.question" /> 
	</a>
</p>

<!-- For exporting QTI packages -->
<iframe id="downloadFileDummyIframe" style="display: none;"></iframe>
