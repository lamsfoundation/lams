/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Helper for JSON objects.
 *
 * @author Marcin Cieslak
 *
 */
public class JsonUtil {
    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static JsonNode opt(JsonNode object, String field) {
	return JsonUtil.opt(object, field, null);
    }

    public static JsonNode opt(JsonNode object, String field, JsonNode defaultValue) {
	return object.hasNonNull(field) ? object.get(field) : defaultValue;
    }

    public static ObjectNode optObject(JsonNode object, String field) {
	return object.hasNonNull(field) ? (ObjectNode) object.get(field) : null;
    }

    public static ArrayNode optArray(JsonNode object, String field) {
	return object.hasNonNull(field) ? (ArrayNode) object.get(field) : null;
    }

    public static Boolean optBoolean(JsonNode object, String field) {
	return JsonUtil.optBoolean(object, field, null);
    }

    public static Boolean optBoolean(JsonNode object, String field, Boolean defaultValue) {
	return object.hasNonNull(field) ? (Boolean) object.get(field).asBoolean() : defaultValue;
    }

    public static Long optLong(JsonNode object, String field) {
	return JsonUtil.optLong(object, field, null);
    }

    public static Long optLong(JsonNode object, String field, Long defaultValue) {
	return object.hasNonNull(field) ? (Long) object.get(field).asLong() : null;
    }

    public static Double optDouble(JsonNode object, String field, Double defaultValue) {
	return object.hasNonNull(field) ? (Double) object.get(field).asDouble() : defaultValue;
    }

    public static Double optDouble(JsonNode object, String field) {
	return JsonUtil.optDouble(object, field, null);
    }

    public static Integer optInt(JsonNode object, String field) {
	return JsonUtil.optInt(object, field, null);
    }

    public static Integer optInt(JsonNode object, String field, Integer defaultValue) {
	return object.hasNonNull(field) ? (Integer) object.get(field).asInt() : defaultValue;
    }

    public static String optString(JsonNode object, String field) {
	return JsonUtil.optString(object, field, null);
    }

    public static String optString(JsonNode object, String field, String defaultValue) {
	return object.hasNonNull(field) ? object.get(field).asText() : defaultValue;
    }

    public static ArrayNode readArray(String content) throws JsonProcessingException, IOException {
	return (ArrayNode) objectMapper.readTree(content);
    }

    public static ObjectNode readObject(String content) throws JsonProcessingException, IOException {
	return (ObjectNode) objectMapper.readTree(content);
    }

    public static ArrayNode readArray(Object object) throws JsonProcessingException, IOException {
	return (ArrayNode) objectMapper.valueToTree(object);
    }

    public static ObjectNode readObject(Object object) throws JsonProcessingException, IOException {
	return (ObjectNode) objectMapper.valueToTree(object);
    }

    public static String toString(Object object) throws JsonProcessingException {
	return objectMapper.writeValueAsString(object);
    }

    public static ObjectNode putOpt(ObjectNode object, String field, Object value) throws JsonProcessingException {
	if (value != null) {
	    object.set(field, objectMapper.valueToTree(value));
	}
	return object;
    }
}