package oms.pc_protector.jwt;

public class JwtProperties {

    // SECRET : JWT Token을 hash 할 때 사용할 secret key 입니다.
    public static final String SECRET = "pcprotector";

    // EXPIRATION_TIME : JWT Token의 validation 기간입니다. (30분)
    public static final int ACCESS_TIME = 100000;
    // REFRESH_TIME : JWT Token의 validation 기간입니다. (10일)
    public static final int REFRESH_TIME = 864000000;

    // TOKEN_PREFIX : JWT Token의 prefix는 Bearer 입니다.
    public static final String TOKEN_PREFIX = "Bearer ";

    //  HEADER_STRING : JWT Token은 Authorization header로 전달됩니다.
    public static final String HEADER_STRING = "Authorization";
}