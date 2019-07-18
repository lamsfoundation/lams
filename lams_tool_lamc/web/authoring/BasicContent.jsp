<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapId]}" />
<c:set var="isAuthoringRestricted" value="${sessionMap.isAuthoringRestricted}" />

<style>
	#itemList .panel-heading.panel-title {
		overflow:hidden;
	}
	#question-bank-div {
    	margin-top: 75px;
	}
	#add-question-div {
		margin-top: -5px;
	}
</style>

<script type="text/javascript">
	$(document).ready(function(){
		//question bank div
		$('#question-bank-collapse').on('show.bs.collapse', function () {
			$('#question-bank-collapse.contains-nothing').load(
				"<lams:LAMSURL/>/searchQB/start.do",
				{
					returnUrl: "<c:url value='/authoring/importQbQuestion.do'/>?sessionMapId=${sessionMapId}",
					toolContentId: ${sessionMap.toolContentID}
				},
				function() {
					$(this).removeClass("contains-nothing");
				}
			);
		})
	});

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

<c:if test="${!isAuthoringRestricted}">
	<div id="add-question-div" class="form-inline form-group pull-right">
		<a href="<lams:WebAppURL />authoring/editQuestionBox.do?sessionMapId=${sessionMapId}&KeepThis=true&TB_iframe=true&modal=true"
			class="btn btn-default btn-sm thickbox"> 
			<i class="fa fa-lg fa-plus-circle" aria-hidden="true" title="<fmt:message key="label.save.question" />"></i>&nbsp;
			<fmt:message key="label.save.question" /> 
		</a>
	</div>

	<!-- Question Bank -->
	<div class="panel-group" id="question-bank-div" role="tablist" aria-multiselectable="true"> 
	    <div class="panel panel-default" >
	        <div class="panel-heading collapsable-icon-left" id="question-bank-heading">
	        	<span class="panel-title">
			    	<a class="collapsed" role="button" data-toggle="collapse" href="#question-bank-collapse" aria-expanded="false" aria-controls="question-bank-collapse" >
		          		<fmt:message key="label.question.bank" />
		        	</a>
	      		</span>
	        </div>
	
			<div id="question-bank-collapse" class="panel-body panel-collapse collapse contains-nothing" role="tabpanel" aria-labelledby="question-bank-heading">
			</div>
		</div>
	</div>
</c:if>
