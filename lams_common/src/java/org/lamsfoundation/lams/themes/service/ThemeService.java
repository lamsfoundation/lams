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
/* $$Id$$ */
package org.lamsfoundation.lams.themes.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.themes.dao.ICSSThemeDAO;
import org.lamsfoundation.lams.themes.dto.CSSThemeBriefDTO;
import org.lamsfoundation.lams.themes.dto.CSSThemeDTO;
import org.lamsfoundation.lams.themes.exception.ThemeException;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.MessageService;
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
    protected MessageService messageService;
    protected IUserManagementService userManagementService;

    public ThemeService() {

    }

    /**********************************************
     * Setter Methods
     *******************************************/

    /**
     * @return Returns the themeDAO.
     */
    public ICSSThemeDAO getThemeDAO() {
	return themeDAO;
    }

    /**
     *
     * @param themeDAO
     *            The ICSSThemeDAO to set.
     */
    public void setThemeDAO(ICSSThemeDAO themeDAO) {
	this.themeDAO = themeDAO;
    }

    /**
     *
     * @param IUserManagementService
     *            The userManagementService to set.
     */
    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    /**
     * Set i18n MessageService
     */
    @Override
    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    /**
     * Get i18n MessageService
     */
    @Override
    public MessageService getMessageService() {
	return this.messageService;
    }

    /**********************************************
     * Utility/Service Methods
     *******************************************/

    /**
     * Store a theme created on a client.
     *
     * @param wddxPacket
     *            The WDDX packet received from Flash
     * @return String The acknowledgement in WDDX format that the theme has been
     *         successfully saved.
     * @throws IOException
     * @throws WddxDeserializationException
     * @throws Exception
     */
    @Override
    public String storeTheme(String wddxPacket) throws Exception {

	FlashMessage flashMessage = null;
	Hashtable table = (Hashtable) WDDXProcessor.deserialize(wddxPacket);

	CSSThemeDTO themeDTO = new CSSThemeDTO(table);
	if (log.isDebugEnabled()) {
	    log.debug("Converted Theme packet. Packet was \n" + wddxPacket + "\nDTO is\n" + themeDTO);
	}

	Theme dbTheme = null;
	Theme storedTheme = null;
	if (themeDTO.getId() != null) {
	    // Flash has supplied an id, get the record from the database for update
	    dbTheme = themeDAO.getThemeById(themeDTO.getId());
	}

	if (dbTheme == null) {
	    storedTheme = themeDTO.createCSSThemeVisualElement();
	} else {
	    storedTheme = themeDTO.updateCSSTheme(dbTheme);
	}

	themeDAO.saveOrUpdateTheme(storedTheme);
	flashMessage = new FlashMessage(IThemeService.STORE_THEME_MESSAGE_KEY, storedTheme.getThemeId());
	return flashMessage.serializeMessage();
    }

    /**
     * Returns a string representing the requested theme in WDDX format
     *
     * @param themeId
     *            The id of the theme whose WDDX packet is requested
     * @return String The requested theme in WDDX format
     * @throws Exception
     */
    @Override
    public String getThemeWDDX(Long themeId) throws IOException {
	FlashMessage flashMessage = null;
	Theme theme = getTheme(themeId);
	if (theme == null) {
	    flashMessage = FlashMessage.getNoSuchTheme("wddxPacket", themeId);
	} else {
	    CSSThemeDTO dto = new CSSThemeDTO(theme);
	    flashMessage = new FlashMessage("getTheme", dto);
	}
	return flashMessage.serializeMessage();
    }

    /**
     * Returns a theme
     */
    @Override
    public Theme getTheme(Long themeId) {
	return themeDAO.getThemeById(themeId);
    }

    /**
     * Returns a theme based on the name.
     */
    @Override
    public Theme getTheme(String themeName) {
	List themes = themeDAO.getThemeByName(themeName);
	if (themes != null && themes.size() > 0) {
	    return (Theme) themes.get(0);
	} else {
	    return null;
	}
    }

    /**
     * This method returns a list of all available themes in WDDX format. We
     * need to work out if this should be restricted by user.
     *
     * @return String The required information in WDDX format
     * @throws IOException
     */
    @Override
    public String getThemes() throws IOException {
	FlashMessage flashMessage = null;
	List themes = themeDAO.getAllThemes();
	ArrayList<CSSThemeBriefDTO> themeList = new ArrayList<CSSThemeBriefDTO>();
	Iterator iterator = themes.iterator();
	while (iterator.hasNext()) {
	    CSSThemeBriefDTO dto = new CSSThemeBriefDTO((Theme) iterator.next());
	    themeList.add(dto);
	}
	flashMessage = new FlashMessage("getThemes", themeList);
	return flashMessage.serializeMessage();
    }

    /**
     * Set the User's theme
     *
     * @return String The acknowledgement or error in WDDX format
     * @throws IOException
     */
    private FlashMessage setTheme(Integer userId, Long themeId, String type)
	    throws IOException, ThemeException, UserException {
	FlashMessage flashMessage = null;
	User user = (User) userManagementService.findById(User.class, userId);
	Theme theme = themeDAO.getThemeById(themeId);

	if (theme == null) {
	    throw new ThemeException(messageService.getMessage(IThemeService.NO_SUCH_THEME_KEY));
	} else if (user == null) {
	    throw new UserException(messageService.getMessage(IThemeService.NO_SUCH_USER_KEY));
	} else {
	    if (type == null) {
		user.setHtmlTheme(theme);
		user.setFlashTheme(theme);
	    } else if (type.equals(IThemeService.FLASH_KEY)) {
		user.setFlashTheme(theme);
	    } else if (type.equals(IThemeService.HTML_KEY)) {
		user.setHtmlTheme(theme);
	    }

	    userManagementService.save(user);
	    flashMessage = new FlashMessage("setTheme",
		    messageService.getMessage(IThemeService.SET_THEME_SAVED_MESSAGE_KEY));
	}

	return flashMessage;
    }

    /**
     * Set the User's theme (Common)
     *
     * @return FlashMessage The acknowledgement or error in WDDX format
     * @throws IOException
     */
    @Override
    public FlashMessage setTheme(Integer userId, Long themeId) throws IOException, ThemeException, UserException {
	return setTheme(userId, themeId, null);
    }

    /**
     * Set the User's HTML theme
     *
     * @return FlashMessage The acknowledgement or error in WDDX format
     * @throws IOException
     */
    @Override
    public FlashMessage setHtmlTheme(Integer userId, Long themeId) throws IOException, ThemeException, UserException {
	return setTheme(userId, themeId, IThemeService.HTML_KEY);
    }

    /**
     * Set the User's Flash theme
     *
     * @return FlashMessage The acknowledgement or error in WDDX format
     * @throws IOException
     */
    @Override
    public FlashMessage setFlashTheme(Integer userId, Long themeId) throws IOException, ThemeException, UserException {
	return setTheme(userId, themeId, IThemeService.FLASH_KEY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.themes.service.IThemeService#getAllThemes()
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Theme> getAllThemes() {
	return themeDAO.getAllThemes();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.themes.service.IThemeService#removeTheme(java.lang.Long)
     */
    @Override
    public void removeTheme(Long themeId) {
	themeDAO.deleteThemeById(themeId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.themes.service.IThemeService#saveOrUpdateTheme(org.lamsfoundation.lams.themes.Theme)
     */
    @Override
    public void saveOrUpdateTheme(Theme theme) {
	themeDAO.saveOrUpdateTheme(theme);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.themes.service.IThemeService#getDefaultTheme()
     */
    @Override
    public Theme getDefaultCSSTheme() {
	List<Theme> themes = getAllThemes();
	String defaultTheme = Configuration.get(ConfigurationKeys.DEFAULT_HTML_THEME);
	for (Theme theme : themes) {
	    if (theme.getName().equals(defaultTheme)) {
		return theme;
	    }
	}

	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.themes.service.IThemeService#getDefaultTheme()
     */
    @Override
    public Theme getDefaultFlashTheme() {
	List<Theme> themes = getAllThemes();
	String defaultTheme = Configuration.get(ConfigurationKeys.DEFAULT_FLASH_THEME);
	for (Theme theme : themes) {
	    if (theme.getName().equals(defaultTheme)) {
		return theme;
	    }
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.themes.service.IThemeService#getAlCSSThemes()
     */
    @Override
    public List<Theme> getAllCSSThemes() {
	return themeDAO.getAllCSSThemes();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.themes.service.IThemeService#getAlFlashThemes()
     */
    @Override
    public List<Theme> getAllFlashThemes() {
	return themeDAO.getAllFlashThemes();
    }

}