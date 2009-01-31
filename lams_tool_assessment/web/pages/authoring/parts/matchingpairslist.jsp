<%@ include file="/common/taglibs.jsp"%>
<div id="optionArea">
	<form id="optionForm" name="optionForm">
		<input type="hidden" name="optionCount" id="optionCount" value="${fn:length(optionList)}">
	
		<div class="field-name space-top">
			<fmt:message key="" />
		</div>

		<table class="alternative-color" cellspacing="0">
			<c:forEach var="option" items="${optionList}" varStatus="status">
				<tr id="optionItem${status.index}">
					<td width="3px" style="vertical-align:middle;">
						${status.index+1}
					</td>
					<td>

					</td>
					
					<td width="40px" style="vertical-align:middle;">
						<c:if test="${not status.first}">
							<img src="<html:rewrite page='/includes/images/uparrow.gif'/>"
								border="0" title="<fmt:message key="label.authoring.basic.up"/>"
								onclick="upOption(${status.index})">
							<c:if test="${status.last}">
								<img
									src="<html:rewrite page='/includes/images/downarrow_disabled.gif'/>"
									border="0" title="<fmt:message key="label.authoring.basic.down"/>">
							</c:if>
						</c:if>
	
						<c:if test="${not status.last}">
							<c:if test="${status.first}">
								<img
									src="<html:rewrite page='/includes/images/uparrow_disabled.gif'/>"
									border="0" title="<fmt:message key="label.authoring.basic.up"/>">
							</c:if>
	
							<img src="<html:rewrite page='/includes/images/downarrow.gif'/>"
								border="0" title="<fmt:message key="label.authoring.basic.down"/>"
								onclick="downOption(${status.index})">
						</c:if>
					</td>
	                
					<td width="20px" style="padding-left:0px; vertical-align:middle;">
						<img src="<html:rewrite page='/includes/images/cross.gif'/>"
							title="<fmt:message key="label.authoring.basic.delete" />"
							onclick="removeOption(${status.index})" />
					</td>
				</tr>
			</c:forEach>
		</table>
		
		<a href="javascript:;" onclick="addOption();" class="button-add-item right-buttons" style="margin-right: 40px; margin-top:0px">
			<fmt:message key="label.authoring.basic.add.option" /> 
		</a>
		
	</form>
</div>