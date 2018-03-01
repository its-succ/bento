package jp.co.esm.bento.web.security;

import static java.util.Collections.singletonList;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import com.google.appengine.repackaged.com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.appengine.repackaged.com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.appengine.repackaged.com.google.api.client.http.javanet.NetHttpTransport;
import com.google.appengine.repackaged.com.google.api.client.json.jackson2.JacksonFactory;
import jp.co.esm.bento.web.model.GoogleUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * Google Sign-Inによる認証を行う{@link AuthenticationProvider}
 *
 * https://developers.google.com/identity/sign-in/web/backend-auth
 */
@Component
public class GoogleIdAuthenticationProvider implements AuthenticationProvider {

  private final String clientId;

  public GoogleIdAuthenticationProvider(@Value("${google.auth.client-id}") String clientId) {
    this.clientId = clientId;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String tokenString = (String) authentication.getCredentials();
    GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
      .setAudience(singletonList(clientId))
      .build();
    GoogleIdToken idToken;
    try {
      idToken = verifier.verify(tokenString);
      if (idToken == null) {
        throw new BadCredentialsException("Failed to verify token");
      }
    } catch (GeneralSecurityException|IOException e) {
      throw new AuthenticationServiceException("Failed to verify token", e);
    }

    GoogleIdToken.Payload payload = idToken.getPayload();
    String userId = payload.getSubject();
    String name = (String) payload.get("name");
    GoogleUser user = new GoogleUser(userId, name);
    List<GrantedAuthority> authorities = singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    return new UsernamePasswordAuthenticationToken(user, tokenString, authorities);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.equals(authentication);
  }
}
