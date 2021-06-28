package com.kye.jwt1.service;

import com.kye.jwt1.auth.PrincipalDetails;
import com.kye.jwt1.model.User;
import com.kye.jwt1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// '/login' 요청이 올때 동작해야 하나 SecurityConfig에서 disable시켰기 때문에 동작 안함
// 그래서 PrincipalDetailsService를 호출하는 필터를 하나 만들어야 한다.
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService의 loadUserByUsername() 호출됨");
        User user = userRepository.findByUsername(username);
        return new PrincipalDetails(user);
    }
}
