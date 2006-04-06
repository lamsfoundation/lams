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

<tr>
	<td class="body" valign="middle">Class cannot continue until the gate is opened by you</td>
	<td width="90" align="center" valign="middle" class="bodyBold">
		<c:if test="${not GateForm.map.gate.gateOpen}" >
			<html:form action="/gate?method=openGate" target="_self">
				<html:submit styleClass="button" value="Open Gate" onmouseover="pviiClassNew(this,'buttonover')" onmouseout="pviiClassNew(this,'button')" /> 
			</html:form>
		</c:if>
		<c:if test="${GateForm.map.gate.gateOpen}">
			 Gate has been opened
		</c:if>
	</td>
	<td width="100" align="center" valign="middle">
		<c:if test="${not GateForm.map.gate.gateOpen}">
			<img height="43" src="../images/synch_active.gif" width="37"><br><span class="bodyBold"> Active</span>
		</c:if>
		<c:if test="${GateForm.map.gate.gateOpen}">
			<img height="43" src="../images/synch_not_active.gif" width="37"><br><span class="bodyBold"> Active</span>
		</c:if>
	</td>
</tr>   
