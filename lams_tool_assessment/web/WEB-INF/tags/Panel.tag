<% 
 /**
  * Panel.tag
  *	Author: Marcin Cieslak
  *	Description: Show a collapsible panel with decorations
  */
 %>
<%@ tag body-content="scriptless" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<c:set var="lams"><lams:LAMSURL/></c:set>

<%@ attribute name="id" required="true" rtexprvalue="true" %>
<%@ attribute name="titleKey" required="false" rtexprvalue="true" %>
<%@ attribute name="icon" required="false" rtexprvalue="true" %>
<%@ attribute name="iconClass" required="false" rtexprvalue="true" %>
<%@ attribute name="colorClass" required="false" rtexprvalue="true" %>
<%@ attribute name="expanded" required="false" rtexprvalue="true" %>

<c:set var="expanded" value="${empty expanded ? true : expanded}" />
<%-- Should left panel (icon, color) be displayed at all --%>
<c:set var="hasLeftPanel" value="${not empty icon or not empty iconClass or not empty colorClass}" />

<div class="banner-box-col d-flex" id="${id}-banner-box">
	<c:if test="${hasLeftPanel}">
		<div class="banner-box-left ${colorClass}">
			<c:choose>
				<c:when test="${not empty icon}">
					<%-- Display regular icon --%>
					<img src="${icon}">
				</c:when>
				<c:when test="${not empty iconClass}">
					<%-- Display Font Awesome icon --%>
					<i class="fa ${iconClass}" aria-hidden="true"></i>
				</c:when>
				<c:otherwise>
					<%-- Blank character for padding --%>
					<span class="no-icon" aria-hidden="true">&nbsp;</span>
				</c:otherwise>
			</c:choose>
		</div>
	</c:if>
	
	<div class="banner-box-body ${hasLeftPanel ? 'banner-box-right' : ''}">
           <div class="banner-box-title">
           	<c:set var="contentId" value="${id}-content" />
           	<a class="collapsible-link ${empty titleKey ? ' no-title' : ''}" role="button" href="#${contentId}"
           	   data-toggle="collapse" data-target="#${contentId}" aria-expanded="${expanded}" aria-controls="${contentId}">
          	   		<c:choose>
          	   			<c:when test="${not empty titleKey}">
          	   				<h2><fmt:message key="${titleKey}" /></h2>
          	   			</c:when>
          	   			<c:otherwise>
          	   				&nbsp;
          	   			</c:otherwise>
          	   		</c:choose>
           	</a>
		</div>
		
		<div id="${id}-content" class="mt-3 collapse ${expanded ? ' show' : ''}">
			<jsp:doBody />
		</div>
	</div>
</div>