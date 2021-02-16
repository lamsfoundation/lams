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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;

/** A database of passwords found in data breaches. */
public interface BreachDatabase {

  /**
   * Returns whether or not the database contains the given password.
   *
   * @param password a candidate password
   * @return {@code true} if the database contains {@code password}
   * @throws IOException if there was a problem communicating with the database
   */
  boolean contains(String password) throws IOException;

  /**
   * A client for <a href="https://haveibeenpwned.com/">Have I Been Pwned</a>'s online password
   * checking. Uses a k-anonymous API which transmits only 20 bits of a password hash.
   *
   * @return an online database of breached passwords
   */
  static BreachDatabase haveIBeenPwned() {
    return haveIBeenPwned(1);
  }

  /**
   * A client for <a href="https://haveibeenpwned.com/">Have I Been Pwned</a>'s online password
   * checking. Uses a k-anonymous API which transmits only 20 bits of a password hash.
   *
   * @param threshold The number of breaches a password can be found in which makes it invalid.
   * @return an online database of breached passwords
   */
  static BreachDatabase haveIBeenPwned(int threshold) {
    return haveIBeenPwned(HttpClient.newHttpClient(), threshold);
  }

  /**
   * A client for <a href="https://haveibeenpwned.com/">Have I Been Pwned</a>'s online password
   * checking. Uses a k-anonymous API which transmits only 20 bits of a password hash.
   *
   * @param client The HTTP client to use
   * @param threshold The number of breaches a password can be found in which makes it invalid.
   * @return an online database of breached passwords
   */
  static BreachDatabase haveIBeenPwned(HttpClient client, int threshold) {
    return new HaveIBeenPwned(Objects.requireNonNull(client), threshold);
  }

  /**
   * Returns an offline database of the given passwords.
   *
   * @param passwords a collection of unusable passwords
   * @return an offline database of the given passwords
   */
  static BreachDatabase passwordSet(Collection<String> passwords) {
    return new PasswordSet(Objects.requireNonNull(passwords).stream());
  }

  /**
   * Returns an offline database of the 100,000 most common passwords.
   *
   * @return an offline database of the 100,000 most common passwords
   */
  static BreachDatabase top100K() {
    try (var in = BreachDatabase.class.getResourceAsStream("weak-passwords.txt");
        var r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
      return new PasswordSet(r.lines());
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * Returns a database which checks the given databases in order.
   *
   * @param databases a set of databases
   * @return a database which checks the given databases in order
   */
  static BreachDatabase anyOf(BreachDatabase... databases) {
    for (var database : databases) {
      Objects.requireNonNull(database);
    }

    return password -> {
      for (var database : databases) {
        if (database.contains(password)) {
          return true;
        }
      }
      return false;
    };
  }
}
