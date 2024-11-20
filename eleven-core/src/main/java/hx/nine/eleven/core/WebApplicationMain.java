package hx.nine.eleven.core;

/**
 * web容器集成进来可实现改接口实现容器启动、停止、重启功能
 * @auth wml
 * @date 2024/11/5
 */
public abstract class WebApplicationMain {

	/**
	 * 启动服务
	 * @param args
	 */
	public abstract void start(String[] args);

	/**
	 * 停止服务
	 * @param args
	 */
	public abstract void stop(String[] args);

	/**
	 * 重启服务
	 * @param args
	 */
	public abstract void restart(String[] args);

}
