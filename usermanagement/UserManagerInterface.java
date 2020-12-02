package usermanagement;

import java.util.ArrayList;

import usermanagerIMP.exceptions.CircularGroupDefinitionException;
import usermanagerIMP.exceptions.UndefinedGroupException;
import usermanagerIMP.exceptions.UndefinedUserException;

public interface UserManagerInterface {
	public ArrayList<String> getGroupsForUser(String username) throws CircularGroupDefinitionException;
	public boolean verifyUser(String username, String plainPasswordEntered);
	public boolean verifyGroupDefinitonsFile() throws UndefinedUserException, UndefinedGroupException;
	public String getUsernameFromEmail(String email);
	public String getEmailFromUsername(String username);
}
