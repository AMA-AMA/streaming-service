package com.epam.audio_streaming.repository.models;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Component;

@Component
@NoRepositoryBean
public interface  GenericRepository<T, U> extends JpaRepository<T, U> {

    T findByName(String name);

    Page<T> findAll(Pageable pageable);

}
