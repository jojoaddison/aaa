package org.agreen.repository;

import org.agreen.domain.Blog;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Blog entity.
 */
@SuppressWarnings("unused")
public interface BlogRepository extends MongoRepository<Blog,String> {

}
