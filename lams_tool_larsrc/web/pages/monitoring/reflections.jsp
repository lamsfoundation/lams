<%@ include file="/common/taglibs.jsp"%>

<br><br>

<table class="alternative-color">

	<tr>
		<th>
			<fmt:message key="title.reflection"/>
		</th>
	</tr>
										
	<c:forEach var="reflectDTO" items="${sessionMap.reflectList}">
		<tr>
			<td>
				<strong><c:out value="${reflectDTO.fullName}" escapeXml="true"/></strong> - <lams:Date value="${reflectDTO.date}"/>
				<br>
				<lams:out value="${reflectDTO.reflect}" escapeHtml="true" />
			</td>
		</tr>
	</c:forEach>

</table>
<br>
