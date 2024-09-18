package com.hx.vertx.file.monitor.shell;

import com.hx.vertx.file.monitor.log.HXLogger;
import com.hx.vertx.file.monitor.utils.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author wangml
 * @date 2022-09-09
 * 脚本文件执行工具类
 */
public class ShellRunTimeExec {

  public static void runShell(String command) {
    if (StringUtils.isEmpty(command)) {
      HXLogger.build(ShellRunTimeExec.class).info("同步脚本为空");
      return;
    }
    BufferedReader br = null;
    Process proc = null;
    try {
      proc = Runtime.getRuntime().exec(command);
      br = new BufferedReader(new InputStreamReader(proc.getInputStream(), "utf-8"));
      String line;
      while ((line = br.readLine()) != null) {
        HXLogger.build(ShellRunTimeExec.class).info("{}", line);
      }
    } catch (IOException e) {
      HXLogger.build(ShellRunTimeExec.class).info("{}", e);
    } finally {
      if (proc != null) {
        proc.destroy();
      }
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          HXLogger.build(ShellRunTimeExec.class).info("{}", e);
        }
      }
    }
  }
}
