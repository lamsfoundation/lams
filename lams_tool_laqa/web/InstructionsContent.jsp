<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>

<div id="richTextContainer">
		<tr> <td>
			<table>
				<tr> 
					<td>
          				<fmt:message key="label.offlineInstructions" />
          			</td>
					<td NOWRAP width=700>
					<FCK:editor id="richTextOfflineInstructions" basePath="/lams/fckEditor/"
					      height="200"
						  width="100%">
						  <c:out value="${sessionScope.richTextOfflineInstructions}" escapeXml="false" />						  
					</FCK:editor>
					</td> 
				</tr>
				
          		<tr> 
          			<td>
          				<fmt:message key="label.onlineInstructions" />
          			</td>
					<td NOWRAP width=700>
					<FCK:editor id="richTextOnlineInstructions" basePath="/lams/fckEditor/"
					      height="200"
						  width="100%">
  						  <c:out value="${sessionScope.richTextOnlineInstructions}" escapeXml="false" />						  
					</FCK:editor>
					</td> 
				</tr>
          		</table>	  	

			  	<hr>
				<table>
	          		<tr>
						 <td colspan=2> 
							 <html:submit property="submitTabDone" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
								<bean:message key="button.done"/>
							</html:submit>
						</td> 
					</tr>
				</table>

		</td></tr>
</div>