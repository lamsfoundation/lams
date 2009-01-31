					<table>
						<tr>
							<td style="padding-left:0px; border-bottom:0px; vertical-align:middle; background:none;">
								<span class="field-name">
									<fmt:message key="label.authoring.basic.option.answer"></fmt:message>
								</span>
							</td>
							<td style="padding-left:0px; border-bottom:0px; background:none;">	
								<input type="hidden" name="optionSequenceId${status.index}" id="optionSequenceId${status.index}" value="${option.sequenceId}">									
								<input type="text" name="optionAnswer${status.index}"
									id="optionAnswer${status.index}" size="56" value="${option.answerString}">
							</td>									
								
						</tr>
						<tr>
							<td style="padding-left:0px; border-bottom:0px; vertical-align:middle; background:none;;">
								<span class="field-name">
									<fmt:message key="label.authoring.basic.option.grade"></fmt:message>
								</span>
							</td>
							<td style="padding-left:0px; border-bottom:0px; background:none;">
								<html:select property="optionGrade${status.index}" style="float: left" value="${option.grade}">
									<html:option value="100.0">100 %</html:option>
									<html:option value="90.0">90 %</html:option>
									<html:option value="83.333">83.333 %</html:option>
									<html:option value="80.0">80 %</html:option>
									<html:option value="75.0">75%</html:option>
									<html:option value="70.0">70 %</html:option>
									<html:option value="66.666">66.666 %</html:option>
									<html:option value="60.0">60 %</html:option>
									<html:option value="50.0">50 %</html:option>
									<html:option value="40.0">40 %</html:option>
									<html:option value="33.333">33.333 %</html:option>
									<html:option value="30.0">30 %</html:option>
									<html:option value="25.0">25 %</html:option>
									<html:option value="20.0">20 %</html:option>
									<html:option value="16.666">16.666 %</html:option>
									<html:option value="14.2857">14.2857 %</html:option>
									<html:option value="12.5">12.5 %</html:option>
									<html:option value="11.111">11.111 %</html:option>
									<html:option value="10.0">10 %</html:option>
									<html:option value="5.0">5 %</html:option>
									<html:option value="0.0"><fmt:message key="label.authoring.basic.none" /></html:option>
									<html:option value="-5.0">-5 %</html:option>	
									<html:option value="-10.0">-10 %</html:option>
									<html:option value="-11.111">-11.111 %</html:option>
									<html:option value="-12.5">-12.5 %</html:option>
									<html:option value="-14.2857">-14.2857 %</html:option>
									<html:option value="-16.666">-16.666 %</html:option>
									<html:option value="-20.0">-20 %</html:option>
									<html:option value="-25.0">-25 %</html:option>
									<html:option value="-30.0">-30 %</html:option>	
									<html:option value="-33.333">-33.333 %</html:option>
									<html:option value="-40.0">-40 %</html:option>
									<html:option value="-50.0">-50 %</html:option>
									<html:option value="-60.0">-60 %</html:option>
									<html:option value="-66.666">-66.666 %</html:option>
									<html:option value="-70.0">-70 %</html:option>
									<html:option value="-75.0">-75 %</html:option>	
									<html:option value="-80.0">-80 %</html:option>
									<html:option value="-83.333">-83.333 %</html:option>
									<html:option value="-90.0">-90 %</html:option>
									<html:option value="-100.0">-100 %</html:option>
								</html:select>
							</td>
						</tr>
						<tr>
							<td style="padding-left:0px; border-bottom:0px; vertical-align:top; background:none;">
								<span class="field-name">
									<fmt:message key="label.authoring.basic.option.feedback"></fmt:message>
								</span>
							</td>
							<td style="padding-left:0px; border-bottom:0px; background:none;">
								<lams:STRUTS-textarea rows="5" cols="43" tabindex="2" property="optionFeedback${status.index}" styleId="optionFeedback${status.index}" value="${option.feedback}"/>									
							</td>
						</tr>	
					</table>
