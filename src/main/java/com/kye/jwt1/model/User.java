package com.kye.jwt1.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // mySql에서 Auto 인크리먼트가 된다.
    private long id;
    private String username;
    private String password;

    // role은 테이블을 만들어서 별도 관리가 필요하면 role 객체를 만들어서 사용해도 된다.
    // 여기서는 간단하게 아래 처럼 활용하기로 한다.
    private String roles;  // USER.ADMIN

    // 외부에서 getRoleList()를 호출하면 첫번째 리스트에는 USER 두전째 리스트에는 ADMIN이 들어간다.
    public List<String> getRoleList(){
        if (this.roles.length() > 0 ) {
            return Arrays.asList(this.roles.split(","));  // 짤라서 ','로 구분해서 리턴한다.
        }

        return new ArrayList<>();  // null이 안뜨게만 해주기 위해 빈 리스트 리턴
    }
}
