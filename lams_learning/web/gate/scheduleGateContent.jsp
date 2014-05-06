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
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c" %>		
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<c:choose>
	<c:when test="${not empty GateForm.map.reachDate}">
		<p><fmt:message key="label.schedule.gate.reach"/> <lams:Date value="${GateForm.map.reachDate}"/><br />
		   <fmt:message key="label.schedule.gate.offset.1"/> <span id="offset"></span> <fmt:message key="label.schedule.gate.offset.2"/>
		</p>
	</c:when>
	<c:otherwise>
		<c:if test="${GateForm.map.startingTime!=null}">
			<p><fmt:message key="label.schedule.gate.open.message"/> <lams:Date value="${GateForm.map.startingTime}"/></p>
		</c:if>
		<c:if test="${GateForm.map.endingTime!=null}">
			<p><fmt:message key="label.schedule.gate.close.message"/> <lams:Date value="${GateForm.map.endingTime}"/></p>
		</c:if>
	</c:otherwise>
</c:choose>

<fmt:message key="label.schedule.gate.open.remaining"/> <span id="remaining"></span>

<script type="text/javascript">
	function buildDateString(offset, longFormat) {
		var result = "",
			day = 24*60*60,
			offsetDay = Math.floor(offset/day);
		offset -= offsetDay * day;
		var offsetHour = Math.floor(offset/(60*60));
		offset -= offsetHour * 60 * 60;
		var offsetMinute =  Math.floor(offset/60);
		
		if (offsetDay > 0) {
			result = offsetDay + " " + dayLabel + " ";
		}
		if (offsetHour > 0) {
			result += offsetHour + " " + hourLabel + " ";
		}
		if (offsetMinute > 0) {
			result += offsetMinute + " " + minuteLabel + " ";
		}
		if (longFormat) {
			var offsetSecond = offset - offsetMinute * 60;
			result += offsetSecond + " " + secondLabel;
		}
		return result;
	}

	var dayLabel = '<fmt:message key="label.days"/>',
		hourLabel = '<fmt:message key="label.hours"/>',
		minuteLabel = '<fmt:message key="label.minutes"/>',
		secondLabel = '<fmt:message key="label.seconds"/>',
		offsetField = document.getElementById('offset'),
		remainingField = document.getElementById('remaining'),
		remainTime = ${GateForm.map.remainTime};

	setInterval(function(){
		remainTime--;
		if (remainTime <= 0) {
			document.location.reload();
		} else {
			var remainingFieldText = buildDateString(remainTime, true);
			remainingField.innerHTML = remainingFieldText;
		}
	}, 1000);
		
	if (offsetField != null) {
		var offset = '${GateForm.map.startOffset}',
			offsetFieldText = buildDateString(+offset, false);
		offsetField.innerHTML = offsetFieldText;
	}

</script>