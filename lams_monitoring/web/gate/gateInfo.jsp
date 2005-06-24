                
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