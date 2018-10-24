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

package com.tacitknowledge.util.migration;

import java.util.List;

/**
 * A source of <code>MigrationTask</code>s.
 *
 * @author Scott Askew (scott@tacitknowledge.com)
 */
public interface MigrationTaskSource
{
    /**
     * Returns a list of <code>MigrationTasks</code> that are in the given
     * package.
     *
     * @param packageName to package to search for migration tasks
     * @return a list of migration tasks; if not tasks were found, then an empty
     *         list must be returned.
     * @throws MigrationException if an unrecoverable error occurs
     */
    public List<MigrationTask> getMigrationTasks(String packageName) throws MigrationException;
}
