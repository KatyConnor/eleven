/*
 * Copyright 2012-2022 the original author or authors.
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

import java.io.IOException;
import java.lang.management.ManagementFactory;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Stop an application that has been started by the "start" goal. Typically invoked once a
 * test suite has completed.
 *
 * @author Stephane Nicoll
 * @since 1.3.0
 */
@Mojo(name = "stop", requiresProject = true, defaultPhase = LifecyclePhase.POST_INTEGRATION_TEST)
public class StopMojo extends AbstractMojo {

	/**
	 * The Maven project.
	 * @since 1.4.1
	 */
	@Parameter(defaultValue = "${project}", readonly = true, required = true)
	private MavenProject project;

	/**
	 * Flag to indicate if the process to stop was forked. By default, the value is
	 * inherited from the {@link MavenProject} with a fallback on the default fork value
	 * ({@code true}). If it is set, it must match the value used to {@link StartMojo
	 * start} the process.
	 * @since 1.3.0
	 * @deprecated since 2.7.0 for removal in 3.0.0 with no replacement
	 */
	@Parameter(property = "eleven.stop.fork")
	@Deprecated
	private Boolean fork;

	/**
	 * The JMX name of the automatically deployed MBean managing the lifecycle of the
	 * application.
	 */
	@Parameter(defaultValue = SpringApplicationAdminClient.DEFAULT_OBJECT_NAME)
	private String jmxName;

	/**
	 * The port to use to look up the platform MBeanServer if the application has been
	 * forked.
	 */
	@Parameter(defaultValue = "9001")
	private int jmxPort;

	/**
	 * Skip the execution.
	 * @since 1.3.2
	 */
	@Parameter(property = "eleven.stop.skip", defaultValue = "false")
	private boolean skip;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (this.skip) {
			getLog().debug("skipping stop as per configuration.");
			return;
		}
		getLog().info("Stopping application...");
		try {
			if (isForked()) {
				stopForkedProcess();
			}
			else {
				stop();
			}
		}
		catch (IOException ex) {
			// The response won't be received as the server has died - ignoring
			getLog().debug("Service is not reachable anymore (" + ex.getMessage() + ")");
		}
	}

	@Deprecated
	private boolean isForked() {
		if (this.fork != null) {
			return this.fork;
		}
		String forkFromStart = this.project.getProperties().getProperty("_spring.boot.fork.enabled");
		if (forkFromStart != null) {
			return Boolean.parseBoolean(forkFromStart);
		}
		return true;
	}

	private void stopForkedProcess() throws IOException, MojoExecutionException {
		try (JMXConnector connector = SpringApplicationAdminClient.connect(this.jmxPort)) {
			MBeanServerConnection connection = connector.getMBeanServerConnection();
			doStop(connection);
		}
	}

	private void stop() throws IOException, MojoExecutionException {
		doStop(ManagementFactory.getPlatformMBeanServer());
	}

	private void doStop(MBeanServerConnection connection) throws IOException, MojoExecutionException {
		try {
			new SpringApplicationAdminClient(connection, this.jmxName).stop();
		}
		catch (InstanceNotFoundException ex) {
			throw new MojoExecutionException("Spring application lifecycle JMX bean not found (fork is " + this.fork
					+ "). Could not stop application gracefully", ex);
		}
	}

}
