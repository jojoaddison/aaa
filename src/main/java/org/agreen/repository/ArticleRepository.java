package org.agreen.repository;

import org.agreen.domain.Article;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends MongoRepository<Article, String>{

	Article findByPid(String pid);

	Article findByType(String type);
}
