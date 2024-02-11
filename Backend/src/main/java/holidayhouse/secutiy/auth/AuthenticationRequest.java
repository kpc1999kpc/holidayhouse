package holidayhouse.secutiy.auth;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String login;
    String password;
}
