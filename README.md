<div style="width:100%">
	<div style="width:50%; display:inline-block">
		<p align="center">
         <img align="center" src="https://avatars2.githubusercontent.com/u/45484907?s=200&v=4"/>
		</p>	
	</div>	
</div>
<br></br><br></br>

# Android Java Chat UI Kit

CometChat Java UI Kit is a collection of custom UI Components designed to build text chat and voice/video calling features in your application.
The UI Kit is developed to keep developers in mind and aims to reduce development efforts significantly<br/><br/>

[![Platform](https://img.shields.io/badge/Platform-Android-brightgreen.svg)](#)
[![Platform](https://img.shields.io/badge/Language-Java-yellowgreen.svg)](#)
![GitHub repo size](https://img.shields.io/github/repo-size/cometchat-pro/android-java-chat-ui-kit)
![GitHub contributors](https://img.shields.io/github/contributors/cometchat-pro/android-java-chat-ui-kit)
![Version](https://shields.io/badge/version-v3.0.4--pluto.beta2-orange)
![GitHub stars](https://img.shields.io/github/stars/cometchat-pro/android-java-chat-ui-kit?style=social)
![Twitter Follow](https://img.shields.io/twitter/follow/cometchat?style=social)

<img align="center" src="https://files.readme.io/b81d92b-UI_Kit__2.png"/>

<hr/>

## Prerequisites :star:
Before you begin, ensure you have met the following requirements:<br/>
✅ &nbsp; You have `Android Studio` installed in your machine.<br/>
✅ &nbsp; You have a `Android Device or Emulator` with Android Version 6.0 or above.<br/>
✅ &nbsp; You have read [CometChat Key Concepts](https://www.cometchat.com/docs/java-uikit-beta/key-concepts).<br/>

<hr/>

## Installing Android Java Chat UI Kit
## Setup :wrench:

To setup Android Chat UI Kit, you  need to first register on CometChat Dashboard. [Click here to sign up](https://app.cometchat.com/login).

### i. Get your Application Keys :key:

1. Create a new app: Click **Add App** option available  →  Enter App Name & other information  → Create App
2. You will find `APP_ID`, `AUTH_KEY` and `REGION` key at top in **QuickStart** section or else go to "API & Auth Keys" section and copy the `APP_ID`, `AUTH_KEY` and `REGION` key from the "Auth Only API Key" tab.
   <img align="center" src="https://files.readme.io/4b771c5-qs_copy.jpg"/>


### ii. Add the CometChat UI Kit Dependency

**Step 1 -** Add the repository URL to the project level build.gradle file in the repositories block under the allprojects section.

<table><td>

```groovy
allprojects {
  repositories {
    maven {
      url "https://dl.cloudsmith.io/public/cometchat/cometchat-pro-android/maven/"
    }
  }
}
```

</td></table>

**Step 2-** Open the app level build.gradle file and follow below <br/>

1. Add the below line in the dependencies section.

<table><td>

```groovy
dependencies {
   implementation 'com.cometchat:pro-android-java-chat-ui-kit:3.0.15-pluto.beta2.1'
}
```

</td></table>


You can refer to the below link for next instructions:<br/>
[📝 Add Java Pluto UI Kit Dependency](https://www.cometchat.com/docs/java-uikit-beta/integration)

<hr/>

## Configure CometChat SDK

### i. Initialize CometChat 🌟
The init() method initializes the settings required for CometChat. We suggest calling the init() method on app startup, preferably in the onCreate() method of any activity before calling any CometChat.

<table><td>

```java
import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;

String appID = "APP_ID"; // Replace with your App ID
String region = "REGION"; // Replace with your App Region ("eu" or "us")
String authKey = "AUTH_KEY"; // Replace with your Auth key
UIKitSettings uiKitSettings = new UIKitSettings.UIKitSettingsBuilder().setRegion(region).setAppId(appID).setAuthKey(authKey).subscribePresenceForAllUsers().build();
        CometChatUIKit.init(this, uiKitSettings, new CometChat.CallbackListener<String>() {
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

</td></table>

| :information_source: &nbsp; <b> Note: Make sure to replace `region` and `appID` with your credentials.</b> |
|------------------------------------------------------------------------------------------------------------|

### ii. Login User 👤
Once you have created the user successfully, you will need to log the user into CometChat using the login() method.

<table><td>

```java
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;

String uid = "user1"; // Replace with the UID of the user to login
CometChatUIKit.login(uid, new CometChat.CallbackListener<User>() {
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

</td></table>

| :information_source: &nbsp; <b>Note - The login() method needs to be called only once. Also replace AUTH_KEY with your App Auth Key.</b> |
|------------------------------------------------------------------------------------------------------------|

<hr/>

📝  &nbsp; Please refer to our [Developer Documentation](https://www.cometchat.com/docs/java-uikit-beta/overview) for more information on how to configure the Java Pluto UI Kit and implement various features using the same.

<hr/>

## Add UI Kit Library
| :information_source: &nbsp; <b>Note - If you have added <code>android-java-ui-kit</code> as Dependency, then you can skip first three steps.</b> |
|------------------------------------------------------------------------------------------------------------|

1. Simply clone the project from [android-java-chat-ui-kit](https://github.com/cometchat-pro/android-java-chat-ui-kit/tree/pluto) repository.

2. Import `uikit` Module from Module Settings.( To know how to import `uikit` as Module visit this [link](https://www.cometchat.com/docs/java-uikit-beta/integration) )

3. If the Library is added successfully, it will look like mentioned in the below image.<br>
   <img align="center" width="auto" height="auto" src="https://github.com/cometchat-pro/android-chat-uikit/blob/master/Screenshot/Screen%20Shot%202019-12-23%20at%207.37.37%20PM.png">

4. Next steps is to adding necessary dependencies inside your app to integrate UI Kit.
   <ul>
   <li>To use UI Kit you have to add Material Design Dependency as the UI Kit uses Material Design Components.
   <table><td>

   ```groovy
   implementation 'com.google.android.material:material:<version>'
   ```

   </td></table>

   Also please make sure that your app's theme should extend `Theme.MaterialComponents`.    Follow the guide on [Getting started Material Components](https://material.io/develop/android/docs/getting-started/)

   The following is the list of Material Components themes you can use to get the latest component styles and theme-level attributes.

   `Theme.MaterialComponents.NoActionBar`  </br>
   `Theme.MaterialComponents.Light.NoActionBar` </br>
   `Theme.MaterialComponents.DayNight.NoActionBar` </br>
   `Theme.MaterialComponents.DayNight.NoActionBar.Bridge` </br>

   Update your app theme to inherit from one of these themes, e.g.:

   <table><td>

   ```xml
   <style name="AppTheme" parent="Theme.MaterialComponents.Light.NoActionBar.Bridge">

       <!-- Customize your theme here. -->

       </style>
   ```

   </td></table>

   </li>
   <li> Replace **YOUR_PACKAGE_NAME** with your application package name:

   <table><td>

   ```groovy
   android {
       defaultConfig {
           manifestPlaceholders = [file_provider: "YOUR_PACKAGE_NAME"] 
           //add your application package.
       }
   }
   ```

   </td></table>

   </li>
    <li> Open the gradle.properties and check if the below stated line is present or not, if not then simply add it.

   <table><td>

   ```groovy
   android.enableJetifier=true
   ```

   </td></table>

   </li>
   </ul>

<hr/>

## Troubleshooting

- To read the full documentation on Java Pluto UI Kit integration visit our [Documentation](https://www.cometchat.com/docs/java-uikit-beta/integration)  .

- Facing any issues while integrating or installing the UI Kit please <a href="https://app.cometchat.com/"> connect with us via real time support present in CometChat Dashboard.</a>.

---


## Contributors

Thanks to the following people who have contributed to this project:

[👨‍💻 @vivekprajapati 💻](https://github.com/vivekCometChat) <br>
[👨‍💻 @darshanbhanushali 💻](https://github.com/darshanbhanushali)

---

## :mailbox: Contact

Contact us via real time support present in [CometChat Dashboard.](https://app.cometchat.com/)

---


