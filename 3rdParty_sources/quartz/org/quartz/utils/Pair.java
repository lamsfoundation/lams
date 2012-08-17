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

/*
 * Previously Copyright (c) 2001-2004 James House
 */

package org.quartz.utils;

/**
 * <p>
 * Utility class for storing two pieces of information together.
 * </p>
 * 
 * @author <a href="mailto:jeff@binaryfeed.org">Jeffrey Wescott</a>
 */
public class Pair {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private Object first;

    private Object second;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * <p>
     * Get the first object in the pair.
     * </p>
     * 
     * @return the first object
     */
    public final Object getFirst() {
        return first;
    }

    /**
     * <p>
     * Set the value of the first object in the pair.
     * </p>
     * 
     * @param first
     *          the first object
     */
    public final void setFirst(Object first) {
        this.first = first;
    }

    /**
     * <p>
     * Get the second object in the pair.
     * </p>
     * 
     * @return the second object
     */
    public final Object getSecond() {
        return second;
    }

    /**
     * <p>
     * Set the second object in the pair.
     * </p>
     * 
     * @param second
     *          the second object
     */
    public final void setSecond(Object second) {
        this.second = second;
    }

    /**
     * <p>
     * Test equality of this object with that.
     * </p>
     * 
     * @param that
     *          object to compare
     * @return true if objects are equal, false otherwise
     */
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        } else {
            try {
                Pair other = (Pair) that;
                return (this.first.equals(other.first) && this.second
                        .equals(other.second));
            } catch (ClassCastException e) {
                return false;
            }
        }
    }
}

// EOF
