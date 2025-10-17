# playwire-android-sample-app

This workspace has 3 schemes that demonstrate the usage of PlaywireSDK in three different scenarios:

- A Kotlin Android app
- A Java Android app
- A Jetpack Compose Kotlin Android app 

### Setup Github Gradle registry access

`Playwire Android SDK` is currently distributed via a remote GitHub Gradle registry.
Playwire SDK is accessible publicly there but GitHub still requires authentication to pull the SDK.

See the official [GitHub Gradle registry's guide](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry) to get more details about Github authentication.

  1. You have to create a personal access token(PAT) to synchronize gradle without errors. See the official [GitHub PAT creation guide](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token) to create token.

  2. Open `settings.gradle` and replace placeholders with your credentials.

    ```gradle
    maven {
        name = "GitHubPackages"
        url = 'https://maven.pkg.github.com/intergi/playwire-android-binaries'
        credentials {
            username = "YOUR_GITHUB_USERNAME"
            password = "YOUR_GITHUB_PERSONAL_ACCESS_TOKEN"
        }
    }  
    ```

  3. Open project and run `Sync project with gradle files` command.

### Run demo app

Select required demo app configuration and run.
