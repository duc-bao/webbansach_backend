package vn.ducbao.springboot.webbansach_backend.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import vn.ducbao.springboot.webbansach_backend.dao.RoleRepository;
import vn.ducbao.springboot.webbansach_backend.dao.UserRepository;
import vn.ducbao.springboot.webbansach_backend.entity.Notification;
import vn.ducbao.springboot.webbansach_backend.entity.Role;
import vn.ducbao.springboot.webbansach_backend.entity.User;
import vn.ducbao.springboot.webbansach_backend.service.email.EmailService;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserSeviceImpl implements UserService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    public UserSeviceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public ResponseEntity<?> register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body(new Notification("Username đã tồn tại!"));
        }
        if (userRepository.existsByEmail((user.getEmail()))) {
            return ResponseEntity.badRequest().body(new Notification("Email đã tồn tại!"));
        }
        // Gán và gửi thông tin kích hoạt
        user.setActivationCode(activationCode());
        user.setEnabled(false);
        // Ma hoa mat khau
        String endecodePassword = bCryptPasswordEncoder.encode(user.getPassword());
        System.out.println(user.getUsername());
        user.setPassword(endecodePassword);
        userRepository.save(user);
        sendEmailUser(user.getEmail(), user.getActivationCode());
        return ResponseEntity.ok("Tạo thành công");
    }

    // Tạo mã kích hoạt
    private String activationCode() {
        return UUID.randomUUID().toString();
    }

    // Gửi email cho người dùng
    private void sendEmailUser(String email, String activationCode) {
        String subject = "Kich hoạt tài khoản của bạn tại web bán sách";
        String text = "Vui lòng sử dụng mã sau để kích hoạt cho tài khoản <" + email + ">:<br> <h1>" + activationCode + "</h1>";
        text += "<br>Click Vào đường link để kích hoạt tài khoản:";
        String url = "http:localhost:3000/active-account/"+email+ "/"+activationCode;
        text += ("<br><a href = "+url+"> "+url+" </a>");
        emailService.sendMessage("truongducbao29042002@gmail.com", email, subject, text);
    }
    //Kích hoạt tài khoản người dùng
    public  ResponseEntity<?> activeUser(String email, String activationCode){
        User user = userRepository.findByEmail(email);
        if(user == null){
          return   ResponseEntity.badRequest().body(new Notification("Người dùng không tồn tại"));
        }
        if(user.isEnabled()){
          return   ResponseEntity.badRequest().body(new Notification("Người dùng đã kích hoạt tài khoản"));
        }
        if(activationCode.equals(user.getActivationCode())){
            user.setEnabled(true);
            System.out.println(user.isEnabled());
            userRepository.save(user);
           return ResponseEntity.badRequest().body(new Notification("Kích hoạt tài khoản thành công"));
        }else{
           return ResponseEntity.badRequest().body(new Notification("Mã kích hoạt nhập không chính xác"));
        }

    }
    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUsername(userName);
    }

    // Lấy ra các thông tin của user đó để xét quyền
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Tai khoan khong ton tai!");
        }
        org.springframework.security.core.userdetails.User user1 =
                new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), roleToAuthorities(user.getRoleList()));

        return user1;
    }

    // Lấy ra tên quyền
    private Collection<? extends GrantedAuthority> roleToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role ->
                new SimpleGrantedAuthority(role.getNameRole())
        ).collect(Collectors.toList());
    }
}
