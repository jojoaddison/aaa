package org.agreen.repository;

import org.agreen.domain.Photo;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Photo entity.
 */
@SuppressWarnings("unused")
public interface PhotoRepository extends MongoRepository<Photo,String> {

}
