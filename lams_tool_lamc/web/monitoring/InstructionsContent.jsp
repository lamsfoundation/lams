<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-c" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

		<table class="forms">
			<tr>
				<td>
					<table align="center">
						<tr> 
							<td NOWRAP valign=top>
				  				<b> <font size=2> <bean:message key="label.offlineInstructions.col" /> </font> </b>
				  			</td>
							<td NOWRAP valign=top>
								  <font size=2> <c:out value="${richTextOfflineInstructions}" escapeXml="false" />	</font>					  
							</td> 
						</tr>
						
						<tr>
							<td colspan=2> &nbsp&nbsp&nbsp </td>
						</tr>
						<tr>
							<td colspan=2> &nbsp&nbsp&nbsp </td>
						</tr>
				
						<tr> 
							<td NOWRAP valign=top>
				  				<b> <font size=2> <bean:message key="label.onlineInstructions.col" /> </font></b>
				  			</td>
							<td NOWRAP valign=top>
								  <font size=2> <c:out value="${sessionScope.richTextOnlineInstructions}" escapeXml="false" />	</font>					  
							</td> 
						</tr>
					</table>
				</td>
			</tr>
		</table>

