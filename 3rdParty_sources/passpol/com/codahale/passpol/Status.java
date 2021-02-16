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

/** The status of a given candidate password. */
public enum Status {
  /** The candidate password is acceptable. */
  OK,

  /** The candidate password is too short. */
  TOO_SHORT,

  /** The candidate password is too long. */
  TOO_LONG,

  /** The candidate password has previously appeared in a data breach. */
  BREACHED
}
