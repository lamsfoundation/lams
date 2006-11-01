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
package org.lamsfoundation.lams.util;

/**
 * Known keys for the configuration data.
 * 
 * @author fmalikoff
 * 
 */
public class ConfigurationKeys {

	public static String ROOT = "Lams";

	public static String SERVER_URL = "ServerURL";

	public static String VERSION = "Version";

	public static String LAMS_TEMP_DIR = "TempDir";

	/**
	 * Directory in which lams.ear is deployed. Usually
	 * {JBOSS}/server/default/deploy/lams.ear
	 */
	public static String LAMS_EAR_DIR = "EARDir";

	public static String SMTP_SERVER = "SMTPServer";

	public static String LAMS_ADMIN_EMAIL = "LamsSupportEmail";

	/**
	 * Flash crash dump files (see FlashCrashDump servlet) are written to the
	 * Dump directory
	 */
	public static String LAMS_DUMP_DIR = "DumpDir";

	public static String CONTENT_REPOSITORY_PATH = "ContentRepositoryPath";

	public static String UPLOAD_FILE_MAX_SIZE = "UploadFileMaxSize";

	public static String UPLOAD_FILE_LARGE_MAX_SIZE = "UploadLargeFileMaxSize";

	public static String UPLOAD_FILE_MAX_MEMORY_SIZE = "UploadFileMaxMemorySize";

	public static String CHAT_SERVER_NAME = "ChatServerName";

	public static String CHAT_PORT_NUMBER = "ChatPortNumber";

	public static String EXE_EXTENSIONS = "ExecutableExtensions";

	public static String LICENSE_TICKET_FILE = "TicketFile";

	public static String PREVIEW_CLEANUP_NUM_DAYS = "CleanupPreviewOlderThanDays";

	/**
	 * Number of milliseconds before a user is considered "inactive".
	 * "Anonymous" sessions are ended after this period (ie ones that haven't
	 * ever access one of the clients )
	 */
	public static String INACTIVE_TIME = "UserInactiveTimeout";

	/**
	 * Allow more than one session to exist for one user. Needed for the test
	 * harness Do not set this parameter to true in production.
	 */
	public static String ALLOW_MULTIPLE_LOGIN = "AllowMultipleLogin";

	/**
	 * Turn on the cache debugging listener. Logs whenever an item is
	 * added/removed/evicted to/from the cache. Not on in production. Must be
	 * set to a boolean value
	 */
	public static String USE_CACHE_DEBUG_LISTENER = "UseCacheDebugListener";

	/** Value for controlling style colour on Canvas Activities */
	public static String AUTHORING_ACTS_COLOUR = "AuthoringActivitiesColour";
	
	/** Values for client updates */
	public static String AUTHORING_CLIENT_VERSION = "AuthoringClientVersion";

	public static String LEARNER_CLIENT_VERSION = "LearnerClientVersion";

	public static String MONITOR_CLIENT_VERSION = "MonitorClientVersion";

	public static String SERVER_VERSION_NUMBER = "ServerVersionNumber";

	/** Default locale for the server. Originally en_AU */
	public static String SERVER_LANGUAGE = "ServerLanguage";

	/** Direction (left to right, right to left) for writing on HTML pages. Originally LTR */
	public static String SERVER_PAGE_DIRECTION = "ServerPageDirection";

	/** universal date of dictionary updates */
	public static String DICTIONARY_DATE_CREATED = "DictionaryDateCreated";

	public static String HELP_URL = "HelpURL";
	
	public static String XMPP_DOMAIN = "XmppDomain";

	public static String XMPP_CONFERENCE = "XmppConference";

	public static String XMPP_ADMIN = "XmppAdmin";

	public static String XMPP_PASSWORD = "XmppPassword";

	public static String DEFAULT_FLASH_THEME = "DefaultFlashTheme";
	
	public static String DEFAULT_HTML_THEME = "DefaultHTMLTheme";
	
	public static String ALLOW_DIRECT_LESSON_LAUNCH = "AllowDirectLessonLaunch";
}