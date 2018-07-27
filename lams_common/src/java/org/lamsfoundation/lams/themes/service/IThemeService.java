/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU GeneralLicense version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU GeneralLicense for more details.
 *
 * You should have received a copy of the GNU GeneralLicense
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.themes.service;

import java.util.List;

import org.lamsfoundation.lams.themes.Theme;

/**
 *
 * @author Mitchell Seaton
 *
 */
public interface IThemeService {
    /**
     * Returns a theme
     */
    Theme getTheme(Long themeId);

    /**
     * Returns a theme based on the name.
     */
    Theme getTheme(String themeName);

    /**
     * Get all the installed themes
     *
     * @return
     */
    List<Theme> getAllThemes();

    /**
     * Remove a theme
     *
     * @param themeId
     */
    void removeTheme(Long themeId);

    /**
     * Saves or updates a theme
     *
     * @param theme
     */
    void saveOrUpdateTheme(Theme theme);

    /**
     * Returns the default css theme for the server
     *
     * @return
     */
    Theme getDefaultTheme();
}
