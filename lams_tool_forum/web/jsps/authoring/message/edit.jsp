<%@ include file="/includes/taglibs.jsp" %>
<script type="text/javascript" src="<html:rewrite page='/includes/javascript/xmlrequest.js'/>"></script>


<html:errors property="error" />
<div align="center">
<html:form action="/authoring/updateTopic.do" focus="message.subject"
	 enctype="multipart/form-data">
	<fieldset>
		<input type="hidden" name="topicIndex" value="<c:out value="${topicIndex}"/>">
		<%@ include file="/jsps/message/topiceditform.jsp" %>
	 </fieldset>
 </html:form>
</div>
