<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMapID" value="${authoringForm.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<style>
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
					returnUrl: "<c:url value='/authoring/importQbQuestion.do'/>?sessionMapID=${authoringForm.sessionMapID}",
					toolContentId: ${sessionMap.toolContentID}
				},
				function() {
					$(this).removeClass("contains-nothing");
				}
			);
		})
	});

	var itemTargetDiv = "#itemArea";
	function removeQuestion(questionIndex){
		var	deletionConfirmed = confirm("<fmt:message key="warning.msg.authoring.do.you.want.to.delete"></fmt:message>");
		
		if (deletionConfirmed) {
			var url = "<c:url value="/authoring/removeQuestion.do"/>";
			$(itemTargetDiv).load(
				url,
				{
					questionIndex: questionIndex,
					sessionMapID: "${sessionMapID}"
				},
				function(){
					refreshThickbox();
				}
			);
		};
	}

	function moveQuestion(questionIndex, actionMethod){
		var url = "<c:url value="/authoring/"/>" + actionMethod + ".do";
		$(itemTargetDiv).load(
			url,
			{
				questionIndex: questionIndex,
				sessionMapID: "${sessionMapID}"
			},
			function(){
				refreshThickbox();
			}
		);
	}

	function refreshThickbox() {
		tb_init('a.thickbox, area.thickbox, input.thickbox');//pass where to apply thickbox
	}
</script>

<form:hidden path="questionIndex" />

<div class="form-group">
    <label for="title">
    	<fmt:message key="label.authoring.title.col"/>
    </label>
    <form:input path="qa.title" id="title" cssClass="form-control"/>
</div>
<div class="form-group">
    <label for="instructions">
    	<fmt:message key="label.authoring.instructions.col" />
    </label>
    <lams:CKEditor id="qa.instructions" value="${authoringForm.qa.instructions}" contentFolderID="${authoringForm.contentFolderID}">
    </lams:CKEditor>
</div>

<div id="itemArea">
	<%@ include file="/authoring/itemlist.jsp"%>
</div>

<div id="add-question-div" class="form-inline form-group pull-right">
	<c:set var="addItemUrl" >
		<c:url value='/authoring/newQuestionBox.do'/>?sessionMapID=${authoringForm.sessionMapID}&KeepThis=true&TB_iframe=true&modal=true
	</c:set>
	
	<a href="${addItemUrl}" id="addTopic" class="btn btn-default btn-sm thickbox">
		<i class="fa fa-lg fa-plus-circle" aria-hidden="true" title="<fmt:message key="label.add.new.question" /> "></i>
		&nbsp;
		<fmt:message key="label.add.new.question" /> 
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
