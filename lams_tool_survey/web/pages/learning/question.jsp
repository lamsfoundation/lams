<%@ include file="/common/taglibs.jsp"%>

<tr>
	<th class="first">
		<c:if test="${not question.optional}">
			<span style="color: #FF0000">*</span>
		</c:if>
		<c:out value="${question.description}" escapeXml="false"/>
	</th>
</tr>
<tr>
	<td>
		<c:set var="answerText" value=""/>
		<c:if test="${not empty question.answer}">
			<c:set var="answerText" value="${question.answer.answerText}"/>
		</c:if>
		<c:forEach var="option" items="${question.options}">
			<c:set var="checked" value=""/>
			<c:if test="${not empty question.answer}">
				<c:forEach var="choice" items="${question.answer.choices}">
					<c:if test="${choice == option.uid}">
						<c:set var="checked" value="checked=\"checked=\""/>
					</c:if>
				</c:forEach>
			</c:if>
			<c:choose>
				<c:when test="${question.type == 1}">
					<input type="radio" name="optionChoice${question.uid}" value="${option.uid}"  ${checked} />
					${option.description}<BR><BR>
				</c:when>
				<c:when test="${question.type == 2}">
					<input type="checkbox" name="optionChoice${question.uid}" value="${option.uid}" ${checked}/>
					${option.description}<BR><BR>
				</c:when>
			</c:choose>
		</c:forEach>
		<c:if test="${question.type == 3}">
			<textarea name="optionText${question.uid}" rows="7" cols="50" type="_moz">${answerText}</textarea><BR><BR>
		</c:if>
		<c:if test="${question.appendText}">
			<textarea name="optionText${question.uid}" rows="7" cols="50" type="_moz">${answerText}</textarea><BR><BR>
		</c:if>
	</td>
</tr>