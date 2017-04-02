package org.agreen.repository;

import java.util.List;
import java.util.Set;

import org.agreen.domain.Gallery;
import org.agreen.domain.Media;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Media entity.
 */
@Repository
public interface MediaRepository extends MongoRepository<Media,String> {

	Media findByName(String name);

	Set<Media> findByDescription(String description);
	/*
	@Query(value="select new Gallery(gm) from Media gm group by gm.description")
	List<Gallery> findGroupMedia();
	*/
}
