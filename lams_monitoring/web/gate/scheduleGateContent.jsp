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

<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
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
					    <td height="31" colspan="3"> <div class="heading" align="center"><fmt:message key="label.schedule.gate.title"/></div></td> 
					</tr> 					  
				    <%@ include file="gateInfo.jsp" %>
			        <tr><td>&nbsp; </td></tr>
			        <tr><td>&nbsp; </td></tr>
					<tr>
						<td width="50%" class="body"><fmt:message key="label.schedule.gate.open.message"/></td>
						<td width="50%" class="bodyBold">
							<fmt:formatDate value="${GateForm.map.startingTime}" type="both" dateStyle="full" timeStyle="full"/>
						</td>
					</tr>		
					<tr>
						<td width="50%" class="body"><fmt:message key="label.schedule.gate.open.message"/></td>
						<td width="50%" class="bodyBold">
							<fmt:formatDate value="${GateForm.map.endingTime}" type="both" dateStyle="full" timeStyle="full"/>
						</td>
					</tr>
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