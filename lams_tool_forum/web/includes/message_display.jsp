<%@page language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<logic:present name="messageKey" scope="request">
<bean:define id="messageKey" name="messageKey" type="String" />
<div align="center" class="message"><bean:message key="<%= messageKey %>" /></div>
</logic:present>
