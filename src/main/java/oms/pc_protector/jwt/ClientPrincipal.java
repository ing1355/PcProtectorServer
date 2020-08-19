package oms.pc_protector.jwt;

import lombok.extern.slf4j.Slf4j;
import oms.pc_protector.restApi.client.model.ClientVO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@CrossOrigin
@Slf4j
public class ClientPrincipal implements UserDetails {
    private ClientVO clientVO;

    public ClientPrincipal(ClientVO clientVO) {
        this.clientVO = clientVO;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        GrantedAuthority authority = new SimpleGrantedAuthority(
                "ROLE_" + this.clientVO.getRoles());
        authorities.add(authority);

        return authorities;
    }

    public ClientVO getClientVO() {
        return clientVO;
    }

    @Override
    public String getPassword() {
        log.info("getClientPassword : " + this.clientVO.getIpAddress());
        return this.clientVO.getMacAddress();
    }

    @Override
    public String getUsername() {
        return this.clientVO.getUserId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
