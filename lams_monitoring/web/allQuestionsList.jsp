<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>
<lams:html>
<lams:head>
	<lams:css/>
	<style type="text/css">
		.question-index {
			float: left; 
			padding-right: 10px;
		}
		.question-title {
			padding-bottom: 8px;
		}
		h1 {
			padding-top: 15px;
			margin-bottom: 10px;
		}
	</style>
</lams:head>

<body class="stripes">
<lams:Page type="learner" title="All questions list">

	<c:if test="${(empty mcContentSet) and (empty scratchieSet) and (empty assessmentMap)}">
		<lams:Alert id="no-activities" type="info" close="true">
			Current lesson doesn't contain activities with questions.
		</lams:Alert>
	</c:if>

	<c:forEach var="mcContent" items="${mcContentSet}">
		<c:set var="title">MCQ: <c:out value="${mcContent.title}"/></c:set>
		<lams:SimplePanel title="${title}">
		
			<c:forEach var="question" items="${mcContent.mcQueContents}" varStatus="i">
				<div class="question-title">
					<span class="question-index">
						${i.index+1})
					</span> 
					<c:out value="${question.qbQuestion.description}" escapeXml="false"/>
				</div>
				
				<table class="table table-striped table-hover table-condensed">
					<c:forEach var="qbOption" items="${question.qbQuestion.qbOptions}">
						<tr>
							<td>
								<c:out value="${qbOption.name}" escapeXml="false"/>
							</td>
						</tr>
					</c:forEach>
				</table>
			</c:forEach>
		
		</lams:SimplePanel>
	</c:forEach>
	
	<c:forEach var="scratchie" items="${scratchieSet}">
		<c:set var="title">Scratchie: <c:out value="${scratchie.title}"/></c:set>
		<lams:SimplePanel title="${title}">
		
			<c:forEach var="item" items="${scratchie.scratchieItems}" varStatus="i">
				<div class="question-title">
					<span class="question-index">
						${i.index+1})
					</span> 
					<c:out value="${item.qbQuestion.description}" escapeXml="false"/>
				</div>
				
				<table class="table table-striped table-hover table-condensed">
					<c:forEach var="qbOption" items="${item.qbQuestion.qbOptions}">
						<tr>
							<td>
								<c:out value="${qbOption.name}" escapeXml="false"/>
							</td>
						</tr>
					</c:forEach>
				</table>
			</c:forEach>
			
		</lams:SimplePanel>
	</c:forEach>
	
	<c:forEach var="entry" items="${assessmentMap}">
		<c:set var="assessment" value="${entry.key}"/>
		<c:set var="questions" value="${entry.value}"/>
		
		<c:set var="title">Assessment: <c:out value="${assessment.title}"/></c:set>
		<lams:SimplePanel title="${title}">
		
			<c:forEach var="question" items="${questions}" varStatus="i">
				<c:set var="qbQuestion" value="${question.qbQuestion}"/>
				<div class="question-title">
					<span class="question-index">
						${i.index+1})
					</span> 
					<c:out value="${qbQuestion.description}" escapeXml="false"/>
				</div>
				
				<table class="table table-striped table-hover table-condensed">
					<%@ include file="assessmentAnswers.jsp"%>
				</table>
			</c:forEach>
			
		</lams:SimplePanel>
	</c:forEach>

	<div id="footer"></div>		

</lams:Page>
</body>
</lams:html>