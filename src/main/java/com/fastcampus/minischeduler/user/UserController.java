package com.fastcampus.minischeduler.user;

import com.fastcampus.minischeduler.core.annotation.MyErrorLog;
import com.fastcampus.minischeduler.core.annotation.MyLog;
import com.fastcampus.minischeduler.core.auth.jwt.JwtTokenProvider;
import com.fastcampus.minischeduler.core.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @MyErrorLog
    @MyLog
    @PostMapping("/join")
    public ResponseEntity<?> join(
            @RequestBody
            @Valid
            UserRequest.JoinDTO joinRequestDTO,
            Errors errors
    ) {
        if(errors.hasErrors()) return null;

        UserResponse.JoinDTO joinResponseDTO = userService.signup(joinRequestDTO);
        ResponseDTO<?> responseDTO = new ResponseDTO<>(joinResponseDTO);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody
            @Valid
            UserRequest.LoginDTO loginRequestDTO,
            Errors errors
    ) {
        if(errors.hasErrors()) return null;

        String jwt = userService.signin(loginRequestDTO);

        ResponseDTO<?> responseDTO = new ResponseDTO<>();

        return ResponseEntity.ok()
                .header(JwtTokenProvider.HEADER, jwt)
                .body(responseDTO);
    }

    /*
    @GetMapping("/s/user/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id, @AuthenticationPrincipal MyUserDetails myUserDetails) throws JsonProcessingException {
        if(id.longValue() != myUserDetails.getUser().getId()){
            throw new Exception403("권한이 없습니다");
        }
        UserResponse.DetailOutDTO detailOutDTO = userService.회원상세보기(id);
        //System.out.println(new ObjectMapper().writeValueAsString(detailOutDTO));
        ResponseDTO<?> responseDTO = new ResponseDTO<>(detailOutDTO);
        return ResponseEntity.ok(responseDTO);
    }
     */

    @PostMapping("/logout")
    public ResponseEntity<?> logout(

    ) {

        return null;
    }

    @GetMapping("/getUserInfo")
    public ResponseEntity<?> getUserInfo(
            @RequestHeader(JwtTokenProvider.HEADER) String token
    ){
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        UserResponse.GetUserInfoDTO getUserInfoDTO = userService.getUserInfo(userId);
        return ResponseEntity.ok(getUserInfoDTO);
    }

    @PostMapping("/updateUserInfo/{id}")
    public ResponseEntity<?> updateUserInfo(
            @PathVariable Long id,
            @RequestHeader(JwtTokenProvider.HEADER) String token,
            @RequestBody
            @Valid
            UserRequest.UpdateUserInfoDTO updateUserInfoDTO,
            Errors errors
    ){
        if(errors.hasErrors()) return null;


        User userPS = userService.updateUserInfo(updateUserInfoDTO, id)
                .orElseThrow(() -> new RuntimeException("유저 업데이트 실패"));
        // user 객체를 이용한 작업 수행


        return ResponseEntity.ok()
                .body(userPS);

    }




}
