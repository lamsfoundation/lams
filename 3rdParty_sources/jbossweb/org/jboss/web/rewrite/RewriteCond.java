/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */


package org.jboss.web.rewrite;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RewriteCond {

    public abstract class Condition {
        public abstract boolean evaluate(String value, Resolver resolver);
    }
    
    public class PatternCondition extends Condition {
        public Pattern pattern;
        public Matcher matcher = null;
        public boolean evaluate(String value, Resolver resolver) {
            Matcher m = pattern.matcher(value);
            if (m.matches()) {
                matcher = m;
                return true;
            } else {
                return false;
            }
        }
    }
    
    public class LexicalCondition extends Condition {
        /**
         * -1: <
         * 0: =
         * 1: >
         */
        public int type = 0;
        public String condition;
        public boolean evaluate(String value, Resolver resolver) {
            int result = value.compareTo(condition);
            switch (type) {
            case -1:
                return (result < 0);
            case 0:
                return (result == 0);
            case 1:
                return (result > 0);
            default:
                return false;
            }
                
        }
    }
    
    public class ResourceCondition extends Condition {
        /**
         * 0: -d (is directory ?)
         * 1: -f (is regular file ?)
         * 2: -s (is regular file with size ?)
         */
        public int type = 0;
        public boolean evaluate(String value, Resolver resolver) {
            switch (type) {
            case 0:
                return true;
            case 1:
                return true;
            case 2:
                return true;
            default:
                return false;
            }
                
        }
    }
    
    protected String testString = null;
    protected String condPattern = null;
    
    public String getCondPattern() {
        return condPattern;
    }

    public void setCondPattern(String condPattern) {
        this.condPattern = condPattern;
    }

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }

    public void parse(Map<String, RewriteMap> maps) {
        test = new Substitution();
        test.setSub(testString);
        test.parse(maps);
        if (condPattern.startsWith("!")) {
            positive = false;
            condPattern = condPattern.substring(1);
        }
        if (condPattern.startsWith("<")) {
            LexicalCondition condition = new LexicalCondition();
            condition.type = -1;
            condition.condition = condPattern.substring(1);
        } else if (condPattern.startsWith(">")) {
            LexicalCondition condition = new LexicalCondition();
            condition.type = 1;
            condition.condition = condPattern.substring(1);
        } else if (condPattern.startsWith("=")) {
            LexicalCondition condition = new LexicalCondition();
            condition.type = 0;
            condition.condition = condPattern.substring(1);
        } else if (condPattern.equals("-d")) {
            ResourceCondition ncondition = new ResourceCondition();
            ncondition.type = 0;
        } else if (condPattern.equals("-f")) {
            ResourceCondition ncondition = new ResourceCondition();
            ncondition.type = 1;
        } else if (condPattern.equals("-s")) {
            ResourceCondition ncondition = new ResourceCondition();
            ncondition.type = 2;
        } else {
            PatternCondition condition = new PatternCondition();
            int flags = 0;
            if (isNocase()) {
                flags |= Pattern.CASE_INSENSITIVE;
            }
            condition.pattern = Pattern.compile(condPattern, flags);
        }
    }
    
    public Matcher getMatcher() {
        Object condition = this.condition.get();
        if (condition instanceof PatternCondition) {
            return ((PatternCondition) condition).matcher; 
        }
        return null;
    }
    
    /**
     * String representation.
     */
    public String toString() {
        // FIXME: Add flags if possible
        return "RewriteCond " + testString + " " + condPattern;
    }
    
    
    protected boolean positive = true;
    
    protected Substitution test = null;

    protected ThreadLocal<Condition> condition = new ThreadLocal<Condition>();
    
    /**
     * This makes the test case-insensitive, i.e., there is no difference between 
     * 'A-Z' and 'a-z' both in the expanded TestString and the CondPattern. This 
     * flag is effective only for comparisons between TestString and CondPattern. 
     * It has no effect on filesystem and subrequest checks.
     */
    public boolean nocase = false;
    
    /**
     * Use this to combine rule conditions with a local OR instead of the implicit AND.
     */
    public boolean ornext = false;

    /**
     * Evaluate the condition based on the context
     * 
     * @param rule corresponding matched rule
     * @param cond last matched condition
     * @return
     */
    public boolean evaluate(Matcher rule, Matcher cond, Resolver resolver) {
        String value = test.evaluate(rule, cond, resolver);
        if (nocase) {
            value = value.toLowerCase();
        }
        Condition condition = this.condition.get();
        if (condition == null) {
            if (condPattern.startsWith("<")) {
                LexicalCondition ncondition = new LexicalCondition();
                ncondition.type = -1;
                ncondition.condition = condPattern.substring(1);
                condition = ncondition;
            } else if (condPattern.startsWith(">")) {
                LexicalCondition ncondition = new LexicalCondition();
                ncondition.type = 1;
                ncondition.condition = condPattern.substring(1);
                condition = ncondition;
            } else if (condPattern.startsWith("=")) {
                LexicalCondition ncondition = new LexicalCondition();
                ncondition.type = 0;
                ncondition.condition = condPattern.substring(1);
                condition = ncondition;
            } else if (condPattern.equals("-d")) {
                ResourceCondition ncondition = new ResourceCondition();
                ncondition.type = 0;
                condition = ncondition;
            } else if (condPattern.equals("-f")) {
                ResourceCondition ncondition = new ResourceCondition();
                ncondition.type = 1;
                condition = ncondition;
            } else if (condPattern.equals("-s")) {
                ResourceCondition ncondition = new ResourceCondition();
                ncondition.type = 2;
                condition = ncondition;
            } else {
                PatternCondition ncondition = new PatternCondition();
                int flags = 0;
                if (isNocase()) {
                    flags |= Pattern.CASE_INSENSITIVE;
                }
                ncondition.pattern = Pattern.compile(condPattern, flags);
                condition = ncondition;
            }
            this.condition.set(condition);
        }
        if (positive) {
            return condition.evaluate(value, resolver);
        } else {
            return !condition.evaluate(value, resolver);
        }
    }
        
    public boolean isNocase() {
        return nocase;
    }

    public void setNocase(boolean nocase) {
        this.nocase = nocase;
    }

    public boolean isOrnext() {
        return ornext;
    }

    public void setOrnext(boolean ornext) {
        this.ornext = ornext;
    }

    public boolean isPositive() {
        return positive;
    }

    public void setPositive(boolean positive) {
        this.positive = positive;
    }

}
