package com.enus.newsletter.db;

import org.springframework.data.jpa.repository.JpaRepository;

public abstract class AbsBaseRepository<T, U extends JpaRepository<T, Long>> {
    protected final U repository;

    protected AbsBaseRepository(U repository) {
        this.repository = repository;
    }

    public T save(T entity) {
        return repository.save(entity);
    }
}
