<%@ tag body-content="scriptless"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-function" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@ attribute name="type" required="false" rtexprvalue="true"%>
<%@ attribute name="formID" required="false" rtexprvalue="true"%>
<%@ attribute name="title" required="false" rtexprvalue="true"%>
<%@ attribute name="titleHelpURL" required="false" rtexprvalue="true"%>
<%@ attribute name="breadcrumbItems" required="false" rtexprvalue="true"%>
 
<body class="component pb-4 pt-2 px-2 px-sm-4">
	<div class="container-fluid" style="max-width: 1600px">
		<a href="#content" class="sr-only sr-only-focusable visually-hidden visually-hidden-focusable">Skip to main content</a>
	
		<%-- The Breadcrumb
			We need to build the breadcrumb in the tag rather than on the page jsp as it is easier to do 
			accessibility this way. Also ensure that all pages implement the same breadcrumb element
					
			BreadcrumbItems is an array that includes the breadcrumb details as follows
					
			String[] breadcrumbItem = ["breadcrumbURL1|breadcrumbLabel1", "breadcrumbURL2|breadcrumbLabel2",...]
					
			In the page, you build these as follows:
					
				<c:set var="breadcrumbTop"><lams:LAMSURL/>admin/sysadminstart.do | <fmt:message key="sysadmin.maintain" /></c:set>
				<c:set var="breadcrumbActive">. | <fmt:message key="admin.timezone.title"/></c:set>
				<c:set var="breadcrumbItems" value="${breadcrumbTop}, ${breadcrumbActive}"/>
					
			Then you pass breadcrumbItems to the Page.tag as:
			
			<lams:Page5 type="admin" title="${title}" titleHelpURL="${help}" breadcrumbItems="${breadcrumbItems}" formID="timezoneForm">
		--%>
	
		<header>
			<c:if test="${not empty breadcrumbItems}">
				<nav aria-label="breadcrumb" role="navigation">
					<ol class="breadcrumb">
						<c:forEach var="breadcrumbItem" items="${breadcrumbItems}">
							<c:forEach var="item" items="${breadcrumbItem}">
								<!--  <p>${breadcrumbItem.toString()}</p> -->
								<c:set var="bURL">${fn:trim(fn:split(item,'|')[0])}</c:set>
								<c:set var="bLabel">${fn:trim(fn:split(item,'|')[1])}</c:set>
								
								<c:choose>
									<c:when test="${bURL eq '.'}">
										<li class="breadcrumb-item active" aria-current="page">
											${bLabel}
										</li>
									</c:when>
									<c:otherwise>
										<li class="breadcrumb-item">
											<a href="${bURL}">${bLabel}</a>
										</li>	
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</c:forEach>
					</ol>
				</nav>
			</c:if>
		</header>
				
		<c:if test="${not empty title}">
			<c:if test="${not empty titleHelpURL}">
				<span class="float-end">${titleHelpURL}</span>
			</c:if>
		</c:if>
				
		<div id="content">
			<main role="main">
				<c:if test="${not empty title}">
					<h2>
						<c:out value="${title}" escapeXml="true" />
					</h2>
				</c:if>
				
				<jsp:doBody />
			</main>
		</div>
	</div>
</body>
