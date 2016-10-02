package org.agreen.repository;

import org.agreen.domain.Media;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Media entity.
 */
public interface MediaRepository extends MongoRepository<Media,String> {

	Media findByName(String name);

}
