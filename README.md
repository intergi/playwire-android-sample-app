# playwire-android-sample-app


## app

This target is was created using the next tech stack

- Android Studio 4.0.1
- Android minSdkVersion 21
- Android targetSdkVersion 30
- Java 8 compatibility
- Kotlin 1.4.0
- PlaywireSDK
- PlaywireSDK_Amazon

1. ### app manifest

Include permissions

```xml 
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

Declare as Google Ad Manager app

```xml 
    <application>

        <meta-data
            android:name="com.google.android.gms.ads.AD_MANAGER_APP"
            android:value="true"/>

    </application>
```

2. ### app gradle

Declare Java 8 compatibility

```gradle
android {

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }

 }
```

Declare dependencies

Include Google Ad Manager dependencies
Include Amazon dependencies
Include lib folder to read aar libraries

```gradle
dependencies {
    implementation 'com.google.android.gms:play-services-ads:19.3.0'
    implementation 'com.google.code.gson:gson:2.8.6'

    implementation 'com.amazon.android:aps-sdk:8.3.2@aar'

    implementation fileTree(include: ['*.jar', '*.aar'], dir: 'libs')

}
```

Copy Playwire libraries into libs folder

PlaywireSDK-release.aar
PlaywireSDK_Amazon-release.aar

3. ### Create PlaywireConfig.json file

In assets folder

```json
{
    "serverConfigs":
    [
        {
            "name": "PWAmazon",
            "serverType": "Amazon",
            "account": "b1f05586-a4dd-4d23-95d4-a605466437b8",
            "useGeo": true,
            "isTest": true
        }
    ],
    "adUnits":
    [
        {
            "name": "300x250 - Amazon",
            "mode": "Banner",
            "gadUnitId": "/154013155/1024308/72818/1024308-72818-medium_rectangle",
            "gadSizes": [{"width": 300, "height": 250}],
            "adUnitConfigs":
            [
                {
                    "serverConfig": "PWAmazon",
                    "adUnitId": "81ef3b9c-90ff-4e53-8250-acdbce2c344c",
                    "adSizes": [{"width": 300, "height": 250}]
                }
            ]
        }
    ]
}
```

4. ### MainActivity


```kotlin
package com.intergi.playwiresdkapps

// Import PlaywireSDK needed
import com.intergi.playwiresdk.PWAdSlot
import com.intergi.playwiresdk.PlaywireSDK
import com.intergi.playwiresdk_amazon.PWAdBidder_Amazon

class MainActivity : AppCompatActivity() {

	// Define Ad Slot
    var adSlot : PWAdSlot? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configure PlaywireSDK
        PlaywireSDK.loadFromAssetFile(this, "PWStoreConfig.json")
        // Register Amazon Ad Bidder in Playwire SDK
        PWAdBidder_Amazon.register(this)

        // Load Ad Slot
        val adUnitName = "300x250 - Amazon"
        adSlot = PWAdSlot(adUnitName)
        adSlot!!.load {

        	// on slot loade create Publisher Ad View
            var ad_view = PublisherAdView(this)

            // Ad view to Activity view tree
            // ...

            // Use Playwire Helper to configure the view
            PWAdBannerViewHelper.loadView(adSlot!!, ad_view)
        }

    }
}}
```

