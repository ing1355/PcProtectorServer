package oms.pc_protector.jwt;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.manager.model.ManagerVO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@CrossOrigin
@Log4j2
public class ManagerPrincipal implements UserDetails {

    private ManagerVO managerVO;

    public ManagerPrincipal(ManagerVO managerVO) {
        this.managerVO = managerVO;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Extract list of permissions (name)
        this.managerVO.getPermissionList().forEach(p -> {
            GrantedAuthority authority = new SimpleGrantedAuthority(p);
            authorities.add(authority);
        });

        // Extract list of roles (ROLE_name)
        this.managerVO.getRoleList().forEach(p -> {
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + p);
            authorities.add(authority);
        });
        return authorities;
    }

    public ManagerVO getManagerVO() {
        return managerVO;
    }

    @Override
    public String getPassword() {
        log.info("getPassword : " + managerVO.getPassword());
        return this.managerVO.getPassword();
    }

    @Override
    public String getUsername() {   // 여기선 name이 id
        return this.managerVO.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.managerVO.getActive() == 1;
    }
}
