package cn.feihutv.zhibofeihu.rxbus;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : Rx bus subscribe
 *     version: 1.0
 * </pre>
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RxBusSubscribe {
    int code() default -1;

    ThreadMode threadMode() default ThreadMode.CURRENT_THREAD;
}
