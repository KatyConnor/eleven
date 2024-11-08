package com.hx.nine.eleven.commons;

/**
 * 文件默认处理权限
 * @auth wml
 * @date 2024/11/8
 */
public class CopyOptions {

	private boolean replaceExisting;
	private boolean copyAttributes;
	private boolean atomicMove;
	private boolean nofollowLinks;

	public CopyOptions() {
		this.replaceExisting = false;
		this.copyAttributes = false;
		this.atomicMove = false;
		this.nofollowLinks = false;
	}

	public CopyOptions(CopyOptions other) {
		this.replaceExisting = other.replaceExisting;
		this.copyAttributes = other.copyAttributes;
		this.atomicMove = other.atomicMove;
		this.nofollowLinks = other.nofollowLinks;
	}

	public boolean isReplaceExisting() {
		return this.replaceExisting;
	}

	public CopyOptions setReplaceExisting(boolean replaceExisting) {
		this.replaceExisting = replaceExisting;
		return this;
	}

	public boolean isCopyAttributes() {
		return this.copyAttributes;
	}

	public CopyOptions setCopyAttributes(boolean copyAttributes) {
		this.copyAttributes = copyAttributes;
		return this;
	}

	public boolean isAtomicMove() {
		return this.atomicMove;
	}

	public CopyOptions setAtomicMove(boolean atomicMove) {
		this.atomicMove = atomicMove;
		return this;
	}

	public boolean isNofollowLinks() {
		return this.nofollowLinks;
	}

	public CopyOptions setNofollowLinks(boolean nofollowLinks) {
		this.nofollowLinks = nofollowLinks;
		return this;
	}
}
