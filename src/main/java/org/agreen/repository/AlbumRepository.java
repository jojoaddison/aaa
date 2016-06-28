package org.agreen.repository;

import org.agreen.domain.Album;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Album entity.
 */
@SuppressWarnings("unused")
public interface AlbumRepository extends MongoRepository<Album,String> {

}
