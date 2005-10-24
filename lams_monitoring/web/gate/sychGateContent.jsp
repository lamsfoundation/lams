<!--
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
-->
<%@ taglib uri="tags-html-el" prefix="html-el" %>
<%@ taglib uri="tags-bean-el" prefix="bean-el" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-core" prefix="c" %>		
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<tr>
	<td>
        <table width="100%" height="295" border="0" align="center" cellpadding="5" cellspacing="0" bgcolor="#FFFFFF" summary="This table is being used for layout purposes only"> 
          <tr> 
            <td valign="top"> <div align="center"> 
                <!-- Page Content table--> 
                <table width="90%" border="0" cellspacing="0" cellpadding="0" summary="This table is being used for layout purposes only"> 
					<tr><td>&nbsp; </td></tr> 
					<tr> 
					    <td height="31" colspan="3"> <div class="heading" align="center"><fmt:message key="label.synch.gate"/></div></td> 
					</tr> 				  
					<%@ include file="gateInfo.jsp" %>
				    <tr><td>&nbsp; </td></tr>
				    <tr><td>&nbsp; </td></tr>
					<%@ include file="openGate.jsp" %>
                </table> 
                <!-- end of Page Content table--> 
              </div></td> 
          </tr> 
		  <tr height="50">
		  	<td></td>
		  </tr>
        </table>

	</td>
</tr>


