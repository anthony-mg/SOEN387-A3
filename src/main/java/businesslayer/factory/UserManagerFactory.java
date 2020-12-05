package businesslayer.factory;

import usermanagerIMP.UserManager;
import util.Configuration;

import java.lang.reflect.Constructor;

public class UserManagerFactory extends Factory {
    private String className;

    private UserManagerFactory(){
        Configuration config = new Configuration();
        config.init();
        className = config.getClassName();
    }

    private static class UserManagerFactoryHelper{
        private static final UserManagerFactory INSTANCE = new UserManagerFactory();
    }

    public static UserManagerFactory getInstance(){
        return UserManagerFactoryHelper.INSTANCE;
    }

    @Override
    public UserManager create(String pathToUserJson, String pathToGroupDefsJson) {
        try{
            Class<?> cl = Class.forName(className);
            Class<?>[] type = {String.class, String.class};
            Constructor<?> cons = cl.getConstructor(type);
            Object[] obj = {pathToUserJson, pathToGroupDefsJson};
            Object newInstacneUM = cons.newInstance(obj);
            return (UserManager)newInstacneUM;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
