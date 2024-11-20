package hx.nine.eleven.mybatisflex;

import hx.nine.eleven.core.core.ElevenApplicationContextAware;
import hx.nine.eleven.mybatisflex.mapper.ElevenBaseMapper;
import com.mybatisflex.core.MybatisFlexBootstrap;

import javax.sql.DataSource;
import java.util.Set;

/**
 * @auth wml
 * @date 2024/9/24
 */
public class ElevenMybatisFlexBootstrap extends MybatisFlexBootstrap{

	private static volatile ElevenMybatisFlexBootstrap instance;

	public static ElevenMybatisFlexBootstrap getInstance() {
		if (instance == null) {
			Class bootstrapClass = ElevenMybatisFlexBootstrap.class;
			synchronized(ElevenMybatisFlexBootstrap.class) {
				if (instance == null) {
					instance = new ElevenMybatisFlexBootstrap();
				}
			}
		}
		return instance;
	}

	public ElevenMybatisFlexBootstrap initDataSource() {
		DataSource dataSource = ElevenApplicationContextAware.getBean("dataSource");
		super.setDataSource(dataSource);
		return this;
	}

	public ElevenMybatisFlexBootstrap addMappers(Set<Class<? extends ElevenBaseMapper>> type) {
		type.forEach(v ->{
			this.addMapper(v);
		});
		 return this;
	}
}
