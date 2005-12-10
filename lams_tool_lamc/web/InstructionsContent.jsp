<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>

<script language="JavaScript" type="text/JavaScript">
<!--

// view a particular item in a file
// questionIndexValue: index of question affected
// actionMethod: name of the method to be called in the DispatchAction
function viewFileItem(fileItemValue, actionMethod) {
	document.McAuthoringForm.fileItem.value=fileItemValue; 
	submitMethod(actionMethod);
}

function deleteFileItem(fileItemValue, actionMethod, offlineFile) {
	document.McAuthoringForm.fileItem.value=fileItemValue; 
	document.McAuthoringForm.offlineFile.value=offlineFile; 
	submitMethod(actionMethod);
}

//-->     
</script>

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
					 	<html:submit styleClass="a.button" onclick="javascript:submitMethod('submitOfflineFiles');">
								<bean:message key="button.upload"/>
						</html:submit>
					</td> 

				</tr>
				<tr> 
					<td align="right" valign=top>
          				<bean:message key="label.uploadedOfflineFiles" />
          			</td>
					<td colspan=3 align=left valign=top width="100%">
						<table align="left" border="1">
									<c:forEach var='item' items='${sessionScope.listUploadedOfflineFileNames}'>
									<tr>
								         <td valign=top><font size=2> <b> <c:out value='${item}'/> </b> </font> </td>
								      	<td valign=top>
								      		<img src="images/edit.gif" align=left onclick="javascript:viewFileItem('<c:out value="${item}"/>','viewFileItem');">
								      	</td>
							      	  	<td valign=top>
								      		<img src="images/delete.gif" align=left onclick="javascript:deleteFileItem('<c:out value="${item}"/>','deleteFileItem', '1');">	  										  		
								      	</td>
								     </tr> 	
				         			</c:forEach>
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
					 	<html:submit onclick="javascript:submitMethod('submitOnlineFiles');">
								<bean:message key="button.upload"/>
						</html:submit>
					</td> 
				</tr>

				<tr>
					<td align="right" valign=top>
          				<bean:message key="label.uploadedOnlineFiles" />
          			</td>
          			
					<td colspan=3 align=left valign=top width="100%">
						<table align="left" border="1">
									<c:forEach var='item' items='${sessionScope.listUploadedOnlineFileNames}'>
										<tr>									
									         <td valign=top><font size=2> <b> <c:out value='${item}'/> </b> </font> </td>
									      	<td valign=top>
									      		<img src="images/edit.gif" align=left onclick="javascript:viewFileItem('<c:out value="${item}"/>','viewFileItem');">	  										  		
									      	</td>
									      	<td valign=top>
									      		<img src="images/delete.gif" align=left onclick="javascript:deleteFileItem('<c:out value="${item}"/>','deleteFileItem', '0');">	  										  		
									      	</td>
					         			</tr>								      	
				         			</c:forEach>
	         			</table>
					</td> 
				</tr>

				<html:hidden property="fileItem"/>
				<html:hidden property="offlineFile"/>				

		  		<tr>
 				 	<td colspan=4 align=center valign=top>								
						&nbsp
				  	</td>
				</tr>
 				 
 				 <tr>
				 	<td valign=top>								
				  	</td>
					 <td colspan=3 valign=top> 
						 <html:submit onclick="javascript:submitMethod('doneInstructionsTab');">
							<bean:message key="button.done"/>
						</html:submit>
					</td> 
 				 </tr>
 				 
 				 <tr>
 				 	<td colspan=4 align=center valign=top>								
						&nbsp
				  	</td>
				</tr>
				
				<c:if test="${requestScope.fileContentReady == 1}"> 			
					<tr>
						<td align="right" valign=top>
	          				 <bean:message key="label.fileContent" /> : 
	          			</td>
					
	 				 	<td colspan=3 align=left valign=top>								
	 				 		<table>
	 				 			<tr> 
	 				 				<td align=center> <font size=2> <b>
	 				 					 <c:out value="${sessionScope.fileName}"/>  
	 				 				</b> </font> </td>
				 				 </tr>
				 				 <tr>
			 				 		<td> <textarea ROWS=20 COLS=80><c:out value="${sessionScope.fileContent}"/> </textarea> </td>				 				 
				 				 </tr>
	 				 		</table>
					  	</td>
					</tr>
				</c:if> 			
 				 
			</table>	  	
