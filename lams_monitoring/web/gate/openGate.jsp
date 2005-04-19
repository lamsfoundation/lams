<tr>
	<td class="body" valign="middle">Class cannot continue until the gate is opened by you</td>
	<td width="90" align="center" valign="middle" class="bodyBold">
		<c:if test="${not GateForm.map.gate.gateOpen}" >
			<html-el:form action="/gate?method=openGate" target="_self">
				<html-el:submit styleClass="button" value="Open Gate" onmouseover="pviiClassNew(this,'buttonover')" onmouseout="pviiClassNew(this,'button')" /> 
			</html-el:form>
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
