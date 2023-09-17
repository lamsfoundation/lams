<%@ include file="/common/taglibs.jsp"%>

<div class="sbox mt-2">
	<div class="sbox-heading clearfix">
		<c:if test="${not question.optional}">
			<abbr class="float-end" title="<fmt:message key='label.answer.required'/>"><i class="fa fa-xs fa-asterisk text-danger float-end"></i></abbr>
		</c:if>

		<c:out value="${question.description}" escapeXml="false" />
		<lams:errors path="questionError${question.uid}"/>
	</div>
	
	<div class="sbox-body">
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
			
			<div class="mb-3">
			<div class="form-check">
				<input id="optionChoice${question.uid}${option.uid}" name="optionChoice${question.uid}" value="${option.uid}" class="form-check-input"
						${checked} 
						<c:choose>
							<c:when test="${question.type == 1}">
								type="radio"
							</c:when>
							<c:when test="${question.type == 2}">
								type="checkbox"
							</c:when>
						</c:choose>						
				/>
				
				<label for="optionChoice${question.uid}${option.uid}" class="form-check-label">
					<c:out value="${option.description}" escapeXml="true" />
				</label>
			</div>
			</div>
		</c:forEach>

		<c:if test="${question.type == 3}">
			<p>
				<lams:textarea name="optionText${question.uid}" rows="7" class="form-control">${answerText}</lams:textarea>
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
				<textarea name="optionText${question.uid}" rows="7" cols="50" type="_moz" ${jsfunc}>${answerText}</textarea>
			</p>
		</c:if>
	</div>
</div>
