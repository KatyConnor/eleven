package com.hx.nine.eleven.domain.factory;

import com.hx.nine.eleven.domain.BeanFactoryLocator;
import com.hx.nine.eleven.commons.utils.StringUtils;
import com.hx.nine.eleven.domain.obj.domain.Domain;
import com.hx.nine.eleven.domain.exception.DomainOperatorException;
import com.hx.nine.eleven.domain.syscode.DomainApplicationSysCode;
import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.github.f4b6a3.ulid.UlidCreator;
import com.hx.nine.eleven.core.annotations.Component;
import net.sf.cglib.beans.BeanMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Optional;

/**
 * @Description 领域对象实例化工厂，暂时只支持属性添加Autowired、Resource注解
 * @Author wangml
 * @Date 2019-08-30
 */
@Component
public class AutowiredDomainFactory{

    private static final Logger LOGGER = LoggerFactory.getLogger(AutowiredDomainFactory.class);

    public <T> T create(Class<T> classzz) {
        if (classzz == null) {
            throw new DomainOperatorException(DomainApplicationSysCode.A0100000000);
        }
        LOGGER.info("create a new domain， [{}]",classzz.getSimpleName());
        try {
            ConstructorAccess<T> constructor = ConstructorAccess.get(classzz);
            T obj = constructor.newInstance();
            Field[] fields = classzz.getDeclaredFields();
            BeanMap beanMap = BeanMap.create(obj);
            for (Field field : fields) {
//                Autowired autowired = field.getAnnotation(Autowired.class);
//                Optional.ofNullable(autowired).ifPresent(auto ->{
//                    beanMap.put(field.getName(), BeanFactoryLocator.getBean(field.getType()));
//                });

                Resource resource = field.getAnnotation(Resource.class);
                Optional.ofNullable(resource).ifPresent(res ->{
                    String name = resource.name();
                    if (StringUtils.isNotBlank(name)) {
                        beanMap.put(field.getName(), BeanFactoryLocator.getBean(name));
                    }else {
                        beanMap.put(field.getName(), BeanFactoryLocator.getBean(field.getType()));
                    }
                });
            }

            if (Domain.class.isInstance(obj)) {
                beanMap.put("gid",UlidCreator.getUlid().toString());
            }
            return (T) beanMap.getBean();
        } catch (Exception e) {
            LOGGER.error(StringUtils.format("Failed to initialize create domain ,[{}]",classzz.getSimpleName()), e);
        }
        throw new DomainOperatorException(DomainApplicationSysCode.A0100000001,classzz.getSimpleName());
    }
}
