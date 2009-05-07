<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<%
	String red5Url = Configuration.get(ConfigurationKeys.RED5_SERVER_URL);
	String red5RecordingsUrl = Configuration.get(ConfigurationKeys.RED5_RECORDINGS_URL);
%>

<c:if test='<%= red5Url.equals("")  || red5RecordingsUrl.equals("") %>'>
	<script type="text/javascript">	
		alert("<fmt:message key='videorecorder.error.noconfig'/>");
	</script>
</c:if>
	
<script type="text/javascript" src="${tool}includes/javascript/authoring.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/prototype.js"></script>
