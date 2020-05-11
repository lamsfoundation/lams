<%@ include file="/taglibs.jsp"%>
<style>
	.font-weight-bold {
		font-weight: bold;
	}
	.group-title {
	   	cursor: pointer;
	}
</style>

<!-- ChartJS-->
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/chartjs.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/portrait.js"></script>
<script>
	$(document).ready(function(){
		initializePortraitPopover(LAMS_URL);
	
		//Change leader button handler 
		$('.change-leader-button').click(function () {
			var groupId = $(this).data("group-id");
			var leaderUserId = $('#change-leader-select-' + groupId).val();

	        $.ajax({
	            url: '<lams:LAMSURL/>/tool/lalead11/tblmonitoring/changeLeader.do',
	            data: {
	            	userID: leaderUserId,
	            	toolContentID: "${leaderselectionToolContentId}",
	    			'<csrf:tokenname/>' : '<csrf:tokenvalue/>',
		        },
	            type: 'post',
	            success: function (json) {
		            if (json.isSuccessful) {
		            		alert("<fmt:message key='label.leader.successfully.changed'/>");
						loadTab("teams");
					} else {
						alert("Leader was not changed.");
					}
	            }
	       	});
		});

		//use jqeury toggle instead of bootstrap collapse 
		$(".group-title").on('click', function () {
			var div =  $("#collapse-" + $(this).data("groupid"));
			div.toggleClass("in");
			$(this).toggleClass("collapsed");
		});

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
			var title = groupName + ": iRA vs tRA comparison";

			var userNames = [];
			$(".belong-to-group-" + groupId).each(function() { 
				userNames.push($(this).html().trim().replace("&nbsp;", " "));
			 });
			
			var canvas = $(this).find('.modal-body canvas');
			$(this).find('.modal-title').html(title);
			var ctx = canvas[0].getContext('2d');

			//draw chart
			chart = new Chart(ctx).Bar({
				responsive: true,
				labels: userNames,
				datasets: [
				{
	                label: "iRA",
	                fillColor: "rgba(26,179,148,0.5)",
	                strokeColor: "rgba(26,179,148,0.8)",
	                highlightFill: "rgba(26,179,148,0.75)",
	                highlightStroke: "rgba(26,179,148,1)",
	                data: iraScores
				},
				{
	                label: "tRA",
	                fillColor: "rgba(176,176,176,0.5)",
	                strokeColor: "rgba(176,176,176,0.8)",
	                highlightFill: "rgba(176,176,176,0.75)",
	                highlightStroke: "rgba(176,176,176,1)",              
	                data: traScores
				}]
			}, {});

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
			var userScore = link.data('ira-score');
			$('#ira-user-score').html(userScore);

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
			var userScore = link.data('tra-score');
			$('#tra-user-score').html(userScore);

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

	});
</script>            

<!-- Header -->
<div class="row no-gutter">
	<div class="col-xs-12 col-md-12 col-lg-8">
		<h3>
			<fmt:message key="label.students.teams"/>
		</h3>
	</div>
</div>
<!-- End header -->              

<!-- Tables -->
<div class="row no-gutter">
<div class="col-xs-12 col-md-12 col-lg-12">

	<c:forEach var="groupDto" items="${groupDtos}">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title panel-collapse">
					<a data-toggle="collapse" data-groupid="${groupDto.groupID}" class="collapsed group-title" id="group-name-${groupDto.groupID}">
						${groupDto.groupName}
					</a>
				</h4>
			</div>
	
			<div id="collapse-${groupDto.groupID}" class="panel-collapse collapse in">
				<div class="panel-body">
	
					<div class="table-responsive">
						<table class="table table-striped table-hover table-condensed">
							<thead>
								<tr>
									<th>
										<fmt:message key="label.student"/>
									</th>
									
									<c:if test="${isIraAssessmentAvailable || isIraMcqAvailable}">
										<th class="text-center">
											<fmt:message key="label.ira"/>
										</th>
									</c:if>
									
									<c:if test="${isScratchieAvailable}">
										<th class="text-center">
											<fmt:message key="label.tra"/>
										</th>
									</c:if>               
								</tr>
							</thead>
	
							<tbody>
								<c:forEach var="userDto" items="${groupDto.userList}">
								
									<tr>
										<td class="col-md-7">
											<span id="user-name-${userDto.userID}" class="belong-to-group-${groupDto.groupID} new-popover <c:if test="${userDto.groupLeader}">font-weight-bold</c:if>" 
													data-portrait="${userDto.portraitUuid}" data-fullname="${userDto.lastName},&nbsp;${userDto.firstName}">
												${userDto.lastName},&nbsp;${userDto.firstName} 
											</span>
											<c:if test="${userDto.groupLeader}">
												<abbr title="Leader" class="fa fa-user-plus" style="color:darkorange"></abbr>
											</c:if>
										</td>
										
										<c:if test="${isIraAssessmentAvailable || isIraMcqAvailable}">
											<td class="col-md-2 text-center">
												<c:choose>
													<c:when test="${userDto.iraScore != null}">
														<a data-toggle="modal" href="#ira-modal"
																data-user-id="${userDto.userID}"
																data-ira-score="${userDto.iraScore}">
															${userDto.iraScore}
														</a>													
													</c:when>
													<c:otherwise>
														0
													</c:otherwise>
												</c:choose>
											</td>
										</c:if>
										
										<c:if test="${isScratchieAvailable}">
											<td class="col-md-2 text-center">
												<a data-toggle="modal" href="#tra-modal"
														data-user-id="${userDto.userID}"
														data-tra-score="${groupDto.traScore}">
													${groupDto.traScore}
												</a>
											</td>
										</c:if>
									</tr>
									
								</c:forEach>
							</tbody>
						</table>
					</div>
	
					<!-- Change leader and Compare buttons -->
					<div class="row">
						<div class="col-xs-12 col-md-12 col-lg-12">
							<c:if test="${(isIraAssessmentAvailable || isIraMcqAvailable) && isScratchieAvailable}">
								<button href="#" data-toggle="modal" data-target="#comparison-modal" type="button" class="btn btn-sm btn-default pull-right"
										data-ira-scores="
										<c:forEach var="userDto" items="${groupDto.userList}">
											${userDto.iraScore},
										</c:forEach>" 
										data-tra-scores="${groupDto.traScore}" 
										data-group-id="${groupDto.groupID}"
										data-user-names="">
									<i class="fa fa-bar-chart"></i> 
									<fmt:message key="label.comparison"/>
								</button>
							</c:if>

							<c:if test="${not empty leaderselectionToolContentId}">
								<button data-toggle="modal" href="#change-leader-modal-${groupDto.groupID}" type="button" class="btn btn-sm btn-default pull-right" style="margin-right: 5px">
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
</div>

<!-- modal chart -->
<div class="modal fade" id="comparison-modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
<div class="modal-dialog">
<div class="modal-content">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">
			<span aria-hidden="true">&times;</span>
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
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">ח</button>
		<h4 class="modal-title">
			<fmt:message key="label.ira.responses.for"/>: <span id="ira-user-name-label"></span> 
		</h4>
		<fmt:message key="label.score"/>: <span id="ira-user-score"></span>
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
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">ח</button>
		<h4 class="modal-title">
			<fmt:message key="label.tra.responses.for"/>: <span id="tra-user-name-label"></span> 
		</h4>
		<fmt:message key="label.score"/>: <span id="tra-user-score"></span>
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

<!-- Change leader Modal -->
<c:forEach var="groupDto" items="${groupDtos}">
	<div class="modal fade" id="change-leader-modal-${groupDto.groupID}">
	<div class="modal-dialog modal-lg">
	<div class="modal-content">
	
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">�</button>
			<h4 class="modal-title">
				<fmt:message key="label.change.leader"/>
			</h4>
			<small>
				<fmt:message key="label.note.leader.change"/>
			</small>
		</div>
	          
		<!-- Begin -->
		<div class="modal-body">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">${groupDto.groupName}:</h4> 
					<small>
						<fmt:message key="label.current.leader"/>
						<c:if test="${not empty groupDto.groupLeader}">
							${groupDto.groupLeader.lastName},&nbsp;${groupDto.groupLeader.firstName} 
						</c:if>
					</small>
				</div>
				
				<div class="panel-body">
					<select class="select-picker" id="change-leader-select-${groupDto.groupID}">
						<c:forEach var="userDto" items="${groupDto.userList}">
							<option value="${userDto.userID}">${userDto.lastName},&nbsp;${userDto.firstName}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<!-- end -->  
	              
			<div class="modal-footer">	
				<a href="#" data-dismiss="modal" class="btn btn-default">
					<fmt:message key="button.close"/>
				</a>
				<a href="#" data-dismiss="modal" class="btn btn-default change-leader-button" 
						data-group-id="${groupDto.groupID}">
					<fmt:message key="button.save"/>
				</a>
			</div>
		</div>
	          
	</div>
	</div>
	</div>
</c:forEach>
<!-- End Change leader Modal -->
