package com.example.safetynet.nonce


data class UserIdentifier(val login: String,
/* device id can be stored for logging purposes */ val deviceId: String)