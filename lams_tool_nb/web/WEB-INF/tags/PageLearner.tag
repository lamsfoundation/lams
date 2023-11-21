<%@ tag body-content="scriptless" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<%@ attribute name="toolSessionID" required="true" rtexprvalue="true"%>
<%@ attribute name="title" required="false" rtexprvalue="true"%>
<%@ attribute name="refresh" required="false" rtexprvalue="true"%>
<%@ attribute name="lessonID" required="false" rtexprvalue="true"%>

<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="pageLearnerPortraitUuid"><lams:user property="portraitUuid" /></c:set>
<c:choose>
    <c:when test="${not empty pageLearnerPortraitUuid}">
        <c:set var="pageLearnerPortraitSrc">${lams}download/?uuid=${pageLearnerPortraitUuid}&preferDownload=false&version=4</c:set>
    </c:when>
    <c:otherwise>
        <c:set var="pageLearnerPortraitSrc">${lams}images/css/john-doe-portrait.jpg</c:set>
    </c:otherwise>
</c:choose>
<c:set var="pageLearnerFirstName"><lams:user property="firstName" /></c:set>
<c:set var="pageLearnerLastName"><lams:user property="lastName" /></c:set>

<lams:html>
    <lams:head>
        <title>LAMS ::
            <c:choose>
                <c:when test="${empty title}">
                    <fmt:message key="activity.title"/>
                </c:when>
                <c:otherwise>
                    <c:out value="${title}" />
                </c:otherwise>
            </c:choose>
        </title>
        <c:if test="${not empty refresh}">
        	<meta http-equiv="refresh" content="${refresh}">
        </c:if>

        <link rel="icon" type="image/x-icon" href="<lams:LAMSURL/>images/svg/lamsv5_logo.svg">
        <link rel="stylesheet" href="<lams:LAMSURL/>learning/css/components-learner.css">
        <link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">

        <script src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
        <script src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
        <script src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
        <lams:JSImport src="learning/includes/javascript/gate-check5.js" />
        <lams:JSImport src="includes/javascript/websocket.js" />
        <lams:JSImport src="learning/includes/javascript/learnerPage.js" />
        <lams:JSImport src="includes/javascript/dialog5.js" />
        <lams:JSImport src="includes/javascript/common.js" />

        <script>
            const LAMS_URL = '<lams:LAMSURL/>',
                decoderDiv = $('<div />');
            var commandWebsocketHookTrigger = null,
                commandWebsocketHook = null;

            $(document).ready(function (){
                const toolSessionID = ${empty lessonID ? toolSessionID : 'null'},
            		lessonID = ${empty lessonID ? 'null' : lessonID};
                initLearnerPage(toolSessionID, lessonID);
            });

            function preventLearnerAutosaveFromMultipleTabs(autosaveInterval) {
                let currentTime = new Date().getTime(),
                    lamsAutosaveTimestamp = +localStorage.getItem('lamsAutosaveTimestamp');
                // check if autosave does not happen too often
                if (autosaveInterval > 0 && lamsAutosaveTimestamp && lamsAutosaveTimestamp + autosaveInterval/2 > currentTime) {
                    // this label is required only in tool which implement autosave
                    alert('<fmt:message key="label.prevent.learner.autosave.mutliple.tabs" />');
                    return false;
                }
                localStorage.setItem('lamsAutosaveTimestamp', currentTime);
                return true;
            }
        </script>
    </lams:head>

    <body class="component">
    <div class="component-page-wrapper">
		<div class="offcanvas offcanvas-start" tabindex="-1" id="component-offcanvas" aria-label="Side menu">
			<div class="offcanvas-body">
				<button type="button" class="btn-close float-end" data-bs-dismiss="offcanvas" aria-label="Close"></button>
				
	            <span class="lams-logo mt-3">
	                <img src="<lams:LAMSURL/>images/svg/lamsv5_logo.svg" alt="LAMS logo" aria-hidden="false"/>
	            </span>
			
	            <div id="offcanvas-support-bar" class="offcanvas-bar lcard d-none pb-3">
	                <div class="offcanvas-title py-3">
	                	Support activities
	                </div>
	                <ul id="support-bar-items" class="progress-bar-items list-group text-start">
	                </ul>
	            </div>
	
				<div class="offcanvas-bar lcard pb-3">
		            <div class="offcanvas-title py-3" id="offcanvas-progress-bar-title">
		            	My progress
		            </div>
		            <ul id="progress-bar-items" class="progress-bar-items list-group text-start">
		            </ul>
	            </div>
			</div>
		</div>

		<div class="component-page-content">
            <a href="#component-main-content" class="visually-hidden-focusable p-2">Skip to main content</a>

            <header id="header" class="d-flex justify-content-between" role="banner">
                <div class="d-flex">
					<button class="no-decoration" id="hamb" type="button" accesskey="p" 
							data-bs-toggle="offcanvas" data-bs-target="#component-offcanvas"
							aria-controls="component-offcanvas"
							aria-labelledby="offcanvas-progress-bar-title"
							title="Your lesson completion">
						<i class="fa-solid fa-fw fa-bars" aria-hidden="true"></i>
					</button>

                    <h1 id="lesson-name" title="Lesson name"></h1>
                </div>
                
                <div class="top-menu">
                    <button id="profile-picture" class="no-decoration px-3" type="button"
                            onclick="javascript:showMyPortraitDialog()" title="Your portrait" >
                        <img class="portrait-sm portrait-round" src="${pageLearnerPortraitSrc}" alt="Your profile portrait">
                    </button>
                    
                    <button type="button" id="progress-bar-widget" class="no-decoration d-none d-sm-none d-md-block"
                    		data-bs-toggle="offcanvas" data-bs-target="#component-offcanvas"
							aria-controls="component-offcanvas"
							aria-labelledby="offcanvas-progress-bar-title"
							title="Your lesson completion">
                        <div class="row m-0">
                            <div class="col-6 text-start p-0">
                                Progress
                            </div>
                            <div class="col-6 text-end p-0" id="progress-bar-widget-value">
                            </div>
                        </div>
                        <div class="progress mt-1 mb-2">
                            <div class="progress-bar bg-success" role="progressbar" aria-label="Progress bar widget"
                                 aria-valuemin="0" aria-valuemax="100"></div>
                        </div>
                    </button>
                    
                    <c:if test="${not isIntegrationLogin}">
                    	<a href="/" id="return-to-index" class="btn-close btn-sm float-end ms-3" title="Close and return to the course page"></a>
                    </c:if>
                </div>
            </header>

            <main class="p-3" id="component-main-content" tabindex="-1">
                <c:if test="${not empty title}">
                    <h2 class="mb-3">
                    	<span title="Activity name">
                    		<c:out value="${title}" escapeXml="true" />
                    	</span>
                    </h2>
                </c:if>
				
                <jsp:doBody/>
            </main>
        </div>
    </div>

    <div id="confirmationDialog" class="modal dialogContainer fade" tabindex="-1" role="dialog">
        <div class="modal-dialog  modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-body">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" id="confirmationDialogCancelButton">Cancel</button>
                    <button type="button" class="btn btn-primary" id="confirmationDialogConfirmButton">Confirm</button>
                </div>
            </div>
        </div>
    </div>

    <div class="toast-container position-fixed top-0 start-50 translate-middle-x p-3" id="toast-container">
    </div>

    <div id="toast-template" class="toast align-items-center bg-white" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="d-flex">
            <div class="toast-body"></div>
            <button type="button" class="btn-close me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    </div>

    </body>

</lams:html>