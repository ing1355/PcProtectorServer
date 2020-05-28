package oms.pc_protector.restApi.manager.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Getter
@Setter
public class ManagerVO {

    private String id;
    private String password;
    private String name;
    private String mobile;
    private String email;
    private boolean locked;

    private int active;
    private String roles = "";
    private String permissions = "";

    //@JsonIgnore
    public List<String> getRoleList(){
        if(this.roles.length() > 0){
            return Arrays.asList(this.roles.split(","));
        }

        return new ArrayList<>();
    }

    //@JsonIgnore
    public List<String> getPermissionList(){
        if(this.permissions.length() > 0){
            return Arrays.asList(this.permissions.split(","));
        }

        return new ArrayList<>();
    }
}


