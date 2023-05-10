<%@ tag body-content="scriptless" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<lams:html>
    <lams:head>
        <title><fmt:message key="activity.title"/></title>

        <link rel="icon" type="image/x-icon" href="<lams:LAMSURL/>images/svg/lamsv5_logo.svg">
        <link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap5.custom.css">
        <link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
        <link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">

        <script src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
        <script src="<lams:LAMSURL/>includes/javascript/popper.min.js"></script>
        <script src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
        <script src="<lams:LAMSURL/>learning/includes/javascript/learnerPage.js"></script>
        <script>
            var LAMS_URL = '<lams:LAMSURL/>';
            $(document).ready(function (){
                initLearnerPage(${toolSessionID});
            });
        </script>

        <%--        <link rel="stylesheet" href="<lams:LAMSURL/>learning/css/jq.css">--%>
        <%--        <link rel="stylesheet" type="text/css" href="<lams:LAMSURL/>learning/css/style.css">--%>
        <%--        <link rel="stylesheet" type="text/css" href="<lams:LAMSURL/>learning/css/responsive-style.css">--%>
    </lams:head>

    <body class="component">
    <div class="component-page-wrapper">
        <div class="component-page-content">
            <header class="d-flex justify-content-between">
                <div class="d-flex">
                    <i id="sidebar-opener-button" class="fa-solid fa-bars pt-1"></i>
                    <p id="lesson-name"></p>
                </div>
                <div class="top-menu">

                </div>
            </header>

            <jsp:doBody/>
        </div>


            <%--
                <div class="wrapper ps-0">
                    <div class="main-content">
                        <div class="main-content-inner">
                            <header class="header d-flex justify-content-between">
                                <div class="menu-hamburger-col d-flex">
                                    <div class="hamburger">
                                        <span></span>
                                        <span></span>
                                        <span style="width: 100%;"></span>
                                    </div>
                                    <p>Lesson Name</p>
                                </div>
                                <div class="top-menu d-flex align-items-center">
                                    <div class="profile-progress">
                                        <div class="user-info-progress-col">
                                            <div class="top-progress">
                                                <p>Progress</p>
                                                <h6>20%</h6>
                                            </div>
                                            <div class="user-info-progress-bar">
                                                <div class="user-info-progress" width="50%"></div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="top-menu-btn top-menu-profile d-flex align-items-center">
                                        <a href="#"><img src="<lams:WebAppURL />images/file.png" alt="#"/></a>
                                        <a href="#"><img src="<lams:WebAppURL />images/bell.png" alt="#"/></a>
                                        <a href="#"><img src="<lams:WebAppURL />images/img1.png" alt="#"/></a>
                                    </div>
                                </div>
                            </header>
                        </div>
                    </div>
                    <div class="title-head-main">
                        <jsp:doBody/>

                        <div class="tasks-col-gate activity-btn">
                            <a href="#">
                                <button class="d-btn mb-5" type="button">Next activity</button>
                            </a>
                        </div>

                    </div>
                </div>


                <!-- Progress Bar Modal Start -->
                <div class="progress-bar-modal active">
                    <main class="main-inner">
                        <div class="col-12 bar-col">
                            <h6>Progress bar</h6>
                            <img src="<lams:WebAppURL />images/cross.png" alt="">
                        </div>

                        <section class="listing-sec">
                            <div class="container-fluid">
                                <div class="bar-inner-1x">

                                    <div class="bar-inner-2x">
                                        <div class="left-listing">
                                            <ul>
                                                <li>
                                                    <span>
                                                        <img src="<lams:WebAppURL />images/tick.png" alt="">
                                                    </span>
                                                    Introduction
                                                </li>
                                                <li>
                                                    <span>
                                                        <img src="<lams:WebAppURL />images/tick.png" alt="">
                                                    </span>
                                                    Team Setup
                                                </li>
                                                <li class="seprate-color">
                                                    <span>

                                                    </span>
                                                    Individual Readiness
                                                </li>
                                                <li class="seprate-color">
                                                    <span>

                                                    </span>
                                                    Leader Selection
                                                </li>
                                                <li class="seprate-color">
                                                    <span>

                                                    </span>
                                                    Introduction
                                                </li>
                                                <li class="seprate-color">
                                                    <span>

                                                    </span>
                                                    Team Setup
                                                </li>
                                                <li class="seprate-color">
                                                    <span>

                                                    </span>
                                                    Leader Selection
                                                </li>
                                            </ul>
                                            <div class="top-menu-btn1x d-flex">
                                                <a href="#"><img src="<lams:WebAppURL />images/down-arw.png" alt=""></a>
                                                <a href="#"><img src="<lams:WebAppURL />images/up-arw.png" alt=""></a>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="table-img">
                                        <div class="container">
                                            <div class="row align-items-center">
                                                <div class="col-md-6 col-12">
                                                    <div>
                                                        <table
                                                                class="table tablesorter tablesorter-bootstrap tablesorterff73dd4148253 hasFilters tablesorter2fb3f6e97d426 tablesorter37635a608e6c6"
                                                                role="grid">
                                                            <colgroup>
                                                                <col width="50%">
                                                                <col width="50%">
                                                            </colgroup>
                                                            <thead>
                                                            <tr role="row" class="tablesorter-headerRow table-sorter-header">
                                                                <th data-column="0"
                                                                    class="tablesorter-header tablesorter-headerDesc" tabindex="0"
                                                                    scope="col" role="columnheader" aria-disabled="false"
                                                                    unselectable="on" aria-sort="descending"
                                                                    aria-label="Name: Descending sort applied, activate to apply an ascending sort"
                                                                    style="user-select: none;" data-sortedby="user">
                                                                    <div class="tablesorter-header-inner">
                                                                        <h1>Name</h1>
                                                                    </div>
                                                                </th>
                                                                <th data-column="1"
                                                                    class="tablesorter-header tablesorter-headerUnSorted"
                                                                    tabindex="0" scope="col" role="columnheader"
                                                                    aria-disabled="false" unselectable="on" aria-sort="none"
                                                                    aria-label="Progress: No sort applied, activate to apply an ascending sort"
                                                                    style="user-select: none;">
                                                                    <div
                                                                            class="tablesorter-header-inner d-flex align-items-center gap-3">
                                                                        <h1 class="h2">Score</h1>
                                                                        <img src="<lams:WebAppURL />images/polygon1x.png" alt="">
                                                                    </div>
                                                                </th>
                                                            </tr>
                                                            </thead>
                                                            <tbody aria-live="polite" aria-relevant="all">
                                                            <tr role="row" class="tablesorter-hasChildRow table-sorter-top">
                                                                <td rowspan="1" class="table-user-name toggle">
                                                                    <img src="<lams:WebAppURL />images/1.png" alt="#">
                                                                    <p>Krystal Johnson</p>
                                                                </td>
                                                                <td class="table-user-progress">
                                                                    <p>20</p>
                                                                </td>
                                                            </tr>

                                                            <tr role="row" class="tablesorter-hasChildRow table-sorter-top">
                                                                <td rowspan="1" class="table-user-name toggle">
                                                                    <img src="<lams:WebAppURL />images/2.png" alt="#">
                                                                    <p>Joel Bryant</p>
                                                                </td>
                                                                <td class="table-user-progress">
                                                                    <p>18</p>
                                                                </td>
                                                            </tr>

                                                            <tr role="row" class="tablesorter-hasChildRow table-sorter-top">
                                                                <td rowspan="1" class="table-user-name toggle">
                                                                    <img src="<lams:WebAppURL />images/3.png" alt="#">
                                                                    <p>Alex Burns</p>
                                                                </td>
                                                                <td class="table-user-progress">
                                                                    <p>16</p>
                                                                </td>
                                                            </tr>

                                                            <tr role="row" class="tablesorter-hasChildRow table-sorter-top">
                                                                <td rowspan="1" class="table-user-name toggle">
                                                                    <img src="<lams:WebAppURL />images/4.png" alt="#">
                                                                    <p>Caroline Scott</p>
                                                                </td>
                                                                <td class="table-user-progress">
                                                                    <p>15</p>
                                                                </td>
                                                            </tr>

                                                            <tr role="row" class="tablesorter-hasChildRow table-sorter-top">
                                                                <td rowspan="1" class="table-user-name toggle">
                                                                    <img src="<lams:WebAppURL />images/5.png" alt="#">
                                                                    <p>Derek Lewis</p>
                                                                </td>
                                                                <td class="table-user-progress">
                                                                    <p>14</p>
                                                                </td>
                                                            </tr>

                                                            <tr role="row" class="tablesorter-hasChildRow table-sorter-top">
                                                                <td rowspan="1" class="table-user-name toggle">
                                                                    <img src="<lams:WebAppURL />images/6.png" alt="#">
                                                                    <p>Frank Hart</p>
                                                                </td>
                                                                <td class="table-user-progress">
                                                                    <p>12</p>
                                                                </td>
                                                            </tr>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div class="col-md-6 col-12">
                                                    <div class="table-graph">
                                                        <img src="<lams:WebAppURL />images/table-graph.png" alt="">
                                                    </div>
                                                </div>
                                                <div class="col-12">
                                                    <div class="info-icon1x">
                                                        <div class="info-icon2x">
                                                            <img src="<lams:WebAppURL />images/info-icon.png" alt="">
                                                        </div>
                                                        <div class="info-icon3x">
                                                            <p>Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam
                                                                nonumy
                                                                eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed
                                                                diam
                                                                voluptua. At vero eos et accusam et justo duo dolores et ea rebum.
                                                                Stet
                                                                clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor
                                                                sit
                                                                amet.</p>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </section>
                    </main>

                </div>
                <!-- Progress Bar Modal End -->

                <script type="text/javascript">
                    $(document).ready(function () {
                        $('.hamburger, .progress-bar-modal .bar-col img').click(function () {
                            $('.progress-bar-modal').toggleClass('active');
                        });
                    });
                </script>
                <script type="text/javascript">
                    $('.profile-forum').hover(function () {
                        $(this).css('z-index', 2).css('position', 'relative');
                    }, function () {
                        $(this).css('z-index', 0).css('position', 'relative');
                    });
                </script>
            --%>
    </div>
    </body>

</lams:html>