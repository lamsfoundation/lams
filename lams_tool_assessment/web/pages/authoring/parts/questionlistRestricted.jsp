<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<div class="panel panel-default voffset5">
	<table class="table table-condensed" id="referencesTable">
		<tr>
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
					<c:choose>
						<c:when test="${questionReference.randomQuestion}">
							<fmt:message key="label.authoring.basic.random.question" />
						</c:when>
						<c:otherwise>
							<c:out value="${question.qbQuestion.name}" escapeXml="true"/>
						</c:otherwise>
					</c:choose>
					
			        <span class='pull-right alert-info btn-xs loffset5 roffset5'>
			       		v.&nbsp;${question.qbQuestion.version}
			        </span>
			        
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
				
				<td width="70px" style="padding-right: 10px;">
					<input name="maxMark${questionReference.sequenceId}" value="${questionReference.maxMark}"
						id="maxMark${questionReference.sequenceId}" class="form-control input-sm">
				</td>
				
				<td class="arrows">
					<!-- Don't display up icon if first line -->
					<c:if test="${not status.first}">
		 				<lams:Arrow state="up" title="<fmt:message key='label.authoring.basic.up'/>" onclick="javascript:upQuestionReference(${status.index})"/>
		 			</c:if>
					<!-- Don't display down icon if last line -->
					<c:if test="${not status.last}">
						<lams:Arrow state="down" title="<fmt:message key='label.authoring.basic.down'/>" onclick="javascript:downQuestionReference(${status.index})"/>
		 			</c:if>
				</td>

				<td width="30px">
					<c:if test="${!questionReference.randomQuestion}">
						<c:set var="editQuestionReferenceUrl" >
							<c:url value='/authoring/editQuestionReference.do'/>?sessionMapID=${sessionMapID}&questionReferenceIndex=${status.index}&KeepThis=true&TB_iframe=true&modal=true
						</c:set>
						<a href="${editQuestionReferenceUrl}" class="thickbox roffset5" style="margin-left: 6px;"> 
							<i class="fa fa-pencil"	title="<fmt:message key="label.authoring.basic.edit" />"></i>
						</a>			
					</c:if>
				</td>
			</tr>
		</c:forEach>
	</table>
</div>
