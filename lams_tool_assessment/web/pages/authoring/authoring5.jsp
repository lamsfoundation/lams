<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.assessment.AssessmentConstants"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="lamsUrl"><lams:LAMSURL/></c:set>

<lams:html>
<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<title><fmt:message key="label.author.title" /></title>
	
	<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap4.min.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome/css/font-awesome.min.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery.tablesorter.theme.bootstrap.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery.tablesorter.custom.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/components-responsive.css">
	
	<script src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>	
	<script src="<lams:LAMSURL/>includes/javascript/bootstrap4.bundle.min.js"></script>
	<script src="<lams:LAMSURL/>includes/javascript/jquery.tablesorter.js"></script>
	<script src="<lams:LAMSURL/>includes/javascript/jquery.tablesorter.pager.js"></script>
	<script>
		$(document).ready(function(){
		  $('[data-toggle="tooltip"]').tooltip();
			// hide child rows
			$('.tablesorter-childRow td').hide();
	
			$(".tablesorter")
				.tablesorter({
					theme : 'bootstrap',
					// this is the default setting
					cssChildRow: "tablesorter-childRow"
				})
				.tablesorterPager({
					container: $("#pager"),
					positionFixed: false
				});
			
			$('.tablesorter').delegate('.toggle', 'click' ,function() {
				$(this).closest('tr').nextUntil('tr:not(.tablesorter-childRow)').find('td').toggle();
				return false;
			});
		});
	</script>
</head>

<body>
<lams:PageComponent titleKey="label.author.title">
	<form:form action="updateContent.do" method="post" modelAttribute="assessmentForm" id="authoringForm">
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>

           <div class="container-fluid" style="max-width: 1600px">
			<div id="content" class="content row">
				
				<lams:Panel id="basic" titleKey="label.authoring.heading.basic" iconClass="fa-file-o" colorClass="green">
					<div class="col-12 p-0">
						<div class="form-group">
                                  <label for="title">Title</label>
                                  <input type="text" id="title" placeholder="Title" class="form-control">
                                  <label for="instructions">Instructions</label>
                                  <textarea type="text" id="instructions" placeholder="Enter instructions" class="form-control"></textarea>
                           </div>
                          </div>    
				</lams:Panel>
					
               	<lams:Panel id="questions" titleKey="label.authoring.basic.question.list.title" iconClass="fa-question-circle-o" colorClass="yellow" expanded="true">
					<div id="questionlist" >
						<table class="table table-bordered user-table-data tablesorter tablesorter-bootstrap tablesorterff73dd4148253 hasFilters tablesorter2fb3f6e97d426" role="grid">
							<colgroup>
								<col width="85">
								<col width="900">
								<col width="220">
								<col width="100">
								<col width="180">
							</colgroup>
							<thead>
								<tr role="row" class="tablesorter-headerRow table-sorter-header">
									<th data-column="0" class="tablesorter-header tablesorter-headerUnSorted" tabindex="0" scope="col" role="columnheader" aria-disabled="false" unselectable="on" aria-sort="none" aria-label="Order #: No sort applied, activate to apply an ascending sort" style="user-select: none;">
										<div class="tablesorter-header-inner">#</div>
									</th>
									<th data-column="1" class="tablesorter-header tablesorter-headerUnSorted" tabindex="0" scope="col" role="columnheader" aria-disabled="false" unselectable="on" aria-sort="none" aria-label="Customer: No sort applied, activate to apply an ascending sort" style="user-select: none;">
										<div class="tablesorter-header-inner">Question</div>
									</th>
									<th data-column="2" class="tablesorter-header tablesorter-headerUnSorted" tabindex="0" scope="col" role="columnheader" aria-disabled="false" unselectable="on" aria-sort="none" aria-label="PO: No sort applied, activate to apply an ascending sort" style="user-select: none;">
										<div class="tablesorter-header-inner"></div>
									</th>
									<th data-column="3" class="tablesorter-header tablesorter-headerUnSorted" tabindex="0" scope="col" role="columnheader" aria-disabled="false" unselectable="on" aria-sort="none" aria-label="Date: No sort applied, activate to apply an ascending sort" style="user-select: none;">
										<div class="tablesorter-header-inner">Mark</div>
									</th>
									<th data-column="4" class="tablesorter-header tablesorter-headerUnSorted" tabindex="0" scope="col" role="columnheader" aria-disabled="false" unselectable="on" aria-sort="none" aria-label="Total: No sort applied, activate to apply an ascending sort" style="user-select: none;">
										<div class="tablesorter-header-inner"></div>
									</th>
								</tr>
							</thead>
							<tbody aria-live="polite" aria-relevant="all">
								<tr role="row" class="tablesorter-hasChildRow table-sorter-top">
									<td><a href="https://mottie.github.io/tablesorter/docs/example-child-rows.html#" class="toggle">1</a></td>
									<td>What is the Capital of Botswana?</td>
									<td>
										<ul class="list-inline">
											<li class="list-inline-item"><span class="badge badge-primary">Multiple Choice</span></li>
											<li class="list-inline-item"><span class="badge badge-primary">V.4</span></li>
										</ul>
									</td>
                                             <td><input type="text" placeholder="1" value="1" aria-label="Points for question: 1" class="form-control"></td>
									<td>
										<ul class="list-inline tools">
											<li class="list-inline-item"><a href="#" aria-label="Answer not required"><i class="fa fa-asterisk" aria-hidden="true"></i></a></li>
											<li class="list-inline-item"><a href="#" aria-label="Edit"><i class="fa fa-pencil" aria-hidden="true"></i></a></li>
											<li class="list-inline-item"><a href="#" aria-label="Remove"><i class="fa fa-times" aria-hidden="true"></i></a></li>
										</ul>
									</td>
								</tr>
								<tr role="row" class="tablesorter-hasChildRow table-sorter-top">
									<td><a href="https://mottie.github.io/tablesorter/docs/example-child-rows.html#" class="toggle">2</a></td>
									<td>What is the Capital of Poland?</td>
									<td>
										<ul class="list-inline">
											<li class="list-inline-item"><span class="badge badge-primary">Multiple Choice</span></li>
											<li class="list-inline-item"><span class="badge badge-primary">V.4</span></li>
										</ul>
									</td>
									<td><input type="text" placeholder="1" value="1" aria-label="Points for question: 1" class="form-control"></td>
									<td>
										<ul class="list-inline tools">
											<li class="list-inline-item"><a href="#" aria-label="Answer not required"><i class="fa fa-asterisk" aria-hidden="true"></i></a></li>
											<li class="list-inline-item"><a href="#" aria-label="Edit"><i class="fa fa-pencil" aria-hidden="true"></i></a></li>
											<li class="list-inline-item"><a href="#" aria-label="Remove"><i class="fa fa-times" aria-hidden="true"></i></a></li>
										</ul>
									</td>
								</tr>
								<tr role="row" class="tablesorter-hasChildRow table-sorter-top">
									<td><a href="https://mottie.github.io/tablesorter/docs/example-child-rows.html#" class="toggle">3</a></td>
									<td>What is the Capital of Pakistan?</td>
									<td>
										<ul class="list-inline">
											<li class="list-inline-item"><span class="badge badge-primary">Multiple Choice</span></li>
											<li class="list-inline-item"><span class="badge badge-primary">V.4</span></li>
										</ul>
									</td>
									<td><input type="text" placeholder="1" value="1" aria-label="Points for question: 1" class="form-control"></td>
									<td>
										<ul class="list-inline tools">
											<li class="list-inline-item"><a href="#" aria-label="Answer not required"><i class="fa fa-asterisk" aria-hidden="true"></i></a></li>
											<li class="list-inline-item"><a href="#" aria-label="Edit"><i class="fa fa-pencil" aria-hidden="true"></i></a></li>
											<li class="list-inline-item"><a href="#" aria-label="Remove"><i class="fa fa-times" aria-hidden="true"></i></a></li>
										</ul>
									</td>
								</tr>
							</tbody>
						</table>
						<ul class="list-inline bottom">
							<li class="list-inline-item">
								<select class="form-control form-control-select" aria-label="Options for gate">
									<option>Multiple choice</option>
									<option>Multiple choice</option>
									<option>Multiple choice</option>
								</select>
							</li>
							<li class="list-inline-item">
								<button  aria-label="Add" class="btn btn-primary"><i class="fa fa-plus-circle" aria-hidden="true"></i></button>
							</li>
							<li class="list-inline-item">
								<button class="btn btn-primary">Import QTI</button>
							</li>
						</ul>
					</div>
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
			</div>
           </div>    
	</form:form>
</lams:PageComponent>
</body>
</lams:html>