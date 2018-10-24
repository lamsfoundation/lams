/*
 * Licensed to the University Corporation for Advanced Internet Development, 
 * Inc. (UCAID) under one or more contributor license agreements.  See the 
 * NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The UCAID licenses this file to You under the Apache 
 * License, Version 2.0 (the "License"); you may not use this file except in 
 * compliance with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml.xml.util;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.WeakHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of {@link SingletonFactory}, which provides some support for handling
 * cases where the output class instance holds a reference to the input class instance.
 * 
 * <p>
 * A {@link WeakHashMap} is used as the underlying store. This ensures that if the input class
 * instance become otherwise unused (weakly reachable), the input class instance key used
 * within the factory will not prevent the input class from being garbage-collected,
 * thereby preventing a memory leak.
 * </p>
 * 
 * <p>
 * This class differs from {@link AbstractSimpleSingletonFactory} in that output value instances 
 * stored and returned by the factory are also wrapped internally in a {@link WeakReference}.
 * This class should be used in cases where the output class holds a reference to the input 
 * class key, so as not to prevent the described weak reference-based garbage collection 
 * of the input class key, and thereby avoiding a memory leak.
 * </p>
 * 
 * <p>
 * Because the output instance is held in a WeakReference, it is subject to aggressive
 * garbage collection if it is otherwise weakly reachable (i.e. no strong or soft references 
 * to it are held outside of this factory), ostensibly defeating the purpose of this factory.
 * Therefore if the lifecycle of external strong or soft references to any obtained output 
 * instances obtained from the factory is shorter than the desired lifecyle of the output instance 
 * (i.e. callers do not hold a strong or soft reference to an output instance for at least as 
 * long as to the input instance), then an option <code>requireExplicitRelease</code> is provided 
 * that causes the factory to internally maintain a strong reference to each output instance.
 * This inhibits the garbage collection of the output instance. If this option is enabled,
 * then callers must explicity indicate when the output instance may be garbage collected by 
 * calling {@link #release(Object)}.  Failure to release an output instance when necessary
 * will result in a memory leak of the output instance as well as the input instance (if
 * the output instance holds a strong or soft reference to the input instance).
 * </p>
 * 
 * <p>
 * The default value of <code>requireExplicitRelease</code> is <code>false</code>.  This is appropriate
 * for cases where calling code holds long-lived strong or soft references to the output instance,
 * typically as long or longer than references to the corresponding input instance, or where explict release 
 * is undesirable or impractical.
 * </p>
 * 
 * <p>
 * Subclasses of this class might also implement automatic release of output instances,
 * instead of or in addition to, the explicit release mechanism supported by this class.
 * This might be based for example on mechanisms such as object aging or a fixed size FIFO queue.
 * </p>
 *
 * @param <Input> the factory input class type
 * @param <Output> the factory output class type
 */
public abstract class AbstractWrappedSingletonFactory<Input, Output> 
        extends AbstractSingletonFactory<Input, Output> {
    
    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(AbstractWrappedSingletonFactory.class);
    
    /** Storage for the factory. */
    private WeakHashMap<Input, WeakReference<Output>> map;
    
    /** Set which holds a separate strong reference to output class instances,
     * to inhibit garbage collection of the referent of the WeakReference. */
    private HashSet<Output> outputSet;
    
    /** Flag indicating whether explicit release of the output instances is required. */
    private boolean explicitRelease;
    
    /** Constructor. */
    public AbstractWrappedSingletonFactory() {
        this(false);
    }
    
    /**
     * Constructor.
     *
     * @param requireExplicitRelease if true, callers must explicitly release
     *              output instances when garbage collection is desired.
     */
    public AbstractWrappedSingletonFactory(boolean requireExplicitRelease) {
        map = new WeakHashMap<Input, WeakReference<Output>>();
        explicitRelease = requireExplicitRelease;
        outputSet = new HashSet<Output>();
    }
    
    /**
     * Obtain an instance of the output class based on an input class instance.
     * 
     * @param input the input class instance
     * @return an output class instance
     */
    public synchronized Output getInstance(Input input) {
        Output output = super.getInstance(input);
        
        if (explicitRelease && output != null) {
            log.trace("Explicit release was indicated, registering output instance to inhibit garbage collection");
            register(output);
        }
        
        return output;
    }
    
    /**
     * Get whether explict release of output instances is required,
     * in order to allow garbage collection and prevent memory leaks.
     * 
     * @return true if enabled, false otherwise
     */
    public boolean isRequireExplicitRelease() {
        return explicitRelease;
    }
    
    /**
     * Release the specified output instance so that, as the referent
     * of a WeakReference, it may be garbage collected when it otherwise
     * becomse weakly reachable.
     * 
     * @param output the output instance to release
     */
    public synchronized void release(Output output) {
        outputSet.remove(output);
    }
    
    /**
     * Release all currently held output instances so they
     * may be garbage collected when they become otherwise
     * weakly reachable.
     */
    public synchronized void releaseAll() {
        outputSet.clear();
    }
    
    /**
     * Register the output instance so as to inhibit garbage collection.
     * 
     * @param output the ouput instance to register
     */
    protected synchronized void register(Output output) {
        outputSet.add(output);
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p>
     * The output instance will be automatically unwrapped from within the WeakReference.
     * </p>
     * 
     * <p>
     * Note this will return null if either the input does not
     * currently have an associated output, or if the WeakReference
     * to the output stored had already been clearly in preparation
     * for garbage collection.
     * </p>
     */
    protected synchronized Output get(Input input) {
        WeakReference<Output> outputRef = map.get(input);
        if (outputRef != null) {
            log.trace("Input key mapped to a non-null WeakReference");
            if (outputRef.get() != null) {
                log.trace("WeakReference referent was non-null, returning referent");
                return outputRef.get();
            } else {
                log.trace("WeakReference referent was null, removing WeakReference entry from map");
                map.remove(input);
            }
        }
        return null;
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p>
     * The output instance will be automatically wrapped in a WeakReference.
     * </p>
     */
    protected synchronized void put(Input input, Output output) {
        map.put(input, new WeakReference<Output>(output));
    }

}
