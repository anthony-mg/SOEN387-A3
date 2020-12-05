package businesslayer.factory;

import usermanagerIMP.UserManager;

public abstract class Factory {
    abstract UserManager create(String pathToUserJson, String pathToGroupDefsJson);
}
