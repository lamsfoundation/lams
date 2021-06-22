<%-- JS import directive that changes with each LAMS server version so the file does not get cached --%>
<%@ tag body-content="scriptless"%>
<%@ taglib uri="tags-core" prefix="c" %>

<%@ attribute name="src" required="true" rtexprvalue="true" %>
<%@ attribute name="relative" required="false" rtexprvalue="true"%>

<%@ tag  import="org.lamsfoundation.lams.util.Configuration"%>
<%@ tag  import="org.lamsfoundation.lams.util.ConfigurationKeys"%>

<c:set scope="request" var="importUrlPrefix" value="/lams/" />
<c:if test="${relative}">
	<c:set scope="request" var="importUrlPrefix"><lams:WebAppURL /></c:set>
</c:if>

<script src="${importUrlPrefix}${src}?v=<%=Configuration.get(ConfigurationKeys.VERSION).hashCode()%>"></script>