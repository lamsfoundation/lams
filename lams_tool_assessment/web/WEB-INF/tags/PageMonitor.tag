<%@ tag body-content="scriptless" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ tag  import="java.util.UUID"%>

<%@ attribute name="title" required="false" rtexprvalue="true"%>
<%@ attribute name="tab1Label" required="false" rtexprvalue="true"%>
<%@ attribute name="tab2Label" required="false" rtexprvalue="true"%>
<%@ attribute name="tab3Label" required="false" rtexprvalue="true"%>
<%@ attribute name="tab1Jsp" required="false" rtexprvalue="true"%>
<%@ attribute name="tab2Jsp" required="false" rtexprvalue="true"%>
<%@ attribute name="tab3Jsp" required="false" rtexprvalue="true"%>
<%@ attribute name="helpPage" required="false" rtexprvalue="true"%>
<%@ attribute name="helpToolSignature" required="false" rtexprvalue="true"%>
<%@ attribute name="extraControl" required="false" rtexprvalue="true"%>
<%@ attribute name="hideHeader" required="false" rtexprvalue="true"%>
<c:set var="showHeader">${empty hideHeader || !hideHeader ? true : false }</c:set>
<%@ attribute name="lessonID" required="false" rtexprvalue="true"%>
<%@ attribute name="initialTabId" required="false" rtexprvalue="true"%>
<c:if test="${empty initialTabId && not empty pageScope.initialTabId}">
	<c:set var="initialTabId">${pageScope.initialTabId}</c:set>
</c:if>
<c:if test="${empty tab1Label}">
	<c:set var="tab1Label">monitoring.tab.summary</c:set>
	<c:set var="tab2Label">monitoring.tab.edit.activity</c:set>
	<c:set var="tab3Label">monitoring.tab.statistics</c:set>
	
	<c:set var="tab1Jsp">summary.jsp</c:set>
	<c:set var="tab2Jsp">editactivity.jsp</c:set>
	<c:set var="tab3Jsp">statistic.jsp</c:set>
</c:if>
<c:set var="lams"><lams:LAMSURL /></c:set>

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
        <link rel="icon" type="image/x-icon" href="${lams}images/svg/lamsv5_logo.svg">

        <link rel="stylesheet" href="${lams}css/components-monitoring.css">
        <link rel="stylesheet" href="${lams}includes/font-awesome6/css/all.css">

        <script src="${lams}includes/javascript/jquery.js"></script>
        <script src="${lams}includes/javascript/jquery-ui.js"></script>
        <script src="${lams}includes/javascript/bootstrap5.bundle.min.js"></script>
        <lams:JSImport src="includes/javascript/common.js" />
        <c:if test="${showHeader}">
	        <script src="${lams}/monitoring/includes/javascript/monitor-activity.js" type="text/javascript"></script>
	        <script>
				const INITIAL_TAB_ID = "${initialTabId}",
					TOOL_CONTENT_ID = <c:out value="${param.toolContentID}" />,
					MONITORING_STATISTIC_URL = "<c:url value="/monitoring/statistic.do?sessionMapID=${sessionMapID}"/>";
	        </script>
        </c:if>
    </lams:head>

    <body class="component" <c:if test="${showHeader}">onLoad='init()'</c:if>>
    	<div class="component-page-wrapper">
			<div class="component-page-content">
            	<a href="#component-main-content" class="visually-hidden-focusable btn btn-sm btn-light p-2">
	            	<fmt:message key='label.skip.to.main.content'/>
	            </a>

				<c:if test="${showHeader}">
		            <nav class="navbar navbar-expand-md d-none">
		  				<div class="container-main">
							<button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
								<span class="navbar-toggler-icon"></span>
							</button>
							
							<div class="collapse navbar-collapse" id="navbarSupportedContent">
								 <div class="nav nav-tabs nav-justified bg-light rounded-5 w-100 py-2 me-2" id="tabs" role="tablist">
								    <button class="nav-link active" id="tab-1" data-bs-toggle="tab" data-bs-target="#tab-1-content" type="button" role="tab" 
								    		aria-controls="tab-1-content" 
								    		aria-selected="true"
								    		onclick="onSelectTab(1);">
								    	<fmt:message key="${tab1Label}" />
								    </button>
								    <button class="nav-link"		id="tab-2" data-bs-toggle="tab" data-bs-target="#tab-2-content" type="button" role="tab" 
								    		aria-controls="tab-2-content" 
								    		aria-selected="false" 
								    		onclick="onSelectTab(2);">
								    	<fmt:message key="${tab2Label}"/>
								    </button>
								    <button class="nav-link" 		id="tab-3" data-bs-toggle="tab" data-bs-target="#tab-3-content" type="button" role="tab" 
								    		aria-controls="tab-3-content" 
								    		aria-selected="false" 
								    		onclick="onSelectTab(3);">
								    	<fmt:message key="${tab3Label}"/>
								    </button>
								</div>
							</div>
			                
					        <c:if test="${not empty helpToolSignature or not empty helpPage or not empty extraControl}">
							    <ul class="navbar-nav" id="page-actions">
							        <c:if test="${not empty helpToolSignature}">
								       	<li class="nav-item d-none d-md-block">
								       		<lams:help toolSignature="${helpToolSignature}" module="monitoring" style="small"/>
								       	</li>
							        </c:if>
							        <c:if test="${not empty helpPage}">
							          	<li class="nav-item d-none d-md-block">
							           		<lams:help page="${helpPage}" style="small"/>
							           	</li>
							        </c:if>
							        <c:if test="${not empty extraControl}">
							           	<li class="nav-item">
							           		${extraControl}
							           	</li>
							        </c:if>
							             
									<li class="nav-item">
										 <a href="${lams}home/monitorLesson.do?lessonID=${param.lessonID}&reqID=<%= UUID.randomUUID().toString() %>" id="btn-close" 
										 		class="btn btn-light btn-close btn-sm nav-link" title="<fmt:message key='label.close.and.return'/>"></a>
							       	</li>
							   	</ul>
					        </c:if>
		  				</div>
		  			</nav>
	  			</c:if>

				<nav style="--bs-breadcrumb-divider: '>';" aria-label="breadcrumb">
					<div class="container-main">
						<ol class="breadcrumb">
						    <li class="breadcrumb-item">
						    	<a href="${lams}home/monitorLesson.do?lessonID=${param.lessonID}">
						    		<span id="lesson-name">
						    			
						    		</span>
						    	</a>
						    </li>
						   	
							<li class="breadcrumb-item active dropdown" aria-current="page">
								<a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button" aria-expanded="false">
									<fmt:message key="label.author.title" />
								</a>
								
							    <ul class="dropdown-menu" style="">
							      <li><a class="dropdown-item" href="#">Forum</a></li>
							      <li><a class="dropdown-item disabled lesson-name" href="#"><fmt:message key="label.author.title" /></a></li>
							      <li><a class="dropdown-item" href="#">Q&A</a></li>
							      <li><a class="dropdown-item" href="#">Another activity</a></li>
							      <li><hr class="dropdown-divider"></li>
							      <li><a class="dropdown-item" href="#">Support activity</a></li>
							    </ul>
							</li>
						</ol>
					</div>
				</nav>

	            <main id="component-main-content" class="p-3" tabindex="-1">
	            	<c:choose>
	            		<c:when test="${showHeader}">
							<div class="container-main tab-content" id="tabs-content">
								<div class="tab-pane fade show active"  id="tab-1-content" role="tabpanel" aria-labelledby="tab-1" tabindex="0"><jsp:include page="${tab1Jsp}"/></div>
								<div class="tab-pane fade" 				id="tab-2-content" role="tabpanel" aria-labelledby="tab-2" tabindex="0"><jsp:include page="${tab2Jsp}"/></div>
								<div class="tab-pane fade" 				id="tab-3-content" role="tabpanel" aria-labelledby="tab-3" tabindex="0"><jsp:include page="${tab3Jsp}"/></div>
							</div>
							
							<input type="hidden" name="currentTab" id="currentTab" />
	            		</c:when>
	            		
	            		<c:otherwise>
			                <jsp:doBody/>
	            		</c:otherwise>
	            	</c:choose>
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
		</c:if>
    </body>
</lams:html>
