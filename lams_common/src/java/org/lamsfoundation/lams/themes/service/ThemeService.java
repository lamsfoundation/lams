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
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.themes.CSSThemeVisualElement;
import org.lamsfoundation.lams.themes.dao.ICSSThemeDAO;
import org.lamsfoundation.lams.themes.dto.CSSThemeBriefDTO;
import org.lamsfoundation.lams.themes.dto.CSSThemeDTO;
import org.lamsfoundation.lams.usermanagement.dao.IUserDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;

import com.allaire.wddx.WddxDeserializationException;

/**
 * 
 * @author Mitchell Seaton
 *
 */
public class ThemeService implements IThemeService {

	protected Logger log = Logger.getLogger(ThemeService.class);
	
	/** Required DAO's */
	protected ICSSThemeDAO themeDAO;
	protected IUserDAO userDAO;
	
	/** for sending acknowledgment/error messages back to flash */
	private FlashMessage flashMessage;
	
	
	public ThemeService() {
		
	}
	
	/**********************************************
	 * Setter Methods
	 * *******************************************/
	
	/**
     * @return Returns the themeDAO.
     */
    public ICSSThemeDAO getThemeDAO() {
        return themeDAO;
    }
	
    /**
     * 
     * @param themeDAO The ICSSThemeDAO to set.
     */
	public void setThemeDAO(ICSSThemeDAO themeDAO) {
		this.themeDAO = themeDAO;
	}
	
	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#setUserDAO(org.lamsfoundation.lams.usermanagement.dao.IUserDAO)
	 */
	public void setUserDAO(IUserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	
	/**********************************************
	 * Utility/Service Methods
	 * *******************************************/
	

	/**
	 * Store a theme created on a client.
	 * @param wddxPacket The WDDX packet received from Flash
	 * @return String The acknowledgement in WDDX format that the theme has been
	 * 				  successfully saved.
	 * @throws IOException
	 * @throws WddxDeserializationException
	 * @throws Exception
	 */
	public String storeTheme(String wddxPacket) throws Exception {
		
		Hashtable table = (Hashtable)WDDXProcessor.deserialize(wddxPacket);
		
		CSSThemeDTO themeDTO = new CSSThemeDTO(table);
		if ( log.isDebugEnabled() ) {
		    log.debug("Converted Theme packet. Packet was \n"+wddxPacket+
		            "\nDTO is\n"+themeDTO);
		}

		CSSThemeVisualElement dbTheme = null;
		CSSThemeVisualElement storedTheme = null;
		if ( themeDTO.getId() != null )  {
		    // Flash has supplied an id, get the record from the database for update
		    dbTheme = themeDAO.getThemeById(themeDTO.getId());
		}
		
		if ( dbTheme == null ) {
		    storedTheme = themeDTO.createCSSThemeVisualElement();
		} else {
		    storedTheme = themeDTO.updateCSSTheme(dbTheme);
		}
		
		themeDAO.saveOrUpdateTheme(storedTheme);
		flashMessage = new FlashMessage(IThemeService.STORE_THEME_MESSAGE_KEY,storedTheme.getId());
		return flashMessage.serializeMessage(); 		
	}

	/**
	 * Returns a string representing the requested theme in WDDX format
	 * 
	 * @param learningDesignID The learning_design_id of the design whose WDDX packet is requested 
	 * @return String The requested LearningDesign in WDDX format
	 * @throws Exception
	 */
	public String getTheme(Long themeId)throws IOException {
	    CSSThemeVisualElement theme = themeDAO.getThemeById(themeId);
		if(theme==null)
			flashMessage = FlashMessage.getNoSuchTheme("wddxPacket",themeId);
		else{
			CSSThemeDTO dto = new CSSThemeDTO(theme);
			flashMessage = new FlashMessage("getTheme",dto);
		}
		return flashMessage.serializeMessage();
	}


	/**
	 * This method returns a list of all available themes in
	 * WDDX format. We need to work out if this should be restricted
	 * by user.
	 * 
	 * @return String The required information in WDDX format
	 * @throws IOException
	 */
	public String getThemes() throws IOException {
	    List themes = themeDAO.getAllThemes();
		ArrayList themeList = new ArrayList();
		Iterator iterator = themes.iterator();
		while(iterator.hasNext()){
		    CSSThemeBriefDTO dto = new CSSThemeBriefDTO((CSSThemeVisualElement)iterator.next());
		    themeList.add(dto);
		}
		flashMessage = new FlashMessage("getThemes",themeList);		
		return flashMessage.serializeMessage();
	}
	
	/**
	 * Set the User's theme
	 * 
	 * @return String The acknowledgement or error in WDDX format
	 * @throws IOException
	 */
	public String setTheme(Integer userId, Long themeId) throws IOException {
		User user = userDAO.getUserById(userId);
		CSSThemeVisualElement theme = themeDAO.getThemeById(themeId);
		
		if(theme==null)
			flashMessage = FlashMessage.getNoSuchTheme("wddxPacket",themeId);
		else if(user==null)
			flashMessage = FlashMessage.getNoSuchUserExists("wddxPacket", userId);
		else{
			user.setTheme(theme);
			userDAO.updateUser(user);
			flashMessage = new FlashMessage("setTheme", "User theme saved.");
		}
		
		return flashMessage.serializeMessage();
	}
	
	
}