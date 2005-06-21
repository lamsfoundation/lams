<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>


		<tr> <td>
			<table>
          		<tr>
          			<td>
          				<fmt:message key="label.offlineInstructions" />
          			</td>
          			<td>
          				<html:textarea property="offlineInstructions" rows="5" cols="50"/>
          			</td>
          		</tr>
          		<tr>
          			<td>
          				<fmt:message key="label.onlineInstructions" />
          			</td>
          			<td>
          				<html:textarea property="onlineInstructions"  rows="5" cols="50"/>
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
		</td>
		</tr>