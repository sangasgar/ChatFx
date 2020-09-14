package Server;

public interface AuthService {
    /*
    @return nickname если пользователь есть
    @return null если пользователя нет
     */
    String getNickNameByLogicANDPassword(String login,String password);
}
