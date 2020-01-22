<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<%@ page import="org.lamsfoundation.lams.qb.service.IQbService" %>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/Sortable.js"></script>
<script>
	// Inform author whether the QB question was modified
	var qbQuestionModified = ${empty qbQuestionModified ? 0 : qbQuestionModified},
		qbMessage = null;
	switch (qbQuestionModified) {
		case <%= IQbService.QUESTION_MODIFIED_UPDATE %>: 
			qbMessage = '<fmt:message key="message.qb.modified.update" />';
			break;
		case <%= IQbService.QUESTION_MODIFIED_VERSION_BUMP %>: 
			let showMessage = true;
		
			// check if we are in main authoring environment
			if (typeof window.parent.GeneralLib != 'undefined') {
				// check if any other activities require updating
				let activitiesWithQuestion = window.parent.GeneralLib.checkQuestionExistsInToolActivities('${oldQbQuestionUid}');
				if (activitiesWithQuestion.length > 1) {
					showMessage = false;
					// update, if teacher agrees to it
					window.parent.GeneralLib.replaceQuestionInToolActivities('${sessionMap.toolContentID}', activitiesWithQuestion, 
																			 '${oldQbQuestionUid}','${newQbQuestionUid}');
				}
			}
	
			if (showMessage) {
				qbMessage = '<fmt:message key="message.qb.modified.version" />';
			}
			break;
		case <%= IQbService.QUESTION_MODIFIED_ID_BUMP %>: 
			qbMessage = '<fmt:message key="message.qb.modified.new" />';
			break;
	}
	if (qbMessage) {
		alert(qbMessage);
	}

	$(document).ready(function(){
		
	    //init questions sorting
	    <c:if test="${not empty sessionMap.questionReferences}">
	    new Sortable($('#referencesTable tbody')[0], {
		    animation: 150,
		    direction: 'vertical',
			store: {
				set: function (sortable) {
					//update all sequenceIds
					for (var i = 0; i < sortable.el.rows.length; i++) {
					 	var tr = sortable.el.rows[i];
					 	var input = $("input[name^=sequenceId]", tr);
					 	input.val(i);
					 	var displayOrder = $(".reference-display-order", tr);
					 	displayOrder.text(i + 1 + ")");
					}

					//prepare SequenceIds parameter
					var serializedSequenceIds = "";
					$("[name^=sequenceId]").each(function() {
						serializedSequenceIds += "&" + this.name + "="  + this.value;
					});

					$.ajax({ 
					    url: '<c:url value="/authoring/cacheReferencesOrder.do"/>',
						type: 'POST',
						data: {
							sessionMapID: "${sessionMapID}",
							sequenceIds: serializedSequenceIds
						}
					});

					//update names
					$("[name^=sequenceId]").each(function() {
						var newSequenceId = this.value;
						//update name of the hidden input
						this.name = "sequenceId" + newSequenceId;
					});
				}
			}
		});
		</c:if>
		
		
	    $('[data-toggle="tooltip"]').bootstrapTooltip();
	});
</script>

<div class="panel panel-default voffset5">
	<table class="table table-condensed" id="referencesTable">
		<thead>
			<tr>
				<th>
					#
				</th>
				<th colspan="5">
					<fmt:message key="label.authoring.basic.list.header.question" />
				</th>
				<th colspan="4">
					<fmt:message key="label.authoring.basic.list.header.mark" />
				</th>
			</tr>
		</thead>	

		<c:forEach var="questionReference" items="${sessionMap.questionReferences}" varStatus="status">
			<c:set var="question" value="${questionReference.question}" />
			<tr>
				<td class="reference-display-order">
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
				</td>
				<td style="width:30px; vertical-align: top">
					<span class='alert-info btn-xs question-type-alert'>
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
				
				<td width="30px">
					<c:if test="${not empty maxOtherVersion and question.qbQuestion.version < maxOtherVersion.key}">
						<i class="fa fa-exclamation-triangle newer-version-prompt" data-toggle="tooltip" data-placement="top"
						   data-toggle="tooltip" data-placement="top" title="<fmt:message key="label.authoring.basic.question.version.newer.tooltip" />" 
						   onClick='javascript:window.open("<lams:LAMSURL/>qb/stats/show.do?qbQuestionUid=${maxOtherVersion.value}", "_blank")'>
					</i>
					</c:if>
				</td>
				
				<td align="center"  width="30px">
		    		<i class='fa fa-bar-chart' data-toggle="tooltip" data-placement="top" title="<fmt:message key="label.authoring.basic.question.version.stats.tooltip" />"
	    			   onClick='javascript:window.open("<lams:LAMSURL/>qb/stats/show.do?qbQuestionUid=${question.qbQuestion.uid}", "_blank")'>
	    			</i>
				</td>	
				
				<td width="70px" style="padding-right: 10px;">
					<input name="maxMark" value="${questionReference.maxMark}" class="form-control input-sm max-mark-input">
				</td>
				
				<td width="30px">
					<i class="fa fa-xs fa-asterisk ${question.answerRequired ? 'text-danger' : ''}" 
								data-toggle="tooltip" data-placement="top" title="<fmt:message key="label.answer.required"/>" 
								alt="<fmt:message key="label.answer.required"/>"
								onClick="javascript:toggleQuestionRequired(this)"></i>
				</td>
				
				<td width="30px">
					<a class="thickbox roffset5x edit-reference-link" onclick="javascript:editReference(this);" style="color: black;"> 
						<i class="fa fa-pencil"	 title="<fmt:message key="label.authoring.basic.edit" />"></i>
					</a>
				</td>

				<td width="30px">
					<i class="fa fa-times delete-reference-link" title="<fmt:message key="label.authoring.basic.delete" />"></i>
				</td>
			</tr>
		</c:forEach>
	</table>
</div>
