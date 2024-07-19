package com.example.android_testing

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RegistrationUtilTest {

    @Test
    fun `empty username returns false` (){
        val res = RegistrationUtil.validateRegistrationInput(
            "",
            "123",
            "123"
        )
        assertThat(res).isFalse()
    }

    @Test
    fun `valid username and correctly repeated password returns true` (){
        val res = RegistrationUtil.validateRegistrationInput(
            "Bsal",
            "123",
            "123"
        )
        assertThat(res).isTrue()
    }

    @Test
    fun `username already exists returns false` (){
        val res = RegistrationUtil.validateRegistrationInput(
            "Zoro",
            "123",
            "123"
        )
        assertThat(res).isFalse()
    }

    @Test
    fun `empty password returns false` (){
        val res = RegistrationUtil.validateRegistrationInput(
            "Zoro",
            "",
            ""
        )
        assertThat(res).isFalse()
    }

    @Test
    fun `incorrectly repeated password returns false` (){
        val res = RegistrationUtil.validateRegistrationInput(
            "Zoro",
            "1234",
            "1233"
        )
        assertThat(res).isFalse()
    }
}