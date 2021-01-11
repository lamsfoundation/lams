<%@ include file="/common/taglibs.jsp"%>
<style>
	#release-marks-learner-list {
		cursor: pointer;
	}

	#gbox_release-marks-learner-list {
		max-width: 600px;
		margin: auto;
	}
	
	#release-marks-email-preview {
		display: none;
	}
	
	#release-marks-email-preview > h4 {
		text-align: center;
	}
	
	#release-marks-email-preview-content {
		border-top: thin darkgray solid;
		border-bottom: thin darkgray solid;
		padding-bottom: 10px;
	}
	
	#release-marks-buttons > button {
		margin-top: 10px;
		margin-bottom: 20px;
	}
</style>

<script type="text/javascript">

	var releaseMarksLessonID = ${param.lessonID};

	jQuery(document).ready(function() {
		displayMarksReleaseOption();
		
		//initialize user list 
		var grid = jQuery("#release-marks-learner-list").jqGrid({
			guiStyle: "bootstrap",
			iconSet: 'fontAwesome',
		   	url: "<lams:LAMSURL/>monitoring/emailNotifications/getUsers.do?searchType=4&lessonID=" + releaseMarksLessonID,
			datatype: "json",
		   	colNames:['<fmt:message key="gradebook.columntitle.learnerName"/>'],
		   	colModel:[
		   		{name:'name',index:'name', sortable: false, sorttype: 'text'}
		   	],
		    rowList:[10,20,30,40,50,100],
		   	rowNum:1,
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
	});
	
	function toggleMarksRelease() {
		if (confirm(marksReleased ? "<fmt:message key="gradebook.monitor.releasemarks.check2"/>" : "<fmt:message key="gradebook.monitor.releasemarks.check"/>")) {
			$.post(
				"<lams:LAMSURL/>gradebook/gradebookMonitoring/toggleReleaseMarks.do", 
				{
				  	"<csrf:tokenname/>":"<csrf:tokenvalue/>",
					lessonID: releaseMarksLessonID
				}, 
				function(xml) {
					var str = new String(xml)
			    	if (str.indexOf("success") != -1) {
				    	marksReleased = !marksReleased;
		    			displayMarksReleaseOption();
		    			
			    	} else {
			    		alert("<fmt:message key="error.releasemarks.fail"/>");
			    	}
		    	}
		    );
	    }
	}
	
	function displayMarksReleaseOption() {
		if (marksReleased) {
			$('#marksNotReleased, #padlockLocked').hide();
			$('#marksReleased, #padlockUnlocked').show();
		} else {
			$('#marksReleased, #padlockUnlocked').hide();
			$('#marksNotReleased, #padlockLocked').show();
		}
	}
	
	function sendReleaseMarksEmails(){
		let grid = $("#release-marks-learner-list"),
			includedLearners = grid.data('included'),
			excludedLearners = grid.data('excluded');
	
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
					alert('Emails were sent');
					return;
				}

				alert('There was a problem with sending emails: ' + response);
			}
		});
	}
</script>

<table id="release-marks-learner-list"></table>
<div id="release-marks-email-preview">
	<h4>Email preview</h4>
	<div id="release-marks-email-preview-content"></div>
</div>
<div id="release-marks-buttons">
	<button type="button" class="btn btn-default pull-left" onClick="javascript:sendReleaseMarksEmails()">Send emails to all selected learners</button>
			
	<button type="button" id="marksNotReleased" onClick="javascript:toggleMarksRelease()" class="btn btn-primary pull-right"
		title="<fmt:message key="gradebook.monitor.releasemarks.1" />&nbsp;<fmt:message key="gradebook.monitor.releasemarks.3" />" >
		<i class="fa fa-share-alt "></i>
		<span class="hidden-xs">
			<fmt:message key="gradebook.monitor.releasemarks.1" />&nbsp;<fmt:message key="gradebook.monitor.releasemarks.3" />
		</span>
	</button>
	<button type="button" id="marksReleased" onClick="javascript:toggleMarksRelease()" class="btn btn-primary pull-right"
		title="<fmt:message key="gradebook.monitor.releasemarks.2" />&nbsp;<fmt:message key="gradebook.monitor.releasemarks.3" />" >
		<i class="fa fa-share-alt "></i>
		<span class="hidden-xs">
			<fmt:message key="gradebook.monitor.releasemarks.2" />&nbsp;<fmt:message key="gradebook.monitor.releasemarks.3" />
		</span>
	</button> 
</div>