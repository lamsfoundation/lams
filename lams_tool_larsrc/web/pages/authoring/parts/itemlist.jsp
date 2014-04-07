<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}"
	scope="request" />

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<div id="itemList">
	<h2 class="spacer-left">
		<fmt:message key="label.authoring.basic.resource.list.title" />
		<img src="${ctxPath}/includes/images/indicator.gif"
			style="display:none" id="resourceListArea_Busy" />
	</h2>
	
	<table class="alternative-color" id="itemTable" cellspacing="0">
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
		<c:set var="listSize" value="${fn:length(sessionMap.resourceList)}" />
		<c:forEach var="resource" items="${sessionMap.resourceList}"
			varStatus="status">
			<tr>
				<%-- Resource Type:1=URL,2=File,3=Website,4=Learning Object --%>
				<c:if test="${resource.type == 1}">
					<td>
						<span class="field-name"> <fmt:message
								key="label.authoring.basic.resource.url" /> : </span>
						<c:out value="${resource.title}" escapeXml="true"/>
					</td>
					<td width="10%">
						<a href="#"
							onclick="previewItem(1,${status.index},'${sessionMapID}')"> <fmt:message
								key="label.authoring.basic.resource.verify.url" /> </a>
					</td>
					<td width="10%">
						<%-- Don't display down icon if last line --%>
						<c:choose>
							<c:when test="${0 != status.index}">
								<img src="<html:rewrite page='/includes/images/uparrow.gif'/>"
									border="0" title="<fmt:message key="label.up"/>"
									onclick="switchItem(${status.index}, ${status.index - 1}, '${sessionMapID}')">
							</c:when>
							<c:otherwise>
								<img
									src="<html:rewrite page='/includes/images/uparrow_disabled.gif'/>"
									border="0" title="<fmt:message key="label.up"/>">
							</c:otherwise>
						</c:choose>

						<c:choose>
							<c:when test="${listSize != status.count}">
								<img src="<html:rewrite page='/includes/images/downarrow.gif'/>"
									border="0" title="<fmt:message key="label.down"/>"
									onclick="switchItem(${status.index}, ${status.index + 1}, '${sessionMapID}')">
							</c:when>

							<c:otherwise>
								<img
									src="<html:rewrite page='/includes/images/downarrow_disabled.gif'/>"
									border="0" title="<fmt:message key="label.down"/>">
							</c:otherwise>

						</c:choose>

						<%-- Don't display down icon if last line --%>
					</td>
					<td width="5%">
						<img src="${tool}includes/images/edit.gif"
							title="<fmt:message key="label.authoring.basic.resource.edit" />"
							onclick="editItem(${status.index},'${sessionMapID}')" />

					</td>
					<td width="5%">
						<img src="${tool}includes/images/cross.gif"
							title="<fmt:message key="label.authoring.basic.resource.delete" />"
							onclick="deleteItem(${status.index},'${sessionMapID}')" />
					</td>
				</c:if>

				<c:if test="${resource.type == 2}">
					<td>
						<span class="field-name"> <fmt:message
								key="label.authoring.basic.resource.file" /> : </span>
						<c:out value="${resource.title}" escapeXml="true"/>
					</td>
					<td>
						<a href="#"
							onclick="previewItem(2,${status.index},'${sessionMapID}')"> <fmt:message
								key="label.authoring.basic.resource.preview" /> </a>
					</td>
					<td width="10%">
						<%-- Don't display down icon if last line --%>
						<c:choose>
							<c:when test="${0 != status.index}">
								<img src="<html:rewrite page='/includes/images/uparrow.gif'/>"
									border="0" title="<fmt:message key="label.up"/>"
									onclick="switchItem(${status.index}, ${status.index - 1}, '${sessionMapID}')">
							</c:when>
							<c:otherwise>
								<img
									src="<html:rewrite page='/includes/images/uparrow_disabled.gif'/>"
									border="0" title="<fmt:message key="label.up"/>">
							</c:otherwise>
						</c:choose>

						<c:choose>
							<c:when test="${listSize != status.count}">
								<img src="<html:rewrite page='/includes/images/downarrow.gif'/>"
									border="0" title="<fmt:message key="label.down"/>"
									onclick="switchItem(${status.index}, ${status.index + 1}, '${sessionMapID}')">
							</c:when>

							<c:otherwise>
								<img
									src="<html:rewrite page='/includes/images/downarrow_disabled.gif'/>"
									border="0" title="<fmt:message key="label.down"/>">
							</c:otherwise>

						</c:choose>

						<%-- Don't display down icon if last line --%>
					</td>
					<td>
						<img src="${tool}includes/images/edit.gif"
							title="<fmt:message key="label.authoring.basic.resource.edit" />"
							onclick="editItem(${status.index},'${sessionMapID}')" />

					</td>
					<td>
						<img src="${tool}includes/images/cross.gif"
							title="<fmt:message key="label.authoring.basic.resource.delete" />"
							onclick="deleteItem(${status.index},'${sessionMapID}')" />
					</td>
				</c:if>

				<c:if test="${resource.type == 3}">
					<td>
						<span class="field-name"> <fmt:message
								key="label.authoring.basic.resource.website" /> : </span>
						<c:out value="${resource.title}" escapeXml="true"/>
					</td>
					<td>
						<a href="#"
							onclick="previewItem(3,${status.index},'${sessionMapID}')"> <fmt:message
								key="label.authoring.basic.resource.preview" /> </a>
					</td>
					<td width="10%">
						<%-- Don't display down icon if last line --%>
						<c:choose>
							<c:when test="${0 != status.index}">
								<img src="<html:rewrite page='/includes/images/uparrow.gif'/>"
									border="0" title="<fmt:message key="label.up"/>"
									onclick="switchItem(${status.index}, ${status.index - 1}, '${sessionMapID}')">
							</c:when>
							<c:otherwise>
								<img
									src="<html:rewrite page='/includes/images/uparrow_disabled.gif'/>"
									border="0" title="<fmt:message key="label.up"/>">
							</c:otherwise>
						</c:choose>

						<c:choose>
							<c:when test="${listSize != status.count}">
								<img src="<html:rewrite page='/includes/images/downarrow.gif'/>"
									border="0" title="<fmt:message key="label.down"/>"
									onclick="switchItem(${status.index}, ${status.index + 1}, '${sessionMapID}')">
							</c:when>

							<c:otherwise>
								<img
									src="<html:rewrite page='/includes/images/downarrow_disabled.gif'/>"
									border="0" title="<fmt:message key="label.down"/>">
							</c:otherwise>

						</c:choose>

						<%-- Don't display down icon if last line --%>
					</td>
					<td>
						<img src="${tool}includes/images/edit.gif"
							title="<fmt:message key="label.authoring.basic.resource.edit" />"
							onclick="editItem(${status.index},'${sessionMapID}')" />
					</td>
					<td>
						<img src="${tool}includes/images/cross.gif"
							title="<fmt:message key="label.authoring.basic.resource.delete" />"
							onclick="deleteItem(${status.index},'${sessionMapID}')" />
					</td>
				</c:if>

				<c:if test="${resource.type == 4}">
					<td>
						<span class="field-name"> <fmt:message
								key="label.authoring.basic.resource.learning.object" /> : </span>
						<c:out value="${resource.title}" escapeXml="true"/>
					</td>
					<td>
						<a href="#"
							onclick="previewItem(4,${status.index},'${sessionMapID}')"> <fmt:message
								key="label.authoring.basic.resource.preview" /> </a>
					</td>
					<td width="10%">
						<%-- Don't display down icon if last line --%>
						<c:choose>
							<c:when test="${0 != status.index}">
								<img src="<html:rewrite page='/includes/images/uparrow.gif'/>"
									border="0" title="<fmt:message key="label.up"/>"
									onclick="switchItem(${status.index}, ${status.index - 1}, '${sessionMapID}')">
							</c:when>
							<c:otherwise>
								<img
									src="<html:rewrite page='/includes/images/uparrow_disabled.gif'/>"
									border="0" title="<fmt:message key="label.up"/>">
							</c:otherwise>
						</c:choose>

						<c:choose>
							<c:when test="${listSize != status.count}">
								<img src="<html:rewrite page='/includes/images/downarrow.gif'/>"
									border="0" title="<fmt:message key="label.down"/>"
									onclick="switchItem(${status.index}, ${status.index + 1}, '${sessionMapID}')">
							</c:when>

							<c:otherwise>
								<img
									src="<html:rewrite page='/includes/images/downarrow_disabled.gif'/>"
									border="0" title="<fmt:message key="label.down"/>">
							</c:otherwise>

						</c:choose>

						<%-- Don't display down icon if last line --%>
					</td>
					<td>
						<img src="${tool}includes/images/edit.gif"
							title="<fmt:message key="label.authoring.basic.resource.edit" />"
							onclick="editItem(${status.index},'${sessionMapID}')" />
					</td>
					<td>
						<img src="${tool}includes/images/cross.gif"
							title="<fmt:message key="label.authoring.basic.resource.delete" />"
							onclick="deleteItem(${status.index},'${sessionMapID}')" />
					</td>
				</c:if>
			</tr>
		</c:forEach>
	</table>
</div>

<%-- This script will works when a new resoruce item submit in order to refresh "Resource List" panel. --%>
<script lang="javascript">
	var win = null;
	if (window.parent && window.parent.hideMessage) {
		win = window.parent;
	} else if (window.top && window.top.hideMessage) {
		win = window.top;
	}
	if (win) {
		win.hideMessage();
		var obj = win.document.getElementById('resourceListArea');
		obj.innerHTML= document.getElementById("itemList").innerHTML;
	}
</script>
