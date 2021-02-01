<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="sessionDtos" value="${sessionMap.sessionDtos}"/>
<c:set var="assessment" value="${sessionMap.assessment}"/>

<script type="text/javascript">
	var activityCompletionChart = null,
		answeredQuestionsChart = null;
	// how often completion charts will be updated
	const COMPLETION_CHART_UPDATE_INTERVAL = 10 * 1000;

	$(document).ready(function(){
		
		initializePortraitPopover("<lams:LAMSURL />");
		
		<c:forEach var="sessionDto" items="${sessionDtos}" varStatus="status">
		
			jQuery("#list${sessionDto.sessionId}").jqGrid({
			   	multiselect: false,
				datatype: "json",
				url: "<c:url value="/monitoring/getUsers.do"/>?sessionMapID=${sessionMapID}&sessionId=${sessionDto.sessionId}",
				height: '100%',
				autowidth: true,
				shrinkToFit: false,
			    pager: 'listPager${sessionDto.sessionId}',
			    rowList:[10,20,30,40,50,100],
			    rowNum:10,
			    viewrecords:true,
				guiStyle: "bootstrap",
				iconSet: 'fontAwesome',
			   	colNames:[
				   	'userId',
					'sessionId',
					"<fmt:message key="label.monitoring.summary.user.name" />",
					"<fmt:message key="label.monitoring.summary.total" />",
					'portraitId'
				],
			   	colModel:[
			   		{name:'userId', index:'userId', width:0, hidden: true},
			   		{name:'sessionId', index:'sessionId', width:0, hidden: true},
			   		{name:'userName', index:'userName', width:570, searchoptions: { clearSearch: false }, formatter:userNameFormatter},
			   		{name:'total', index:'total', width:174, align:"right", formatter:'number', search:false},
			   		{name:'portraitId', index:'portraitId', width:0, hidden: true},
			   	],
			   	ondblClickRow: function(rowid) {
			   		var userId = jQuery("#list${sessionDto.sessionId}").getCell(rowid, 'userId');
			   		var sessionId = jQuery("#list${sessionDto.sessionId}").getCell(rowid, 'sessionId');
					var userSummaryUrl = '<c:url value="/monitoring/userSummary.do?sessionMapID=${sessionMapID}"/>';
					var newUserSummaryHref = userSummaryUrl + "&userID=" + userId + "&sessionId=" + sessionId + "&KeepThis=true&TB_iframe=true&modal=true";
					$("#userSummaryHref").attr("href", newUserSummaryHref);	
					$("#userSummaryHref").click(); 		
			  	},
			  	onSelectRow: function(rowid) { 
			  	    if(rowid == null) { 
			  	    	rowid=0; 
			  	    } 
			   		var userId = jQuery("#list${sessionDto.sessionId}").getCell(rowid, 'userId');
			   		var sessionId = jQuery("#list${sessionDto.sessionId}").getCell(rowid, 'sessionId');
					var userMasterDetailUrl = '<c:url value="/monitoring/userMasterDetail.do"/>';
		  	        jQuery("#userSummary${sessionDto.sessionId}").clearGridData().setGridParam({gridstate: "visible"}).trigger("reloadGrid");
		  	        $("#masterDetailArea").load(
		  	        	userMasterDetailUrl,
		  	        	{
		  	        		userID: userId,
		  	        		sessionId: sessionId,
		  	        		sessionMapID: '${sessionMapID}'
		  	       		}
		  	       	);    
	  	  		},
			    loadError: function(xhr,st,err) {
			    	jQuery("#list${sessionDto.sessionId}").clearGridData();
			    	$.jgrid.info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="error.loaderror"/>", "<fmt:message key="label.ok"/>");
			    },
			    loadComplete: function () {
			   	 	initializePortraitPopover('<lams:LAMSURL/>');
			    },

			})
			<c:if test="${!sessionMap.assessment.useSelectLeaderToolOuput}">
				.jqGrid('filterToolbar', { 
					searchOnEnter: false
				})
			</c:if>
			.navGrid("#listPager${sessionDto.sessionId}", {edit:false,add:false,del:false,search:false});
	        
	        var oldValue = 0;
			jQuery("#userSummary${sessionDto.sessionId}").jqGrid({
				datatype: "local",
				autoencode:false,
				rowNum: 10000,
				gridstate:"hidden",
				height: 180,
				autowidth: true,
				shrinkToFit: false,
				guiStyle: "bootstrap",
				iconSet: 'fontAwesome',
			   	colNames:[
				   	'#',
					'questionResultUid',
  					'Question',
  					"<fmt:message key="label.authoring.basic.list.header.mark" />",
  					<c:if test="${sessionMap.assessment.enableConfidenceLevels}">
  						"<fmt:message key="label.confidence" />",
  					</c:if>
  					"<fmt:message key="label.monitoring.user.summary.response" />"
  				], 
			   	colModel:[
	  			   	{name:'id', index:'id', width:20, sorttype:"int"},
	  			   	{name:'questionResultUid', index:'questionResultUid', width:0, hidden: true},
	  			   	{name:'title', index:'title', width: 200},
	  			    {name:'grade', index:'grade', width:80, sorttype:"float", editable:true, editoptions: {size:4, maxlength: 4}, align:"right", classes: 'vertical-align' },
	  			   	<c:if test="${sessionMap.assessment.enableConfidenceLevels}">
	  			   		{name:'confidence', index:'confidence', width: 80, classes: 'vertical-align', formatter: gradientNumberFormatter},
	  			  	</c:if>
	  			  	{name:'response', index:'response', datatype:'html', width:443, sortable:false}
			   	],
			   	multiselect: false,

				cellurl: '<c:url value="/monitoring/saveUserGrade.do?sessionMapID=${sessionMapID}"/>&<csrf:token/>',
  				cellEdit: true,
  				afterEditCell: function (rowid,name,val,iRow,iCol){
  					oldValue = eval(val);
				},
				beforeSaveCell : function(rowid, name, val, iRow, iCol) {
					if (isNaN(val)) {
  						return null;
  					}
					
					// get maxGrade attribute which was set in masterDetailLoadUp.jsp
					var maxGrade = jQuery("table#userSummary${sessionDto.sessionId} tr#" + iRow 
							              + " td[aria-describedby$='_" + name + "']").attr("maxGrade");
					if (+val > +maxGrade) {
						return maxGrade;
					}
				},
  				afterSaveCell : function (rowid,name,val,iRow,iCol){
  					if (isNaN(val)) {
  						jQuery("#userSummary${sessionDto.sessionId}").restoreCell(iRow,iCol); 
  					} else {
  						var parentSelectedRowId = jQuery("#list${sessionDto.sessionId}").getGridParam("selrow");
  						var previousTotal =  eval(jQuery("#list${sessionDto.sessionId}").getCell(parentSelectedRowId, 'total'));
  						jQuery("#list${sessionDto.sessionId}").setCell(parentSelectedRowId, 'total', previousTotal - oldValue + eval(val), {}, {});
  					}
				},	  		
  				beforeSubmitCell : function (rowid,name,val,iRow,iCol){
  					if (isNaN(val)) {
  						return {nan:true};
  					} else {
  						var questionResultUid = jQuery("#userSummary${sessionDto.sessionId}").getCell(rowid, 'questionResultUid');
  						return {questionResultUid:questionResultUid};		  				  		
  				  	}
  				}
			});
			
		</c:forEach>


		
		//jqgrid autowidth (http://stackoverflow.com/a/1610197)
		$(window).bind('resize', function() {
			resizeJqgrid(jQuery(".ui-jqgrid-btable:visible"));
		});

		//resize jqGrid on openning of bootstrap collapsible
		$('div[id^="collapse"]').on('shown.bs.collapse', function () {
			resizeJqgrid(jQuery(".ui-jqgrid-btable:visible", this));
		})

		var questionUidSelect = $('#questionUid'),
			summaryButton = $('#questionSummaryHref'),
			correctButton = $('#questionDiscloseCorrect'),
			correctAllButton = $('#discloseAllCorrect'),
			groupsAllButton = $('#discloseAllGroups'),
			groupsButton = $('#questionDiscloseGroups'),
			correctButtonLabel = "<fmt:message key='label.disclose.correct.answers' />",
			groupsButtonLabel = "<fmt:message key='label.disclose.groups.answers' />",
			checkIcon = '<i class="fa fa-check text-success">&nbsp;</i>';

			// when question dropdow changes, manipulate buttons
			questionUidSelect.change(function(event) {
				var questionUid = $(this).val(),
					selectedOption = $('option:selected', this),
					// disclosing correct answers works only for multiple choice
					isMultipleChoice = selectedOption.attr('isMultipleChoice') == 'true',
					correctDisclosed = isMultipleChoice && selectedOption.attr('correctDisclosed'),
					groupsDisclosed = selectedOption.attr('groupsDisclosed');
			if (questionUid == -1) {
				// user went back to "Choose..." option
				summaryButton.addClass('disabled');
				correctButton.show().addClass('disabled').text(correctButtonLabel);
				groupsButton.show().addClass('disabled').text(groupsButtonLabel);
			} else {
				// set link for thickbox
				summaryButton.removeClass('disabled').attr("href", 
					'<c:url value="/monitoring/questionSummary.do?sessionMapID=${sessionMapID}"/>&questionUid='
					+ questionUid + "&KeepThis=true&TB_iframe=true&modal=true");
				// manipulate disclose buttons
				if (isMultipleChoice) {
					correctButton.show();
					if (correctDisclosed == "true") {
						correctButton.addClass('disabled').html(checkIcon + correctButtonLabel);
					} else {
						correctButton.removeClass('disabled').html(correctButtonLabel);
					}
				} else {
					correctButton.hide();
				}
				
				if (groupsDisclosed == "true") {
					groupsButton.addClass('disabled').html(checkIcon + groupsButtonLabel);
				} else {
					groupsButton.removeClass('disabled').html(groupsButtonLabel);
				}
			}
	    });

		// ajax calls to disclose correct/groups answers
	    correctButton.click(function(){
	    	if (!confirm("<fmt:message key='message.disclose.correct.answers' />")) {
	    		return;
	    	};
	    	
			$.ajax({
                type: 'POST',
				'url'  : '<lams:WebAppURL />monitoring/discloseCorrectAnswers.do?<csrf:token/>',
				'data' : {
					'questionUid'   : questionUidSelect.val(),
					'toolContentID' : '${sessionMap.assessment.contentId}'
				}
			}).done(function(){
				$('option:selected', questionUidSelect).attr('correctDisclosed', 'true');
				correctButton.addClass('disabled').html(checkIcon + correctButtonLabel);
			});
		});

	    groupsButton.click(function(){
	    	if (!confirm("<fmt:message key='message.disclose.groups.answers' />")) {
	    		return;
	    	};
	    	
			$.ajax({
                type: 'POST',
				'url'  : '<lams:WebAppURL />monitoring/discloseGroupsAnswers.do?<csrf:token/>',
				'data' : {
					'questionUid'   : questionUidSelect.val(),
					'toolContentID' : '${sessionMap.assessment.contentId}'
				}
			}).done(function(){
				$('option:selected', questionUidSelect).attr('groupsDisclosed', 'true');
				groupsButton.addClass('disabled').html(checkIcon + groupsButtonLabel);
			});
		});

	    correctAllButton.click(function(){
	    	if (!confirm("<fmt:message key='message.disclose.all.correct.answers' />")) {
	    		return;
	    	};
	    	
		    $('option[correctDisclosed="false"]', questionUidSelect).each(function(){
			    var option = $(this),
			    	questionUid = option.val();
				$.ajax({
                    type: 'POST',
					'url'  : '<lams:WebAppURL />monitoring/discloseCorrectAnswers.do?<csrf:token/>',
					'data' : {
						'questionUid'   : questionUid,
						'toolContentID' : '${sessionMap.assessment.contentId}'
					}
				}).done(function(){
					option.attr('correctDisclosed', 'true');
					if (questionUidSelect.val() == questionUid) {
						correctButton.addClass('disabled').html(checkIcon + correctButtonLabel);
					}
				});
			});
		    correctAllButton.addClass('disabled').html(checkIcon + correctAllButton.text());
		});

	    groupsAllButton.click(function(){
	    	if (!confirm("<fmt:message key='message.disclose.all.groups.answers' />")) {
	    		return;
	    	};
	    	
		    $('option[groupsDisclosed="false"]', questionUidSelect).each(function(){
			    var option = $(this),
			    	questionUid = option.val();
				$.ajax({
                    type: 'POST',
					'url'  : '<lams:WebAppURL />monitoring/discloseGroupsAnswers.do?<csrf:token/>',
					'data' : {
						'questionUid'   : questionUid,
						'toolContentID' : '${sessionMap.assessment.contentId}'
					}
				}).done(function(){
					option.attr('groupsDisclosed', 'true');
					if (questionUidSelect.val() == questionUid) {
						groupsButton.addClass('disabled').html(checkIcon + groupsButtonLabel);
					}
				});
			});
		    groupsAllButton.addClass('disabled').html(checkIcon + groupsAllButton.text());
		});

		// trigger the resize when the window first opens so that the grid uses all the space available.
		setTimeout(function(){ window.dispatchEvent(new Event('resize')); }, 300);
		
		// create counter if absolute time limit is set
		if (absoluteTimeLimit) {
			updateAbsoluteTimeLimitCounter();
			
			// expand time limit panel if absolute time limit is set and not expired
			if (absoluteTimeLimit > new Date().getTime() / 1000) {
				$('#time-limit-collapse').collapse('show');
			}
		}
		initInidividualTimeLimitAutocomplete();
		
		drawCompletionCharts(${assessment.contentId}, ${assessment.useSelectLeaderToolOuput}, true);
	});

	function resizeJqgrid(jqgrids) {
		jqgrids.each(function(index) {
			var gridId = $(this).attr('id');
	    	var gridParentWidth = jQuery('#gbox_' + gridId).parent().width();
	    	jQuery('#' + gridId).setGridWidth(gridParentWidth, true);
	    });
	};

	function userNameFormatter (cellvalue, options, rowObject) {
		return definePortraitPopover(rowObject[4], rowObject[0],  rowObject[2]);
	}

	function exportSummary() {
		var url = "<c:url value='/monitoring/exportSummary.do'/>?<csrf:token/>&sessionMapID=${sessionMapID}&reqID="+(new Date()).getTime();
		return downloadFile(url, 'messageArea_Busy', '<fmt:message key="label.summary.downloaded"/>', 'messageArea', 'btn-disable-on-submit');
	};
	
	
	// TIME LIMIT
	
		// in minutes since learner entered the activity
	var relativeTimeLimit = ${assessment.relativeTimeLimit},
		// in seconds since epoch started
		absoluteTimeLimit = ${empty assessment.absoluteTimeLimit ? 'null' : assessment.absoluteTimeLimitSeconds};
	
	function updateTimeLimit(type, toggle, adjust) {
		// relavite time limit set
		if (type == 'relative') {
			// what is set at the moment on screen, not at server
			var displayedRelativeTimeLimit = +$('#relative-time-limit-value').text();
			
			// start/stop
			if (toggle !== null) {
				
				if (toggle === false) {
					// stop, i.e. set time limit to 0
					relativeTimeLimit = 0;
					updateTimeLimitOnServer();
					return;
				}
				
				// start, i.e. set backend time limit to whatever is set on screen
				if (toggle === true && displayedRelativeTimeLimit > 0) {
					relativeTimeLimit = displayedRelativeTimeLimit;
					// when teacher enables relative time limit, absolute one gets disabled
					absoluteTimeLimit = null;
					updateTimeLimitOnServer();
				}
				return;
			}
			
			// no negative time limit is allowed
			if (displayedRelativeTimeLimit == 0 && adjust < 0) {
				return;
			}
			
			var adjustedRelativeTimeLimit = displayedRelativeTimeLimit + adjust;
			// at least one minute is required
			// if teacher wants to set less, he should disable the limit or click "finish now"
			if (adjustedRelativeTimeLimit < 1) {
				adjustedRelativeTimeLimit = 1;
			}
			
			// is time limit already enforced? if so, update the server
			if (relativeTimeLimit > 0) {
				relativeTimeLimit = adjustedRelativeTimeLimit;
				updateTimeLimitOnServer();
				return;
			}
			
			// if time limit is not enforced yet, just update the screen
			displayedRelativeTimeLimit = adjustedRelativeTimeLimit;
			$('#relative-time-limit-value').text(displayedRelativeTimeLimit);
			$('#relative-time-limit-start').prop('disabled', false);
			return;
		}
		
		if (type == 'absolute') {
			// get existing value on counter, if it is set already
			var counter = $('#absolute-time-limit-counter'),
				secondsLeft = null;
			if (counter.length === 1) {
				var periods = counter.countdown('getTimes');
				secondsLeft = $.countdown.periodsToSeconds(periods);
			}
			
			if (toggle !== null) {
				
				// start/stop
				if (toggle === false) {
					absoluteTimeLimit = null;
					updateAbsoluteTimeLimitCounter();
					return;
				} 
				
				// turn on the time limit, if there is any value on counter set already
				if (toggle === true && secondsLeft) {
					updateAbsoluteTimeLimitCounter(secondsLeft, true);
					return;
				}
				
				if (toggle === 'stop') {
					absoluteTimeLimit =  Math.round(new Date().getTime() / 1000);
					updateAbsoluteTimeLimitCounter();
				}
				return;
			}
			
			// counter is not set yet and user clicked negative value
			if (!secondsLeft && adjust < 0) {
				return;
			}
			
			// adjust time
			secondsLeft += adjust * 60;
			if (secondsLeft < 60) {
				secondsLeft = 60;
			}

			// is time limit already enforced, update the server
			// if time limit is not enforced yet, just update the screen
			updateAbsoluteTimeLimitCounter(secondsLeft);
			$('#absolute-time-limit-start').prop('disabled', false);
			return;
		}
		
		if (type == 'individual') {
			// this method is called with updateTimeLimit.call() so we can change meaning of "this"
			// and identify row and userUid
			var button = $(this),
				row = button.closest('.individual-time-limit-row'),
				userId = row.data('userId');
			
			// disable individual time adjustment
			if (toggle === false) {
				updateIndividualTimeLimitOnServer('user-' + userId);
				return;
			}
			var existingAdjustment = +$('.individual-time-limit-value', row).text(),
				newAdjustment = existingAdjustment + adjust;
			
			updateIndividualTimeLimitOnServer('user-' + userId, newAdjustment);
			return;
		}
	}
	
	function updateTimeLimitOnServer() {
		
		// absolute time limit has higher priority
		if (absoluteTimeLimit != null) {
			relativeTimeLimit = 0;
		}
		
		$.ajax({
			'url' : '<c:url value="/monitoring/updateTimeLimit.do"/>',
			'type': 'post',
			'cache' : 'false',
			'data': {
				'toolContentID' : '${assessment.contentId}',
				'relativeTimeLimit' : relativeTimeLimit,
				'absoluteTimeLimit' : absoluteTimeLimit,
				'<csrf:tokenname/>' : '<csrf:tokenvalue/>'
			},
			success : function(){
				// update widgets
				$('#relative-time-limit-value').text(relativeTimeLimit);
				
				if (relativeTimeLimit > 0) {
					$('#relative-time-limit-disabled').addClass('hidden');
					$('#relative-time-limit-cancel').removeClass('hidden');
					$('#relative-time-limit-enabled').removeClass('hidden');
					$('#relative-time-limit-start').addClass('hidden');
				} else {
					$('#relative-time-limit-disabled').removeClass('hidden');
					$('#relative-time-limit-cancel').addClass('hidden');
					$('#relative-time-limit-enabled').addClass('hidden');
					$('#relative-time-limit-start').removeClass('hidden').prop('disabled', true);
				}
				
				if (absoluteTimeLimit === null) {
					// no absolute time limit? destroy the counter
					$('#absolute-time-limit-counter').countdown('destroy');
					$('#absolute-time-limit-value').empty();
					
					$('#absolute-time-limit-disabled').removeClass('hidden');
					$('#absolute-time-limit-cancel').addClass('hidden');
					$('#absolute-time-limit-enabled').addClass('hidden');
					$('#absolute-time-limit-start').removeClass('hidden').prop('disabled', true);
					$('#absolute-time-limit-finish-now').prop('disabled', false);
				} else {
					$('#absolute-time-limit-disabled').addClass('hidden');
					$('#absolute-time-limit-cancel').removeClass('hidden');
					$('#absolute-time-limit-enabled').removeClass('hidden');
					$('#absolute-time-limit-start').addClass('hidden');
					$('#absolute-time-limit-finish-now').prop('disabled', absoluteTimeLimit <= Math.round(new Date().getTime() / 1000));
				}
			}
		});
	}
	
	function updateAbsoluteTimeLimitCounter(secondsLeft, start) {
		var now = Math.round(new Date().getTime() / 1000),
			// preset means that counter is set just on screen and the time limit is not enforced for learners
			preset = start !== true && absoluteTimeLimit == null;
		
		if (secondsLeft) {
			if (!preset) {
				// time limit is already enforced on server, so update it there now
				absoluteTimeLimit = now + secondsLeft;
				updateTimeLimitOnServer();
			}
		} else {
			if (absoluteTimeLimit == null) {
				// disable the counter
				updateTimeLimitOnServer();
				return;
			}
			// counter initialisation on page load or "finish now"
			secondsLeft = absoluteTimeLimit - now;
			if (secondsLeft <= 0) {
				// finish now
				updateTimeLimitOnServer();
			}
		}
		
		var counter = $('#absolute-time-limit-counter');
	
		if (counter.length == 0) {
			counter = $('<div />').attr('id', 'absolute-time-limit-counter').appendTo('#absolute-time-limit-value')
				.countdown({
					until: '+' + secondsLeft +'S',
					format: 'hMS',
					compact: true,
					alwaysExpire : true,
					onTick: function(periods) {
						// check for 30 seconds or less and display timer in red
						var secondsLeft = $.countdown.periodsToSeconds(periods);
						if (secondsLeft <= 30) {
							counter.addClass('countdown-timeout');
						} else {
							counter.removeClass('countdown-timeout');
						}				
					},
					expiryText : '<span class="countdown-timeout">Expired</span>'
				});
		} else {
			// if counter is paused, we can not adjust time, so resume it for a moment
			counter.countdown('resume');
			counter.countdown('option', 'until', secondsLeft + 'S');
		}
		
		if (preset) {
			counter.countdown('pause');
			$('#absolute-time-limit-start').removeClass('disabled');
		} else {
			counter.countdown('resume');
		}
	}
	
	function timeLimitFinishNow(){
		if (confirm('<fmt:message key="label.monitoring.summary.time.limit.finish.now.confirm" />')) {
			updateTimeLimit('absolute', 'stop');
		}
	}
	
	
	function initInidividualTimeLimitAutocomplete(){
		$('#individual-time-limit-autocomplete').autocomplete({
			'source' : '<c:url value="/monitoring/getPossibleIndividualTimeLimitUsers.do"/>?toolContentID=${assessment.contentId}',
			'delay'  : 700,
			'minLength' : 3,
			'select' : function(event, ui){
				// user ID or group ID, and default 0 adjustment
				updateIndividualTimeLimitOnServer(ui.item.value, 0);

				// clear search field
				$(this).val('');
				return false;
			},
			'focus': function() {
				// Stop the autocomplete of resetting the value to the selected one
				// It puts LAMS user ID instead of user name
				event.preventDefault();
			}
		});
		
		refreshInidividualTimeLimitUsers();
	}
	
	
	function updateIndividualTimeLimitOnServer(itemId, adjustment) {
		$.ajax({
			'url' : '<c:url value="/monitoring/updateIndividualTimeLimit.do"/>',
			'type': 'post',
			'cache' : 'false',
			'data': {
				'toolContentID' : '${assessment.contentId}',
				// itemId can user-<userId> or group-<groupId>
				'itemId' : itemId,
				'adjustment' : adjustment,
				'<csrf:tokenname/>' : '<csrf:tokenvalue/>'
			},
			success : function(){
				refreshInidividualTimeLimitUsers();
			}
		});
	}


	function refreshInidividualTimeLimitUsers() {
		var table = $('#time-limit-table');
		
		$.ajax({
			'url' : '<c:url value="/monitoring/getExistingIndividualTimeLimitUsers.do"/>',
			'dataType' : 'json',
			'cache' : 'false',
			'data': {
				'toolContentID' : '${assessment.contentId}'
			},
			success : function(users) {
				// remove existing users
				$('.individual-time-limit-row', table).remove();
				
				if (!users) {
					return;
				}
				
				var template = $('#individual-time-limit-template-row'),
					now = new Date().getTime();
				$.each(users, function(){
					var row = template.clone()
									  .attr('id', 'individual-time-limit-row-' + this.userId)
									  .data('userId', this.userId)
									  .addClass('individual-time-limit-row')
									  .appendTo(table);
					$('.individual-time-limit-user-name', row).text(this.name);
					$('.individual-time-limit-value', row).text(this.adjustment);
					
					row.removeClass('hidden');
				});
			}
		});
	}

	// END OF TIME LIMIT

	function showChangeLeaderModal(toolSessionId) {
		$('#change-leader-modals').empty()
		.load('<c:url value="/monitoring/displayChangeLeaderForGroupDialogFromActivity.do" />',{
			toolSessionID : toolSessionId
		});
	}

	function onChangeLeaderCallback(response, leaderUserId, toolSessionId){
        if (response.isSuccessful) {
            $.ajax({
    			'url' : '<c:url value="/monitoring/changeLeaderForGroup.do"/>',
    			'type': 'post',
    			'cache' : 'false',
    			'data': {
    				'toolSessionID' : toolSessionId,
    				'leaderUserId' : leaderUserId,
    				'<csrf:tokenname/>' : '<csrf:tokenvalue/>'
    			},
    			success : function(){
    				alert("<fmt:message key='label.monitoring.leader.successfully.changed'/>");
    				location.reload();
    			},
    			error : function(){
    				alert("<fmt:message key='label.monitoring.leader.not.changed'/>");
        		}
            });
        	
		} else {
			alert("<fmt:message key='label.monitoring.leader.not.changed'/>");
		}
	}
	
	
</script>

<div class="panel">
	<h4>
	  <c:out value="${assessment.title}" escapeXml="true"/>
	</h4>
	
	<div class="instructions voffset5">
	  <c:out value="${assessment.instructions}" escapeXml="false"/>
	</div>
	
	<c:choose>
		<c:when test="${empty sessionDtos}">
			<lams:Alert type="info" id="no-session-summary" close="false">
				<fmt:message key="message.monitoring.summary.no.session" />
			</lams:Alert>
		</c:when>
		<c:otherwise>
			<div id="completion-charts-container">
				<div class="col-sm-12 col-md-6">
					<canvas id="activity-completion-chart"></canvas>
				</div>
				
				<div class="col-sm-12 col-md-6">
					<canvas id="answered-questions-chart"></canvas>
				</div>
			</div>
		</c:otherwise>
	</c:choose>


	<lams:WaitingSpinner id="messageArea_Busy"></lams:WaitingSpinner>
	<div class="voffset5 help-block" id="messageArea"></div>
</div>

<c:if test="${not empty sessionDtos}">
	
	<c:choose>
		<c:when test="${displayStudentChoices and not empty questions}">
			<h5><fmt:message key="label.student.choices" /></h5>
			<%@ include file="/pages/monitoring/parts/mcqStudentChoices.jsp" %>
		</c:when>
		<c:otherwise>
			<%-- To maintain structure as if student choices table was present --%>
			<div class="row"></div>
		</c:otherwise>
	</c:choose>
	
	<button onclick="return exportSummary();" class="btn btn-default btn-sm btn-disable-on-submit pull-right">
		<i class="fa fa-download" aria-hidden="true"></i> 
		<fmt:message key="label.monitoring.summary.export.summary" />
	</button>
			
	<h5><fmt:message key="label.monitoring.summary.summary" /></h5>
	
	<div class="comments">
		<fmt:message key="label.monitoring.summary.double.click" />
	</div>

	<div id="masterDetailArea" class="voffset10">
		<%@ include file="parts/masterDetailLoadUp.jsp"%>
	</div>
	<a onclick="" href="return false;" class="thickbox initially-hidden" id="userSummaryHref"></a>
	
	<c:if test="${sessionMap.isGroupedActivity}">
		<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true"> 
	</c:if>
	
	<c:forEach var="sessionDto" items="${sessionDtos}" varStatus="status">
	
		<c:choose>
			<c:when test="${sessionMap.isGroupedActivity}">	
			    <div class="panel panel-default" >
			        <div class="panel-heading" id="heading${sessionDto.sessionId}">
			        	<span class="panel-title collapsable-icon-left">
			        		<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapse${sessionDto.sessionId}" 
									aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapse${sessionDto.sessionId}" >
								<fmt:message key="monitoring.label.group" />:	<c:out value="${sessionDto.sessionName}" />
							</a>
						</span>
						<c:if test="${assessment.useSelectLeaderToolOuput and sessionDto.numberOfLearners > 0 and not sessionDto.leaderFinished}">
							<button type="button" class="btn btn-default btn-xs pull-right"
									onClick="javascript:showChangeLeaderModal(${sessionDto.sessionId})">
								<fmt:message key='label.monitoring.change.leader'/>
							</button>
						</c:if>
			        </div>
		        
		        <div id="collapse${sessionDto.sessionId}" class="panel-collapse collapse ${status.first ? 'in' : ''}" 
		        		role="tabpanel" aria-labelledby="heading${sessionSummary.sessionId}">
			</c:when>
			<c:when test="${assessment.useSelectLeaderToolOuput and sessionDto.numberOfLearners > 0 and not sessionDto.leaderFinished}">
				<div style="text-align: right">
					<button type="button" class="btn btn-default btn-sm voffset10" style="margin-bottom: 10px;"
							onClick="javascript:showChangeLeaderModal(${sessionDto.sessionId})">
						<fmt:message key='label.monitoring.change.leader'/>
					</button>
				</div>
			</c:when>
		</c:choose>
				
		<table id="list${sessionDto.sessionId}"></table>
		<div class="voffset10"></div>
		<table id="userSummary${sessionDto.sessionId}"></table>
		<div id="listPager${sessionDto.sessionId}"></div>

		<c:if test="${sessionMap.isGroupedActivity}">
			</div> <!-- end collapse area  -->
			</div> <!-- end collapse panel  -->
		</c:if>
		${ !sessionMap.isGroupedActivity || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}

	</c:forEach>
		
	<c:if test="${sessionMap.isGroupedActivity}">
		</div> <!--  end panel group -->
	</c:if>

	<!-- Dropdown menu for choosing a question type -->	
	<h5><fmt:message key="label.monitoring.summary.report.by.question" /></h5>
	
	<c:set var="allCorrectDisclosed" value="true" />
	<c:set var="allGroupsDisclosed" value="true" />
	<div class="form-inline form-group voffset5">
		<select id="questionUid" class="form-control input-sm">
			<option selected="selected" value="-1"><fmt:message key="label.monitoring.summary.choose" /></option>
    		<c:forEach var="question" items="${sessionMap.questionList}" varStatus="questionCount">
				<option value="${question.uid}"
						isMultipleChoice="${question.type == 1}"
						correctDisclosed="${question.correctAnswersDisclosed}"
						groupsDisclosed="${question.groupsAnswersDisclosed}">
					${questionCount.count})&nbsp;<c:out value="${question.qbQuestion.name}" escapeXml="true"/>
				</option>
				<c:set var="allCorrectDisclosed"
					   value="${allCorrectDisclosed && (question.type != 1 || question.correctAnswersDisclosed)}" />
				<c:set var="allGroupsDisclosed" value="${allGroupsDisclosed && question.groupsAnswersDisclosed}" />
		   	</c:forEach>
		</select>
			
		<a id="questionSummaryHref" class="thickbox btn btn-default disabled">
			<fmt:message key="label.monitoring.summary.results.question" />
		</a>
		<c:if test="${assessment.allowDiscloseAnswers}">
			<a id="questionDiscloseCorrect" class="btn btn-default disabled">
				<fmt:message key="label.disclose.correct.answers" />
			</a>
			<c:if test="${sessionMap.isGroupedActivity}">
				<a id="questionDiscloseGroups" class="btn btn-default disabled">
					<fmt:message key="label.disclose.groups.answers" />
				</a>
			</c:if>
		</c:if>
	</div>

	<c:if test="${assessment.allowDiscloseAnswers}">
		<div class="voffset5">
			<a id="discloseAllCorrect" class="btn btn-default ${allCorrectDisclosed ? 'disabled' : ''}">
				<c:if test="${allCorrectDisclosed}">
					<i class="fa fa-check text-success">&nbsp;</i>
				</c:if>
				<fmt:message key="label.disclose.all.correct.answers" />
			</a>
			<c:if test="${sessionMap.isGroupedActivity}">
				<a id="discloseAllGroups" class="btn btn-default ${allGroupsDisclosed ? 'disabled' : ''}">
					<c:if test="${allGroupsDisclosed}">
						<i class="fa fa-check text-success">&nbsp;</i>
					</c:if>
					<fmt:message key="label.disclose.all.groups.answers" />
				</a>
			</c:if>
		</div>
	</c:if>
</c:if>

<br/>

<c:if test="${assessment.reflectOnActivity && not empty sessionMap.reflectList}">
	<%@ include file="parts/reflections.jsp"%>
</c:if>

<%@ include file="parts/advanceoptions.jsp"%>

<%@ include file="parts/timeLimit.jsp"%>

<%@ include file="parts/dateRestriction.jsp"%>

<div id="change-leader-modals"></div>