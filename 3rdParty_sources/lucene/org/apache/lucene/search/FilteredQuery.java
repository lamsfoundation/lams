package org.apache.lucene.search;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.util.ToStringUtils;

import java.io.IOException;
import java.util.Set;


/**
 * A query that applies a filter to the results of another query.
 *
 * <p>Note: the bits are retrieved from the filter each time this
 * query is used in a search - use a CachingWrapperFilter to avoid
 * regenerating the bits every time.
 *
 * <p>Created: Apr 20, 2004 8:58:29 AM
 *
 * @since   1.4
 *
 * @see     CachingWrapperFilter
 */
public class FilteredQuery
extends Query {

  Query query;
  Filter filter;

  /**
   * Constructs a new query which applies a filter to the results of the original query.
   * Filter.getDocIdSet() will be called every time this query is used in a search.
   * @param query  Query to be filtered, cannot be <code>null</code>.
   * @param filter Filter to apply to query results, cannot be <code>null</code>.
   */
  public FilteredQuery (Query query, Filter filter) {
    this.query = query;
    this.filter = filter;
  }



  /**
   * Returns a Weight that applies the filter to the enclosed query's Weight.
   * This is accomplished by overriding the Scorer returned by the Weight.
   */
  protected Weight createWeight (final Searcher searcher) throws IOException {
    final Weight weight = query.createWeight (searcher);
    final Similarity similarity = query.getSimilarity(searcher);
    return new Weight() {
      private float value;
        
      // pass these methods through to enclosed query's weight
      public float getValue() { return value; }
      public float sumOfSquaredWeights() throws IOException { 
        return weight.sumOfSquaredWeights() * getBoost() * getBoost(); 
      }
      public void normalize (float v) { 
        weight.normalize(v);
        value = weight.getValue() * getBoost();
      }
      public Explanation explain (IndexReader ir, int i) throws IOException {
        Explanation inner = weight.explain (ir, i);
        if (getBoost()!=1) {
          Explanation preBoost = inner;
          inner = new Explanation(inner.getValue()*getBoost(),"product of:");
          inner.addDetail(new Explanation(getBoost(),"boost"));
          inner.addDetail(preBoost);
        }
        Filter f = FilteredQuery.this.filter;
        DocIdSetIterator docIdSetIterator = f.getDocIdSet(ir).iterator();
        if (docIdSetIterator.skipTo(i) && (docIdSetIterator.doc() == i)) {
          return inner;
        } else {
          Explanation result = new Explanation
            (0.0f, "failure to match filter: " + f.toString());
          result.addDetail(inner);
          return result;
        }
      }

      // return this query
      public Query getQuery() { return FilteredQuery.this; }

      // return a filtering scorer
       public Scorer scorer (IndexReader indexReader) throws IOException {
        final Scorer scorer = weight.scorer(indexReader);
        final DocIdSetIterator docIdSetIterator = filter.getDocIdSet(indexReader).iterator();

        return new Scorer(similarity) {

          private boolean advanceToCommon() throws IOException {
            while (scorer.doc() != docIdSetIterator.doc()) {
              if (scorer.doc() < docIdSetIterator.doc()) {
                if (!scorer.skipTo(docIdSetIterator.doc())) {
                  return false;
                }
              } else if (!docIdSetIterator.skipTo(scorer.doc())) {
                return false;
              }
            }
            return true;
          }

          public boolean next() throws IOException {
            return docIdSetIterator.next() && scorer.next() && advanceToCommon();
          }

          public int doc() { return scorer.doc(); }

          public boolean skipTo(int i) throws IOException {
            return docIdSetIterator.skipTo(i)
                && scorer.skipTo(docIdSetIterator.doc())
                && advanceToCommon();
          }

          public float score() throws IOException { return getBoost() * scorer.score(); }

          // add an explanation about whether the document was filtered
          public Explanation explain (int i) throws IOException {
            Explanation exp = scorer.explain(i);
            
            if (docIdSetIterator.skipTo(i) && (docIdSetIterator.doc() == i)) {
              exp.setDescription ("allowed by filter: "+exp.getDescription());
              exp.setValue(getBoost() * exp.getValue());
            } else {
              exp.setDescription ("removed by filter: "+exp.getDescription());
              exp.setValue(0.0f);
            }
            return exp;
          }
        };
      }
    };
  }

  /** Rewrites the wrapped query. */
  public Query rewrite(IndexReader reader) throws IOException {
    Query rewritten = query.rewrite(reader);
    if (rewritten != query) {
      FilteredQuery clone = (FilteredQuery)this.clone();
      clone.query = rewritten;
      return clone;
    } else {
      return this;
    }
  }

  public Query getQuery() {
    return query;
  }

  public Filter getFilter() {
    return filter;
  }

  // inherit javadoc
  public void extractTerms(Set terms) {
      getQuery().extractTerms(terms);
  }

  /** Prints a user-readable version of this query. */
  public String toString (String s) {
    StringBuffer buffer = new StringBuffer();
    buffer.append("filtered(");
    buffer.append(query.toString(s));
    buffer.append(")->");
    buffer.append(filter);
    buffer.append(ToStringUtils.boost(getBoost()));
    return buffer.toString();
  }

  /** Returns true iff <code>o</code> is equal to this. */
  public boolean equals(Object o) {
    if (o instanceof FilteredQuery) {
      FilteredQuery fq = (FilteredQuery) o;
      return (query.equals(fq.query) && filter.equals(fq.filter) && getBoost()==fq.getBoost());
    }
    return false;
  }

  /** Returns a hash code value for this object. */
  public int hashCode() {
    return query.hashCode() ^ filter.hashCode() + Float.floatToRawIntBits(getBoost());
  }
}
