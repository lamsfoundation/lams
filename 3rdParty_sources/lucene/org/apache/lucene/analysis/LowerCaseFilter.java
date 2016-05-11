package org.apache.lucene.analysis;

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

import java.io.IOException;

/**
 * Normalizes token text to lower case.
 *
 *
 */
public final class LowerCaseFilter extends TokenFilter {
  public LowerCaseFilter(TokenStream in) {
    super(in);
  }

  public final Token next(final Token reusableToken) throws IOException {
    assert reusableToken != null;
    Token nextToken = input.next(reusableToken);
    if (nextToken != null) {

      final char[] buffer = nextToken.termBuffer();
      final int length = nextToken.termLength();
      for(int i=0;i<length;i++)
        buffer[i] = Character.toLowerCase(buffer[i]);

      return nextToken;
    } else
      return null;
  }
}
