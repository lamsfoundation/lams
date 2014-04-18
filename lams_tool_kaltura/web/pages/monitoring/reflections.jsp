<%@ include file="/common/taglibs.jsp"%>

<c:if test="${kaltura.reflectOnActivity && not empty reflectList}">

	<br><br>
	<table class="alternative-color" id="reflections">
							
		<tr>
			<th>
				<fmt:message key="label.reflections"/>
			</th>
		</tr>
	
		<c:forEach var="reflectDTO" items="${reflectList}">
			<tr>
				<td valign=top class="align-left">
					<c:out value="${reflectDTO.fullName}" escapeXml="true"/> <lams:Date value="${reflectDTO.lastModified}"/>
					<br>
					<lams:out value="${reflectDTO.entry}" escapeHtml="true"/>
	
				</td>
			</tr>
		</c:forEach>
	
	</table>

</c:if>