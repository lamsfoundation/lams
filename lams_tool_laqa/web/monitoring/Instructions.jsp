<%-- 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as 
  published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>


<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

		<table class="forms">
			<tr>
				<td>
					<table align="center">

						<tr> 
							<td NOWRAP valign=top>
				  				<b> <font size=2> <bean:message key="label.onlineInstructions.col" /> </font></b>
				  			</td>
							<td NOWRAP valign=top>
								  <font size=2> <c:out value="${sessionScope.richTextOnlineInstructions}" escapeXml="false" />	</font>					  
							</td> 
						</tr>
					
						<tr>
							<td colspan=2> &nbsp&nbsp&nbsp </td>
						</tr>

					
						<tr> 
							<td NOWRAP valign=top>
				  				<b> <font size=2> <bean:message key="label.offlineInstructions.col" /> </font> </b>
				  			</td>
							<td NOWRAP valign=top>
								  <font size=2> <c:out value="${sessionScope.richTextOfflineInstructions}" escapeXml="false" />	</font>					  
							</td> 
						</tr>
				
					</table>
				</td>
			</tr>
		</table>

		
		
<table align=center>
<tr><td align=center>
<logic:present name="attachmentList">
<bean:size id="count" name="attachmentList" />
<logic:notEqual name="count" value="0">
	<h2><font size=2> <bean:message key="label.attachments" /> </font></h2>
		<table  width="100%" align=center  border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<table width="70%" align="left">
		            <tr>
		                <td NOWRAP  valign=top><font size=2> <b><bean:message key="label.filename" /> </b> </font> </td>
		                <td NOWRAP  valign=top><font size=2> <b> <bean:message key="label.type" /></b> </font> </td>
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
					                	<bean:message key="instructions.type.online" />
					               	</c:when>
					                <c:otherwise>
					                	<bean:message key="instructions.type.offline" />
					                </c:otherwise>
				                </c:choose>
								 </font>				                
				            </td>
				            <td>
					        	<table>
						        	<tr>
						            	<td>
						                	<a href='javascript:launchInstructionsPopup("<html:rewrite page='<%=view%>'/>")' class="button">
						                   		<bean:message key="link.view" />
						                    </a>
						               	</td>
						                <td>
              								<font size=2> <b>
							            	<html:link page="<%=download%>" styleClass="button">
							                	<bean:message key="link.download" />
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
 </logic:notEqual>
 </logic:present>
		 	</td>
		</tr>
	</table>
		




		