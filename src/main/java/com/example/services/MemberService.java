package com.example.services;

import com.example.models.Member;
import com.example.repositories.MemberRepository;
import java.util.List;

public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member addMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }

        return memberRepository.save(member);
    }

    public Member updateMember(Member member) {
        if (member == null || member.getId() == null) {
            throw new IllegalArgumentException("Member or ID cannot be null");
        }

        return memberRepository.update(member);
    }

    public void deleteMember(Member member) {
        if (member == null || member.getId() == null) {
            throw new IllegalArgumentException("Member or ID cannot be null");
        }

        memberRepository.delete(member);
    }

    public List<Member> getAllMembers() {
        return memberRepository.getAllMember();
    }
}
