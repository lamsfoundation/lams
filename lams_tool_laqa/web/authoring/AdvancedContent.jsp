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
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<%@ include file="/common/taglibs.jsp"%>

<p class="small-space-top">
	<html:checkbox property="showOtherAnswers" value="1" styleId="showOtherAnswers" onclick="updateUsernameVisible(this);"
		styleClass="noBorder">
	</html:checkbox>
	<label for="showOtherAnswers">
		<fmt:message key="label.learner.answer" />
	</label>

	<br>	
	&nbsp&nbsp<html:checkbox property="usernameVisible" value="1" styleId="usernameVisible"
		styleClass="noBorder">
	</html:checkbox>
	<label for="usernameVisible">
		<fmt:message key="label.show.names" />
	</label>
</p>


<p>
	<html:checkbox property="reflect" value="1" styleClass="noBorder" styleId="reflect">
	</html:checkbox>
	<label for="reflect">
		<fmt:message key="label.reflect" />
	</label>
</p>
<p>
	<html:textarea cols="30" rows="3" property="reflectionSubject"></html:textarea>
</p>

<p>
	<html:checkbox property="questionsSequenced" value="1" styleId="questionsSequenced"
		styleClass="noBorder">
	</html:checkbox>
	<label for="questionsSequenced">
		<fmt:message key="radiobox.questionsSequenced" />
	</label>
</p>

<p>
	<html:checkbox property="lockWhenFinished" value="1" styleId="lockWhenFinished"
		styleClass="noBorder">
	</html:checkbox>
	<label for="lockWhenFinished">
		<fmt:message key="label.lockWhenFinished" />
	</label>
</p>









