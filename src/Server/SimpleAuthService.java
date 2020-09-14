package Server;

import java.util.ArrayList;
import java.util.List;

public class SimpleAuthService implements AuthService{
    private class UserData {
        String login;
        String password;
        String nickname;

        public UserData(String login, String password, String nickname) {
            this.login = login;
            this.password = password;
            this.nickname = nickname;
        }
    }
    List<UserData> users;
    public SimpleAuthService(){
        users = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            users.add(new UserData("login" + i,"pass"+i,"nick" + i));
        }
    }
    @Override
    public String getNickNameByLogicANDPassword(String login, String password) {
        return null;
    }
}
