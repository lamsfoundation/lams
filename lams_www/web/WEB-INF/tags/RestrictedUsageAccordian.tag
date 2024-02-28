<%
	 /****************************************************************
	 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
	 * =============================================================
	 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
	 * 
	 * This program is free software; you can redistribute it and/or modify
	 * it under the terms of the GNU General Public License version 2.0
	 * as published by the Free Software Foundation.
	 * 
	 * This program is distributed in the hope that it will be useful,
	 * but WITHOUT ANY WARRANTY; without even the implied warranty of
	 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	 * GNU General Public License for more details.
	 * 
	 * You should have received a copy of the GNU General Public License
	 * along with this program; if not, write to the Free Software
	 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
	 * USA
	 * 
	 * http://www.gnu.org/licenses/gpl.txt
	 * ****************************************************************
	 */

	/**
	 *  RestrictedUsageAccordian.tag
	 *	Author: Fiona Malikoff
	 *	Description: Creates the show/hide entry for the Restricted Usage Settings in Monitoring.
	 */
%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<%@ attribute name="titleLabel" required="false" rtexprvalue="true"%>
<%@ attribute name="submissionDeadline" required="false" rtexprvalue="true"%>
<%@ attribute name="cssClass" required="false" rtexprvalue="true"%>

<c:if test="${empty cssClass}">
	<c:set var="cssClass" value="my-2" />
</c:if>

<div class="accordion ${cssClass}" id="accordionRestrictUsageDiv"> 
    <div class="accordion-item">
    	<h2 class="accordion-header" id="headingRestrictedUsageDiv">
        	<button class="accordion-button collapsed text-bg-secondary" type="button" data-bs-toggle="collapse" data-bs-target="#restrictUsageDiv" aria-expanded="false" aria-controls="restrictUsageDiv">
	          	<fmt:message key="monitor.summary.date.restriction" />
    		</button>
        </h2>

		<div id="restrictUsageDiv" class="accordion-collapse collapse p-3" aria-labelledby="headingRestrictedUsageDiv">
			<p>
				<fmt:message key="monitor.summary.when.date.restriction.is.set" />
			</p>
			
			<div id="datetimeDiv" <c:if test='${not empty submissionDeadline}'> style="display: none;" </c:if>>
				<div class="row align-items-center">
					<div class="col-auto pe-0">
						<label for="datetime">
							<fmt:message key="monitor.summary.after.date" />
						</label>
					</div>
					
					<div class="col-auto pe-0">
						<input type="text" name="datetime" id="datetime" value="" class="form-control form-control-inline" autocomplete="off"/>
					</div>
					
					<div class="col-auto pe-0">
						<button type="button" onclick="setSubmissionDeadline();" class="btn btn-secondary">
							<i class="fa-solid fa-wrench me-1"></i>
							<fmt:message key="monitor.summary.set.restriction" />
						</button>
					</div>
				</div>
			</div>
		
			<div id="dateInfoDiv" <c:if test='${empty submissionDeadline}'> style="display: none;" </c:if>>
				<span>
					<fmt:message key="monitor.summary.after.date" />
					<span id="dateInfo" class="badge text-bg-danger mx-2"></span>
				</span>
				
				<button type="button" onclick="removeSubmissionDeadline();" class="btn btn-secondary">
					<i class="fa-solid fa-trash-can me-1"></i>
					<fmt:message key="monitor.summary.unset.restriction" />
				</button>
			</div>
      	</div>
	</div>
</div>
