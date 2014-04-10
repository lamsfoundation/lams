<%@ include file="/common/taglibs.jsp"%>

<br>
<table class="alternative-color" id="reflections">
						
	<tr>
		<th>
			<fmt:message key="label.export.reflection"/>
		</th>
	</tr>

	<c:forEach var="reflectDTO" items="${sessionMap.reflectList}">
		<tr>
			<td valign=top class="align-left">
				<c:out value="${reflectDTO.fullName}" escapeXml="true"/> <lams:Date value="${reflectDTO.date}"/>
				<br>
				<lams:out value="${reflectDTO.reflect}" escapeHtml="true"/>

			</td>
		</tr>
	</c:forEach>

</table>
