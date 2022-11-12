package me.taling.live.infra.aws.cloudfront;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CloudFrontManager {
    private static final Logger log = LoggerFactory.getLogger(CloudFrontManager.class);

//    private String distributionDomain = "stream.dev.taling.me";
//    private String keyPairId = "KG4OUMCS5B02S";
//    private String resourcePath = "ivs/v1/639374893179/3VhLasQVYMd1/2021/11/9/10/25/VrZ0EnXMeHEW/media/hls/*";
//
//    private File privateKeyFile;
//
//    @PostConstruct
//    public void init() {
//        try {
//            privateKeyFile = ResourceUtils.getFile("classpath:pem/privateKey.pem");
//        } catch (FileNotFoundException e) {
//            log.error("An error occurred during [CloudManager.class] initialization. cause: [privateKey.pem] file not found.");
//            throw new LiveException(UNKNOWN_ERROR_MESSAGE);
//        }
//    }
//
//    public List<Cookie> signedCookie() {
//        try {
//            Date expiresOn = Date.from(LocalDateTime.now().plusSeconds(600).atZone(ZoneId.systemDefault()).toInstant());
//            log.debug("expiresOn:{}", expiresOn);
//            String resourceOrPath = SignerUtils.generateResourcePath(SignerUtils.Protocol.https, distributionDomain, resourcePath);
//            log.debug("resourceOrPath:{}", resourceOrPath);
//
//            CloudFrontCookieSigner.CookiesForCannedPolicy cookiesForCannedPolicy = CloudFrontCookieSigner.getCookiesForCannedPolicy(resourceOrPath, keyPairId, privateKeyFile, expiresOn);
//            List<Cookie> cookies = new ArrayList<>();
//            cookies.add(createCookie(cookiesForCannedPolicy.getExpires().getKey(), cookiesForCannedPolicy.getExpires().getValue()));
//            cookies.add(createCookie(cookiesForCannedPolicy.getSignature().getKey(), cookiesForCannedPolicy.getSignature().getValue()));
//            cookies.add(createCookie(cookiesForCannedPolicy.getKeyPairId().getKey(), cookiesForCannedPolicy.getKeyPairId().getValue()));
//            return cookies;
//
//        } catch (IOException | InvalidKeySpecException e) {
//            log.error("error:{}", e);
//            throw new LiveException(UNKNOWN_ERROR_MESSAGE);
//        }
//    }
//
//    private Cookie createCookie(String key, String value) {
//
//        Cookie cookie = new Cookie(key, value);
//        cookie.setDomain(distributionDomain);
//        cookie.setSecure(true);
//        cookie.setHttpOnly(true);
//        cookie.setPath("/");
//
//
//        return cookie;
//    }
}
