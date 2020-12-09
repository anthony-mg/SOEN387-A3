package businesslayer.interfaces.usermanagement;

import java.util.ArrayList;

import exceptions.CircularGroupDefinitionException;
import exceptions.MissingGroupDefinitionException;
import exceptions.UndefinedGroupException;
import exceptions.UndefinedUserException;

public interface UserManagerInterface {
	public ArrayList<String> getGroupsForUser(String username) throws CircularGroupDefinitionException;
	public boolean verifyUser(String username, String plainPasswordEntered);
	public boolean verifyGroupDefinitonsFile() throws UndefinedUserException, UndefinedGroupException, MissingGroupDefinitionException;
	public String getUsernameFromEmail(String email);
	public String getEmailFromUsername(String username);
}
