<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<div id="datatablecontainer">
<logic:messagesPresent> 
<table border="0" cellspacing="2" cellpadding="2" summary="This table is being used for layout purposes only">
<tr>
	<td width="10%"  align="right" >
		<img src="<lams:LAMSURL/>/images/error.jpg" alt="Error occured"/>
	</td>
	<td width="90%" valign="middle" class="body" colspan="2">
		 <html:messages id="error" message="false"> 
			 <c:out value="${error}" escapeXml="false"/><BR>
		 </html:messages> 
	</td>
</tr>
</logic:messagesPresent>
</div>

