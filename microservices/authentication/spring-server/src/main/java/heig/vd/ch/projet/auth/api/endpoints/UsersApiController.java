package heig.vd.ch.projet.auth.api.endpoints;

import heig.vd.ch.projet.auth.api.model.Password;
import heig.vd.ch.projet.auth.api.model.Roles;
import heig.vd.ch.projet.auth.api.model.UserDTO;
import heig.vd.ch.projet.auth.api.service.AuthenticateService;
import heig.vd.ch.projet.auth.api.service.DecodedToken;
import heig.vd.ch.projet.auth.entities.UserEntity;
import heig.vd.ch.projet.auth.api.UsersApi;
import heig.vd.ch.projet.auth.api.model.User;
import heig.vd.ch.projet.auth.repositories.UserRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-07-26T19:36:34.802Z")

@Controller
public class UsersApiController implements UsersApi {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticateService authenticateService;

    @Autowired
    HttpServletRequest request;

    @Override
    public ResponseEntity<Void> createUser(@ApiParam(value = "" ,required=true) @RequestHeader(value="Authorization", required=true) String authorization,
                                           @ApiParam(value = "", required = true) @Valid @RequestBody User user) {

        DecodedToken decodedToken = (DecodedToken) request.getAttribute("decodedToken");
        if(decodedToken.getRole().equals(Roles.ADMIN.toString())){
            //Check if email is used
            if(userRepository.existsById(user.getEmail())){
                //Return a conflict status
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            //create a user
            UserEntity newUserEntity = toUserEntity(user);

            //Save the user
            userRepository.save(newUserEntity);

            //Return a created status (201)
            return ResponseEntity.status(HttpStatus.CREATED).build();

        }else {
            //Return an forbidden (403)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }


    @Override
    public ResponseEntity<Void> deleteUser(@ApiParam(value = "",required=true ) @PathVariable("userEmail") String userEmail,
                                           @ApiParam(value = "" ,required=true) @RequestHeader(value="Authorization", required=true) String authorization) {

        try {
            DecodedToken decodedToken = (DecodedToken) request.getAttribute("decodedToken");
            if(decodedToken.getRole().equals(Roles.ADMIN.toString())){
                //Get the existing user
                UserEntity userEntity = userRepository.findById(userEmail).get();

                //Delete the user
                userRepository.delete(userEntity);

                //Return an no content status (204)
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }else {
                //Return an forbidden (403)
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }catch (NoSuchElementException e){
            //Return an not found status status (404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @Override
    public ResponseEntity<UserDTO> getUserById(@ApiParam(value = "",required=true ) @PathVariable("userEmail") String userEmail,
                                               @ApiParam(value = "" ,required=true) @RequestHeader(value="Authorization", required=true) String authorization) {

        try {
            DecodedToken decodedToken = (DecodedToken) request.getAttribute("decodedToken");
            if((decodedToken.getEmail().equals(userEmail) && decodedToken.getRole().equals(Roles.USER.toString())) || decodedToken.getRole().equals(Roles.ADMIN.toString())) {
                //Get the existing user
                UserEntity userEntity = userRepository.findById(userEmail).get();

                //Change userEntity to userDTO
                UserDTO userDTO = toUserDTO(userEntity);

                //Return the user and ok status (200)
                return ResponseEntity.ok(userDTO);
            }else {
                //Return an forbidden status (403)
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

        }catch(NoSuchElementException e) {
            //Return an not found status (404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @Override
    public ResponseEntity<List<UserDTO>> getUsers(@ApiParam(value = "" ,required=true) @RequestHeader(value="Authorization", required=true) String authorization,
                                                  @ApiParam(value = "", defaultValue = "0") @Valid @RequestParam(value = "offset", required = false, defaultValue="0") Integer offset,
                                                  @ApiParam(value = "", defaultValue = "10") @Valid @RequestParam(value = "limit", required = false, defaultValue="10") Integer limit) {

        try {
            DecodedToken decodedToken = (DecodedToken) request.getAttribute("decodedToken");
            if(decodedToken.getRole().equals(Roles.ADMIN.toString())){
                //Get the page
                Page<UserEntity> userEntities = userRepository.findAll(PageRequest.of(offset,limit));

                //Get all users
                List<UserDTO> usersDTO = userEntities.get().map(userEntity -> toUserDTO(userEntity)).collect(Collectors.toList());

                //Return an array of user and an ok status (200)
                return ResponseEntity.ok(usersDTO);
            }else {
                //Return an forbidden status (403)
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }catch (IllegalArgumentException e){
            //Return an bad request status (403)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Override
    public ResponseEntity<Void> updateUser(@ApiParam(value = "",required=true ) @PathVariable("userEmail") String userEmail,
                                           @ApiParam(value = "" ,required=true) @RequestHeader(value="Authorization", required=true) String authorization,
                                           @ApiParam(value = "", required = true) @Valid @RequestBody Password password) {

        try {
            DecodedToken decodedToken = (DecodedToken) request.getAttribute("decodedToken");
            if((decodedToken.getEmail().equals(userEmail) && decodedToken.getRole().equals(Roles.USER.toString()))
                    || decodedToken.getRole().equals(Roles.ADMIN.toString())){

                //Get the existing user
                UserEntity userEntity = userRepository.findById(userEmail).get();

                //Update the user password
                String hashedPassword = authenticateService.hashPassword(password.getPassword());
                userEntity.setPassword(hashedPassword);

                //Save the user
                userRepository.save(userEntity);

                //Return an accept status (202)
                return ResponseEntity.accepted().build();

            }else{
                //Return an forbidden status (403)
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }catch (NoSuchElementException e){
            //Return an not found status (404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }


    /*Utils methods---------------------------------------------------------------------------------------------------*/
    private UserEntity toUserEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setEmail(user.getEmail());
        entity.setLastname(user.getLastname());
        entity.setFirstname(user.getFirstname());

        String hashedPassword = authenticateService.hashPassword(user.getPassword());
        entity.setPassword(hashedPassword);

        entity.setRole(user.getRole().toString());
        return entity;
    }

    private User toUser(UserEntity entity) {
        User user = new User();
        user.setEmail(entity.getEmail());
        user.setLastname(entity.getLastname());
        user.setFirstname(entity.getFirstname());
        user.setPassword(entity.getPassword());
        user.setRole(Roles.fromValue(entity.getRole()));
        return user;
    }

    private UserDTO toUserDTO(UserEntity entity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(entity.getEmail());
        userDTO.setLastname(entity.getLastname());
        userDTO.setFirstname(entity.getFirstname());
        userDTO.setRole(Roles.fromValue(entity.getRole()));

        return userDTO;
    }

}
