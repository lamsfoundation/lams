<% 
 /**
  * PageComponent.tag
  *	Author: Marcin Cieslak
  *	Description: Wrap contents in a standardised page template
  */
 %>
<%@ tag body-content="scriptless" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<%@ attribute name="titleKey" required="false" rtexprvalue="true" %>

<div class="wrapper d-flex">
	<div class="main-content">
		<div class="main-content-inner">
            <a href="#content" class="sr-only sr-only-focusable">Skip to main content</a>
            
            <c:if test="${not empty titleKey}">
				<header class="header d-flex justify-content-between">
					<div class="header-col d-flex">
						<h1><fmt:message key="${titleKey}" /></h1>
					</div>
				</header>
			</c:if>
			
			<jsp:doBody />
			
		</div>
	</div>
</div>