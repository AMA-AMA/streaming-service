package com.epam.audio_streaming.service;

import com.epam.audio_streaming.annotation.Decorate;
import com.epam.audio_streaming.annotation.StorageType;
import com.epam.audio_streaming.service.storage.StorageFactory;
import com.epam.audio_streaming.service.storage.StorageSourceService;
import com.epam.audio_streaming.service.storage.decorator.StorageDecoratorFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

@Component
public class CustomBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    private StorageDecoratorFactory storageDecoratorFactory;
    @Autowired
    private StorageFactory storageFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if (StorageSourceService.class.isAssignableFrom(bean.getClass())) {

            Decorate decorators = AnnotationUtils.findAnnotation(bean.getClass(), Decorate.class);
            StorageType storageType = AnnotationUtils.findAnnotation(bean.getClass(), StorageType.class);

            if (decorators != null) {
                for (Class<?> decorator : decorators.decorators()) {
                    bean = storageDecoratorFactory.decoratorOrder((StorageSourceService) bean, decorator);
                }
            }
            if (storageType != null) {
                storageFactory.getStorage(storageType.value(), (StorageSourceService) bean);
            }
        }
        return bean;
    }
}
