<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<title>Question statistics</title>
	
	<lams:css/>
	<link type="text/css" href="${lams}/css/defaultHTML_chart.css" rel="stylesheet">
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
	</style>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.timeago.js"></script>	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/d3.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/chart.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			drawChart('bar', 'chartDiv', ${stats.answersJSON});
			$("time.timeago").timeago();
		});
	</script>
</lams:head>
<body class="stripes">
<c:set var="question" value="${stats.question}" />

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
	</div>
	
	<div class="panel panel-default">
		<div class="panel-heading">
			Average selection chart
		</div>
		<div id="chartDiv" class="panel-body">
		</div>
	</div>
	
	<div class="panel panel-default">
		<div class="panel-heading">
			Burning questions
		</div>
		<div class="panel-body">
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
		</div>
	</div>
	
	<div class="panel panel-default">
		<div class="panel-heading">
			Usage
		</div>
		<div class="panel-body">
			<table class="table table-striped qb-stats-table">
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
							Average correct selection<br>(as first choice)
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
								<c:out value="${activityDTO.activity.title}" />
							</td>
							<td>
								<c:out value="${activityDTO.activity.tool.toolDisplayName}" />
							</td>
							<td>
								<c:choose>
									<c:when test="${empty activityDTO.average}">
										-
									</c:when>
									<c:otherwise>
										<c:out value="${activityDTO.average}" />%
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
			</table>
		</div>
	</div>
	
	<div class="panel panel-default">
		<div class="panel-heading">
			Previous versions
		</div>
		<div class="panel-body">
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
		</div>
	</div>
</lams:Page>
</body>
</lams:html>