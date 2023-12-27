<c:set var="adTitle"><spring:escapeBody javaScriptEscape="true"><fmt:message key="monitor.summary.th.advancedSettings" /></spring:escapeBody></c:set>
<lams:AdvancedAccordian title="${adTitle}">
             
<table class="table table-striped table-condensed">

		<tr>
			<td>
				<fmt:message key="label.authoring.advance.lock.on.finished" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${forum.lockWhenFinished == true}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		
		<tr>
			<td>
				<fmt:message key="label.authoring.advance.allow.edit" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${forum.allowEdit == true}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="label.authoring.advance.enable.anonymous.posts" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${forum.allowAnonym}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		
		<tr>
			<td>
				<fmt:message key="label.authoring.advance.allow.rate.postings" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${forum.allowRateMessages}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>

		<tr>
			<td>
				<fmt:message key="label.authoring.advance.minimum.reply" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${forum.minimumRate == 0}">
						<fmt:message key="label.authoring.advance.no.minimum" />
					</c:when>
					<c:otherwise>
						${forum.minimumRate}
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="label.authoring.advance.maximum.reply" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${forum.maximumRate == 0}">
						<fmt:message key="label.authoring.advance.no.maximum" />
					</c:when>
					<c:otherwise>
						${forum.maximumRate}
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="label.authoring.advance.allow.upload" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${forum.allowUpload == true}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="label.authoring.advance.use.richeditor" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${forum.allowRichEditor == true}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="label.authoring.advance.min.limited.input" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${forum.limitedMinCharacters == true}">
						<fmt:message key="label.on" />, ${forum.minCharacters}
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="label.authoring.advance.limited.input" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${forum.limitedMaxCharacters == true}">
						<fmt:message key="label.on" />, ${forum.maxCharacters}
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="label.authoring.advance.allow.new.topics" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${forum.allowNewTopic == true}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="label.authoring.advance.number.reply" />
			</td>
			
			<td>
				<fmt:message key="label.authoring.advance.minimum.reply" />
				<c:choose>
					<c:when test="${forum.minimumReply != 0}">
						${forum.minimumReply}
					</c:when>
					<c:otherwise>
						<fmt:message key="label.authoring.advance.no.minimum" />
					</c:otherwise>
				</c:choose>
				<br />
				
				<fmt:message key="label.authoring.advance.maximum.reply" />
				<c:choose>
					<c:when test="${forum.maximumReply != 0}">
						${forum.maximumReply}
					</c:when>
					<c:otherwise>
						<fmt:message key="label.authoring.advance.no.maximum" />
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="label.authoring.advanced.send.emails.to" /> <fmt:message key="label.authoring.advanced.learners" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${forum.notifyLearnersOnForumPosting == true}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="label.authoring.advanced.send.emails.to" /> <fmt:message key="label.authoring.advanced.teachers" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${forum.notifyTeachersOnForumPosting == true}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="label.authoring.advanced.notify.mark.release" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${forum.notifyLearnersOnMarkRelease == true}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
</table>
</lams:AdvancedAccordian>
