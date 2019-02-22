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
		
		 {
			font-weight: bold;
		}
		
		 {
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
						<c:out value="${question.name}" />
					</div>
				</div>
				<div class="row">
					<div class="col-sm-1">
						Description:
					</div>
					<div class="col-sm-11">
						<c:out value="${question.description}" />
					</div>
				</div>
				<div class="row">
					<div class="col-sm-1">
						Feedback:
					</div>
					<div class="col-sm-11">
						<c:out value="${question.feedback}" />
					</div>
				</div>
				<div class="row">
					<div class="col-sm-1">
						Mark:
					</div>
					<div class="col-sm-11">
						<c:out value="${question.mark}" />
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
							<c:out value="${option.name}" />
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
					</tr>
					<c:forEach var="activity" items="${stats.activities}">
						<c:set var="lesson" value="${activity.learningDesign.lessons.iterator().next()}" />
						<tr>
							<td>
								<c:out value="${lesson.organisation.name}" />
							</td>
							<td>
								<c:out value="${lesson.lessonName}" />
							</td>
							<td>
								<c:out value="${activity.title}" />
							</td>
							<td>
								<c:out value="${activity.tool.toolDisplayName}" />
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