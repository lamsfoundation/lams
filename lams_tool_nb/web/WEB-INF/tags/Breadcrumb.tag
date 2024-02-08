<% 
 /**
  * Breadcrumb.tag
  *	Author: Marcin Cieslak
  *	Description: Displays a row with a label and a styled select widget
  *	
  *	 
  * We need to build the breadcrumb in the tag rather than on the page jsp as it is easier to do 
  * accessibility this way. Also ensure that all pages implement the same breadcrumb element
  * 				
  * BreadcrumbItems is an array that includes the breadcrumb details as follows
  * 				
  * String[] breadcrumbItem = ["breadcrumbURL1|breadcrumbLabel1", "breadcrumbURL2|breadcrumbLabel2",...]
  * 				
  * In the page, you build these as follows:
  * 
  * 	<c:set var="breadcrumbTop"><lams:LAMSURL/>admin/sysadminstart.do | <fmt:message key="sysadmin.maintain" /></c:set>
  * 	<c:set var="breadcrumbActive">. | <fmt:message key="admin.timezone.title"/></c:set>
  * 	<c:set var="breadcrumbItems" value="${breadcrumbTop}, ${breadcrumbActive}"/>
  * 				
  * Then you pass breadcrumbItems to the Page.tag as:
  * 				
  * <lams:Page5 type="admin" title="${title}" titleHelpURL="${help}" breadcrumbItems="${breadcrumbItems}" formID="timezoneForm">
*/
 %>
<%@ tag body-content="scriptless" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-function" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ attribute name="breadcrumbItems" required="true" rtexprvalue="true" %>

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
							<li class="breadcrumb-item active" aria-current="page">${bLabel}</li>
						</c:when>
						<c:otherwise>
							<li class="breadcrumb-item"><a href="${bURL}">${bLabel}</a></li>	
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</c:forEach>
		</ol>
	</nav>
</c:if>
