package com.nibado.example.spring_us_versioning.versioning;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import static java.lang.Integer.parseInt;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class AdapterAdvice implements ResponseBodyAdvice<Versioned> {
    private static final String PROTOCOL_VERSION_HEADER = "X-Protocol-Version";

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {

        log.info ("Supports? {}", aClass);

        for(Class<?> interf : ((Class<?>)methodParameter.getGenericParameterType()).getInterfaces()) {
            if(interf.equals(Versioned.class)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Versioned beforeBodyWrite(
            Versioned versioned,
            MethodParameter methodParameter,
            MediaType mediaType,
            Class<? extends HttpMessageConverter<?>> aClass,
            ServerHttpRequest serverHttpRequest,
            ServerHttpResponse serverHttpResponse) {

        log.info ("beforeBodyWrite");

        String version = serverHttpRequest.getHeaders().getFirst(PROTOCOL_VERSION_HEADER);

        if(version == null) {
            throw new RuntimeException(String.format("Missing '%s' header.", PROTOCOL_VERSION_HEADER));
        }

        return versioned.toVersion(parseInt(version));
    }
}
