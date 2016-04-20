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
				}
			});
		});
	});		
</script>
		
<h2>
	<fmt:message key="label.number.learners.per.session" />
</h2>

<table cellspacing="3" style="width: 400px; padding-left: 30px;">

	<c:choose>
		<c:when test="${empty sessionDtos}">
			<div align="center">
				<b> <fmt:message key="message.monitoring.summary.no.session" /> </b>
			</div>
		</c:when>
		
		<c:otherwise>
			<tr>
				<th width="150px;" style="padding-left: 0px;">
					<fmt:message key="label.monitoring.summary.user.name" />
				</th>
				<th width="80px;" style="padding-left: 0px;">
					<fmt:message key="label.monitoring.summary.total" />
				</th>					
			</tr>	
		</c:otherwise>
	
	</c:choose>
	
	<c:forEach var="sessionDto" items="${sessionDtos}" varStatus="firstGroup">
		<tr>				
			<td>
				<fmt:message key="monitoring.label.group" /> ${sessionDto.sessionName}
			</td>
			<td>
				${sessionDto.numberLearners}
			</td>
		</tr>
	</c:forEach>
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
		
		<c:forEach var="toolOutputDefinition" items="${sessionMap.toolOutputDefinitions}" varStatus="firstGroup">
		
			<option value="${toolOutputDefinition}"
				<c:if test="${toolOutputDefinition == sessionMap.activityEvaluation}">selected="selected"</c:if>
			>
				<fmt:message key="output.desc.${toolOutputDefinition}" />
			</option>
			
		</c:forEach>
	</select>
</div>

