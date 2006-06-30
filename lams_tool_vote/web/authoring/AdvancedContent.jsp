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

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

				<table class="forms">
				<tr>
					<td class="field-name">							
		      			 <b>
			      			<bean:message key="label.vote.changable"/>:
			      		</b> 
					</td>
	  				<td NOWRAP valign=top>
						<html:radio property="voteChangable" value="ON">
						 <bean:message key="option.on"/>
						</html:radio>

						<html:radio property="voteChangable" value="OFF">
						 <bean:message key="option.off"/>
						</html:radio>
	      			</td>
		         </tr>
		         
				<tr>
					<td class="field-name">							
		      			 <b>
			      			<bean:message key="label.vote.lockedOnFinish"/>:
			      		</b> 
					</td>
					<td NOWRAP valign=top>
						<html:radio property="lockOnFinish" value="ON">
						 <bean:message key="option.on"/>
						</html:radio>

						<html:radio property="lockOnFinish" value="OFF">
						 <bean:message key="option.off"/>
						</html:radio>
	      			</td>
		         </tr>
		         

				<tr>
					<td class="field-name">							
		      			 <b>
			      			<bean:message key="label.allowText"/>:
			      		</b> 
					</td>
					<td NOWRAP valign=top>
						<html:radio property="allowText" value="ON">
						 <bean:message key="option.on"/>
						</html:radio>

						<html:radio property="allowText" value="OFF">
						 <bean:message key="option.off"/>
						</html:radio>
	      			</td>
		         </tr>


			  	<tr> 
					<td class="field-name">							
	      			 <b>
				 		<bean:message key="label.maxNomCount"/>: </td>
		      		</b> 
					<td NOWRAP valign=top>
			 			<html:text property="maxNominationCount" size="3" maxlength="3"/>
			 		</td>
			  	</tr>
				

			</table>	  	
		
