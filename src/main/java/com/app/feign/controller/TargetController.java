package com.app.feign.controller;

import com.app.feign.common.dto.BaseResponseInfo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/target_server")
public class TargetController {
    @GetMapping("/get")
    public BaseResponseInfo demoGet(@RequestHeader("CustomHeaderName") String header,
                                    @RequestParam("name") String name,
                                    @RequestParam("age") Long age) {
        return BaseResponseInfo.builder()
                .header(header)
                .name(name + " From Target Server")
                .age(age)
                .build();
    }
}
