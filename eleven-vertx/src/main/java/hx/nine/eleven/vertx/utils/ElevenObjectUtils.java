package hx.nine.eleven.vertx.utils;

import hx.nine.eleven.commons.utils.StringUtils;
import hx.nine.eleven.core.utils.ElevenLoggerFactory;
import hx.nine.eleven.core.utils.SystemUtils;
import hx.nine.eleven.vertx.FutureCall;
import io.vertx.core.Future;
import io.vertx.core.file.CopyOptions;
import io.vertx.core.file.FileSystem;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Set;

public class ElevenObjectUtils {

	//文件处理
	private FileSystem fileSystem;
	public static final CopyOptions DEFAULT_OPTIONS = new CopyOptions();

	public boolean move(String from, String to){
		isCreateTargetPath(to);
		Future<Void> future = fileSystem.move(from,to);
		return future.onComplete(h->{
			if (h.succeeded()){
				ElevenLoggerFactory.build(this).info("文件从[{}]移动到[{}]成功",from,to);
			}else {
				ElevenLoggerFactory.build(this).info("文件从[{}]移动到[{}]失败",from,to);
			}
		}).succeeded();
	}

	public boolean move(String from, String to, FutureCall futureCall){
		isCreateTargetPath(to);
		Future<Void> future = fileSystem.move(from,to);
		return future.onComplete(h->{
			if (h.succeeded()){
				ElevenLoggerFactory.build(this).info("文件从[{}]移动到[{}]成功",from,to);
				futureCall.successCall();
			}else {
				ElevenLoggerFactory.build(this).info("文件从[{}]移动到[{}]失败",from,to);
				futureCall.failCall();
			}
		}).succeeded();
	}

	/**
	 * 线程同步处理文件
	 * @param from     源文件地址
	 * @param to	   目标文件文件地址
	 * @param options  参数
	 * @return
	 */
	public boolean move(String from, String to,CopyOptions options){
		isCreateTargetPath(to);
		Set<CopyOption> copyOptionSet = toCopyOptionSet(options);
		CopyOption[] copyOptions = copyOptionSet.toArray(new CopyOption[0]);
		try {
			Files.move(Paths.get(from), Paths.get(to), copyOptions);
			return true;
		} catch (IOException e) {
			ElevenLoggerFactory.build(this).info("文件从[{}]移动到[{}]失败",from,to);
		}
		return false;
	}

	/**
	 * 异步处理删掉文件
	 * 需保证后续没有线程获取当前文件
	 * @param path
	 * @return
	 */
	public boolean delete(String path){
		Future<Void> future = fileSystem.delete(path);
		return future.onComplete(h->{
			if (h.succeeded()){
				ElevenLoggerFactory.build(this).info("文件[{}]删除成功",path);
			}else {
				ElevenLoggerFactory.build(this).info("文件[{}]删除失败",path);
			}
		}).succeeded();
	}

	public boolean delete(Path path){
		try {
			Files.delete(path);
			return true;
		} catch (IOException e) {
			ElevenLoggerFactory.build(this).error(StringUtils.format("文件[{}]删除失败， ",path.toUri().getPath()),e);
		}
		return false;
	}

	public FileSystem getFileSystem() {
		return fileSystem;
	}

	public void setFileSystem(FileSystem fileSystem) {
		this.fileSystem = fileSystem;
	}

	public static ElevenObjectUtils build(){
		return Single.INSTANCE;
	}

	private final static class Single{
		private final static ElevenObjectUtils INSTANCE = new ElevenObjectUtils();
	}

	/**
	 * 分析文件路径是否存在，不存在直接创建目录，如果文件存在则先删除文件
	 * @param targetFile
	 */
	private void isCreateTargetPath(String targetFile){
		Path tFilePath = Paths.get(targetFile);
		if (Files.exists(tFilePath)) {
			try {
				Files.delete(tFilePath);
			} catch (IOException e) {
				ElevenLoggerFactory.build(this).error(StringUtils.format("删除已存在的目标文件[{}]失败",targetFile),e);
			}
		}

		int lastSpile = targetFile.lastIndexOf(SystemUtils.osName() == 1 ? "\\" : "/");
		String targetFilePath = targetFile.substring(0, lastSpile) + (SystemUtils.osName() == 1 ? "\\" : "/");
		Path targetPath = Paths.get(targetFilePath);
		if (!Files.exists(targetPath)) {
			try {
				Files.createDirectory(targetPath);
			} catch (IOException e) {
				ElevenLoggerFactory.build(this).error(StringUtils.format("文件目录[{}]创建失败",targetFilePath),e);
			}
		}
	}

	private Set<CopyOption> toCopyOptionSet(CopyOptions copyOptions) {
		Set<CopyOption> copyOptionSet = new HashSet<>();
		if (copyOptions.isReplaceExisting()) copyOptionSet.add(StandardCopyOption.REPLACE_EXISTING);
		if (copyOptions.isCopyAttributes()) copyOptionSet.add(StandardCopyOption.COPY_ATTRIBUTES);
		if (copyOptions.isAtomicMove()) copyOptionSet.add(StandardCopyOption.ATOMIC_MOVE);
		if (copyOptions.isNofollowLinks()) copyOptionSet.add(LinkOption.NOFOLLOW_LINKS);
		return copyOptionSet;
	}
}
