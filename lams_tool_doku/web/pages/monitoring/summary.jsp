<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>
<c:set var="dokumaran" value="${sessionMap.dokumaran}" />
<%@ page import="org.lamsfoundation.lams.tool.dokumaran.DokumaranConstants"%>

<link rel="stylesheet" type="text/css" href="${lams}css/jquery.countdown.css" />
<link rel="stylesheet" type="text/css" href="${lams}css/jquery.jgrowl.css" />
<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap.css"/>
<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager.css" />
	
<style media="screen,projection" type="text/css">
	#countdown {
		min-width: 150px;
		width: 100%;
		font-size: 110%; 
		font-style: italic; 
		color:#47bc23; 
		margin-bottom: 10px;
		text-align: center;
		position: static;
	}
	#countdown-label {
		font-size: 170%; padding-top:5px; padding-bottom:5px; font-style: italic; color:#47bc23;
	}
	#timelimit-expired {
		font-size: 145%; padding: 15px;
	}
		
	.jGrowl {
	  	font-size: 22px;
	  	font-family: arial, helvetica, sans-serif;
	  	margin-left: 120px;
	}
	.jGrowl-notification {
		opacity: .6;
		border-radius: 10px;
		width: 260px;
		padding: 10px 20px;
		margin: 10px 20px;
	}
	.jGrowl-message {
		padding-left: 10px;
		padding-top: 5px;
	}
	
	.panel {
		overflow: auto;
	}
	#control-buttons {
		padding-bottom: 20px;
	}
	
	#gallery-walk-start {
		margin-left: 20px;
	}
	
	#gallery-walk-rating-table {
		width: 60%;
		margin: 50px auto;
		border-bottom: 1px solid #ddd;
	}
	
	#gallery-walk-rating-table th {
		font-weight: bold;
		font-style: normal;
		text-align: center;
	}
	
	#gallery-walk-rating-table td {
		text-align: center;
	}
	
	#gallery-walk-rating-table th:first-child, #gallery-walk-rating-table td:first-child {
		text-align: right;
	}
	
	.tablesorter tbody > tr > td > div[contenteditable=true]:focus {
	  outline: #337ab7 2px solid;
	}
</style>

<script type="text/javascript" src="${lams}includes/javascript/jquery.plugin.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.countdown.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.jgrowl.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script> 
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script>  
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script> 
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-editable.js"></script> 
<script type="text/javascript" src="${lams}includes/javascript/portrait.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/etherpad.js"></script>
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
		ALLOW_RERATE = false,
		
		isCountdownStarted = ${not empty dokumaran.timeLimitLaunchedDate};
	
	$(document).ready(function(){
		// show etherpads only on Group expand
		$('.etherpad-collapse').on('show.bs.collapse', function(){
			var etherpad = $('.etherpad-container', this);
			if (!etherpad.hasClass('initialised')) {
				var id = etherpad.attr('id'),
					groupId = id.substring('etherpad-container-'.length);
				etherpadInitMethods[groupId]();
			}
		});
		
		$(".fix-faulty-pad").click(function() {
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
		
		//display countdown 
		if (${dokumaran.timeLimit > 0}) {
			displayCountdown();
		}
		
		$("#start-activity").click(function() {
			var button = $(this);
			button.hide();
			
	        $.ajax({
	        	async: true,
	            url: '<c:url value="/monitoring/launchTimeLimit.do"/>',
	            data : 'toolContentID=${sessionMap.toolContentID}',
	            type: 'post',
	            success: function (response) {
                	$.jGrowl(
                    	"<i class='fa fa-lg fa-floppy-o'></i> <fmt:message key='label.started.activity' />",
                    	{ 
                    		life: 2000, 
                    		closeTemplate: ''
                    	}
                    );
                	
                	//unpause countdown
                	$('#countdown').countdown('resume');
                	isCountdownStarted = true;
	            },
	            error: function (request, status, error) {
	            	button.show();
	                alert(request.responseText);
	            }
	       	});
	        
	        return false;
		});
		
		$("#add-one-minute").click(function() {
			var button = $(this);
			
	        $.ajax({
	        	async: true,
	            url: '<c:url value="/monitoring/addOneMinute.do"/>',
	            data : 'toolContentID=${sessionMap.toolContentID}',
	            type: 'post',
	            success: function (response) {
	            	var times = $("#countdown").countdown('getTimes');
	            	var secondsLeft = times[4]*3600 + times[5]*60 + times[6] + 60;
	            	if (isCountdownStarted) {
		            	$('#countdown').countdown('option', { until: '+' + secondsLeft + 'S' });
		            
		            //fixing countdown bug of not updating it in case if being paused 
	            	} else {
	            		$('#countdown').countdown('resume');
		            	$('#countdown').countdown('option', { until: '+' + secondsLeft + 'S' });
		            	$('#countdown').countdown('pause');
	            	}
	            	
	            },
	            error: function (request, status, error) {
	                alert(request.responseText);
	            }
	       	});
	        
	        return false;
		});
		
		
		// marks table for each group
		var tablesorters = $(".tablesorter");
		// intialise tablesorter tables
		tablesorters.tablesorter({
			theme: 'bootstrap',
			headerTemplate : '{content} {icon}',
		    sortInitialOrder: 'asc',
		    sortList: [[0]],
		    widgets: [ "uitheme", "resizable", "editable" ],
		    headers: { 0: { sorter: true}, 1: { sorter: true}, 2: { sorter: false}  }, 
		    sortList : [[0,1]],
		    showProcessing: false,
		    widgetOptions: {
		    	resizable: true,
		    	
		    	// only marks is editable
		        editable_columns       : [2],
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
							var userData = data.rows[i];
							
							rows += '<tr userId="' + userData['userId'] + '">';

							rows += '<td>';
							rows += 	userData['firstName'];
							rows += '</td>';
							
							rows += '<td>';
							rows += 	userData['lastName'];
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
	});
	
	function displayCountdown() {		
		$('#countdown').countdown({
			until: '+${sessionMap.secondsLeft}S',
			format: 'hMS',
			compact: true,
			description: "<div id='countdown-label'><fmt:message key='label.time.left' /></div>",
			onTick: function(periods) {
				//check for 30 seconds
				if ((periods[4] == 0) && (periods[5] == 0) && (periods[6] <= 30)) {
					$('#countdown').css('color', '#FF3333');
				} else {
					$('#countdown').css('color', '#47bc23');
				}					
			},
			onExpiry: function(periods) {
			}
		});
		
		//pause countdown in case it hasn't been yet started
		if (${empty dokumaran.timeLimitLaunchedDate}) {
			$('#countdown').countdown('pause');
		}
	}
	
	function startGalleryWalk(){
		if (!confirm('<fmt:message key="monitoring.summary.gallery.walk.start.confirm" />')) {
			return;
		}
		
		$.ajax({
			'url' : '<c:url value="/monitoring/startGalleryWalk.do"/>',
			'data': {
				toolContentID : ${dokumaran.contentId}
			},
			'success' : function(){
				$('#gallery-walk-start, #countdown, #add-one-minute, #start-activity').hide();
				$('#gallery-walk-finish').removeClass('hidden');
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
				toolContentID : ${dokumaran.contentId}
			},
			'success' : function(){
				<c:choose>
					<c:when test="${dokumaran.galleryWalkReadOnly}">
						$('#gallery-walk-finish').hide();
					</c:when>
					<c:otherwise>
						location.reload();
					</c:otherwise>
				</c:choose>
			}
		});
	}


	function showChangeLeaderModal(toolSessionId) {
		$('#change-leader-modals').empty()
		.load('<lams:LAMSURL/>tool/lalead11/monitoring/displayChangeLeaderForGroupDialogFromActivity.do',{
			toolSessionId : toolSessionId
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
</script>
<script type="text/javascript" src="${lams}includes/javascript/rating.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>

<div class="panel">
	<h4>
	    <c:out value="${dokumaran.title}" escapeXml="true"/>
	</h4>
	
	<c:out value="${dokumaran.description}" escapeXml="false"/>
	
	
	<c:if test="${empty summaryList}">
		<lams:Alert type="info" id="no-session-summary" close="false">
			 <fmt:message key="message.monitoring.summary.no.session" />
		</lams:Alert>
	</c:if>
	
	<!--For release marks feature-->
	<i class="fa fa-spinner" style="display:none" id="message-area-busy"></i>
	<div id="message-area"></div>

	<c:if test="${dokumaran.timeLimit > 0 or dokumaran.galleryWalkEnabled}">
		<div class="pull-right" id="control-buttons">
	
			<c:if test="${dokumaran.timeLimit > 0 and not dokumaran.galleryWalkStarted}">
				<div id="countdown"></div>
			
				<c:if test="${empty dokumaran.timeLimitLaunchedDate and dokumaran.timeLimitManualStart}">
					<a href="#nogo" class="btn btn-default" id="start-activity">
						<fmt:message key="label.start.activity" />
					</a>		
				</c:if>
				
				<a href="#nogo" class="btn btn-default" id="add-one-minute">
					<fmt:message key="label.plus.one.minute" />
				</a>	
			</c:if>
			
			<c:if test="${dokumaran.galleryWalkEnabled}">
				<button id="gallery-walk-start" type="button"
				        class="btn btn-default 
				        	   ${not dokumaran.galleryWalkStarted and not dokumaran.galleryWalkFinished ? '' : 'hidden'}"
				        onClick="javascript:startGalleryWalk()">
					<fmt:message key="monitoring.summary.gallery.walk.start" /> 
				</button>
				
				<button id="gallery-walk-finish" type="button"
				        class="btn btn-default ${dokumaran.galleryWalkStarted and not dokumaran.galleryWalkFinished ? '' : 'hidden'}"
				        onClick="javascript:finishGalleryWalk()">
					<fmt:message key="monitoring.summary.gallery.walk.finish" /> 
				</button>
			</c:if>
		</div>
		
		<br>
	</c:if>
</div>

<c:if test="${dokumaran.galleryWalkFinished and not dokumaran.galleryWalkReadOnly}">
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
	
	<c:if test="${sessionMap.isGroupedActivity}">
	    <div class="panel panel-default" >
        <div class="panel-heading" id="heading${groupSummary.sessionId}">
        	<span class="panel-title collapsable-icon-left">
        		<a class="collapsed" role="button" data-toggle="collapse" href="#collapse${groupSummary.sessionId}" 
						aria-expanded="false" aria-controls="collapse${groupSummary.sessionId}" >
					<fmt:message key="monitoring.label.group" />&nbsp;${groupSummary.sessionName}
				</a>
			</span>
			<c:if test="${dokumaran.useSelectLeaderToolOuput and groupSummary.numberOfLearners > 0 and not groupSummary.sessionFinished}">
				<button type="button" class="btn btn-default btn-xs pull-right"
						onClick="javascript:showChangeLeaderModal(${groupSummary.sessionId})">
					<fmt:message key='label.monitoring.change.leader'/>
				</button>
			</c:if>
        </div>
        
        <div id="collapse${groupSummary.sessionId}" class="panel-collapse collapse etherpad-collapse" 
        	 role="tabpanel" aria-labelledby="heading${groupSummary.sessionId}">
	</c:if>
	
	<c:choose>
		<c:when test="${groupSummary.sessionFaulty}">
		
			<div class="faulty-pad-container">
				<fmt:message key="label.cant.display.faulty.pad" />
				
				<a href="#nogo" class="btn btn-default btn-xs fix-faulty-pad" data-session-id="${groupSummary.sessionId}">
					<fmt:message key="label.recreate.faulty.pad" />
				</a>
			</div>
									
		</c:when>
		<c:otherwise>
			<c:if test="${dokumaran.galleryWalkStarted and not dokumaran.galleryWalkReadOnly}">
				<lams:Rating itemRatingDto="${groupSummary.itemRatingDto}" isItemAuthoredByUser="true" />
			</c:if>
			
			<div class="btn-group btn-group-xs pull-right">
				<c:url  var="exportHtmlUrl" value="${etherpadServerUrl}/p/${groupSummary.padId}/export/html"/>
				<a href="#nogo" onclick="window.location = '${exportHtmlUrl}';" class="btn btn-default btn-sm " 
						title="<fmt:message key="label.export.pad.html" />">
					<i class="fa fa-lg fa-file-text-o"></i>
					<fmt:message key="label.export.pad.html" />
				</a>
			</div>	
			
			<lams:Etherpad groupId="${groupSummary.sessionId}" padId="${groupSummary.padId}"
						   showControls="true" showChat="${dokumaran.showChat}" showOnDemand="${sessionMap.isGroupedActivity}"
						   heightAutoGrow="true" height="600" />	
		</c:otherwise>
	</c:choose>
	

	<!-- Editable marks section -->
	<div class="voffset10">	
		<h4>
		   <fmt:message key="label.monitoring.learner.marks.header"/>
		</h4>
		<lams:TSTable numColumns="3" dataId='toolSessionId="${groupSummary.sessionId}"'>
			<th><fmt:message key="label.monitoring.learner.marks.first.name"/></th>
			<th><fmt:message key="label.monitoring.learner.marks.last.name"/></th>
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
	
<c:if test="${sessionMap.dokumaran.reflectOnActivity}">
	<%@ include file="reflections.jsp"%>
</c:if>

<%@ include file="advanceoptions.jsp"%>

<div id="change-leader-modals"></div>