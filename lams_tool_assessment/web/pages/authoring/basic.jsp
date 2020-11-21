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
	
	.question-type-alert {
		white-space: nowrap;
		display: inline-block;
		margin-top: 3px;
	}
	.newer-version-prompt {
		text-align: left;
		color: orange;
		font-size: 1.3em;
	}
	.question-version-dropdown {
		margin-top: -3px;
	}
	
	.question-version-dropdown .dropdown-menu {
		min-width: 160px;
	}
	
	.question-version-dropdown li a {
		display: inline-block;
	}
	.question-version-dropdown li.disabled a:first-child {
		text-decoration: underline;
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

	function changeItemQuestionVersion(sequenceId, oldQbQuestionUid, newQbQuestionUid) {
		if (oldQbQuestionUid == newQbQuestionUid) {
			return;
		}
		
		var url = "<c:url value="/authoring/changeItemQuestionVersion.do"/>";
		$(questionListTargetDiv).load(
			url,
			{
				referenceSequenceId : sequenceId,
				sessionMapID: "${sessionMapID}",
				newQbQuestionUid : newQbQuestionUid
			},
			function(){
				refreshThickbox();
				
				// check if we are in main authoring environment
				if (typeof window.parent.GeneralLib != 'undefined') {
					// check if any other activities require updating
					let activitiesWithQuestion = window.parent.GeneralLib.checkQuestionExistsInToolActivities(oldQbQuestionUid);
					if (activitiesWithQuestion.length > 1) {
						// update, if teacher agrees to it
						window.parent.GeneralLib.replaceQuestionInToolActivities('${sessionMap.toolContentID}', activitiesWithQuestion,
																				 oldQbQuestionUid, newQbQuestionUid);
					}
				}
			}
		);
	}
	
	function refreshThickbox(){
		tb_init('a.thickbox, area.thickbox, input.thickbox');//pass where to apply thickbox
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
	
	function importQTI(type){
    	window.open('<lams:LAMSURL/>questions/questionFile.jsp?collectionChoice=true&importType='+type,
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

        <a onclick="initNewReferenceHref();return false;" href="javascript:void(0)" class="btn btn-default btn-sm thickbox" id="newQuestionInitHref">  
			<i class="fa fa-lg fa-plus-circle" aria-hidden="true" title="<fmt:message key="label.authoring.basic.add.question.to.pool" />"></i>
		</a>
    
        <!-- Import question widget -->
        <div class="dropdown pull-right" style="padding-left: 1em;">
          <button class="btn btn-default btn-sm dropdown-toggle" type="button" id="dropdownMenu2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
              <i class="fa fa-plus text-primary"></i> <fmt:message key="authoring.title.import"/>&nbsp;
            <span class="caret"></span>
          </button>
          <ul class="dropdown-menu dropdown-menu-right">
              <li><a href="javascript:void(0)" id="divass${appexNumber}CQTI" onClick="javascript:importQTI('word')"><i class="fa fa-file-word-o text-primary"></i> <fmt:message key="label.authoring.basic.import.word"/>...</a></li>
            <li><a href="javascript:void(0)" id="divass${appexNumber}CWord" onClick="javascript:importQTI('qti')"><i class="fa fa-file-code-o text-primary"></i> <fmt:message key="label.authoring.basic.import.qti"/>...</a></li>
          </ul>
        </div>
                
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
				<i class="fa fa-refresh fa-spin fa-2x fa-fw" style="margin: auto; display: block"></i>	
			</div>
		</div>
	</div>
</c:if>
