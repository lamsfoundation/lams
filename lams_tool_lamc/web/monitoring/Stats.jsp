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

<table class="alternative-color">
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

<h1>
	<img src="<lams:LAMSURL/>/images/tree_closed.gif" id="tree-icon" onclick="javascript:toggleAdvancedOptionsVisibility(document.getElementById('tool-output'), document.getElementById('tree-icon'), '<lams:LAMSURL/>');" />

	<a href="javascript:toggleAdvancedOptionsVisibility(document.getElementById('tool-output'), document.getElementById('tree-icon'),'<lams:LAMSURL/>');" >
		<fmt:message key="label.tool.output" />
	</a>
</h1>
<br />

<div class="monitoring-advanced" id="tool-output" style="display:none">
	<select name="activityEvaluation" id="activity-evaluation" autocomplete="off">
		<option value="dummy"></option>
		
		<c:forEach var="toolOutputDefinition" items="${toolOutputDefinitions}" varStatus="firstGroup">
		
			<option value="${toolOutputDefinition}"
				<c:if test="${toolOutputDefinition == activityEvaluation}">selected="selected"</c:if>
			>
				<fmt:message key="output.desc.${toolOutputDefinition}" />
			</option>
			
		</c:forEach>
	</select>
</div>
