<%@ include file="/includes/taglibs.jsp" %>
<%@ include file="/includes/messages.jsp" %>
<%@ taglib uri="datetime" prefix="date" %>
<%@ taglib uri="forum" prefix="fm"%>
<html>
<head>
    <%-- <title><bean:message key="message.title.open"/></title> --%>
</head>

<body>
 <table width="100%"  class="displayHeaderCell">
    <tr>
		<td><bean:write name="message" property="subject"/></td>
		<td><date:format pattern="dd/MM/yy hh:mm a"><bean:write name="message" property="created.time"/></date:format></td>
        <bean:define id="topicId" name="message" property="id" />
        <td><a href="/forum/learning/message/post.do?topicId=<%= topicId %>&parentId=<%= topicId%>"><b><bean:message key="message.link.reply"/></b></a></td>
	</tr>
	<tr class="displayHeaderCell">
		<td colspan="3"><bean:write name="message" property="body"/></td>
	</tr>
 </table>
<br/><br/>

<logic:present name="message" property="replies">
 <bean:define id="replies" name="message" property="replies" type="java.util.Set"/>
    <fm:messagemap replies="<%=replies %>" ordered="false" topicId="<%= topicId.toString() %>" path="" />
 </logic:present>

<br/><br/>

</body>
</html>
