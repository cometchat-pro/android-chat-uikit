<div style="width:100%">
    <div style="width:50%; display:inline-block">
        <p align="center">
        <img align="center" width="auto" height="auto"  src="https://github.com/cometchat-pro/android-chat-uikit/blob/master/Screenshot/ui%20main.png">
        </p>
    </div>
</div>
</br>
</br>
</div>

# What is UI Kit
The UI Kit library is collection of custom UI Component and UI Screens design to build chat application within few minutes.
UI kit is designed to avoid boilerplate code for building UI,it has three different ways to build a chat application with fully customizable UI.It will help developers to build a chat application within using various UI Components.


## Setup
 Follow the below metioned steps for easy setup and seamless integration with UI Kit

### Get your Application Keys
<a href="https://app.cometchat.io/" traget="_blank">Signup for CometChat</a> and then:

* Create a new app
* Head over to the API Keys section and note the `API_Key` and `App_ID` (for Auth Only key)
---
### Add the CometChat Dependency

First, add the repository URL to the **project level** `build.gradle` file in the repositories block under the allprojects section.
```groovy
allprojects {
  repositories {
    maven {
      url "https://dl.bintray.com/cometchat/pro"
    }
  }
}
```
Then, add CometChat to the **app level** `build.gradle` file in the dependencies section.

```groovy
dependencies {
  implementation 'com.cometchat:pro-android-chat-sdk:2.0.2'
}
```
Finally, add the below lines android section of the **app level** gradle file

```groovy
android {
  compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
  }
}
````
### Initialize CometChat

The `init()` method initializes the settings required for CometChat. We suggest calling the `init()` method on app startup, preferably in the `onCreate()` method of the Application class.
```java
private String appID = "APP_ID";
private String region = "REGION";

AppSettings appSettings=new AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(region).build();

CometChat.init(this, appID,appSettings, new CometChat.CallbackListener<String>() {
  @Override
  public void onSuccess(String successMessage) {
    Log.d(TAG, "Initialization completed successfully");
  }
  @Override
  public void onError(CometChatException e) {
    Log.d(TAG, "Initialization failed with exception: " + e.getMessage());
  }
});
```
**Note :**
Make sure you replace the APP_ID with your CometChat `App_ID` and `REGION` with your app region in the above code.

### Log in your User

The `login()` method returns the User object containing all the information of the logged-in user.

```java
private String UID = "SUPERHERO1";
private String API_KEY = "API_KEY";

CometChat.login(UID, API_KEY , new CometChat.CallbackListener<User>() {
  @Override
  public void onSuccess(User user) {
    Log.d(TAG, "Login Successful : " + user.toString());
  }
  @Override
  public void onError(CometChatException e) {
    Log.d(TAG, "Login failed with exception: " + e.getMessage());
  }
});
```
**Note :** </br>
* Make sure you replace the `API_KEY` with your CometChat API Key in the above code.
* We have setup 5 users for testing having UIDs: `SUPERHERO1`, `SUPERHERO2`, `SUPERHERO3`,`SUPERHERO4` and `SUPERHERO5`.


## Add UI Kit to your project
After adding necessary dependancies inside you app to integrate UI Kit inside your app.Kindly follow the below steps:
1. Simply clone the UI Kit Library from android-chat-uikit repository.
3. Import `uikit` Module from Module Settings.
4. If the Library is added sucessfully, it will look like mentioned in the below image.
<img align="center" width="auto" height="auto" src="https://github.com/cometchat-pro/android-chat-uikit/blob/master/Screenshot/Screen%20Shot%202019-12-23%20at%207.37.37%20PM.png">


### Launch UI Unified

*UI Unified is a way to launch a fully working chat application using the UI Kit .In UI Unified all the UI Screens and UI Components working together to give the full experience of a chat application with minimal coding effort.*

 Add the following code snippet in `onSuccess` of CometChat `login`.

 ```java
 startActivity(new Intent(YourActivity.this,CometChatUnified.class))

```
</br>
<img align="center" width="100%" height="auto" src="https://github.com/cometchat-pro/android-chat-uikit/blob/master/Screenshot/UI%20Unified%20(1).png">


### Note

As **UI Kit**  is using material components your app's theme should extend `Theme.MaterialComponents`.    Follow the guide on [Getting started Material Components](https://material.io/develop/android/docs/getting-started/)

The following is the list of Material Components themes you can use to get the latest component styles and theme-level attributes.

`Theme.MaterialComponents` </br>
`Theme.MaterialComponents.NoActionBar`  </br>
`Theme.MaterialComponents.Light` </br>
`Theme.MaterialComponents.Light.NoActionBar` </br>
`Theme.MaterialComponents.Light.DarkActionBar` </br>
`Theme.MaterialComponents.DayNight` </br>
`Theme.MaterialComponents.DayNight.NoActionBar` </br>
`Theme.MaterialComponents.DayNight.DarkActionBar` </br>

Update your app theme to inherit from one of these themes, e.g.:

```xml
<style name="AppTheme" parent="Theme.MaterialComponents.Light.NoActionBar.Bridge">

    <!-- Customize your theme here. -->

    </style>
```




## Next Step

 To read the full dcoumentation on UI Kit integration visit our [Documentation](https://prodocs.cometchat.com/docs/android-ui-kit)



## Troubleshooting
Facing any issues while integrating or installing the UI Kit please <a href="https://forum.cometchat.com/"> visit our forum</a>.
