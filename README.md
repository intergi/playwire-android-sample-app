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
and make available the app-id in google ad manager to the UMP (User Messaging Platform)

```xml 
    <application>

        <meta-data
            android:name="com.google.android.gms.ads.AD_MANAGER_APP"
            android:value="true"/>

        <meta-data
            android:name="com.google.android.gms.ads.APPLICATION_ID"
            android:value="ca-app-pub-6531503260671471~3119413040"/>

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

    implementation fileTree(include: ['*.jar', '*.aar'], dir: 'libs')
    implementation 'com.google.android.gms:play-services-ads:19.3.0'
    implementation 'com.google.android.ump:user-messaging-platform:1.0.0'
    implementation 'com.amazon.android:aps-sdk:8.3.2@aar'
}
```

Copy Playwire libraries into libs folder

PlaywireSDK-1.0.3-release.aar
PlaywireSDK_Amazon-1.0.3-release.aar

3. ### Create PlaywireConfig.json file

In assets folder

```json
{
  "serverConfigs":
  [
    {
      "name": "PWAmazon",
      "serverType": "Amazon",
      "account": "555c965d-7e48-4960-a8b1-f730ef9eb000",
      "useGeo": true,
      "isTest": true
    }
  ],
  "adUnits":
  [
    {
      "name": "300x250 - Amazon",
      "mode": "Banner",
      "gadUnitId": "/154013155,1082185/1024308/72734/1024308-72734-medium_rectangle/1024308-72734-medium_rectangle-CP/1024308-72734-medium_rectangle-CP-in-article",
      "gadSizes": [{"width": 300, "height": 250}, {"width": 320, "height": 50}],
      "adUnitConfigs":
      [
        {
          "serverConfig": "PWAmazon",
          "adUnitId": "666dcfe0-d023-4c2f-86df-4b692d36d18e",
          "adSizes": [{"width": 300, "height": 250}]
        }
      ]
    }
  ]
}
```

4. ### MainActivity


```kotlin
    // we only are able to ask for an ad only after users are given their consent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureAdManager() {showAd()}
    }
    
    private fun configureAdManager(onReady: ()->Unit) {
        // load configuration from json file
        PlaywireSDK.loadFromAssetFile(this, "PWStoreConfig.json")
        // use amazon header bidding
        PWAdBidder_Amazon.register(this)

        // in debug pretend user are in EU
        if (BuildConfig.DEBUG) {
            val debugBuilder = PWUMPDebug.PWUMPDebugBuilder(this)
                .resettingInfo()
                .forcingEEALocation()
                .addTestDeviceHashedId("26F4F73131B7FBDD640FC59E5A4DA646")

            PlaywireSDK.umpManager.debug = debugBuilder.build()
        }

        // request user consent with a helper into the sdk
        // result is async
        PlaywireSDK.umpManager.requestConsent(this, {
            onReady()
        })

    }

    private fun showAd(){
        // create adslot given the name
        val adUnitName = "300x250 - Amazon"
        adSlot = PWAdSlot(adUnitName)

        // prebid and create view after prebid result
        adSlot!!.load {
            var ad_view = PublisherAdView(this)
            addAdView(ad_view)
            PWAdBannerViewHelper.loadView(adSlot!!, ad_view)
        }

    }
```

