<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapId]}" />
<style>
	#itemList .panel-heading.panel-title {
		overflow:hidden;
	}
</style>

<script type="text/javascript">
	function removeQuestion(questionIndex) {
		document.forms.mcAuthoringForm.questionIndex.value=questionIndex;
		document.forms.mcAuthoringForm.action='removeQuestion.do'; 
		
		$('#mcAuthoringForm').ajaxSubmit({ 
    		target:  $('#itemArea'),
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
</script>

<input type="hidden" name="questionIndex" />

<div class="form-group">
    <label for="title"><fmt:message key="label.authoring.title.col"/></label>
    <input type="text" name="title" value="${mcAuthoringForm.title}" class="form-control"/>
</div>

<div class="form-group">
    <label for="instructions"><fmt:message key="label.authoring.instructions.col" /></label>
    <lams:CKEditor id="instructions" value="${mcAuthoringForm.instructions}" contentFolderID="${sessionMap.contentFolderID}"/>
</div>

<div id="itemArea">
	<%@ include file="/authoring/itemlist.jsp"%>
</div>
  
<p>
	<a href="<lams:WebAppURL />authoring/editQuestionBox.do?sessionMapId=${sessionMapId}&KeepThis=true&TB_iframe=true&modal=true"
		class="btn btn-default btn-sm thickbox"> 
		<i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.save.question" /> 
	</a>
</p>
