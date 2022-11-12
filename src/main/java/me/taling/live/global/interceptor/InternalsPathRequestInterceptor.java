package me.taling.live.global.interceptor;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Component
public class InternalsPathRequestInterceptor implements HandlerInterceptor {
	private final Logger log = LoggerFactory.getLogger(InternalsPathRequestInterceptor.class);

	// TODO: refactoring
	//@Autowired
	// private ServicesConfigProperties servicesConfigProperties;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		log.debug("[interceptor] internal path.");
		log.debug("request:{}", request);
		Map<String, String> headers = Collections.list(request.getHeaderNames())
				.stream()
				.collect(Collectors.toMap(header -> header, request::getHeader));

		// TODO: app-key, secret-key 적용.!!
		if (headers.containsKey("service")) {
			String serviceName = headers.get("service");
//			KeyMap keyMap = servicesConfigProperties.getReference().get(serviceName);
//			log.debug("serviceName:{}", serviceName);
//			log.debug("appKey:{}", keyMap.getAppKey());
//			log.debug("secretKey:{}", keyMap.getSecretKey());
		}

		log.debug("header:{}", request.getHeaderNames());
		return true;
	}
}