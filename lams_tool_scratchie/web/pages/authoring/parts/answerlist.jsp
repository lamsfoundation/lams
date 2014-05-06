<%@ include file="/common/taglibs.jsp"%>

<div id="answerArea">
	<input type="hidden" name="answerCount" id="answerCount" value="${fn:length(answerList)}">
	
	<table class="alternative-color" cellspacing="0">
		<c:forEach var="answer" items="${answerList}" varStatus="status">
			<tr>
				<td style="padding-top:15px; padding-bottom:15px;">
					<table>
						<tr>
							<td style="padding-left:10px; border-bottom:0px; vertical-align:middle; background:none;">
								<input type="hidden" name="answerOrderId${status.index}" id="answerOrderId${status.index}" value="${answer.orderId}">
								<input type="hidden" name="answerUid${status.index}" id="answerUid${status.index}" value="${answer.uid}">
								<span class="field-name">
									&#${status.index + 65};)
								</span>
							</td>
						</tr>
						<tr>		
							<td style="border-bottom:0px; background:none;">	
								<lams:CKEditor id="answerDescription${status.index}" value="${answer.description}" contentFolderID="${contentFolderID}" />					
							</td>									
						</tr>
						<tr>
							<td style="border-bottom:0px; vertical-align:top; background:none;" class="right-buttons">
								<input type="radio" alt="${status.index}" name="answerCorrect" value="${answer.orderId}" <c:if test="${answer.correct == true}">checked="checked"</c:if> >
								
								<span class="field-name">
									<fmt:message key="label.authoring.basic.item.correct" />
								</span>
							</td>
						</tr>	
					</table>

				</td>
					
				<td width="20px" style="padding-left:10px; vertical-align:middle; text-align: center;">
					<c:if test="${not status.first}">
						<img src="<html:rewrite page='/includes/images/uparrow.gif'/>"
							border="0" title="<fmt:message key="label.up"/>"
							onclick="upAnswer(${status.index})">
						<c:if test="${status.last}">
							<img
								src="<html:rewrite page='/includes/images/downarrow_disabled.gif'/>"
								border="0" title="<fmt:message key="label.down"/>">
						</c:if>
					</c:if>
	
					<c:if test="${not status.last}">
						<c:if test="${status.first}">
							<img
								src="<html:rewrite page='/includes/images/uparrow_disabled.gif'/>"
								border="0" title="<fmt:message key="label.up"/>">
						</c:if>
	
						<img src="<html:rewrite page='/includes/images/downarrow.gif'/>"
							border="0" title="<fmt:message key="label.down"/>"
							onclick="downAnswer(${status.index})">
					</c:if>
				</td>
	                
				<td width="20px" style="padding-left:10px; vertical-align:middle; text-align: center;">
					<img src="<html:rewrite page='/includes/images/cross.gif'/>"
						title="<fmt:message key="label.authoring.online.delete" />"
						onclick="removeAnswer(${status.index})" />
				</td>
			</tr>
		</c:forEach>
	</table>
</div>