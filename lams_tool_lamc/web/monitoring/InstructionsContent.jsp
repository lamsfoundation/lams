<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-c" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

		<table align="left">
			<tr> 
				<td NOWRAP class="formlabel" valign=top>
	  				<b> <bean:message key="label.offlineInstructions.col" /> </b>
	  			</td>
				<td NOWRAP class="formlabel" valign=top>
					  <c:out value="${richTextOfflineInstructions}" escapeXml="false" />						  
				</td> 
			</tr>
			
			<tr>
				<td colspan=2> &nbsp&nbsp&nbsp </td>
			</tr>
			<tr>
				<td colspan=2> &nbsp&nbsp&nbsp </td>
			</tr>
	
			<tr> 
				<td NOWRAP class="formlabel" valign=top>
	  				<b> <bean:message key="label.onlineInstructions.col" /> </b>
	  			</td>
				<td NOWRAP class="formlabel" valign=top>
					  <c:out value="${sessionScope.richTextOnlineInstructions}" escapeXml="false" />						  
				</td> 
			</tr>
		</table>

