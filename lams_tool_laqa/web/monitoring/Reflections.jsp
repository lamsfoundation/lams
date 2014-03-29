<%@ include file="/common/taglibs.jsp"%>

<table class="alternative-color" id="reflections">
						
	<tr>			
		<th>
			<fmt:message key="label.reflection"/>
		</th>
	</tr>	
						
	<c:forEach var="reflectDTO" items="${reflectionsContainerDTO}">
		<tr>			
			<td valign=top class="align-left">
				<c:out value="${reflectDTO.userName}" escapeXml="true"/> <lams:Date value="${reflectDTO.date}"/>
				<br>
				<lams:out value="${reflectDTO.entry}" escapeHtml="true" />
			</td>
		</tr>	
	</c:forEach>

</table>
