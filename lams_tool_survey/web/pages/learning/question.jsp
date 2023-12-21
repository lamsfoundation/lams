<%@ include file="/common/taglibs.jsp"%>

<div class="card lcard mt-2">
	<div class="card-header">
		<div class="badge text-bg-danger float-end">${questionSequenceNumber}</div>
		<c:if test="${not question.optional}">
			<abbr class="float-end badge text-bg-danger me-2" title="<fmt:message key='label.answer.required'/>">
				<i class="fa fa-xs fa-asterisk"></i>
			</abbr>
		</c:if>

		<c:out value="${question.description}" escapeXml="false" />
	</div>
	
	<div class="card-body">
		<lams:errors5 path="questionError${question.uid}"/>
	
		<c:set var="answerText" value="" />
		<c:if test="${not empty question.answer}">
			<c:set var="answerText" value="${question.answer.answerText}" />
		</c:if>

		<fieldset>
			<legend class="visually-hidden" id="fieldset-legend-${question.uid}">
				<c:out value="${question.description}" escapeXml="false" />
			</legend>
			
			<div class="div-hover mx-2">
				<c:forEach var="option" items="${question.options}">
					<c:set var="checked" value="" />
					<c:if test="${not empty question.answer}">
						<c:forEach var="choice" items="${question.answer.choices}">
							<c:if test="${choice == option.uid}">
								<c:set var="checked" value="checked='checked'" />
							</c:if>
						</c:forEach>
					</c:if>
					
					<div class="row">
						<div class="form-check">
							<input id="optionChoice${question.uid}${option.uid}" name="optionChoice${question.uid}" value="${option.uid}" class="form-check-input"
									${checked} 
									<c:choose>
										<c:when test="${question.type == 1}">
											type="radio"
											onclick="emptyTextarea('${question.uid}');"
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

				<c:if test="${question.appendText}">
					<div class="row">
						<c:set var="jsfunc" value="" />
						<%-- for single choice question, use javascript to ensure only one option or openText can be chosen --%>
						<c:if test="${question.type == 1}">
							<c:set var="jsfunc" value="onclick=\" singleChoice('optionChoice${question.uid}')\""/>
						</c:if>
			
						<p>
							<fmt:message key="label.specifyOther" />
						</p>
						<p>
							<textarea name="optionText${question.uid}" rows="7" cols="50" type="_moz" ${jsfunc} class="form-control">${answerText}</textarea>
						</p>
					</div>
				</c:if>
			</div>
		</fieldset>

		<c:if test="${question.type == 3}">
			<p>
				<lams:textarea name="optionText${question.uid}" rows="7" class="form-control" aria-labelledby="fieldset-legend-${question.uid}">${answerText}</lams:textarea>
			</p>
		</c:if>
	</div>
</div>
