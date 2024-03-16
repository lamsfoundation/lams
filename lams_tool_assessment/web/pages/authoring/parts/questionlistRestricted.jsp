<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<div class="panel panel-default voffset5">
	<table class="table table-condensed" id="referencesTable">
		<tr>
			<th>
				#
			</th>
			<th>
				<fmt:message key="label.authoring.basic.list.header.question" />
			</th>
			<th colspan="3">
				<fmt:message key="label.authoring.basic.list.header.mark" />
			</th>
		</tr>	

		<c:forEach var="questionReference" items="${sessionMap.questionReferences}" varStatus="status">
			<c:set var="question" value="${questionReference.question}" />
			<tr>
				<td>
					${status.count})
				</td>
				<td>
					<input type="hidden" name="sequenceId${questionReference.sequenceId}" value="${status.index}" class="reference-sequence-id">
				
					<c:choose>
						<c:when test="${questionReference.randomQuestion}">
							<fmt:message key="label.authoring.basic.type.random.question" />
						</c:when>
						<c:otherwise>
							<c:out value="${question.qbQuestion.name}" escapeXml="true"/>
						</c:otherwise>
					</c:choose>

			       	<span class='pull-right alert-info btn-xs'>
						<c:choose>
							<c:when test="${questionReference.randomQuestion}">
								<fmt:message key="label.authoring.basic.type.random.question" />
							</c:when>
							<c:when test="${question.type == 1}">
								<fmt:message key="label.authoring.basic.type.multiple.choice" />
							</c:when>
							<c:when test="${question.type == 2}">
								<fmt:message key="label.authoring.basic.type.matching.pairs" />
							</c:when>
							<c:when test="${question.type == 3}">
								<fmt:message key="label.authoring.basic.type.short.answer" />
							</c:when>
							<c:when test="${question.type == 4}">
								<fmt:message key="label.authoring.basic.type.numerical" />
							</c:when>
							<c:when test="${question.type == 5}">
								<fmt:message key="label.authoring.basic.type.true.false" />
							</c:when>
							<c:when test="${question.type == 6}">
								<fmt:message key="label.authoring.basic.type.essay" />
							</c:when>
							<c:when test="${question.type == 7}">
								<fmt:message key="label.authoring.basic.type.ordering" />
							</c:when>
							<c:when test="${question.type == 8}">
								<fmt:message key="label.authoring.basic.type.mark.hedging" />
							</c:when>
						</c:choose>
	       			</span>
				</td>

				<td width="30px">
			        <c:if test="${!questionReference.randomQuestion}">
				        <c:set var="maxOtherVersion" />
					    <c:choose>
							<c:when test="${fn:length(question.qbQuestion.versionMap) == 1}">
								<button class="btn btn-default btn-xs dropdown-toggle2 question-version-dropdown" disabled="disabled">
								   <fmt:message key="label.authoring.basic.question.version" />&nbsp;${question.qbQuestion.version}
								</button>
							</c:when>

							<c:otherwise>
								<div class="dropdown question-version-dropdown">
									<button class="btn btn-default btn-xs dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
								    	<fmt:message key="label.authoring.basic.question.version" />&nbsp;${question.qbQuestion.version}&nbsp;<span class="caret"></span>
									</button>

									<ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
										<c:forEach items="${question.qbQuestion.versionMap}" var="otherVersion">
											 <c:set var="maxOtherVersion" value="${otherVersion}" />

								    		<li <c:if test="${question.qbQuestion.version == otherVersion.key}">class="disabled"</c:if>>
								    			<a href="#nogo"
								    			   data-toggle="tooltip" data-placement="top" title="<fmt:message key="label.authoring.basic.question.version.change.tooltip" />"
								    			   onclick="javascript:changeItemQuestionVersion(${status.index}, ${question.qbQuestion.uid}, ${otherVersion.value})">
								    				  <fmt:message key="label.authoring.basic.question.version" />&nbsp;${otherVersion.key}
								    			</a>
								    			<a href="#nogo" class="pull-right"
								    			   data-toggle="tooltip" data-placement="top" title="<fmt:message key="label.authoring.basic.question.version.stats.tooltip" />"
								    			   onClick='javascript:window.open("<lams:LAMSURL/>qb/stats/show.do?qbQuestionUid=${otherVersion.value}", "_blank")'>
									    			  <i class='fa fa-bar-chart'></i>
												</a>
								    		</li>
								    	</c:forEach>
									</ul>

								</div>
							</c:otherwise>
						</c:choose>
					</c:if>
				</td>

				<td width="70px">
					<input name="maxMark" value="${questionReference.maxMark}" class="form-control input-sm max-mark-input">
				</td>

				<td width="30px">
					<i class="fa fa-xs fa-asterisk ${question.answerRequired ? 'text-danger' : ''}"
								title="<fmt:message key="label.answer.required"/>"
								alt="<fmt:message key="label.answer.required"/>"
								onClick="javascript:toggleQuestionRequired(this)"></i>
				</td>

				<td width="30px" style="padding-right: 20px;">
					<c:if test="${!questionReference.randomQuestion}">
						<a class="thickbox roffset5x edit-reference-link" onclick="javascript:editReference(this);" style="color: black;">
							<i class="fa fa-pencil"	title="<fmt:message key="label.authoring.basic.edit" />"></i>
						</a>
					</c:if>
				</td>
			</tr>
		</c:forEach>
	</table>
</div>