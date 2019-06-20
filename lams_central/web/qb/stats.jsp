<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<c:set var="question" value="${stats.question}" />

<!DOCTYPE html>
<lams:html>
<lams:head>
	<title>Question statistics</title>
	
	<lams:css/>
	<link type="text/css" href="<lams:LAMSURL/>css/defaultHTML_chart.css" rel="stylesheet">
	<style>
		.question-table {
			margin-bottom
		}
		.question-table > .row {
			padding-bottom: 10px;
		}
		
		.question-table > .row > div:first-child, .qb-stats-table th, .section-header  {
			font-weight: bold;
		}
		
		#chartDiv {
			height: 220px;
		}
		
		#usage a {
			text-decoration: underline;
		}
	</style>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.timeago.js"></script>	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/d3.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/chart.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript">
		var permanentRemove = ${permanentRemove},
			permanentRemovePossible = ${permanentRemovePossible};
		
		$(document).ready(function(){
			drawChart('bar', 'chartDiv', ${stats.answersJSON});
			$("time.timeago").timeago();
		});
		
		// add or copy questions to a collection
		function addCollectionQuestion(copy) {
			var targetCollectionUid = $('#targetCollectionSelect').val();
			$.ajax({
				'url'  : '<lams:LAMSURL />qb/collection/addCollectionQuestion.do',
				'type' : 'POST',
				'dataType' : 'text',
				'data' : {
					'targetCollectionUid' : targetCollectionUid,
					'copy'				  : copy,
					'qbQuestionUid'	      : ${question.uid}
				},
				'cache' : false
			}).done(function(){
				document.location.reload();
			});
		}
		
		function removeCollectionQuestion(collectionUid) {
			if (permanentRemove) {
				if (!permanentRemovePossible) {
					alert("The question is in one collection only, so it would be permanently removed.\n"
						  + "It is not possible as the question is used in sequences.");
					return;
				}
				if (!confirm("The question is in one collection only. Are you sure that you want to remove it permanently?")){
					return;
				}
			}
			$.ajax({
				'url'  : '<lams:LAMSURL />qb/collection/removeCollectionQuestion.do',
				'type' : 'POST',
				'dataType' : 'text',
				'data' : {
					'collectionUid' : collectionUid,
					'qbQuestionUid'	: ${question.uid}
				},
				'cache' : false
			}).done(function(){
				if (permanentRemove) {
					document.location.href = '<lams:LAMSURL />qb/collection/show.do';
				} else {
					document.location.reload();
				}
			});
		}
	</script>
</lams:head>
<body class="stripes">

<lams:Page title="Question statistics" type="admin">
	<div class="panel panel-default">
		<div class="panel-heading">
			Question
		</div>
		<div class="panel-body">
			<div class="question-table">
				<div class="row">
					<div class="col-sm-1">
						Version:
					</div>
					<div class="col-sm-11">
						<c:out value="${question.version}" />
					</div>
				</div>
				<div class="row">
					<div class="col-sm-1">
						Title:
					</div>
					<div class="col-sm-11">
						<c:out value="${question.name}" escapeXml="false" />
					</div>
				</div>
				<div class="row">
					<div class="col-sm-1">
						Description:
					</div>
					<div class="col-sm-11">
						<c:out value="${question.description}" escapeXml="false" />
					</div>
				</div>
				<div class="row">
					<div class="col-sm-1">
						Feedback:
					</div>
					<div class="col-sm-11">
						<c:out value="${question.feedback}" escapeXml="false" />
					</div>
				</div>
				<div class="row">
					<div class="col-sm-1">
						Mark:
					</div>
					<div class="col-sm-11">
						<c:out value="${question.maxMark}" />
					</div>
				</div>
			</div>
			
			<c:if test="${not empty availableCollections and transferAllowed}">
				<div>
					<span>Transfer questions to </span>
					<select class="form-control" id="targetCollectionSelect">
						<c:forEach var="target" items="${availableCollections}">
							<option value="${target.uid}">
								<c:out value="${target.name}" />
							</option>
						</c:forEach>
					</select>
					<button class="btn btn-default" onClick="javascript:addCollectionQuestion(false)">Add</button>
					<button class="btn btn-default" onClick="javascript:addCollectionQuestion(true)">Copy</button>
				</div>
			</c:if>
			
			<div>
				<span>Existing collections</span><br />
				<c:forEach var="collection" items="${existingCollections}">
					<c:out value="${collection.name}" />
					<c:if test="${transferAllowed}">
						<button class="btn btn-default" onClick="javascript:removeCollectionQuestion(${collection.uid})">Remove</button><br />
					</c:if>
				</c:forEach>
			</div>
				
			<c:if test="${not empty question.qbOptions}">
				<p class="question-section-header">Options</p>
				<table class="table table-striped qb-stats-table">
					<tr>
						<th>
							#
						</th>
						<th>
							Title
						</th>
						<th>
							Correct?
						</th>
						<th>
							Average selection<br>(as first choice)
						</th>
					</tr>
					<c:forEach var="option" items="${question.qbOptions}" varStatus="status">
						<tr>
							<td>
								${status.index + 1}
							</td>
							<td>
								<c:out value="${option.name}" escapeXml="false" />
							</td>
							<td>
								<c:if test="${option.correct}">
									<i class="fa fa-check"></i>
								</c:if>
							</td>
							<td>
								<%--(${empty stats.answersRaw[option.uid] ? 0 : stats.answersRaw[option.uid]})--%>
								${stats.answersPercent[option.uid]}%
							</td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</c:if>
	</div>
	
	<c:if test="${not empty question.qbOptions}">
		<div class="panel panel-default">
			<div class="panel-heading">
				Average selection chart
			</div>
			<div id="chartDiv" class="panel-body">
			</div>
		</div>
	</c:if>
	
	<div class="panel panel-default">
		<div class="panel-heading">
			Burning questions
		</div>
		<div class="panel-body">
			<c:choose>
				<c:when test="${empty stats.burningQuestions}">
					This question does not have any burning questions
				</c:when>
				<c:otherwise>
					<table class="table table-striped qb-stats-table">
						<tr>
							<th>
								Question
							</th>
							<th>
								Likes
							</th>
						</tr>
						<c:forEach var="question" items="${stats.burningQuestions}">
							<tr>
								<td>
									<c:out value="${question.key}" />
								</td>
								<td>
									<c:out value="${question.value}" />
								</td>
							</tr>
						</c:forEach>
					</table>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	
	<div class="panel panel-default">
		<div class="panel-heading">
			Usage in lessons
		</div>
		<div class="panel-body">
			<c:choose>
				<c:when test="${empty stats.activities}">
					This question is not used in any lesson
				</c:when>
				<c:otherwise>
					<table id="usage" class="table table-striped qb-stats-table">
						<tr>
							<th>
								Organisation
							</th>
							<th>
								Lesson
							</th>
							<th>
								Activity
							</th>
							<th>
								Tool type
							</th>
							<th>
								Test participant count
							</th>
							<th>
								Difficulty index
							</th>
							<th>
								Discrimination index
							</th>
							<th>
								Point biserial
							</th>
						</tr>
						<c:forEach var="activityDTO" items="${stats.activities}">
							<c:set var="lesson" value="${activityDTO.activity.learningDesign.lessons.iterator().next()}" />
							<tr>
								<td>
									<c:out value="${lesson.organisation.name}" />
								</td>
								<td title="${lesson.lessonId}">
									<c:out value="${lesson.lessonName}" />
								</td>
								<td title="${activityDTO.activity.activityId}">
									<c:choose>
										<c:when test="${empty activityDTO.monitorURL}">
											<c:out value="${activityDTO.activity.title}" />
										</c:when>
										<c:otherwise>
											<a href="${activityDTO.monitorURL}">
												<c:out value="${activityDTO.activity.title}" />
											</a>
										</c:otherwise>
									</c:choose>
								</td>
								<td>
									<c:out value="${activityDTO.activity.tool.toolDisplayName}" />
								</td>
								<td>
									<c:out value="${activityDTO.participantCount}" />
								</td>
								<c:choose>
									<c:when test="${activityDTO.participantCount < 2}">
										<td>-</td>
										<td>-</td>
										<td>-</td>
									</c:when>
									<c:otherwise>
										<td><fmt:formatNumber type="number" maxFractionDigits="2" value="${activityDTO.difficultyIndex}" /></td>
										<td><fmt:formatNumber type="number" maxFractionDigits="2" value="${activityDTO.discriminationIndex}" /></td>
										<td><fmt:formatNumber type="number" maxFractionDigits="2" value="${activityDTO.pointBiserial}" /></td>
										
									</c:otherwise>
								</c:choose>
							</tr>
						</c:forEach>
					</table>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	
	<div class="panel panel-default">
		<div class="panel-heading">
			Previous versions
		</div>
		<div class="panel-body">
			<c:choose>
				<c:when test="${empty stats.versions}">
					This question does not have any previous versions
				</c:when>
				<c:otherwise>
					<table class="table table-striped qb-stats-table">
						<tr>
							<th>
								#
							</th>
							<th>
								Created date
							</th>
							<th>
								Created ago
							</th>
						</tr>
						<c:forEach var="version" items="${stats.versions}">
							<tr>
								<td>
									<a href="/lams/qb/stats.do?qbQuestionUid=${version.uid}">v${version.version}</a>
								</td>
								<td>
									<lams:Date value="${version.createDate}" />
								</td>
								<td>
									<lams:Date value="${version.createDate}" timeago="true"/>
								</td>
							</tr>
						</c:forEach>
					</table>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</lams:Page>
</body>
</lams:html>