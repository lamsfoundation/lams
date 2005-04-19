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
<%@ taglib uri="/WEB-INF/struts/struts-html-el.tld" prefix="html-el" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean-el.tld" prefix="bean-el" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>		
<%@ taglib uri="/WEB-INF/jstl/fmt.tld" prefix="fmt" %>

<tr>
	<td>
        <table width="100%" height="295" border="0" align="center" cellpadding="5" cellspacing="0" bgcolor="#FFFFFF" summary="This table is being used for layout purposes only"> 
          <tr> 
            <td valign="top"> <div align="center"> 
                <!-- Page Content table--> 
                <table width="90%" border="0" cellspacing="0" cellpadding="0" summary="This table is being used for layout purposes only"> 
					<tr><td>&nbsp; </td></tr> 
					<tr> 
					    <td height="31" colspan="3"> <div class="heading" align="center"><fmt:message key="label.schedule.gate"/></div></td> 
					</tr> 					  
				    <%@ include file="gateInfo.jsp" %>
			        <tr><td>&nbsp; </td></tr>
			        <tr><td>&nbsp; </td></tr>
						
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