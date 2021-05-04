package com.epam.audio_streaming.repository.elastic;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoRepositoryBean
public interface GenericElasticRepository<T, U> extends ElasticsearchRepository<T, U> {

    T findByName(String name);

    List<T> findByNameContaining(String name);
}
