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

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import org.springframework.boot.loader.tools.RunProcess;

/**
 * Run an application in place.
 *
 * @author wml
 * @since 1.0.0
 */
@Mojo(name = "run", requiresProject = true, defaultPhase = LifecyclePhase.VALIDATE,
		requiresDependencyResolution = ResolutionScope.TEST)
@Execute(phase = LifecyclePhase.TEST_COMPILE)
public class RunMojo extends AbstractRunMojo {

	private static final int EXIT_CODE_SIGINT = 130;

	private static final String RESTARTER_CLASS_LOCATION = "org/springframework/boot/devtools/restart/Restarter.class";

	/**
	 * Devtools存在标志，以避免每次执行多次检查
	 */
	private Boolean hasDevtools;

	/**
	 * 是否应该优化JVM的启动
	 * @since 2.2.0
	 */
	@Parameter(property = "eleven.run.optimizedLaunch", defaultValue = "true")
	private boolean optimizedLaunch;

	@Override
	@Deprecated
	protected void logDisabledFork() {
		super.logDisabledFork();
		if (hasDevtools()) {
			getLog().warn("Fork mode disabled, devtools will be disabled");
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	protected RunArguments resolveJvmArguments() {
		RunArguments jvmArguments = super.resolveJvmArguments();
		if (isFork() && this.optimizedLaunch) {
			jvmArguments.getArgs().addFirst("-XX:TieredStopAtLevel=1");
			if (!isJava13OrLater()) {
				jvmArguments.getArgs().addFirst("-Xverify:none");
			}
		}
		return jvmArguments;
	}

	private boolean isJava13OrLater() {
		for (Method method : String.class.getMethods()) {
			if (method.getName().equals("stripIndent")) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void runWithForkedJvm(File workingDirectory, List<String> args, Map<String, String> environmentVariables)
			throws MojoExecutionException {
		int exitCode = forkJvm(workingDirectory, args, environmentVariables);
		if (exitCode == 0 || exitCode == EXIT_CODE_SIGINT) {
			return;
		}
		throw new MojoExecutionException("Application finished with exit code: " + exitCode);
	}

	private int forkJvm(File workingDirectory, List<String> args, Map<String, String> environmentVariables)
			throws MojoExecutionException {
		try {
			RunProcess runProcess = new RunProcess(workingDirectory, getJavaExecutable());
			Runtime.getRuntime().addShutdownHook(new Thread(new RunProcessKiller(runProcess)));
			return runProcess.run(true, args, environmentVariables);
		}
		catch (Exception ex) {
			throw new MojoExecutionException("Could not exec java", ex);
		}
	}

	@Override
	@Deprecated
	protected void runWithMavenJvm(String startClassName, String... arguments) throws MojoExecutionException {
		IsolatedThreadGroup threadGroup = new IsolatedThreadGroup(startClassName);
		Thread launchThread = new Thread(threadGroup, new LaunchRunner(startClassName, arguments), "main");
		launchThread.setContextClassLoader(new URLClassLoader(getClassPathUrls()));
		launchThread.start();
		join(threadGroup);
		threadGroup.rethrowUncaughtException();
	}

	private void join(ThreadGroup threadGroup) {
		boolean hasNonDaemonThreads;
		do {
			hasNonDaemonThreads = false;
			Thread[] threads = new Thread[threadGroup.activeCount()];
			threadGroup.enumerate(threads);
			for (Thread thread : threads) {
				if (thread != null && !thread.isDaemon()) {
					try {
						hasNonDaemonThreads = true;
						thread.join();
					}
					catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
					}
				}
			}
		}
		while (hasNonDaemonThreads);
	}

	private boolean hasDevtools() {
		if (this.hasDevtools == null) {
			this.hasDevtools = checkForDevtools();
		}
		return this.hasDevtools;
	}

	private boolean checkForDevtools() {
		try {
			URL[] urls = getClassPathUrls();
			try (URLClassLoader classLoader = new URLClassLoader(urls)) {
				return (classLoader.findResource(RESTARTER_CLASS_LOCATION) != null);
			}
		}
		catch (Exception ex) {
			return false;
		}
	}

	private static final class RunProcessKiller implements Runnable {

		private final RunProcess runProcess;

		private RunProcessKiller(RunProcess runProcess) {
			this.runProcess = runProcess;
		}

		@Override
		public void run() {
			this.runProcess.kill();
		}

	}

}
