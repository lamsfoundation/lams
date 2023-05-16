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
        <title><fmt:message key="activity.title"/></title>

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

                $('.component-page-wrapper .sidebar-toggle-button').click(function () {
                    let topToggleButton = $('.component-page-wrapper .component-page-content > header .sidebar-toggle-button');
                    topToggleButton.toggleClass(topToggleButton.data('closed-class')).toggleClass(topToggleButton.data('opened-class'));
                    $('.component-page-wrapper .component-sidebar').toggleClass('active');
                });
            });
        </script>
    </lams:head>

    <body class="component">
    <div class="component-page-wrapper">
        <div class="component-page-content">
            <header class="d-flex justify-content-between">
                <div class="d-flex">
                    <i class="sidebar-toggle-button fa-solid fa-fw fa-bars pt-1"
                       data-closed-class="fa-bars" data-opened-class="fa-bars-staggered"></i>
                    <p id="lesson-name"></p>
                </div>
                <div class="top-menu">
                </div>
            </header>

            <div class="card">
                <c:if test="${not empty title}">
                    <div class="card-header">
                        <h5><c:out value="${title}" escapeXml="true" /></h5>
                    </div>
                </c:if>

                <div class="card-body">
                    <main>
                        <jsp:doBody/>
                    </main>
                </div>
            </div>


        </div>

        <!-- Progress Bar Modal Start -->
        <div class="component-sidebar">
            <i class="fa-solid fa-xmark sidebar-toggle-button"></i>
            <a href="/" title="Return to index page" class="lams-logo">
                <img src="<lams:LAMSURL/>images/svg/lamsv5_logo.svg" alt="LAMS logo"/>
            </a>
            <h6 class="sidebar-title"><i class="fa-solid fa-bars-progress"></i>&nbsp;Progress bar</h6>
            <ul id="progress-bar-items">
            </ul>
        </div>


    </div>
    </body>

</lams:html>