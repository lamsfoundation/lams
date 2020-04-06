<%@ tag body-content="scriptless"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-function" prefix="fn" %>

<%@ attribute name="type" required="true" rtexprvalue="true"%>
<%@ attribute name="style" required="false" rtexprvalue="true"%>
<%@ attribute name="shadow" required="false" rtexprvalue="true"%>
<%@ attribute name="title" required="false" rtexprvalue="true"%>
<%@ attribute name="titleAlignment" required="false" rtexprvalue="true"%>
<%@ attribute name="titleFontSize" required="false" rtexprvalue="true"%>
<%@ attribute name="bodyText" required="false" rtexprvalue="true"%>
<%@ attribute name="bodyTextAlignment" required="false" rtexprvalue="true"%>
<%@ attribute name="bodyTextFontSize" required="false" rtexprvalue="true"%>

<%@ attribute name="icon" required="false" rtexprvalue="true"%>
<%@ attribute name="iconLink" required="false" rtexprvalue="true"%>
<%@ attribute name="footerText" required="false" rtexprvalue="true"%>


<c:if test="${empty style}">				
	<c:set var="style">info</c:set>
</c:if>

<c:if test="${empty titleAlignment}">				
	<c:set var="titleAlignment">text-left</c:set>
</c:if>

<c:if test="${empty titleFontSize}">				
	<c:set var="titleFontSize">inherit</c:set>
</c:if>

<c:if test="${empty bodyTextAlignment}">				
	<c:set var="bodyTextAlignment">text-left</c:set>
</c:if>

<c:if test="${empty bodyTextFontSize}">				
	<c:set var="bodyTextFontSize">inherit</c:set>
</c:if>

<c:if test="${empty shadow}">				
	<c:set var="shadow">shadow-none</c:set>
</c:if>

	<div class="card mb-1 ${shadow}">
	  <div class="row no-gutters">
	    <div class="col-4 d-flex  bg-${style} border border-${style} align-items-center justify-content-center" style="border-bottom-left-radius: 0.25rem; border-top-left-radius: 0.25rem;">
	      <i class="fa ${icon}" aria-hidden="true"></i>
	    </div>
	    <div class="col-8">
	      <div class="card-body m-0">
	        <div class="card-text ${titleAlignment}">${title}</div>
	        <div class="card-text ${bodyTextAlignment}" style="font-size: ${bodyTextFontSize}"><c:out value="${bodyText}" escapeXml="true" /></div>
	      </div>
	      <c:if test="{not empty footerText}">
	      <div class="card-footer">${footerText}</div>
	      </c:if>
	    </div>
	  </div>
	</div>