<%@ tag body-content="scriptless" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@ attribute name="toolSessionID" required="true" rtexprvalue="true"%>
<%@ attribute name="title" required="false" rtexprvalue="true"%>
<%@ attribute name="refresh" required="false" rtexprvalue="true"%>
<%@ attribute name="hideHeader" required="false" rtexprvalue="true"%>
<c:set var="showHeader">${empty hideHeader ? true : false }</c:set>
<%@ attribute name="hideTitle" required="false" rtexprvalue="true"%>
<c:set var="hideTitle">${empty hideTitle ? false : true }</c:set>
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
        <c:if test="${showHeader}">
	        <lams:JSImport src="learning/includes/javascript/gate-check5.js" />
	        <lams:JSImport src="includes/javascript/websocket.js" />
	        <lams:JSImport src="includes/javascript/dialog5.js" />
	        <lams:JSImport src="includes/javascript/common.js" />
        	<lams:JSImport src="learning/includes/javascript/learnerPage.js" />
	        <script>
	            const LAMS_URL = '<lams:LAMSURL/>',
	                decoderDiv = $('<div />'),
	                LABEL_CLICK_TO_OPEN = "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.click.to.open' /></spring:escapeBody>",
	                LABEL_SUPPORT_ACTIVITY = "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.support.activity' /></spring:escapeBody>",
	                LABEL_COMPLETED_ACTIVITY = "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.completed.activity' /></spring:escapeBody>",
	                LABEL_CURRENT_ACTIVITY = "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.current.activity' /></spring:escapeBody>",
	                LABEL_NOT_STARTED_ACTIVITY = "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.not.started.activity' /></spring:escapeBody>";
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
        </c:if>
    </lams:head>

    <body class="component">
	    <div class="component-page-wrapper">
	    	<c:if test="${showHeader}">
				<div class="offcanvas offcanvas-start" tabindex="-1" id="component-offcanvas" aria-label="<fmt:message key='label.side.menu' />">
					<div class="offcanvas-body">
						<button type="button" class="btn-close float-end me-1" data-bs-dismiss="offcanvas" 
								aria-label="<fmt:message key='label.close' />" 
								title="<fmt:message key='label.close' />"></button>
						
			            <span class="lams-logo mt-3">
			                <img src="<lams:LAMSURL/>images/svg/lamsv5_logo.svg" alt="<fmt:message key='label.lams.logo' />" aria-hidden="false"/>
			            </span>
					
			            <div id="offcanvas-support-bar" class="offcanvas-bar lcard d-none pb-3">
			                <div class="offcanvas-title py-3">
			                	<fmt:message key='label.support.activities'/>
			                </div>
			                <ul id="support-bar-items" class="progress-bar-items list-group text-start">
			                </ul>
			            </div>
			
						<div class="offcanvas-bar lcard pb-3 mb-3">
				            <div class="offcanvas-title py-3" id="offcanvas-progress-bar-title">
				            	<fmt:message key='label.my.progress'/>
				            </div>
				            <ul id="progress-bar-items" class="progress-bar-items list-group text-start">
				            </ul>
			            </div>
					</div>
				</div>
			</c:if>
	
			<div class="component-page-content">
	            <a href="#component-main-content" class="visually-hidden-focusable btn btn-sm btn-light p-2">
	            	<fmt:message key='label.skip.to.main.content'/>
	            </a>
	
				<c:if test="${showHeader}">
		            <header id="header" class="d-flex justify-content-between align-items-center" role="banner">
		                <div class="d-flex align-items-center">
							<button class="btn btn-light no-decoration" id="hamb" type="button" accesskey="p" 
									data-bs-toggle="offcanvas" data-bs-target="#component-offcanvas"
									aria-controls="component-offcanvas"
									aria-labelledby="offcanvas-progress-bar-title"
									title="<fmt:message key='label.your.lesson.completion'/>">
								<i class="fa-solid fa-fw fa-bars" aria-hidden="true"></i>
							</button>
		
		                    <h1 id="lesson-name" title="<fmt:message key='label.lesson.name'/>"></h1>
		                </div>
		                
		                <div class="top-menu">
		                    <button id="profile-picture" class="btn btn-light no-decoration px-3 d-none d-sm-block" type="button"
		                            onclick="javascript:showMyPortraitDialog()" title="<fmt:message key='label.your.portrait'/>" >
		                        <img class="portrait-sm portrait-round" src="${pageLearnerPortraitSrc}" alt="<fmt:message key='label.your.portrait'/>">
		                    </button>
		                    
		                    <button type="button" id="progress-bar-widget" class="btn btn-light no-decoration d-none d-sm-none d-md-block"
		                    		data-bs-toggle="offcanvas" 
		                    		data-bs-target="#component-offcanvas"
									aria-controls="component-offcanvas"
									aria-labelledby="offcanvas-progress-bar-title"
									title="<fmt:message key='label.your.lesson.completion'/>">
		                        <div class="row m-0">
		                            <div class="col-6 text-start p-0">
		                                <fmt:message key='label.progress'/>
		                            </div>
		                            <div class="col-6 text-end p-0" id="progress-bar-widget-value">%</div>
		                        </div>
		                        <div class="progress mt-1 mb-2">
		                        	<div class="progress-bar bg-success" role="progressbar"
		                            		aria-valuemin="0" 
		                        			aria-valuemax="100"></div>
		                    	</div>
		                    </button>
		                    
		                    <c:if test="${not isIntegrationLogin}">
		                    	<a href="/" id="return-to-index" class="btn btn-light btn-close btn-sm" 
		                    			title="<fmt:message key='label.close.and.return'/>"></a>
		                    </c:if>
		                </div>
		            </header>
	            </c:if>
	
	            <main class="p-3" id="component-main-content" tabindex="-1">
	                <c:if test="${not empty title && !hideTitle}">
	                    <${showHeader ? 'h2' : 'h1'} class="mb-3">
	                    	<span title="<fmt:message key='label.activity.name'/>">
	                    		<c:out value="${title}" escapeXml="true" />
	                    	</span>
	                    </${showHeader ? 'h2' : 'h1'} >
	                </c:if>
					
	                <jsp:doBody/>
	            </main>
	        </div>
	    </div>
	
		<c:if test="${showHeader}">
		    <div id="confirmationDialog" class="modal dialogContainer fade" tabindex="-1" role="dialog">
		        <div class="modal-dialog  modal-dialog-centered">
		            <div class="modal-content">
		                <div class="modal-body">
		                </div>
		                <div class="modal-footer">
		                    <button type="button" class="btn btn-secondary" id="confirmationDialogCancelButton">
		                    	<fmt:message key='label.cancel'/>
		                    </button>
		                    <button type="button" class="btn btn-primary" id="confirmationDialogConfirmButton">
		                    	<fmt:message key='label.confirm'/>
		                    </button>
		                </div>
		            </div>
		        </div>
		    </div>
		
		    <div class="toast-container position-fixed top-0 start-50 translate-middle-x p-3" id="toast-container"></div>
		
		    <div id="toast-template" class="toast align-items-center bg-white" role="alert" aria-live="assertive" aria-atomic="true">
		        <div class="d-flex">
		            <div class="toast-body"></div>
		            <button type="button" class="btn-close me-2 m-auto" data-bs-dismiss="toast" 
		            		aria-label="<fmt:message key='label.close'/>"></button>
		        </div>
		    </div>
	    </c:if>
    </body>
</lams:html>
