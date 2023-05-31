<%@ tag body-content="scriptless" %>
<%@ attribute name="toolSessionID" required="true" rtexprvalue="true"%>
<%@ attribute name="title" required="false" rtexprvalue="true"%>

<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

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

        <link rel="icon" type="image/x-icon" href="<lams:LAMSURL/>images/svg/lamsv5_logo.svg">
        <link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap5.custom.css">
        <link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
        <link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
        <link rel="stylesheet" href="<lams:LAMSURL/>learning/css/components-learner.css">

        <script src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
        <script src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
        <script src="<lams:LAMSURL/>includes/javascript/popper.min.js"></script>
        <script src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
        <lams:JSImport src="learning/includes/javascript/gate-check5.js" />
        <lams:JSImport src="learning/includes/javascript/learnerPage.js" />
        <lams:JSImport src="includes/javascript/dialog5.js" />

        <script>
            const LAMS_URL = '<lams:LAMSURL/>',
                decoderDiv = $('<div />');

            $(document).ready(function (){
                initLearnerPage(${toolSessionID});
            });
        </script>
    </lams:head>

    <body class="component">
    <div class="component-page-wrapper">
        <nav inert class="component-sidebar" role="navigation dialog"
             aria-label="Side menu" aria-expanded="false"  aria-modal="true">
            <button class="sidebar-toggle-button no-decoration" aria-labelledby="progress-bar-title">
                <i class="fa-solid fa-xmark"></i>
            </button>
            <a href="/" title="Return to course page" class="lams-logo">
                <img src="<lams:LAMSURL/>images/svg/lamsv5_logo.svg" alt="LAMS logo" aria-hidden="true"/>
            </a>

            <div id="support-bar" class="d-none pb-4 w-100">
                <span class="sidebar-title"><i class="fa-solid fa-toolbox sidebar-title-icon"></i>&nbsp;<span tabindex="0" id="support-bar-title">Support activities</span></span>
                <ul id="support-bar-items" class="progress-bar-items" role="menu">
                </ul>
            </div>

            <span class="sidebar-title"><i class="fa-solid fa-fw fa-bars-progress sidebar-title-icon"></i>&nbsp;<span tabindex="0" id="progress-bar-title">Progress bar</span></span>
            <ul id="progress-bar-items" class="progress-bar-items w-100" role="menu">
            </ul>
        </nav>

        <div class="component-page-content">
            <a href="#component-main-content" class="visually-hidden-focusable p-2">Skip to main content</a>

            <header id="header" class="d-flex justify-content-between" role="banner">
                <div class="d-flex">
                    <button class="sidebar-toggle-button no-decoration" accesskey="p" id="hamb"
                            data-closed-class="fa-bars" data-opened-class="fa-bars-staggered"
                            aria-labelledby="progress-bar-title" aria-expanded="false">
                        <i class="fa-solid fa-fw fa-bars" aria-hidden="true"></i>
                    </button>
                    <h1 id="lesson-name"></h1>
                </div>
                <div class="top-menu">
                    <div id="progress-bar-widget" title="Your lesson completion" class="d-none d-sm-none d-md-block">
                        <div class="row">
                            <div class="col-6">
                                Progress
                            </div>
                            <div class="col-6 text-end" id="progress-bar-widget-value">
                            </div>
                        </div>
                        <div class="progress mt-1 mb-2">
                            <div class="progress-bar bg-success" role="progressbar" aria-label="Progress bar widget"
                                 aria-valuemin="0" aria-valuemax="100"></div>
                        </div>
                    </div>
                    <button id="profile-picture" class="no-decoration px-3 py-1" type="button"
                            onclick="javascript:showMyPortraitDialog()">
                        <img class="portrait-sm portrait-round" src="${pageLearnerPortraitSrc}" aria-label="<c:out value="${pageLearnerFirstName} ${pageLearnerLastName}" escapeXml="true"/> profile picture" alt="User profile picture">
                    </button>
                </div>
            </header>

            <main class="p-3" id="component-main-content" tabindex="-1">
                <c:if test="${not empty title}">
                    <h2 class="mb-3"><c:out value="${title}" escapeXml="true" /></h2>
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