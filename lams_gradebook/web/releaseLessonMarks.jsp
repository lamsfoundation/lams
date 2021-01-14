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
	
	.release-marks-buttons {
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		align-items: flex-end;
	}
	
	.release-marks-buttons > button {
		width: 70%;
		margin-bottom: 20px;
	}
</style>
	
<script type="text/javascript">

	var releaseMarksLessonID = ${param.lessonID},
		releaseMarksAlertBox = null,
		releaseMarksScheduleDate = '${releaseMarksScheduleDate}';
		

	jQuery(document).ready(function() {
		releaseMarksAlertBox = $('#release-marks-alert');
		$('#release-marks-schedule-date').datetimepicker({
			minDate : 0,
			dateFormat : 'yy-mm-dd'
		}).change(function(){
			var date = $(this).val();
			$('#release-marks-schedule-confirm').prop('disabled', !date || date.trim() == '');
		});
		
		onReleaseMarksOpen();
	});

	function onReleaseMarksOpen(){
		if (releaseMarksScheduleDate) {
			$('#release-marks-schedule-date').val(releaseMarksScheduleDate);
			displayReleaseMarksSchedule();
		} else {
			displayReleaseMarksLearners();
		}
	}

	function onReleaseMarksClose(){
		$('#release-marks-email-preview').hide();
		$('#release-marks-learners-panel').empty();
		releaseMarksAlertBox.hide();
	}
	
	function toggleMarksRelease() {
		if (confirm(marksReleased ? "<fmt:message key="gradebook.monitor.releasemarks.check2"/>" : "<fmt:message key="gradebook.monitor.releasemarks.check"/>")) {
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
		let grid = $("#release-marks-learners-table"),
			includedLearners = grid.data('included'),
			excludedLearners = grid.data('excluded');
		
		releaseMarksAlertBox.hide();
	
		$.ajax({
			'url'      : '<lams:LAMSURL/>gradebook/gradebookMonitoring/sendReleaseMarksEmails.do',
			'data'     : {
				'lessonID' : releaseMarksLessonID,
				 'includedLearners' : includedLearners === null ? null : JSON.stringify(includedLearners),
				 'excludedLearners' : excludedLearners === null ? null : JSON.stringify(excludedLearners)
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
		    rowList:[10,20,30,40,50,100],
		   	rowNum:2,
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
					row = target.closest('tr'),
					isHighlighted = row.hasClass('warning');
			   if (isCheckboxClicked) {
				   return true;
			   }
			   
			   row.siblings('tr.warning').removeClass('warning');
			   if (!isHighlighted) {
				   row.addClass('warning');

				   let userID = row.attr('id');
				   $('#release-marks-email-preview-content')
				   		.load('<lams:LAMSURL/>gradebook/gradebookMonitoring/getReleaseMarksEmailContent.do',{
							'lessonID' : releaseMarksLessonID,
							'userID'   : userID
					   }, function(){
						   $(this).parent().slideDown();
					   });
			   }
			   return false;
			},
		    onSelectRow : function(id, status, event) {
			    var grid = $(this),
			   		included = grid.data('included'),
					excluded = grid.data('excluded'),
					selectAllChecked = grid.closest('.ui-jqgrid-view').find('.jqgh_cbox .cbox').prop('checked');
				if (selectAllChecked) {
					var index = excluded.indexOf(+id);
					// if row is deselected, add it to excluded array
					if (index < 0) {
						if (!status) {
							excluded.push(+id);
						}
					} else if (status) {
						excluded.splice(index, 1);
					}
				} else {
					var index = included.indexOf(+id);
					// if row is selected, add it to included array
					if (index < 0) {
						if (status) {
							included.push(+id);
						}
					} else if (!status) {
						included.splice(index, 1);
					}
				}
			},
			gridComplete : function(){
				let grid = $(this),
						   included = grid.data('included'),
						   // cell containing "(de)select all" button
						   selectAllCell = grid.closest('.ui-jqgrid-view').find('.jqgh_cbox > div');
				// remove the default button provided by jqGrid
				$('.cbox', selectAllCell).remove();
				// create own button which follows own rules
				var selectAllCheckbox = $('<input type="checkbox" class="cbox" />')
										.prop('checked', included === null)
										.prependTo(selectAllCell)
										.change(function(){
											// start with deselecting everyone on current page
											grid.resetSelection();
											if ($(this).prop('checked')){
												// on select all change mode and select all on current page
												grid.data('included', null);
												grid.data('excluded', []);
												$('[role="row"]', grid).each(function(){
													grid.jqGrid('setSelection', +$(this).attr('id'), false);
												});
											} else {
												// on deselect all just change mode
												grid.data('excluded', null);
												grid.data('included', []);
											}
										});
	
				grid.resetSelection();
				if (selectAllCheckbox.prop('checked')) {
					var excluded = grid.data('excluded');
					// go through each loaded row
					$('[role="row"]', grid).each(function(){
						var id = +$(this).attr('id'),
							selected = $(this).hasClass('success');
						// if row is not selected and is not excluded, select it
						if (!selected && (!excluded || !excluded.includes(id))) {
							// select without triggering onSelectRow
							grid.jqGrid('setSelection', id, false);
						}
					}); 
				} else {
					// go through each loaded row
					$('[role="row"]', grid).each(function(){
						var id = +$(this).attr('id'),
							selected = $(this).hasClass('success');
						// if row is not selected and is included, select it
						if (!selected && included.includes(id)) {
							// select without triggering onSelectRow
							grid.jqGrid('setSelection', id, false);
						}
					});
				}

				// empty email preview on grid page change
				$('#release-marks-email-preview').slideUp(function(){
					 $('#release-marks-email-preview-content', this).empty();
				});
			}}).data({'included' : null, 
				 	  'excluded' : []});
	}

	function displayReleaseMarksSchedule(){
		$('#release-marks-learners').hide();
		$('#release-marks-email-preview').hide();
		$('#release-marks-learners-panel').empty();

		$('#release-marks-schedule-date').change();
		$('#release-marks-schedule').show();
	}

	function cancelScheduleReleaseMarks(){
		if (releaseMarksScheduleDate) {
			scheduleReleaseMarks(true);
		} else {
			$('#release-marks-schedule-date').val(null);
			displayReleaseMarksLearners();
		}
	}

	function scheduleReleaseMarks(isCancel){
		var scheduleDate = isCancel ? null : $('#release-marks-schedule-date').val(),
			result = false;
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
						toggleReleaseMarksPanel(true);
						result = true;
					}
		    	}
		});
		
		return result;
	}
</script>

<div id="release-marks-panel" class="panel panel-default panel-body">
	<div id="release-marks-alert" class="alert alert-dismissable"></div>
	
	<div id="release-marks-learners">
		<div class="row">
			<div id="release-marks-learners-panel" class="col-xs-6">
			</div>
			<div class="release-marks-buttons col-xs-6">
					<button type="button" class="btn btn-default" onClick="javascript:sendReleaseMarksEmails()">Send emails to all selected learners</button>
					<button type="button" id="release-marks-schedule-display" class="btn btn-default" onClick="javascript:displayReleaseMarksSchedule()">
						Schedule release marks
					</button>
					
					<button type="button" id="marksNotReleased" onClick="javascript:toggleMarksRelease()" class="btn btn-primary"
						title="<fmt:message key="gradebook.monitor.releasemarks.1" />&nbsp;<fmt:message key="gradebook.monitor.releasemarks.3" />" >
						<i class="fa fa-share-alt "></i>
						<span class="hidden-xs">
							<fmt:message key="gradebook.monitor.releasemarks.1" />&nbsp;<fmt:message key="gradebook.monitor.releasemarks.3" />
						</span>
					</button>
					<button type="button" id="marksReleased" onClick="javascript:toggleMarksRelease()" class="btn btn-primary"
						title="<fmt:message key="gradebook.monitor.releasemarks.2" />&nbsp;<fmt:message key="gradebook.monitor.releasemarks.3" />" >
						<i class="fa fa-share-alt "></i>
						<span class="hidden-xs">
							<fmt:message key="gradebook.monitor.releasemarks.2" />&nbsp;<fmt:message key="gradebook.monitor.releasemarks.3" />
						</span>
					</button> 
			</div>
		</div>
		
		<div id="release-marks-email-preview">
			<h4>Email preview</h4>
			<div id="release-marks-email-preview-content"></div>
		</div>
	</div>
	
	<div id="release-marks-schedule">
		<div class="row">
			<div class="col-xs-6">
				<c:choose>
					<c:when test="${empty releaseMarksScheduleDate}">
						Schedule date: <input type="text" id="release-marks-schedule-date" autocomplete="off" />
						<label>
						<input type="checkbox" id="release-marks-schedule-emails" /> send emails</label>
					</c:when>
					<c:otherwise>
						Release marks are scheduled for ${releaseMarksScheduleDate}.
						<br>Emails will ${releaseMarksSendEmails ? '' : 'not '}be sent.
					</c:otherwise>
				</c:choose>

			</div>
			
			<div class="release-marks-buttons col-xs-6">
				<button type="button" id="release-marks-schedule-cancel" onClick="javascript:cancelScheduleReleaseMarks()" class="btn btn-default"
					title="" >
						Cancel schedule
				</button>
				<c:if test="${empty releaseMarksScheduleDate}">
					<button type="button" id="release-marks-schedule-confirm" onClick="javascript:scheduleReleaseMarks()" class="btn btn-primary"
						title="" >
						<i class="fa fa-share-alt "></i>
						<span class="hidden-xs">
							Confirm release marks schedule date
						</span>
					</button>
				</c:if>
			</div>
		</div>
		
	</div>
</div>