package com.alpha.RideAxis.Config;

import com.alpha.RideAxis.Repository.UserrRepository;
import com.alpha.RideAxis.Entites.Userr;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation implements UserDetailsService {

    private final UserrRepository ur;

    public UserServiceImplementation(UserrRepository ur) {
        this.ur = ur;
    }

    @Override
    public UserDetails loadUserByUsername(String mobno) {

        long mobile;
        try {
            mobile = Long.parseLong(mobno);
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("Invalid mobile number");
        }

        Userr user = ur.findByMobileno(mobile)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with mobile: " + mobno));

        return User.builder()
                .username(String.valueOf(user.getMobileno()))
                .password(user.getPassword()) // already encoded
                .roles(user.getRole())        // CUSTOMER / DRIVER / ADMIN
                .build();
    }
}
