package org.agreen.repository;

import org.agreen.domain.Project;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Project entity.
 */
@SuppressWarnings("unused")
public interface ProjectRepository extends MongoRepository<Project,String> {

}
