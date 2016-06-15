<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="sessionDtos" value="${sessionMap.sessionDtos}"/>

<script type="text/javascript">
	$(document).ready(function(){
		$('#activity-evaluation').on('change', function() {

			if (this.value == "dummy") {
				return;
			}

			$.ajax({
				url: '<c:url value="/monitoring/setActivityEvaluation.do"/>',
				data: {
					toolContentID: "${sessionMap.toolContentID}",
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

<c:choose>
	<c:when test="${empty sessionDtos}">
		<lams:Alert type="warn" id="no-edit" close="false">
			<fmt:message key="message.monitoring.summary.no.session" />
		</lams:Alert>
	</c:when>
		
	<c:otherwise>
	
		<h5>
			<fmt:message key="label.number.learners.per.session" />
		</h5>
	
		<table class="table table-condensed table-striped">
			<tr>
				<th width="150px;">
					<fmt:message key="label.monitoring.summary.user.name" />
				</th>
				<th width="80px;">
					<fmt:message key="label.monitoring.summary.total" />
				</th>		
			</tr>
		
			<c:forEach var="sessionDto" items="${sessionDtos}" varStatus="firstGroup">
				<tr>				
					<td>
						<fmt:message key="monitoring.label.group" />&nbsp;${sessionDto.sessionName}
					</td>
					<td>
						${sessionDto.numberLearners}
					</td>
				</tr>
			</c:forEach>
		</table>
	
	</c:otherwise>
</c:choose>

<%@ include file="parts/toolOutput.jsp"%>
