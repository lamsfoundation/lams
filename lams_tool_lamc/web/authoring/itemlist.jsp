<%@ include file="/common/taglibs.jsp"%>

<div id="itemList">

	<div class="panel panel-default add-file">
	
	<div class="panel-heading panel-title">
		<fmt:message key="label.questions" />
		<i class="fa fa-spinner" style="display: none" id="resourceListArea_Busy"></i>
		<div class="btn-group btn-group-xs pull-right">
			<a href="#nogo" onClick="javascript:importQTI()" class="btn btn-default">
				<fmt:message key="label.authoring.import.qti" />
			</a>
			<a href="#nogo" onClick="javascript:exportQTI()" class="btn btn-default">
				<fmt:message key="label.authoring.export.qti" />
			</a>
		</div>
	</div>

	<table id="itemTable" class="table table-striped table-condensed">
		<c:set var="queIndex" scope="request" value="0" />

		<tr>
			<c:if test="${totalQuestionCount > 0}">
			<th>
				<fmt:message key="label.questions" />
			</th>

			<th class="text-center">
				<fmt:message key="label.question.marks" />
			</th>

			<th colspan="3">
				&nbsp;
			</th>
			</c:if>
		</tr>

		<c:forEach items="${listQuestionContentDTO}" var="currentDTO" varStatus="status">
			<c:set var="queIndex" scope="request" value="${queIndex +1}" />
			<c:set var="question" scope="request" value="${currentDTO.question}" />
			<c:set var="feedback" scope="request" value="${currentDTO.feedback}" />
			<c:set var="mark" scope="request" value="${currentDTO.mark}" />
			<c:set var="displayOrder" scope="request" value="${currentDTO.displayOrder}" />

			<tr>
				<td>
					<div style="overflow: auto;">
						<c:out value="${question}" escapeXml="false" />
					</div>					
				</td>

				<td width="70px" class="text-center">
					<c:out value="${mark}" />
				</td>

				<td class="arrows" style="width:5%">
					<!-- Don't display up icon if first line -->
					<c:if test="${totalQuestionCount != 1}">
						<!-- Don't display up icon if first line -->
						<c:if test="${queIndex != 1}">
							<c:set var="tip"><fmt:message key='label.tip.moveQuestionUp'/></c:set>
 		 					<lams:Arrow state="up" title="${tip} ${queIndex}" 
		 						onclick="javascript:submitModifyAuthoringQuestion(${queIndex},'moveQuestionUp');"/>
 		 				</c:if>
						<!-- Don't display down icon if last line -->
						<c:if test="${queIndex != totalQuestionCount}">
							<c:set var="tip"><fmt:message key='label.tip.moveQuestionDown'/></c:set>
	 						<lams:Arrow state="down" title="${tip} ${queIndex}"  
								onclick="javascript:submitModifyAuthoringQuestion(${queIndex},'moveQuestionDown');"/>
	 	 				</c:if>
					</c:if>
				</td>

				<td align="center" style="width:5%">
					<c:set var="editItemUrl" >
						<html:rewrite page="/authoring.do"/>?dispatch=newEditableQuestionBox&questionIndex=${queIndex}&sessionMapId=${sessionMapId}&sln=${mcGeneralAuthoringDTO.sln}&showMarks=${mcGeneralAuthoringDTO.showMarks}&randomize=${mcGeneralAuthoringDTO.randomize}&questionsSequenced=${mcGeneralAuthoringDTO.questionsSequenced}&retries=${mcGeneralAuthoringDTO.retries}&reflect=${mcGeneralAuthoringDTO.reflect}&KeepThis=true&TB_iframe=true&modal=true
					</c:set>
					<a href="${editItemUrl}" class="thickbox"> 
						<i class="fa fa-pencil" title="<fmt:message key='label.tip.editQuestion'/>"></i>
					</a>
				</td>

				<td  align="center" style="width:5%">
					<i class="fa fa-times"	title="<fmt:message key="label.tip.deleteQuestion" />" 	onclick="removeQuestion(${queIndex});"></i>
				</td>

			</tr>
		</c:forEach>

	</table>
	</div>
</div>
