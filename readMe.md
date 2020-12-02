READ ME FOR THE USERMANAGEMENT PACKAGES

usermanagerIMP.exceptions:
	CircularGroupDefinitionException: Thrown by getGroupsFromUser()
	UndefinedGroupExceptiom: Thrown by verifyGroupDefinitionsFile()
	UndefinedUSerException: Thrown by verifyGroupDefinitionsFile()
	
UserManager:
	Takes care of: User Authentification, getting the user's groups, and verifying the group definitions json.

	Constructor: UserManager(Path to users json file, path to group definitions json file)
		Sets the respective path variables and loads the users.

	verifyUser(): 
		Authenticates user
	
	getGroupsFromUser(String username):
		IMPORTANT: This method throws the exception CircularGroupDefinitionException.
			   This exception should be dealt with in the servlet.

		This method returns the groups(ArrayList<String>) the user is a member of, including all child groups.
		In the servlet, during the log in process, call this method with the username and set the 
		groups in the session for the user.

	verifyGroupDefinitionsFile():
		IMPORTANT: This method throws exceptions UndefinedGroupExceptiona and UndefinedUserExceptions.
			   These must be dealth with in the servlet.

		Verifies that the group definitons json file has no undefined groups or undefined users.
		Should probably call this method after creating the UserMananger object in the servlet. 