<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>
<c:set var="whiteboard" value="${sessionMap.whiteboard}" />
<%@ page import="org.lamsfoundation.lams.tool.whiteboard.WhiteboardConstants"%>

<lams:css suffix="jquery.jRating"/>
<link href="${lams}css/jquery-ui-bootstrap-theme.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap.css"/>
<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager.css" />
	
<style media="screen,projection" type="text/css">
	 		
	.whiteboard-monitoring-summary .countdown-timeout {
		color: #FF3333 !important;
	}
	
	.whiteboard-monitoring-summary #time-limit-table th {
		vertical-align: middle;
	}
	
	.whiteboard-monitoring-summary #time-limit-table td.centered {
		text-align: center;
	}

	.whiteboard-monitoring-summary .panel {
		overflow: auto;
	}
	
	.whiteboard-monitoring-summary #control-buttons {
		float: right;
		margin-bottom: 20px;
	}
	
	.whiteboard-monitoring-summary #gallery-walk-start {
		margin-left: 20px;
	}
	
	.whiteboard-monitoring-summary #gallery-walk-rating-table {
		width: 60%;
		margin: 50px auto;
		border-bottom: 1px solid #ddd;
	}
	
	.whiteboard-monitoring-summary #gallery-walk-rating-table th {
		font-weight: bold;
		font-style: normal;
		text-align: center;
	}
	
	.whiteboard-monitoring-summary #gallery-walk-rating-table td {
		text-align: center;
	}
	
	.whiteboard-monitoring-summary #gallery-walk-rating-table th:first-child, .whiteboard-monitoring-summary #gallery-walk-rating-table td:first-child {
		text-align: right;
	}
	
	.whiteboard-monitoring-summary .tablesorter tbody > tr > td > div[contenteditable=true]:focus {
	  outline: #337ab7 2px solid;
	}
	
	.whiteboard-monitoring-summary #no-session-summary, .whiteboard-monitoring-summary .attendance-row {
		margin-right: 0;
	}
	
	/* We need to overwrite settings coming from main CSS as in Whiteboard monitoring they look different */
	
	.whiteboard-monitoring-summary .ts-pager {
	    color: black;
	}
	
	.whiteboard-monitoring-summary .ts-pager .btn {
	    background-color: #eee;
	}
	.whiteboard-monitoring-summary .tablesorter tfoot th {
		background-color: #eee !important;
	}
	
	.whiteboard-monitoring-summary .pagesize {
	    border: black;
	    background: white;
	}
					
	.whiteboard-monitoring-summary .whiteboard-frame {
		width: 100%;
		height: 700px;
		border: 1px solid #c1c1c1;
	}
	
	.full-screen-launch-button {
		margin-bottom: 5px;
		margin-top: 5px;
	}
	
	.full-screen-exit-button {
		display: none;
		margin-bottom: 5px;
	}

	.full-screen-content-div:fullscreen {
		padding: 20px 0 70px 0;
	}
	
	.full-screen-content-div:fullscreen .full-screen-flex-div {
		margin: 0 2%;
	}
	
	.full-screen-content-div:fullscreen .full-screen-flex-div,
	.full-screen-content-div:fullscreen .full-screen-main-div,
	.full-screen-content-div:fullscreen .whiteboard-frame {
		height: 100%;
		width: 100%;
	}
</style>

<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.plugin.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script> 
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script>  
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script> 
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-editable.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.countdown.js"></script> 
<script type="text/javascript" src="${lams}includes/javascript/portrait.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/fullscreen.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script type="text/javascript">
	//var for jquery.jRating.js
	var pathToImageFolder = "${lams}images/css/",
		//vars for rating.js
		AVG_RATING_LABEL = '<fmt:message key="label.average.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param></fmt:message>',
		YOUR_RATING_LABEL = '<fmt:message key="label.your.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param><fmt:param>@3@</fmt:param></fmt:message>',
		MAX_RATES = 0,
		MIN_RATES = 0,
		LAMS_URL = '${lams}',
		COUNT_RATED_ITEMS = true,
		ALLOW_RERATE = false;
	
	$(document).ready(function(){
		// show Whiteboards only on Group expand
		$('#whiteboard-monitoring-summary-${sessionMap.toolContentID} .whiteboard-collapse').on('show.bs.collapse', function(){
			var whiteboard = $('.whiteboard-frame', this);
			if (whiteboard.data('src')) {
				whiteboard.attr('src', whiteboard.data('src'));
				whiteboard.data('src', null);
			}
		});
		
		$("#whiteboard-monitoring-summary-${sessionMap.toolContentID} .fix-faulty-pad").click(function() {
			var toolSessionId = $(this).data("session-id");
			var button = $(this);
			
	    	//block #buttons
	    	$(this).block({
	    		message: '<h4 style="color:#fff";><fmt:message key="label.pad.started.fixing" /></h4>',
	    		baseZ: 1000000,
	    		fadeIn: 0,
	    		css: {
	    			border: 'none',
	    		    padding: "2px 7px", 
	    		    backgroundColor: '#000', 
	    		    '-webkit-border-radius': '10px', 
	    		    '-moz-border-radius': '10px', 
	    		    opacity: .98 ,
	    		    left: "0px",
	    		    width: "360px"
	    		},
	    		overlayCSS: {
	    			opacity: 0
	    		}
	    	});
	    	
	        $.ajax({
	        	async: true,
	            url: '<c:url value="/monitoring/fixFaultySession.do"/>',
	            data : 'toolSessionID=' + toolSessionId,
	            type: 'post',
	            success: function (response) {
	            	button.parent().html('<fmt:message key="label.pad.fixed" />');
	            	alert('<fmt:message key="label.pad.fixed" />');
	            },
	            error: function (request, status, error) {
	            	button.unblock();
	                alert(request.responseText);
	            }
	       	});
		});
		
		// marks table for each group
		var tablesorters = $("#whiteboard-monitoring-summary-${sessionMap.toolContentID} .tablesorter");
		// intialise tablesorter tables
		tablesorters.tablesorter({
			theme: 'bootstrap',
			headerTemplate : '{content} {icon}',
		    sortInitialOrder: 'asc',
		    sortList: [[0]],
		    widgets: [ "uitheme", "resizable", "editable" ],
		    headers: { 0: { sorter: true}, 1: { sorter: true}  }, 
		    sortList : [[0,1]],
		    showProcessing: false,
		    widgetOptions: {
		    	resizable: true,
		    	
		    	// only marks is editable
		        editable_columns       : [1],
		        editable_enterToAccept : true,          // press enter to accept content, or click outside if false
		        editable_autoAccept    : false,          // accepts any changes made to the table cell automatically
		        editable_autoResort    : false,         // auto resort after the content has changed.
		        editable_validate      : function (text, original, columnIndex) {
		        	// removing all text produces "&nbsp;", so get rid of it
		        	text = text ? text.replace(/&nbsp;/g, '').trim() : null;
		        	// acceptable values are empty text or a number
		        	return !text || !isNaN(text) ? text : original;
		        },
		        editable_selectAll     : function(txt, columnIndex, $element) {
		          // note $element is the div inside of the table cell, so use $element.closest('td') to get the cell
		          // only select everthing within the element when the content starts with the letter "B"
		          return true;
		        },
		        editable_wrapContent   : '<div>',       // wrap all editable cell content... makes this widget work in IE, and with autocomplete
		        editable_trimContent   : true,          // trim content ( removes outer tabs & carriage returns )
		        editable_editComplete  : 'editComplete' // event fired after the table content has been edited
		    }
		});
		
		// update mark on edit
		tablesorters.each(function(){
		    // config event variable new in v2.17.6
		    $(this).children('tbody').on('editComplete', 'td', function(event, config) {
		      var $this = $(this),
		        mark = $this.text() ? +$this.text() : null,
		        toolSessionId = +$this.closest('.tablesorter').attr('toolSessionId'),
		        userId = +$this.closest('tr').attr('userId'); 
		        
		        // max mark is 100
		        if (mark > 100) {
		        	mark = 100;
		        	$this.text(mark);
		        }

		        $.ajax({
		        	async: true,
		            url: '<c:url value="/monitoring/updateLearnerMark.do"/>',
		            data : {
		            	'toolSessionId' : toolSessionId,
		            	'userId'		: userId,
		            	'mark'			: mark,
		            	'<csrf:tokenname/>' : '<csrf:tokenvalue/>'
		            },
		            type: 'post',
		            error: function (request, status, error) {
		                alert('<fmt:message key="messsage.monitoring.learner.marks.update.fail" />');
		            }
		       	});
		        
		    });
		});

		// pager processing
		tablesorters.each(function() {
			var toolSessionId = $(this).attr('toolSessionId');
			
			$(this).tablesorterPager({
				processAjaxOnInit: true,
				initialRows: {
			        total: 10
			      },
			    savePages: false, 
			    container: $(this).find(".ts-pager"),
		        output: '{startRow} to {endRow} ({totalRows})',
		        cssPageDisplay: '.pagedisplay',
		        cssPageSize: '.pagesize',
		        cssDisabled: 'disabled',
				ajaxUrl : "<c:url value='/monitoring/getLearnerMarks.do?{sortList:column}&page={page}&size={size}&toolSessionId='/>" + toolSessionId,
				ajaxProcessing: function (data, table) {
			    	if (data && data.hasOwnProperty('rows')) {
			    		var rows = [],
			            json = {};

			    		
			    		for (i = 0; i < data.rows.length; i++){
							var userData = data.rows[i],
								isLeader = userData['isLeader'];
							
							rows += '<tr userId="' + userData['userId'] + '" ' + (isLeader ? 'class="info"' : '') + '>';

							rows += '<td style="width: 80%">';
							rows += 	userData['firstName'] + ' ' +userData['lastName'];
							if (isLeader) {
								rows += '&nbsp;<i title="<fmt:message key="label.monitoring.team.leader"/>" class="text-primary fa fa-star"></i>';
							}
							rows += '</td>';
														
							rows += '<td>';
							rows += 	userData['mark'];
							rows += '</td>';

							rows += '</tr>';
						}
			            
						json.total = data.total_rows;
						json.rows = $(rows);
						return json;
			    	}
				}
		  	})
		   .bind('pagerInitialized pagerComplete', function(event, options){
			  if ( options.totalRows == 0 ) {
				  $.tablesorter.showError($(this), '<fmt:message key="messsage.monitoring.learner.marks.no.data"/>');
			  }
			});
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
		
		
		<c:if test="${isTbl}">
			//insert total learners number taken from the parent tblmonitor.jsp
			$("#whiteboard-monitoring-summary-${sessionMap.toolContentID} .total-learners-number").text(TOTAL_LESSON_LEARNERS_NUMBER);
		</c:if>

		setupFullScreenEvents();
	});
	
	function startGalleryWalk(){
		if (!confirm('<fmt:message key="monitoring.summary.gallery.walk.start.confirm" />')) {
			return;
		}
		
		$.ajax({
			'url' : '<c:url value="/monitoring/startGalleryWalk.do"/>',
			'data': {
				toolContentID : ${whiteboard.contentId}
			},
			'success' : function(){
				let summaryPane = $('#whiteboard-monitoring-summary-${sessionMap.toolContentID}');
				
				$('#gallery-walk-start', summaryPane).hide();
				$('#gallery-walk-finish, #learner-reedit', summaryPane).removeClass('hidden');
			}
		});
	}
	
	function finishGalleryWalk(){
		if (!confirm('<fmt:message key="monitoring.summary.gallery.walk.finish.confirm" />')) {
			return;
		}
		
		$.ajax({
			'url' : '<c:url value="/monitoring/finishGalleryWalk.do"/>',
			'data': {
				toolContentID : ${whiteboard.contentId}
			},
			'success' : function(){
				<c:choose>
					<c:when test="${whiteboard.galleryWalkReadOnly}">
						$('#whiteboard-monitoring-summary-${sessionMap.toolContentID} #gallery-walk-finish').hide();
					</c:when>
					<c:when test="${isTbl}">
						// reload current tab with Whiteboard summary
						loadTab(null, null, false);
					</c:when>
					<c:otherwise>
						location.reload();
					</c:otherwise>
				</c:choose>
			}
		});
	}

	function learnerReedit(){
		if (!confirm('<fmt:message key="monitoring.summary.learner.reedit.confirm" />')) {
			return;
		}
		
		$.ajax({
			'url' : '<c:url value="/monitoring/learnerReedit.do"/>',
			'data': {
				toolContentID : ${whiteboard.contentId}
			},
			'success' : function(){
				<c:choose>
					<c:when test="${isTbl}">
						// reload current tab with Doku summary
						loadTab(null, null, false);
					</c:when>
					<c:otherwise>
						location.reload();
					</c:otherwise>
				</c:choose>
			}
		});
	}
	
	function showChangeLeaderModal(toolSessionId) {
		$('#whiteboard-monitoring-summary-${sessionMap.toolContentID} #change-leader-modals').empty()
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
    			},
    			error : function(){
    				alert("<fmt:message key='label.monitoring.leader.not.changed'/>");
        		}
            });
        	
		} else {
			alert("<fmt:message key='label.monitoring.leader.not.changed'/>");
		}
	}



	// TIME LIMIT
		// in minutes since learner entered the activity
	var relativeTimeLimit = ${whiteboard.relativeTimeLimit},
		// in seconds since epoch started
		absoluteTimeLimit = ${empty whiteboard.absoluteTimeLimit ? 'null' : whiteboard.absoluteTimeLimitSeconds};
	
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
				'toolContentID' : '${sessionMap.toolContentID}',
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
			'source' : '<c:url value="/monitoring/getPossibleIndividualTimeLimitUsers.do"/>?toolContentID=${sessionMap.toolContentID}',
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
				'toolContentID' : '${sessionMap.toolContentID}',
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
				'toolContentID' : '${sessionMap.toolContentID}'
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
</script>
<script type="text/javascript" src="${lams}includes/javascript/rating.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>

<!-- Extra container div to isolate content from multiple Application Excercise tabs in TBL monitoring -->
<div id="whiteboard-monitoring-summary-${sessionMap.toolContentID}" class="whiteboard-monitoring-summary">
	<div class="panel">
		<c:choose>
			<c:when test="${isTbl}">
				<div class="row attendance-row">
					<div class="col-xs-6 col-sm-4">
						<div class="panel panel-default">
							<div class="panel-heading">
								<h4 class="panel-title">
									<i class="fa fa-users" style="color:gray" ></i> 
									<fmt:message key="label.attendance"/>: <span>${attemptedLearnersNumber}</span>/<span class="total-learners-number"></span> 
								</h4> 
							</div>
						</div>
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<h4>
				    <c:out value="${whiteboard.title}" escapeXml="true"/>
				</h4>
			</c:otherwise>
		</c:choose>
	
	
		<c:if test="${empty summaryList}">
			<lams:Alert type="info" id="no-session-summary" close="false">
				 <fmt:message key="message.monitoring.summary.no.session" />
			</lams:Alert>
		</c:if>
		
		<!--For release marks feature-->
		<i class="fa fa-spinner" style="display:none" id="message-area-busy"></i>
		<div id="message-area"></div>
	
		<c:if test="${not empty summaryList and whiteboard.galleryWalkEnabled}">
			<div id="control-buttons">
				<button id="gallery-walk-start" type="button"
				        class="btn btn-default 
				        	   ${not whiteboard.galleryWalkStarted and not whiteboard.galleryWalkFinished ? '' : 'hidden'}"
				        onClick="javascript:startGalleryWalk()">
					<fmt:message key="monitoring.summary.gallery.walk.start" /> 
				</button>
				
				<button id="learner-reedit" type="button"
				        class="btn btn-default ${whiteboard.galleryWalkStarted or whiteboard.galleryWalkFinished ? '' : 'hidden'}"
				        onClick="javascript:learnerReedit()">
					<fmt:message key="monitoring.summary.learner.reedit" /> 
				</button>
				
				<button id="gallery-walk-finish" type="button"
				        class="btn btn-default ${whiteboard.galleryWalkStarted and not whiteboard.galleryWalkFinished ? '' : 'hidden'}"
				        onClick="javascript:finishGalleryWalk()">
					<fmt:message key="monitoring.summary.gallery.walk.finish" /> 
				</button>
			</div>
			
			<br>
		</c:if>
	</div>

	<c:if test="${whiteboard.galleryWalkFinished and not whiteboard.galleryWalkReadOnly}">
		<h4 class="voffset20" style="text-align: center"><fmt:message key="label.gallery.walk.ratings.header" /></h4>
		<table id="gallery-walk-rating-table" class="table table-hover table-condensed">
		  <thead class="thead-light">
		    <tr>
		      <th scope="col"><fmt:message key="monitoring.label.group" /></th>
		      <th scope="col"><fmt:message key="label.rating" /></th>
		    </tr>
		  </thead>
		  <tbody>
			<c:forEach var="groupSummary" items="${summaryList}">
				<tr>
					<td>${groupSummary.sessionName}</td>
					<td>
						<lams:Rating itemRatingDto="${groupSummary.itemRatingDto}" 
									 isItemAuthoredByUser="true"
									 hideCriteriaTitle="true" />
					</td>
				</tr>
			</c:forEach>
		  </tbody>
		</table>
	</c:if>
	
	<c:if test="${sessionMap.isGroupedActivity}">
		<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true"> 
	</c:if>
	
	<c:forEach var="groupSummary" items="${summaryList}" varStatus="status">
		<c:choose>
			<c:when test="${sessionMap.isGroupedActivity}">		
			    <div class="panel panel-default" >
		        <div class="panel-heading" id="heading${groupSummary.sessionId}">
		        	<span class="panel-title collapsable-icon-left">
		        		<a class="collapsed" role="button" data-toggle="collapse" href="#collapse${groupSummary.sessionId}" 
								aria-expanded="false" aria-controls="collapse${groupSummary.sessionId}" >
							<fmt:message key="monitoring.label.group" />&nbsp;${groupSummary.sessionName}
						</a>
					</span>
					<c:if test="${whiteboard.useSelectLeaderToolOuput and groupSummary.numberOfLearners > 0 and not groupSummary.sessionFinished}">
						<button type="button" class="btn btn-default btn-xs pull-right"
								onClick="javascript:showChangeLeaderModal(${groupSummary.sessionId})">
							<fmt:message key='label.monitoring.change.leader'/>
						</button>
					</c:if>
		        </div>
		        
		        <div id="collapse${groupSummary.sessionId}" class="panel-collapse collapse whiteboard-collapse" 
		        	 role="tabpanel" aria-labelledby="heading${groupSummary.sessionId}">
			</c:when>
			<c:when test="${whiteboard.useSelectLeaderToolOuput and groupSummary.numberOfLearners > 0 and not groupSummary.sessionFinished}">
				<div style="text-align: right">
					<button type="button" class="btn btn-default" style="margin-bottom: 10px"
							onClick="javascript:showChangeLeaderModal(${groupSummary.sessionId})">
						<fmt:message key='label.monitoring.change.leader'/>
					</button>
				</div>
			</c:when>
		</c:choose>
		
		<div class="full-screen-content-div">
			<div class="full-screen-flex-div">
				<a href="#" class="btn btn-default fixed-button-width pull-right full-screen-launch-button" onclick="javascript:launchIntoFullscreen(this)"
				   title="<fmt:message key='label.fullscreen.open' />">
					<i class="fa fa-arrows-alt" aria-hidden="true"></i>
				</a> 
		       	<a href="#" class="btn btn-default fixed-button-width pull-right full-screen-exit-button" onclick="javascript:exitFullscreen()"
				   title="<fmt:message key='label.fullscreen.close' />">
		       		<i class="fa fa-compress" aria-hidden="true"></i>
		       	</a>
		       	<div class="full-screen-main-div">
					<%-- If there is no grouping, data is loaded immediately.
					     If there is grouping, data is loaded on panel expand. --%>
					<iframe class="whiteboard-frame"
							${sessionMap.isGroupedActivity ? "data-" : ""}src='${whiteboardServerUrl}/?whiteboardid=${groupSummary.wid}&username=${whiteboardAuthorName}${empty groupSummary.accessToken ? "" : "&accesstoken=".concat(groupSummary.accessToken)}&copyfromwid=${sourceWid}${empty whiteboardCopyAccessToken ? "" : "&copyaccesstoken=".concat(groupSummary.copyAccessToken)}'>
					</iframe>		
				</div>
			</div>
		</div>

		<!-- Editable marks section -->
		<div class="voffset10">	
			<h4>
			   <fmt:message key="label.monitoring.learner.marks.header"/>
			</h4>
			<lams:TSTable numColumns="2" dataId='toolSessionId="${groupSummary.sessionId}"'>
				<th><fmt:message key="label.monitoring.learner.marks.name"/></th>
				<th><fmt:message key="label.monitoring.learner.marks.mark"/>&nbsp;
					<small><fmt:message key="label.monitoring.learner.marks.mark.tip"/></small>
				</th>
			</lams:TSTable>
		</div>
		
		<c:if test="${sessionMap.isGroupedActivity}">
			</div> <!-- end collapse area  -->
			</div> <!-- end collapse panel  -->
		</c:if>
		${ !sessionMap.isGroupedActivity || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}
		
	</c:forEach>
	
	<c:if test="${sessionMap.isGroupedActivity}">
		</div> <!--  end accordianSessions --> 
	</c:if>
		
	<c:if test="${sessionMap.whiteboard.reflectOnActivity}">
		<%@ include file="reflections.jsp"%>
	</c:if>
	
	<c:if test="${not isTbl}">
		<%@ include file="advanceoptions.jsp"%>
	</c:if>
	
	<%@ include file="timeLimit.jsp"%>
	
	<div id="change-leader-modals"></div>
</div>