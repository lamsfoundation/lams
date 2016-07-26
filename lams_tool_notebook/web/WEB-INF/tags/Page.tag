<%@ tag body-content="scriptless"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<%@ attribute name="type" required="true" rtexprvalue="true"%>
<%@ attribute name="style" required="false" rtexprvalue="true"%>
<%@ attribute name="title" required="false" rtexprvalue="true"%>
<%@ attribute name="titleHelpURL" required="false" rtexprvalue="true"%>
<%@ attribute name="headingContent" required="false" rtexprvalue="true"%>

			
<c:choose>

	<c:when test='${type == "navbar"}'>
	<%-- Combined tab and navigation bar used in authoring and monitoring --%>
		<div class="row no-gutter">
		<div class="col-xs-12">
		<div class="container" id="content">
		<jsp:doBody />
		</div>
		</div>
		</div>
	</c:when> 

	<c:when test='${type == "learner"}'>
	<%-- Learner --%>
	
		<%-- Links placed in body instead of head. Ugly, but it works. --%>
		<link rel="stylesheet" href="<lams:LAMSURL/>css/progressBar.css" type="text/css" />
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/snap.svg.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/progressBar.js"></script>
		
		<c:if test="${empty lessonID}">
			<%-- Desperately try to get tool session ID from the tool activity --%>
			<c:if test="${empty toolSessionId}">
				<c:set var="toolSessionId" value="${toolSessionID}" />
			</c:if>
			<c:if test="${empty toolSessionId}">
				<c:set var="toolSessionId" value="${param.toolSessionId}" />
			</c:if>
			<c:if test="${empty toolSessionId}">
				<c:set var="toolForm" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
				<c:set var="toolSessionId" value="${toolForm.toolSessionID}" />
			</c:if>
			<c:if test="${empty toolSessionId}">
				<c:if test="${empty sessionMapID}">
					<c:set var="sessionMapID" value="${param.sessionMapID}"/>
				</c:if>
				<c:set var="toolSessionId" value="${sessionScope[sessionMapID].toolSessionID}" />
			</c:if>
		</c:if>
		
		<script type="text/javascript">
			var allowRestart = false,
				restartLessonConfirmation = "Are you sure you want to start the lesson from the beginning?",
				toolSessionId = '${toolSessionId}',
				lessonId = '${lessonID}',
				
				LAMS_URL = '<lams:LAMSURL/>',
				APP_URL = LAMS_URL + 'learning/',
						
				bars = {
					'learnerMainBar' : {
						'containerId' : 'progressBarDiv'
					}
				};

			function restartLesson(){
				if (confirm(restartLessonConfirmation)) {
					window.location.href = APP_URL + 'learner.do?method=restartLesson&lessonID=' + lessonId;
				}
			}

			function viewNotebookEntries(){
				openPopUp(APP_URL + "notebook.do?method=viewAll&lessonID=" + lessonId,
						"Notebook",
						570,796,
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
				$("nav.sidebar").removeClass("expandmenu");
			}
			
			function onProgressBarLoaded() {
				$('#exitlabel').html(LABELS.EXIT);
				$('#notebooklabel').html(LABELS.NOTEBOOK);
				if ( allowRestart ) {
					$('#restartlabel').html(LABELS.RESTART);
					restartLessonConfirmation = LABELS.CONFIRM_RESTART;
					$('#restartitem').show();
				}
			}
			
			$(document).ready(function() {
				var showControlBar = true;
				var showIM = true;
				debugger;
				if ( window.frameElement ) {
					var myId = window.frameElement.id;
					if ( myId ) {
						if ( myId == 'lamsDynamicFrame0' ) {
							showIM = false;
						} else if ( myId == 'lamsDynamicFrame1' ) {
							showControlBar = false;
						}
					}
				}

					debugger;
				if ( lessonId != "" || toolSessionId != "" ) {
					$.ajax({
						url : APP_URL + 'learner.do',
						data : {
							'method'   : 'getLessonDetails',
							'lessonID' : lessonId,
							'toolSessionID' : toolSessionId,
						},
						cache : false,
						dataType : 'json',
						success : function(result) {
							
							debugger;
							lessonId = result.lessonID;
							
							if ( showControlBar ) {
								allowRestart = result.allowRestart;
								$('.lessonName').html(result.title);
								fillProgressBar('learnerMainBar');
								$('#sidebar').show();
							}
							
							var presenceEnabledPatch = result.presenceEnabledPatch;
							var presenceImEnabled = result.presenceImEnabled;
							if ( showIM && (presenceEnabledPatch || presenceImEnabled) ) {
								presenceURL = APP_URL+"presenceChat.jsp?presenceEnabledPatch="+presenceEnabledPatch
										+"&presenceImEnabled="+presenceImEnabled+"&lessonID="+lessonId;
								$('#presenceEnabledPatchDiv').load(presenceURL, function( response, status, xhr ) {
									if ( status == "error" ) {
										alert("Unable to load IM: " + xhr.status);
									} 
								});
							}
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
						<li><a href="#" class="hidden-xs visible-sm visible-md visible-lg slidesidemenu" onClick="javascript:toggleSlideMenu()">
							<i class="pull-right fa fa-bars" style="color:#337ab7"></i>
							<p class="lessonName"></p></a></li>
						<li><a href="#" onClick="javascript:closeWindow()" ><span id="exitlabel">ExitX</span><i class="pull-right fa fa-times"></i></a></li>
						<li><a href="#" onClick="javascript:viewNotebookEntries()" ><span id="notebooklabel">NotebookX</span><i class="pull-right fa fa-book"></i></a></li>
						<li id="restartitem" style="display:none"><a href="#" onClick="javascript:restartLesson()"><span id="restartlabel">RestartX</span><i class="pull-right fa fa-recycle"></i></a></li>
						<li><a href="#" class="slidesidemenu" onClick="javascript:toggleSlideMenu()">
							<i class="pull-right fa fa-map"></i></a>
							<div id="progressBarDiv" class="progressBarContainer"></div></li>
					</ul>
				</div>
			</div>
		</nav>

		<div class="content">
	
			<div class="row no-gutter">
			<div class="col-xs-12">
			<div class="container">
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
					
						<div id="presenceEnabledPatchDiv"></div>
						
			</div>
				</div>
			</div>
	</div>

	</c:when>

	<c:otherwise>
	<!-- Standard Screens  --> 
		<div class="row no-gutter">
		<div class="col-xs-12">
		<div class="container" id="content">
		
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
		</div>
		</div>
		</div>
	</c:otherwise>
</c:choose>


		
			