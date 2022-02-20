<%@ tag body-content="scriptless"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<%@ attribute name="type" required="true" rtexprvalue="true"%>
<%@ attribute name="formID" required="false" rtexprvalue="true"%>
<%@ attribute name="style" required="false" rtexprvalue="true"%>
<%@ attribute name="title" required="false" rtexprvalue="true"%>
<%@ attribute name="titleHelpURL" required="false" rtexprvalue="true"%>
<%@ attribute name="headingContent" required="false" rtexprvalue="true"%>
<%@ attribute name="usePanel" required="false" rtexprvalue="true"%>
<%@ attribute name="hideProgressBar" required="false" rtexprvalue="true"%>

<%@ tag  import="org.lamsfoundation.lams.util.Configuration"%>
<%@ tag  import="org.lamsfoundation.lams.util.ConfigurationKeys"%>

<c:if test="${empty usePanel}">				
	<c:set var="usePanel">true</c:set>
</c:if>
<c:set var="displayPortrait"><%=Configuration.get(ConfigurationKeys.DISPLAY_PORTRAIT)%></c:set>

<c:choose>

	<c:when test='${type == "navbar"}'>
	<%-- Combined tab and navigation bar used in authoring and monitoring --%>
		<div class="row no-gutter no-margin">
		<div class="col-xs-12">
		<div class="container-fluid" id="content" style="max-width: 1400px">
			<jsp:doBody />
		</div>
		</div>
		</div>
	</c:when> 

	<c:when test='${type == "learner"}'>
	<%-- Learner --%>

	<%-- Try to get authoring preview/learning/monitoring from the tool activity so we don't show the progress bar in monitoring --%>
	<c:if test="${empty mode}">
		<c:set var="mode" value="${param.mode}" />
	</c:if>
	<c:if test="${empty mode}">
		<c:if test="${empty sessionMapID}">
			<c:set var="sessionMapID" value="${param.sessionMapID}"/>
		</c:if>
		<c:set var="mode" value="${sessionScope[sessionMapID].mode}" />
	</c:if>

	<%--  only have sidebar and presence in learner main window, not in popup windows --%>
	<c:if test="${ not hideProgressBar && ( empty mode || mode == 'author' || mode == 'learner') }">
	
		<%-- Links placed in body instead of head. Ugly, but it works. --%>
		<lams:css suffix="progressBar"/>
		<script type="text/javascript">
			// Adding jquery-ui.js if it hasn't been loaded already
			jQuery.ui || document.write('<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"><\/script>');
		
			// Adding bootstrap.js if it hasn't been loaded already
			(typeof($.fn.modal) != 'undefined') || document.write('<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"><\/script>');
		</script>
		<lams:JSImport src="includes/javascript/dialog.js" />
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/snap.svg.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/progressBar.js"></script>
		
		<c:if test="${empty lessonID}">
			<c:set var="lessonID" value="${param.lessonID}" />
		</c:if>

		<c:if test="${empty lessonID}">
			<%-- Desperately try to get tool session ID from the tool activity --%>
			<c:if test="${empty toolSessionId}">
				<c:set var="toolSessionId" value="${toolSessionID}" />
			</c:if>
			<c:if test="${empty toolSessionId}">
				<c:set var="toolSessionId" value="${param.toolSessionId}" />
			</c:if>
			<c:if test="${empty toolSessionId}">
				<c:set var="toolSessionId" value="${param.toolSessionID}" />
			</c:if>
 			<c:if test="${empty toolSessionId}">
				<c:if test="${empty sessionMapID}">
					<c:set var="sessionMapID" value="${param.sessionMapID}"/>
				</c:if>
 				<c:if test="${not empty sessionMapID}">
					<c:set var="toolSessionId" value="${sessionScope[sessionMapID].toolSessionID}" />
					<c:if test="${empty toolSessionId}">
						<c:set var="toolSessionId" value="${sessionScope[sessionMapID].toolSessionId}" />
					</c:if>
				</c:if>
			</c:if>
			<c:if test="${empty toolSessionId and not empty formID}">
				<c:set var="toolForm" value="${requestScope[formID]}" />
				<c:if test="${not empty toolForm}"> 
				    <c:set var="toolSessionId"><c:catch var="exception">${toolForm.toolSessionID}</c:catch></c:set>
				</c:if>
			</c:if>
		</c:if>
		
		<script type="text/javascript">
			var allowRestart = false,
				restartLessonConfirmation = "Are you sure you want to start the lesson from the beginning?",
				toolSessionId = '${toolSessionId}',
				lessonId = '${lessonID}',
				mode = '${mode}',
				
				LAMS_URL = '<lams:LAMSURL/>',
				LEARNING_URL = LAMS_URL + 'learning/',

				// it gets initialised along with progress bar
				commandWebsocketInitTime = null,
				commandWebsocket = null,
				commandWebsocketPingTimeout = null,
				commandWebsocketPingFunc = null,
				commandWebsocketReconnectAttempts = 0,
				commandWebsocketHookTrigger = null,
				commandWebsocketHook = null,
				
				bars = {
					'learnerMainBar' : {
						'containerId' : 'progressBarDiv'
					}
				};
				
			commandWebsocketPingFunc = function(skipPing){
				if (commandWebsocket.readyState == commandWebsocket.CLOSING 
						|| commandWebsocket.readyState == commandWebsocket.CLOSED){
					return;
				}
				
				// check and ping every 3 minutes
				commandWebsocketPingTimeout = setTimeout(commandWebsocketPingFunc, 3*60*1000);
				// initial set up does not send ping
				if (!skipPing) {
					commandWebsocket.send("ping");
				}
			};
				
			function restartLesson(){
				if (confirm(restartLessonConfirmation)) {
					window.location.href = LEARNING_URL + 'learner/restartLesson.do?lessonID=' + lessonId;
				}
			}

			function viewNotebookEntries(){
				openPopUp(LEARNING_URL + "notebook/viewAll.do?lessonID=" + lessonId,
						"Notebook",
						648,1152,
						"no");
				hideSlideMenu(); /* For touch screen */
			}
		
			function closeWindow() {
	 			top.window.close();
			}

			function toggleSlideMenu() {	
				if ( $("nav.sidebar").hasClass("expandmenu") ) {
					hideSlideMenu();
				}
				else 
					$("nav.sidebar").addClass("expandmenu");
			}

			function hideSlideMenu() {	
				hideProgressBars();
				$("nav.sidebar").removeClass("expandmenu");
			}
			
			function onProgressBarLoaded() {
				$('#exitlabel').html(LABELS.EXIT);
				$('#notebooklabel').html(LABELS.NOTEBOOK);
				$('#supportlabel').html(LABELS.SUPPORT_ACTIVITIES);
				$('#progresslabel').html(LABELS.PROGRESS_BAR);
				if ( allowRestart ) {
					$('#restartlabel').html(LABELS.RESTART);
					restartLessonConfirmation = LABELS.CONFIRM_RESTART;
					$('#restartitem').show();
				}
				$('#sidebar').show();
			}

			function preventLearnerAutosaveFromMultipleTabs(autosaveInterval) {
				let currentTime = new Date().getTime(),
					lamsAutosaveTimestamp = +localStorage.getItem('lamsAutosaveTimestamp');
				// check if autosave does not happen too often
				if (autosaveInterval > 0 && lamsAutosaveTimestamp && lamsAutosaveTimestamp + +autosaveInterval - 200 > currentTime) {
					// this label is required only in tool which implement autosave
					alert('<fmt:message key="label.prevent.learner.autosave.mutliple.tabs" />');
					return false;
				}
				localStorage.setItem('lamsAutosaveTimestamp', currentTime);
				return true;
			}

			function initCommandWebsocket(){
				commandWebsocketInitTime = Date.now();
				// it is not an obvious place to init the websocket, but we need lesson ID
				commandWebsocket = new WebSocket(LEARNING_URL.replace('http', 'ws')
						+ 'commandWebsocket?lessonID=' + lessonId);

				commandWebsocket.onclose = function(e){
					// check reason and whether the close did not happen immediately after websocket creation
					// (possible access denied, user logged out?)
					if (e.code === 1006 &&
						Date.now() - commandWebsocketInitTime > 1000 &&
						commandWebsocketReconnectAttempts < 20) {
						commandWebsocketReconnectAttempts++;
						// maybe iPad went into sleep mode?
						// we need this websocket working, so init it again after delay
						setTimeout(initCommandWebsocket, 3000);
					}
				};

				// set up timer for the first time
				commandWebsocketPingFunc(true);
				
				// when the server pushes new commands
				commandWebsocket.onmessage = function(e){
					// read JSON object
					var command = JSON.parse(e.data);
					if (command.message) {
						// some tools implement autosave feature
						// if it is such a tool, trigger it
						if (command.message === 'autosave') {
							// the name of this function is same in all tools
							if (typeof learnerAutosave == 'function') {
								learnerAutosave(true);
							}
						} else {
							alert(command.message);
						}
					}
					
					// if learner's currently displayed page has hookTrigger same as in the JSON
					// then a function also defined on that page will run
					if (command.hookTrigger && command.hookTrigger == commandWebsocketHookTrigger 
										    && typeof commandWebsocketHook === 'function') {
					    commandWebsocketHook(command.hookParams);
					}

					if (command.redirectURL) {
						window.location.href = command.redirectURL;
					}

					if (command.discussion) {
						var discussionCommand = $('#discussion-sentiment-command');
						if (discussionCommand.length === 0) {
							discussionCommand = $('<div />').attr('id', 'discussion-sentiment-command').appendTo('body');
						}
						discussionCommand.load(LEARNING_URL + "discussionSentiment/" + command.discussion + ".do", {
							lessonId : lessonId
						});
					}

					// reset ping timer
					clearTimeout(commandWebsocketPingTimeout);
					commandWebsocketPingFunc(true);
				};

				// check if there is a running discussion; if so, a websocket command will come to display the widget
				$.ajax({
					url : LEARNING_URL + "discussionSentiment/checkLearner.do",
					data: {
						lessonId : lessonId
					}
				});
			}
			
			$(document).ready(function() {
				var showControlBar = 1; // 0/1/2 none/full/keep space
				var showIM = true;
				if ( window.name.match("LearnerActivity") || window.parent.name.match("LearnerActivity")) { 
					// popup window
					showControlBar = 0;
					showIM = false;
				} else 	if ( window.frameElement ) { // parallel
					var myId = window.frameElement.id;
					if ( myId ) {
						if ( myId == 'lamsDynamicFrame0' ) {
							showIM = false;
						} else if ( myId == 'lamsDynamicFrame1') {
							showControlBar = 2;
						}
					}
				}

				if ( lessonId != "" || toolSessionId != "" ) {
					$.ajax({
						url : LEARNING_URL + 'learner/getLessonDetails.do',
						data : {
							'lessonID' : lessonId,
							'toolSessionID' : toolSessionId,
						},
						cache : false,
						dataType : 'json',
						success : function(result) {
							
							lessonId = result.lessonID;
							
							if ( showControlBar == 1  ) {
								allowRestart = result.allowRestart;
								$('.lessonName').html(result.title.replace(/_/g, " "));
								fillProgressBar('learnerMainBar');
								$('#navcontent').addClass('navcontent');
							} else if ( showControlBar == 2 ) {
								$('#navcontent').addClass('navcontent');
							}
							
							var presenceEnabledPatch = result.presenceEnabledPatch;
							var presenceImEnabled = result.presenceImEnabled;
							if ( showIM && (presenceEnabledPatch || presenceImEnabled) ) {
								presenceURL = LEARNING_URL+"presenceChat.jsp?presenceEnabledPatch="+presenceEnabledPatch
										+"&presenceImEnabled="+presenceImEnabled+"&lessonID="+lessonId;
								<c:if test="${not usePanel}">
								presenceURL = presenceURL + "&reloadBootstrap=true";
								</c:if>
								$('#presenceEnabledPatchDiv').load(presenceURL, function( response, status, xhr ) {
									if ( status == "error" ) {
										alert("Unable to load IM: " + xhr.status);
									} 
								});
							}

							initCommandWebsocket();
						}
					});
				}


			});
			
		</script>
	
		<nav class="navbar navbar-default sidebar" id="sidebar" role="navigation" onMouseEnter="javascript:toggleSlideMenu()" onMouseLeave="javascript:hideSlideMenu()" style="display:none" >
		    <div class="container-fluid">
				<!-- Brand and toggle get grouped for better mobile display -->
				<div class="navbar-header">
					<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-sidebar-navbar-collapse-1">
						<span class="sr-only">Toggle navigation</span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
					</button>
					<a class="navbar-brand visible-xs hidden-sm hidden-md hidden-lg" href="#"><span class="lessonName"></span></a>
				</div>
				
				<!-- Collect the nav links, forms, and other content for toggling -->
				<div class="collapse navbar-collapse" id="bs-sidebar-navbar-collapse-1">
					<ul class="nav navbar-nav">
						<li>
							<c:if test="${displayPortrait}">
								<a id="navbar-portrait" href="#" onclick="javascript:showMyPortraitDialog(); return false;">
									<c:set var="userId"><lams:user property="userID" /></c:set>
									<lams:Portrait userId="${userId}"/>	
								</a>
							</c:if>
					
							<a href="#" class="hidden-xs visible-sm visible-md visible-lg slidesidemenu" onClick="javascript:toggleSlideMenu(); return false;">
								<c:if test="${!displayPortrait}">
									<i class="pull-right fa fa-bars"></i>
								</c:if>
								
								<p class="lessonName <c:if test='${!displayPortrait}'>no-portrait</c:if>"></p>
							</a>
						</li>
						<li>
							<a href="#" onClick="javascript:viewNotebookEntries(); return false;" >
								<span id="notebooklabel">Notebook</span>
								<i class="pull-right fa fa-book"></i>
							</a>
						</li>
						<li id="restartitem" style="display:none">
							<a href="#" onClick="javascript:restartLesson()">
								<span id="restartlabel">Restart</span>
								<i class="pull-right fa fa-fast-backward "></i>
							</a>
						</li>
						<li id="supportitem" style="display:none">
							<a href="#" class="slidesidemenu" onClick="javascript:toggleSlideMenu(); return false;">
								<span id="supportlabel">Support Activities</span>
								<i class="pull-right fa fa-th-large"></i>
							</a>
							<div id="supportPart" class="progressBarContainer"></div>
						</li>
						<li>
							<a href="#" class="slidesidemenu" onClick="javascript:toggleSlideMenu(); return false;">
								<span id="progresslabel">My Progress</span>
								<i class="pull-right fa fa-map"></i>
							</a>
							<div id="progressBarDiv" class="progressBarContainer"></div>
							
						</li>
					</ul>
				</div>
			</div>
		</nav>
		
		<div class="progress-bar-tooltip" id="progress-bar-tooltip"></div>
		
	</c:if> <%--  end of sidebar stuff - only used if in learner screen --%>

		<div id="navcontent" class="content">
			<div class="row no-gutter no-margin">
			<div class="col-xs-12">
			<div class="container">
				<c:choose>
				<c:when test="${usePanel}">
					<div class="panel panel-default panel-${type}-page">
						<c:if test="${not empty title}">
							<div class="panel-heading">
								<div class="panel-title panel-${type}-title">
									<c:out value="${title}" escapeXml="true" />
									<c:if test="${not empty titleHelpURL}">
										<span class="pull-right">${titleHelpURL}</span>
									</c:if>
								</div>
								<c:if test="${not empty headingContent}">
									<c:out value="${headingContent}" escapeXml="true" />
								</c:if>
							</div>
						</c:if>
						
						<div class="panel-body panel-${type}-body">
							<jsp:doBody />
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<jsp:doBody />
				</c:otherwise>
				</c:choose>						
				<%--  only have sidebar and presence in learner --%>
				<c:if test="${ not hideProgressBar && ( empty mode || mode == 'author' || mode == 'learner') }">
					<div id="presenceEnabledPatchDiv"></div>
				</c:if>
			</div>
				</div>
			</div>
		</div>

	</c:when>

	<c:otherwise>
	<!-- Standard Screens  --> 
		<div class="row no-gutter no-margin">
		<div class="col-xs-12">
		<div class="container" id="content">

		<c:choose>
		<c:when test="${usePanel}">
		<div class="panel panel-default panel-${type}-page">
			<c:if test="${not empty title}">
				<div class="panel-heading">
					<div class="panel-title panel-${type}-title">
						<c:out value="${title}" escapeXml="true" />
						<c:if test="${not empty titleHelpURL}">
							<span class="pull-right">${titleHelpURL}</span>
						</c:if>
					</div>
					<c:if test="${not empty headingContent}">
						<c:out value="${headingContent}" escapeXml="true" />
					</c:if>
				</div>
			</c:if>
			
			<div class="panel-body panel-${type}-body">
				<jsp:doBody />
			</div>
		</div>
		</c:when>

		<c:otherwise>
		<jsp:doBody />
		</c:otherwise>
		</c:choose>						
		
		</div>
		</div>
		</div>
	</c:otherwise>
</c:choose>
