package com.fivedragons.jpa.practice.repository;

import com.fivedragons.jpa.practice.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByName(String name);

}
