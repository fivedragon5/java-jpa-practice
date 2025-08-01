package com.fivedragons.jpa.practice.api;

import com.fivedragons.jpa.practice.domain.Address;
import com.fivedragons.jpa.practice.domain.Member;
import com.fivedragons.jpa.practice.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result membersV2() {
        List<Member> members = memberService.findMembers();
        List<MemberDto> memberDtos = members.stream()
                .map(member -> new MemberDto(member.getId(), member.getName(), member.getAddress()))
                .collect(Collectors.toList());
        return new Result<>(memberDtos.size(), memberDtos);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private Long id;
        private String name;
        private Address address;
    }

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Long id = memberService.join(request.toMember());
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable Long id,
            @RequestBody @Valid UpdateMemberRequest request
    ) {
        memberService.update(id, request.getName());
        Member updatedMember = memberService.findOne(id);
        return new UpdateMemberResponse(updatedMember.getId(), updatedMember.getName());
    }

    @Data
    public static class CreateMemberRequest {
        @NotEmpty
        private final String name;
        private final String city;
        private final String street;
        private final String zipcode;

        public Member toMember() {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }
    }

    @Data
    public static class UpdateMemberRequest {
        @NotEmpty
        private String name;
        private String city;
        private String street;
        private String zipcode;

        public Member toMember() {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }
    }

    @Data
    @AllArgsConstructor
    public static class CreateMemberResponse {
        private Long id;
    }

    @Data
    @AllArgsConstructor
    public static class UpdateMemberResponse {
        private Long id;
        private String name;
    }
}
