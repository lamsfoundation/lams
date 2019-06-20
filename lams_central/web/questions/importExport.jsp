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


	
	<!-- For exporting QTI packages -->
	<iframe id="downloadFileDummyIframe" style="display: none;"></iframe>
