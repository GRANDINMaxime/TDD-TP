package com.example.repositories;

import java.util.List;

import com.example.models.*;

public interface MemberRepository {
    Member save(Member member);

    Member delete(Member member);

    List<Member> getAllMember();

    Member update(Member member);

    Member findById(String memberId);
}
