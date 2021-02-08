<%@ include file="/taglibs.jsp"%>
<% pageContext.setAttribute("newLineChar", "\r\n"); %>

<style>
	.tab-pane {
		padding-top: 10px;
	}
</style>
<script>
	var tblSelectedAeToolContentId = tblSelectedAeToolContentId || '${empty param.toolContentID ? aeToolContentIds[0] : param.toolContentID}';

	$(document).ready(function(){
		$('#aes-tab .tab-pane').each(function(){
			var aePane = $(this),
				toolContentId = aePane.data('toolContentId'),
				nav = $('#aes-tab .nav-tabs a[data-tool-content-id="' + toolContentId + '"]').closest('li');

			// load AE tab content for the given tool content ID
			aePane.load("<lams:LAMSURL/>tool/laasse10/tblmonitoring/assessment.do?toolContentID="  + toolContentId);

			// re-select the previous tab or stick to whatever is in the parameter
			if (toolContentId == tblSelectedAeToolContentId) {
				aePane.addClass('active');
				nav.addClass('active');
			} else {
				aePane.removeClass('active');
				nav.removeClass('active');
			}
		});

		// change attempted and all learners numbers
		$('#aes-tab .nav-tabs').bind('click', function (event) {
			var link = $(event.target);
			// store which tab was cliked last
			tblSelectedAeToolContentId = link.data("tool-content-id");
		});
	});
</script>
<div id="aes-tab">
	<!-- Header -->
	<div class="row">
		<div class="col-xs-12">
			<h3>
				<fmt:message key="label.aes.questions.marks"/>
			</h3>
		</div>
	</div>
	<!-- End header -->    
	
	<ul class="nav nav-tabs">
		<c:forEach var="aeToolContentId" items="${aeToolContentIds}" varStatus="status">
			<li>
				<a data-toggle="tab" href="#assessment-pane-${aeToolContentId}" 
				   data-tool-content-id="${aeToolContentId}">
					<c:out value="${aeActivityTitles[status.index]}" escapeXml="false"/>
				</a>
			</li>
		</c:forEach>
	</ul>
			
	<div class="tab-content">
		<c:forEach var="aeToolContentId" items="${aeToolContentIds}" varStatus="status">
			<div id="assessment-pane-${aeToolContentId}"
				 class="tab-pane"
				 data-tool-content-id="${aeToolContentId}">
			</div>
		</c:forEach>
	</div>
</div>