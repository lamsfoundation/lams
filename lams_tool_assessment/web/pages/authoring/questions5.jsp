<%@ include file="/common/taglibs.jsp"%>

<style>
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
				"<lams:LAMSURL/>searchQB/start5.do",
				{
					returnUrl: "<c:url value='/authoring/importQbQuestion.do'/>?sessionMapID=${sessionMapID}",
					toolContentId: ${sessionMap.toolContentID}
				},
				function() {
					$(this).removeClass("contains-nothing");
				}
			);
		})

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
    					refreshThickbox();
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
	function initNewReferenceHref() {
		var questionTypeDropdown = document.getElementById("questionType");
		var questionType = questionTypeDropdown.value;
		
		var newQuestionInitHref = "<c:url value='/authoring/initNewReference.do'/>?sessionMapID=${sessionMapID}"
			+ "&questionType=" + questionType 
			+ "&KeepThis=true&TB_iframe=true&modal=true";
		$("#newQuestionInitHref").attr("href", newQuestionInitHref);
	};

	//handler for "edit" buttons
	function editReference(link) {
		var sequenceId = $('.reference-sequence-id', $(link).parents('tr')).val();
    	
		var editHref = "<c:url value='/authoring/editReference.do'/>?sessionMapID=${sessionMapID}" 
		+ "&referenceSequenceId=" + sequenceId 
		+ "&KeepThis=true&TB_iframe=true&modal=true";
		$(link).attr("href", editHref);
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
						refreshThickbox();
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
				<%@ include file="/pages/authoring/parts/questionlist.jsp"%>
			</c:otherwise>
		</c:choose>
	</div>
	
	<c:if test="${!isAuthoringRestricted}">
		<!-- Dropdown menu for choosing a question type -->
		<ul id="add-question-div" class="list-inline bottom">
			<li class="list-inline-item">
				<select id="questionType" class="form-control form-control-select" aria-label="<fmt:message key="label.authoring.basic.question.list.title" />">
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
			</li>
			<li class="list-inline-item">
				<a onclick="initNewReferenceHref(); return false;" href="#nogo" class="btn btn-primary thickbox" id="newQuestionInitHref">  
					<i class="fa fa-lg fa-plus-circle" aria-hidden="true" title="<fmt:message key="label.authoring.basic.add.question.to.pool" />"></i>
				</a>
			</li>
			<li class="list-inline-item">
				<a href="#nogo" onClick="javascript:importQTI()" class="btn btn-primary">
					<fmt:message key="label.authoring.basic.import.qti" /> 
				</a>
			</li>
		</ul>
		
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
	</c:if>
</div>