<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMapID" value="${assessmentForm.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="isAuthoringRestricted" value="${sessionMap.isAuthoringRestricted}" />
<style>
	#question-bank-div {
		margin-top: 75px;
	}
    
	#question-bank-heading a {
		color:#333
	}
	
	#referencesTable thead {
		background-color: #f5f5f5;
	}
	#referencesTable th {
		font-weight: 500;
		font-style: normal;
	}
	
	#add-question-div {
		margin-top: -5px;
	}
</style>

<script lang="javascript">
	$(document).ready(function(){
		reinitializePassingMarkSelect(true);
		
		//load question bank div
		$('#question-bank-collapse').on('show.bs.collapse', function () {
			$('#question-bank-collapse.contains-nothing').load(
				"<lams:LAMSURL/>/searchQB/start.do",
				{
					returnUrl: "<c:url value='/authoring/importQbQuestion.do'/>?sessionMapID=${sessionMapID}",
					toolContentId: ${sessionMap.toolContentID}
				},
				function() {
					$(this).removeClass("contains-nothing");
				}
			);
		})
	});

	//The panel of assessment list panel
	var questionListTargetDiv = "#itemArea";
	function deleteQuestionReference(idx){
		var	deletionConfirmed = confirm("<fmt:message key="warning.msg.authoring.do.you.want.to.delete"></fmt:message>");

		if (deletionConfirmed) {
			var url = "<c:url value="/authoring/removeQuestionReference.do"/>";
			$(questionListTargetDiv).load(
				url,
				{
					questionReferenceIndex: idx, 
					sessionMapID: "${sessionMapID}",
					referenceMaxMarks: serializeReferenceMaxMarks()
				},
				function(){
					reinitializePassingMarkSelect(false);
					refreshThickbox();
				}
			);
		};
	}
	function upQuestionReference(idx){
		var url = "<c:url value="/authoring/upQuestionReference.do"/>";
		$(questionListTargetDiv).load(
			url,
			{
				questionReferenceIndex: idx, 
				sessionMapID: "${sessionMapID}",
				referenceMaxMarks: serializeReferenceMaxMarks()
			},
			function(){
				refreshThickbox();
			}
		);
	}
	function downQuestionReference(idx){
		var url = "<c:url value="/authoring/downQuestionReference.do"/>";
		$(questionListTargetDiv).load(
			url,
			{
				questionReferenceIndex: idx, 
				sessionMapID: "${sessionMapID}",
				referenceMaxMarks: serializeReferenceMaxMarks()
			},
			function(){
				refreshThickbox();
			}
		);
	}
	function serializeReferenceMaxMarks(){
		var serializedMaxMarks = "";
		$("[name^=maxMark]").each(function() {
			serializedMaxMarks += "&" + this.name + "="  + this.value;
		});
		return serializedMaxMarks;
	}

	function initNewReferenceHref() {
		var questionTypeDropdown = document.getElementById("questionType");
		var questionType = questionTypeDropdown.value;
		
		var newQuestionInitHref = "<c:url value='/authoring/initNewReference.do'/>?sessionMapID=${sessionMapID}&questionType=" + questionType + "&referenceMaxMarks=" + encodeURIComponent(serializeReferenceMaxMarks()) + "&KeepThis=true&TB_iframe=true&modal=true";
		$("#newQuestionInitHref").attr("href", newQuestionInitHref);
	};
	function createEditQuestionHref(idx){
		var editHref = "<c:url value='/authoring/editReference.do'/>?sessionMapID=${sessionMapID}&questionReferenceIndex=" + idx + "&referenceMaxMarks=" + encodeURIComponent(serializeReferenceMaxMarks()) + "&KeepThis=true&TB_iframe=true&modal=true"; 
		$("#edit-ref-" + idx).attr("href", editHref);
	}
	
	function refreshThickbox(){
		tb_init('a.thickbox, area.thickbox, input.thickbox');//pass where to apply thickbox
	};
	function reinitializePassingMarkSelect(isPageFresh){
		var oldValue = (isPageFresh) ? "${assessmentForm.assessment.passingMark}" : $("#passingMark").val();
		$('#passingMark').empty();
		$('#passingMark').append( new Option("<fmt:message key='label.authoring.advance.passing.mark.none' />",0) );
		
		var sumMaxMark = 0;
		$("[name^=maxMark]").each(function() {
			sumMaxMark += eval(this.value);
		});
		
		for (var i = 1; i<=sumMaxMark; i++) {
			var isSelected = (oldValue == i);
		    $('#passingMark').append( new Option(i, i, isSelected, isSelected) );
		}
	};
</script>

<c:if test="${isAuthoringRestricted}">
	<lams:Alert id="edit-in-monitor-while-assessment-already-attempted" type="error" close="true">
		<fmt:message key="label.edit.in.monitor.warning"/>
	</lams:Alert>
</c:if>

<!-- Basic Tab Content -->
<div class="form-group">
    <label for="assessment.title">
    	<fmt:message key="label.authoring.basic.title"/>
    </label>
    <form:input path="assessment.title" cssClass="form-control" maxlength="255"/>
</div>

<div class="form-group">
    <label for="assessment.instructions">
    	<fmt:message key="label.authoring.basic.instruction"/>
    </label>
	<lams:CKEditor id="assessment.instructions" value="${assessmentForm.assessment.instructions}"
			contentFolderID="${assessmentForm.contentFolderID}">
	</lams:CKEditor>
</div>

<div id="itemArea">
	<c:set var="sessionMapID" value="${assessmentForm.sessionMapID}" />
	<c:choose>
		<c:when test="${isAuthoringRestricted}">
			<%@ include file="/pages/authoring/parts/questionlistRestricted.jsp"%>
		</c:when>
		<c:otherwise>
			<%@ include file="/pages/authoring/parts/questionlist.jsp"%>
		</c:otherwise>
	</c:choose>
</div>

<c:if test="${!isAuthoringRestricted}">
	<!-- Dropdown menu for choosing a question type -->
	<div id="add-question-div" class="form-inline form-group pull-right">
		<select id="questionType" class="form-control input-sm">
			<option selected="selected" value="1"><fmt:message key="label.authoring.basic.type.multiple.choice" /></option>
			<option value="2"><fmt:message key="label.authoring.basic.type.matching.pairs" /></option>
			<option value="3"><fmt:message key="label.authoring.basic.type.short.answer" /></option>
			<option value="4"><fmt:message key="label.authoring.basic.type.numerical" /></option>
			<option value="5"><fmt:message key="label.authoring.basic.type.true.false" /></option>
			<option value="6"><fmt:message key="label.authoring.basic.type.essay" /></option>
			<option value="7"><fmt:message key="label.authoring.basic.type.ordering" /></option>
			<option value="8"><fmt:message key="label.authoring.basic.type.mark.hedging" /></option>
			<option value="-1">
				<fmt:message key="label.authoring.basic.type.random.question" />
			</option>
		</select>
		
		<a onclick="initNewReferenceHref();return false;" href="#nogo" class="btn btn-default btn-sm thickbox" id="newQuestionInitHref">  
			<i class="fa fa-lg fa-plus-circle" aria-hidden="true" title="<fmt:message key="label.authoring.basic.add.question.to.pool" />"></i>
		</a>
	</div>

	<!-- Question Bank -->
	<div class="panel-group" id="question-bank-div" role="tablist" aria-multiselectable="true"> 
	    <div class="panel panel-default" >
	        <div class="panel-heading collapsable-icon-left" id="question-bank-heading">
	        	<span class="panel-title">
			    	<a class="collapsed" role="button" data-toggle="collapse" href="#question-bank-collapse" aria-expanded="false" aria-controls="question-bank-collapse" >
		          		<fmt:message key="label.authoring.basic.question.bank.title" />
		        	</a>
	      		</span>
	        </div>
	
			<div id="question-bank-collapse" class="panel-body panel-collapse collapse contains-nothing" role="tabpanel" aria-labelledby="question-bank-heading">
	
			</div>
		</div>
	</div>
</c:if>
