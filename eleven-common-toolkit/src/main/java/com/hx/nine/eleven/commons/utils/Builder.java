package com.hx.nine.eleven.commons.utils;

import com.hx.nine.eleven.commons.function.ConsumerParam;
import com.hx.nine.eleven.commons.function.ConsumerParams;
import com.hx.nine.eleven.commons.function.ConsumerParamsTwo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 初始化类，并设置属性值
 * @author wangml
 * @Date 2019-09-10
 */
public class Builder<T> {

    private final Supplier<T> instantiator;
    private List<Consumer<T>> modifiers = new ArrayList<>();

    public Builder(Supplier<T> instantiator) {
        this.instantiator = instantiator;
    }

    public static <T> Builder<T> of(Supplier<T> instantiator){
        return new Builder<>(instantiator);
    }

    public <P> Builder<T> with(ConsumerParam<T,P> consumer, P p){
        Consumer<T> c = instance -> consumer.accept(instance,p);
        modifiers.add(c);
        return this;
    }

    public <P,P1> Builder<T> with(ConsumerParamsTwo<T,P,P1> consumer, P p, P1 p1){
        Consumer<T> c = instance -> consumer.accept(instance,p,p1);
        modifiers.add(c);
        return this;
    }

    public <P> Builder<T> with(ConsumerParams<T,P> consumer, List<P> ps){
        Consumer<T> c = instance -> consumer.accept(instance,ps);
        modifiers.add(c);
        return this;
    }

    public T build(){
        T value = this.instantiator.get();
        modifiers.forEach(m -> m.accept(value));
        modifiers.clear();
        return value;
    }
}
