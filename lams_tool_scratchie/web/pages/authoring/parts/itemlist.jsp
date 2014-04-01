<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />


	<input type="hidden" name="itemCount" id="itemCount" value="${fn:length(sessionMap.itemList)}">
	
	<table class="alternative-color" cellspacing="0">
		<c:forEach var="item" items="${sessionMap.itemList}" varStatus="status">
			<tr>
				<td style="padding-top:15px; padding-bottom:15px;">
					<c:out value="${item.title}" escapeXml="true"/>			
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
				
				<td width="20px" style="vertical-align:middle;">
					<c:set var="editItemUrl" >
						<c:url value='/authoring/editItem.do'/>?sessionMapID=${sessionMapID}&itemIndex=${status.index}&KeepThis=true&TB_iframe=true&height=540&width=850&modal=true
					</c:set>		
					<a href="${editItemUrl}" class="thickbox" style="margin-left: 20px;"> 
						<img src="<html:rewrite page='/includes/images/edit.gif'/>" 
							title="<fmt:message key="label.edit" />" style="border-style: none;"/>
					</a>				
                </td>
	                
				<td width="20px" style="padding-left:10px; vertical-align:middle; text-align: center;">
					<img src="<html:rewrite page='/includes/images/cross.gif'/>"
						title="<fmt:message key="label.authoring.online.delete" />"
						onclick="removeItem(${status.index})" />
				</td>
			</tr>
		</c:forEach>
	</table>
