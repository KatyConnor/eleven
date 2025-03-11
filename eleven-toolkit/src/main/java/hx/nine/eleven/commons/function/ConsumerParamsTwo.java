package hx.nine.eleven.commons.function;

/**
 * @author wangml
 * @Date 2019-09-10
 */
@FunctionalInterface
public interface ConsumerParamsTwo<T,P,P1> {

    /**
     *
     * @param t
     * @param p
     * @param p1
     */
    void accept(T t,P p,P1 p1);
}
