<% 
 /**
  * Switch.tag
  *	Author: Marcin Cieslak
  *	Description: Displays a row with a label and a styled checkbox
  */
 %>
<%@ tag body-content="scriptless" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>


<%@ attribute name="labelKey" required="true" rtexprvalue="true" %>

<%@ attribute name="name" required="false" rtexprvalue="true" %>
<%@ attribute name="checked" required="false" rtexprvalue="true" %>
<%@ attribute name="id" required="false" rtexprvalue="true" %>
<%@ attribute name="useSpringForm" required="false" rtexprvalue="true" %>
<%@ attribute name="inputCellClass" required="false" rtexprvalue="true" %>
<%@ attribute name="iconClass" required="false" rtexprvalue="true" %>
<%@ attribute name="tooltipKey" required="false" rtexprvalue="true" %>
<%@ attribute name="tooltipDescriptionKey" required="false" rtexprvalue="true" %>

<c:set var="checked" value="${empty checked ? false : checked}" />
<c:set var="id" value="${empty id ? name : id}" />
<c:set var="useSpringForm" value="${empty useSpringForm ? false : useSpringForm}" />

<lams:Input id="${id}" labelKey="${labelKey}" inputCellClass="d-flex justify-content-end switch switch-sm ${inputCellClass}"
			iconClass="${iconClass}" tooltipKey="${tooltipKey}" tooltipDescriptionKey="${tooltipDescriptionKey}">
    <c:choose>
   		<c:when test="${useSpringForm}">
   			<form:checkbox path="${name}" id="${id}" cssClass="switch" />
   		</c:when>
   		<c:otherwise>
			<input type="checkbox" class="switch" id="${id}" ${checked ? ' checked' : ''}
				<c:if test="${not empty name}">
					name="${name}"
				</c:if>
			>
   		</c:otherwise>
   	</c:choose>
	
	<label for="${id}" />
</lams:Input>			