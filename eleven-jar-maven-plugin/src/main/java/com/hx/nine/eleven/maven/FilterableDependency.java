/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hx.nine.eleven.maven;

import org.apache.maven.plugins.annotations.Parameter;

/**
 * A model for a dependency to include or exclude.
 *
 * @author Stephane Nicoll
 * @author David Turanski
 */
abstract class FilterableDependency {

	/**
	 * The groupId of the artifact to exclude.
	 */
	@Parameter(required = true)
	private String groupId;

	/**
	 * The artifactId of the artifact to exclude.
	 */
	@Parameter(required = true)
	private String artifactId;

	/**
	 * The classifier of the artifact to exclude.
	 */
	@Parameter
	private String classifier;

	String getGroupId() {
		return this.groupId;
	}

	void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	String getArtifactId() {
		return this.artifactId;
	}

	void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	String getClassifier() {
		return this.classifier;
	}

	void setClassifier(String classifier) {
		this.classifier = classifier;
	}

}
