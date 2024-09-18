package com.hx.vertx.file.monitor.thread;

import com.hx.vertx.file.monitor.log.HXLogger;
import com.hx.vertx.file.monitor.queue.FileSyncFailedQueue;
import com.hx.vertx.file.monitor.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.Set;

/**
 * 同步失败文件持久化线程
 *
 * @author wml
 * @Discription
 * @Date 2023-03-11
 */
public class FileSyncFailedStorageThread extends Thread {

  private String fileStoragePath;

  private String storageFileName;

  public FileSyncFailedStorageThread() {
    HXLogger.build(this).info("启动文件同步失败持久化线程");
    this.fileStoragePath = System.getProperty("user.dir");
    this.storageFileName = this.fileStoragePath+"/fileStorage.db";
  }

  @Override
  public void run() {
    while (true){
      Set<String> fileSet = FileSyncFailedQueue.build().getAll();
      if (Optional.ofNullable(fileSet).isPresent()){
        try {
          HXLogger.build(this).info("休眠2分钟");
          Thread.sleep(2000 * 30);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      Optional.ofNullable(fileSet).ifPresent(file -> {
        HXLogger.build(this).info("开始持久化文件同步失败队列");
        try {
          if (!Files.exists(Paths.get(this.storageFileName))) {
            Files.createFile(Paths.get(this.storageFileName));
          }else {
            Files.delete(Paths.get(this.fileStoragePath+"/fileStorage-tmp.db"));
            Files.copy(Paths.get(this.storageFileName),Paths.get(this.fileStoragePath+"/fileStorage-tmp.db"));
            Files.delete(Paths.get(this.storageFileName));
            Files.createFile(Paths.get(this.storageFileName));
          }
          file.forEach(v -> {
            //以追加的形式写入文件
            try {
              Files.write(Paths.get(this.storageFileName), v.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            } catch (IOException e) {
              e.printStackTrace();
            }
          });
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
    }
  }
}
