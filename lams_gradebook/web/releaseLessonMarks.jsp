<%@ include file="/common/taglibs.jsp"%>
<style>
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
	
	#release-marks-buttons {
		margin-top: 10px;
		margin-bottom: 20px;
		float: right;
	}
</style>

<script type="text/javascript">

	var releaseMarksLessonID = ${param.lessonID};

	jQuery(document).ready(function() {
		
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
		   	rowNum:10,
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
				   $('#release-marks-email-preview')
				   		.show()
				   		.children('#release-marks-email-preview-content')
				   		.load('<lams:LAMSURL/>gradebook/gradebookMonitoring/getReleaseMarksEmailContent.do',{
							'lessonID' : releaseMarksLessonID,
							'userID'   : userID
					   });
			   }
			   return false;
			}
		});
	});

	function sendReleaseMarksEmails(){
		$.ajax({
			'url'      : '<lams:LAMSURL/>gradebook/gradebookMonitoring/sendReleaseMarksEmails.do',
			'data'     : {
				'lessonID' : releaseMarksLessonID
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
	<button type="button" class="btn btn-default" onClick="javascript:sendReleaseMarksEmails()">Send emails to all selected learners</button>
</div>