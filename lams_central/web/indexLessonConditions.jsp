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
<div id="page">
<div id="content">

<logic:messagesPresent> 
	<div class="warning">
		<html:messages id="error">
			<c:out value="${error}" escapeXml="false"/><br/>
		</html:messages>
    </div>
</logic:messagesPresent>

<table>
<!-- Preceding lessons setup -->
	<tr>
		<td colspan="2">
			<h4>
				<fmt:message key="label.conditions.box.title">
					<fmt:param>${fn:escapeXml(title)}</fmt:param>
				</fmt:message>
			</h4>
		</td>
	</tr>
	<c:choose>
		<c:when test="${empty precedingLessons}">
			<tr>
				<td colspan="2" class="lessonList emptyList">
					<fmt:message key="label.conditions.box.no.dependency" />
				</td>
			</tr>
		</c:when>
		<c:otherwise>
			<c:forEach var="precedingLesson" items="${precedingLessons}">
				<tr>
					<td class="lessonList" style="width: 70%">
						<c:out value="${precedingLesson.name}" />
					</td>
					<td>
						<c:if test="${edit}">
							<img src="<lams:LAMSURL/>images/icons/cross.png"
								 style="cursor: pointer;"
								 title="<fmt:message key="label.conditions.box.remove.dependency" />"
								 onclick="javascript:removePrecedingLesson(${precedingLesson.id})" />
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</c:otherwise>
	</c:choose>
	
	<!-- Adding new preceding lesson -->
	<c:if test="${edit}">
		<tr>
			<td colspan="2">
				<h4>
					<fmt:message key="label.conditions.box.add.dependency" />
				</h4>
			</td>
		</tr>
		
		<c:choose>
			<c:when test="${empty availableLessons}">
				<tr>
					<td colspan="2" class="lessonList emptyList">
						<fmt:message key="label.conditions.box.no.dependency" />
					</td>
				</tr>
			</c:when>
			<c:otherwise>
				<form action="${addLessonDependencyUrl}" method="post">
				<tr>
					<td class="lessonList" style="width: 70%">
						<select name="precedingLessonId">
							<c:forEach var="availableLesson" items="${availableLessons}">
								<option value="${availableLesson.id}">${fn:escapeXml(availableLesson.name)}</option>
							</c:forEach>
						</select>
					</td>
					<td>
						<input class="button" type="submit" value="<fmt:message key="index.addlesson"/>" />
					</td>
				</tr>
				</form>
			</c:otherwise>
		</c:choose>
	</c:if>
	
	<!-- Finish date setup -->
	<tr>
		<td colspan="2">
			<h4>
				<c:choose>
					<c:when test="${empty lessonDaysToFinish}">
						<fmt:message key="label.conditions.box.finish.no.date" />
					</c:when>
					<c:when test="${lessonIndividualFinish}">
						<fmt:message key="label.conditions.box.finish.individual.date">
							<fmt:param>${lessonDaysToFinish}</fmt:param>
						</fmt:message>
					</c:when>
					<c:otherwise>
						<fmt:message key="label.conditions.box.finish.global.date">
							<fmt:param>${lessonDaysToFinish}</fmt:param>
							<fmt:param><lams:Date style="short" value="${lessonStartDate}"/></fmt:param>
						</fmt:message>
					</c:otherwise>
				</c:choose>
			</h4>
		</td>
	</tr>
	
	<!-- Changing finish date -->
	<c:if test="${edit}">
		<form action="${setDaysToLessonFinishUrl}" method="post">
			<tr>
				<td class="lessonList" style="width: 70%">
					<select name="lessonDaysToFinish">
						<c:forEach begin="0" end="180" var="index">
							<option
							<c:if test="${(empty lessonDaysToFinish and index eq 0) or index eq lessonDaysToFinish}">
								selected="selected"
							</c:if>
							>${index}</option>
						</c:forEach>
					</select>
					<fmt:message key="advanced.tab.form.enter.number.days.label"/>
				</td>
				<td rowspan="2" style="vertical-align: middle">
					<input class="button" type="submit" value="<fmt:message key="label.set"/>" />
				</td>
			</tr>
			<tr>
				<td>
					<input type="checkbox" name="lessonIndividualFinish" value="true" 
					<c:if test="${lessonIndividualFinish}">
						checked="checked"
					</c:if>
					/><fmt:message key="advanced.tab.form.individual.not.entire.group.label"/>
				</td>
			</tr>	
		</form>
	</c:if>
</table>
</div>
</div>
</body>
</lams:html>