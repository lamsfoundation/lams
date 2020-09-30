<%@ include file="/common/taglibs.jsp"%>

<script lang="javascript">
	$(document).ready(function(){
		reinitializePassingMarkSelect(true);
		
		//load question bank div
		$('#question-bank-collapse').on('show.bs.collapse', function () {
			$('#question-bank-collapse.contains-nothing').load(
				"<lams:LAMSURL/>searchQB/start5.do",
				{
					returnUrl: "<c:url value='/authoring/importQbQuestion.do'/>?sessionMapID=${sessionMapID}",
					toolContentId: ${sessionMap.toolContentID}
				},
				function() {
					$(this).removeClass("contains-nothing");
				}
			);
		});

    	//handler for "delete" buttons
    	$(document).on("click", '.delete-reference-link', function() {
    		var	deletionConfirmed = confirm("<fmt:message key="warning.msg.authoring.do.you.want.to.delete"></fmt:message>");

    		if (deletionConfirmed) {
    			var sequenceId = $('.reference-sequence-id', $(this).parents('tr')).val();

    			$(questionListTargetDiv).load(
    				"<c:url value="/authoring/removeQuestionReference.do"/>",
    				{
    					referenceSequenceId: sequenceId, 
    					sessionMapID: "${sessionMapID}"
    				},
    				function(){
    					reinitializePassingMarkSelect(false);
    				}
    			);
    		};
        });

    	//handler for "maxMark" inputs
    	$(document).on("change", '.max-mark-input', function() {
    		var sequenceId = $('.reference-sequence-id', $(this).parents('tr')).val();
    		
			$.ajax({ 
			    url: '<c:url value="/authoring/changeMaxMark.do"/>',
				type: 'POST',
				data: {
					sessionMapID: "${sessionMapID}",
					referenceSequenceId: sequenceId,
					maxMark: this.value
				}
			});
        });
	});

	//The panel of assessment list panel
	var questionListTargetDiv = "#itemArea";
	
	function initNewReference() {
		var questionType = $('#questionType').val(),
			newQuestionInitUrl = "<c:url value='/authoring/initNewReference.do'/>?sessionMapID=${sessionMapID}"
								  + "&questionType=" + questionType,
			modal = $('#qb-question-authoring-modal');
			
		$('iframe', modal).attr('src', newQuestionInitUrl);
		modal.modal('show');
	};

	//handler for "edit" buttons
	function editReference(link) {
		var sequenceId = $('.reference-sequence-id', $(link).parents('tr')).val(),
			editUrl = "<c:url value='/authoring/editReference.do'/>?sessionMapID=${sessionMapID}" 
						+ "&referenceSequenceId=" + sequenceId,
			modal = $('#qb-question-authoring-modal');
		
		$('iframe', modal).attr('src', editUrl);
		modal.modal('show');
    }
 	
	function refreshThickbox() {
		// dummy method for current QB question authoring to work
	}
	
	// for consistency with yet not rewritten QB question authoring
	function tb_remove() {
		$('#qb-question-authoring-modal').modal('hide');
	}
	
	function toggleQuestionRequired(icon){
		var sequenceId = $('.reference-sequence-id', $(icon).parents('tr')).val();
		
		$.ajax({ 
		    url: '<c:url value="/authoring/toggleQuestionRequired.do"/>',
			type: 'POST',
			dataType : 'text',
			data: {
				sessionMapID: "${sessionMapID}",
				referenceSequenceId: sequenceId
			},
			success(isRequired) {
				if (isRequired == 'true') {
				 	$(icon).addClass('text-danger');
				} else if (isRequired == 'false') {
					$(icon).removeClass('text-danger');
				}
			}
		});
	}
	
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
	
	function importQTI(){
    	window.open('<lams:LAMSURL/>questions/questionFile.jsp?collectionChoice=true',
			'QuestionFile','width=500,height=370,scrollbars=yes');
    }
	
 	// this method is called by QTI questionChoice.jsp 
    function saveQTI(formHTML, formName) {
    	var form = $($.parseHTML(formHTML));
    	// first, save questions in the QB
		$.ajax({
			url: '<lams:LAMSURL />imsqti/saveQTI.do?<csrf:token/>',
			data: form.serializeArray(),
			type: "POST",
			dataType: 'text',
			// the response is a comma-delimited list of QB question UIDs, for example 4,5,65 
			success: function(qbQuestionUids) {
				$.ajax({
					type: "POST",
					url: '<lams:WebAppURL />authoring/importQbQuestions.do',
					data: {
						// send QB question uids to MCQ authoring controller
						'qbQuestionUids' : qbQuestionUids,
						'sessionMapID'   : '${sessionMapID}'
					},
					dataType: 'html',
					success: function(response) {
						// question list gets refreshed
						$(questionListTargetDiv).html(response);
					}
				});
			}
		});
    }
</script>

<div id="questionlist" class="col-12 p-0">
	<div id="itemArea">
		<c:choose>
			<c:when test="${isAuthoringRestricted}">
				<%@ include file="/pages/authoring/parts/questionlistRestricted.jsp"%>
			</c:when>
			<c:otherwise>
				<%@ include file="/pages/authoring/parts/questionlist5.jsp"%>
			</c:otherwise>
		</c:choose>
	</div>
	
	<c:if test="${!isAuthoringRestricted}">
		<!-- Dropdown menu for choosing a question type -->
		<div id="add-question-div" class="d-flex justify-content-end">
			<select id="questionType" class="form-control form-control-select" style="width: inherit" 
					aria-label="<fmt:message key="label.authoring.basic.question.list.title" />">
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
			<button onclick="javscript:initNewReference()" type="button" class="btn btn-primary mx-2">  
				<i class="fa fa-lg fa-plus-circle" aria-hidden="true" title="<fmt:message key="label.authoring.basic.add.question.to.pool" />"></i>
			</button>
			<button onClick="javascript:importQTI()" type="button" class="btn btn-primary">
				<fmt:message key="label.authoring.basic.import.qti" /> 
			</button>
		</div>
		
		<!-- Question Bank -->
	    <div class="card mt-5" >
			<button class="card-header btn btn-block text-left collapsed collapsable-icon-left" type="button"
					data-toggle="collapse" data-target="#question-bank-collapse" aria-expanded="false" aria-controls="question-bank-collapse">
	       		<fmt:message key="label.authoring.basic.question.bank.title" />
	        </button>
			<div id="question-bank-collapse" class="card-body collapse contains-nothing" aria-labelledby="question-bank-heading">
				<i class="fa fa-refresh fa-spin fa-2x fa-fw" style="margin: auto; display: block"></i>	
			</div>
		</div>
		
		<div id="qb-question-authoring-modal" class="modal p-3" tabindex="-1" data-backdrop="static" data-keyboard="false">
			<div class="modal-content">
				<div class="modal-body">
		        	<iframe></iframe>
		    	</div>
			</div>
		</div>
		
	</c:if>
</div>