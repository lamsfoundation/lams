<%-- JS import directive that changes with each LAMS server version so the file does not get cached --%>
<%@ tag body-content="scriptless" dynamic-attributes='dynamicAttributesVar' %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<%@ attribute name="titleKey" required="false" rtexprvalue="true" %>

<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/popover-tag.js"></script>

<%-- Generate a random ID for each popover --%>
<c:set var="popoverId">lams-popover-<%= java.lang.Math.round(java.lang.Math.random() * 1000) %></c:set>

<a class="lams-popover" id="${popoverId}" style="color: #777; text-decoration: none;"
   <%-- Add any other Boostrap Popover attributes that have been added to the tage --%>
   <c:forEach items="${dynamicAttributesVar}" var="att"> 
   		${att.key} = '${att.value}' 
   </c:forEach> 
></a>

<%-- Content is provided in a separate, hidden box next to the popover icon--%>
<span id="${popoverId}-content" class="lams-popover-content hidden">
    <%-- Set up title, if any --%>
   	<c:if test="${not empty titleKey}">
   		<span class="lams-popover-title"><fmt:message key="${titleKey}" /></span>
   	</c:if>
   	 <%-- Set up content, if any --%>
	<span class="lams-popover-body">
		<jsp:doBody />
	</span>
</span>