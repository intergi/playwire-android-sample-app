# Playwire SDK
###### COPPA Version 8.1.0 - Android

> **COPPA applications** have a reduced set of networks providing ads for children. The documentation below is only to be used on COPPA applications.

## Tech Stack
- Android Studio 4.0.1
- Android minSdkVersion 21
- Android targetSdkVersion 31
- Java 8 compatibility
- Kotlin 1.6.0

## Contents
- [Project Configuration](#project-configuration)
  - [App Manifest file](#app-manifest-file)
    - [Playwire Mobile CLI tool](#playwire-mobile-cli-tool)
    - [Manual Configuration](#manual-configuration)
  - [Gradle file](#gradle-file)
    - [Repositories](#repositories)
    - [Dependencies](#dependencies)
    - [Declare Java 8 compatibility](#declare-java-8-compatibility)
- [Usage](#usage)
  - [Initialization](#initialization)
  - [Firebase Initialization](#firebase-initialization)
  - [Test Ads](#test-ads)
  - [View Ads](#view-ads)
    - [Banner](#banner)
    - [Adaptive Anchored Banner](#adaptive-anchored-banner)
    - [Adaptive Inline Banner](#adaptive-inline-banner)
    - [Native Ad](#native-ad)
    - [View Ad Listener](#view-ad-listener)
  - [Fullscreen Ads](#full-screen-ads)
    - [Interstitial](#interstitial)
    - [Rewarded](#rewarded)
    - [Rewarded Interstitial](#rewarded-interstitial)
    - [App Open Ad](#app-open-ad)
    - [Full Screen Ad Listener](#full-screen-ad-listener)
  - [Migrate to SDK 6.0.0+](#migrate-to-sdk-600)
    - [SDK initialization](#sdk-initialization)
    - [Partner registration](#partner-registration)
  - [Migrate to SDK 8.1.0+](#migrate-to-sdk-810)
    - [iab/GDPR/CMP/TCF](#iabgdprcmptcf)
  - [Debugging and Analytics](#debugging-and-analytics)
    - [Registering a Listener into the Notifier](#registering-a-listener-into-the-notifier)
    - [Console Logger](#console-logger)
  - [Additional steps on Header Bidding and Mediation](#additional-steps-on-header-bidding-and-mediation)
    - [Chartboost](#chartboost)
      - [Manifest file](#manifest-file-1)
      - [Lifecycle events](#lifecycle-events)
    - [IronSource](#ironsource)
      - [Application lifecycle](#application-lifecycle)
    - [Vungle](#vungle)
      - [Known issues](#known-issues)

## Project Configuration

### App Manifest file

You have 2 options to configure the AndroidManifest.xml file with mandatory values: manually or using the Playwire Mobile CLI tool to run commands via terminal. Select which one is more preferable for you and follow the instructions.

#### Playwire Mobile CLI tool

- See the instructions [here](https://docs.google.com/document/d/1xeAOWQWOrZXp22aYa5yf7s1J-60UZBhPxPjvQ43vPwM/edit#heading=h.7i983aw6sqxo) to install our Playwire Mobile CLI tool to your working machine.
- Playwire will provide you **publisher_id** and **app_id**, along with a config file. Use [the command](https://docs.google.com/document/d/1xeAOWQWOrZXp22aYa5yf7s1J-60UZBhPxPjvQ43vPwM/edit#heading=h.9f23w41631or) to get **com.google.android.gms.ads.APPLICATION_ID** and update your AndroidManifest.xml with this value.

> **Note**: In case any issues with the Playwire Mobile CLI tool contact your Playwire Account Manager to resolve issues or follow the Manual Configuration section below.

#### Manual Configuration 
Declare as Google Ad Manager app and make available the app-id in google ad manager to the UMP (User Messaging Platform).

```xml
<!--required-->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />

<!--recommended by AdColony -->
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.VIBRATE" />

<application>

    <!--required by Google -->
    <meta-data
        android:name="com.google.android.gms.ads.AD_MANAGER_APP"
        android:value="true"/>
    <meta-data
        android:name="com.google.android.gms.ads.APPLICATION_ID"
        android:value="YOUR_GOOGLE_APP_ID"/>

</application>
```

### Gradle file
#### Repositories

**Please pay attention that the Android SDK is consumed  from the remote GitHub Packages repository. Even though the SDK repository is accessible publicly there, GitHub still requires authentication.** 

You can visit the [official GitHub Package's guide](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry) to check how to do authentication.  

Below is the code to be added to an app for authentication, with your specific Github credentials.

```groovy
repositories {
    mavenCentral()
    google()
    jcenter()
    maven {
       name = "GitHubPackages"
       url = 'https://maven.pkg.github.com/intergi/playwire-android-binaries'
       credentials {
          username = "USERNAME"
          password = "TOKEN"
       }
    }
    maven {
       // ironsource
       url 'https://android-sdk.is.com/'
    }
    maven {
       // pangle
       url 'https://artifact.bytedance.com/repository/pangle/'
    }
}
```

#### Dependencies

> **COPPA version** (**ONLY** to be used for **COPPA applications** as it has a reduced set of networks providing ads for children.)

```groovy
dependencies {
    // kotlin needed if your app is only java
    compile 'org.jetbrains.kotlin:kotlin-stdlib:1.3.21'
    // Playwire Total version
    implementation 'com.intergi.playwire:playwiresdk_coppa:11.5.2'
    implementation 'com.google.firebase:firebase-analytics-ktx:21.1.2'
}
```

#### Declare Java 8 compatibility

```groovy
android {
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
 }
```

## Usage
### Initialization
Initialization must be done from an Activity and using the main thread. This must be done during the early stage of your app life cycle. 

Search for the initialization metadata (**publisherId** and **appId**) emailed by your Playwire Account Manager.

Success Handler only will be called on initialization success and that means you can start requesting ads

###### Java
[Initialization](https://github.com/intergi/playwire-android-sample-app/blob/0790756aaf65b1a1f6d5a1fca7b3d5654cdb3e14/demo-java/src/main/java/com/playwire/demo_java/adtypes/AdTypesActivity.java#L52-L54)
###### Kotlin
[Initialization](https://github.com/intergi/playwire-android-sample-app/blob/0790756aaf65b1a1f6d5a1fca7b3d5654cdb3e14/demo-kotlin/src/main/java/com/playwire/demo_kotlin/adtypes/AdTypesActivity.kt#L39-L41)

### Firebase Initialization
If you want to integrate Firebase into the project, see the [Android Firebase guide](https://firebase.google.com/docs/android/setup) to complete the integration properly. Make sure the project contains **google-services.json** and required classpath in the project’s **build.gradle** file.

```groovy
buildscript {
    // ...
    dependencies {
      // ...
      classpath 'com.google.gms:google-services:4.3.14'
    }
    // ...
}
```

### Test Ads
In order to get **test** ads, the test property needs to be set to **true**. This can be done before the initialization. The recommended way would be to set the **test** to **true** for **debug** builds only.  For banners, interstitials and app open ads, test mode will show a blue ad. For video ads, i.e - rewarded and rewarded interstitials, test mode will show an actual video instead of a blue ad.

###### Java
[Test Ads](https://github.com/intergi/playwire-android-sample-app/blob/931f6d858f99425b895721bc8585296314458628/demo-java/src/main/java/com/playwire/demo_java/adtypes/AdTypesActivity.java#L89-L90)
###### Kotlin
[Test Ads](https://github.com/intergi/playwire-android-sample-app/blob/931f6d858f99425b895721bc8585296314458628/demo-kotlin/src/main/java/com/playwire/demo_kotlin/adtypes/AdTypesActivity.kt#L72-L73)

### View Ads
#### Banner

Banner ads are located in a spot within an app's layout, either at the top or bottom of the screen. Banners may stay on screen while a user is interacting with the app, and can be refreshed automatically or manually on a specific condition.

To show banner ads 4 sequential steps should be completed:

1. Instantiation
2. Configuration
3. Loading
4. Showing

These steps can be completely done using Layout Files but you can decide to make them through code.

[PWViewAd.Listener](#view-ad-listener) provides methods to inform you about a view ad lifecycle. You can implement it to be notified about events regarding loading and ad content presentation status. See [PWViewAd.Listener](#view-ad-listener) for more details.

To perform a banner cleanup and avoid memory leaks use **destroy()** method. This method should be called in the parent Activity's [onDestroy()](https://developer.android.com/reference/android/app/Activity.html#onDestroy\(\)) method. No other methods should be called on the banner view after destroy() is called.

###### Layout File

Instantiation: Include a PWBAnnerView in the layout file and configure custom parameters:

**ad_unit_name**: it’s the name in the config file identifying your banner. You can set this property or wait to be done by code.

**autoload**: This is a boolean property that configures this banner to be loaded as soon your view is instantiated. If you don’t define this property or if you set this property to false, you must load your banner using code. You don’t have to set this to true if the Ad Unit Name is not configured. It will throw an error.

###### Java
[Banner Layout File](https://github.com/intergi/playwire-android-sample-app/blob/playwiresdk/8.1.0/demo-java/src/main/res/layout/activity_banner_layout.xml)
[Banner Implementation via Layout File](https://github.com/intergi/playwire-android-sample-app/blob/playwiresdk/8.1.0/demo-java/src/main/java/com/playwire/demo_java/ads/view/banner/BannerLayoutActivity.java)
###### Kotlin
[Banner Layout File](https://github.com/intergi/playwire-android-sample-app/blob/playwiresdk/8.1.0/demo-kotlin/src/main/res/layout/activity_banner_layout.xml)
[Banner Implementation via Layout File](https://github.com/intergi/playwire-android-sample-app/blob/playwiresdk/8.1.0/demo-kotlin/src/main/java/com/playwire/demo_kotlin/ads/view/banner/BannerLayoutActivity.kt)

###### Source Code

Also you can make banners through code. See examples below how to configure a banner programmatically.

###### Java
[Banner Implementation](https://github.com/intergi/playwire-android-sample-app/blob/playwiresdk/8.1.0/demo-java/src/main/java/com/playwire/demo_java/ads/view/banner/BannerActivity.java)
###### Kotlin
[Banner Implementation](https://github.com/intergi/playwire-android-sample-app/blob/playwiresdk/8.1.0/demo-kotlin/src/main/java/com/playwire/demo_kotlin/ads/view/banner/BannerActivity.kt)

##### Refresh
Depending on the banner configuration, it can be either automatically or manually refreshed or both. If a banner can be manually refreshed: you can fire a refresh in the banner as described below.

###### Java
[Refresh Implementation](https://github.com/intergi/playwire-android-sample-app/blob/0790756aaf65b1a1f6d5a1fca7b3d5654cdb3e14/demo-java/src/main/java/com/playwire/demo_java/ads/view/banner/BannerActivity.java#L103-L106)
###### Kotlin
[Refresh Implementation](https://github.com/intergi/playwire-android-sample-app/blob/0790756aaf65b1a1f6d5a1fca7b3d5654cdb3e14/demo-kotlin/src/main/java/com/playwire/demo_kotlin/ads/view/banner/BannerActivity.kt#L89-L93)

##### Adaptive Anchored Banner

Adaptive banners are the next generation of responsive ads, maximizing performance by optimizing ad size for each device. Improving on smart banners, which only supported fixed heights, adaptive banners let developers specify the ad-width and use this to determine the optimal ad size.

Adaptive banners are designed to be a drop-in replacement for the industry standard 320x50 banner size.

These banner sizes are commonly used as anchored banners, which are usually locked to the top or bottom of the screen. For such anchored banners, the aspect ratio when using adaptive banners will be similar to that of a standard 320x50.

As a difference with [standard banners](#banner), at load time the actual width of the container view is taken into account. To do this you can either add the banner to the parent view before loading or send the required width as part of the load parameters.

###### Java
[Anchored Banner Implementation](https://github.com/intergi/playwire-android-sample-app/blob/playwiresdk/8.1.0/demo-java/src/main/java/com/playwire/demo_java/ads/view/banner/AnchoredBannerActivity.java)
###### Kotlin
[Anchored Banner Implementation](https://github.com/intergi/playwire-android-sample-app/blob/playwiresdk/8.1.0/demo-kotlin/src/main/java/com/playwire/demo_kotlin/ads/view/banner/AnchoredBannerActivity.kt)

##### Adaptive Inline Banner
Inline adaptive banners are larger, taller banners compared to anchored adaptive banners. They are of variable height, and can be as tall as the device screen.

They are intended to be placed in scrolling content.

In addition to the required width you are required to define the device orientation at loading time. The current device orientation is used as a default in case you don’t define one.

###### Java
[Inline Banner Implementation](https://github.com/intergi/playwire-android-sample-app/blob/playwiresdk/8.1.0/demo-java/src/main/java/com/playwire/demo_java/ads/view/banner/InlineBannerActivity.java)

###### Kotlin
[Inline Banner Implementation](https://github.com/intergi/playwire-android-sample-app/blob/playwiresdk/8.1.0/demo-kotlin/src/main/java/com/playwire/demo_kotlin/ads/view/banner/InlineBannerActivity.kt)

#### Native Ad

Native ads are ad assets that are presented to users via UI components that are native to the platform. They're shown using the same classes you already use in your layout files, and can be formatted to match your app's visual design.

You have to create a View to display the ad content and configure this view with the ad data that is returned after the ad loading. You can create this view either programmatically or using layout editor.

The **content view** is the main view containing all views that will display the ad data.

The **action view** is a special view that will redirect users to the ad related information when it’s clicked. It’s usually a button but you can decide as SDK only requires it to be a generic view.

The **media view** holder is a generic view used to hold the media content of the ad. It can be simply an image or it can be a video.

You can use any kind of view to display the ad content, you will be in charge of configuring the view based on the content when the ad is loaded. As an example, the star rating is usually a number between 0 and 5 and it is usually presented as a view with actual stars filled depending on the attribute value.

In order to interact with SDK to properly show the native ad you have to implement two Interfaces.

[PWViewAd.Listener](#view-ad-listener) provides methods to inform you about a view ad lifecycle. You can implement it to be notified about events regarding loading and ad content presentation status. See [PWViewAd.Listener](#view-ad-listener) for more details.

PWNativeViewFactory interface provides methods being used by the SDK to get the view that has been configured using the ad content given by the SDK. A special method in this protocol is used to specify which view in the ad view is the action view that will be used to handle the user action.

###### Java
[Custom Native Ad View](https://github.com/intergi/playwire-android-sample-app/blob/playwiresdk/8.1.0/demo-java/src/main/java/com/playwire/demo_java/ads/view/nativead/NativeView.java)

[Native Ad Implementation](https://github.com/intergi/playwire-android-sample-app/blob/playwiresdk/8.1.0/demo-java/src/main/java/com/playwire/demo_java/ads/view/nativead/NativeAdActivity.java)
###### Kotlin
[Custom Native Ad View](https://github.com/intergi/playwire-android-sample-app/blob/playwiresdk/8.1.0/demo-kotlin/src/main/java/com/playwire/demo_kotlin/ads/view/nativead/NativeView.kt)

[Native Ad Implementation](https://github.com/intergi/playwire-android-sample-app/blob/playwiresdk/8.1.0/demo-kotlin/src/main/java/com/playwire/demo_kotlin/ads/view/nativead/NativeAdActivity.kt)

#### View Ad Listener

In order to receive events of view ads lifecycle, you have to implement the **PWViewAd.Listener** and pass it during initialization of the selected view ad unit. The **PWViewAd.Listener** handles callbacks when the ad content is loaded successfully or not, if the ad destination is presented or ad content is clicked.

###### Java
```java
import com.intergi.playwiresdk.ads.view.PWViewAd;
import androidx.annotation.NonNull;
// ...
PWViewAd.Listener listener = new PWViewAd.Listener() {
    @Override
    public void onViewAdLoaded(@NonNull PWViewAd ad) {
      // add ad_view to Activity's main view
    }
    @Override
    public void onViewAdFailedToLoad(@NonNull PWViewAd ad) {}
    @Override
    public void onViewAdOpened(@NonNull PWViewAd ad) {}
    @Override
    public void onViewAdClosed(@NonNull PWViewAd ad) {}
    @Override
    public void onViewAdClicked(@NonNull PWViewAd ad) {}
    @Override
    public void onViewAdImpression(@NonNull PWViewAd ad) {}
};
// ...
```

###### Kotlin
```kotlin
import com.intergi.playwiresdk.ads.view.PWViewAd
// ...
val listener = object: PWViewAd.Listener {
    override fun onViewAdLoaded(ad: PWViewAd) {
      // add ad_view to Activity's main view
    }
    override fun onViewAdFailedToLoad(ad: PWViewAd) {}
    override fun onViewAdOpened(ad: PWViewAd) {}
    override fun onViewAdClosed(ad: PWViewAd) {}
    override fun onViewAdClicked(ad: PWViewAd) {}
    override fun onViewAdImpression(ad: PWViewAd) {}
}
// ... 
```

### Full Screen Ads
#### Interstitial

Interstitial ad is a full-screen ad that covers the interface of an app until closed by a user.

Once an interstitial ad is presented, a user may redirect to ad destination or close and back to the application.

To display an interstitial ad on your app, you must first request it and provide the ad unit.

When requesting  an interstitial ad, we recommend that you do so in advance before planning to present it to your user as the loading process may take time.

[FullScreenAd.Listener](#full-screen-ad-listener) provides methods to inform you about an interstitial ad lifecycle. You have to pass during ad initialization to be notified about events regarding loading and presentation status. See [FullScreenAd.Listener](#full-screen-ad-listener) for more details.

###### Java
[Interstitial Implementation](https://github.com/intergi/playwire-android-sample-app/blob/playwiresdk/8.1.0/demo-java/src/main/java/com/playwire/demo_java/ads/fullscreen/interstitial/InterstitialActivity.java)
###### Kotlin
[Interstitial Implementation](https://github.com/intergi/playwire-android-sample-app/blob/playwiresdk/8.1.0/demo-kotlin/src/main/java/com/playwire/demo_kotlin/ads/fullscreen/interstitial/InterstitialActivity.kt)

> **Note**: An interstitial ad is a one-time-use object, which means it must be initialized and loaded again after its presentation.

#### Rewarded
Rewarded ad is a full-screen ad that covers the interface of an app until closed by a user. It is used to give the option for a user to earn some in-app rewards, which are configured during ad unit creation. A user is given the option to watch a video ad or view a display ad to receive an in-app reward, e.g. a new level or an extra life in a game, access to premium content, etc.

To display a rewarded ad on your app, you must first request it and provide the ad unit.

When requesting a rewarded ad, we recommend that you do so in advance before planning to present it to your user as the loading process may take time.

[FullScreenAd.Listener](#full-screen-ad-listener) provides methods to inform you about a rewarded ad lifecycle. You have to pass during ad initialization to be notified about events regarding loading and presentation status. See [FullScreenAd.Listener](#full-screen-ad-listener) for more details.
###### Java
[Rewarded Implementation](https://github.com/intergi/playwire-android-sample-app/blob/playwiresdk/8.1.0/demo-java/src/main/java/com/playwire/demo_java/ads/fullscreen/rewarded/RewardedActivity.java)
###### Kotlin
[Rewarded Implementation](https://github.com/intergi/playwire-android-sample-app/blob/playwiresdk/8.1.0/demo-kotlin/src/main/java/com/playwire/demo_kotlin/ads/fullscreen/rewarded/RewardedActivity.kt)

> **Note**: A rewarded ad is a one-time-use object, which means it must be initialized and loaded again after its presentation.

#### Rewarded Interstitial

Rewarded interstitial is a full-screen ad that covers the interface of an app until closed by a user. This type of ad format allows you to offer in-app rewards for ads that can appear automatically during app transitions. Rewards may be represented as any in-app values, e.g. a new level or an extra life in a game, access to premium content, etc.

To display a rewarded interstitial ad on your app, you must first request it and provide the ad unit.

When requesting a rewarded interstitial ad, we recommend that you do so in advance before planning to present it to your user as the loading process may take time.

[FullScreenAd.Listener](#full-screen-ad-listener) provides methods to inform you about a rewarded interstitial ad lifecycle. You can implement it to be notified about events regarding loading and presentation status. See [FullScreenAd.Listener](#full-screen-ad-listener) for more details.

###### Java
[Rewarded Interstitial Implementation](https://github.com/intergi/playwire-android-sample-app/blob/playwiresdk/8.1.0/demo-java/src/main/java/com/playwire/demo_java/ads/fullscreen/rewardedinterstitial/RewardedInterstitialActivity.java)
###### Kotlin
[Rewarded Interstitial Implementation](https://github.com/intergi/playwire-android-sample-app/blob/playwiresdk/8.1.0/demo-kotlin/src/main/java/com/playwire/demo_kotlin/ads/fullscreen/rewardedinterstitial/RewardedInterstitialActivity.kt)
> **Note**: A rewarded interstitial ad is a one-time-use object, which means it must be initialized and loaded again after its presentation.

#### App Open Ad

App open ads are a special ad format intended for publishers wishing to monetize their app load screens. This format of ads can be shown when a user brings the app to the foreground.

To request an app open ad you have to make initialization first.

When your app displays an app open ad, you should rely on the [FullScreenAd.Listener](#full-screen-ad-listener) to handle certain presentation events. In particular, you’ll want to request the next app open ad once the first one finishes presenting.

An app open ad will time out after four hours. If you present an ad content that was requested for more than four hours, it will no longer be valid and may not earn revenue.

To ensure you do not show an expired ad, you can check how long it has been since your ad loaded and reload it manually, or you may enable **autoReloadOnExpiration** to let the PlaywireSDK monitor the ad expiration and take care about reloading the expired ad. It is disabled by default.

As app open ads are designed to be shown when a user brings your app to the foreground, you need to listen to the application state changes. First, edit your application-level **build.gradle** file to include the LifecycleObserver libraries.

Then by registering your LifecycleEventObserver, your app will be alerted to app launch and foregrounding events and be able to show the ad at the appropriate times.

###### Java
[App Open Ad Implementation](https://github.com/intergi/playwire-android-sample-app/blob/playwiresdk/8.1.0/demo-java/src/main/java/com/playwire/demo_java/ads/fullscreen/appopenad/AppOpenAdActivity.java)
###### Kotlin
[App Open Ad Implementation](https://github.com/intergi/playwire-android-sample-app/blob/playwiresdk/8.1.0/demo-kotlin/src/main/java/com/playwire/demo_kotlin/ads/fullscreen/appopenad/AppOpenAdActivity.kt)

> **Note**: An app open ad is a one-time-use object, which means it must be initialized and loaded again after its presentation.

#### Full Screen Ad Listener

In order to receive events of full screen ads lifecycle, you have to implement the **FullScreenAd.Listener** and pass it during initialization of the selected full screen ad unit. The **FullScreenAd.Listener** handles callbacks when the ad content is loaded successfully or not, if the ad is presented, when it is dismissed and a reward-compatible ads earned a reward.

###### Java
```java
import com.intergi.playwiresdk.ads.fullscreen.PWFullScreenAd;
import androidx.annotation.NonNull;
// ...
PWFullScreenAd.Listener listener = new PWFullScreenAd.Listener() {
    @Override
    public void onFullScreenAdLoaded(@NonNull PWFullScreenAd ad) {}
    @Override
    public void onFullScreenAdFailedToLoad(@NonNull PWFullScreenAd ad) {}
    @Override
    public void onFullScreenAdShowedFullScreenContent(@NonNull PWFullScreenAd ad) {}
    @Override
    public void onFullScreenAdFailedToShowFullScreenContent(@NonNull PWFullScreenAd ad) {}
    @Override
    public void onFullScreenAdDismissedFullScreenContent(@NonNull PWFullScreenAd ad) {}
    @Override
    public void onFullScreenAdImpression(@NonNull PWFullScreenAd ad) {}
    @Override
    public void onFullScreenAdReward(@NonNull PWFullScreenAd ad, @NonNull String type, int amount) {}
});
// ...
```

###### Kotlin
```kotlin
import com.intergi.playwiresdk.ads.fullscreen.PWFullScreenAd
// ...
val listener = object: PWFullScreenAd.Listener {
    override fun onFullScreenAdLoaded(ad: PWFullScreenAd) {}
    override fun onFullScreenAdFailedToLoad(ad: PWFullScreenAd) {}
    override fun onFullScreenAdShowedFullScreenContent(ad: PWFullScreenAd) {}
    override fun onFullScreenAdFailedToShowFullScreenContent(ad: PWFullScreenAd) {}
    override fun onFullScreenAdDismissedFullScreenContent(ad: PWFullScreenAd) {}
    override fun onFullScreenAdImpression(ad: PWFullScreenAd) {}
    override fun onFullScreenAdReward(ad: PWFullScreenAd, type: String, amount: Int) {}
}
// ...
```

### Migrate to SDK 6.0.0+
The Playwire SDK version 6.0.0 contains a few major changes in public APIs. Follow this section to check what has been changed.
#### SDK initialization
- No version is required in SDK initialization. SDK will get the right version from the backend
- Initialization callback will only be called on success to start requesting ads.

#### Partner registration
SDK will automatically register for you all available header bidders and mediation partners

### Migrate to SDK 8.1.0+
Loading-time parameters have been added. This has been reflected in the Adaptive Banners section.
#### iab/GDPR/CMP/TCF
To comply with the [General Data Protection Regulation (GDPR) of the Interactive Advertising Bureau (iab)](https://www.iab.com/topics/privacy/gdpr/), PlaywireSDK uses a Consent Management Platform (CMP) compatible with the iab [Transparence and Consent Framework (TCF)](https://github.com/InteractiveAdvertisingBureau/GDPR-Transparency-and-Consent-Framework/tree/master/TCFv2)

If you want to use your own CMP, in order to avoid colliding with the CMP used by PlaywireSDK, it has to be compatible with TCF.
You have to launch your CMP and wait for the user response before you initialize our SDK.

### Debugging and Analytics

For debugging and analytics purposes, Playwire SDK offers a notifier that acts as a broadcaster of events. Diverse modules within the SDK send events to the notifier, which then broadcasts those events to all registered listeners.

#### Registering a Listener into the Notifier
A publisher can register a custom-made listener to handle events with custom actions as it’s explained below.

###### Java
[Event Listener](https://github.com/intergi/playwire-android-sample-app/blob/931f6d858f99425b895721bc8585296314458628/demo-java/src/main/java/com/playwire/demo_java/adtypes/AdTypesActivity.java#L77-L87)
[Event Listener Cancellation](https://github.com/intergi/playwire-android-sample-app/blob/931f6d858f99425b895721bc8585296314458628/demo-java/src/main/java/com/playwire/demo_java/adtypes/AdTypesActivity.java#L45-L49)

###### Kotlin
[Event Listener](https://github.com/intergi/playwire-android-sample-app/blob/931f6d858f99425b895721bc8585296314458628/demo-kotlin/src/main/java/com/playwire/demo_kotlin/adtypes/AdTypesActivity.kt#L58-L70)
[Event Listener Cancellation](https://github.com/intergi/playwire-android-sample-app/blob/931f6d858f99425b895721bc8585296314458628/demo-kotlin/src/main/java/com/playwire/demo_kotlin/adtypes/AdTypesActivity.kt#L30-L32)

In the examples above:
- **addListener**: adds a new listener into the notifier;
- **self**: is the actual listener; whenever self is cleaned from memory, listener will be cleaned too;
- **filter**: the block of code to select which events will be addressed by the action block, based on event name, critical status and event context;
- **action**: handles the event data.

#### Console Logger
A default listener is available to log events to the console. It can be launched on the notifier for all events or passing a filter as a parameter to select what events should be logged.

###### Java
[Console Logger](https://github.com/intergi/playwire-android-sample-app/blob/931f6d858f99425b895721bc8585296314458628/demo-java/src/main/java/com/playwire/demo_java/adtypes/AdTypesActivity.java#L62-L75)

###### Kotlin
[Console Logger](https://github.com/intergi/playwire-android-sample-app/blob/931f6d858f99425b895721bc8585296314458628/demo-kotlin/src/main/java/com/playwire/demo_kotlin/adtypes/AdTypesActivity.kt#L43-L56)

### Additional steps on Header Bidding and Mediation

### Chartboost
#### Manifest file
**Required:** Add the following attribute to each activity that will be showing Chartboost ads and supports different orientations:

```xml
android:configChanges="keyboardHidden|orientation|screenSize"
```

#### Lifecycle events
**Recommended:** Per Chartboost's recommendation, add the following code to all of your activity's lifecycle events that will be showing Chartboost ads.

```kotlin
import com.chartboost.sdk.Chartboost

@Override
fun onBackPressed() {
    // If an interstitial is on screen, close it.
    if (Chartboost.onBackPressed())
        return
    else
        super.onBackPressed()
}
```

### IronSource
#### Application lifecycle
Override the **onPause()** and **onResume()** methods in each of your activities to call the corresponding ironSource methods as follows:

```kotlin
public override fun onResume() {
    super.onResume()
    IronSource.onResume(this)
}

public override fun onPause() {
    super.onPause()
    IronSource.onPause(this)
}
```

### Vungle
#### Known issues
If you need *WRITE_EXTERNAL_STORAGE* permissions

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
   xmlns:tools="http://schemas.android.com/tools"
   package="...">

<!--    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>-->
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
   tools:node="remove"
   tools:selector="com.vungle.warren">
```