package com.epam.audio_streaming.annotation;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Decorate {

    Class<?>[] decorators() default {};

}


