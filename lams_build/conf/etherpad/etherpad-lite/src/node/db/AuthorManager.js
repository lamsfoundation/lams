'use strict';
/**
 * The AuthorManager controlls all information about the Pad authors
 */

/*
 * 2011 Peter 'Pita' Martischka (Primary Technology Ltd)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS-IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

const db = require('./DB');
const CustomError = require('../utils/customError');
const randomString = require('../../static/js/pad_utils').randomString;

//*LAMS* modified the following array: replaced dark colors with the more lighter ones  
exports.getColorPalette = () => ["#ffc7c7", "#fff1c7", "#e3ffc7", "#c7ffd5", "#c7ffff", "#c7d5ff", "#e3c7ff", "#ffc7f1", "#ff8f8f", "#ffe38f", "#c7ff8f", "#8fffab", "#8fffff", "#8fabff", "#c78fff", "#ff8fe3", "#ecbcbc", "#d9c179", "#a9d979", "#79d991", "#79d9d9", "#bcc8ec", "#dcc9ef", "#efc9e6", "#d9a9a9", "#d9cda9", "#c1d9a9", "#a9d9b5", "#a9d9d9", "#a9b5d9", "#c1a9d9", "#d9a9cd", "#c9e1d9", "#12d1ad", "#d5e8e5", "#d5daed", "#a091c7", "#c1dae5", "#e0d0f0", "#e6e76d", "#e3bfd0", "#f386e5", "#4ecc0c", "#c0c236", "#d2c1bd", "#b5de6a", "#9b88fd", "#c2dde1", "#c8d3c0", "#e267fe", "#f1c0cc", "#babad0", "#cde3c2", "#f7d2f1", "#86dc6c", "#b5a714", "#dfcdd2", "#ebd4e6", "#4b81c8", "#c4d2cd", "#c6c9b9", "#d16084", "#efe1ce", "#8c8bd8"];

/**
 * Checks if the author exists
 */
exports.doesAuthorExist = async (authorID) => {
  const author = await db.get(`globalAuthor:${authorID}`);

  return author != null;
};

/* exported for backwards compatibility */
exports.doesAuthorExists = exports.doesAuthorExist;

/**
 * Returns the AuthorID for a token.
 * @param {String} token The token
 */
exports.getAuthor4Token = async (token) => {
  const author = await mapAuthorWithDBKey('token2author', token);

  // return only the sub value authorID
  return author ? author.authorID : author;
};

/**
 * Returns the AuthorID for a mapper.
 * @param {String} token The mapper
 * @param {String} name The name of the author (optional)
 */
exports.createAuthorIfNotExistsFor = async (authorMapper, name) => {
  const author = await mapAuthorWithDBKey('mapper2author', authorMapper);

  if (name) {
    // set the name of this author
    await exports.setAuthorName(author.authorID, name);
  }

  return author;
};

/**
 * Returns the AuthorID for a mapper. We can map using a mapperkey,
 * so far this is token2author and mapper2author
 * @param {String} mapperkey The database key name for this mapper
 * @param {String} mapper The mapper
 */
const mapAuthorWithDBKey = async (mapperkey, mapper) => {
  // try to map to an author
  const author = await db.get(`${mapperkey}:${mapper}`);

  if (author == null) {
    // there is no author with this mapper, so create one
    const author = await exports.createAuthor(null);

    // create the token2author relation
    await db.set(`${mapperkey}:${mapper}`, author.authorID);

    // return the author
    return author;
  }

  // there is an author with this mapper
  // update the timestamp of this author
  await db.setSub(`globalAuthor:${author}`, ['timestamp'], Date.now());

  // return the author
  return {authorID: author};
};

/**
 * Internal function that creates the database entry for an author
 * @param {String} name The name of the author
 */
exports.createAuthor = async (name) => {
  // create the new author name
  const author = `a.${randomString(16)}`;

  // create the globalAuthors db entry
  const authorObj = {
    colorId: Math.floor(Math.random() * (exports.getColorPalette().length)),
    name,
    timestamp: Date.now(),
  };

  // set the global author db entry
  await db.set(`globalAuthor:${author}`, authorObj);

  return {authorID: author};
};

/**
 * Returns the Author Obj of the author
 * @param {String} author The id of the author
 */
exports.getAuthor = async (author) => await db.get(`globalAuthor:${author}`);

/**
 * Returns the color Id of the author
 * @param {String} author The id of the author
 */
exports.getAuthorColorId = async (author) => await db.getSub(`globalAuthor:${author}`, ['colorId']);

/**
 * Sets the color Id of the author
 * @param {String} author The id of the author
 * @param {String} colorId The color id of the author
 */
exports.setAuthorColorId = async (author, colorId) => await db.setSub(
    `globalAuthor:${author}`, ['colorId'], colorId);

/**
 * Returns the name of the author
 * @param {String} author The id of the author
 */
exports.getAuthorName = async (author) => await db.getSub(`globalAuthor:${author}`, ['name']);

/**
 * Sets the name of the author
 * @param {String} author The id of the author
 * @param {String} name The name of the author
 */
exports.setAuthorName = async (author, name) => await db.setSub(
    `globalAuthor:${author}`, ['name'], name);

/**
 * Returns an array of all pads this author contributed to
 * @param {String} author The id of the author
 */
exports.listPadsOfAuthor = async (authorID) => {
  /* There are two other places where this array is manipulated:
   * (1) When the author is added to a pad, the author object is also updated
   * (2) When a pad is deleted, each author of that pad is also updated
   */

  // get the globalAuthor
  const author = await db.get(`globalAuthor:${authorID}`);

  if (author == null) {
    // author does not exist
    throw new CustomError('authorID does not exist', 'apierror');
  }

  // everything is fine, return the pad IDs
  const padIDs = Object.keys(author.padIDs || {});

  return {padIDs};
};

/**
 * Adds a new pad to the list of contributions
 * @param {String} author The id of the author
 * @param {String} padID The id of the pad the author contributes to
 */
exports.addPad = async (authorID, padID) => {
  // get the entry
  const author = await db.get(`globalAuthor:${authorID}`);

  if (author == null) return;

  /*
   * ACHTUNG: padIDs can also be undefined, not just null, so it is not possible
   * to perform a strict check here
   */
  if (!author.padIDs) {
    // the entry doesn't exist so far, let's create it
    author.padIDs = {};
  }

  // add the entry for this pad
  author.padIDs[padID] = 1; // anything, because value is not used

  // save the new element back
  await db.set(`globalAuthor:${authorID}`, author);
};

/**
 * Removes a pad from the list of contributions
 * @param {String} author The id of the author
 * @param {String} padID The id of the pad the author contributes to
 */
exports.removePad = async (authorID, padID) => {
  const author = await db.get(`globalAuthor:${authorID}`);

  if (author == null) return;

  if (author.padIDs != null) {
    // remove pad from author
    delete author.padIDs[padID];
    await db.set(`globalAuthor:${authorID}`, author);
  }
};
