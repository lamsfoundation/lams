<% 
 /**
  * Dropdown.tag
  *	Author: Marcin Cieslak
  *	Description: Displays a row with a label and a styled select widget
  */
 %>
<%@ tag body-content="scriptless" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ attribute name="name" required="true" rtexprvalue="true" %>
<%@ attribute name="labelKey" required="true" rtexprvalue="true" %>

<%@ attribute name="id" required="false" rtexprvalue="true" %>
<%@ attribute name="useSpringForm" required="false" rtexprvalue="true" %>
<%@ attribute name="tooltipKey" required="false" rtexprvalue="true" %>
<%@ attribute name="tooltipDescriptionKey" required="false" rtexprvalue="true" %>

<c:set var="id" value="${empty id ? name : id}" />
<c:set var="useSpringForm" value="${empty useSpringForm ? false : useSpringForm}" />

<lams:Input id="${id}" labelKey="${labelKey}" tooltipKey="${tooltipKey}" tooltipDescriptionKey="${tooltipDescriptionKey}">
	<c:choose>
   		<c:when test="${useSpringForm}">
   			<form:select path="${name}" id="${id}" cssClass="form-control form-control-select" aria-label="<fmt:message key='${tooltipKey}' />">
   				<jsp:doBody />
   			</form:select>
   		</c:when>
   		<c:otherwise>
   			<select name="${name}" id="${id}" class="form-control form-control-select" >
           		<jsp:doBody />
       		</select>
   		</c:otherwise>
   	</c:choose>
</lams:Input>