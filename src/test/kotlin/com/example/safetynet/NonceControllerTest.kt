package com.example.safetynet

import io.restassured.RestAssured.given
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = DEFINED_PORT)
class NonceControllerTest {

    @Test
    fun `generate nonce`() {
        given().log().all()
                .queryParam("login", "kkocel")
                .queryParam("deviceId", "123")
                .`when`()
                .get("/nonce")
                .then().log().all()
                .statusCode(200)

    }

    @Test
    fun `login`() {
        given().log().all()
                .queryParam("login", "kkocel")
                .queryParam("password", "valid")
                .queryParam("jwt", "token")
                .`when`()
                .post("/login")
                .then().log().all()
                .statusCode(200)
    }
}