package controllers.usercontrollers;

import domain.entities.User;
import domain.usecases.UserManager;

class PublicUserController extends UserController {
    protected final UserManager userManager;

    protected PublicUserController(UserManager userManager) {
        this.userManager = userManager;
    }

    /**
     * Creates a new user account as the specified account type
     * THIS IS A TEMPORARY METHOD THAT CAN BE USED TO QUICKLY CREATE DIFFERENT ACCOUNTS. IT WILL BE REMOVED AFTER THE
     * PROGRAM IS FINISHED!!!!!!!!!
     * @param userType    the type of user ("ATTENDEE"/"SPEAKER"/"ORGANIZER").
     * @param name    the name of the user.
     * @param userID    the login ID that will be used to log in the user.
     * @param userPassword  the login password that will be used to log in the user.
     * @return  true if account creation was successful, false otherwise (for reasons such as if userID is already
     *          taken).
     */

    public boolean createAnyUserTEMPORARY(String userType, String name, String userID, String userPassword) {
        return userManager.createUser(name, userType, userID, userPassword);
    }

    /**
     * Creates a new user account as type "ATTENDEE"
     * @param name    the name of the user.
     * @param userID    the login ID that will be used to log in the user.
     * @param userPassword  the login password that will be used to log in the user.
     * @return  true if account creation was successful, false otherwise (for reasons such as if userID is already
     *          taken).
     */

    public boolean createUser(String name, String userID, String userPassword) {
        return userManager.createUser(name, "ATTENDEE", userID, userPassword);
    }

    /**
     * Gets the name of an user by their userID.
     * @param userID    the login ID of the user to search for.
     * @return  the name of the user if userID points to an existing user, null otherwise.
     */

    public String getUserName(String userID) {
        if (!userManager.userExists(userID)) return null;

        User u = userManager.getUser(userID);
        return userManager.getName(u);
    }

    /**
     * Gets the type of an user by their userID.
     * @param userID    the login ID of the user to search for.
     * @return  the type of the user if userID points to an existing user, null otherwise.
     */

    public String getUserType(String userID) {
        if (!userManager.userExists(userID)) return null;

        User u = userManager.getUser(userID);
        return userManager.getUserType(u);
    }


}