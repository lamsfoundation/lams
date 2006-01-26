<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-c" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

				<table class="forms">
				<tr> 
					<td NOWRAP class="formlabel" valign=top>
						<font size=2>
    	      				<b> <bean:message key="label.offlineInstructions" />: <b>
    	      			</font>
          			</td>
					<td NOWRAP colspan=3 class="formcontrol" valign=top>
					<FCK:editor id="richTextOfflineInstructions" basePath="/lams/fckeditor/">
						  <c:out value="${richTextOfflineInstructions}" escapeXml="false" />						  
					</FCK:editor>
					</td> 
				</tr>
				
				<tr> 
					<td NOWRAP class="formlabel" valign=top>
						<font size=2>
							<b> <bean:message key="label.offlineFiles" /> <b>
						</font>
          			</td>
          			<td colspan=3 NOWRAP valign=top> 
	          			<font size=2>
							<html:file  property="theOfflineFile"></html:file>
						 	<html:submit onclick="javascript:submitMethod('submitOfflineFiles');" styleClass="buttonLeft">
								 <bean:message key="button.upload"/> 
							</html:submit>
						</font>
					</td> 

				</tr>
				<tr> 
					<td NOWRAP align="right" valign=top>
						<font size=2>
    	      				<b> <bean:message key="label.uploadedOfflineFiles" /> </b>
    	      			</font>
          			</td>
					<td NOWRAP colspan=3 align=left valign=top width="100%">
						<table align="left" border="1">
									<c:forEach var='file' items='${sessionScope.listOfflineFilesMetadata}'>
											<tr>
												<td NOWRAP valign=top>
													<font size=2> <c:out value="${file.filename}"/> </font>
												</td>
												<td NOWRAP valign=top>
													<c:set var="viewURL">
														<html:rewrite page="/download/?uuid=${file.uuid}&preferDownload=false"/>
													</c:set>
													<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
														<font size=2> <bean:message key="label.view"/> </font>
													</a>
												</td>
												<td NOWRAP valign=top>
													<c:set var="downloadURL">
															<html:rewrite page="/download/?uuid=${file.uuid}&preferDownload=true"/>
													</c:set>
													<a href="<c:out value='${downloadURL}' escapeXml='false'/>">
														<font size=2> <bean:message key="label.download"/> </font>
													</a>
												</td>
												<td NOWRAP valign=top> 
													 <font size=2>	<img src="images/delete.gif" align=left onclick="javascript:submitDeleteFile('<c:out value="${file.uuid}"/>','deleteOfflineFile');"> </font>													
												</td>
											</tr>
				         			</c:forEach>
	         			</table>
					</td> 
				</tr>

				<tr> 
					<td NOWRAP align="right" valign=top>
						<font size=2>
    	      				<b> <bean:message key="label.onlineInstructions" /> </b>
    	      			</font>
          			</td>
					<td NOWRAP colspan=3 class="formcontrol" valign=top>
					<FCK:editor id="richTextOnlineInstructions" basePath="/lams/fckeditor/">
						  <c:out value="${sessionScope.richTextOnlineInstructions}" escapeXml="false" />						  
					</FCK:editor>
					</td> 
				</tr>
				
				<tr> 
					<td NOWRAP class="formlabel" valign=top>
						<font size=2>
    	      				<b> <bean:message key="label.onlineFiles" /> </b>
    	      			</font>
          			</td>
          			<td colspan=3 NOWRAP valign=top> 
	          			<font size=2>
							<html:file  property="theOnlineFile"></html:file>
						 	<html:submit onclick="javascript:submitMethod('submitOnlineFiles');" styleClass="buttonLeft">
								 <bean:message key="button.upload"/> 
							</html:submit>
						</font>
					</td> 
				</tr>

				<tr>
					<td NOWRAP align="right" valign=top>
	          			<font size=2>
		      				<b> <bean:message key="label.uploadedOnlineFiles" /> </b>
	      				</font>
          			</td>
          			
					<td NOWRAP colspan=3 align=left valign=top width="100%">
						<table align="left" border="1">
									<c:forEach var='file' items='${sessionScope.listOnlineFilesMetadata}'>
											<tr>
												<td NOWRAP valign=top>
													<font size=2> <c:out value="${file.filename}"/> </font>
												</td>
												<td NOWRAP valign=top>
													<c:set var="viewURL">
														<html:rewrite page="/download/?uuid=${file.uuid}&preferDownload=false"/>
													</c:set>
													<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
														 <font size=2>	<bean:message key="label.view"/> </font>
													</a>
												</td>
												<td NOWRAP valign=top>
													<c:set var="downloadURL">
															<html:rewrite page="/download/?uuid=${file.uuid}&preferDownload=true"/>
													</c:set>
													<a href="<c:out value='${downloadURL}' escapeXml='false'/>">
														<font size=2> <bean:message key="label.download"/> </font>
													</a>
												</td>
												<td NOWRAP valign=top> 
													<font size=2> <img src="images/delete.gif" align=left onclick="javascript:submitDeleteFile('<c:out value="${file.uuid}"/>','deleteOnlineFile');"> </font>	
												</td>
											</tr>
				         			</c:forEach>
	         			</table>
					</td> 
				</tr>

				<html:hidden property="fileItem"/>
				<html:hidden property="offlineFile"/>				
				<html:hidden property="uuid"/>				
				
		  		<tr>
 				 	<td NOWRAP colspan=4 align=center valign=top>								
						&nbsp
				  	</td>
				</tr>
 				 
 				 <tr>
				 	<td NOWRAP valign=top>								
				  	</td>
					 <td NOWRAP colspan=3 valign=top> 
						 <font size=2>
							 <html:submit onclick="javascript:submitMethod('doneInstructionsTab');" styleClass="button">
								  <bean:message key="button.done"/> 
							</html:submit>
						</font>						
					</td> 
 				 </tr>

			</table>	  	
