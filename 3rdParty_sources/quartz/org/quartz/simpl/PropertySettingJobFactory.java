/* 
 * Copyright 2004-2005 OpenSymphony 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */
package org.quartz.simpl;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.quartz.spi.TriggerFiredBundle;



/**
 * A JobFactory that instantiates the Job instance (using the default no-arg
 * constructor, or more specifically: <code>class.newInstance()</code>), and
 * then attempts to set all values in the <code>JobExecutionContext</code>'s
 * <code>JobDataMap</code> onto bean properties of the <code>Job</code>.
 * 
 * @see org.quartz.spi.JobFactory
 * @see SimpleJobFactory
 * @see JobExecutionContext#getMergedJobDataMap()
 * @see #setWarnIfPropertyNotFound(boolean)
 * @see #setThrowIfPropertyNotFound(boolean)
 * 
 * @author jhouse
 */
public class PropertySettingJobFactory extends SimpleJobFactory {

    private Log log = LogFactory.getLog(SimpleJobFactory.class);
    
    private boolean warnIfNotFound = true;
    private boolean throwIfNotFound = false;
    
    public Job newJob(TriggerFiredBundle bundle) throws SchedulerException {

        Job job = super.newJob(bundle);
        
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAll(bundle.getJobDetail().getJobDataMap());
        jobDataMap.putAll(bundle.getTrigger().getJobDataMap());

        setBeanProps(job, jobDataMap);
        
        return job;
    }
    
    protected void setBeanProps(Object obj, JobDataMap data) throws SchedulerException {

        BeanInfo bi = null;
        try {
            bi = Introspector.getBeanInfo(obj.getClass());
        } catch (IntrospectionException e1) {
            if(isThrowIfPropertyNotFound()) {
                throw new SchedulerException("Unable to introspect Job class.", e1);
            }
            if(isWarnIfPropertyNotFound()) {
                log.warn("Unable to introspect Job class.", e1);
            }
        }
        
        PropertyDescriptor[] propDescs = bi.getPropertyDescriptors();
        
        java.util.Iterator keys = data.keySet().iterator();
        while (keys.hasNext()) {
            String name = (String) keys.next();
            String c = name.substring(0, 1).toUpperCase(Locale.US);
            String methName = "set" + c + name.substring(1);
        
            java.lang.reflect.Method setMeth = getSetMethod(methName, propDescs);
        
            Class paramType = null;
            Object o = null;
            
            try {
                if (setMeth == null) {
                    if(isThrowIfPropertyNotFound()) {
                        throw new SchedulerException(
                                "No setter on Job class " + obj.getClass() + 
                                " for property '" + name + "'");
                    }
                    if(isWarnIfPropertyNotFound()) {
                        log.warn(
                                "No setter on Job class " + obj.getClass() + 
                                " for property '" + name + "'");
                    }
                    continue;
                }
                
                paramType = setMeth.getParameterTypes()[0];
                o = data.get(name);
                
                if (paramType.equals(int.class)) {
                    if(o instanceof Integer)
                        setMeth.invoke(obj, 
                                new Object[]{o});
                    else if(o instanceof String)
                        setMeth.invoke(obj, 
                                new Object[]{data.getIntegerFromString(name)});
                } else if (paramType.equals(long.class)) {
                    if(o instanceof Long)
                        setMeth.invoke(obj, 
                                new Object[]{o});
                    else if(o instanceof String)
                        setMeth.invoke(obj, 
                                new Object[]{data.getLongFromString(name)});
                } else if (paramType.equals(float.class)) {
                    if(o instanceof Float)
                        setMeth.invoke(obj, 
                                new Object[]{o});
                    else if(o instanceof String)
                        setMeth.invoke(obj, 
                                new Object[]{data.getFloatFromString(name)});
                } else if (paramType.equals(double.class)) {
                    if(o instanceof Double)
                        setMeth.invoke(obj, 
                                new Object[]{o});
                    else if(o instanceof String)
                        setMeth.invoke(obj, 
                                new Object[]{data.getDoubleFromString(name)});
                } else if (paramType.equals(boolean.class)) {
                    if(o instanceof Boolean)
                        setMeth.invoke(obj, 
                                new Object[]{o});
                    else if(o instanceof String)
                        setMeth.invoke(obj, 
                                new Object[]{data.getBooleanFromString(name)});
                } else if (paramType.equals(String.class)) {
                    if(o instanceof String)
                        setMeth.invoke(obj, 
                                new Object[]{o});
                } else {
                    if(paramType.isAssignableFrom(o.getClass())) {
                        setMeth.invoke(obj, 
                                new Object[]{o});
                    }
                    else
                        throw new NoSuchMethodException();
                }
            } catch (NumberFormatException nfe) {
                if(isThrowIfPropertyNotFound()) {
                    throw new SchedulerException(
                            "The setter on Job class " + obj.getClass() + 
                            " for property '" + name + 
                            "' expects a " + paramType + 
                            " but was given " + o, nfe);
                }
                if(isWarnIfPropertyNotFound()) {
                    log.warn(
                            "The setter on Job class " + obj.getClass() + 
                            " for property '" + name + 
                            "' expects a " + paramType + 
                            " but was given " + o, nfe);
                }
                continue;
            } catch (NoSuchMethodException e) {
                if(isThrowIfPropertyNotFound()) {
                    throw new SchedulerException(
                            "The setter on Job class " + obj.getClass() + 
                            " for property '" + name + 
                            "' expects a " + paramType + 
                            " but was given " + o.getClass());
                }
                if(isWarnIfPropertyNotFound()) {
                    log.warn(
                            "The setter on Job class " + obj.getClass() + 
                            " for property '" + name + 
                            "' expects a " + paramType + 
                            " but was given a " + o.getClass());
                }
                continue;
            } catch (IllegalArgumentException e) {
                if(isThrowIfPropertyNotFound()) {
                    throw new SchedulerException(
                            "The setter on Job class " + obj.getClass() + 
                            " for property '" + name + 
                            "' expects a " + paramType + 
                            " but was given " + o.getClass(),e );
                }
                if(isWarnIfPropertyNotFound()) {
                    log.warn(
                            "The setter on Job class " + obj.getClass() + 
                            " for property '" + name + 
                            "' expects a " + paramType + 
                            " but was given a " + o.getClass(), e);
                }
                continue;
            } catch (IllegalAccessException e) {
                if(isThrowIfPropertyNotFound()) {
                    throw new SchedulerException(
                            "The setter on Job class " + obj.getClass() + 
                            " for property '" + name + 
                            "' could not be accessed.", e);
                }
                if(isWarnIfPropertyNotFound()) {
                    log.warn(
                            "The setter on Job class " + obj.getClass() + 
                            " for property '" + name + 
                            "' expects a " + paramType + 
                            "' could not be accessed.", e);
                }
                continue;
            } catch (InvocationTargetException e) {
                if(isThrowIfPropertyNotFound()) {
                    throw new SchedulerException(
                            "The setter on Job class " + obj.getClass() + 
                            " for property '" + name + 
                            "' could not be accessed.", e);
                }
                if(isWarnIfPropertyNotFound()) {
                    log.warn(
                            "The setter on Job class " + obj.getClass() + 
                            " for property '" + name + 
                            "' expects a " + paramType + 
                            "' could not be accessed.", e);
                }
                continue;
            }
        }
    }
        
    private java.lang.reflect.Method getSetMethod(String name,
            PropertyDescriptor[] props) {
        for (int i = 0; i < props.length; i++) {
            java.lang.reflect.Method wMeth = props[i].getWriteMethod();
        
            if(wMeth == null)
                continue;
            
            if(wMeth.getParameterTypes().length != 1)
                continue;
            
            if (wMeth.getName().equals(name)) return wMeth;
        }
        
        return null;
    }

    /**
     * Whether the JobInstantiation should fail and throw and exception if
     * a key (name) and value (type) found in the JobDataMap does not 
     * correspond to a proptery setter on the Job class.
     *  
     * @return Returns the throwIfNotFound.
     */
    public boolean isThrowIfPropertyNotFound() {
        return throwIfNotFound;
    }

    /**
     * Whether the JobInstantiation should fail and throw and exception if
     * a key (name) and value (type) found in the JobDataMap does not 
     * correspond to a proptery setter on the Job class.
     *  
     * @param throwIfNotFound defaults to <code>false</code>.
     */
    public void setThrowIfPropertyNotFound(boolean throwIfNotFound) {
        this.throwIfNotFound = throwIfNotFound;
    }

    /**
     * Whether a warning should be logged if
     * a key (name) and value (type) found in the JobDataMap does not 
     * correspond to a proptery setter on the Job class.
     *  
     * @return Returns the warnIfNotFound.
     */
    public boolean isWarnIfPropertyNotFound() {
        return warnIfNotFound;
    }

    /**
     * Whether a warning should be logged if
     * a key (name) and value (type) found in the JobDataMap does not 
     * correspond to a proptery setter on the Job class.
     *  
     * @param warnIfNotFound defaults to <code>true</code>.
     */
    public void setWarnIfPropertyNotFound(boolean warnIfNotFound) {
        this.warnIfNotFound = warnIfNotFound;
    }
        
    
}