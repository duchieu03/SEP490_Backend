package com.sep490_backend.authentication.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sep490_backend.authentication.core.enums.AuthenticationErrorCode;
import com.sep490_backend.authentication.core.exception.ApiException;
import com.sep490_backend.authentication.entity.tenant.User;
import com.sep490_backend.authentication.repository.tenant.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {
    private final UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    private long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    private long REFRESHABLE_DURATION;

    public String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("HieuNDHE")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try{
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes(StandardCharsets.UTF_8)));
        } catch (JOSEException e){
            log.error(e.getMessage());
            throw new RuntimeException();
        }
        return jwsObject.serialize();
    }

    public void verifyToken(String token, boolean isRefresh){
        try {
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes(StandardCharsets.UTF_8));

            SignedJWT signedJWT = SignedJWT.parse(token);

            Date expiryTime = (isRefresh)
                    ? new Date(signedJWT
                    .getJWTClaimsSet()
                    .getIssueTime()
                    .toInstant()
                    .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                    .toEpochMilli())
                    : signedJWT.getJWTClaimsSet().getExpirationTime();

            boolean verified = signedJWT.verify(verifier);
            if (!(verified)) throw new ApiException(AuthenticationErrorCode.UNAUTHENTICATED);
        } catch (JOSEException | ParseException e){
            throw new ApiException(AuthenticationErrorCode.UNAUTHENTICATED);
        }

    }

//    public ApiRes<String> refreshToken(RefreshRequest request)   {
//        try {
//            SignedJWT signedJWT = verifyToken(request.getToken(), true);
//            String username = signedJWT.getJWTClaimsSet().getSubject();
//
//            User user =
//                    userRepository.findByUsername(username).orElseThrow(() -> new ApiException(ErrorCode.UNAUTHENTICATED));
//
//            return ApiRes.success(generateToken(user));
//        } catch (ParseException e){
//            throw new ApiException(ErrorCode.UNAUTHENTICATED);
//        }
//    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });

        return stringJoiner.toString();
    }
}
