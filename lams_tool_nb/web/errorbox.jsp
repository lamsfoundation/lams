<%@ include file="/includes/taglibs.jsp"%>

<logic:messagesPresent>
	<table cellpadding="0">
		<tr>
			<td width="10%" align="right">
				<img src="<lams:LAMSURL/>/images/error.jpg" alt="Error occured" />
			</td>
			<td width="90%" valign="middle" class="body">
				<html:messages id="error" message="false">
					<c:out value="${error}" escapeXml="false" />
					<BR>
				</html:messages>
			</td>
		</tr>
	</table>
</logic:messagesPresent>
