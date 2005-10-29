<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>

				<table class="forms">
				<tr> 
					<td class="formlabel">
          				<bean:message key="label.offlineInstructions" />:
          			</td>
					<td colspan=3 class="formcontrol">
					<FCK:editor id="richTextOfflineInstructions" basePath="/lams/fckeditor/">
						  <c:out value="${richTextOfflineInstructions}" escapeXml="false" />						  
					</FCK:editor>
					</td> 
				</tr>

				<tr> 
					<td class="formlabel">
          				<bean:message key="label.offlineFiles" />
          			</td>
          			<td colspan=3 NOWRAP> 
						<html:file  property="theOfflineFile"></html:file>
					 	<html:submit property="submitOfflineFile" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
								<bean:message key="label.upload"/>
						</html:submit>
					</td> 

				</tr>
				<tr> 
					<td align="right">
          				<bean:message key="label.uploadedOfflineFiles" />
          			</td>
					<td colspan=3>
						<c:forEach var='item' items='${sessionScope.listUploadedOfflineFileNames}'>
				            <li><c:out value='${item}'/></li>
	         			</c:forEach>
					</td> 
				</tr>
          		<tr> 
					<td align="right">
          				<bean:message key="label.onlineInstructions" />
          			</td>
					<td colspan=3 class="formcontrol">
					<FCK:editor id="richTextOnlineInstructions" basePath="/lams/fckeditor/">
						  <c:out value="${sessionScope.richTextOnlineInstructions}" escapeXml="false" />						  
					</FCK:editor>
					</td> 
				</tr>
				
				<tr> 
					<td class="formlabel">
          				<bean:message key="label.onlineFiles" />
          			</td>
          			<td colspan=3 NOWRAP> 
						<html:file  property="theOnlineFile"></html:file>
					 	<html:submit property="submitOnlineFile" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
								<bean:message key="label.upload"/>
						</html:submit>
					</td> 
				
				</tr>

				<tr>
					<td align="right">
          				<bean:message key="label.uploadedOnlineFiles" />
          			</td>
					<td colspan=3>
						<c:forEach var='item' items='${sessionScope.listUploadedOnlineFileNames}'>
				            <li><c:out value='${item}'/></li>
	         			</c:forEach>
					</td> 
				</tr>

		  		<tr>
 				 	<td colspan=4 align=center>								
						&nbsp
				  	</td>
				</tr>
 				 
 				 <tr>
				 	<td>								
				  	</td>
					 <td colspan=3> 
						 <html:submit property="instructionsTabDone" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
							<bean:message key="button.done"/>
						</html:submit>
					</td> 
 				 </tr>
 				 
			</table>	  	
