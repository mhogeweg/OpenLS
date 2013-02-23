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
package com.esri.gpt.server.openls.provider.util;

public class ImageOutput {
	private Output output;
	private String url="";
	private BBoxContext bboxContext;
	/**
	 * @return the output
	 */
	public Output getOutput() {
		return output;
	}
	/**
	 * @param output the output to set
	 */
	public void setOutput(Output output) {
		this.output = output;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the bboxContext
	 */
	public BBoxContext getBboxContext() {
		return bboxContext;
	}
	/**
	 * @param bboxContext the bboxContext to set
	 */
	public void setBboxContext(BBoxContext bboxContext) {
		this.bboxContext = bboxContext;
	}


}
