package org.agreen.repository;

import java.util.List;

import org.agreen.domain.Page;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Page entity.
 */
public interface PageRepository extends MongoRepository<Page,String> {

	Page findOneByLangAndName(String lang, String name);

	List<Page> findAllByName(String name);

	Page findOneByPid(String pid);

}
