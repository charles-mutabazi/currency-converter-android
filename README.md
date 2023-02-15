# Currency Exchange App

This is an Android app was developed as a PayPay challenge for Android Engineer position. 

Below is the information on how is structure the application and libraries used.

- Design Pattern: MVVM (Clean Architecture) - https://developer.android.com/topic/architecture
- **Jetpack Compose** was used as UI layer (with out-of-the-box Material 3 theme)
- **Koin** for Dependency Injection
- **Ktor** for making api request - api used: https://docs.openexchangerates.org/
- **Room database** for data persistence
- **WorkManager** for job/task scheduling
- **Mockk** for testing and mocks - 
	- NOTE: Only **UNIT TESTS** were made, I intentionally ignored instrumentTests because according to the challenge description, only Unit Test are required.

It was fun working on this project.
***Date of submission: 15th Feb, 2023***
