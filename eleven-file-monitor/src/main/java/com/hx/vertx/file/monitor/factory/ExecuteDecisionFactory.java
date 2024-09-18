package com.hx.vertx.file.monitor.factory;

/**
 * @author wml
 * @Discription
 * @Date 2023-03-11
 */
public class ExecuteDecisionFactory {

  public static <E extends StrategyFactory> void strategy(E factory){
    factory.runExe();
  }

  public static <E extends StrategyCallFactory> Object strategy(E factory){
    return factory.runExe();
  }
}
