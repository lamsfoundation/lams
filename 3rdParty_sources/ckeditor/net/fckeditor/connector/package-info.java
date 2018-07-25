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

/**
 * Central interfaces and classes for the connector life cycle. They accept a
 * File Browser request and manage its correct handling.
 * <p>
 * The sequence diagram below depicts the interaction between interfaces and
 * classes:<br />
 * <img src="doc-files/sequence-diagram.png" alt="Connector Life Cycle Diagram" 
 * title="Connector Life Cycle Diagram" />
 * </p>
 *
 */
package net.fckeditor.connector;