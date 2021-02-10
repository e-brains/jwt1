package com.kye.jwt.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String username;
	private String password;
	private String roles; //USER, ADMIN
	
	//모델에 roles을 하나 더 만들어도 되고 또는 아래처럼 리스트로 만들어도 됨
	//하나의 유저에 1개 이상의 복수의 롤이 있으면 만들고 그렇지 않고 하나만 있으면 만들필요 없음
	public List<String> getRoleList(){ //0번째에 USER 1번째에 ADMIN
		if (this.roles.length() > 0 ) {
			return Arrays.asList(this.roles.split(","));
		}
		return new ArrayList<>(); // 우선 null만 안뜨게 리턴
	}
}
