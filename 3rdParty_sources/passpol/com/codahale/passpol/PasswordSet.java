/*
 * Copyright © 2018 Coda Hale (coda.hale@gmail.com)
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
package com.codahale.passpol;

import java.text.Normalizer;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class PasswordSet implements BreachDatabase {

  private final Set<String> passwords;

  PasswordSet(Stream<String> passwords) {
    this.passwords = passwords.map(PasswordSet::normalize).collect(Collectors.toUnmodifiableSet());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PasswordSet)) {
      return false;
    }
    var that = (PasswordSet) o;
    return Objects.equals(passwords, that.passwords);
  }

  @Override
  public int hashCode() {
    return Objects.hash(passwords);
  }

  @Override
  public String toString() {
    return passwords.toString();
  }

  @Override
  public boolean contains(String password) {
    return passwords.contains(normalize(password));
  }

  static String normalize(String s) {
    return Normalizer.normalize(s, Normalizer.Form.NFKC);
  }
}
