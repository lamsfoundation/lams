<%@ include file="/common/taglibs.jsp"%>

<div id="accordion-tool-output" class="panel-group voffset20" role="tablist" aria-multiselectable="true"> 
    <div class="panel panel-default">
        <div class="panel-heading collapsable-icon-left" id="heading-tool-output">
        	<span class="panel-title">
		    	<a class="collapsed" role="button" data-toggle="collapse" href="#tool-output" aria-expanded="true" aria-controls="tool-output">
	          		<fmt:message key="label.tool.output" />
	          	</a>
      		</span>
        </div>

		<div aria-expanded="false" id="tool-output" class="panel-body panel-collapse collapse" role="tabpanel" aria-labelledby="heading-tool-output">
		
			<select name="activityEvaluation" id="activity-evaluation" autocomplete="off">
				<option value=""><fmt:message key="output.desc.none" /></option>
				
				<c:forEach var="toolOutputDefinition" items="${toolOutputDefinitions}" varStatus="firstGroup">
				
					<option value="${toolOutputDefinition}"
							<c:if test="${toolOutputDefinition == activityEvaluation}">selected="selected"</c:if>>
						<fmt:message key="output.desc.${toolOutputDefinition}" />
					</option>
					
				</c:forEach>
			</select>

		</div>
	</div>
</div>
