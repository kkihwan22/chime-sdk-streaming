package me.taling.live.global.configurations;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import me.taling.live.global.interceptor.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.CacheControl;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

@Configuration
@EnableWebMvc
public class WebMvcConfiguration implements WebMvcConfigurer {
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/",
            "classpath:/resources/",
            "classpath:/static/",
            "classpath:/templates/"
    };

    private static final String[] ALLOWED_ORIGINS = {
            "http://localhost:8080",
            "https://live.dev.taling.me",
            "https://live-qa.dev.taling.me",
            "https://live-front.dev.taling.me",
            "https://live-stag-front.taling.me",
            "https://live.taling.me",
            "https://early-bird-front.dev.taling.me",
            "https://live-api.taling.me"
    };

    private static final long MAX_AGE = 3600;


    private static final String[] LIVE_INTERCEPTOR_URL_PATTERN = {
            "/lives/**", "/internals/space"
    };

    private static final String[] INTERNAL_INTERCEPTOR_URL_PATTERN = {
            "/internals/**"
    };

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS)
                .setCacheControl(CacheControl.noCache());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(ALLOWED_ORIGINS)
                .allowedMethods("*")
                .allowCredentials(true)
                .maxAge(MAX_AGE);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(threadLocalContextInterceptor())
                .addPathPatterns("/*")
                .order(0);
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns(LIVE_INTERCEPTOR_URL_PATTERN)
                .order(1);
        registry.addInterceptor(internalAuthenticationInterceptor())
                .addPathPatterns(INTERNAL_INTERCEPTOR_URL_PATTERN)
                .order(2);
        registry.addInterceptor(allowIpInterceptor())
                .addPathPatterns("/", "/index.html")
                .order(3);
        registry.addInterceptor(talingHttpHeaderInterceptor())
                .addPathPatterns("/lives/**/waiting-room", "/lives/**/attendees")
                .order(4);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(this.objectMapper());
        converters.add(converter);
    }

    @Bean
    public ThreadLocalContextInterceptor threadLocalContextInterceptor() {
        return new ThreadLocalContextInterceptor();
    }

    @Bean
    public LivesPathRequestInterceptor authenticationInterceptor() {
        return new LivesPathRequestInterceptor();
    }

    @Bean
    public InternalsPathRequestInterceptor internalAuthenticationInterceptor() {
        return new InternalsPathRequestInterceptor();
    }

    @Bean
    public AllowIpInterceptor allowIpInterceptor() {
        return new AllowIpInterceptor();
    }

    @Bean
    public TalingHttpHeaderInterceptor talingHttpHeaderInterceptor() { return new TalingHttpHeaderInterceptor(); }


    @Primary
    @Bean
    public ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .modules(new JavaTimeModule())
                .serializationInclusion(JsonInclude.Include.ALWAYS)
                .build();
    }
}

