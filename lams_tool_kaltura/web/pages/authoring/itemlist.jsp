<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<c:set var="PARTNER_ID"><%=Configuration.get(ConfigurationKeys.KALTURA_PARTNER_ID)%></c:set>
<c:set var="SUB_PARTNER_ID"><%=Configuration.get(ConfigurationKeys.KALTURA_SUB_PARTNER_ID)%></c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}"	scope="request" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="availableQuestions" value="${sessionMap.availableQuestions}" />

<div>
	<div class="field-name">
		<fmt:message key="label.video.list" />
		<img src="${ctxPath}/includes/images/indicator.gif"	style="display:none" id="referencesArea_Busy" />
	</div>

	<table class="alternative-color" id="videoTable" cellspacing="0">
		<c:forEach var="item" items="${sessionMap.itemList}" varStatus="status">
			<tr>
				<td width="100px">
					<c:set var="previewUrl" >
						<c:url value='/authoring.do'/>?dispatch=preview&sessionMapID=${sessionMapID}&itemIndex=${status.index}&KeepThis=true&TB_iframe=true&height=340&width=650
					</c:set>
				
					<a href="${previewUrl}" class="thickbox" title="<fmt:message key="label.item.preview" />">
						<img src="http://cdnbakmi.kaltura.com/p/${PARTNER_ID}/sp/${SUB_PARTNER_ID}/thumbnail/entry_id/${item.entryId}/version/100001/width/96!/height/54" height="54" width="96" alt="Video">
					</a>
				</td>
				
				<td style="vertical-align:middle;">
					<a href="${previewUrl}" class="thickbox" title="<fmt:message key="label.item.preview" />">
						<c:out value="${item.title}" escapeXml="true"/>
					</a>
				</td>
				
				<td  width="60px" style="vertical-align:middle;">
					<a href="${previewUrl}" class="thickbox" title="<fmt:message key="label.item.preview" />">
						<fmt:message key="label.item.preview" />
					</a>
				</td>
				
				<td width="40px" style="vertical-align:middle;">
					<c:if test="${not status.first}">
						<img src="<html:rewrite page='/images/up.gif'/>"
							border="0" title="<fmt:message key="label.authoring.basic.up"/>"
							onclick="upItem(${status.index})">
						<c:if test="${status.last}">
							<img src="<html:rewrite page='/images/down_disabled.gif'/>"
								border="0" title="<fmt:message key="label.authoring.basic.down"/>">
						</c:if>
					</c:if>

					<c:if test="${not status.last}">
						<c:if test="${status.first}">
							<img src="<html:rewrite page='/images/up_disabled.gif'/>"
								border="0" title="<fmt:message key="label.authoring.basic.up"/>">
						</c:if>

						<img src="<html:rewrite page='/images/down.gif'/>"
							border="0" title="<fmt:message key="label.authoring.basic.down"/>"
							onclick="downItem(${status.index})">
					</c:if>
				</td>
                
				<td width="20px" style="vertical-align:middle;">
					<img src="<html:rewrite page='/images/cross.gif'/>"
						title="<fmt:message key="label.authoring.basic.delete" />"	
						onclick="deleteItem(${status.index})" />
				</td>
			</tr>
		</c:forEach>
	</table>
</div>


