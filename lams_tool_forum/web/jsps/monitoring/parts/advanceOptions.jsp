<c:set var="adTitle"><spring:escapeBody javaScriptEscape="true"><fmt:message key="monitor.summary.th.advancedSettings" /></spring:escapeBody></c:set>
<lams:AdvancedAccordian title="${adTitle}">             
	<div class="ltable table-striped table-sm no-header mb-0">
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.lock.on.finished" />
			</div>
			
			<div class="col text-start">
				<c:choose>
					<c:when test="${forum.lockWhenFinished == true}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>

		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.allow.edit" />
			</div>
			
			<div class="col text-start">
				<c:choose>
					<c:when test="${forum.allowEdit == true}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
		
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.enable.anonymous.posts" />
			</div>
			
			<div class="col text-start">
				<c:choose>
					<c:when test="${forum.allowAnonym}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
		
		
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.allow.rate.postings" />
			</div>
			
			<div class="col text-start">
				<c:choose>
					<c:when test="${forum.allowRateMessages}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>

		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.minimum.reply" />
			</div>
			
			<div class="col text-start">
				<c:choose>
					<c:when test="${forum.minimumRate == 0}">
						<fmt:message key="label.authoring.advance.no.minimum" />
					</c:when>
					<c:otherwise>
						${forum.minimumRate}
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
		
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.maximum.reply" />
			</div>
			
			<div class="col text-start">
				<c:choose>
					<c:when test="${forum.maximumRate == 0}">
						<fmt:message key="label.authoring.advance.no.maximum" />
					</c:when>
					<c:otherwise>
						${forum.maximumRate}
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
		
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.allow.upload" />
			</div>
			
			<div class="col text-start">
				<c:choose>
					<c:when test="${forum.allowUpload == true}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
		
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.use.richeditor" />
			</div>
			
			<div class="col text-start">
				<c:choose>
					<c:when test="${forum.allowRichEditor == true}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
		
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.min.limited.input" />
			</div>
			
			<div class="col text-start">
				<c:choose>
					<c:when test="${forum.limitedMinCharacters == true}">
						<fmt:message key="label.on" />, ${forum.minCharacters}
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
		
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.limited.input" />
			</div>
			
			<div class="col text-start">
				<c:choose>
					<c:when test="${forum.limitedMaxCharacters == true}">
						<fmt:message key="label.on" />, ${forum.maxCharacters}
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
		
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.allow.new.topics" />
			</div>
			
			<div class="col text-start">
				<c:choose>
					<c:when test="${forum.allowNewTopic == true}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
		
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.number.reply" />
			</div>
			
			<div class="col text-start">
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
			</div>
		</div>
		
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advanced.send.emails.to" /> <fmt:message key="label.authoring.advanced.learners" />
			</div>
			
			<div class="col text-start">
				<c:choose>
					<c:when test="${forum.notifyLearnersOnForumPosting == true}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
		
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advanced.send.emails.to" /> <fmt:message key="label.authoring.advanced.teachers" />
			</div>
			
			<div class="col text-start">
				<c:choose>
					<c:when test="${forum.notifyTeachersOnForumPosting == true}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
		
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advanced.notify.mark.release" />
			</div>
			
			<div class="col text-start">
				<c:choose>
					<c:when test="${forum.notifyLearnersOnMarkRelease == true}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
	</div>
</lams:AdvancedAccordian>
