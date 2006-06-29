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

<%@ include file="/common/taglibs.jsp" %>

		<table class="forms">
			<tr>
				<td NOWRAP valign=top>
	  				<b>  <bean:message key="label.onlineInstructions.col" /> </b>
	  			</td>
				<td NOWRAP valign=top>
					  <c:out value="${sessionScope.richTextOnlineInstructions}" escapeXml="false" />	
				</td> 
			</tr>
		
			<tr>
				<td colspan=2> &nbsp </td>
			</tr>

		
			<tr> 
				<td NOWRAP valign=top>
	  				<b>  <bean:message key="label.offlineInstructions.col" />  </b>
	  			</td>
				<td NOWRAP valign=top>
					  <c:out value="${sessionScope.richTextOfflineInstructions}" escapeXml="false" />	
				</td> 
			</tr>
		</table>

		
		
<table class="forms"> 	  
<tr><td align=center>
<logic:present name="attachmentList">
<bean:size id="count" name="attachmentList" />
<logic:notEqual name="count" value="0">

		<table  width="100%" align=center  border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<table width="70%" align="left">
		            <tr>
		                <td NOWRAP  valign=top> <b><bean:message key="label.filename" /> </b>  </td>
		                <td NOWRAP  valign=top> <b> <bean:message key="label.type" /></b>  </td>
		            	<td>&nbsp;</td>
		            </tr>
		            <logic:iterate name="attachmentList" id="attachment">
		            	<bean:define id="view">/download/?uuid=<bean:write name="attachment" property="uuid"/>&preferDownload=false</bean:define>
						<bean:define id="download">/download/?uuid=<bean:write name="attachment" property="uuid"/>&preferDownload=true</bean:define>
                        <bean:define id="uuid" name="attachment" property="uuid" />
                        
                        <tr>
			            	<td>  <bean:write name="attachment" property="fileName"/>  </td>
			                <td>
			                	<c:choose>
					            	<c:when test="${attachment.fileOnline}" >
					                	<bean:message key="instructions.type.online" />
					               	</c:when>
					                <c:otherwise>
					                	<bean:message key="instructions.type.offline" />
					                </c:otherwise>
				                </c:choose>
				            </td>
				            <td>
					        	<table>
						        	<tr>
						            	<td>
						                	<a href='javascript:launchInstructionsPopup("<html:rewrite page='<%=view%>'/>")' class="button">
						                   		<bean:message key="link.view" />
						                    </a>
						                    &nbsp&nbsp
							            	<html:link page="<%=download%>" styleClass="button">
							                	<bean:message key="link.download" />
							                </html:link>
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
		




		