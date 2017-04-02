package org.agreen.repository;

import org.agreen.domain.Member;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Member entity.
 */
@SuppressWarnings("unused")
public interface MemberRepository extends MongoRepository<Member,String> {

}
