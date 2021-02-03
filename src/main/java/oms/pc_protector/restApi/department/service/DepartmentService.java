package oms.pc_protector.restApi.department.service;

import oms.pc_protector.restApi.department.mapper.DepartmentMapper;
import oms.pc_protector.restApi.department.model.DepartmentVO;
import oms.pc_protector.restApi.department.model.UpdateDepartmentVO;
import oms.pc_protector.restApi.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class DepartmentService {

    private final DepartmentMapper departmentMapper;
    private final UserMapper userMapper;

    public DepartmentService(DepartmentMapper departmentMapper, UserMapper userMapper) {
        this.departmentMapper = departmentMapper;
        this.userMapper = userMapper;
    }

    @Transactional
    public boolean findUserInDepartment(String department) {
        return departmentMapper.findUserInDepartment(department) > 0;
    }

    @Transactional
    public List<DepartmentVO> findAll(String User_Idx) {
        return Optional.ofNullable(departmentMapper.selectAll(User_Idx))
                .orElseThrow(() -> new RuntimeException("값이 존재하지 않습니다."));
    }


    @Transactional
    public DepartmentVO findByDepartment(String department) {
        return Optional.ofNullable(departmentMapper.selectByDepartment(department))
                .orElseThrow(() -> new RuntimeException("값이 존재하지 않습니다."));
    }


    @Transactional
    public DepartmentVO findByDepartmentIdx(Long departmentIdx) {
        return Optional.ofNullable(departmentMapper.selectBycode(departmentIdx))
                .orElseThrow(() -> new RuntimeException("값이 존재하지 않습니다."));
    }


    @Transactional
    public List<DepartmentVO> findChildAscByParentCode(Long parentCode) {
        return Optional.ofNullable(departmentMapper.selectChildCodeAscByParentCode(parentCode))
                .orElseThrow(() -> new RuntimeException("값이 존재하지 않습니다."));
    }


    @Transactional
    public List<DepartmentVO> findChildDescByParentCode(Long parentCode) {
        return Optional.ofNullable(departmentMapper.selectChildCodeDescByParentCode(parentCode))
                .orElseThrow(() -> new RuntimeException("값이 존재하지 않습니다."));
    }


    @Transactional
    public void registerByExcel(List<DepartmentVO> departmentVO, String User_Idx) {
        departmentMapper.deleteAll(User_Idx);
        for (DepartmentVO dept : departmentVO) {
            departmentMapper.registerByExcel(dept);
        }
    }


    @Transactional
    public String register(DepartmentVO departmentVO) {
        String code = null;
        do {
            code = CreateRandomDptCode();
        } while (departmentMapper.findDepartmentCode(code) == 0);
        departmentVO.setDptCode(code);
        if(departmentVO.getCode() / 100 < 10) {
            departmentMapper.insertRoot(departmentVO);
        } else {
            departmentMapper.insert(departmentVO);
        }
        return code;
    }


    @Transactional
    public void update(UpdateDepartmentVO updateDepartmentVO) {
        departmentMapper.update(updateDepartmentVO);
        userMapper.departmentModified(updateDepartmentVO);
    }

    private String CreateRandomDptCode() {
        StringBuffer temp = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < 6; i++) {
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
                case 0:
                    temp.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    temp.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    temp.append((rnd.nextInt(10)));
                    break;
            }
        }
        return temp.toString();
    }
}
