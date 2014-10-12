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

package com.tacitknowledge.util.discovery;

import java.io.File;
import java.io.Serializable;

import junit.framework.TestCase;

/**
 * Tests the <code>ClassCritera</code> class.
 * <p>
 * This class implements <code>Serializable</code> so that it can be used as
 * test fodder when determining if a named class implements all of a specific
 * number of interfaces.
 * 
 * @author  Scott Askew (scott@tacitknowledge.com)
 */
public class ClassCriteriaTest extends TestCase implements Serializable
{

    /**
     * Constructor for ClassCriteriaTest.
     * 
     * @param name the name of the test to run 
     */
    public ClassCriteriaTest(String name)
    {
        super(name);
    }
    
    /**
     * Valiates the normal operation of <code>matches</code> when matching on a
     * single class.
     */
    public void testMatchesSingleInterface()
    {
        ClassCriteria criteria = new ClassCriteria(getClass());
        String className = getClass().getName().replace('.', File.separatorChar);
        assertTrue(criteria.matches(className + ".class"));
        assertFalse(criteria.matches(new File("java/lang/Object.class").getPath()));
    }

    /**
     * Valiates the normal operation of <code>matches</code> when matching on a
     * single class.
     */
    public void testMatchesMultipleInterfaces()
    {
        ClassCriteria criteria = new ClassCriteria(new Class[] {getClass(), Serializable.class});
        String className = getClass().getName().replace('.', File.separatorChar);
        // This class implements both interfaces
        assertTrue(criteria.matches(className + ".class"));
        // Object doesn't implement either
        assertFalse(criteria.matches(new File("java/lang/Object.class").getPath()));
        // String implement Serializable, but not ClassCriteriaTest
        assertFalse(criteria.matches(new File("java/lang/String.class").getPath()));
    }
    
    /**
     * Ensures that classes that implement the right interfaces but are not
     * concrete themselves are do not match.
     */
    public void testMatchesOnlyConcreteClasses()
    {
        ClassCriteria criteria = new ClassCriteria(Serializable.class);
        String className = TestAbstractClass.class.getName().replace('.', File.separatorChar);
        assertFalse(criteria.matches(className + ".class"));
    }
}