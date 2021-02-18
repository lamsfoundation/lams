<%@ include file="/common/taglibs.jsp"%>
<c:set var="listSize" value="${fn:length(instructionList)}" />

<div id="instructionArea">
	<script></script> <!--  Do not remove empty scripts. They are stopping the form tags from being stripped by the AddInstruction call. http://forum.jquery.com/topic/is-jquery-stripping-form-tags -->
	<form></form>
	<form id="instructionForm">
		<input type="hidden" name="instructionCount" id="instructionCount" value="${listSize}" class="form-control"/>

		<label><fmt:message key="label.authoring.basic.resource.instructions" /></label>

		<table class="table table-condensed table-no-border">
			<c:forEach var="item" items="${instructionList}" varStatus="status">
				<tr id="instructionItem${status.index}">
					<td width="10px">
						${status.index+1}
					</td>
					<td>
						<lams:textarea name="instructionItemDesc${status.index}" class="form-control"
									   id="instructionItemDesc${status.index}" rows="3" cols="82">${item}</lams:textarea>
					</td>

					<td class="arrows" style="width:5%">
						<!-- Don't display up icon if first line -->
						<c:if test="${not status.first}">
		 					<lams:Arrow state="up" titleKey="label.up" onclick="upItem('${status.index}')"/>
		 				</c:if>
						<!-- Don't display down icon if last line -->
						<c:if test="${not status.last}">
							<lams:Arrow state="down" titleKey="label.down" onclick="downItem('${status.index}','${listSize}')"/>
		 				</c:if>
					</td>
					<td  align="center" style="width:5%"><i class="fa fa-times"	title="<fmt:message key="label.delete" />"  
						onclick="removeInstruction('${status.index}')"></i>
					</td>
				</tr>
			</c:forEach>
		</table>
		
		<lams:WaitingSpinner id="instructionArea_Busy"/>
		
		<button onclick="addInstruction(); return false;" class="btn btn-default btn-sm btn-disable-on-submit pull-right">
			<i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.basic.resource.add.instruction" /> 
		</button>

	</form>
</div>
