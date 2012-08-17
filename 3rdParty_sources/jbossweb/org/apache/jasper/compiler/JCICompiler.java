/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jasper.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.apache.commons.jci.compilers.CompilationResult;
import org.apache.commons.jci.compilers.JavaCompiler;
import org.apache.commons.jci.compilers.JavaCompilerFactory;
import org.apache.commons.jci.compilers.JavaCompilerSettings;
import org.apache.commons.jci.problems.CompilationProblem;
import org.apache.commons.jci.readers.FileResourceReader;
import org.apache.commons.jci.stores.FileResourceStore;
import org.apache.jasper.JasperException;

/**
 * JDT class compiler. This compiler will load source dependencies from the
 * context classloader, reducing dramatically disk access during 
 * the compilation process.
 *
 * @author Cocoon2
 * @author Remy Maucherat
 */
public class JCICompiler extends org.apache.jasper.compiler.Compiler {

    
    /** 
     * Compile the servlet from .java file to .class file
     */
    protected void generateClass(String[] smap)
        throws FileNotFoundException, JasperException, Exception {

        long t1 = 0;
        if (log.isDebugEnabled()) {
            t1 = System.currentTimeMillis();
        }
        
        String packageName = ctxt.getServletPackageName();
        ClassLoader classLoader = ctxt.getJspLoader();

        String targetResource = (((packageName.length() != 0) ? (packageName + ".") : "") 
                    + ctxt.getServletClassName()).replace('.', '/') + ".java";
        String[] resources = new String[] {targetResource};

        JavaCompiler javaCompiler = (new JavaCompilerFactory()).createCompiler(options.getCompiler().substring(4));
        FileResourceReader reader = new FileResourceReader(ctxt.getOptions().getScratchDir());
        FileResourceStore store = new FileResourceStore(ctxt.getOptions().getScratchDir());
        JavaCompilerSettings settings = javaCompiler.createDefaultSettings();
        if (settings == null) {
            settings = new JavaCompilerSettings();
        }
        settings.setDeprecations(false);
        if (ctxt.getOptions().getJavaEncoding() != null) {
            settings.setSourceEncoding(ctxt.getOptions().getJavaEncoding());
        }
        if (ctxt.getOptions().getClassDebugInfo()) {
            // No support
        }
        // Source JVM
        if (ctxt.getOptions().getCompilerSourceVM() != null) {
            settings.setSourceVersion(ctxt.getOptions().getCompilerSourceVM());
        } else {
            // Default to 1.5
            settings.setSourceVersion("1.5");
        }
        // Target JVM
        if (ctxt.getOptions().getCompilerTargetVM() != null) {
            settings.setTargetVersion(ctxt.getOptions().getCompilerTargetVM());
        } else {
            // Default to 1.5
            settings.setTargetVersion("1.5");
        }

        CompilationResult result = javaCompiler.compile(resources, reader, store, classLoader, settings);
        
        ArrayList<JavacErrorDetail> problemList = new ArrayList<JavacErrorDetail>();
        CompilationProblem[] problems = result.getErrors();
        if (problems != null) {
            try {
                for (int i = 0; i < problems.length; i++) {
                    CompilationProblem problem = problems[i];
                    problemList.add(ErrorDispatcher.createJavacError
                            (problem.getFileName(), pageNodes, new StringBuffer(problem.getMessage()), 
                                    problem.getStartLine(), ctxt));
                }
            } catch (JasperException e) {
                log.error("Error visiting node", e);
            }
        }
        
        if (!ctxt.keepGenerated()) {
            File javaFile = new File(ctxt.getServletJavaFileName());
            javaFile.delete();
        }
    
        if (!problemList.isEmpty()) {
            JavacErrorDetail[] jeds = 
                (JavacErrorDetail[]) problemList.toArray(new JavacErrorDetail[0]);
            errDispatcher.javacError(jeds);
        }
        
        if( log.isDebugEnabled() ) {
            long t2=System.currentTimeMillis();
            log.debug("Compiled " + ctxt.getServletJavaFileName() + " "
                      + (t2-t1) + "ms");
        }

        if (ctxt.isPrototypeMode()) {
            return;
        }

        // JSR45 Support
        if (! options.isSmapSuppressed()) {
            SmapUtil.installSmap(smap);
        }
        
    }
    
    
}
