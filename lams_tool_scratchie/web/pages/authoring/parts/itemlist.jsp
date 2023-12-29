<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:if test="${mode == null}"><c:set var="mode" value="${sessionMap.mode}" /></c:if>
<c:set var="isAuthoringRestricted" value="${mode == 'teacher'}" />

<%@ page import="org.lamsfoundation.lams.qb.service.IQbService" %>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/Sortable.js"></script>
<script>
	// Inform author whether the QB question was modified
	var qbQuestionModified = ${empty qbQuestionModified ? 0 : qbQuestionModified},
		qbMessage = null;
	switch (qbQuestionModified) {
		case <%= IQbService.QUESTION_MODIFIED_UPDATE %>: 
			qbMessage = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="message.qb.modified.update" /></spring:escapeBody>';
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
				qbMessage = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="message.qb.modified.version" /></spring:escapeBody>';
			}

			break;
		case <%= IQbService.QUESTION_MODIFIED_ID_BUMP %>: 
			qbMessage = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="message.qb.modified.new" /></spring:escapeBody>';
			break;
	}
	if (qbMessage) {
		questionsEdited = true;
		alert(qbMessage);
	}
	
    $(document).ready(function(){
    	$('[data-toggle="tooltip"]').bootstrapTooltip();
    	
    	//init questions sorting
	    <c:if test="${not empty sessionMap.itemList}">
		    new Sortable($('#itemTable tbody')[0], {
			    animation: 150,
			    direction: 'vertical',
				store: {
					set: function (sortable) {
						questionsEdited = true;
						//update all sequenceIds
						for (var i = 0; i < sortable.el.rows.length; i++) {
						 	var tr = sortable.el.rows[i];
						 	var input = $("input[name^=sequenceId]", tr);
						 	input.val(i);
						 	var displayOrder = $(".item-display-order", tr);
						 	displayOrder.text(i + 1 + ")");
						}
	
						//prepare SequenceIds parameter
						var serializedSequenceIds = "";
						$("[name^=sequenceId]").each(function() {
							serializedSequenceIds += "&" + this.name + "="  + this.value;
						});
	
						$.ajax({ 
						    url: '<c:url value="/authoring/cacheItemsOrder.do"/>',
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
    });	
</script>

<!-- Dropdown menu for choosing a question from question bank -->
<div class="panel panel-default voffset20">
	<div class="panel-heading panel-title">
		<fmt:message key="label.questions"/>
	</div>
	<input type="hidden" name="itemCount" id="itemCount" value="${fn:length(sessionMap.itemList)}">
		
	<table id="itemTable" class="table table-condensed table-striped">
		<c:forEach var="item" items="${sessionMap.itemList}" varStatus="status">
			<tr>
				<td style="width:5%" class="item-display-order">
					${status.count})
				</td>
				<td style="padding-top:15px; padding-bottom:15px;">
					<input type="hidden" name="sequenceId${item.displayOrder}" value="${status.index}" class="item-sequence-id">
					
					<c:out value="${item.qbQuestion.name}" escapeXml="true"/>
				</td>
				
				<td style="width:1%; vertical-align: top">
					<span class='alert-info btn-xs question-type-alert'>
						<c:choose>
							<c:when test="${item.qbQuestion.type == 1 or item.qbQuestion.type == 8}">
								<fmt:message key="label.type.multiple.choice" />
							</c:when>
							<c:when test="${item.qbQuestion.type == 3}">
								<fmt:message key="label.type.short.answer" />
							</c:when>
						</c:choose>
	       			</span>
				</td>
				<td style="width:3%">
				    <c:set var="maxOtherVersion" />
				    <c:choose>
						<c:when test="${fn:length(item.qbQuestion.versionMap) == 1}">
							<button class="btn btn-default btn-xs dropdown-toggle2 question-version-dropdown" disabled="disabled">
							   <fmt:message key="label.authoring.question.version" />&nbsp;${item.qbQuestion.version}
							</button>
						</c:when>
			
						<c:otherwise>
							<div class="dropdown question-version-dropdown">
								<button class="btn btn-default btn-xs dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							    	<fmt:message key="label.authoring.question.version" />&nbsp;${item.qbQuestion.version}&nbsp;<span class="caret"></span>
								</button>
								
								<ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
									<c:forEach items="${item.qbQuestion.versionMap}" var="otherVersion">
										 <c:set var="maxOtherVersion" value="${otherVersion}" />
										 
							    		<li <c:if test="${item.qbQuestion.version == otherVersion.key}">class="disabled"</c:if>>
							    			<a href="#nogo" data-toggle="tooltip" data-placement="top"
							    			   title="<fmt:message key="label.authoring.question.version.change.tooltip" />"
							    			   onclick="javascript:changeItemQuestionVersion(${status.index}, ${item.qbQuestion.uid}, ${otherVersion.value})">
							    				  <fmt:message key="label.authoring.question.version" />&nbsp;${otherVersion.key}
							    			</a>
							    			<a href="#nogo" class="pull-right" data-toggle="tooltip" data-placement="top"
							    			   title="<fmt:message key="label.authoring.question.version.stats.tooltip" />"
							    			   onClick='javascript:window.open("<lams:LAMSURL/>qb/stats/show.do?qbQuestionUid=${otherVersion.value}", "_blank")'>
								    			  <i class='fa fa-bar-chart'></i>
											</a>
							    		</li>
							    	</c:forEach>
								</ul>
	
							</div>			
						</c:otherwise>
					</c:choose>
				</td>
				
				<td style="width: 3%">
					<c:if test="${not empty maxOtherVersion and item.qbQuestion.version < maxOtherVersion.key}">
						<i class="fa fa-exclamation-triangle newer-version-prompt" data-toggle="tooltip" data-placement="top"
						   title="<fmt:message key="label.authoring.question.version.newer.tooltip" />" 
						   onClick='javascript:window.open("<lams:LAMSURL/>qb/stats/show.do?qbQuestionUid=${maxOtherVersion.value}", "_blank")'>
					</i>
					</c:if>
				</td>
				
				<td align="center" style="width:3%">
		    		<i class='fa fa-bar-chart' data-toggle="tooltip" data-placement="top" title="<fmt:message key="label.authoring.question.version.stats.tooltip" />"
	    			   onClick='javascript:window.open("<lams:LAMSURL/>qb/stats/show.do?qbQuestionUid=${item.qbQuestion.uid}", "_blank")'>
	    			</i>
				</td>	
					
				<td align="center" style="width:3%">
					<c:set var="editItemUrl" >
						<c:url value='/authoring/editItem.do'/>?sessionMapID=${sessionMapID}&itemIndex=${status.index}&KeepThis=true&TB_iframe=true&modal=true
					</c:set>		
					<a href="${editItemUrl}" class="thickbox"> 
						<i class="fa fa-pencil"	title="<fmt:message key='label.edit' />"/></i>
					</a>
				</td>			
				
				<c:if test="${!isAuthoringRestricted}">
					<td align="center" style="width:3%">
						<i class="fa fa-times"	title="<fmt:message key="label.delete" />" id="delete${status.index}" 
							onclick="removeItem(${status.index})"></i>
					</td>
				</c:if>
			</tr>
		</c:forEach>
	</table>
</div>