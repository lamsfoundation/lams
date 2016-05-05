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
import java.util.List;

import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.themes.exception.ThemeException;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.wddx.FlashMessage;

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

    /** Key for saving Flash theme for user */
    public static final String FLASH_KEY = "flash";

    /** Key for saving Html Theme for user */
    public static final String HTML_KEY = "theme.service.setTheme.saved";

    public static final String NO_SUCH_THEME_TYPE_KEY = "theme.service.setTheme.type.invalid";

    public static final String NO_SUCH_THEME_KEY = "theme.service.setTheme.noSuchTheme";

    public static final String NO_SUCH_USER_KEY = "theme.service.setTheme.noSuchUser";

    public void setMessageService(MessageService messageService);

    public MessageService getMessageService();

    /**
     * Store a theme created on a client.
     * 
     * @param wddxPacket
     *            The WDDX packet received from Flash
     * @return String The acknowledgement in WDDX format that the theme has been
     *         successfully saved.
     * @throws Exception
     */
    public String storeTheme(String wddxPacket) throws Exception;

    /**
     * Returns a string representing the requested theme in WDDX format
     * 
     * @param themeId
     *            The id of the theme whose WDDX packet is requested
     * @return String The requested theme in WDDX format
     * @throws Exception
     */
    public String getThemeWDDX(Long themeId) throws IOException;

    /**
     * Returns a theme
     */
    public Theme getTheme(Long themeId);

    /**
     * Returns a theme based on the name.
     */
    public Theme getTheme(String themeName);

    /**
     * This method returns a list of all available themes in
     * WDDX format. We need to work out if this should be restricted
     * by user.
     * 
     * @return String The required information in WDDX format
     * @throws IOException
     */
    public String getThemes() throws IOException;

    public FlashMessage setTheme(Integer userId, Long themeId) throws IOException, ThemeException, UserException;

    public FlashMessage setHtmlTheme(Integer userId, Long themeId) throws IOException, ThemeException, UserException;

    public FlashMessage setFlashTheme(Integer userId, Long themeId) throws IOException, ThemeException, UserException;

    /**
     * Get all the installed themes
     * 
     * @return
     */
    public List<Theme> getAllThemes();

    /**
     * Get all the installed CSS themes
     * 
     * @return
     */
    public List<Theme> getAllCSSThemes();

    /**
     * Get all the installed flash themes
     * 
     * @return
     */
    public List<Theme> getAllFlashThemes();

    /**
     * Remove a theme
     * 
     * @param themeId
     */
    public void removeTheme(Long themeId);

    /**
     * Saves or updates a theme
     * 
     * @param theme
     */
    public void saveOrUpdateTheme(Theme theme);

    /**
     * Returns the default css theme for the server
     * 
     * @return
     */
    public Theme getDefaultCSSTheme();

    /**
     * Returns the default theme for the server
     * 
     * @return
     */
    public Theme getDefaultFlashTheme();
}
