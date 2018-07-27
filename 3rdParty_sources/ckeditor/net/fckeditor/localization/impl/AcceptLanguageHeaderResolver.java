/*
 * FCKeditor - The text editor for Internet - http://www.fckeditor.net
 * Copyright (C) 2004-2010 Frederico Caldeira Knabben
 * 
 * == BEGIN LICENSE ==
 * 
 * Licensed under the terms of any of the following licenses at your
 * choice:
 * 
 *  - GNU General Public License Version 2 or later (the "GPL")
 *    http://www.gnu.org/licenses/gpl.html
 * 
 *  - GNU Lesser General Public License Version 2.1 or later (the "LGPL")
 *    http://www.gnu.org/licenses/lgpl.html
 * 
 *  - Mozilla Public License Version 1.1 or later (the "MPL")
 *    http://www.mozilla.org/MPL/MPL-1.1.html
 * 
 * == END LICENSE ==
 */
package net.fckeditor.localization.impl;

import java.util.Locale;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import net.fckeditor.localization.LocaleResolver;
import net.fckeditor.tool.Utils;

/**
 * HTTP header implementation of the locale resolver. It resolves the current
 * user locale by calling {@link ServletRequest#getLocale()} if and only if the
 * <code>Accept-Language</code> header is not empty.
 * 
 *
 */
public class AcceptLanguageHeaderResolver implements LocaleResolver {

	public Locale resolveLocale(final HttpServletRequest request) {

		if (Utils.isNotEmpty(request.getHeader("Accept-Language")))
			return request.getLocale();
		else
			return null;
	}

}