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


			<table>
				<tr>
	      			<td NOWRAP class="formlabel" valign=top>
		      			<font size=2> 
			      			<b> <bean:message key="radiobox.sln"/>: </b>
		      			</font>
					</td>
					<td NOWRAP valign=top>
						<font size=2>
							<html:radio property="sln"  value="ON">
							 <bean:message key="option.on"/>
							</html:radio>
	
							<html:radio property="sln"  value="OFF">
							 <bean:message key="option.off"/>
							</html:radio>
						</font>
	      			</td>
		         </tr>
		         
   				<tr>
					<td NOWRAP class="formlabel" valign=top>
						<font size=2>
			      			<b> <bean:message key="radiobox.onepq"/>: </b>
		      			</font>
					</td>
					<td NOWRAP valign=top>
						<font size=2>
							<html:radio property="questionsSequenced" value="ON">
							 <bean:message key="option.on"/>
							</html:radio>
	
							<html:radio property="questionsSequenced" value="OFF">
							 <bean:message key="option.off"/>
							</html:radio>
						</font>
	      			</td>
		        </tr>
		         
		         <tr>
					<td NOWRAP class="formlabel" valign=top>
						<font size=2>
			      			<b> <bean:message key="radiobox.retries"/>: </b>
		      			</font>
					</td>
					<td NOWRAP valign=top>
						<font size=2>
							<html:radio property="retries" value="ON">
							 <bean:message key="option.on"/>
							</html:radio>
	
							<html:radio property="retries" value="OFF">
							 <bean:message key="option.off"/>
							</html:radio>
						</font>
	      			</td>

				</tr>	      			
		        
		         <tr> 
					<td NOWRAP class="formlabel" valign=top>
						<font size=2>
					 		<b>	<bean:message key="label.report.title"/>: </b>
				 		</font>
				 	</td>
	  				<td NOWRAP valign=top>
		  				<font size=2>
							<lams:SetEditor id="richTextReportTitle" text="${sessionScope.richTextReportTitle}" small="true"/>							
						</font>
					</td> 
			  	</tr>
			  	
			  	<tr> 
					<td NOWRAP class="formlabel" valign=top>
						<font size=2>
					 		<b>	<bean:message key="label.report.endLearningMessage"/>: </b>
			 			</font>
			 		</td>
	  				<td NOWRAP>
		  				<font size=2>
							<lams:SetEditor id="richTextEndLearningMsg" text="${sessionScope.richTextEndLearningMsg}" small="true"/>														
						</font>
					</td> 
			  	</tr>
			  	
		  		<tr>
 				 	<td NOWRAP colspan=2 align=center valign=top>								
						&nbsp&nbsp
				  	</td>
				</tr>

			</table>	  	
		
			