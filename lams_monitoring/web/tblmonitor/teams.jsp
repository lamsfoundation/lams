<%@ include file="/taglibs.jsp"%>
<style>

	#tbl-teams-tab-title {
		text-align: center;
		margin: 23px 0 0 100px;
	}
	
	.font-weight-bold {
		font-weight: bold;
	}
	
	.group-title {
	   	cursor: pointer;
	}
	
	.fa-arrow-circle-down {
		color: #d9534f;
	}
	
	.fa-arrow-circle-up {
		color: #5cb85c;
	}
	
	.fa-arrow-circle-left {
		visibility: hidden;
	}
</style>

<!-- ChartJS-->
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/chart.bundle.min.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/portrait.js"></script>
<script>
	$(document).ready(function(){
		initializePortraitPopover(LAMS_URL);

		//use jqeury toggle instead of bootstrap collapse 
		$(".group-title").on('click', function () {
			var div =  $("#collapse-" + $(this).data("groupid"));
			div.toggleClass("in");
			$(this).toggleClass("collapsed");
		});

		<c:if test="${not empty chartNamesDataset}">
			// summary chart
			var summaryChartIraDataset = JSON.parse('${chartIraDataset}'),
				summaryChartTraDataset = JSON.parse('${chartTraDataset}'),
				summaryChartNamesDataset = JSON.parse('${chartNamesDataset}'),
				ctx = $('#summary-chart')[0].getContext('2d'),
				summaryChart = new Chart(ctx, {
					type : 'bar',
					data : {
						datasets : [ {
							label: "<fmt:message key='label.ira' />",
							data : summaryChartIraDataset,
							backgroundColor : 'rgba(255, 195, 55, 1)'
											  
						},
						{
							label: "<fmt:message key='label.tra' />",
							data : summaryChartTraDataset,
							backgroundColor : 'rgba(5, 204, 214, 1)'
											  
						}],
						labels : summaryChartNamesDataset
					},
					options : {
						legend : {
							display : false
						},
						animation : {
							duration : 0
						},
						scales : {
							yAxes : [
								{
								    ticks : {
										beginAtZero   : true
									},
									scaleLabel : {
										display : true,
										labelString : "<fmt:message key='label.ira.tra.correct.count'/>",
										fontSize : 15
									}
								}
							]
						}
					}
				});
		</c:if>
		

		//Comparison button modal window
		var chart;
		$('#comparison-modal').on('shown.bs.modal', function (event) {
			
			var link = $(event.relatedTarget);
			var iraScores = link.data('ira-scores').split(',');
			for (var i = 0; i < iraScores.length; i++) {
				iraScores[i] = iraScores[i].trim();
			}

			//construct traScores Array
			var traScore = link.data('tra-scores');
			var traScores = new Array(iraScores.length);
			for (var i=0; i<iraScores.length; i++) {
				traScores[i] = traScore;
			}

			//titles
			var groupId = link.data('group-id');
			var groupName = $("#group-name-" + groupId).html();
			var title = groupName + ": <fmt:message key='label.ira.tra.correct.count'/>";

			var userNames = [];
			$(".belong-to-group-" + groupId).each(function() { 
				userNames.push($(this).html().trim().replace("&nbsp;", " "));
			 });
			
			var canvas = $(this).find('.modal-body canvas');
			$(this).find('.modal-title').html(title);

			var ctx = canvas[0].getContext('2d');

			chart = new Chart(ctx, {
				type : 'bar',
				data : {
					datasets : [ {
						label: "iRAT",
						data : iraScores,
						backgroundColor : 'rgba(255, 195, 55, 1)'
										  
					},
					{
						label: "tRAT",
						data : traScores,
						backgroundColor : 'rgba(5, 204, 214, 1)'
										  
					}],
					labels : userNames
				},
				options : {
					legend : {
						display : false
					},
					animation : {
						duration : 1000
					},
					scales : {
						yAxes : [
							{
							    ticks : {
									beginAtZero   : true
								}
							}
						]
					}
				}
			});
		//on hiding chart modal window
		}).on('hidden.bs.modal', function (event) {
			var canvas = $(this).find('.modal-body canvas');
			canvas.attr('width', '568px');
			canvas.attr('height', '300px');
			chart.destroy();
			$(this).data('bs.modal', null);
		});

		//ira button modal window
		$('#ira-modal').on('shown.bs.modal', function (event) {

			var link = $(event.relatedTarget);
			var userId = link.data('user-id');

			//populate user name
			var userName = $("#user-name-" + userId).html();
			$('#ira-user-name-label').html(userName);

			//populate user score
			var userScore = link.data('ira-correct-answer-count');
			$('#ira-correct-answer-count').html(userScore);

			//load modal dialog content using Ajax
			var url = "${isIraMcqAvailable}" == "true" ? "<lams:LAMSURL/>tool/lamc11/tblmonitoring/getModalDialogForTeamsTab.do" : "<lams:LAMSURL/>tool/laasse10/tblmonitoring/getModalDialogForTeamsTab.do";
			$('#ira-modal .modal-body').load(
				url, 
				{
					toolContentID: "${iraToolContentId}",
					userID: userId
				},
				function( response, status, xhr ) {
					if ( status == "error" ) {
						alert("Unable to load iRA modal dialog: " + xhr.status);
					} 
				}
			);
		});

		//tra button modal window
		$('#tra-modal').on('shown.bs.modal', function (event) {

			var link = $(event.relatedTarget);
			var userId = link.data('user-id');

			//Populate real user name
			var userName = $("#user-name-" + userId).html();
			$('#tra-user-name-label').html(userName);

			//populate user score
			var userScore = link.data('tra-correct-answer-count');
			$('#tra-correct-answer-count').html(userScore);

			//load modal dialog content using Ajax
			$('#tra-modal .modal-body').load(
				"<lams:LAMSURL/>tool/lascrt11/tblmonitoring/getModalDialogForTeamsTab.do", {
					toolContentID: "${traToolContentId}",
					userID: userId
				},
				function( response, status, xhr ) {
					if ( status == "error" ) {
						alert("Unable to load tRA modal dialog: " + xhr.status);
					} 
				}
			);
		});

		$('[data-toggle="tooltip"]').tooltip();
	});

	function showChangeLeaderModal(groupID) {
		$('#change-leader-modals').empty()
		.load('<lams:LAMSURL/>tool/lalead11/monitoring/displayChangeLeaderForGroupDialog.do',{
			leaderSelectionToolContentId : '${leaderSelectionToolContentId}',
			groupId : groupID
		});
	}

	function onChangeLeaderCallback(response){
        if (response.isSuccessful) {
        	alert("<fmt:message key='label.leader.successfully.changed'/>");
			loadTab("teams");
		} else {
			alert("Leader was not changed.");
		}
	}
</script>            



<!-- Tables -->
<div class="row no-gutter">
<div class="col-xs-2"></div>
<div class="col-xs-8">

	<c:if test="${not empty groupsSetupUrl}">
		<div onClick="javascript:openPopUp('<lams:LAMSURL/>${groupsSetupUrl}','GroupsSetup', 800, 1280, true)" type="button"
			    class="btn btn-default pull-right voffset20">
			<fmt:message key="label.teams.setup"/>
		</div>
	</c:if>
	
	<h3 id="tbl-teams-tab-title">
		<fmt:message key="label.students.teams"/>
	</h3>
	
	<c:if test="${not empty chartNamesDataset}">
		<canvas id="summary-chart" class="voffset20"></canvas>
	</c:if>
	
	<div class="panel panel-default voffset20">
		<div class="panel-heading">
			<h4 class="panel-title panel-collapse">
				<a data-toggle="collapse" data-groupid="0" class="collapsed group-title">
					<fmt:message key="label.ira.tra.summary"/>
				</a>
			</h4>
		</div>

		<div id="collapse-0" class="panel-collapse collapse in">
			<div class="panel-body">
				
				
					<div class="table-responsive">
						<table class="table table-striped table-hover table-condensed">
							<thead>
								<tr>
									<th style="width: 30%">
										<fmt:message key="label.teams"/>
									</th>
									
									<c:if test="${isIraAssessmentAvailable || isIraMcqAvailable}">
										<th class="text-center">
											<fmt:message key="label.ira.correct.count.average"/>
										</th>
									</c:if>
									
									<c:if test="${isScratchieAvailable}">
										<th class="text-center">
											<fmt:message key="label.tra.correct.count"/>
										</th>
										
										<c:if test="${isIraAssessmentAvailable || isIraMcqAvailable}">
											<th class="text-center">
												<fmt:message key="label.ira.tra.delta"/>
											</th>
										</c:if>
									</c:if>
								</tr>
							</thead>
	
							<tbody>
								<c:forEach var="groupDto" items="${groupDtos}">
								
									<tr>
										<td>
											<a href="#teams-panel-${groupDto.groupID}"><c:out value="${groupDto.groupName}" /></a>
										</td>
										
										<c:if test="${isIraAssessmentAvailable || isIraMcqAvailable}">
											<td class="text-center">
												<c:choose>
													<c:when test="${empty groupDto.iraCorrectAnswerCountAverage}">
														-
													</c:when>
													<c:otherwise>
														<fmt:formatNumber type="number" minFractionDigits="0" maxFractionDigits="2" value="${groupDto.iraCorrectAnswerCountAverage}" />
														<c:choose>
															<c:when test="${not empty highestIraCorrectAnswerCountAverage && groupDto.iraCorrectAnswerCountAverage >= highestIraCorrectAnswerCountAverage}">
																&nbsp;<i class="fa fa-arrow-circle-up" data-toggle="tooltip" title="<fmt:message key="label.ira.tra.highest"/>"></i>
															</c:when>
															<c:when test="${not empty lowestIraCorrectAnswerCountAverage && groupDto.iraCorrectAnswerCountAverage <= lowestIraCorrectAnswerCountAverage}">
																&nbsp;<i class="fa fa-arrow-circle-down" data-toggle="tooltip" title="<fmt:message key="label.ira.tra.lowest"/>"></i>
															</c:when>
															<c:otherwise>
																&nbsp;<i class="fa fa-arrow-circle-left"></i>
															</c:otherwise>
														</c:choose>		
													</c:otherwise>
												</c:choose>
											</td>
										</c:if>
										
										<c:if test="${isScratchieAvailable}">
											
											<td class="text-center">
												<c:choose>
													<c:when test="${empty groupDto.traCorrectAnswerCount}">
														-
													</c:when>
													<c:otherwise>
														${groupDto.traCorrectAnswerCount}
														<c:choose>
															<c:when test="${not empty highestTraCorrectAnswerCount && groupDto.traCorrectAnswerCount >= highestTraCorrectAnswerCount}">
																&nbsp;<i class="fa fa-arrow-circle-up" data-toggle="tooltip" title="<fmt:message key="label.ira.tra.highest"/>"></i>
															</c:when>
															<c:when test="${not empty lowestTraCorrectAnswerCount && groupDto.traCorrectAnswerCount <= lowestTraCorrectAnswerCount}">
																&nbsp;<i class="fa fa-arrow-circle-down" data-toggle="tooltip" title="<fmt:message key="label.ira.tra.lowest"/>"></i>
															</c:when>
															<c:otherwise>
																&nbsp;<i class="fa fa-arrow-circle-left"></i>
															</c:otherwise>
														</c:choose>		
													</c:otherwise>
												</c:choose>
											</td>
											
											<c:if test="${isIraAssessmentAvailable || isIraMcqAvailable}">
												<td class="text-center">
													<c:choose>
														<c:when test="${empty groupDto.correctAnswerCountPercentDelta}">
															-
														</c:when>
														<c:otherwise>
															${groupDto.correctAnswerCountPercentDelta}%
															
															<c:choose>					 
																<c:when test="${not empty highestCorrectAnswerCountDelta && groupDto.correctAnswerCountPercentDelta >= highestCorrectAnswerCountDelta}">
																	&nbsp;<i class="fa fa-arrow-circle-up" data-toggle="tooltip" title="<fmt:message key="label.ira.tra.highest"/>"></i>
																</c:when>
																<c:when test="${not empty lowestCorrectAnswerCountDelta && groupDto.correctAnswerCountPercentDelta <= lowestCorrectAnswerCountDelta}">
																	&nbsp;<i class="fa fa-arrow-circle-down" data-toggle="tooltip" title="<fmt:message key="label.ira.tra.lowest"/>"></i>
																</c:when>
																<c:otherwise>
																	&nbsp;<i class="fa fa-arrow-circle-left"></i>
																</c:otherwise>
															</c:choose>	
														</c:otherwise>
													</c:choose>
												</td>
											</c:if>
										</c:if>
									</tr>
								</c:forEach>
								
								<c:if test="${not empty averageIraCorrectAnswerCountAverage or not empty averageTraCorrectAnswerCount}">
									<tr>
										<th><fmt:message key="label.ira.tra.average"/></th>
										
										<c:if test="${isIraAssessmentAvailable || isIraMcqAvailable}">
											<td class="text-center">
												<c:choose>
													<c:when test="${empty averageIraCorrectAnswerCountAverage}">
														-
													</c:when>
													<c:otherwise>
														<fmt:formatNumber type="number" minFractionDigits="0" maxFractionDigits="2" value="${averageIraCorrectAnswerCountAverage}" />
													</c:otherwise>
												</c:choose>
											</td>
										</c:if>
										
										<c:if test="${isScratchieAvailable}">
											<td class="text-center">
												<c:choose>
													<c:when test="${empty averageTraCorrectAnswerCount}">
														-
													</c:when>
													<c:otherwise>
														<fmt:formatNumber type="number" minFractionDigits="0" maxFractionDigits="2" value="${averageTraCorrectAnswerCount}" />
													</c:otherwise>
												</c:choose>
											</td>
											
											<c:if test="${isIraAssessmentAvailable || isIraMcqAvailable}">
												<td class="text-center">
													<c:choose>
														<c:when test="${empty averageCorrectAnswerCountDelta}">
															-
														</c:when>
														<c:otherwise>
															<fmt:formatNumber type="number" minFractionDigits="0" maxFractionDigits="2" value="${averageCorrectAnswerCountDelta}" />%
														</c:otherwise>
													</c:choose>
												</td>
											</c:if>
										</c:if>
									</tr>
								</c:if>
							</tbody>
						</table>
					</div>
			</div>
		</div>
	</div>

	<c:forEach var="groupDto" items="${groupDtos}">
		<div class="panel panel-default" id="teams-panel-${groupDto.groupID}">
			<div class="panel-heading">
				<h4 class="panel-title panel-collapse">
					<a data-toggle="collapse" data-groupid="${groupDto.groupID}" class="collapsed group-title" id="group-name-${groupDto.groupID}">
						<c:out value="${groupDto.groupName}" />
					</a>
				</h4>
			</div>
	
			<div id="collapse-${groupDto.groupID}" class="panel-collapse collapse in">
				<div class="panel-body">
					<c:if test="${isScratchieAvailable}">
						<c:choose>
							<c:when test="${empty groupDto.traCorrectAnswerCount}">
								-
							</c:when>
							<c:when test="${empty groupDto.groupLeader}">
								<h4><fmt:message key="label.tra.correct.count"/>: ${groupDto.traCorrectAnswerCount}</h4>
							</c:when>
							<c:otherwise>
								<a data-toggle="modal" href="#tra-modal"
								   data-user-id="${groupDto.groupLeader.userID}"
								   data-tra-correct-answer-count="${groupDto.traCorrectAnswerCount}">
								   <h4><fmt:message key="label.tra.correct.count"/>:
										${groupDto.traCorrectAnswerCount}
								   </h4>
								</a>
							</c:otherwise>
						</c:choose>
					</c:if>
					
					<c:if test="${isIraAssessmentAvailable || isIraMcqAvailable}">
						<div class="table-responsive">
							<table class="table table-striped table-hover table-condensed">
								<thead>
									<tr>
										<th>
											<fmt:message key="label.student"/>
										</th>
										
										<th class="text-center">
											<fmt:message key="label.ira.correct.count"/>
										</th>
										
										<c:if test="${isScratchieAvailable}">
																					
											<th class="text-center">
												<fmt:message key="label.ira.tra.delta"/>
											</th>
										</c:if>
									</tr>
								</thead>
		
								<tbody>
									<c:set var="iraHighestCorrectAnswerCount" value="${groupDto.iraHighestCorrectAnswerCount}" />
									<c:set var="iraLowestCorrectAnswerCount" value="${groupDto.iraLowestCorrectAnswerCount}" />
									<c:set var="highestCorrectAnswerCountPercentDelta" value="${groupDto.highestCorrectAnswerCountPercentDelta}" />
									<c:set var="lowestCorrectAnswerCountPercentDelta" value="${groupDto.lowestCorrectAnswerCountPercentDelta}" />
									
									<c:forEach var="userDto" items="${groupDto.userList}">
									
										<tr>
											<td class="">
												<c:choose>
													<c:when test="${empty userDto.iraCorrectAnswerCount}">
														<c:out value="${userDto.lastName}" />,&nbsp;<c:out value="${userDto.firstName}" />
													</c:when>
													<c:otherwise>
														<a data-toggle="modal" href="#ira-modal"
														   data-user-id="${userDto.userID}"
														   data-ira-correct-answer-count="${userDto.iraCorrectAnswerCount}">
															<span id="user-name-${userDto.userID}" class="belong-to-group-${groupDto.groupID} new-popover <c:if test="${userDto.groupLeader}">font-weight-bold</c:if>" 
																	data-portrait="${userDto.portraitUuid}" data-fullname="${userDto.lastName},&nbsp;${userDto.firstName}">
																<c:out value="${userDto.lastName}" />,&nbsp;<c:out value="${userDto.firstName}" />
															</span>
														</a>
													</c:otherwise>
												</c:choose>
												<c:if test="${userDto.groupLeader}">
													<abbr title="Leader" class="fa fa-user-plus" style="color:darkorange"></abbr>
												</c:if>
											</td>
											
											<td class="text-center">
												<c:choose>
													<c:when test="${empty userDto.iraCorrectAnswerCount}">
														-													
													</c:when>
													<c:otherwise>
														<a data-toggle="modal" href="#ira-modal"
																data-user-id="${userDto.userID}"
																data-ira-correct-answer-count="${userDto.iraCorrectAnswerCount}">
															${userDto.iraCorrectAnswerCount}
														</a>
														
														<c:if test="${fn:length(groupDto.userList) > 1}">
															<c:choose>
																<c:when test="${not empty iraHighestCorrectAnswerCount && userDto.iraCorrectAnswerCount >= iraHighestCorrectAnswerCount}">
																	&nbsp;<i class="fa fa-arrow-circle-up" data-toggle="tooltip" title="<fmt:message key="label.ira.tra.highest"/>"></i>
																</c:when>
																<c:when test="${not empty iraLowestCorrectAnswerCount && userDto.iraCorrectAnswerCount <= iraLowestCorrectAnswerCount}">
																	&nbsp;<i class="fa fa-arrow-circle-down" data-toggle="tooltip" title="<fmt:message key="label.ira.tra.lowest"/>"></i>
																</c:when>
																<c:otherwise>
																	&nbsp;<i class="fa fa-arrow-circle-left"></i>
																</c:otherwise>
															</c:choose>		
														</c:if>
													</c:otherwise>
												</c:choose>
											</td>
											
											
											<c:if test="${isScratchieAvailable}">
																						
												<td class="text-center">
													<c:choose>
														<c:when test="${empty userDto.correctAnswerCountPercentDelta}">
															-													
														</c:when>
														<c:otherwise>
															${userDto.correctAnswerCountPercentDelta}%
															
															<c:if test="${fn:length(groupDto.userList) > 1}">
																<c:choose>
																	<c:when test="${not empty highestCorrectAnswerCountPercentDelta && userDto.correctAnswerCountPercentDelta >= highestCorrectAnswerCountPercentDelta}">
																		&nbsp;<i class="fa fa-arrow-circle-up" data-toggle="tooltip" title="<fmt:message key="label.ira.tra.highest"/>"></i>
																	</c:when>
																	<c:when test="${not empty lowestCorrectAnswerCountPercentDelta && userDto.correctAnswerCountPercentDelta <= lowestCorrectAnswerCountPercentDelta}">
																		&nbsp;<i class="fa fa-arrow-circle-down" data-toggle="tooltip" title="<fmt:message key="label.ira.tra.lowest"/>"></i>
																	</c:when>
																	<c:otherwise>
																		&nbsp;<i class="fa fa-arrow-circle-left"></i>
																	</c:otherwise>
																</c:choose>		
															</c:if>
														</c:otherwise>
													</c:choose>
												</td>
											</c:if>
										</tr>
									</c:forEach>
									
									<c:if test="${fn:length(groupDto.userList) > 1}">
										<tr>
											<th><fmt:message key="label.ira.tra.average"/></th>
											
											<td class="text-center">
												<fmt:formatNumber type="number" minFractionDigits="0" maxFractionDigits="2" value="${groupDto.iraCorrectAnswerCountAverage}" />
											</td>
											
											<td class="text-center">
												<fmt:formatNumber type="number" minFractionDigits="0" maxFractionDigits="2" value="${groupDto.correctAnswerCountPercentDeltaAverage}" />%
											</td>
										</tr>
									</c:if>
								</tbody>
							</table>
						</div>
					</c:if>	
					
					<!-- Change leader and Compare buttons -->
					<div class="row">
						<div class="col-xs-12 col-md-12 col-lg-12">
							<c:if test="${(isIraAssessmentAvailable || isIraMcqAvailable) && isScratchieAvailable && not empty groupDto.userList && not empty groupDto.traCorrectAnswerCount}">
								<button href="#" data-toggle="modal" data-target="#comparison-modal" type="button" class="btn btn-sm btn-default pull-right"
										data-ira-scores="
										<c:forEach var="userDto" items="${groupDto.userList}">
											<c:if test="${userDto.iraCorrectAnswerCount != null}">
												${userDto.iraCorrectAnswerCount},
											</c:if>
										</c:forEach>" 
										data-tra-scores="${groupDto.traCorrectAnswerCount}" 
										data-group-id="${groupDto.groupID}"
										data-user-names="">
									<i class="fa fa-bar-chart"></i> 
									<fmt:message key="label.comparison"/>
								</button>
							</c:if>

							<c:if test="${not empty leaderselectionToolActivityId and not empty groupDto.userList}">
								<button onClick="javascript:showChangeLeaderModal(${groupDto.groupID})" type="button"
									    class="btn btn-sm btn-default pull-right" style="margin-right: 5px">
									<fmt:message key="label.change.leader"/>
								</button>
							</c:if>
						</div>
					</div>
					<!-- End change leader -->                       
	
				</div>
	
			</div>
		</div>
	</c:forEach>     

</div>
<div class="col-xs-2"></div>
</div>

<!-- modal chart -->
<div class="modal fade" id="comparison-modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
<div class="modal-dialog">
<div class="modal-content">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">
			<span aria-hidden="true"><i class="fa fa-close"></i></span>
			<span class="sr-only">
				<fmt:message key="button.close"/>
			</span>
		</button>
		<h4 class="modal-title" id="exampleModalLabel">
			<fmt:message key="label.bar.chart"/>
		</h4>
	</div>
          
	<div class="modal-body">
		<canvas id="canvas" width="568" height="300"></canvas>
	</div>
</div>
</div>
</div>
<!-- end modal chart -->

<!-- iRA Modal -->
<div class="modal fade" id="ira-modal">
<div class="modal-dialog modal-lg">
<div class="modal-content">

	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-close"></i></button>
		<h4 class="modal-title">
			<fmt:message key="label.ira.responses.for"/>: <span id="ira-user-name-label"></span> 
		</h4>
		<fmt:message key="label.ira.correct.count"/>: <span id="ira-correct-answer-count"></span>
	</div>
          
	<!-- Begin -->
	<div class="modal-body">
	</div>
	<!-- end -->  
	
	<div class="modal-footer">	
		<a href="#" data-dismiss="modal" class="btn btn-default">
			<fmt:message key="button.ok"/>
		</a>
	</div>

</div>
</div>
</div>
<!-- End iRA Modal -->

<!-- tRA Modal -->
<div class="modal fade" id="tra-modal">
<div class="modal-dialog modal-lg">
<div class="modal-content">

	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-close"></i></button>
		<h4 class="modal-title">
			<fmt:message key="label.tra.responses.for"/>: <span id="tra-user-name-label"></span> 
		</h4>
		<fmt:message key="label.tra.correct.count"/>: <span id="tra-correct-answer-count"></span>
	</div>
          
	<!-- Begin -->
	<div class="modal-body">
	</div>
	<!-- end -->  
	
	<div class="modal-footer">	
		<a href="#" data-dismiss="modal" class="btn btn-default">
			<fmt:message key="button.ok"/>
		</a>
	</div>

</div>
</div>
</div>
<!-- End tRA Modal -->

<!-- container for Change Leader modals -->
<div id="change-leader-modals">
</div>