<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />


	<input type="hidden" name="itemCount" id="itemCount" value="${fn:length(itemList)}">
	
	<table class="alternative-color" cellspacing="0">
		<c:forEach var="item" items="${itemList}" varStatus="status">
			<tr>
				<td style="padding-top:15px; padding-bottom:15px;">
					<table>
						<tr>
							<td style="padding-left:10px; border-bottom:0px; vertical-align:middle; background:none;">
								<input type="hidden" name="itemOrderId${status.index}" id="itemOrderId${status.index}" value="${item.orderId}">
								<span class="field-name">
									&#${status.index + 65};)
								</span>
							</td>
						</tr>
						<tr>		
							<td style="border-bottom:0px; background:none;">	
								<lams:CKEditor id="itemDescription${status.index}" value="${item.description}" contentFolderID="${sessionMap.contentFolderID}" />					
							</td>									
						</tr>
						<tr>
							<td style="border-bottom:0px; vertical-align:top; background:none;" class="right-buttons">
								<input type="radio" name="itemCorrect" value="${item.orderId}" <c:if test="${item.correct == true}">checked="checked"</c:if> >
								
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
							border="0" title="<fmt:message key="label.authoring.basic.up"/>"
							onclick="upItem(${status.index})">
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
							onclick="downItem(${status.index})">
					</c:if>
				</td>
	                
				<td width="20px" style="padding-left:10px; vertical-align:middle; text-align: center;">
					<img src="<html:rewrite page='/includes/images/cross.gif'/>"
						title="<fmt:message key="label.authoring.basic.delete" />"
						onclick="removeItem(${status.index})" />
				</td>
			</tr>
		</c:forEach>
	</table>
