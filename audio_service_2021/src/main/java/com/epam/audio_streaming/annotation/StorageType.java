package com.epam.audio_streaming.annotation;

import com.epam.audio_streaming.model.StorageTypes;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface StorageType {

    StorageTypes value();

}


