package com.blockchain.wallet.api.annotation;


import java.lang.annotation.*;

/**
 * 在Controller方法上加入改注解会自动记录日志
 * @author : zhangkai
 * @date : 2018/05/08
 */
@Target( { ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface Log {
	/**
	 * 描述.
	 */
	String description() default "";

}
