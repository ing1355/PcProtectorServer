package oms.pc_protector.restApi.login.service;

        import lombok.extern.slf4j.Slf4j;
        import oms.pc_protector.restApi.client.model.ClientVO;
        import oms.pc_protector.restApi.login.mapper.LoginMapper;
        import oms.pc_protector.restApi.login.model.ClientLoginVO;
        import oms.pc_protector.restApi.login.model.LoginVO;
        import oms.pc_protector.restApi.manager.mapper.ManagerMapper;
        import oms.pc_protector.restApi.manager.service.ManagerService;
        import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
        import org.springframework.security.crypto.password.PasswordEncoder;
        import org.springframework.stereotype.Service;
        import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class LoginService {

    private ManagerService managerService;

    private LoginMapper loginMapper;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginService(ManagerService managerService, LoginMapper loginMapper) {
        this.managerService = managerService;
        this.loginMapper = loginMapper;

    }

    public boolean login(LoginVO login) {

        boolean isExistId = isExistId(login.getId());
        boolean isEqualsPw =
                passwordEncoder.matches(login.getPassword(),
                        findPasswordById(login.getId()));
        log.info("비밀번호 왼 : {}", login.getPassword());
        log.info("비밀번호 우 : {}", findPasswordById(login.getId()));
        log.info("비밀번호 비교 : {}", isEqualsPw);

        if (!isExistId) {
            throw new RuntimeException("아이디를 확인해주세요.");
        }

        if (!isEqualsPw) {
            throw new RuntimeException("패스워드를 확인해주세요.");
        }
        return true;
    }

    public boolean loginForClientFirst(ClientVO clientVO) {
        return loginMapper.loginForClientFirst(clientVO) > 0;
    }

    @Transactional(readOnly = true)
    public ClientVO findClient(ClientLoginVO loginVO) {
        return loginMapper.findClient(loginVO);
    }

    @Transactional(readOnly = true)
    public String findPasswordById(String id) {
        return loginMapper.findPasswordById(id);
    }


    @Transactional(readOnly = true)
    public boolean isExistId(String id) {
        int resultCount = loginMapper.isExistId(id);
        return resultCount > 0;
    }

}
