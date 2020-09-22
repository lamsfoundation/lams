<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<table class="table table-condensed table-striped" >
	<c:if test="${empty sessionMap.headings}">
		<tr>
			<td>
				<fmt:message key="message.noHeadings" />
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

 			<td class="arrows" style="width:5%">
				<c:if test="${index ne 0}">
					<c:set var="moveUpURL">
						<c:url
							value="authoring/moveHeading.do?sessionMapID=${sessionMapID}&amp;headingIndex=${index}&amp;direction=up" />
					</c:set>
					<lams:Arrow state="up" titleKey="link.moveUp" 
	 						onclick="runUrl('${moveUpURL}')"/>
				</c:if>

				<c:if test="${index ne fn:length(sessionMap.headings)-1}">
					<c:set var="moveDownURL">
						<c:url
							value="authoring/moveHeading.do?sessionMapID=${sessionMapID}&amp;headingIndex=${index}&amp;direction=down" />
					</c:set>
					<lams:Arrow state="down" titleKey="link.moveDown"  
								onclick="runUrl('${moveDownURL}')"/>
				</c:if>
			</td>

			<td class="text-center" style="width:3%">
				<c:set var="editURL">
					<c:url
						value="authoring/loadHeadingForm.do?sessionMapID=${sessionMapID}&amp;headingIndex=${index}" />
				</c:set>
				<i class="fa fa-pencil" title="<fmt:message key='link.edit'/>" onclick="showMessage('${editURL}')"></i>
			</td>

			<td class="text-center"  style="width:3%">
				<c:set var="deleteURL">
					<c:url
						value="authoring/deleteHeading.do?sessionMapID=${sessionMapID}&amp;headingIndex=${index}" />
				</c:set>
				<i class="fa fa-times"	title="<fmt:message key="link.delete"/>" onclick="runUrl('${deleteURL}')"></img>
			</td> 
		</tr>
		
		<c:set var="index" value="${index + 1}" />
	</c:forEach>

</table>
