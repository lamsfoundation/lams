<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-function" prefix="fn"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<c:url value="/lessonConditions.do" var="addLessonDependencyUrl">
		<c:param name="method" value="addLessonDependency" />
		<c:param name="lsId" value="${lsId}" />
	</c:url>
	<c:url value="/lessonConditions.do" var="setDaysToLessonFinishUrl">
		<c:param name="method" value="setDaysToLessonFinish" />
		<c:param name="lsId" value="${lsId}" />
	</c:url>
	
	<lams:css/>
	<style type="text/css">
		td.lessonList {
			padding: 0px 0px 10px 10px;
		}
		
		td.emptyList {
			font-style: italic;
		}
	</style>
	
	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript">
		var lessonId="${lsId}";
		var edit="${edit}";
		
		function removePrecedingLesson(precedingLessonId){
			document.location.href="<lams:LAMSURL/>lessonConditions.do?method=removeLessonDependency&lsId=" + lessonId
						  + "&precedingLessonId=" + precedingLessonId;
		}

		$(document).ready(function(){
			if (edit == 'true'){
				// refresh Index page after editable conditions thickbox is closed
				$('#TB_window',window.parent.document).attr('TB_refreshParentOnClose','true');
			}
		});
	</script>
</lams:head>
    
<body class="stripes">
<lams:Page type="admin" title="">
<logic:messagesPresent> 
	<lams:Alert id="errors" type="danger" close="false">
		<html:messages id="error">
			<c:out value="${error}" escapeXml="false"/><br/>
		</html:messages>
   </lams:Alert>
</logic:messagesPresent>

<div class="container">
	<div class="col-xs-12">
	<!-- Preceding lessons setup -->
		<p class="lead">
			<fmt:message key="label.conditions.box.title">
				<fmt:param>${fn:escapeXml(title)}</fmt:param>
			</fmt:message>
		</p>
	<c:choose>
		<c:when test="${empty precedingLessons}">
			<p>
					<fmt:message key="label.conditions.box.no.dependency" />
			</p>		
		</c:when>
		<c:otherwise>
			<c:forEach var="precedingLesson" items="${precedingLessons}">
				<div class="row">
					<div class="col-xs-6">
								<c:out value="${precedingLesson.name}" />
					</div>
					<div class="col-xs-6">
								<c:if test="${edit}">
									<i class="fa fa-fw fa-trash-o text-danger" style="cursor: pointer;" title="<fmt:message key="label.conditions.box.remove.dependency" />" onclick="javascript:removePrecedingLesson(${precedingLesson.id})"></i>
								</c:if>
					</div>			
				</div>
			</c:forEach>
		</c:otherwise>
	</c:choose>
	<!-- Adding new preceding lesson -->
	<c:if test="${edit}">
		<div class="panel panel-default voffset10">
			<div class="panel-heading">
				<fmt:message key="label.conditions.box.add.dependency" />
			</div>
			<div class="panel-body">
			<c:choose>
				<c:when test="${empty availableLessons}">
					<p>
						<fmt:message key="label.conditions.box.no.dependency" />
					</p>
				</c:when>
				<c:otherwise>
					<form action="${addLessonDependencyUrl}" method="post">
						<div class="form-group">	
							<select class="form-control" name="precedingLessonId">
								<c:forEach var="availableLesson" items="${availableLessons}">
									<option value="${availableLesson.id}">${fn:escapeXml(availableLesson.name)}</option>
								</c:forEach>
							</select>
						</div>
						<div class="form-group pull-right">	
							<input class="btn btn-sm btn-default" type="submit" value="<fmt:message key="index.addlesson"/>" />
						</div>
					</form>
				</c:otherwise>
			</c:choose>
			</div>
		</div>
	</c:if>
	
	<hr/>
	
	<c:choose>
		<c:when test="${empty lessonDaysToFinish}">
			<c:set var="conditionFinishText"><fmt:message key="label.conditions.box.finish.no.date" /></c:set>
		</c:when>
		<c:when test="${lessonIndividualFinish}">
			<c:set var="conditionFinishText">
			<fmt:message key="label.conditions.box.finish.individual.date">
				<fmt:param>${lessonDaysToFinish}</fmt:param>
			</fmt:message>
			</c:set>
		</c:when>
		<c:otherwise>
			<c:set var="conditionFinishText">
			<fmt:message key="label.conditions.box.finish.global.date">
				<fmt:param>${lessonDaysToFinish}</fmt:param>
				<fmt:param><lams:Date style="short" value="${lessonStartDate}"/></fmt:param>
			</fmt:message>
			</c:set>
		</c:otherwise>
	</c:choose>
				
	<c:choose>
	<c:when test="${edit}">
	<!-- Finish date setup -->
		<div class="panel panel-default">
			<div class="panel-heading">${conditionFinishText}</div>
	
		<!-- Changing finish date -->
		<div class="panel-body">
			<form action="${setDaysToLessonFinishUrl}" method="post">
						<div class="form-group">
							<label for="lessonDaysToFinish"><fmt:message key="advanced.tab.form.enter.number.days.label"/>: </label>
							<select class="form-control" id="lessonDaysToFinish" name="lessonDaysToFinish">
							<c:forEach begin="0" end="180" var="index">
								<option
								<c:if test="${(empty lessonDaysToFinish and index eq 0) or index eq lessonDaysToFinish}">
									selected="selected"
								</c:if>
								>${index}</option>
							</c:forEach>
						</select>
						</div>
						<div class="checkbox">
							<label>
							<input type="checkbox" name="lessonIndividualFinish" value="true" 
							<c:if test="${lessonIndividualFinish}">
								checked="checked"
							</c:if>
							/><fmt:message key="advanced.tab.form.individual.not.entire.group.label"/>
							</label>
						</div>
						<div class="form-group">
							<input class="btn btn-sm btn-default pull-right" type="submit" value="<fmt:message key="label.set"/>" />
						</div>								
			</form>
		</div>
		</div>
	</c:when>
	<c:otherwise>
		<p class="lead">${conditionFinishText}</p>
	</c:otherwise>
	</c:choose>
</div>
</div>

</lams:Page>
</body>
</lams:html>
