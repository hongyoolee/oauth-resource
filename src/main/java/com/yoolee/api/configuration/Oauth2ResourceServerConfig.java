package com.yoolee.api.configuration;

import com.google.gson.Gson;
import com.yoolee.api.vo.JWTKey;
import io.micrometer.core.instrument.util.IOUtils;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
@Configuration
public class Oauth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    //@Value("${security.oauth2.resource.jwt.key-set-uri}")
    private String publicKeyUri = "http://localhost:8089/oauth/token_key";

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/main")
                .access("#oauth2.hasAnyScope('read')")
                .anyRequest()
                .authenticated();
    }

    @Bean
    public TokenStore tokenStore(){
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        try{
            // 공개키를 직접 파일로 만들어 읽어서 jwt 디코드 키 등록
           /* JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            Resource resource = new ClassPathResource("key.txt");
            converter.setVerifierKey(IOUtils.toString(resource.getInputStream()));
            return converter;*/

            // 직접 oauth 서버를 호출하여 공개키 읽어서 jwt 디코드 키 등록
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            converter.setVerifierKey(getPublicKeyValue(publicKeyUri));
            return converter;
        }catch (Exception e){
            return new JwtAccessTokenConverter();
        }
    }

    private String getPublicKeyValue(String uriKey) {
        JsonNode response = Unirest.get(publicKeyUri)
                .asJson().getBody();
        log.error("key >> {}",response.toString());
        return StringUtils.isEmpty(response.toString()) ? "" : new Gson().fromJson(response.toString(),JWTKey.class).getValue();
    }



}
