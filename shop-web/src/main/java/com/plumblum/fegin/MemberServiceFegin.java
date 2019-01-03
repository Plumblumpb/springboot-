package com.plumblum.fegin;


import org.springframework.stereotype.Component;
import service.MemberService;

@Component
//@FeignClient("member")
public interface MemberServiceFegin extends MemberService {

}
