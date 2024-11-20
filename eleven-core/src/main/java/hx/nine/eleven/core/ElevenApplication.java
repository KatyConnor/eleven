package hx.nine.eleven.core;

import com.google.common.base.Stopwatch;
import hx.nine.eleven.commons.utils.StringUtils;
import hx.nine.eleven.core.constant.DefualtProperType;
import hx.nine.eleven.core.core.ElevenApplicationContextAware;
import hx.nine.eleven.core.core.context.ClassPathBeanDefinitionScanner;
import hx.nine.eleven.core.env.ElevenYamlReadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 服务启动类
 * @auth wml
 * @date 2024/11/5
 */
public class ElevenApplication {

	private final static Logger LOGGER = LoggerFactory.getLogger(ElevenApplication.class);

	/**
	 * 启动服务,参数可以指定 eleven.boot.configPath 来加载外部配置文件
	 * @param args
	 */
	public static void start(String[] args) {
		Stopwatch stopwatch = Stopwatch.createStarted();
		String properties = null;
		if (args != null && args.length > 0){
			for (String arg : args){
				LOGGER.info("入参：{}！", arg);
				if (arg.startsWith("-D"+ DefualtProperType.CONFIG_PATH) || arg.startsWith(DefualtProperType.CONFIG_PATH)){
					properties = arg;
					break;
				}
			}
		}
		// 1、加载配置文件，初始化环境变量
		if (StringUtils.isNotEmpty(properties)){
			String[] prop = properties.split("=");
			if (prop[0].startsWith("-D")){
				prop[0] = prop[0].substring(2);
			}
			ElevenYamlReadUtils.build().addProperties(prop[0],prop[1]).readYamlConfiguration(prop[1]);
		}else {
			ElevenYamlReadUtils.build().readYamlConfiguration(null);
		}
		// 初始化bean容器，和上下文HXVertxApplicationContext
		ClassPathBeanDefinitionScanner.build().initClass();
		LOGGER.info("容器初始化完成！");
		// 启动服务
		WebApplicationMain webApplicationMain = ElevenApplicationContextAware.getSubTypesOfBean(WebApplicationMain.class);
		webApplicationMain.start(args);
		stopwatch.stop();
		LOGGER.info("服务启动成功！耗时：[{}] 秒",stopwatch.elapsed(TimeUnit.SECONDS));
	}

	/**
	 * 停止服务
	 */
	public static void stop(){

	}

	/**
	 * 重启服务
	 * @param args
	 */
	public static void restart(String[] args){

	}
}
