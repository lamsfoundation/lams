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
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ attribute name="name" required="true" rtexprvalue="true" %>
<%@ attribute name="labelKey" required="true" rtexprvalue="true" %>

<%@ attribute name="id" required="false" rtexprvalue="true" %>
<%@ attribute name="useSpringForm" required="false" rtexprvalue="true" %>
<%@ attribute name="tooltipKey" required="false" rtexprvalue="true" %>
<%@ attribute name="tooltipDescriptionKey" required="false" rtexprvalue="true" %>

<c:set var="id" value="${empty id ? name : id}" />
<c:set var="useSpringForm" value="${empty useSpringForm ? false : useSpringForm}" />

<div class="form-group row">
	<label class="col-sm-8 col-form-label" for="${id}">
		<fmt:message key="${labelKey}" />
		
		<c:if test="${not empty tooltipKey}">
			<a tabindex="0" role="button" data-toggle="tooltip" title="<fmt:message key='${tooltipKey}' />">
				<i class="info_icon fa fa-info-circle text-info fa-fw"
					<c:if test="${not empty tooltipDescriptionKey}">
						aria-label="<fmt:message key="${tooltipDescriptionKey}" />"
					</c:if>
				></i>
			</a>
		</c:if>
	</label>
	
    <div class="col-sm-4 justify-content-end d-flex">
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
    </div>
</div>