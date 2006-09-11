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
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>

		
			<table>
				<tr>
					<td class="field-name">							
			      			<bean:message key="label.vote.changable"/>:
					</td>
					<td>							
		      			<html:checkbox property="voteChangable" value="1"
							styleClass="noBorder" styleId="voteChangable">
						</html:checkbox>
	      			</td>
		         </tr>
		         
				<tr>
					<td class="field-name">							
			      			<bean:message key="label.vote.lockedOnFinish"/>:
					</td>
					<td>							
		      			<html:checkbox property="lockOnFinish" value="1"
							styleClass="noBorder" styleId="lockOnFinish">
						</html:checkbox>
	      			</td>
		         </tr>
		         
   				<tr>
					<td class="field-name">							
			      			<bean:message key="label.allowText"/>:
					</td>
					<td>							
		      			<html:checkbox property="allowText" value="1"
							styleClass="noBorder" styleId="allowText">
						</html:checkbox>
						
	      			</td>
		        </tr>
		         
		         <tr> 
					<td class="field-name">							
					 		<bean:message key="label.maxNomCount"/>: 
					</td>

					<td>							
			 			<html:text property="maxNominationCount" size="8" maxlength="3"/>
			 		</td>
			  	</tr>

   				<tr>
					<td class="field-name">							
			      			<bean:message key="label.reflect"/>:
					</td>
					<td>							
		      			<html:checkbox property="reflect" value="1"
							styleClass="noBorder" styleId="reflect">
						</html:checkbox>
	      			</td>
		        </tr>

   				<tr>
					<td class="field-name">							
							&nbsp
					</td>
					<td>							
 						<html:textarea cols="30" rows="3" property="reflectionSubject"></html:textarea> 						
	      			</td>
		        </tr>
		  	
			  	
			</table>	  	

		