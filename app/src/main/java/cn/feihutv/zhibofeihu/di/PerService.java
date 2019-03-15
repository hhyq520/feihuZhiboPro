package cn.feihutv.zhibofeihu.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : 依赖注入：定义注入体
 *     version: 1.0
 * </pre>
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerService {
}

