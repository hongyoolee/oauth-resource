package com.yoolee.api.vo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.util.StringUtils;

@Slf4j
@Data
public class JWTKey {

    String alg = "";
    String value = "";

    public String getValue() {
        if (!StringUtils.isEmpty(value)){
            return value;
        }
        return "";
    }
}
