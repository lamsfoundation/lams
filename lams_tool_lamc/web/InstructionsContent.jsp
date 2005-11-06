<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>

				<table class="forms">
				<tr> 
					<td class="formlabel" valign=top>
          				<bean:message key="label.offlineInstructions" />:
          			</td>
					<td colspan=3 class="formcontrol" valign=top>
					<FCK:editor id="richTextOfflineInstructions" basePath="/lams/fckeditor/">
						  <c:out value="${richTextOfflineInstructions}" escapeXml="false" />						  
					</FCK:editor>
					</td> 
				</tr>

				<tr> 
					<td class="formlabel" valign=top>
          				<bean:message key="label.offlineFiles" />
          			</td>
          			<td colspan=3 NOWRAP valign=top> 
						<html:file  property="theOfflineFile"></html:file>
					 	<html:submit property="submitOfflineFile" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
								<bean:message key="label.upload"/>
						</html:submit>
					</td> 

				</tr>
				<tr> 
					<td align="right" valign=top>
          				<bean:message key="label.uploadedOfflineFiles" />
          			</td>
					<td colspan=3 align=left valign=top width="100%">
						<table align="left" border="1">
							<tr>
									<c:forEach var='item' items='${sessionScope.listUploadedOfflineFileNames}'>
								         <td valign=top><font size=2> <b> <c:out value='${item}'/> </b> </font> </td>
								      	<td valign=top>
								      		<img src="images/edit.gif" align=left onclick="javascript:document.forms[0].fileItem.value='<c:out value='${item}'/>' ; document.forms[0].viewFileItem.value=1; document.forms[0].submit();">	  										  		
								      	</td>
				         			</c:forEach>
		         			</tr>
	         			</table>
					</td> 
				</tr>
          		<tr> 
					<td align="right" valign=top>
          				<bean:message key="label.onlineInstructions" />
          			</td>
					<td colspan=3 class="formcontrol" valign=top>
					<FCK:editor id="richTextOnlineInstructions" basePath="/lams/fckeditor/">
						  <c:out value="${sessionScope.richTextOnlineInstructions}" escapeXml="false" />						  
					</FCK:editor>
					</td> 
				</tr>
				
				<tr> 
					<td class="formlabel" valign=top>
          				<bean:message key="label.onlineFiles" />
          			</td>
          			<td colspan=3 NOWRAP valign=top> 
						<html:file  property="theOnlineFile"></html:file>
					 	<html:submit property="submitOnlineFile" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
								<bean:message key="label.upload"/>
						</html:submit>
					</td> 
				</tr>

				<tr>
					<td align="right" valign=top>
          				<bean:message key="label.uploadedOnlineFiles" />
          			</td>
          			
					<td colspan=3 align=left valign=top width="100%">
						<table align="left" border="1">
							<tr>
									<c:forEach var='item' items='${sessionScope.listUploadedOnlineFileNames}'>
								         <td valign=top><font size=2> <b> <c:out value='${item}'/> </b> </font> </td>
								      	<td valign=top>
								      		<img src="images/edit.gif" align=left onclick="javascript:document.forms[0].fileItem.value='<c:out value='${item}'/>' ; document.forms[0].viewFileItem.value=1; document.forms[0].submit();">	  										  		
								      	</td>
				         			</c:forEach>
		         			</tr>
	         			</table>
					</td> 
				</tr>

				<html:hidden property="fileItem"/>
				<html:hidden property="viewFileItem"/>


		  		<tr>
 				 	<td colspan=4 align=center valign=top>								
						&nbsp
				  	</td>
				</tr>
 				 
 				 <tr>
				 	<td valign=top>								
				  	</td>
					 <td colspan=3 valign=top> 
						 <html:submit property="instructionsTabDone" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
							<bean:message key="button.done"/>
						</html:submit>
					</td> 
 				 </tr>
 				 
			</table>	  	
