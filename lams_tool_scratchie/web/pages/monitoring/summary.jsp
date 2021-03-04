<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>
<c:set var="scratchie" value="${sessionMap.scratchie}"/>

<style type="text/css">
	/* remove jqGrid borders */
	.ui-jqgrid {
		border-left-style: none !important;
		border-right-style: none !important;
		border-bottom-style: none !important;
	}
	.ui-jqgrid tr {
		border-left-style: none !important;
	}
	.ui-jqgrid td {
		border-style: none !important;
	}
	
	.ui-jqgrid tr.jqgrow td {
	    padding-top:2px;
	}
	.ui-jqgrid tr.jqgrow td {
		vertical-align:middle !important
	}
	
	/* remove jqGrid border radius */
	.ui-jqgrid.ui-jqgrid-bootstrap {
	    border-radius:0;
	    -moz-border-radius:0;
	    -webkit-border-radius:0;
	    -khtml-border-radius:0;
	}
	
	#collapseBurning .panel {
		margin-bottom: 20px;
	}
	
	#collapseBurning .panel-heading a:after {
	  font-family: FontAwesome;
	  content: "\f139";
	  float: right;
	  color: grey;
	}
	
	#collapseBurning .panel-heading a.collapsed:after {
	  content: "\f13a";
	 }
	
	#collapseBurning .burning-question-title {
	  cursor: pointer;
	}
	
		 		
	.countdown-timeout {
		color: #FF3333 !important;
	}
	
	#time-limit-table th {
		vertical-align: middle;
	}
	
	#time-limit-table td.centered {
		text-align: center;
	}
</style>

<script type="text/javascript">
	$(document).ready(function(){
		 $('[data-toggle="tooltip"]').bootstrapTooltip();
		 
		var oldValue = 0;
		
		<c:forEach var="summary" items="${summaryList}" varStatus="status">
		
			jQuery("#list${summary.sessionId}").jqGrid({
				datatype: "local",
				rowNum: 10000,
				height: 'auto',
				autowidth: true,
				shrinkToFit: false,
				guiStyle: "bootstrap",
				iconSet: 'fontAwesome',
			   	colNames:[
				   	'#',
					'userId',
					'sessionId',
					"<fmt:message key="label.monitoring.summary.user.name" />",
					"<fmt:message key="label.monitoring.summary.attempts" />",
					"<fmt:message key="label.monitoring.summary.mark" />",
					'portraitId',
					'isLeader',
					'reachedActivity'
				],
			   	colModel:[
			   		{name:'id', index:'id', width:0, sorttype:"int", hidden: true},
			   		{name:'userId', index:'userId', width:0, hidden: true},
			   		{name:'sessionId', index:'sessionId', width:0, hidden: true},
			   		{name:'userName', index:'userName', width:570, formatter:userNameFormatter,	cellattr: leaderRowFormatter},
			   		{name:'totalAttempts', index:'totalAttempts', width:100, align:"right", sortable: false, cellattr: leaderRowFormatter},
			   		{name:'mark', index:'mark', width:100, align:"right", sortable: false, editoptions: {size:4, maxlength: 4}, cellattr: leaderRowFormatter,
				   	       editable: function (options) {
				               var row = $(this).jqGrid("getLocalRow", options.rowid);
				               return row.isLeader == 'true';
				           }
			   		},
			   		{name:'portraitId', index:'portraitId', width:0, hidden: true},
			   		{name:'isLeader', index:'isLeader', width:0, hidden: true},
			   		{name:'reachedActivity', index:'reachedActivity', width:0, hidden: true},
			   	],
			   	ondblClickRow: function(rowid) {
			   		var jqGrid = $("#list${summary.sessionId}");
			   		if (jqGrid.getCell(rowid, 'isLeader') != 'true') {
			   			return;
			   		}
			   		var userId = jqGrid.getCell(rowid, 'userId');
			   		var toolSessionId = jqGrid.getCell(rowid, 'sessionId');

			   		var userSummaryUrl = "<c:url value='/learning/start.do'/>?userID=" + userId + "&toolSessionID=" + toolSessionId + "&mode=teacher&reqId=" + (new Date()).getTime();
					launchPopup(userSummaryUrl, "MonitoringReview");		
			  	},
			   	// caption: "${summary.sessionName}",
				cellurl: '<c:url value="/monitoring/saveUserMark.do"/>?<csrf:token/>',
  				cellEdit: true,
  				afterEditCell: function (rowid,name,val,iRow,iCol){
  					oldValue = eval(val);
				},
  				afterSaveCell : function (rowid,name,val,iRow,iCol){
  					var intRegex = /^\d+$/;
  					if (!intRegex.test(val)) {
  						jQuery("#list${summary.sessionId}").restoreCell(iRow,iCol); 
  					}
				},	  		
  				beforeSubmitCell : function (rowid,name,val,iRow,iCol){
  					var intRegex = /^\d+$/;
  					if (!intRegex.test(val)) {
  						return {nan:true};
  					} else {
  						var userId = jQuery("#list${summary.sessionId}").getCell(rowid, 'userId');
  						var sessionId = jQuery("#list${summary.sessionId}").getCell(rowid, 'sessionId');
  						return {userId:userId, sessionId:sessionId};		  				  		
  				  	}
  				}
			});
			
   	        <c:forEach var="user" items="${summary.users}" varStatus="i">
   	     		jQuery("#list${summary.sessionId}").addRowData(${i.index + 1}, {
   	   	     		id:"${i.index + 1}",
   	   	     		userId:"${user.userId}",
   	   	     		sessionId:"${empty user.session ? '' : user.session.sessionId}",
   	   	     		userName:"${user.lastName}, ${user.firstName}",
   	   				totalAttempts:"${summary.leaderUid eq user.uid ? summary.totalAttempts : ''}",
   	   				mark:"${summary.leaderUid eq user.uid ? (summary.totalAttempts == 0 ? '-' : summary.mark) : ''}",
   	   				portraitId:"${user.portraitId}",
   	   				isLeader : "${not empty summary.leaderUid and summary.leaderUid eq user.uid}",
   	   				reachedActivity : "${summary.getUsersWhoReachedActivity().contains(user.userId)}"
   	   	   	    });
	        </c:forEach>

		</c:forEach>

		initializePortraitPopover('<lams:LAMSURL/>');

		<!-- Display burningQuestionItemDtos -->
		<c:forEach var="burningQuestionItemDto" items="${sessionMap.burningQuestionItemDtos}" varStatus="i">
			<c:set var="scratchieItem" value="${burningQuestionItemDto.scratchieItem}"/>
			
			jQuery("#burningQuestions${scratchieItem.uid}").jqGrid({
				datatype: "local",
				rowNum: 10000,
				height: 'auto',
				autowidth: true,
				shrinkToFit: false,
				autoencode: false,
				guiStyle: "bootstrap",
				iconSet: 'fontAwesome',
			   	colNames:['#',
						"<fmt:message key='label.team' />",
					    "<fmt:message key='label.burning.questions' />",
						"<fmt:message key='label.count' />"
				],
			   	colModel:[
			   		{name:'id', index:'id', width:0, sorttype:"int", hidden: true},
			   		{name:'groupName', index:'groupName', width:150},
			   		{name:'feedback', index:'feedback', width:520},
			   		{name:'count', index:'count', align:"right", width:70}
			   	],
			   	// caption: "${scratchieItem.qbQuestion.name}"
			});
			
			<c:forEach var="burningQuestionDto" items="${burningQuestionItemDto.burningQuestionDtos}" varStatus="i">
		    	jQuery("#burningQuestions${scratchieItem.uid}").addRowData(${i.index + 1}, {
		   			id:"${i.index + 1}",
		   	     	groupName:"${burningQuestionDto.sessionName}",
		   	    	feedback:"${burningQuestionDto.escapedBurningQuestion}",
		   	  		count:"${burningQuestionDto.likeCount}"
		   	   	});
	        </c:forEach>
	        
	     	jQuery("#burningQuestions${scratchieItem.uid}").jqGrid('sortGrid','groupName', false, 'asc');
        </c:forEach>
		
		<!-- Display reflection entries -->
		jQuery("#reflections").jqGrid({
			datatype: "local",
			rowNum: 10000,
			height: 'auto',
			autowidth: true,
			shrinkToFit: false,
		   	colNames:['#',
					"<fmt:message key="label.monitoring.summary.user.name" />",
				    "<fmt:message key='label.learners.feedback' />"
			],
		   	colModel:[
		   		{name:'id', index:'id', width:0, sorttype:"int", hidden: true},
		   		{name:'groupName', index:'groupName', width:200},
		   		{name:'feedback', index:'feedback', width:570}
		   	],
		   	//caption: "<fmt:message key='label.learners.feedback' />"
		});
	    <c:forEach var="reflectDTO" items="${sessionMap.reflections}" varStatus="i">
	    	jQuery("#reflections").addRowData(${i.index + 1}, {
	   			id:"${i.index + 1}",
	   	     	groupName:"${reflectDTO.groupName}",
		   	    feedback:"<lams:out value='${reflectDTO.reflection}' escapeHtml='true' />"
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

        function resizeJqgrid(jqgrids) {
            jqgrids.each(function(index) {
                var gridId = $(this).attr('id');
                var gridParentWidth = jQuery('#gbox_' + gridId).parent().width();
                jQuery('#' + gridId).setGridWidth(gridParentWidth, true);
            });
        };
        setTimeout(function(){ window.dispatchEvent(new Event('resize')); }, 300);

        function userNameFormatter (cellvalue, options, rowObject) {
    		var name = definePortraitPopover(rowObject.portraitId, rowObject.userId,  rowObject.userName);
    		var icon = '';
    		
    		if (rowObject.isLeader == 'true') {
    			icon = '&nbsp;<i title="<fmt:message key="label.monitoring.team.leader"/>" class="text-primary fa fa-star"></i>';
    		} else if (rowObject.reachedActivity == 'true') {
    			icon = '&nbsp;<i class="text-primary fa fa-check"></i>';
    		}
    		
    		if (icon != '') {
    			if (rowObject.portraitId == '') {
    				name += icon;
    			} else {
    				name = name.replace('</a>', icon + '</a>');
    			}
    		}
    		
    		return name;
    	}
        
        function leaderRowFormatter (rowID, val, rawObject, cm, rdata) {
			if (rdata.isLeader == 'true') {
				return 'class="info"';
			} else if (rdata.reachedActivity == 'true') {
				return 'title="<fmt:message key="label.summary.reached.activity"/>"';
			}
		}
    	
		$("#item-uid").change(function() {
			var itemUid = $(this).val();
			if (itemUid != -1) {
				var itemSummaryUrl = '<c:url value="/monitoring/itemSummary.do?sessionMapID=${sessionMapID}"/>';
				var itemSummaryHref = itemSummaryUrl + "&itemUid=" + itemUid + "&KeepThis=true&TB_iframe=true";
				$("#item-summary-href").attr("href", itemSummaryHref);	

				//return;
				$("#item-summary-href").click(); 		 
			}
	    });
		
		$("#userid-dropdown").change(function() {
			var userId = $(this).val();
			
			if (userId != -1) {
				var toolSessionId = $(this).find('option:selected').attr("alt");
				var userSummaryUrl = "<c:url value='/learning/start.do'/>?userID=" + userId + "&toolSessionID=" + toolSessionId + "&mode=teacher";
	
				launchPopup(userSummaryUrl, "MonitoringReview");
			}
	    });
		
		//filter reflections by group name
		$("#reflection-group-selector").change(function() {
            var grid = $("#reflections");
            var searchFiler = $(this).val();

            if (searchFiler.length === 0) {
                grid[0].p.search = false;
                $.extend(grid[0].p.postData,{filters:""});
            }
            var f = {groupOp:"OR",rules:[]};
            f.rules.push({field:"groupName",op:"cn",data:searchFiler});
            grid[0].p.search = true;
            $.extend(grid[0].p.postData,{filters:JSON.stringify(f)});
            grid.trigger("reloadGrid",[{page:1,current:true}]);
	    });

   		// create counter if absolute time limit is set
   		if (absoluteTimeLimit) {
   			updateAbsoluteTimeLimitCounter();
   			
   			// expand time limit panel if absolute time limit is set and not expired
   			if (absoluteTimeLimit > new Date().getTime() / 1000) {
   				$('#time-limit-collapse').collapse('show');
   			}
   		}
   		initInidividualTimeLimitAutocomplete();
	});
	
	function exportExcel(){
		var url = "<c:url value='/monitoring/exportExcel.do'/>?<csrf:token/>&sessionMapID=${sessionMapID}&reqID=" + (new Date()).getTime();
		return downloadFile(url, 'messageArea_Busy', '<fmt:message key="label.summary.downloaded"/>', 'messageArea', 'btn-disable-on-submit');
	};

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
	
	// pass settings to monitorToolSummaryAdvanced.js
	var submissionDeadlineSettings = {
		lams: '<lams:LAMSURL />',
		submissionDeadline: '${submissionDeadline}',
		submissionDateString: '${submissionDateString}',
		setSubmissionDeadlineUrl: '<c:url value="/monitoring/setSubmissionDeadline.do"/>?<csrf:token/>',
		toolContentID: '${param.toolContentID}',
		messageNotification: '<fmt:message key="monitor.summary.notification" />',
		messageRestrictionSet: '<fmt:message key="monitor.summary.date.restriction.set" />',
		messageRestrictionRemoved: '<fmt:message key="monitor.summary.date.restriction.removed" />'
	};



	// TIME LIMIT
	
	// in minutes since learner entered the activity
	var relativeTimeLimit = ${scratchie.relativeTimeLimit},
		// in seconds since epoch started
		absoluteTimeLimit = ${empty scratchie.absoluteTimeLimit ? 'null' : scratchie.absoluteTimeLimitSeconds};
	
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
				sessionId = row.data('sessionId');
			
			// disable individual time adjustment
			if (toggle === false) {
				updateIndividualTimeLimitOnServer('group-' + sessionId);
				return;
			}
			var existingAdjustment = +$('.individual-time-limit-value', row).text(),
				newAdjustment = existingAdjustment + adjust;
			
			updateIndividualTimeLimitOnServer('group-' + sessionId, newAdjustment);
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
				'toolContentID' : '${scratchie.contentId}',
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
					expiryText : '<span class="countdown-timeout"><fmt:message key="label.monitoring.summary.time.limit.expired" /></span>'
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
			'source' : '<c:url value="/monitoring/getPossibleIndividualTimeLimitUsers.do"/>?toolContentID=${scratchie.contentId}',
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
				'toolContentID' : '${scratchie.contentId}'
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
									  .attr('id', 'individual-time-limit-row-' + this.sessionId)
									  .data('sessionId', this.sessionId)
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

</script>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js" ></script>

	<c:if test="${not empty summaryList}">
		<button onclick="return exportExcel();" class="btn btn-default btn-sm btn-disable-on-submit pull-right">
			<i class="fa fa-download" aria-hidden="true"></i> 
			<fmt:message key="label.export.excel" />
		</button>
	</c:if>

	<div class="panel">
		<h4>
		    <c:out value="${scratchie.title}" escapeXml="true"/>
		</h4>
		<div class="instructions voffset5">
		    <c:out value="${scratchie.instructions}" escapeXml="false"/>
		</div>
		
		<c:if test="${empty summaryList}">
			<lams:Alert type="info" id="no-session-summary" close="false">
				 <fmt:message key="message.monitoring.summary.no.session" />
			</lams:Alert>
		</c:if>
	
		<lams:WaitingSpinner id="messageArea_Busy"></lams:WaitingSpinner>
		<div class="voffset5 help-block" id="messageArea"></div>
	
	</div>
	
	<c:set var="showStudentChoicesTableOnly" value="true" />
	<h4><fmt:message key="monitoring.tab.summary" /></h4>
	<%@ include file="studentChoices.jsp"%>

	<div class="form-group">
		<!-- Dropdown menu for choosing scratchie item -->
		<label for="item-uid"><h4><fmt:message key="label.monitoring.summary.report.by.scratchie" /></h4></label>
		<select id="item-uid" class="form-control">
			<option selected="selected" value="-1"><fmt:message key="label.monitoring.summary.choose" /></option>
   			<c:forEach var="item" items="${scratchie.scratchieItems}" varStatus="questionCount">
				<option value="${item.uid}">${questionCount.count})&nbsp;<c:out value="${item.qbQuestion.name}" escapeXml="true"/></option>
		   	</c:forEach>
		</select>
		<a href="#nogo" class="thickbox" id="item-summary-href" style="display: none;"></a>
	</div>

	<h4 style="padding-top: 10px"><fmt:message key="label.report.by.team.tra" /></h4>
	<fmt:message key="label.monitoring.summary.select.student" />

	<c:set var="summaryTitle"><fmt:message key="label.monitoring.summary.summary" /></c:set>
	<c:forEach var="summary" items="${summaryList}" varStatus="status">

		<c:if test="${sessionMap.isGroupedActivity}">
			<c:set var="summaryTitle"><strong><fmt:message key="monitoring.label.group" /></strong> ${summary.sessionName}</c:set>
		</c:if>
		
	    <div class="panel panel-default" >
        <div class="panel-heading" id="heading${summary.sessionId}">
        	<span class="panel-title collapsable-icon-left">
        	<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapse${summary.sessionId}" 
					aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapse${summary.sessionId}" >
				${summaryTitle}</a>
			</span>
			<c:if test="${fn:length(summary.users) > 0 and not summary.scratchingFinished}">
				<button type="button" class="btn btn-default btn-xs pull-right"
						onClick="javascript:showChangeLeaderModal(${summary.sessionId})">
					<fmt:message key='label.monitoring.change.leader'/>
				</button>
			</c:if>
        </div>
        
        <div id="collapse${summary.sessionId}" class="panel-collapse collapse ${status.first ? 'in' : ''}" role="tabpanel" aria-labelledby="heading${summary.sessionId}">

		<table id="list${summary.sessionId}" class="scroll" cellpadding="0" cellspacing="0"></table>
		
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	
	</c:forEach>

	<!-- Display burningQuestionItemDtos -->
	<c:if test="${scratchie.burningQuestionsEnabled}">
		<div class="panel-group" style="padding-top: 10px" id="accordionBurning" role="tablist" aria-multiselectable="true"> 
		    <div class="panel panel-default" >
		        <div class="panel-heading collapsable-icon-left" id="headingBurning">
		        	<span class="panel-title">
			    	<a role="button" data-toggle="collapse" href="#collapseBurning" aria-expanded="false" aria-controls="collapseBurning" >
		          	<fmt:message key="label.burning.questions" />
		        	</a>
		      		</span>
		        </div>
		
		        <div id="collapseBurning" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingBurning" style="padding: 10px;">
					<%@ include file="parts/burningQuestions.jsp"%>
				</div>
			</div>
		</div>
	</c:if>
	
	<!-- Display reflection entries -->
	<c:if test="${sessionMap.reflectOn}">

		<div class="panel-group" id="accordionReflections" role="tablist" aria-multiselectable="true"> 
		    <div class="panel panel-default" >
		        <div class="panel-heading collapsable-icon-left" id="headingReflections">
		        	<span class="panel-title">
			    	<a role="button" data-toggle="collapse" href="#collapseReflections" aria-expanded="false" aria-controls="collapseReflections" >
		          	<fmt:message key="label.learners.feedback" />
		        	</a>
		        	<select id="reflection-group-selector" class="input-sm pull-right">
						<option selected="selected" value=""><fmt:message key="label.all" /></option>
		    			<c:forEach var="reflectDTO" items="${sessionMap.reflections}">
							<option value="${reflectDTO.groupName}">${reflectDTO.groupName}</option>
					   	</c:forEach>
					</select>
		      		</span>
		        </div>
		
		        <div id="collapseReflections" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingReflections">
					<table id="reflections" class="scroll" cellpadding="0" cellspacing="0"></table>
				</div>
			</div>
		</div>
	</c:if>
	
<%@ include file="parts/advanceOptions.jsp"%>

<%@ include file="parts/timeLimit.jsp"%>

<%@ include file="parts/dateRestriction.jsp"%>

<div id="change-leader-modals"></div>
