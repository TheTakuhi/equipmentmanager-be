package com.interstellar.equipmentmanager.mapper;

import com.interstellar.equipmentmanager.config.ModelMapperConfig;
import com.interstellar.equipmentmanager.config.mapper.UserMapperConfig;
import com.interstellar.equipmentmanager.faker.TestFakeGeneratorProvider;
import com.interstellar.equipmentmanager.helper.TestPrinter;
import com.interstellar.equipmentmanager.model.dto.keycloak.user.out.KeycloakUserDTO;
import com.interstellar.equipmentmanager.model.dto.user.in.UserCreateDTO;
import com.interstellar.equipmentmanager.model.dto.user.out.UserCroppedDTO;
import com.interstellar.equipmentmanager.model.dto.user.out.UserDTO;
import com.interstellar.equipmentmanager.model.dto.user.in.UserEditDTO;
import com.interstellar.equipmentmanager.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Random;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {UserMapperConfig.class, ModelMapperConfig.class})
@ExtendWith({SpringExtension.class, InstancioExtension.class})
@Slf4j
public class UserMapperTest {
    @Autowired
    private ModelMapper mapper;
    private static final Long seed = new Random().nextLong();
    @BeforeAll
    static void beforeAll() {
        log.info("Seed {} is used for testing", seed);
    }

    @ParameterizedTest
    @MethodSource("genUsers")
    public void UserToCroppedDTO(User user) {
        mapper.typeMap(User.class, UserCroppedDTO.class).validate();
        log.info(TestPrinter.prettifyObjectPrint(user));
        log.info(TestPrinter.prettifyObjectPrint(mapper.map(user, UserCroppedDTO.class)));
    }

    @ParameterizedTest
    @MethodSource("genUsers")
    public void UserToDTO(User user) {
        mapper.typeMap(User.class, UserDTO.class).validate();
        log.info(TestPrinter.prettifyObjectPrint(user));
        log.info(TestPrinter.prettifyObjectPrint(mapper.map(user, UserDTO.class)));
    }

    @ParameterizedTest
    @MethodSource("genUsers")
    public void UserDTOToCroppedDTO(User user) {
        mapper.typeMap(User.class, UserDTO.class).validate();

        var dto = mapper.map(user, UserDTO.class);
        mapper.typeMap(UserDTO.class, UserCroppedDTO.class).validate();
        var res =mapper.map(dto, UserCroppedDTO.class);

        log.info(TestPrinter.prettifyObjectPrint(res));
    }

    @ParameterizedTest
    @MethodSource("genUserRepresentations")
    public void UserRepresentationToKeycloakUserDTO(UserRepresentation userRepresentation) {

        mapper.typeMap(UserRepresentation.class, KeycloakUserDTO.class).validate();
        log.info(TestPrinter.prettifyObjectPrint(userRepresentation));
        var res = mapper.map(userRepresentation, KeycloakUserDTO.class);

        log.info(TestPrinter.prettifyObjectPrint(res));
    }

    @ParameterizedTest
    @MethodSource("genUserDTO")
    public void UserDTOToCroppedDTO(UserDTO userDTO) {
        mapper.typeMap(UserDTO.class, UserCroppedDTO.class).validate();

        log.info(TestPrinter.prettifyObjectPrint(userDTO));
        var res = mapper.map(userDTO, UserCroppedDTO.class);

        assertThat(res.getId()).isEqualTo(userDTO.getId());
        assertThat(res.getUserRoles().size()).isEqualTo(userDTO.getUserRoles().size());
        assertThat(res.getOwnedContractIds().size()).isEqualTo(userDTO.getOwnedContracts().size());
    }

    @ParameterizedTest
    @MethodSource("genKeycloakUser")
    public void KeycloakUserToUserCreateDTO(KeycloakUserDTO keycloakUserDTO) {
        mapper.typeMap(KeycloakUserDTO.class, UserCreateDTO.class);

        log.info(TestPrinter.prettifyObjectPrint(keycloakUserDTO));
        var res = mapper.map(keycloakUserDTO, UserCreateDTO.class);

        assertThat(res.getId()).isEqualTo(keycloakUserDTO.getLdapId());
        assertThat(res.getLogin()).isEqualTo(keycloakUserDTO.getLogin());
        assertThat(res.getEmail()).isEqualTo(keycloakUserDTO.getEmail());
        assertThat(res.getUserRoles()).isEqualTo(keycloakUserDTO.getUserRoles());
    }

    @ParameterizedTest
    @MethodSource("genKeycloakUser")
    public void KeycloakUserToUserEditDTO(KeycloakUserDTO keycloakUserDTO) {
        mapper.typeMap(KeycloakUserDTO.class, UserEditDTO.class);

        log.info(TestPrinter.prettifyObjectPrint(keycloakUserDTO));
        var res = mapper.map(keycloakUserDTO, UserEditDTO.class);
        log.info(TestPrinter.prettifyObjectPrint(res));

        assertThat(res.getId()).isEqualTo(keycloakUserDTO.getLdapId());
        assertThat(res.getLogin()).isEqualTo(keycloakUserDTO.getLogin());
        assertThat(res.getEmail()).isEqualTo(keycloakUserDTO.getEmail());
        assertThat(res.getUserRoles()).isEqualTo(keycloakUserDTO.getUserRoles());
    }

    @ParameterizedTest
    @MethodSource("genCreateDTO")
    public void UserCreateDTOToEntity(UserCreateDTO userCreateDTO) {
        mapper.typeMap(UserCreateDTO.class, User.class).validate();
        log.info(TestPrinter.prettifyObjectPrint(userCreateDTO));

        var res = mapper.map(userCreateDTO, User.class);
        log.info(TestPrinter.prettifyObjectPrint(res));

        assertThat(res.getEmail()).isEqualTo(userCreateDTO.getEmail());
        assertThat(res.getLogin()).isEqualTo(userCreateDTO.getLogin());

        assertThat(res.getFullName()).contains(userCreateDTO.getFirstName());
        assertThat(res.getFullName()).contains(userCreateDTO.getLastName());
    }

    @ParameterizedTest
    @MethodSource("genEditDTO")
    public void UserEditDTOToEntity(UserEditDTO contractEditDTO) {
        mapper.typeMap(UserEditDTO.class, User.class).validate();
        log.info(TestPrinter.prettifyObjectPrint(contractEditDTO));
        var res = mapper.map(contractEditDTO, User.class);
        log.info(TestPrinter.prettifyObjectPrint(res));
    }


    public static Stream<UserRepresentation> genUserRepresentations() {
        return genObjects(UserRepresentation.class, 10);
    }
    public static Stream<User> genUsers() {
        return genObjects(User.class, 3);
    }
    public static Stream<UserCreateDTO> genCreateDTO() {
        return genObjects(UserCreateDTO.class, 10);
    }
    public static Stream<UserEditDTO> genEditDTO() {
        return genObjects(UserEditDTO.class, 3);
    }
    public static Stream<UserDTO> genUserDTO() {return genObjects(UserDTO.class, 10);}
    public static Stream<KeycloakUserDTO> genKeycloakUser() {return genObjects(KeycloakUserDTO.class, 10);}
    public static <T> Stream<T> genObjects(Class<T> clazz, int limit) {
        var gen = TestFakeGeneratorProvider.getFakeModelGenerator(clazz, seed);
        return Stream.generate(gen::create).limit(limit);
    }

}
