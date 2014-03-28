<c:choose>
																			<c:when test="${content.allowRichEditor}">
																				<lams:CKEditor id="updatedResponse" value="${userData.response}"
																					toolbarSet="DefaultMonitor">
																				</lams:CKEditor>
																			</c:when>
																				<c:otherwise>
																					<lams:textarea name="updatedResponse" rows="6" cols="60"><c:out value="${userData.response}" escapeXml="false"/></lams:textarea>
																				</c:otherwise>
																			</c:choose>																			