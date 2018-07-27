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
 * @author jliew
 *
 *         Fix workspace folder names for groups that have been renamed -
 *         LDEV1447.
 */
public class Patch0012FixWorkspaceNames extends MigrationTaskSupport {

    private static Logger log = Logger.getLogger(Patch0012FixWorkspaceNames.class);

    private static final Integer LEVEL = new Integer(12);

    private static final String NAME = "FixRunSeqFolders";

    private String updateWorkspaceName = "UPDATE lams_workspace w, lams_organisation o " + "SET w.name=o.name "
	    + "WHERE o.workspace_id=w.workspace_id";

    private String updateFolderName = "UPDATE lams_workspace_folder wf, lams_workspace w, lams_organisation o "
	    + "SET wf.name=o.name " + "WHERE o.workspace_id=w.workspace_id "
	    + "AND w.default_fld_id=wf.workspace_folder_id";

    private String updateRunSeqFolderName = "UPDATE lams_workspace_folder wf, lams_workspace w, lams_organisation o "
	    + "SET wf.name=concat(o.name, ?) " + "WHERE o.workspace_id=w.workspace_id "
	    + "AND w.def_run_seq_fld_id=wf.workspace_folder_id";

    public Patch0012FixWorkspaceNames() {
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

	    // update workspace names
	    PreparedStatement query = conn.prepareStatement(updateWorkspaceName);
	    int numUpdatedWorkspaces = query.executeUpdate();
	    log.info("Updated " + numUpdatedWorkspaces + " workspace names.");

	    // update workspace folder names
	    query = conn.prepareStatement(updateFolderName);
	    int numUpdatedFolderNames = query.executeUpdate();
	    log.info("Updated " + numUpdatedFolderNames + " workspace folder names.");

	    // update run sequences workspace folder names
	    String i18nMessage = getI18nMessage(conn);
	    query = conn.prepareStatement(updateRunSeqFolderName);
	    query.setString(1, i18nMessage);
	    int numUpdatedRunSeqFolderNames = query.executeUpdate();
	    log.info("Updated " + numUpdatedRunSeqFolderNames + " run sequences workspace folder names.");

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
	String i18nMessage = messageSource.getMessage("runsequences.folder.name", new Object[] { "" }, locale);

	if (i18nMessage != null && i18nMessage.startsWith("???")) {
	    // default to English if not present
	    return " Run Sequences";
	}
	return i18nMessage;
    }

}
