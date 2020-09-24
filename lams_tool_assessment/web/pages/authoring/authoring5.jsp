<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.assessment.AssessmentConstants"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" scope="request" />
<c:set var="mode" value="${sessionMap.mode}" scope="request" />
<c:set var="isAuthoringRestricted" value="${sessionMap.isAuthoringRestricted}" scope="request" />
<c:set var="lamsUrl" scope="request"><lams:LAMSURL/></c:set>

<lams:html>
<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<title><fmt:message key="label.author.title" /></title>
	
	<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap4.min.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome/css/font-awesome.min.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery.tablesorter.theme.bootstrap.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery.tablesorter.custom.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/thickbox.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/components-responsive.css">
	<link rel="stylesheet" href="<lams:WebAppURL/>includes/css/assessment.css">

	<script src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>	
	<script src="<lams:LAMSURL/>includes/javascript/bootstrap4.bundle.min.js"></script>
	<script src="<lams:LAMSURL/>includes/javascript/jquery.tablesorter.js"></script>
	<script src="<lams:LAMSURL/>includes/javascript/jquery.tablesorter.pager.js"></script>
	<script src="<lams:LAMSURL/>includes/javascript/thickbox.js"></script>
	<script src="<lams:LAMSURL/>includes/javascript/common.js"></script> 
	
	<script>
		$(document).ready(function(){
		  $('[data-toggle="tooltip"]').tooltip();
		});
	</script>
</head>

<body>
<lams:PageComponent titleKey="label.author.title">
	<c:if test="${isAuthoringRestricted}">
		<lams:Alert id="edit-in-monitor-while-assessment-already-attempted" type="error" close="true">
			<fmt:message key="label.edit.in.monitor.warning"/>
		</lams:Alert>
	</c:if>
	
	<form:form action="updateContent.do" method="post" modelAttribute="assessmentForm" id="authoringForm">
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
		<input type="hidden" name="mode" value="${mode}">
		
		<form:hidden path="assessment.contentId" />
		<form:hidden path="sessionMapID" />
		<form:hidden path="contentFolderID" />

           <div class="container-fluid" style="max-width: 1600px">
			<div id="content" class="content row">
				
				<lams:Panel id="basic" titleKey="label.authoring.heading.basic" iconClass="fa-file-o" colorClass="green">
                    <jsp:include page="basic5.jsp"/>
				</lams:Panel>
					
               	<lams:Panel id="questions" titleKey="label.authoring.basic.question.list.title" iconClass="fa-question-circle-o" colorClass="yellow" expanded="true">
               		<jsp:include page="questions5.jsp"/>
				</lams:Panel>
				
				<lams:Panel id="advanced" titleKey="label.authoring.heading.advance" iconClass="fa-gear" colorClass="purple" expanded="false">
                   	<div class="col-12 col-xl-6 pr-5">
                   		<lams:Dropdown name="questions-per-page" 
                   					   labelKey="label.authoring.advance.questions.per.page"
							 		   tooltipKey="label.authoring.advance.questions.per.page.tooltip"
							 		   tooltipDescriptionKey="label.authoring.advance.questions.per.page.tooltip.description"
						>
                           <option>All</option>
                           <option>All</option>
                           <option>All</option>
                        </lams:Dropdown>
                                    
                        <lams:Switch name="question-numbering" checked="true" 
							 labelKey="label.authoring.advance.numbered.questions"
							 tooltipKey="label.authoring.advance.numbered.questions.tooltip"
							 tooltipDescriptionKey="label.authoring.advance.numbered.questions.tooltip.description"
						/>
						
						<lams:Dropdown name="time-limit" 
                   					   labelKey="label.authoring.advance.time.limit"
							 		   tooltipKey="label.authoring.advance.time.limit.tooltip"
							 		   tooltipDescriptionKey="label.authoring.advance.time.limit.tooltip.description"
						>
                           <option>All</option>
                           <option>All</option>
                           <option>All</option>
                        </lams:Dropdown>
                        		
						<lams:Dropdown name="attempts-allowed" 
                   					   labelKey="label.authoring.advance.attempts.allowed"
							 		   tooltipKey="label.authoring.advance.attempts.allowed.tooltip"
							 		   tooltipDescriptionKey="label.authoring.advance.attempts.allowed.tooltip.description"
						>
                           <option>1</option>
                           <option>1</option>
                           <option>1</option>
                        </lams:Dropdown>
                        
						<lams:Dropdown name="passing-mark" 
                   					   labelKey="label.authoring.advance.passing.mark"
							 		   tooltipKey="label.authoring.advance.passing.mark.tooltip"
							 		   tooltipDescriptionKey="label.authoring.advance.passing.mark.tooltip.description"
						>
                            <option>None</option>
                            <option>None</option>
                            <option>None</option>
                        </lams:Dropdown>
					</div>
					
					<div class="col-12 col-xl-6 pr-4">
						<lams:Switch name="shuffle-questions" checked="true" 
									 labelKey="label.authoring.basic.shuffle.the.choices"
									 tooltipKey="label.authoring.basic.shuffle.the.choices.tooltip"
									 tooltipDescriptionKey="label.authoring.basic.shuffle.the.choices.tooltip.description"
						/>
						
						<lams:Switch name="display-all"
									 labelKey="label.authoring.advance.display.summary"
									 tooltipKey="label.authoring.advance.display.summary.tooltip"
									 tooltipDescriptionKey="label.authoring.advance.display.summary.tooltip.description"
						/>
						
                       <lams:Switch name="display-over-all" checked="true" 
									labelKey="label.authoring.advance.allow.students.overall.feedback"
									tooltipKey="label.authoring.advance.allow.students.overall.feedback.tooltip"
									tooltipDescriptionKey="label.authoring.advance.allow.students.overall.feedback.tooltip.description"
						/>
						
						<lams:Switch name="allow-learners"
									 labelKey="label.authoring.advance.allow.students.grades"
									 tooltipKey="label.authoring.advance.allow.students.grades.tooltip"
									 tooltipDescriptionKey="label.authoring.advance.allow.students.grades.tooltip.description"
						/>
						
						<lams:Switch name="enable-justification" checked="true" 
									labelKey="label.authoring.advance.answer.justification"
									tooltipKey="label.authoring.advance.answer.justification.tooltip"
									tooltipDescriptionKey="label.authoring.advance.answer.justification.tooltip.description"
						/>
						
						<lams:Switch name="enable-confidence"
									 labelKey="label.enable.confidence.levels"
									 tooltipKey="label.enable.confidence.levels.tooltip"
									 tooltipDescriptionKey="label.enable.confidence.levels.tooltip.description"
						/>
					</div>
				</lams:Panel>
				
				<%--
				<lams:Panel id="leaderselection" titleKey="label.select.leader" iconClass="fa-star-o" colorClass="yellow" expanded="false">
					<div class="col-12 col-xl-6 pr-5">
                   		<lams:Dropdown name="questions-per-page" 
                   					   labelKey="label.authoring.advance.questions.per.page"
							 		   tooltipKey="label.authoring.advance.questions.per.page.tooltip"
							 		   tooltipDescriptionKey="label.authoring.advance.questions.per.page.tooltip.description"
						>
                           <option>All</option>
                           <option>All</option>
                           <option>All</option>
                        </lams:Dropdown>
                                    
                        <lams:Switch name="question-numbering" checked="true" 
							 labelKey="label.authoring.advance.numbered.questions"
							 tooltipKey="label.authoring.advance.numbered.questions.tooltip"
							 tooltipDescriptionKey="label.authoring.advance.numbered.questions.tooltip.description"
						/>
						
						<lams:Dropdown name="time-limit" 
                   					   labelKey="label.authoring.advance.time.limit"
							 		   tooltipKey="label.authoring.advance.time.limit.tooltip"
							 		   tooltipDescriptionKey="label.authoring.advance.time.limit.tooltip.description"
						>
                           <option>All</option>
                           <option>All</option>
                           <option>All</option>
                        </lams:Dropdown>
                        		
						<lams:Dropdown name="attempts-allowed" 
                   					   labelKey="label.authoring.advance.attempts.allowed"
							 		   tooltipKey="label.authoring.advance.attempts.allowed.tooltip"
							 		   tooltipDescriptionKey="label.authoring.advance.attempts.allowed.tooltip.description"
						>
                           <option>1</option>
                           <option>1</option>
                           <option>1</option>
                        </lams:Dropdown>
                        
						<lams:Dropdown name="passing-mark" 
                   					   labelKey="label.authoring.advance.passing.mark"
							 		   tooltipKey="label.authoring.advance.passing.mark.tooltip"
							 		   tooltipDescriptionKey="label.authoring.advance.passing.mark.tooltip.description"
						>
                            <option>None</option>
                            <option>None</option>
                            <option>None</option>
                        </lams:Dropdown>
					</div>
					
					<div class="col-12 col-xl-6 pr-4">
						<lams:Switch name="shuffle-questions" checked="true" 
									 labelKey="label.authoring.basic.shuffle.the.choices"
									 tooltipKey="label.authoring.basic.shuffle.the.choices.tooltip"
									 tooltipDescriptionKey="label.authoring.basic.shuffle.the.choices.tooltip.description"
						/>
						
						<lams:Switch name="display-all"
									 labelKey="label.authoring.advance.display.summary"
									 tooltipKey="label.authoring.advance.display.summary.tooltip"
									 tooltipDescriptionKey="label.authoring.advance.display.summary.tooltip.description"
						/>
						
                       <lams:Switch name="display-over-all" checked="true" 
									labelKey="label.authoring.advance.allow.students.overall.feedback"
									tooltipKey="label.authoring.advance.allow.students.overall.feedback.tooltip"
									tooltipDescriptionKey="label.authoring.advance.allow.students.overall.feedback.tooltip.description"
						/>
						
						<lams:Switch name="allow-learners"
									 labelKey="label.authoring.advance.allow.students.grades"
									 tooltipKey="label.authoring.advance.allow.students.grades.tooltip"
									 tooltipDescriptionKey="label.authoring.advance.allow.students.grades.tooltip.description"
						/>
						
						<lams:Switch name="enable-justification" checked="true" 
									labelKey="label.authoring.advance.answer.justification"
									tooltipKey="label.authoring.advance.answer.justification.tooltip"
									tooltipDescriptionKey="label.authoring.advance.answer.justification.tooltip.description"
						/>
						
						<lams:Switch name="enable-confidence"
									 labelKey="label.enable.confidence.levels"
									 tooltipKey="label.enable.confidence.levels.tooltip"
									 tooltipDescriptionKey="label.enable.confidence.levels.tooltip.description"
						/>
					</div>
				</lams:Panel>
				
				<lams:Panel id="notifications" titleKey="label.notifications" iconClass="fa-bell-o" colorClass="purple" expanded="false">
					<div class="col-12 col-xl-6 pr-5">
                   		<lams:Dropdown name="questions-per-page" 
                   					   labelKey="label.authoring.advance.questions.per.page"
							 		   tooltipKey="label.authoring.advance.questions.per.page.tooltip"
							 		   tooltipDescriptionKey="label.authoring.advance.questions.per.page.tooltip.description"
						>
                           <option>All</option>
                           <option>All</option>
                           <option>All</option>
                        </lams:Dropdown>
                                    
                        <lams:Switch name="question-numbering" checked="true" 
							 labelKey="label.authoring.advance.numbered.questions"
							 tooltipKey="label.authoring.advance.numbered.questions.tooltip"
							 tooltipDescriptionKey="label.authoring.advance.numbered.questions.tooltip.description"
						/>
						
						<lams:Dropdown name="time-limit" 
                   					   labelKey="label.authoring.advance.time.limit"
							 		   tooltipKey="label.authoring.advance.time.limit.tooltip"
							 		   tooltipDescriptionKey="label.authoring.advance.time.limit.tooltip.description"
						>
                           <option>All</option>
                           <option>All</option>
                           <option>All</option>
                        </lams:Dropdown>
                        		
						<lams:Dropdown name="attempts-allowed" 
                   					   labelKey="label.authoring.advance.attempts.allowed"
							 		   tooltipKey="label.authoring.advance.attempts.allowed.tooltip"
							 		   tooltipDescriptionKey="label.authoring.advance.attempts.allowed.tooltip.description"
						>
                           <option>1</option>
                           <option>1</option>
                           <option>1</option>
                        </lams:Dropdown>
                        
						<lams:Dropdown name="passing-mark" 
                   					   labelKey="label.authoring.advance.passing.mark"
							 		   tooltipKey="label.authoring.advance.passing.mark.tooltip"
							 		   tooltipDescriptionKey="label.authoring.advance.passing.mark.tooltip.description"
						>
                            <option>None</option>
                            <option>None</option>
                            <option>None</option>
                        </lams:Dropdown>
					</div>
					
					<div class="col-12 col-xl-6 pr-4">
						<lams:Switch name="shuffle-questions" checked="true" 
									 labelKey="label.authoring.basic.shuffle.the.choices"
									 tooltipKey="label.authoring.basic.shuffle.the.choices.tooltip"
									 tooltipDescriptionKey="label.authoring.basic.shuffle.the.choices.tooltip.description"
						/>
						
						<lams:Switch name="display-all"
									 labelKey="label.authoring.advance.display.summary"
									 tooltipKey="label.authoring.advance.display.summary.tooltip"
									 tooltipDescriptionKey="label.authoring.advance.display.summary.tooltip.description"
						/>
						
                       <lams:Switch name="display-over-all" checked="true" 
									labelKey="label.authoring.advance.allow.students.overall.feedback"
									tooltipKey="label.authoring.advance.allow.students.overall.feedback.tooltip"
									tooltipDescriptionKey="label.authoring.advance.allow.students.overall.feedback.tooltip.description"
						/>
						
						<lams:Switch name="allow-learners"
									 labelKey="label.authoring.advance.allow.students.grades"
									 tooltipKey="label.authoring.advance.allow.students.grades.tooltip"
									 tooltipDescriptionKey="label.authoring.advance.allow.students.grades.tooltip.description"
						/>
						
						<lams:Switch name="enable-justification" checked="true" 
									labelKey="label.authoring.advance.answer.justification"
									tooltipKey="label.authoring.advance.answer.justification.tooltip"
									tooltipDescriptionKey="label.authoring.advance.answer.justification.tooltip.description"
						/>
						
						<lams:Switch name="enable-confidence"
									 labelKey="label.enable.confidence.levels"
									 tooltipKey="label.enable.confidence.levels.tooltip"
									 tooltipDescriptionKey="label.enable.confidence.levels.tooltip.description"
						/>
					</div>
				</lams:Panel>
				
				<lams:Panel id="feedback" titleKey="label.authoring.basic.general.feedback" iconClass="fa-comment-o" colorClass="blue" expanded="false">
					<div class="col-12 col-xl-6 pr-5">
                   		<lams:Dropdown name="questions-per-page" 
                   					   labelKey="label.authoring.advance.questions.per.page"
							 		   tooltipKey="label.authoring.advance.questions.per.page.tooltip"
							 		   tooltipDescriptionKey="label.authoring.advance.questions.per.page.tooltip.description"
						>
                           <option>All</option>
                           <option>All</option>
                           <option>All</option>
                        </lams:Dropdown>
                                    
                        <lams:Switch name="question-numbering" checked="true" 
							 labelKey="label.authoring.advance.numbered.questions"
							 tooltipKey="label.authoring.advance.numbered.questions.tooltip"
							 tooltipDescriptionKey="label.authoring.advance.numbered.questions.tooltip.description"
						/>
						
						<lams:Dropdown name="time-limit" 
                   					   labelKey="label.authoring.advance.time.limit"
							 		   tooltipKey="label.authoring.advance.time.limit.tooltip"
							 		   tooltipDescriptionKey="label.authoring.advance.time.limit.tooltip.description"
						>
                           <option>All</option>
                           <option>All</option>
                           <option>All</option>
                        </lams:Dropdown>
                        		
						<lams:Dropdown name="attempts-allowed" 
                   					   labelKey="label.authoring.advance.attempts.allowed"
							 		   tooltipKey="label.authoring.advance.attempts.allowed.tooltip"
							 		   tooltipDescriptionKey="label.authoring.advance.attempts.allowed.tooltip.description"
						>
                           <option>1</option>
                           <option>1</option>
                           <option>1</option>
                        </lams:Dropdown>
                        
						<lams:Dropdown name="passing-mark" 
                   					   labelKey="label.authoring.advance.passing.mark"
							 		   tooltipKey="label.authoring.advance.passing.mark.tooltip"
							 		   tooltipDescriptionKey="label.authoring.advance.passing.mark.tooltip.description"
						>
                            <option>None</option>
                            <option>None</option>
                            <option>None</option>
                        </lams:Dropdown>
					</div>
					
					<div class="col-12 col-xl-6 pr-4">
						<lams:Switch name="shuffle-questions" checked="true" 
									 labelKey="label.authoring.basic.shuffle.the.choices"
									 tooltipKey="label.authoring.basic.shuffle.the.choices.tooltip"
									 tooltipDescriptionKey="label.authoring.basic.shuffle.the.choices.tooltip.description"
						/>
						
						<lams:Switch name="display-all"
									 labelKey="label.authoring.advance.display.summary"
									 tooltipKey="label.authoring.advance.display.summary.tooltip"
									 tooltipDescriptionKey="label.authoring.advance.display.summary.tooltip.description"
						/>
						
                       <lams:Switch name="display-over-all" checked="true" 
									labelKey="label.authoring.advance.allow.students.overall.feedback"
									tooltipKey="label.authoring.advance.allow.students.overall.feedback.tooltip"
									tooltipDescriptionKey="label.authoring.advance.allow.students.overall.feedback.tooltip.description"
						/>
						
						<lams:Switch name="allow-learners"
									 labelKey="label.authoring.advance.allow.students.grades"
									 tooltipKey="label.authoring.advance.allow.students.grades.tooltip"
									 tooltipDescriptionKey="label.authoring.advance.allow.students.grades.tooltip.description"
						/>
						
						<lams:Switch name="enable-justification" checked="true" 
									labelKey="label.authoring.advance.answer.justification"
									tooltipKey="label.authoring.advance.answer.justification.tooltip"
									tooltipDescriptionKey="label.authoring.advance.answer.justification.tooltip.description"
						/>
						
						<lams:Switch name="enable-confidence"
									 labelKey="label.enable.confidence.levels"
									 tooltipKey="label.enable.confidence.levels.tooltip"
									 tooltipDescriptionKey="label.enable.confidence.levels.tooltip.description"
						/>
					</div>
				</lams:Panel>
				
				<lams:Panel id="outcomes" titleKey="outcome.authoring.title" icon="${lamsUrl}images/components/assess-icon7.svg" colorClass="green2" expanded="false">
				<div class="col-12 col-xl-6 pr-5">
                   		<lams:Dropdown name="questions-per-page" 
                   					   labelKey="label.authoring.advance.questions.per.page"
							 		   tooltipKey="label.authoring.advance.questions.per.page.tooltip"
							 		   tooltipDescriptionKey="label.authoring.advance.questions.per.page.tooltip.description"
						>
                           <option>All</option>
                           <option>All</option>
                           <option>All</option>
                        </lams:Dropdown>
                                    
                        <lams:Switch name="question-numbering" checked="true" 
							 labelKey="label.authoring.advance.numbered.questions"
							 tooltipKey="label.authoring.advance.numbered.questions.tooltip"
							 tooltipDescriptionKey="label.authoring.advance.numbered.questions.tooltip.description"
						/>
						
						<lams:Dropdown name="time-limit" 
                   					   labelKey="label.authoring.advance.time.limit"
							 		   tooltipKey="label.authoring.advance.time.limit.tooltip"
							 		   tooltipDescriptionKey="label.authoring.advance.time.limit.tooltip.description"
						>
                           <option>All</option>
                           <option>All</option>
                           <option>All</option>
                        </lams:Dropdown>
                        		
						<lams:Dropdown name="attempts-allowed" 
                   					   labelKey="label.authoring.advance.attempts.allowed"
							 		   tooltipKey="label.authoring.advance.attempts.allowed.tooltip"
							 		   tooltipDescriptionKey="label.authoring.advance.attempts.allowed.tooltip.description"
						>
                           <option>1</option>
                           <option>1</option>
                           <option>1</option>
                        </lams:Dropdown>
                        
						<lams:Dropdown name="passing-mark" 
                   					   labelKey="label.authoring.advance.passing.mark"
							 		   tooltipKey="label.authoring.advance.passing.mark.tooltip"
							 		   tooltipDescriptionKey="label.authoring.advance.passing.mark.tooltip.description"
						>
                            <option>None</option>
                            <option>None</option>
                            <option>None</option>
                        </lams:Dropdown>
					</div>
					
					<div class="col-12 col-xl-6 pr-4">
						<lams:Switch name="shuffle-questions" checked="true" 
									 labelKey="label.authoring.basic.shuffle.the.choices"
									 tooltipKey="label.authoring.basic.shuffle.the.choices.tooltip"
									 tooltipDescriptionKey="label.authoring.basic.shuffle.the.choices.tooltip.description"
						/>
						
						<lams:Switch name="display-all"
									 labelKey="label.authoring.advance.display.summary"
									 tooltipKey="label.authoring.advance.display.summary.tooltip"
									 tooltipDescriptionKey="label.authoring.advance.display.summary.tooltip.description"
						/>
						
                       <lams:Switch name="display-over-all" checked="true" 
									labelKey="label.authoring.advance.allow.students.overall.feedback"
									tooltipKey="label.authoring.advance.allow.students.overall.feedback.tooltip"
									tooltipDescriptionKey="label.authoring.advance.allow.students.overall.feedback.tooltip.description"
						/>
						
						<lams:Switch name="allow-learners"
									 labelKey="label.authoring.advance.allow.students.grades"
									 tooltipKey="label.authoring.advance.allow.students.grades.tooltip"
									 tooltipDescriptionKey="label.authoring.advance.allow.students.grades.tooltip.description"
						/>
						
						<lams:Switch name="enable-justification" checked="true" 
									labelKey="label.authoring.advance.answer.justification"
									tooltipKey="label.authoring.advance.answer.justification.tooltip"
									tooltipDescriptionKey="label.authoring.advance.answer.justification.tooltip.description"
						/>
						
						<lams:Switch name="enable-confidence"
									 labelKey="label.enable.confidence.levels"
									 tooltipKey="label.enable.confidence.levels.tooltip"
									 tooltipDescriptionKey="label.enable.confidence.levels.tooltip.description"
						/>
					</div>
				</lams:Panel>
				 --%>
				
			</div>
           </div>    
	</form:form>
</lams:PageComponent>
</body>
</lams:html>