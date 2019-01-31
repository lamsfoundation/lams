<%@ include file="/common/taglibs.jsp"%>
<script>
$(document).ready(function(){
	//update slider's label with the initial value
	$('.slider').trigger('slide');
})
</script>

<div id="optionArea">
	<input type="hidden" name="optionCount" id="optionCount" value="${fn:length(optionList)}">
	<input type="hidden" name="questionType" id="questionType" value="${questionType}" />
	
	<div id="option-table" class="hover-active">
		<c:forEach var="option" items="${optionList}" varStatus="status">
			<table id="option-table-${status.index}" data-id="${status.index}">
				<tr>
				
					<td>
						<span>${status.index+1}</span>
					</td>
					
					<td>
						<c:choose>
							<c:when test="${questionType == 1}">
								<%@ include file="option.jsp"%>
							</c:when>
							<c:when test="${questionType == 2}">
								<%@ include file="matchingpairoption.jsp"%>
							</c:when>
							<c:when test="${questionType == 3}">
								<%@ include file="shortansweroption.jsp"%>
							</c:when>					
							<c:when test="${questionType == 4}">
								<%@ include file="numericaloption.jsp"%>
							</c:when>
							<c:when test="${questionType == 7}">
								<%@ include file="orderingoption.jsp"%>
							</c:when>
							<c:when test="${questionType == 8}">
								<%@ include file="optionhedgingmark.jsp"%>
							</c:when>
						</c:choose>	
					</td>
	
					<c:if test="${!isAuthoringRestricted}">
						<td width="30px">
							<i class="fa fa-trash delete-button" title="<fmt:message key="label.authoring.basic.delete" />"
								onclick="javascript:removeOption(${status.index})"></i>
						</td>
					</c:if>
					
				</tr>
			</table>
		</c:forEach>
	</div>

</div>