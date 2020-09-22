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
<%@ attribute name="titleKey" required="true" rtexprvalue="true" %>
<%@ attribute name="iconClass" required="true" rtexprvalue="true" %>
<%@ attribute name="colorClass" required="true" rtexprvalue="true" %>

<%-- Optional attribute --%>

<div class="col-12 p-0">
	<div class="bbox-col d-flex slide_col" id="${id}-bbox">
		<div class="bbox-left icon ${colorClass}">
			<i class="fa ${iconClass}" aria-hidden="true"></i>
		</div>
		<div class="bbox-right bbox_body">
            <div class="grey_title grey_title1">
            	<a class="collapsible-link" data-toggle="collapse" href="#${id}-content" data-target="#${id}-content"
            	   role="button" aria-expanded="false" aria-controls="${id}-content">
            	   	<h2><fmt:message key="${titleKey}" /></h2>
            	</a>
			</div>
			<div id="${id}-content" class="row mt-3 collapse">
				<jsp:doBody />
			</div>
		</div>
	</div>
</div>