package com.app.feign.service;

import com.app.feign.common.dto.BaseRequestInfo;
import com.app.feign.common.dto.BaseResponseInfo;
import com.app.feign.feign.client.DemoFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DemoService {
    private final DemoFeignClient demoFeignClient;

    public String get() {
        ResponseEntity<BaseResponseInfo> response = demoFeignClient.callGet("CustomHeader", "CustomName", 1L);

        System.out.println("Name : " + response.getBody().getName());
        System.out.println("Age : " + response.getBody().getAge());
        System.out.println("Header : " + response.getBody().getHeader());

        return "get";
    }

    public String post() {
        BaseRequestInfo baseRequestInfo = BaseRequestInfo.builder()
                                            .name("CustomName")
                                            .age(1L)
                                            .build();

        ResponseEntity<BaseResponseInfo> response = demoFeignClient.callPost("CustomHeader", baseRequestInfo);

        System.out.println("Name : " + response.getBody().getName());
        System.out.println("Age : " + response.getBody().getAge());
        System.out.println("Header : " + response.getBody().getHeader());

        return "post";
    }
}
