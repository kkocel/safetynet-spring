# Secure implementation of SafetyNet Attestation API developed in Spring

This project aims to provide implementation of secure flow of [SafetyNet Attestation API](https://developer.android.com/training/safetynet/attestation.html) by Google.

## What's a SafetyNet Attestation?
SafetyNet is a mechanism designed to check whether a mobile device has been tampered.
It means that it could be rooted, runs a custom ROM or has been infected with a malware.

## What's a secure flow anyway?
The secure flow is a implementation of SafetyNet Attestation that is performed on the server instead of a 
mobile phone.

![SafetyNet Attestation secure flow](https://raw.githubusercontent.com/kkocel/safetynet-spring/master/img/safetynet-secure-flow.png "SafetyNet Attestation secure flow")

More on that topic: https://www.synopsys.com/blogs/software-security/using-safetynet-api/

## Running the tests

```
./gradlew clean test
```

## Authors

* **Krzysztof Kocel** - [kkocel](https://github.com/kkocel)

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details
