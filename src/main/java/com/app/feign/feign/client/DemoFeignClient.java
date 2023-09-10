package com.app.feign.feign.client;

import com.app.feign.common.dto.BaseRequestInfo;
import com.app.feign.common.dto.BaseResponseInfo;
import com.app.feign.feign.config.DemoFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * name: demoFeignClient를 나타내는 PK같은 값 정의
 * url: 클라이언트가 요청을 보내고자 하는 타겟의 URL
 */
@FeignClient(
        name = "demo-client"
        , url = "${feign.url.prefix}"
        , configuration = DemoFeignConfig.class
)
public interface DemoFeignClient {
    @GetMapping("/get")
    ResponseEntity<BaseResponseInfo> callGet(@RequestHeader("CustomHeaderName") String customHeader,
                                             @RequestParam("name") String name,
                                             @RequestParam("age") Long age);

    @PostMapping("/post")
    ResponseEntity<BaseResponseInfo> callPost(@RequestHeader("CustomHeaderName") String customHeader,
                                              @RequestBody BaseRequestInfo baseRequestInfo);

    @GetMapping("/error")
    ResponseEntity<BaseResponseInfo> callErrorDecoder();
}
