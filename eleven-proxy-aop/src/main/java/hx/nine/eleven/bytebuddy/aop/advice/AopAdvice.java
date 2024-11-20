package hx.nine.eleven.bytebuddy.aop.advice;

public interface AopAdvice {

	Object adviceBefore(Object object, String methodName, Class[] paramTypes, Object... args);

	Object adviceAfter(Object object);
}
