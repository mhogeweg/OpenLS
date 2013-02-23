/* See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * Esri Inc. licenses this file to You under the Apache License, Version 2.0
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
package com.esri.gpt.server.openls.provider.services.poi;


public class QueryFilter {
    public static String exact = "0";
    public static String like = "1";
    public static String key = "2";
    private String operation;
    private String field;
    private String value;

    public QueryFilter(String operation, String field, String value) {
        this.operation = operation;
        this.field = field;
        this.value = value;
    }

    public QueryFilter(String field, String value) {
    	this.operation = exact;
    	this.field = field;
        this.value = value;    	
	}

	public String getOperation() {
        if (operation.equalsIgnoreCase(like)){
            return " LIKE ";
        }
        return " = ";
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }


    public String toString() {
        if (operation.equals(exact))
            return field+" = '"+value+"'";
        else if(operation.equals(like))
            return field+" LIKE '"+value.toUpperCase()+"%'";
        else
          return field+" LIKE '%"+value.toUpperCase()+"%'";
    }
}
