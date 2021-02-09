package oms.pc_protector.restApi.department.service;

import oms.pc_protector.restApi.department.mapper.DepartmentMapper;
import oms.pc_protector.restApi.department.model.*;
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
    public boolean findUserInDepartment(String departmentIdx) {
        String code = departmentMapper.selectCodeByIdx(departmentIdx);
        if(code.length() == 3) {
            return departmentMapper.findManagerInDepartment(code) > 0;
        } else {
            return departmentMapper.findUserInDepartment(code) > 0;
        }
    }

    @Transactional
    public List<DepartmentVO> findAll(String User_Idx) {
        return Optional.ofNullable(departmentMapper.selectAll(User_Idx))
                .orElseThrow(() -> new RuntimeException("값이 존재하지 않습니다."));
    }

    @Transactional
    public List<DepartmentRootVO> findAllRoot(String User_Idx) {
        List<DepartmentRootVO> departmentRootVO = Optional.ofNullable(departmentMapper.selectAllRoot(User_Idx))
                .orElseThrow(() -> new RuntimeException("값이 존재하지 않습니다."));
        for(DepartmentRootVO departmentRootVO1 : departmentRootVO) {
            departmentRootVO1.setAgentNum(departmentMapper.selectAllClientByIdx(departmentRootVO1.getIdx()));
        }
        return departmentRootVO;
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
    public void changeDepartment(List<DepartmentVO> departmentVO, String UserIdx) {
        List<DepartmentVO> departmentVOList = findAll(UserIdx);
        int count = 0;
        for(DepartmentVO departmentVO1 : departmentVO) {
            DepartmentVO temp = departmentVOList.get(count);
            if(departmentVO1.getCode() != temp.getCode() ||
                    !departmentVO1.getName().equals(temp.getName())) {
                departmentMapper.changeDepartment(departmentVO1);
            }
        }
    }


    @Transactional
    public String registerByExcel(List<DepartmentVO> departmentVO, String rootIdx) {
        String rootDptCode = departmentMapper.selectDptCode(rootIdx);
        departmentMapper.deleteAll(rootIdx);
        Integer count = 0;
        String userIdx = null;
        for (DepartmentVO dept : departmentVO) {
            if(count == 0) {
                dept.setDptCode(rootDptCode);
            } else {
                dept.setDptCode(CreateRandomDptCode());
            }
            departmentMapper.registerByExcel(dept);
            if(count == 0) {
                departmentMapper.updateByExcel(rootIdx, dept.getIdx());
                userIdx = dept.getIdx();
            }
            count++;
        }
        return userIdx;
    }


    @Transactional
    public DepartmentResponseVO register(DepartmentVO departmentVO) {
        String code = null;
        do {
            code = CreateRandomDptCode();
        } while (departmentMapper.findDepartmentCode(code) == 0);
        departmentVO.setDptCode(code);
        DepartmentResponseVO departmentResponseVO = null;
        if(departmentVO.getCode() / 100 < 10) {
            departmentResponseVO = new DepartmentResponseVO(departmentMapper.insertRoot(departmentVO), code);
        } else {
            departmentResponseVO = new DepartmentResponseVO(departmentMapper.insert(departmentVO), code);
        }
        return departmentResponseVO;
    }


    @Transactional
    public void update(UpdateDepartmentVO updateDepartmentVO) {
        departmentMapper.update(updateDepartmentVO);
    }

    @Transactional
    public void delete(DepartmentDeleteVO departmentDeleteVO, String User_Idx) {
        String code = departmentMapper.selectCodeByIdx(departmentDeleteVO.getIdx());
        if(code.length() == 3) {
            departmentMapper.deleteRoot(departmentDeleteVO.getIdx());
        } else {
            departmentMapper.delete(departmentDeleteVO.getIdx());
            changeDepartment(departmentDeleteVO.getDepartmentVOList(), User_Idx);
        }

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
