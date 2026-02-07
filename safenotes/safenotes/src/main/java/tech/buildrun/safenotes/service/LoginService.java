package tech.buildrun.safenotes.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.buildrun.safenotes.config.JwtConfig;
import tech.buildrun.safenotes.controller.dto.LoginResponse;
import tech.buildrun.safenotes.entity.User;
import tech.buildrun.safenotes.repository.UserRepository;

import java.util.UUID;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final AccessTokenService accessTokenService;
    private final JwtConfig jwtConfig;

    public LoginService(UserRepository userRepository,
                        BCryptPasswordEncoder bCryptPasswordEncoder,
                        RefreshTokenService refreshTokenService,
                        AccessTokenService accessTokenService,
                        JwtConfig jwtConfig) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.refreshTokenService = refreshTokenService;
        this.accessTokenService = accessTokenService;
        this.jwtConfig = jwtConfig;
    }

    @Transactional
    public LoginResponse login(String username, String password) {

        // 1. validar o usuario
        var user = validateUser(username, password);

        // 2. gerar a familia do token
        var familyId = UUID.randomUUID();

        // 3. emitir o refresh token
        var opaqueToken = refreshTokenService.generateRefreshToken(user, familyId);

        // 4. emitir o access token
        var accessToken = accessTokenService.generateAccessToken(user, familyId);

        return new LoginResponse(
                accessToken.getTokenValue(),
                jwtConfig.getExpiresIn(),
                opaqueToken,
                jwtConfig.getRefreshExpiresIn(),
                accessToken.getClaimAsStringList("scope")
        );
    }

    private User validateUser(String username, String password) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(RuntimeException::new);

        var isPasswordValid = bCryptPasswordEncoder.matches(password, user.getPassword());
        if (!isPasswordValid) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
}
