package com.example.services;

import com.example.models.Member;
import com.example.models.Gender;
import com.example.repositories.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MemberServiceTest {

    private MemberService memberService;
    private MemberRepository memberRepositoryMock;

    @BeforeEach
    void setUp() {
        memberRepositoryMock = mock(MemberRepository.class);
        memberService = new MemberService(memberRepositoryMock);
    }

    @Test
    void testAddMember_Success() {
        Member member = new Member(1L, "MBR123", "Dupont", "Jean", LocalDate.of(1990, 5, 20), Gender.MONSIEUR,"lala@lala.fr");
        when(memberRepositoryMock.save(member)).thenReturn(member);

        Member savedMember = memberService.addMember(member);

        verify(memberRepositoryMock).save(member);
        assertNotNull(savedMember);
        assertEquals("Dupont", savedMember.getLastName());
    }

    @Test
    void testAddMember_NullMember() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberService.addMember(null);
        });

        assertEquals("Member cannot be null", exception.getMessage());
        verify(memberRepositoryMock, never()).save(any(Member.class));
    }

    @Test
    void testUpdateMember_Success() {
        Member member = new Member(1L, "MBR123", "Dupont", "Jean", LocalDate.of(1990, 5, 20), Gender.MONSIEUR, "lala@lala.fr");
        when(memberRepositoryMock.update(member)).thenReturn(member);

        Member updatedMember = memberService.updateMember(member);

        verify(memberRepositoryMock).update(member);
        assertNotNull(updatedMember);
    }

    @Test
    void testUpdateMember_NullMember() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberService.updateMember(null);
        });

        assertEquals("Member or ID cannot be null", exception.getMessage());
        verify(memberRepositoryMock, never()).update(any(Member.class));
    }

    @Test
    void testUpdateMember_NullId() {
        Member member = new Member(null, "MBR123", "Dupont", "Jean", LocalDate.of(1990, 5, 20), Gender.MONSIEUR, "lala@lala.fr");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberService.updateMember(member);
        });

        assertEquals("Member or ID cannot be null", exception.getMessage());
        verify(memberRepositoryMock, never()).update(any(Member.class));
    }

    @Test
    void testDeleteMember_Success() {
        Member member = new Member(1L, "MBR123", "Dupont", "Jean", LocalDate.of(1990, 5, 20), Gender.MONSIEUR, "lala@lala.fr");

        memberService.deleteMember(member);

        verify(memberRepositoryMock).delete(member);
    }

    @Test
    void testDeleteMember_NullMember_ThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberService.deleteMember(null);
        });

        assertEquals("Member or ID cannot be null", exception.getMessage());
        verify(memberRepositoryMock, never()).delete(any(Member.class));
    }

    @Test
    void testDeleteMember_NullId_ThrowsException() {
        Member member = new Member(null, "MBR123", "Dupont", "Jean", LocalDate.of(1990, 5, 20), Gender.MONSIEUR, "lala@lala.fr");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberService.deleteMember(member);
        });

        assertEquals("Member or ID cannot be null", exception.getMessage());
        verify(memberRepositoryMock, never()).delete(any(Member.class));
    }

    @Test
    void testGetAllMembers_ReturnsList() {
        Member member1 = new Member(1L, "MBR123", "Dupont", "Jean", LocalDate.of(1990, 5, 20), Gender.MONSIEUR, "lala@lala.fr");
        Member member2 = new Member(2L, "MBR456", "Martin", "Sophie", LocalDate.of(1985, 3, 10), Gender.MADAME, "lala@lala.fr");
        List<Member> members = Arrays.asList(member1, member2);

        when(memberRepositoryMock.getAllMember()).thenReturn(members);

        List<Member> retrievedMembers = memberService.getAllMembers();

        verify(memberRepositoryMock).getAllMember();
        assertNotNull(retrievedMembers);
        assertEquals(2, retrievedMembers.size());
    }
}
