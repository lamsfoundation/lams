<%@ include file="/common/taglibs.jsp"%>
<div class="shading-bg">
	<c:if test="${not question.optional}">
		<span style="color: #FF0000">*</span>
	</c:if>
	<c:out value="${question.description}" escapeXml="false" />

	<logic:messagesPresent property="questionError${question.uid}">
		<p class="warning">
			<html:messages id="error" property="questionError${question.uid}">
				<c:out value="${error}" escapeXml="false" />
			</html:messages>
		</p>
	</logic:messagesPresent>

	<c:set var="answerText" value="" />
	<c:if test="${not empty question.answer}">
		<c:set var="answerText" value="${question.answer.answerText}" />
	</c:if>
	<c:forEach var="option" items="${question.options}">
		<c:set var="checked" value="" />
		<c:if test="${not empty question.answer}">
			<c:forEach var="choice" items="${question.answer.choices}">
				<c:if test="${choice == option.uid}">
					<c:set var="checked" value="checked='checked'" />
				</c:if>
			</c:forEach>
		</c:if>
		<p>
			<c:choose>
				<c:when test="${question.type == 1}">
					<input type="radio" name="optionChoice${question.uid}" class="noBorder"
						value="${option.uid}" ${checked} />
					<c:out value="${option.description}" escapeXml="true"/>
				</c:when>
				<c:when test="${question.type == 2}">
					<input type="checkbox" class="noBorder"
					 name="optionChoice${question.uid}"
						value="${option.uid}" ${checked}/>
					<c:out value="${option.description}" escapeXml="true"/>
				</c:when>
			</c:choose>
		</p>
	</c:forEach>

	<c:if test="${question.type == 3}">
		<p>
			<lams:textarea name="optionText${question.uid}" rows="7" cols="50" class="text-area">${answerText}</lams:textarea>
		</p>
	</c:if>
	<c:if test="${question.appendText}">
		<c:set var="jsfunc" value="" />
		<%-- for single choice question, use javascript to ensure only one option or openText can be chosen --%>
		<c:if test="${question.type == 1}">
			<c:set var="jsfunc" value="onclick=\" singleChoice('optionChoice${question.uid}')\""/>
		</c:if>
		
		<p>
			<fmt:message key="label.specifyOther" />
		</p>
		<p>
			<textarea name="optionText${question.uid}" rows="7" cols="50"
				type="_moz"${jsfunc}>${answerText}</textarea>
		</p>
	</c:if>

</div>

