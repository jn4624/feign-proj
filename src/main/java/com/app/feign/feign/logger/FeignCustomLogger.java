package com.app.feign.feign.logger;

import feign.*;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import static feign.Util.*;

/**
 * Bean으로 등록해줘야 하는데 config의 위치가 중요
 * Global한 설정이기 때문에 공통 config에 등록
 */
@RequiredArgsConstructor
public class FeignCustomLogger extends Logger {
    private static final int DEFAULT_SLOW_API_TIME = 3_000;
    private static final String SLOW_API_NOTICE = "Slow API";

    /**
     * log 어떤 형식으로 남길지 포맷 설정
     */
    @Override
    protected void log(String configKey, String format, Object... args) {
        System.out.println(String.format(methodTag(configKey) + format, args));
    }

    /**
     * Request만 handling 가능
     */
    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        System.out.println("[logRequest] : " + request);
    }

    /**
     * Request와 Response 모두 handling 가능
     * Request, Response 같이 handling 해도 되고 별도로 handling 해도 된다
     */
    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        String protocolVersion = resolveProtocolVersion(response.protocolVersion());
        String reason =
                response.reason() != null && logLevel.compareTo(Level.NONE) > 0 ? " " + response.reason()
                        : "";
        int status = response.status();
        log(configKey, "<--- %s %s%s (%sms)", protocolVersion, status, reason, elapsedTime);
        if (logLevel.ordinal() >= Level.HEADERS.ordinal()) {

            for (String field : response.headers().keySet()) {
                if (shouldLogResponseHeader(field)) {
                    for (String value : valuesOrEmpty(response.headers(), field)) {
                        log(configKey, "%s: %s", field, value);
                    }
                }
            }

            int bodyLength = 0;
            if (response.body() != null && !(status == 204 || status == 205)) {
                // HTTP 204 No Content "...response MUST NOT include a message-body"
                // HTTP 205 Reset Content "...response MUST NOT include an entity"
                if (logLevel.ordinal() >= Level.FULL.ordinal()) {
                    log(configKey, ""); // CRLF
                }
                byte[] bodyData = Util.toByteArray(response.body().asInputStream());
                bodyLength = bodyData.length;
                if (logLevel.ordinal() >= Level.FULL.ordinal() && bodyLength > 0) {
                    log(configKey, "%s", decodeOrDefault(bodyData, UTF_8, "Binary data"));
                }

                /**
                 * 설정한 기본 처리 시간보다 오버되는 호출이 있을시
                 * 슬로우 로거 표출될 수 있도록 설정
                 */
                if (elapsedTime > DEFAULT_SLOW_API_TIME) {
                    log(configKey, "[%s] elapsedTime : %s", SLOW_API_NOTICE, elapsedTime);
                }

                log(configKey, "<--- END HTTP (%s-byte body)", bodyLength);
                return response.toBuilder().body(bodyData).build();
            } else {
                log(configKey, "<--- END HTTP (%s-byte body)", bodyLength);
            }
        }
        return response;
    }
}
