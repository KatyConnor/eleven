package hx.nine.eleven.bytebuddy.aop.advice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 *  切点注解
 * @author wml
 * @date 2023-04-15
 */
public class AnnotationMethodMatcher implements MethodMatcher {

	private final List<Class<? extends Annotation>> annotationTypes;

	public AnnotationMethodMatcher(List<Class<? extends Annotation>> annotationTypes) {
		this.annotationTypes = annotationTypes;
	}

	/**
	 * 目标类查找切点,如果目标类有多个切点注解，此处可能只会随机创建一个切点代理类
	 * @TODO issue： 如果目标类有多个切点注解，此处可能只会随机创建一个切点代理类
	 * <p>
	 *     此issue后续计划进行修复，满足多切点类型实现
	 * </p>
	 * @param targetClass
	 * @return
	 */
	@Override
	public Annotation matches(Class<?> targetClass) {
		if (Optional.ofNullable(this.annotationTypes).isPresent()){
			for (Class<? extends Annotation> anClasses : this.annotationTypes){
				Annotation annotation = targetClass.getAnnotation(anClasses);
				if (!Optional.ofNullable(annotation).isPresent()){
					annotation = targetClass.getDeclaredAnnotation(anClasses);
				}
				if (Optional.ofNullable(annotation).isPresent()){
					return annotation;
				}
				Method[] methods = targetClass.getDeclaredMethods();
				if (Optional.ofNullable(methods).isPresent()){
					for (Method method : methods){
						Annotation an = matchesMethod(method,anClasses);
						if (Optional.ofNullable(an).isPresent()){
							return an;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * 根据注解查找当前方式是否匹配
	 * @param method     执行方法
	 * @param anClasses  注解
	 * @return
	 */
	private Annotation matchesMethod(Method method,Class<? extends Annotation> anClasses) {
		Annotation annotation = method.getAnnotation(anClasses);
		if (!Optional.ofNullable(annotation).isPresent()){
			annotation = method.getDeclaredAnnotation(anClasses);
		}
		return annotation;
	}
}
