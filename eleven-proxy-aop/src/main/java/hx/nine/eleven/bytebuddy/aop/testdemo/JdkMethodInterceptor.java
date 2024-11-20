package hx.nine.eleven.bytebuddy.aop.testdemo;

import hx.nine.eleven.bytebuddy.aop.interceptor.MethodInterceptor;
import hx.nine.eleven.bytebuddy.aop.invoke.Invocation;

import java.lang.reflect.Method;

public class JdkMethodInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = null;
        Method method = invocation.getMethod();
        Object target = invocation.getTarget();
        Object[] arguments = invocation.getArguments();
        String da = AnnotaionUtils.findKey(method,target,arguments);
        try {
            System.out.println("方法执行前处理,查找到注解："+da);
            result = invocation.proceed();
            System.out.println("方法执行结束");
        }catch (Throwable ex){
            System.out.println("方法执行异常");
            throw ex;
        }
        return result;
    }
}
