<%--
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

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

	        	<tr><td>
				<!-- start tabs -->
				<!-- tab holder table -->
				<table border="0" cellspacing="0" cellpadding="0">
				  <tr>
				  	<td> &nbsp&nbsp</td>
				  </tr>
					
				  <tr>
					    <td NOWRAP valign="bottom">
							<table border="0" cellspacing="0" cellpadding="0">
							  <tr>
								<td><img src="<c:out value="${lams}"/>images/aqua_tab_s_left.gif" name="tab_left_su" width="8" height="25" border="0" id="tab_left_su"/></td>
								<td NOWRAP class="tab tabcentre_selected" width="90" id="tab_tbl_centre_su" ><label>
								<a href="?method=getSummary" id="su" >
									<font size=2> 	<bean:message key="label.summary"/> </font>
								</a></label></td>
								<td><a href="?method=getSummary">
									<img src="<c:out value="${lams}"/>images/aqua_tab_s_right.gif"  name="tab_right_su" width="8" height="25" border="0" id="tab_right_su"/></a></td>
							  </tr>
							</table>
						</td>
		
					    <td NOWRAP valign="bottom">
							<table border="0" cellspacing="0" cellpadding="0">
							  <tr>
								<td><img src="<c:out value="${lams}"/>images/aqua_tab_s_left.gif" name="tab_left_i" width="8" height="25" border="0" id="tab_left_i"/></td>
								<td NOWRAP class="tab tabcentre_selected" width="90" id="tab_tbl_centre_i" ><label>
								<a href="?method=getInstructions" id="i" >
										<font size=2>  <bean:message key="label.instructions"/> </font>
									</a></label></td>
								<td><a href="?method=getInstructions">
									<img src="<c:out value="${lams}"/>images/aqua_tab_s_right.gif"  name="tab_right_i" width="8" height="25" border="0" id="tab_right_i"/></a></td>
							  </tr>
							</table>
						</td>
		
						
					    <td NOWRAP valign="bottom">
							<table border="0" cellspacing="0" cellpadding="0">
							  <tr>
								<td><img src="<c:out value="${lams}"/>images/aqua_tab_s_left.gif" name="tab_left_e" width="8" height="25" border="0" id="tab_left_e"/></td>
								<td NOWRAP class="tab tabcentre_selected" width="90" id="tab_tbl_centre_e" ><label>
								<a href="?method=editActivity" id="e" >
										<font size=2>  <bean:message key="label.editActivity"/>	 </font>					
									</a></label></td>
								<td><a href="?method=editActivity">
									<img src="<c:out value="${lams}"/>images/aqua_tab_s_right.gif"  name="tab_right_e" width="8" height="25" border="0" id="tab_right_e"/></a></td>
							  </tr>
							</table>
						</td>
		
									
					    <td NOWRAP valign="bottom">
							<table border="0" cellspacing="0" cellpadding="0">
							  <tr>
								<td><img src="<c:out value="${lams}"/>images/aqua_tab_s_left.gif" name="tab_left_s" width="8" height="25" border="0" id="tab_left_s"/></td>
								<td NOWRAP class="tab tabcentre_selected" width="90" id="tab_tbl_centre_s" ><label>
									<a href="?method=getStats" id="e" >
										<font size=2>  <bean:message key="label.stats"/> </font>								
									</a></label></td>
								<td><a href="?method=getStats">
									<img src="<c:out value="${lams}"/>images/aqua_tab_s_right.gif"  name="tab_right_s" width="8" height="25" border="0" id="tab_right_s"/></a></td>
							  </tr>
							</table>
						</td>
				  </tr>
				</table>
				
				</td> </tr>
		
				<!-- end tab buttons -->	
