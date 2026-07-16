package modules.user.service;

import config.exception.DatabaseConnectionException;
import modules.shared.http.HttpClientWrapper;
import modules.shared.integrations.decolecta.DecolectaService;
import modules.user.dto.UserRegistrationDto;
import modules.user.exception.UserValidatorException;
import modules.user.mapper.UserMapper;
import modules.user.model.User;
import modules.user.repository.IUserRepository;
import modules.user.validator.UserValidator;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class UserService {

  private final IUserRepository IUserRepository;
  private final UserValidator userValidator;
  private final UserMapper userMapper;

  public UserService(IUserRepository IUserRepository, UserValidator userValidator, UserMapper userMapper) {
    this.IUserRepository = IUserRepository;
    this.userValidator = userValidator;
    this.userMapper = userMapper;
  }

  public void registerUser(UserRegistrationDto userRegistrationDto) throws DatabaseConnectionException {
    // validar
    userValidator.validateRegistration(userRegistrationDto);

    // Validar DNI con la API de RENIEC
    DecolectaService decolectaService = new DecolectaService(new HttpClientWrapper());
    String nombreCompleto = decolectaService.obtenerNombrePorDni(userRegistrationDto.getDni());
    if ("DNI no existe".equalsIgnoreCase(nombreCompleto)) {
      throw new UserValidatorException("El DNI ingresado no existe en la RENIEC.");
    }

    // convertir (delegamos a maper)
    User user = userMapper.toEntity(userRegistrationDto);

    // hashear pin
    String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
    user.setPassword(hashedPassword);

    // guardar base de datos
    IUserRepository.save(user);
  }

  public boolean login(String nickname, String pin) throws DatabaseConnectionException {
    Optional<User> userOptional = IUserRepository.findByNickname(nickname);
    if (userOptional.isEmpty()) {
      return false;
    }
    User user = userOptional.get();
    return BCrypt.checkpw(pin, user.getPassword());
  }


  public Optional<User> findById(Integer id) throws DatabaseConnectionException {
     return IUserRepository.findById(id);
  }


  public Optional<User> findByDni(String dni) throws DatabaseConnectionException {

    return IUserRepository.findByDni(dni);
  }

  // Si también necesitas buscar por nickname en otros módulos, puedes delegarlo así:
  public Optional<User> findByNickname(String nickname) throws DatabaseConnectionException {
    return IUserRepository.findByNickname(nickname);
  }



}
