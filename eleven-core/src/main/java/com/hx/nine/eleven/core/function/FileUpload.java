package com.hx.nine.eleven.core.function;

/**
 * @auth wml
 * @date 2024/11/7
 */
@FunctionalInterface
public interface FileUpload {

	boolean upload(String uploadedFileName, String targetFile);
}
