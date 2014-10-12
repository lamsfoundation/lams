/* Copyright 2004 Tacit Knowledge
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tacitknowledge.util.migration.jdbc.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Util class for Sybase specific functionality.
 *
 * @author Alex Soto (apsoto@gmail.com)
 */
public class SybaseUtil
{

    /**
     * alter table pattern - this one is actually not documented
     */
    protected static final Pattern ALTER_TABLE_PATTERN =
            Pattern.compile("(?is).*alter\\s+table.*");

    /**
     * alter database pattern
     */
    protected static final Pattern ALTER_DATABASE_PATTERN =
            Pattern.compile("(?is).*alter\\s+database.*");

    /**
     * create database pattern
     */
    protected static final Pattern CREATE_DATABASE_PATTERN =
            Pattern.compile("(?is).*create\\s+database.*");

    /**
     * dbcc reindex pattern
     */
    protected static final Pattern DBCC_REINDEX_PATTERN =
            Pattern.compile("(?is).*dbcc\\s+reindex.*");

    /**
     * dbcc_fix_text pattern
     */
    protected static final Pattern DBCC_FIX_TEXT_PATTERN =
            Pattern.compile("(?is).*dbcc\\s+fix_text.*");

    /**
     * drop database pattern
     */
    protected static final Pattern DROP_DATABASE_PATTERN =
            Pattern.compile("(?is).*drop\\s+database.*");

    /**
     * dump database pattern
     */
    protected static final Pattern DUMP_DATABASE_PATTERN =
            Pattern.compile("(?is).*dump\\s+database.*");

    /**
     * dump transaction pattern
     */
    protected static final Pattern DUMP_TRANSACTION_PATTERN =
            Pattern.compile("(?is).*dump\\s+transaction.*");

    /**
     * load database pattern
     */
    protected static final Pattern LOAD_DATABASE_PATTERN =
            Pattern.compile("(?is).*load\\s+database.*");

    /**
     * load transaction pattern
     */
    protected static final Pattern LOAD_TRANSACTION_PATTERN =
            Pattern.compile("(?is).*load\\s+transaction.*");

    /**
     * select into pattern
     */
    protected static final Pattern SELECT_INTO_PATTERN =
            Pattern.compile("(?is).*select\\s+into.*");

    /**
     * set transaction isolation level pattern
     */
    protected static final Pattern SET_TRANSACTION_ISOLATION_LEVEL_PATTERN =
            Pattern.compile("(?is).*set\\s+transaction\\s+isolation\\s+level.*",
                    Pattern.CASE_INSENSITIVE & Pattern.MULTILINE);

    /**
     * truncate table pattern
     */
    protected static final Pattern TRUNCATE_TABLE_PATTERN =
            Pattern.compile("(?is).*truncate\\s+table.*");

    /**
     * update statistics pattern
     */
    protected static final Pattern UPDATE_STATISTICS_PATTERN =
            Pattern.compile("(?is).*update\\s+statistics.*");

    /**
     * setuser pattern
     */
    protected static final Pattern SETUSER_PATTERN =
            Pattern.compile("(?is).*setuser.*");

    /**
     * List of {@link Pattern} of statements that are illegal in multi
     * statement transactions.
     */
    protected static final List ILLEGAL_MULTISTATEMENT_TRANSACTION_COMMANDS;

    static
    {
        ArrayList list = new ArrayList();
        list.add(ALTER_DATABASE_PATTERN);
        list.add(ALTER_TABLE_PATTERN);
        list.add(CREATE_DATABASE_PATTERN);
        list.add(DBCC_FIX_TEXT_PATTERN);
        list.add(DBCC_REINDEX_PATTERN);
        list.add(DROP_DATABASE_PATTERN);
        list.add(DUMP_DATABASE_PATTERN);
        list.add(DUMP_TRANSACTION_PATTERN);
        list.add(LOAD_DATABASE_PATTERN);
        list.add(LOAD_TRANSACTION_PATTERN);
        list.add(SELECT_INTO_PATTERN);
        list.add(SET_TRANSACTION_ISOLATION_LEVEL_PATTERN);
        list.add(SETUSER_PATTERN);
        list.add(TRUNCATE_TABLE_PATTERN);
        list.add(UPDATE_STATISTICS_PATTERN);
        ILLEGAL_MULTISTATEMENT_TRANSACTION_COMMANDS = Collections.unmodifiableList(list);
    }

    /**
     * Singleton
     */
    protected SybaseUtil()
    {
        // does nothing
    }

    /**
     * Determines whether the statement (typically some sql) contains one of the
     * sql commands that are not allowed in a multi-statement transaction.
     *
     * @param statement the text to check
     * @return true if one of the illegal commands is found in the statement.
     * @see <a href="http://manuals.sybase.com/onlinebooks/group-as/asg1250e/svrtsg/Generic__BookTextView/13155;pt=13085">
     *      Sybase Troubleshooting and Error Messages Guide</a>
     */
    public static boolean containsIllegalMultiStatementTransactionCommand(String statement)
    {
        boolean contains = false;

        for (Iterator iter = ILLEGAL_MULTISTATEMENT_TRANSACTION_COMMANDS.iterator();
             iter.hasNext();)
        {
            Pattern pattern = (Pattern) iter.next();
            Matcher matcher = pattern.matcher(statement);
            if (matcher.matches())
            {
                contains = true;
                break;
            }
        }

        return contains;
    }
}
