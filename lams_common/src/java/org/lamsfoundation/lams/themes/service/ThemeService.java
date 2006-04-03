/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
 * ****************************************************************
 */
/* $$Id$$ */
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
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import com.allaire.wddx.WddxDeserializationException;
import org.lamsfoundation.lams.util.MessageService;

import org.lamsfoundation.lams.themes.exception.ThemeException;

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
	protected MessageService messageService;
	
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
	
	/**
	 * Set i18n MessageService
	 */
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	/**
	 * Get i18n MessageService
	 */
	public MessageService getMessageService() {
		return this.messageService;
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
	private FlashMessage setTheme(Integer userId, Long themeId, String type) throws IOException, ThemeException, UserException {
		User user = userDAO.getUserById(userId);
		CSSThemeVisualElement theme = themeDAO.getThemeById(themeId);
		
		if(theme==null)
			throw new ThemeException(messageService.getMessage(IThemeService.NO_SUCH_THEME_KEY));
		else if(user==null)
			throw new UserException(messageService.getMessage(IThemeService.NO_SUCH_USER_KEY));
		else{
			if(type==null) {
				user.setHtmlTheme(theme);
				user.setFlashTheme(theme);
			} 
			else if(type.equals(IThemeService.FLASH_KEY))
				user.setFlashTheme(theme);
			
			else if(type.equals(IThemeService.HTML_KEY))
				user.setHtmlTheme(theme);
			
			userDAO.updateUser(user);
			flashMessage = new FlashMessage("setTheme", messageService.getMessage(IThemeService.SET_THEME_SAVED_MESSAGE_KEY));
		}
		
		return flashMessage;
	}
	
	/**
	 * Set the User's theme (Common)
	 * 
	 * @return FlashMessage The acknowledgement or error in WDDX format
	 * @throws IOException
	 */
	public FlashMessage setTheme(Integer userId, Long themeId) throws IOException, ThemeException, UserException {
		return setTheme(userId, themeId, null);
	}
	
	/**
	 * Set the User's HTML theme
	 * 
	 * @return FlashMessage The acknowledgement or error in WDDX format
	 * @throws IOException
	 */
	public FlashMessage setHtmlTheme(Integer userId, Long themeId) throws IOException, ThemeException, UserException {
		return setTheme(userId, themeId, IThemeService.HTML_KEY);
	}
	
	/**
	 * Set the User's Flash theme
	 * 
	 * @return FlashMessage The acknowledgement or error in WDDX format
	 * @throws IOException
	 */
	public FlashMessage setFlashTheme(Integer userId, Long themeId) throws IOException, ThemeException, UserException {
		return setTheme(userId, themeId, IThemeService.FLASH_KEY);
	}
	
	
}