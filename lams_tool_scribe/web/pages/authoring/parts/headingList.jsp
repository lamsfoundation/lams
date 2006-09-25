<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<table>
	<c:if test="${empty sessionMap.headings}">
		<tr>
			<td>
				<fmt:message key="message.noHeadings" />
				<%--	TODO should we ever reach this state ??--%>
			<td>
		</tr>
	</c:if>

	<c:set var="index" value="0" />
	<c:forEach var="heading" items="${sessionMap.headings}">
		<tr>
			<td style="width: 70%">
				<div style="overflow: auto;">
					${heading.headingText}
				</div>
			</td>

			<td style="width: 10%">

				<c:if test="${index ne 0}">
					<c:set var="moveUpURL">
						<html:rewrite
							page="/authoring.do?dispatch=moveHeading&amp;sessionMapID=${sessionMapID}&amp;headingIndex=${index}&amp;direction=up" />
					</c:set>
					<img src="${tool}images/up.gif"
						title="<fmt:message key="link.moveUp"/>"
						onclick="showMessage('${moveUpURL}')"></img>
				</c:if>
				
				<c:if test="${index ne fn:length(sessionMap.headings)-1}">
					<c:set var="moveDownURL">
						<html:rewrite
							page="/authoring.do?dispatch=moveHeading&amp;sessionMapID=${sessionMapID}&amp;headingIndex=${index}&amp;direction=down" />
					</c:set>
					<img src="${tool}images/down.gif"
						title="<fmt:message key="link.moveDown"/>"
						onclick="showMessage('${moveDownURL}')"></img>
				</c:if>
			</td>

			<td style="width: 10%">
				<c:set var="editURL">
					<html:rewrite
						page="/authoring.do?dispatch=loadHeadingForm&amp;sessionMapID=${sessionMapID}&amp;headingIndex=${index}" />
				</c:set>
				<img src="${tool}images/edit.gif"
					title="<fmt:message key="link.edit"/>"
					onclick="showMessage('${editURL}')"></img>
			</td>

			<td style="width: 10%">
				<c:set var="deleteURL">
					<html:rewrite
						page="/authoring.do?dispatch=deleteHeading&amp;sessionMapID=${sessionMapID}&amp;headingIndex=${index}" />
				</c:set>
				<img src="${tool}images/delete.gif"
					title="<fmt:message key="link.delete"/>"
					onclick="showMessage('${deleteURL}')"></img>

				<c:set var="index" value="${index + 1}" />
			</td>
		</tr>
	</c:forEach>

</table>
