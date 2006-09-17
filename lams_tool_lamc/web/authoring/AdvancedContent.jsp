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
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
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

			
	<table cellpadding="0">
					<tr>
						<td>							
							<html:checkbox property="sln"  value="1" styleClass="noBorder">
								<bean:message key="radiobox.sln" />
							</html:checkbox>
		      			</td>		      			
			         </tr>
			         
					<tr>
						<td>							
							<html:checkbox property="questionsSequenced" value="1" styleClass="noBorder">
								<bean:message key="radiobox.onepq" />
							</html:checkbox>
		      			</td>		      			
			         </tr>
			         
	   				<tr>
						<td>							
							<html:checkbox property="retries" value="1" styleClass="noBorder">
								<bean:message key="radiobox.retries" />
							</html:checkbox>
		      			</td>		      			
			        </tr>
			        

   				<tr>
						<td>							
							<html:checkbox property="reflect" value="1" styleClass="noBorder">
								<bean:message key="label.reflect" />
							</html:checkbox>
		      			</td>		      			
		        </tr>

   				<tr>
					<td>							
 						<html:textarea cols="30" rows="3" property="reflectionSubject"></html:textarea> 						
	      			</td>
		        </tr>
		</table>






		
		
		

					