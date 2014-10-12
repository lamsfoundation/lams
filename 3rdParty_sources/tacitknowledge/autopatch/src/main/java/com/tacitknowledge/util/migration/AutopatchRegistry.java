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


import com.tacitknowledge.util.migration.jdbc.JdbcMigrationLauncherFactoryLoader;
import com.tacitknowledge.util.migration.jdbc.StandaloneMigrationLauncher;
import com.tacitknowledge.util.migration.jdbc.util.MigrationUtil;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.injectors.CompositeInjection;
import org.picocontainer.injectors.ConstructorInjection;
import org.picocontainer.injectors.SetterInjection;

/**
 * AutopatchRegistry using PicoContainer to set all the dependencies of the application, we favor
 * constructor injection over other methods of injection.
 *
 * @author Oscar Gonzalez (oscar@tacitknowledge.com)
 */
public class AutopatchRegistry
{
    MutablePicoContainer pico;

    public PicoContainer configureContainer(){
        pico = new DefaultPicoContainer();
        pico.addComponent(StandaloneMigrationLauncher.class);
        pico.addComponent(MigrationUtil.class);
        pico.start();
        return pico;
    }

    public void destroyContainer(){
        pico.stop();
        pico.dispose();
    }

}
