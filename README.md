# playwire-android-sample-app

This project is was created using the next tech stack:

- Android minSdkVersion 21
- Android targetSdkVersion 32
- Java 8 compatibility
- Kotlin 1.7.10

### Setup Github Gradle registry access

As the `Playwire Android SDK` is distributed via the remote GitHub Gradle registry, even though such SDK is accessible publicly there, GitHub still requires to do authentication.
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

### Build variants

Pay attention to build variants of this project. The **COPPA** and **Total** flavors are used to install the corresponding Playwire dependency. 
Select required one based on your needs.