/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */
package org.lamsfoundation.lams.themes.service;

import java.io.IOException;

import org.lamsfoundation.lams.usermanagement.dao.IUserDAO;
import org.lamsfoundation.lams.themes.dao.ICSSThemeDAO;

/**
 * 
 * @author Mitchell Seaton
 *
 */
public interface IThemeService {
	
	/** Message key returned by the storeTheme() method */
	public static final String STORE_THEME_MESSAGE_KEY = "storeTheme";
	
	/** Message key for successful saved theme - setTheme() method */
	public static final String SET_THEME_SAVED_MESSAGE_KEY = "theme.service.setTheme.saved";
	
	/**
     * Set IThemeDAO
     *
     * @param themeDao
     */
	public void setThemeDAO(ICSSThemeDAO themeDao);

	
	/**
     * Set IUserDAO
     *
     * @param userDao
     */
	public void setUserDAO(IUserDAO userDao);

	
	/**
	 * Store a theme created on a client.
	 * @param wddxPacket The WDDX packet received from Flash
	 * @return String The acknowledgement in WDDX format that the theme has been
	 * 				  successfully saved.
	 * @throws Exception
	 */
	public String storeTheme(String wddxPacket) throws Exception;

	/**
	 * Returns a string representing the requested theme in WDDX format
	 * 
	 * @param learningDesignID The learning_design_id of the design whose WDDX packet is requested 
	 * @return String The requested LearningDesign in WDDX format
	 * @throws Exception
	 */
	public String getTheme(Long themeId)throws IOException;


	/**
	 * This method returns a list of all available themes in
	 * WDDX format. We need to work out if this should be restricted
	 * by user.
	 * 
	 * @return String The required information in WDDX format
	 * @throws IOException
	 */
	public String getThemes() throws IOException;
	
	
	public String setTheme(Integer userId, Long themeId) throws IOException;
}

