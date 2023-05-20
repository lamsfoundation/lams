<%@ tag body-content="scriptless" %>
<%@ attribute name="toolSessionID" required="true" rtexprvalue="true"%>
<%@ attribute name="title" required="false" rtexprvalue="true"%>

<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:set var="lams">
    <lams:LAMSURL />
</c:set>

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
        <script src="<lams:LAMSURL/>includes/javascript/popper.min.js"></script>
        <script src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
        <lams:JSImport src="learning/includes/javascript/gate-check5.js" />
        <lams:JSImport src="learning/includes/javascript/learnerPage.js" />

        <script>
            var LAMS_URL = '<lams:LAMSURL/>';
            $(document).ready(function (){
                initLearnerPage(${toolSessionID});
            });
        </script>
    </lams:head>

    <body class="component">
    <div class="component-page-wrapper">
        <div class="component-page-content">
            <a href="#component-main-content" class="visually-hidden-focusable">Skip to main content</a>

            <header class="d-flex justify-content-between" role="banner">
                <div class="d-flex">
                    <button class="sidebar-toggle-button no-decoration"
                            data-closed-class="fa-bars" data-opened-class="fa-bars-staggered"
                            aria-labelledby="progress-bar-title" aria-expanded="false">
                        <i class="fa-solid fa-fw fa-bars" aria-hidden="true"></i>
                    </button>
                    <h1 id="lesson-name"></h1>
                </div>
                <div class="top-menu">
                </div>
            </header>

            <main class="m-3" id="component-main-content">
                <c:if test="${not empty title}">
                    <h3 class="mb-3"><c:out value="${title}" escapeXml="true" /></h3>
                </c:if>

                <jsp:doBody/>
            </main>


        </div>

        <nav class="component-sidebar" aria-label="Side menu" aria-expanded="false">
            <button class="sidebar-toggle-button no-decoration" aria-labelledby="progress-bar-title">
                <i class="fa-solid fa-xmark"></i>
            </button>
            <a href="/" title="Return to index page" class="lams-logo" role="navigation">
                <img src="<lams:LAMSURL/>images/svg/lamsv5_logo.svg" alt="LAMS logo" aria-hidden="true"/>
            </a>
            <h6 class="sidebar-title"><i class="fa-solid fa-bars-progress"></i>&nbsp;<span id="progress-bar-title">Progress bar</span></h6>
            <ul id="progress-bar-items">
            </ul>
        </nav>


    </div>
    </body>

</lams:html>