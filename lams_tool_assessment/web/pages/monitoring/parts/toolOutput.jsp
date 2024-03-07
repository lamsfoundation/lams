<%@ include file="/common/taglibs.jsp"%>

<div class="card" id="heading-tool-output">
    <div class="card-header text-bg-light">
      	<span class="card-title collapsable-icon-left">
	    	<button type="button" class="btn btn-light no-shadow collapsed" data-bs-toggle="collapse" data-bs-target="#tool-output" aria-expanded="false" aria-controls="tool-output">
          		<fmt:message key="label.tool.output" />
          	</button>
   		</span>
    </div>

	<div aria-expanded="false" id="tool-output" class="card-body collapse p-2">
		<select name="activityEvaluation" id="activity-evaluation" autocomplete="off">
			<option value=""><fmt:message key="output.desc.none" /></option>
				
			<c:forEach var="toolOutputDefinition" items="${sessionMap.toolOutputDefinitions}" varStatus="firstGroup">
				<option value="${toolOutputDefinition}"
						<c:if test="${toolOutputDefinition == sessionMap.activityEvaluation}">selected="selected"</c:if>>
					<fmt:message key="output.desc.${toolOutputDefinition}" />
				</option>
			</c:forEach>
		</select>
	</div>
</div>
