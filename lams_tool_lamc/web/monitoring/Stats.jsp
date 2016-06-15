<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

<script type="text/javascript">
	$(document).ready(function(){
		$('#activity-evaluation').on('change', function() {

			if (this.value == "dummy") {
				return;
			}

			$.ajax({
				url: '<c:url value="/monitoring.do"/>',
				data: {
					dispatch: "setActivityEvaluation",
					toolContentID: "${toolContentID}",
					activityEvaluation: this.value
				},
				dataType: 'json',
				success: function (json) {
					if (json.success == "true") {
						alert("<fmt:message key='label.tool.output.has.been.changed'/>");
					}
				},
				error: function (xhr, ajaxOptions, thrownError) {
					alert("<fmt:message key='label.operation.failed'/>");
				}
			});
		});
	});		
</script>

<table class="table table-condensed">
	<tr> 
		<th>
			<b> <fmt:message key="count.total.user" /> </b>
		</th>
		<td align="right">
			<c:out value="${mcGeneralMonitoringDTO.countAllUsers}"/>
		</td> 
	</tr>
						
	<tr> 
		<th>
			<b>  <fmt:message key="count.finished.session" /> </b>
		</th>
		<td align="right">
			<c:out value="${mcGeneralMonitoringDTO.countSessionComplete}"/>
		</td> 
	</tr>
</table>

<%@ include file="parts/toolOutput.jsp"%>
