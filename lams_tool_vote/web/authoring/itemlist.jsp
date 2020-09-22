<%@ include file="/common/taglibs.jsp"%>

<div id="itemList">

<div class="panel panel-default voffset5">
	<div class="panel-heading panel-title">
		<fmt:message key="label.vote.nominations" />
		<i class="fa fa-spinner" style="display: none" id="resourceListArea_Busy"></i>
	</div>

	<table class="table table-striped table-condensed" id="itemTable">
		<c:set var="queIndex" scope="request" value="0" />

		<c:forEach items="${listQuestionDTO}" var="currentDTO" varStatus="status">
			<c:set var="queIndex" scope="request" value="${queIndex +1}" />
			<c:set var="question" scope="request" value="${currentDTO.question}" />
			<c:set var="displayOrder" scope="request" value="${currentDTO.displayOrder}" />
			<c:if test="${status.first}">
				<input type="hidden" id="firstNomination" />
			</c:if>
			
			<tr>
				<td>
					<c:out value="${question}" escapeXml="false" />
				</td>

				<td class="arrows" style="width:5%">
					<c:if test="${fn:length(listQuestionDTO) != 1}">
						<c:if test="${queIndex != 1}">
							<lams:Arrow state="up" titleKey="label.tip.moveNominationUp" 
								onclick="javascript:submitModifyAuthoringNomination('${queIndex}','moveNominationUp');"/>
						</c:if>

						<c:if test="${queIndex != fn:length(listQuestionDTO)}">
							<lams:Arrow state="down" titleKey="label.tip.moveNominationDown" 
								onclick="javascript:submitModifyAuthoringNomination('${queIndex}','moveNominationDown');"/>						
						</c:if>							
					</c:if>
				</td>

				<td align="center" style="width:5%"><i class="fa fa-pencil"	title="<fmt:message key="label.tip.editNomination" />"
						onclick="javascript:showMessage('<lams:WebAppURL />authoring/newEditableNominationBox.do?questionIndex=${queIndex}&contentFolderID=${voteGeneralAuthoringDTO.contentFolderID}&httpSessionID=${voteGeneralAuthoringDTO.httpSessionID}&toolContentID=${voteGeneralAuthoringDTO.toolContentID}&lockOnFinish=${voteGeneralAuthoringDTO.lockOnFinish}&allowText=${voteGeneralAuthoringDTO.allowText}&maxNominationCount=${voteGeneralAuthoringDTO.maxNominationCount}&minNominationCount=${voteGeneralAuthoringDTO.minNominationCount}&reflect=${voteGeneralAuthoringDTO.reflect}')">
						</i>
				</td>

				<td  align="center" style="width:5%"><i class="fa fa-times"	title="<fmt:message key="label.tip.deleteNomination" />"
						onclick="removeNomination(${queIndex});"></i>
				</td>

			</tr>
		</c:forEach>

	</table>
</div>

</div> 

<%-- This script will works when a new resource item submit in order to refresh "Resource List" panel. --%>
<script lang="javascript">
	hideMessage();
	var obj = document.getElementById('resourceListArea');
	obj.innerHTML= document.getElementById("itemList").innerHTML;
   	changeMinMaxVotes(-1, -1);
</script>