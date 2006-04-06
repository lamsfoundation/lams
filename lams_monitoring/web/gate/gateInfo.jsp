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

<!-- general information section--> 
<tr>
    <td class="subHeader"><fmt:message key="lable.description"/></td>
</tr> 
<tr>
    <td class="body"><c:out value="${GateForm.map.gate.description}"/></td>
</tr> 
<!-- end of general information section--> 
    <tr> 
<td class="body">&nbsp;</td> 
</tr>
<!--waiting learner information table-->
<tr> 
	<td class="bodyBold">
	<c:out value="${GateForm.map.waitingLearners}"/> out of <c:out value="${GateForm.map.totalLearners}"/>
	are waiting in front of the gate.
	</td>
</tr>