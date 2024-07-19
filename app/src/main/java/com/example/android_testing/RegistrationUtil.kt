package com.example.android_testing

object RegistrationUtil {

    private val existingUsers = listOf("Luffy", "Zoro", "Sanji");

//    the input is not valid if
//    ...the username or password is empty
//    ...the username is already taken
//    ...the confirmed password isnot same as the real password

    fun validateRegistrationInput(
        username : String,
        password : String,
        confirmedPassword : String
    ) : Boolean {
        if (username.isEmpty() || password.isEmpty()){
            return false;
        }
        if (!password.equals(confirmedPassword)){
            return false;
        }
        if (username in existingUsers){
            return false;
        }
        return true;
    }
}
