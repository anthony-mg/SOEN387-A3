package test;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import usermanagerIMP.UserManager;
import exceptions.*;

public class UserManagerTest {

    @Test (expected = UndefinedGroupException.class)
    public void undefinedGroupTest() throws UndefinedUserException, UndefinedGroupException, MissingGroupDefinitionException {
        UserManager um = new UserManager("src/main/java/test/jsonTestingFiles/users.json", "src/main/java/test/jsonTestingFiles/undefinedGroup.json");

        um.verifyGroupDefinitonsFile();
    }

    @Test (expected = UndefinedGroupException.class)
    public void undefinedParentTest() throws UndefinedUserException, UndefinedGroupException, MissingGroupDefinitionException {
        UserManager um = new UserManager("src/main/java/test/jsonTestingFiles/users.json", "src/main/java/test/jsonTestingFiles/undefinedParent.json");

        um.verifyGroupDefinitonsFile();
    }

    @Test (expected = UndefinedUserException.class)
    public void undefinedUserTest() throws UndefinedUserException, UndefinedGroupException, MissingGroupDefinitionException {
        UserManager um = new UserManager("src/main/java/test/jsonTestingFiles/users.json", "src/main/java/test/jsonTestingFiles/undefinedUser.json");

        um.verifyGroupDefinitonsFile();
    }

    @Test (expected = CircularGroupDefinitionException.class)
    public void circularDefinitonTest() throws CircularGroupDefinitionException {
        UserManager um = new UserManager("src/main/java/test/jsonTestingFiles/users.json", "src/main/java/test/jsonTestingFiles/circularDefs.json");

        um.getGroupsForUser("testUser");
    }

    @Test (expected = MissingGroupDefinitionException.class)
    public void missingDefinitionTest() throws MissingGroupDefinitionException, UndefinedGroupException, UndefinedUserException {
        UserManager um = new UserManager("src/main/java/test/jsonTestingFiles/users.json", "src/main/java/test/jsonTestingFiles/missingGroupDefinition.json");

        um.verifyGroupDefinitonsFile();
    }
}
