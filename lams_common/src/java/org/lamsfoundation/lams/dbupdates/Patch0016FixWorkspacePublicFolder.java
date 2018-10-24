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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.dbupdates;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.MessageService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tacitknowledge.util.migration.MigrationContext;
import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.MigrationTaskSupport;
import com.tacitknowledge.util.migration.jdbc.DataSourceMigrationContext;

/**
 * @author mseaton
 *
 *         Insert new workspace and workspace folder for new Public Folder -
 *         LDEV2107.
 */
public class Patch0016FixWorkspacePublicFolder extends MigrationTaskSupport {

    private static Logger log = Logger.getLogger(Patch0016FixWorkspacePublicFolder.class);

    private static final Integer LEVEL = new Integer(16);

    private static final String NAME = "FixWkspcPublicFolder";

    private String insertWorkspacePublicFolder = "INSERT INTO lams_workspace_folder (parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id) values(?,?,?,?,?,?);";

    private String insertNewWorkspace = "INSERT INTO lams_workspace (name, default_fld_id) values(?,?)";

    private String insertNewWkspcWkspcFolder = "INSERT INTO lams_wkspc_wkspc_folder (workspace_id, workspace_folder_id) values (?,?)";

    public Patch0016FixWorkspacePublicFolder() {
	setLevel(LEVEL);
	setName(NAME);
    }

    @Override
    public void migrate(MigrationContext context) throws MigrationException {

	// using data source defined in application container
	DataSourceMigrationContext ctx = (DataSourceMigrationContext) context;

	Connection conn = null;

	try {
	    conn = ctx.getConnection();
	    conn.setAutoCommit(false);

	    // add workspace public folder
	    String i18nMessage = getI18nMessage(conn);
	    PreparedStatement query = conn.prepareStatement(insertWorkspacePublicFolder,
		    Statement.RETURN_GENERATED_KEYS);
	    query.setLong(1, new Long(1));
	    query.setString(2, i18nMessage);
	    query.setLong(3, new Long(1));
	    query.setDate(4, new java.sql.Date(System.currentTimeMillis()));
	    query.setDate(5, new java.sql.Date(System.currentTimeMillis()));
	    query.setInt(6, new Integer(3));

	    int numUpdatedWorkspaces = query.executeUpdate();
	    ResultSet results = query.getGeneratedKeys();

	    log.info("Inserted " + numUpdatedWorkspaces + " new workspace folder.");
	    long wkspcFolderId = -1;
	    if (results.next()) {
		wkspcFolderId = results.getLong(1);
	    }

	    // insert new workspace
	    query = conn.prepareStatement(insertNewWorkspace, Statement.RETURN_GENERATED_KEYS);
	    query.setString(1, i18nMessage);
	    query.setLong(2, wkspcFolderId);

	    numUpdatedWorkspaces = query.executeUpdate();
	    results = query.getGeneratedKeys();

	    log.info("Inserted " + numUpdatedWorkspaces + " new workspace.");
	    long wkspcId = -1;
	    if (results.next()) {
		wkspcId = results.getLong(1);
	    }

	    // insert new wkspc to wkspc folder mapping
	    query = conn.prepareStatement(insertNewWkspcWkspcFolder);
	    query.setLong(1, wkspcId);
	    query.setLong(2, wkspcFolderId);

	    numUpdatedWorkspaces = query.executeUpdate();
	    log.info("Inserted " + numUpdatedWorkspaces + " wkspc_wkspc_folder links.");

	    ctx.commit();

	    // conn.close(); // container managed data source
	} catch (Exception e) {
	    ctx.rollback();
	    throw new MigrationException("Problem running update; ", e);
	}
    }

    private String getI18nMessage(Connection conn) throws MigrationException {
	// get spring bean
	ApplicationContext context = new ClassPathXmlApplicationContext("org/lamsfoundation/lams/messageContext.xml");
	MessageService messageService = (MessageService) context.getBean("commonMessageService");

	// get server locale
	String defaultLocale = "en_AU";
	String getDefaultLocaleStmt = "select config_value from lams_configuration where config_key='ServerLanguage'";
	try {
	    PreparedStatement query = conn.prepareStatement(getDefaultLocaleStmt);
	    ResultSet results = query.executeQuery();
	    while (results.next()) {
		defaultLocale = results.getString("config_value");
	    }
	} catch (Exception e) {
	    throw new MigrationException("Problem running update; ", e);
	}

	String[] tokenisedLocale = defaultLocale.split("_");
	Locale locale = new Locale(tokenisedLocale[0], tokenisedLocale[1]);

	// get i18n'd message for text 'run sequences'
	MessageSource messageSource = messageService.getMessageSource();
	String i18nMessage = messageSource.getMessage("public.folder.name", null, locale);

	if (i18nMessage != null && i18nMessage.startsWith("???")) {
	    // default to English if not present
	    return "Public Folder";
	}
	return i18nMessage;
    }

}
