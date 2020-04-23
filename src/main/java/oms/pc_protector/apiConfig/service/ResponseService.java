package oms.pc_protector.apiConfig.service;


import oms.pc_protector.apiConfig.model.CommonResult;
import oms.pc_protector.apiConfig.model.ListResult;
import oms.pc_protector.apiConfig.model.SingleResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

    // enum으로 api 요청 결과에 대한 code, message를 정의합니다.
    public enum CommonResponse {
        SUCCESS(200, "성공하였습니다."),
        FAIL(-1, "실패하였습니다.");

        int status;
        String msg;

        CommonResponse(int status, String msg){
            this.status = status;
            this.msg = msg;
        }

        public int getStatus() {
            return status;
        }

        public String getMsg() {
            return msg;
        }
    }

    // 단일 결과를 처리하는 메소드
    public <T> SingleResult<T> getSingleResult(T data) {
        SingleResult<T> result = new SingleResult<>();
        result.setRows(data);
        setSuccessResult(result);
        return result;
    }

    // 복수 결과를 처리하는 메소드
    public <T> ListResult<T> getListResult(List<T> list) {
        ListResult<T> result = new ListResult<>();
        result.setRows(list);
        setSuccessResult(result);
        return result;
    }

    // 결과 모델에 api 요청 성공 데이터를 세팅해주는 메소드
    private void setSuccessResult(CommonResult result) {
        result.setSuccess(true);
        result.setStatus(CommonResponse.SUCCESS.getStatus());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    // 실패 결과만 처리하는 메소드
    public CommonResult getFailResult() {
        CommonResult result = new CommonResult();
        setSuccessResult(result);
        result.setStatus(CommonResponse.FAIL.getStatus());
        result.setMsg(CommonResponse.FAIL.getMsg());
        return result;
    }

}
