package user;

import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.DuplicatedUsernameException;
import exceptions.EmptyRequiredAttributeException;
import exceptions.NotExistingUserException;
import exceptions.NotSamePasswordException;
import utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *Aquesta classe gestionem els usuaris, la seva informació...
 *Per aquesta classe disposem de tres atributs. El primer es l’username de úsuari (que és el
 que l’identifica), el segon és el nom d’aquest i l'últim és la contrasenya del usuari (la que usarà per entrar al seu perfil)
 */
public class User {
    public static final String USERS = "users.json";

    private String username;
    private String name;
    private String password;

    public User() {
    }

    public User(String username, String name) {
        this.username = username;
        this.name = name;
    }

    /**
     * Retorna dins un map d'string i user el conjunt de tots els User de dins persistència.
     La clau és el username del User i el valor és el propi User
     * @return els usuaris
     */
    public static Map<String, User> getUsers() {
        Map<String, User> users = new HashMap<>();

        File file = new File(USERS);
        if (!file.exists()) return users;
        ObjectMapper mapper = new ObjectMapper();
        try {
            users = mapper.readValue(
                    file, mapper.getTypeFactory().constructMapType(Map.class, String.class, User.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public String getPassword() { return password;}

    public void setPassword(String password) { this.password = password;}

    /**
     * Mètode static que retorna un User donat el seu username (que l’identifica)
     * @param username el nom de l'usuari
     * @return User
     */
    public static User getUserByUsername(String username) {
        return getUsers().get(username);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Mètode que guarda dins persistència l’usuari de la instància i si aquest ja existeix el
     reempleça
     * @throws DuplicatedUsernameException excepció
     * @throws EmptyRequiredAttributeException excepció
     */
    public void save() throws DuplicatedUsernameException, EmptyRequiredAttributeException {
        if (username == null || username.length() == 0 || password == null || password.length() == 0|| name == null || name.length() == 0)
            throw new EmptyRequiredAttributeException();

        Map<String, User> users = getUsers();
        if (users.containsKey(this.username)) throw new DuplicatedUsernameException();
        users.put(this.username, this);
        FileUtils.saveObjectInFile(users, USERS);
    }

    /**
     *Aquest mètode actualitza l'informació d'un usuari que ja existeix al programa i que ja està registrat
     */
    private void update() {
        Map<String, User> users = getUsers();
        users.put(this.username, this);
        FileUtils.saveObjectInFile(users, USERS);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (username != null ? !username.equals(user.username) : user.username != null) return false;
        return name != null ? name.equals(user.name) : user.name == null;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    /**
     * Aquest mètode valida que l'usuari que se li passa com a paràmetre existeixi
     * @param username el nom de l'usuari
     * @param password la contrasenya de l'usuari
     * @throws NotExistingUserException excepció
     * @throws NotSamePasswordException excepció
     */
    public static void validateCredentials(String username, String password) throws NotExistingUserException, NotSamePasswordException {
        if (!getUsers().containsKey(username)) throw new NotExistingUserException();
        if (!getUserByUsername(username).getPassword().equals(password)) throw new NotSamePasswordException();
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Username: " + username;
    }

    /**
     * Aquest mètode modifica la informació d'un usuari a partir de la nova que se li passa com a paràmetre
     * @param newName el nou nom
     * @param newPassword la nova conrasenya
     * @throws DuplicatedUsernameException excepció
     * @throws EmptyRequiredAttributeException excepció
     */
    public void modifyInformation(String newName, String newPassword) throws DuplicatedUsernameException, EmptyRequiredAttributeException {
        this.name = newName;
        this.password = newPassword;
        update();
    }
}
