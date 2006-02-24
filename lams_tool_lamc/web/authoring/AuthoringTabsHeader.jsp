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

	<!-- tab holder table -->
	<table border="0" cellspacing="0" cellpadding="0">
	
	  <tr>
	    <td valign="bottom">
			<!-- table for tab 1 (basic)-->
			<table border="0" cellspacing="0" cellpadding="0">
			  <tr>
				<td><a href="#"  onClick="showTab('b');return false;" ><img src="<c:out value="${lams}"/>images/aqua_tab_s_left.gif" name="tab_left_b" width="8" height="25" border="0" id="tab_left_b"/></a></td>
				<td class="tab tabcentre_selected" width="90" id="tab_tbl_centre_b"  onClick="showTab('b');return false;" ><label><a href="#" onClick="showTab('b');return false;" id="b" >
					<bean:message key="label.basic"/>
				</a></label></td>
				<td><a href="#" onClick="showTab('b');return false;"><img src="<c:out value="${lams}"/>images/aqua_tab_s_right.gif"  name="tab_right_b" width="8" height="25" border="0" id="tab_right_b"/></a></td>
			  </tr>
			</table>
		</td>
		
	    <td valign="bottom">
			<!-- table for tab 2 (advanced) -->
			<table border="0" cellspacing="0" cellpadding="0">
			  <tr>
				<td><a href="#" onClick="showTab('a');return false;"><img src="<c:out value="${lams}"/>images/aqua_tab_left.gif" name="tab_left_a" width="8" height="22" border="0" id="tab_left_a" /></a></td>
				<td class="tab tabcentre" width="90" id="tab_tbl_centre_a" onClick="showTab('a');return false;"><label><a href="#" onClick="showTab('a');return false;" id="a" >
					<bean:message key="label.advanced"/>
				</a></label></td>
				<td><a href="#" onClick="showTab('a');return false;"><img src="<c:out value="${lams}"/>images/aqua_tab_right.gif" name="tab_right_a" width="9" height="22" border="0" id="tab_right_a" /></a></td>
			  </tr>
			</table>
		</td>
		
	    <td valign="bottom">
			<!-- table for tab 3 (instructions) -->
			<table border="0" cellspacing="0" cellpadding="0">
			  <tr>
				<td ><a href="#" onClick="showTab('i');return false;"><img border="0" src="<c:out value="${lams}"/>images/aqua_tab_left.gif" width="8" height="22" id="tab_left_i"   name="tab_left_i" /></a></td>
				<td class="tab tabcentre" width="90" id="tab_tbl_centre_i"  onClick="showTab('i');return false;" ><label><a href="#" onClick="showTab('i');return false;" id="i" >
					<bean:message key="label.instructions"/>
				</a></label></td>
				<td ><a href="#" onClick="showTab('i');return false;"><img src="<c:out value="${lams}"/>images/aqua_tab_right.gif"  width="9" height="22" border="0" id="tab_right_i"  name="tab_right_i"/></a></td>
			  </tr>
			</table>
		</td>

	  </tr>
	</table>
