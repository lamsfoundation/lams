<%@ include file="/common/taglibs.jsp"%>

<div id="answerArea">
	<input type="hidden" name="optionCount" id="optionCount" value="${fn:length(optionList)}">
	
	<table class="table table-condensed table-striped">
		<c:forEach var="answer" items="${optionList}" varStatus="status">
			<tr>
				<td class="text-center" style="width:5%">
					<input type="hidden" name="optionDisplayOrder${status.index}" id="optionDisplayOrder${status.index}" value="${answer.displayOrder}">
					<input type="hidden" name="optionUid${status.index}" id="optionUid${status.index}" value="${answer.uid}">
					<span class="field-name">&#${status.index + 65};)</span>
				</td>
				
				<td style="background:none;">	
					<lams:CKEditor id="optionName${status.index}" value="${answer.name}" contentFolderID="${contentFolderID}" />					
				</td>
				
				<td class="text-right" style="width:15%">
					<input type="radio" alt="${status.index}" name="optionCorrect" value="${answer.displayOrder}" <c:if test="${answer.correct == true}">checked="checked"</c:if> >
					<span class="field-name"><fmt:message key="label.authoring.basic.item.correct" /></span>
				</td>
				
				<c:if test="${!isAuthoringRestricted}">
					<td class="arrows" style="width:5%">
						<c:if test="${not status.first}">
							<lams:Arrow state="up" titleKey="label.up" onclick="upAnswer(${status.index})"/>
						</c:if>
						<c:if test="${not status.last}">
							<lams:Arrow state="down" titleKey="label.down" onclick="downAnswer(${status.index})"/>
						</c:if>
					</td>
		                
					<td  align="center" style="width:5%">
						<i class="fa fa-times"	title="<fmt:message key='label.authoring.online.delete' />"
							onclick="removeAnswer(${status.index})"></i>
					</td>
				</c:if>
			</tr>
		</c:forEach>
	</table>
</div>