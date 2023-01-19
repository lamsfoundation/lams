<%@ include file="/taglibs.jsp"%>
<% pageContext.setAttribute("newLineChar", "\r\n"); %>

<style>
	#aes-tab .nav-tabs .nav-item {
		min-width: 200px;
	}
</style>
<script>
	var tblSelectedAeToolContentId = tblSelectedAeToolContentId || '${empty param.toolContentID ? aeToolContentIds[0] : param.toolContentID}';

	$(document).ready(function(){
		loadAePane(tblSelectedAeToolContentId, 'studentChoices');
		
		// change attempted and all learners numbers
		$('#aes-tab .nav-tabs li').bind('click', function (event) {
			var link = $(event.target);
			// store which tab was cliked last
			tblSelectedAeToolContentId = link.data("tool-content-id");
			
			loadAePane(tblSelectedAeToolContentId);
		});
	});
	
	function loadAePane(targetToolContentId, contentType){
		$('#aes-tab .tab-pane').each(function(){
			var aePane = $(this).hide(),
				toolContentId = aePane.data('toolContentId'),
				toolType = aePane.data('toolType'),
				nav = $('#aes-tab .nav-tabs a[data-tool-content-id="' + toolContentId + '"]');
	
			if (toolContentId == targetToolContentId) {
				var url = null;
				if (toolType == 'd') {
					url =  "<lams:LAMSURL/>tool/ladoku11/monitoring/ae.do?toolContentID="  + toolContentId;
				} else if (contentType == 'default' || (!contentType && aePane.data('contentType') == 'default')){
					// contentType is an extra setting saying which content from the given tool type to display
					url = "<lams:LAMSURL/>tool/laasse10/tblmonitoring/assessment.do?toolContentID="  + toolContentId;
					aePane.data('contentType', 'default');
				} else {
					url =  "<lams:LAMSURL/>tool/laasse10/tblmonitoring/aesStudentChoices.do?toolContentID="  + toolContentId;
					aePane.data('contentType', 'studentChoices');
				}
				// load AE tab content for the given tool content ID
				aePane.show().load(url);
					
				aePane.addClass('active');
				nav.addClass('active');
			} else {
				aePane.empty().removeClass('active');
				nav.removeClass('active');
			}
		});

	}
	
</script>
<div id="aes-tab" class="container-fluid">
	<div class="row">
		<div class="col-10 offset-1 text-center">
			<h3>
				<fmt:message key="label.aes.questions.marks"/>
			</h3>
		</div>
	</div>
	
	<ul class="nav nav-tabs">
		<c:forEach var="aeToolContentId" items="${aeToolContentIds}" varStatus="status">
			<li class="nav-item text-center">
				<a data-toggle="tab" href="#" 
				   data-tool-content-id="${aeToolContentId}" class="nav-link">
					<c:out value="${aeActivityTitles[status.index]}" escapeXml="false"/>
				</a>
			</li>
		</c:forEach>
	</ul>
			
	<div>
		<c:forEach var="aeToolContentId" items="${aeToolContentIds}" varStatus="status">
			<div id="assessment-pane-${aeToolContentId}"
				 class="tab-pane assessment-questions-pane pt-4"
				 data-tool-type="${aeToolTypes[status.index]}"
				 data-tool-content-id="${aeToolContentId}">
			</div>
		</c:forEach>
	</div>
</div>