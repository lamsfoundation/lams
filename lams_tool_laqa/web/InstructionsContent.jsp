<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<div id="richTextContainer">
		<tr> <td align=center>
			<table align=center>
				<tr> 
					<td NOWRAP  valign=top><font size=2> <b>
          				<fmt:message key="label.offlineInstructions" />:
	          			</b>
    	      			</font>
          			</td>
					<td NOWRAP width=700> <!-- Dave,I found width was necessary to present all the elements of the editor, feel free to change -->
                        <span id="previewOfflineInstructions" style="visibility: hidden; display: none;">
                            <div>
                                <a href="javascript:doWYSWYGEdit('OfflineInstructions')"><font size=2> <b> Open Richtext Editor </font> </b></a>
                            </div>
                            <div class="previewPanel" id="previewOfflineInstructions.text"></div>
                        </span>
                        <span id="txOfflineInstructions">
                            <div>
                                <a href="javascript:doTextToHTML('OfflineInstructions'); doWYSWYGEdit('OfflineInstructions')"><font size=2> <b> Open Richtext Editor </font> </b> </a>
                            </div>
                            <textarea class="textareaPanel" name="offlineInstructions" id="txOfflineInstructions.textarea"><c:out value="${QaAuthoringForm.offlineInstructions}" escapeXml="false" /></textarea>
                        </span>
					</td> 
				</tr>

				<tr> 
					<td NOWRAP  valign=top><font size=2> <b>
          				<fmt:message key="label.offlineFiles" />
	          			</b>
    	      			</font> 
          			</td>
          			<td NOWRAP> 
						<html:file  property="theOfflineFile"></html:file>
					 	<html:submit property="submitOfflineFile" 
                                     styleClass="linkbutton" 
                                     onmouseover="pviiClassNew(this,'linkbutton')" 
                                     onmouseout="pviiClassNew(this,'linkbutton')"
                                     onclick="submitMethod('addNewFile');">
								<bean:message key="label.upload"/>
						</html:submit>
					</td> 

				</tr>
          		<tr> 
					<td NOWRAP  valign=top><font size=2> <b>
          				<fmt:message key="label.onlineInstructions" />:
	       				</b> </font>
          			</td>
					<td NOWRAP width=700> <!-- Dave,I found width was necessary to present all the elements of the editor, feel free to change -->
                        <span id="previewOnlineInstructions" style="visibility: hidden; display: none;">
                            <div>
                                <a href="javascript:doWYSWYGEdit('OnlineInstructions')"> <font size=2> <b> Open Richtext Editor </font> </b></a>
                            </div>
                            <div class="previewPanel" id="previewOnlineInstructions.text"></div>
                        </span>
                        <span id="txOnlineInstructions">
                            <div>
                                <a href="javascript:doTextToHTML('OnlineInstructions'); doWYSWYGEdit('OnlineInstructions')"><font size=2> <b> Open Richtext Editor </font> </b></a>
                            </div>
                            <textarea class="textareaPanel" name="onlineInstructions" id="txOnlineInstructions.textarea"><c:out value="${QaAuthoringForm.onlineInstructions}" escapeXml="false" /></textarea>
                        </span>
					</td> 
				</tr>
				
				<tr> 
						<td NOWRAP  valign=top><font size=2> <b>
	          				<fmt:message key="label.onlineFiles" />
          				</b> </font>
          			</td>
          			<td NOWRAP> 
						<html:file  property="theOnlineFile"></html:file>
					 	<html:submit property="submitOnlineFile" 
                                     styleClass="linkbutton" 
                                     onmouseover="pviiClassNew(this,'linkbutton')" 
                                     onmouseout="pviiClassNew(this,'linkbutton')"
                                     onclick="submitMethod('addNewFile');">
								<bean:message key="label.upload"/>
						</html:submit>
					</td> 
				
				</tr>
          		</table>	  	

		</td></tr>

		<tr><td align=center>

<logic:present name="attachmentList">
<bean:size id="count" name="attachmentList" />

<logic:notEqual name="count" value="0">

	<h2><fmt:message key="label.attachments" /></h2>
	<div id="datatablecontainer">
		<table width="100%" align=center  border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<table width="70%" align="left">
		            <tr>
		                <td NOWRAP  valign=top><font size=2> <b><fmt:message key="label.filename" /> </b> </font> </td>
		                <td NOWRAP  valign=top><font size=2> <b> <fmt:message key="label.type" /></b> </font> </td>
		            	<td>&nbsp;</td>
		            </tr>
		            <logic:iterate name="attachmentList" id="attachment">
		            	<bean:define id="view">/download/?uuid=<bean:write name="attachment" property="uuid"/>&preferDownload=false</bean:define>
						<bean:define id="download">/download/?uuid=<bean:write name="attachment" property="uuid"/>&preferDownload=true</bean:define>
                        <bean:define id="uuid" name="attachment" property="uuid" />
                        
                        <tr>
			                         	
			            	<td><font size=2>  <bean:write name="attachment" property="fileName"/> </font> </td>
			                <td>
								<font size=2> 
			                	<c:choose>
					            	<c:when test="${attachment.fileOnline}" >
					                	<fmt:message key="instructions.type.online" />
					               	</c:when>
					                <c:otherwise>
					                	<fmt:message key="instructions.type.offline" />
					                </c:otherwise>
				                </c:choose>
								 </font>				                
				            </td>
				            <td>
					        	<table>
						        	<tr>
						            	<td>
						                	<a href='javascript:launchInstructionsPopup("<html:rewrite page='<%=view%>'/>")' class="button">
						                   		<fmt:message key="link.view" />
						                    </a>
						               	</td>
						                <td>
              								<font size=2> <b>
							            	<html:link page="<%=download%>" styleClass="button">
							                	<fmt:message key="link.download" />
							                </html:link>
											</b> </font>				                							                
						                </td>
						                <td>
											<font size=2> <b>						                
							            	<html:link page="/authoring.do?dispatch=deleteFile" 
							                         	paramId="uuid" paramName="attachment" paramProperty="uuid"
							                         	onclick="javascript:return confirm('Are you sure you want to delete this file?')"
							                         	target="_self" styleClass="button">
							                	<fmt:message key="link.delete" />
							                </html:link> 
											</b> </font>				                							                							                
							            </td>
						           	</tr>
					            </table>
				           	</td>
			   	     	</tr>
		    	    </logic:iterate>
					</table>
			 	</td>
			</tr>
		</table>
	</div>
	
 </logic:notEqual>
 </logic:present>
		
		</td></tr>
</div>




