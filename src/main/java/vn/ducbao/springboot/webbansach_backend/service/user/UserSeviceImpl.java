package vn.ducbao.springboot.webbansach_backend.service.user;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.ducbao.springboot.webbansach_backend.dao.RoleRepository;
import vn.ducbao.springboot.webbansach_backend.dao.UserRepository;
import vn.ducbao.springboot.webbansach_backend.entity.Notification;
import vn.ducbao.springboot.webbansach_backend.entity.Role;
import vn.ducbao.springboot.webbansach_backend.entity.User;
import vn.ducbao.springboot.webbansach_backend.security.JwtResponse;
import vn.ducbao.springboot.webbansach_backend.service.email.EmailService;
import vn.ducbao.springboot.webbansach_backend.service.image.ImageService;
import vn.ducbao.springboot.webbansach_backend.service.jwt.JwtService;
import vn.ducbao.springboot.webbansach_backend.service.util.Base64MuiltipartFileConverter;

import java.sql.Date;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Optional;
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
    private ImageService imageService;
    @Autowired
    private JwtService jwtService;
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

    // Forgot-password
    public ResponseEntity<?> forgotPassword(JsonNode jsonNode){
        try {
            User user = userRepository.findByEmail(formatStringByJson(String.valueOf(jsonNode.get("email").toString())));
            if(user == null){
                return ResponseEntity.notFound().build();
            }
            // Doi mat khau cho user
            String passwordTemp = generateTemporaryPassword();
            user.setPassword(bCryptPasswordEncoder.encode(passwordTemp));
            userRepository.save(user);

            // Gui email de nhan mat khau

            sendEmailForgotPassword(user.getEmail(), passwordTemp);

        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }



    private ResponseEntity<?> sendEmailForgotPassword(String email, String passwordTemp) {
        String subject = "Reset mật khẩu";
        String text = "Mật khẩu tạm thời của bạn là: <strong>" + passwordTemp + " </strong>";
        text += "<br/>  <span>Vui lòng đăng nhập và đổi lại mật khẩu của bạn</span>";
        try {
            emailService.sendMessage("truongducbao290402@gmail.com", email, subject,text);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        return  ResponseEntity.ok().build();
    }

    private String generateTemporaryPassword() {
        return RandomStringUtils.random(10, true, true);
    }
    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUsername(userName);
    }

    // Change-Password
    @Override
    public ResponseEntity<?> changePassword(JsonNode jsonNode) {
        try{
            int idUser = Integer.parseInt(formatStringByJson(String.valueOf(jsonNode.get("idUser"))));
            String newPassword = formatStringByJson(String.valueOf(jsonNode.get("newPassword")));

            Optional<User> user = userRepository.findById(idUser);
            user.get().setPassword(bCryptPasswordEncoder.encode(newPassword));
            userRepository.save(user.get());
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
    // Update profile
    @Override
    public ResponseEntity<?> updateProfile(JsonNode jsonNode) {
        try {
            int idUser = Integer.parseInt(formatStringByJson(String.valueOf(jsonNode.get("idUser"))));
            String fisrtName = formatStringByJson(String.valueOf(jsonNode.get("firstName")));
            String lastName = formatStringByJson(String.valueOf(jsonNode.get("lastName")));
            // Fomat ngay sinh
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            Instant instant = Instant.from(formatter.parse(String.valueOf(jsonNode.get("dateOfBirth"))));
            java.sql.Date dateOfBirth = new java.sql.Date(Date.from(instant).getTime());
            String phoneNumber = formatStringByJson(String.valueOf(jsonNode.get("phoneNumber")));
            String deliveryAddress = formatStringByJson(String.valueOf(jsonNode.get("deliveryAddress")));
            String gender = formatStringByJson(String.valueOf(jsonNode.get("gender")));

            Optional<User> user = userRepository.findById(idUser);
            user.get().setFirstName(fisrtName);
            user.get().setLastName(lastName);
            user.get().setDateOfBirth(dateOfBirth);
            user.get().setPhoneNumber(phoneNumber);
            user.get().setGender(gender);
            user.get().setDeliveryAdress(deliveryAddress);

            userRepository.save(user.get());

        }catch (Exception e){
            e.printStackTrace();
            return  ResponseEntity.badRequest().build();
        }
        return  ResponseEntity.ok().build();
    }
    // Change Avatar
    @Override
    @Transactional
    public ResponseEntity<?> changeAvatar(JsonNode jsonNode) {
        try {
            int idUser = Integer.parseInt(formatStringByJson(String.valueOf(jsonNode.get("idUser"))));
            String dataAvatar = formatStringByJson(String.valueOf(jsonNode.get("avatar")));
            Optional<User> user = userRepository.findById(idUser);
            // Xoá đi ảnh trước đó trong cloudinary
            if(user.get().getAvatar().length() > 0){
                imageService.deleteImage(user.get().getAvatar());
            }
            if(Base64MuiltipartFileConverter.isBase64(dataAvatar)){
                MultipartFile multipartFile = Base64MuiltipartFileConverter.convert(dataAvatar);
                String avatarURL = imageService.uploadImage(multipartFile, "User_" + idUser);
                user.get().setAvatar(avatarURL);
            }
            User newUser = userRepository.save(user.get());
            final String jwt = jwtService.generateToken(newUser.getUsername());
            return ResponseEntity.ok(new JwtResponse(jwt));
        }catch (Exception e){
            e.printStackTrace();
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }


    // Lấy ra các thông tin của user đó để xét quyền
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username);
//        if (user == null) {
//            throw new UsernameNotFoundException("Tai khoan khong ton tai!");
//        }
//        org.springframework.security.core.userdetails.User user1 =
//                new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), roleToAuthorities(user.getRoleList()));
//
//        return user1;
//    }

    // Lấy ra tên quyền
//    private Collection<? extends GrantedAuthority> roleToAuthorities(Collection<Role> roles) {
//        return roles.stream().map(role ->
//                new SimpleGrantedAuthority(role.getNameRole())
//        ).collect(Collectors.toList());
//    }
    private String formatStringByJson(String json) {
        return json.replaceAll("\"", "");
    }
}
