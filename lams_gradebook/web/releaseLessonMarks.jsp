<%@ include file="/common/taglibs.jsp"%>

<style>
	#release-marks-alert {
		display: none;
		width: 50%;
		margin: auto;
		margin-bottom: 10px;
		padding: 5px 15px;
	}
	
	#release-marks-learner-table {
		cursor: pointer;
	}

	#release-marks-email-preview {
		display: none;
	}
	
	#release-marks-email-preview > h4 {
		text-align: center;
	}
	
	#release-marks-email-preview-content {
		border-top: thin darkgray solid;
	}
	
	#release-marks-email-preview-content > iframe {
		width: 100%;
		min-height: 480px;
		border: none;
	}
	
	.release-marks-buttons {
		text-align: right;
		margin-bottom: 10px;
	}
	
	#release-marks-schedule-display {
		margin-right: 20px;
	}
	
	#release-marks-schedule-labels > label {
		vertical-align: text-top;
	}
</style>
	
<script type="text/javascript">

	// reset variables which was set in gradebookMonitor.jsp
	marksReleased = ${marksReleased};

	
	var releaseMarksLessonID = ${param.lessonID},
		releaseMarksAlertBox = null,
		// is release marks scheduled?
		releaseMarksScheduleDate = '${releaseMarksScheduleDate}';
		

	jQuery(document).ready(function() {
		// store this frequently used element
		releaseMarksAlertBox = $('#release-marks-alert');
		
		$('#release-marks-schedule-date').datetimepicker({
			// date must be in future
			minDate : 0,
			dateFormat : 'yy-mm-dd'
		}).change(function(){
			// if there is no date selected, disable the confirmation button
			var date = $(this).val();
			$('#release-marks-schedule-confirm').prop('disabled', !date || date.trim() == '');
		});
		
		onReleaseMarksOpen();
	});

	// when this panels gets (re)loaded
	function onReleaseMarksOpen(){
		if (releaseMarksScheduleDate) {
			// release marks are scheduled
			$('#release-marks-schedule-date').val(releaseMarksScheduleDate);
			displayReleaseMarksSchedule();
		} else {
			displayReleaseMarksLearners();
		}
	}

	// release/hide marks
	function toggleMarksRelease() {
		if (confirm(marksReleased ? "<fmt:message key="gradebook.monitor.releasemarks.check.hide"/>" : "<fmt:message key="gradebook.monitor.releasemarks.check.release"/>")) {
			releaseMarksAlertBox.hide();
			
			$.ajax({
				url : "<lams:LAMSURL/>gradebook/gradebookMonitoring/toggleReleaseMarks.do", 
				data : {
				  	"<csrf:tokenname/>":"<csrf:tokenvalue/>",
					lessonID: releaseMarksLessonID
				}, 
				type     : 'post',
				dataType : 'text',
				success : function(response) {
			    	if (response == 'success') {
				    	marksReleased = !marksReleased;
				    	updateReleaseMarksDependantElements();
			    	} else {
						releaseMarksAlertBox.removeClass('alert-success').addClass('alert-danger').text("<fmt:message key="error.releasemarks.fail"/>").show();
			    	}
		    	}
			});
	    }
	}
	
	function sendReleaseMarksEmails(){
		releaseMarksAlertBox.hide();
		
		let grid = $("#release-marks-learners-table"),
			filteredData = grid.jqGrid('getGridParam', 'lastSelectedData'),
			selectedLearners = grid.jqGrid('getGridParam','selarrrow'),
			finalList = [];
		filteredData.forEach(function(learner){
			if (selectedLearners.indexOf(learner.id) >= 0) {
				finalList.push(this.id);
			}
		});

		if (finalList.length == 0) {
			releaseMarksAlertBox.removeClass('alert-success').addClass('alert-danger')
								.text('<fmt:message key="gradebook.monitor.releasemarks.send.emails.no.learners" />').show();
			return;
		}
		
		if (!confirm('<fmt:message key="gradebook.monitor.releasemarks.send.emails.confirm" />'.replace('[COUNT_PLACEHOLDER]', finalList.length))){
			return;
		}
	
		$.ajax({
			'url'      : '<lams:LAMSURL/>gradebook/gradebookMonitoring/sendReleaseMarksEmails.do',
			'data'     : {
				'lessonID' : releaseMarksLessonID,
				 'includedLearners' : JSON.stringify(finalList)
			 },
			'dataType' : 'text',
			'cache'    : false,
			'success' : function(response) {
				if (response == 'success') {
					releaseMarksAlertBox.removeClass('alert-danger').addClass('alert-success').text('Emails were sent').show();
					return;
				}

				releaseMarksAlertBox.removeClass('alert-success').addClass('alert-danger').text('There was a problem with sending emails: ' + response).show();
			}
		});
	}

	function updateReleaseMarksDependantElements(){
		if (marksReleased) {
			$('#marksNotReleased, #padlockLocked, #release-marks-schedule-display').hide();
			$('#marksReleased, #padlockUnlocked').show();
		} else {
			$('#marksReleased, #padlockUnlocked').hide();
			$('#marksNotReleased, #padlockLocked, #release-marks-schedule-display').show();
		}
	}
	
	function displayReleaseMarksLearners() {
		updateReleaseMarksDependantElements();
		releaseMarksAlertBox.hide();
		$('#release-marks-schedule').hide();
		$('#release-marks-learners').show();

		// initialize user list 
		var grid = $('<table id="release-marks-learners-table"></table>').appendTo('#release-marks-learners-panel').jqGrid({
			guiStyle: "bootstrap",
			iconSet: 'fontAwesome',
		   	url: "<lams:LAMSURL/>monitoring/emailNotifications/getUsers.do?searchType=4&lessonID=" + releaseMarksLessonID,
			datatype: "json",
		   	colNames:['<fmt:message key="gradebook.columntitle.learnerName"/>'],
		   	colModel:[
		   		{name:'name',index:'name', sortable: false, sorttype: 'text'}
		   	],
		    rowList:[20, 50],
		   	rowNum: 20,
		   	pager: true,
		   	sortname: 'name',
		   	multiselect: true,
			multiPageSelection : true,
		    sortorder: "asc",
		    loadonce: true,
		    height:'100%',
			autowidth:true,
			beforeSelectRow: function(rowid, e) {
				let target = $(e.target),
					isCheckboxClicked = target.is('input[type=checkbox]'),
					row = target.closest('tr')
			   if (isCheckboxClicked) {
				   return true;
			   }

			   highlightReleaseMarksLearnerRow(row.attr('id'));
			   return false;
			},
			gridComplete : function(){
				let grid = $(this),
					rows = $('[role="row"]:not(.jqgfirstrow)', grid),
				    mode = grid.data('mode'),
				    // cell containing "(de)select all" button
				    selectAllCell = grid.closest('.ui-jqgrid-view').find('.ui-jqgrid-labels .jqgh_cbox > div');
				// remove the default button provided by jqGrid
				$('.cbox', selectAllCell).remove();
				// create own button which follows own rules
				var selectAllCheckbox = $('<input type="checkbox" class="cbox" />')
										.prop('checked', mode == 'all' || mode == 'start')
										.prependTo(selectAllCell)
										.change(function(){
											// start with deselecting
											grid.resetSelection();
											var ids = [];
											if ($(this).prop('checked')){
												grid.data('mode', 'all');
												// on select all change mode and select all
												grid.jqGrid('getGridParam', 'data').forEach(function(row) {
													ids.push(row.id);
													// also select on current page as it is too late for selarrrow to be picked up
													grid.jqGrid("setSelection", row.id, false);
												});
											} else {
												grid.data('mode', 'none');
											}
											grid.jqGrid("setGridParam", { selarrrow: ids }, true);
										});
				
				// initial select all
				if (mode == 'start') {
					selectAllCheckbox.change();
				}
				
				if (rows.length === 0) {
					// empty email preview on grid page change
					$('#release-marks-email-preview').slideUp(function(){
						 $('#release-marks-email-preview-content', this).empty();
					});
				} else {
					// highlight first row
					highlightReleaseMarksLearnerRow(rows[0].id);
				}
			}}).data({'mode' : 'start'})
			   .jqGrid('filterToolbar', {
				       stringResult: true,
				       searchOnEnter: true,
				       defaultSearch: 'cn'
					  });
	}

	function highlightReleaseMarksLearnerRow(userID) {
		let table = $('#release-marks-learners-table'),
			row = $('#' + userID, table),
			isHighlighted = row.hasClass('warning');
		
		   row.siblings('tr.warning').removeClass('warning');
		   if (!isHighlighted) {
			   row.addClass('warning');

			   $('<iframe />').appendTo($('#release-marks-email-preview-content').empty())
								  .attr('src', '<lams:LAMSURL/>gradebook/gradebookMonitoring/getReleaseMarksEmailContent.do?lessonID=' 
	   								         + releaseMarksLessonID + '&userID=' + userID)
	   						  .one('load',	function(){
								   // set learner name in email preview
								   $('#release-marks-email-preview-user').text(row.find('td:nth-child(2)').text());
								   $(this).parent().parent().slideDown();
							   });
		   }
	}

	function displayReleaseMarksSchedule(){
		$('#release-marks-learners').hide();
		$('#release-marks-email-preview').hide();
		$('#release-marks-learners-panel').empty();
		releaseMarksAlertBox.hide();

		// disable/enable schedule confirmation button depending on its content
		$('#release-marks-schedule-date').change();
		$('#release-marks-schedule').show();
	}

	function cancelScheduleReleaseMarks(){
		if (releaseMarksScheduleDate) {
			// cancel schedule and reload panel
			scheduleReleaseMarks(true);
		} else {
			// reset date field and switch to other tab
			$('#release-marks-schedule-date').val(null);
			displayReleaseMarksLearners();
		}
	}

	function scheduleReleaseMarks(isCancel){
		var scheduleDate = isCancel ? null : $('#release-marks-schedule-date').val();
		$.ajax({
				url : "<lams:LAMSURL/>gradebook/gradebookMonitoring/scheduleReleaseMarks.do", 
				data: {
				  	"<csrf:tokenname/>":"<csrf:tokenvalue/>",
					lessonID: releaseMarksLessonID,
					sendEmails : scheduleDate == null ? false : $('#release-marks-schedule-emails').prop('checked'),
					scheduleDate : scheduleDate
				}, 
				type     : 'post',
				dataType : 'text',
				async    : false,
				success: function(response) {
					if (response == 'success') {
						// reload panel; function is defined in gradebookMonitor.jsp
						toggleReleaseMarksPanel(true);
					} else {
						releaseMarksAlertBox.removeClass('alert-success').addClass('alert-danger').text('There was a problem with scheduling marks release: ' + response).show();
					}
		    	}
		});
	}
</script>

<div id="release-marks-panel" class="panel panel-default panel-body">
	<div id="release-marks-alert" class="alert alert-dismissable"></div>
	
	<div id="release-marks-learners">
		<div class="release-marks-buttons">
        	<button type="button" id="release-marks-schedule-display" class="btn btn-sm btn-default" onClick="javascript:displayReleaseMarksSchedule()">
        	    <i class="fa fa-calendar"></i>
                <span class="hidden-xs">
                    <fmt:message key="gradebook.monitor.releasemarks.schedule.button" />
                </span>
        	</button>
              
			<button type="button" class="btn btn-sm btn-default" onClick="javascript:sendReleaseMarksEmails()">
			    <i class="fa fa-bullhorn"></i>
                <span class="hidden-xs">
                    <fmt:message key="gradebook.monitor.releasemarks.send.emails" />
                </span>
            </button>
              
            <button type="button" id="marksNotReleased" onClick="javascript:toggleMarksRelease()" class="btn btn-sm btn-primary"
                title="<fmt:message key="gradebook.monitor.releasemarks.release" />" >
                <i class="fa fa-share-alt "></i>
                <span class="hidden-xs">
                    <fmt:message key="gradebook.monitor.releasemarks.release" />
                </span>
            </button>
            <button type="button" id="marksReleased" onClick="javascript:toggleMarksRelease()" class="btn btn-sm btn-primary"
                title="<fmt:message key="gradebook.monitor.releasemarks.hide" />" >
                <i class="fa fa-share-alt "></i>
                <span class="hidden-xs">
                    <fmt:message key="gradebook.monitor.releasemarks.hide" />
                </span>
            </button>  
		</div>
		<div class="row">
			<div class="col-sm-4 col-xs-12">
				<!-- A dummy element so learners table starts on the same height as email preview -->
				<span>&nbsp;</span>
				
                <div id="release-marks-learners-panel"> </div>
			</div>
			<div class="col-sm-8 col-xs-12">
                <div id="release-marks-email-preview">
					<p style="margin: 0 0 0 0;" class="text-muted text-center"><small><em><fmt:message key="gradebook.monitor.releasemarks.email.preview" />&nbsp;<span id="release-marks-email-preview-user"></span></em></small></p>
                    <div id="release-marks-email-preview-content"></div>
                </div>
            </div>
		</div>

	</div>
	
	<div id="release-marks-schedule">
		<div class="release-marks-buttons">
			<button type="button" id="release-marks-schedule-cancel" onClick="javascript:cancelScheduleReleaseMarks()" class="btn btn-sm btn-default">
				<i class="fa fa-ban"></i>
                <span class="hidden-xs">
                    <fmt:message key="gradebook.monitor.releasemarks.schedule.cancel" />
                </span>
			</button>
			
			<c:if test="${empty releaseMarksScheduleDate}">
				<button type="button" id="release-marks-schedule-confirm" onClick="javascript:scheduleReleaseMarks()" class="btn btn-sm btn-primary"
					title="" >
					<i class="fa fa-calendar"></i>
					<span class="hidden-xs">
						<fmt:message key="gradebook.monitor.releasemarks.schedule.confirm" />
					</span>
				</button>
			</c:if>
		</div>
		<div class="row">
			<div class="col-xs-1"></div>
			<c:choose>
				<c:when test="${empty releaseMarksScheduleDate}">
					<div id="release-marks-schedule-labels" class="col-xs-3">
						<label for="release-marks-schedule-date"><fmt:message key="gradebook.monitor.releasemarks.schedule.date" /></label><br><br>
						<label for="release-marks-schedule-emails"><fmt:message key="gradebook.monitor.releasemarks.schedule.send.emails" /></label>
					</div>
					<div class="col-xs-3">
						<input type="text" id="release-marks-schedule-date" autocomplete="nope" /><br><br>
						<input type="checkbox" id="release-marks-schedule-emails" />
					</div>
				</c:when>
				<c:otherwise>
					<div class="col-xs-6">
						<p>
							<fmt:message key="gradebook.monitor.releasemarks.scheduled.date">
								<fmt:param value="${releaseMarksScheduleDate}" />
							</fmt:message>
							<br>
							<c:choose>
								<c:when test="${releaseMarksSendEmails}">
									<fmt:message key="gradebook.monitor.releasemarks.scheduled.send.emails" />
								</c:when>
								<c:otherwise>
									<fmt:message key="gradebook.monitor.releasemarks.scheduled.not.send.emails" />
								</c:otherwise>
							</c:choose>
						</p>
					</div>
				</c:otherwise>
			</c:choose>
			<div class="col-xs-5"></div>
		</div>
	</div>
</div>
