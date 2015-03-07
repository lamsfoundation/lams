/****************************************************************
 * Copyright (C) 2014 LAMS Foundation (http://lamsfoundation.org)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */ 

package org.lamsfoundation.lams.pages.learner;

import org.lamsfoundation.lams.pages.AbstractPage;
import org.lamsfoundation.lams.pages.tool.forum.learner.TopicIndexPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class ContentFrame extends AbstractPage {

	
	public interface PageResult {}
	
	public ContentFrame(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public TopicIndexPage openLearnerForum() {
		return PageFactory.initElements(driver, TopicIndexPage.class);
		
	}
	
	

	
}
