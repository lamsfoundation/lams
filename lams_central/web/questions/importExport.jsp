<%@ include file="/common/taglibs.jsp"%>

<script lang="javascript">	
    function importQTI(){
    	window.open('<lams:LAMSURL/>questions/questionFile.jsp',
			'QuestionFile','width=500,height=240,scrollbars=yes');
    }
	
    function saveQTI(formHTML, formName) {
    	var form = $($.parseHTML(formHTML));
		$.ajax({
			type: "POST",
			url: '<c:url value="/imsqti/saveQTI.do" />?contentFolderID=${contentFolderID}&collectionUid={collectionUid}',
			data: form.serializeArray(),
			success: function(response) {
				//$(questionListTargetDiv).html(response);
			}
		});
    }

    function exportQTI(){
    	var frame = document.getElementById("downloadFileDummyIframe");
    	frame.src = '<c:url value="/imsqti/exportCollectionAsQTI.do" />?collectionUid=${collectionUid}';
    }
</script>
		
	<div class="btn-group btn-group-xs pull-right" role="group">
		<a href="#nogo" onClick="javascript:importQTI()" class="btn btn-default">
			<fmt:message key="label.import.qti" /> 
		</a>
		<a href="#nogo" onClick="javascript:exportQTI()" class="btn btn-default">
			<fmt:message key="label.export.qti" />
		</a>
	</div>

	<!-- Dropdown menu for choosing a question type -->
	<div class="form-inline form-group">
		<select id="questionType" class="form-control input-sm">
			<option selected="selected"><fmt:message key="label.question.type.multiple.choice" /></option>
			<option><fmt:message key="label.question.type.matching.pairs" /></option>
			<option><fmt:message key="label.question.type.short.answer" /></option>
			<option><fmt:message key="label.question.type.numerical" /></option>
			<option><fmt:message key="label.question.type.true.false" /></option>
			<option><fmt:message key="label.question.type.essay" /></option>
			<option><fmt:message key="label.question.type.ordering" /></option>
			<option><fmt:message key="label.question.type.mark.hedging" /></option>
		</select>
		
		<a onclick="createNewQuestionInitHref();return false;" href="" class="btn btn-default btn-sm button-add-item thickbox" id="newQuestionInitHref">  
			<fmt:message key="label.add.question.to.pool" />
		</a>
	</div>
	<br><br>
	
	<!-- For exporting QTI packages -->
	<iframe id="downloadFileDummyIframe" style="display: none;"></iframe>
