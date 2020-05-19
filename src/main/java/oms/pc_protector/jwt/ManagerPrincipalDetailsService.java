package oms.pc_protector.jwt;

import oms.pc_protector.restApi.manager.model.ManagerVO;
import oms.pc_protector.restApi.manager.service.ManagerService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@Service
public class ManagerPrincipalDetailsService implements UserDetailsService {

    private ManagerService managerService;

    public ManagerPrincipalDetailsService(ManagerService managerService) {
        this. managerService = managerService;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        ManagerVO manager = this.managerService.findById(userId);
        ManagerPrincipal managerPrincipal = new ManagerPrincipal(manager);
        return managerPrincipal;
    }
}
