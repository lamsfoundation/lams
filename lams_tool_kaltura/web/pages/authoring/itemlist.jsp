<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<c:set var="PARTNER_ID"><%=Configuration.get(ConfigurationKeys.KALTURA_PARTNER_ID)%></c:set>
<c:set var="SUB_PARTNER_ID"><%=Configuration.get(ConfigurationKeys.KALTURA_SUB_PARTNER_ID)%></c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}"	scope="request" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="availableQuestions" value="${sessionMap.availableQuestions}" />

<div id="itemList" >

		<c:if test="${not empty sessionMap.itemList}">
	<div class="panel panel-default voffset5">
			<div class="panel-heading panel-title">
				<fmt:message key="label.video.list" />
				<i class="fa fa-refresh fa-spin fa-fw" style="display:none" id="referencesArea_Busy"></i>
			</div>

		<table class="table table-condensed" id="videoTable">
			<c:forEach var="item" items="${sessionMap.itemList}" varStatus="status">
				<tr>
				
					<td width="100px">
						<c:set var="previewUrl" >
							<c:url value='/authoring/preview.do'/>?sessionMapID=${sessionMapID}&itemIndex=${status.index}&KeepThis=true&TB_iframe=true
						</c:set>
					
						<a href="${previewUrl}" class="thickbox" title="<fmt:message key="label.item.preview" />">
							<img src="http://cdnbakmi.kaltura.com/p/${PARTNER_ID}/sp/${SUB_PARTNER_ID}/thumbnail/entry_id/${item.entryId}/width/96!/height/54" height="54" width="96" alt="Video">
						</a>
					</td>
					
					<td style="vertical-align:middle;">
						<a href="${previewUrl}" class="thickbox" title="<fmt:message key="label.item.preview" />">
							<c:out value="${item.title}" escapeXml="true"/>
						</a>
					</td>
					
					<td  width="60px" style="vertical-align:middle;">
						<a href="${previewUrl}" class="thickbox btn btn-default btn-xs" title="<fmt:message key="label.item.preview" />">
							<fmt:message key="label.item.preview" />
						</a>
					</td>
					
					<td class="arrows">
						<!-- Don't display up icon if first line -->
						<c:if test="${not status.first}">
			 				<lams:Arrow state="up" title="<fmt:message key='label.authoring.basic.up'/>" 
			 						onclick="javascript:upItem(${status.index})" />
			 			</c:if>
						<!-- Don't display down icon if last line -->
						<c:if test="${not status.last}">
							<lams:Arrow state="down" title="<fmt:message key='label.authoring.basic.down'/>" 
									onclick="javascript:downItem(${status.index})" />
			 			</c:if>
					</td>			
					
					<td width="30px">
						<i class="fa fa-times" title="<fmt:message key="label.authoring.basic.delete" />"
							onclick="javascript:deleteItem(${status.index})"></i>
					</td>
					
				</tr>
			</c:forEach>
		</table>
	</div>
		</c:if>
</div>

