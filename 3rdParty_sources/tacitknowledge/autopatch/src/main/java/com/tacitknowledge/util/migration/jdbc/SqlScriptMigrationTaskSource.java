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

package com.tacitknowledge.util.migration.jdbc;

import com.tacitknowledge.util.discovery.ClassDiscoveryUtil;
import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.MigrationTask;
import com.tacitknowledge.util.migration.MigrationTaskSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Search a package (directory) for SQL scripts that a specific pattern and
 * returns corresponding <code>SqlScriptMigrationTasks</code>. The name of
 * each script must follow the pattern of &quot;patch(\d+)(_.+)?\.sql&quot;.
 *
 * @author Scott Askew (scott@tacitknowledge.com)
 */
public class SqlScriptMigrationTaskSource implements MigrationTaskSource
{
    /**
     * Class logger
     */
    private static Log log = LogFactory
            .getLog(SqlScriptMigrationTaskSource.class);

    /**
     * The regular expression used to match SQL patch files.
     */
    private static final String SQL_PATCH_REGEX = "^patch(\\d++)(?!-rollback)_?(.+)?\\.sql";

    /**
     * The regular expression used to match SQL rollback files
     */
    private static final String SQL_ROLLBACK_REGEX = "^patch(\\d++)-rollback_?(.+)?\\.sql";

    /**
     * {@inheritDoc}
     */
    public List<MigrationTask> getMigrationTasks(String packageName) throws MigrationException
    {
        String path = packageName.replace('.', '/');

        String[] upScripts = getSqlScripts(path, SQL_PATCH_REGEX);
        String[] downScripts = getSqlScripts(path, SQL_ROLLBACK_REGEX);

        return createMigrationScripts(upScripts, downScripts);
    }

    /**
     * Returns the SQL scripts which exist in the specified patch and match the specified regex.
     *
     * @param path  the Path to search
     * @param regex the regex which the scripts should match
     * @return a String array of script names.
     */
    private String[] getSqlScripts(String path, String regex)
    {
        String[] scripts = ClassDiscoveryUtil.getResources(path, regex);
        if (log.isDebugEnabled())
        {
            log.debug("Found " + scripts.length + " patches in path: " + path);
            for (int i = 0; i < scripts.length; i++)
            {
                log.debug(" -- \"" + scripts[i] + "\"");
            }
        }
        return scripts;
    }

    /**
     * Creates a list of <code>SqlScriptMigrationTask</code>s based on the
     * array of SQL scripts.
     *
     * @param upScripts   the classpath-relative array of SQL migration scripts
     * @param downScripts the classpath-relative array of SQL migration scripts
     * @return a list of <code>SqlScriptMigrationTask</code>s based on the
     *         array of SQL scripts
     * @throws MigrationException if a SqlScriptMigrationTask could no be created
     */
    private List createMigrationScripts(String[] upScripts, String[] downScripts)
            throws MigrationException
    {
        Pattern upFileNamePattern = Pattern.compile(SQL_PATCH_REGEX);
        Pattern downFileNamePattern = Pattern.compile(SQL_ROLLBACK_REGEX);

        List<MigrationTask> tasks = new ArrayList<MigrationTask>();
        for (int i = 0; i < upScripts.length; i++)
        {
            String script = upScripts[i];

            if (script != null)
            {
                // get the file name
                File scriptFile = new File(script);
                String scriptFileName = scriptFile.getName();

                // Get the version out of the script name
                int order = getOrder(upFileNamePattern, script, scriptFileName);

                // get the down script which matches this patch order
                String downScript = getMatchingDownScript(downScripts, order,
                        downFileNamePattern);

                // read the scripts
                String upSql = readSql(getInputStream(script));
                String downSql = "";

                // if the down script is not the empty string, then try to read
                // it.
                if (!"".equals(downScript))
                    downSql = readSql(getInputStream(downScript));

                // create a new task
                SqlScriptMigrationTask task = new SqlScriptMigrationTask(
                        scriptFileName, order, upSql, downSql);
                task.setName(scriptFileName);

                // add the task to the list of tasks
                tasks.add(task);
            }
        }
        return tasks;
    }

    /**
     * Returns the filename of the downscript which matches the order of the order parameter.
     *
     * @param downScripts an array of scripts
     * @param order       the order of the down script whose name should be returned
     * @return the name of the down script that matches the order
     */
    private String getMatchingDownScript(String[] downScripts, int order,
            Pattern fileNamePattern) throws MigrationException
    {
        boolean isScriptFound = false;
        String script = "";
        for (int i = 0; i < downScripts.length && !isScriptFound; i++)
        {
            File scriptFile = new File(downScripts[i]);
            String scriptFileName = scriptFile.getName();
            int downScriptOrder = getOrder(fileNamePattern, downScripts[i],
                    scriptFileName);

            if (downScriptOrder == order)
            {
                script = downScripts[i];
                isScriptFound = true;
            }
        }

        if (!isScriptFound)
        {
            log.info("There was no rollback script for patch level: " + order);
        }
        return script;
    }

    /**
     * Returns an input stream that points to the script name
     *
     * @param scriptName the name of the script to create an InputStream
     * @return an InputStream returns an InputStream based upon the scriptName
     */
    private InputStream getInputStream(String scriptName)
    {
        scriptName = scriptName.replace('\\', '/');
        log.debug("Examining possible SQL patch file \"" + scriptName + "\"");
        return Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(scriptName);
    }

    /**
     * Reads the file contents into a String object.
     *
     * @param is the <code>InputStream</code>
     * @return a <code>String</code> with the contents of the InputStream
     * @throws MigrationException if there's an error reading in the contents
     */
    private String readSql(InputStream is) throws MigrationException
    {
        StringBuffer sqlBuffer = new StringBuffer();
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));

        try
        {
            String line = buf.readLine();
            while (line != null)
            {
                sqlBuffer.append(line).append("\n");
                line = buf.readLine();
            }
        }
        catch (IOException ioe)
        {
            throw new MigrationException(
                    "There was an error reading in a script", ioe);

        }
        finally
        {
            try
            {
                is.close();
            }
            catch (IOException ioe)
            {
                log.error("Could not close input stream", ioe);
            }
        }
        return sqlBuffer.toString();
    }

    /**
     * Returns the order for the file.
     *
     * @param p              a Pattern defining the file name pattern
     * @param script         the Script
     * @param scriptFileName the name of the file
     * @return an int indicating the order
     * @throws MigrationException in case the file name is invalid
     */
    private int getOrder(Pattern p, String script, String scriptFileName)
            throws MigrationException
    {
        Matcher matcher = p.matcher(scriptFileName);
        if (!matcher.matches() || matcher.groupCount() != 2)
        {
            throw new MigrationException("Invalid SQL script name: " + script);
        }
        int order = Integer.parseInt(matcher.group(1));
        return order;
    }
}
