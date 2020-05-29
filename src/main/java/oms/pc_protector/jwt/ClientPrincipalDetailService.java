package oms.pc_protector.jwt;

import oms.pc_protector.restApi.client.model.ClientVO;
import oms.pc_protector.restApi.client.service.ClientService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@Service
public class ClientPrincipalDetailService implements UserDetailsService {
    private ClientService clientService;

    public ClientPrincipalDetailService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        List<ClientVO> client = this.clientService.findById(userId);
        List<ClientVO> clientPrincipal = new ArrayList<>();
        client.forEach(p -> {

        });
        return (UserDetails) clientPrincipal;
    }
}
