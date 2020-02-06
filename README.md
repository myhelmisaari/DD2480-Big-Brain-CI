# DD2480-CI
CI is the second assignment in the course DD2480 Software Engineering Fundamentals. The goal of the assignment is to implement a continuous integration server that first clones a repository on GitHub when there are any changes pushed on it, builds the project, runs the tests in the project and notifies the user of the results. 

## Getting Started
This implementation of a CI-server is made with Java SDK 11 and IntelliJ; therefore make sure you have these installed. The project that uses the CI-server needs to have `Gradle` installed, due to it being used by the CI-server. There must exist a `build.gradle` file in the project containing the dependencies needed for the project to build and run.

### Branches
There are two branches: `master` and `assessment`. The master branch contains the CI-server. The assessment branch is simulating a project with a main class and a simple test for it. The purpose of the assessment branch is to use it to test the functionality of the CI-server. 

## Testing and building locally
Tests for the CI-server are written using the `JUnit` library. If you want to run the tests locally, there are two ways.
* **Using the terminal:** first navigate to the the project folder, then use the command `./gradlew test`. To build the project locally use the command `./gradlew build`.
* **Using IntelliJ:** Open the project in IntelliJ. Right-click on the src-folder and choose `Run 'Tests in DD2480-Big-Brain-CI.test'`. To build the project right-click on the src-folder and choose  `Build module DD2480-Big-Brain-CI`.
If the build was successful there will be a new directory in the project named 'Build' that contains the generated classes. If it does not succeed the directory will not contain any generated classes.

## Managing the CI-server
### Deploy the server
Using [ngrok](https://ngrok.com/) we can make the server visible on the internet. 
If you don't have it installed write the following in the terminal:

```
#For linux users: 
curl -LO --tlsv1 https://bin.equinox.io/c/4VmDzA7iaHb/ngrok-stable-linux-amd64.zip
unzip ngrok-stable-linux-amd64.zip 
#For Mac users:
curl -LO --tlsv1 https://bin.equinox.io/c/4VmDzA7iaHb/ngrok-stable-darwin-386.zip
unzip ngrok-stable-darwin-386.zip
```

All you have to do is open a new terminal window and use the command `./ngrok http 8083`. The ngrok service provides a forwarding URL that works for 8 hours.

After this you have to set up a Webhook. This can be done on the Github repository online.
1. Go to `Settings >> Webhooks`, click on `Add webhook`.
1. Paste the forwarding URL that `ngrok` provides (for example `https://7al9b010.ngrok.io`) in the payload field and proceed to click `Add webhook`. Nothing else is required than the URL.
Now the CI-server should be ready to be used. 

### Using the CI-server
To test the CI-server there is a specific branch prepared. Follow the following steps in the terminal to proceed. These steps assumes you have already cloned the repository.
1. Go to the assessment branch using the command `git checkout -b assessment origin/assessment`
1. Make a change somewhere
1. Commit and push the changes to `GitHub`
1. The CI-server will do its part and notify about the results of the build and tests using Slack

## When the server is activated by the Webhook
1. Clones the project using `JGit`and saves it to a directory named 'assessment'.
1. Builds the cloned project and runs the tests defined in the project. This is done by executing the commands `./gradlew build` and `./gradlew test`.
1. The results of the build and tests are sent via a notification on Slack. This is done using the `XML-file` that is generated by `Gradle` when building the project.

## Contributing
### Branches
Each issue should be worked on separately in its own branch; issue #420 should only be worked on in the branch `issue-420`. Solutions to issues can only be merged to `master` after a successful review of a pull request.

### Documentation
* Every method should have a `JavaDoc` comment explaining what it does, the parameters and the return value. 

### Statement of Contributions
_Axel Kennedal_
* created the Slack app
* implemented Slack integration for notifications
* implemented parsing Gradle test results XML
    
_My Helmisaari_
 * this README
 * Compiling and testing with GradleConnector

       
_Henrik Mellin_
 * Webhook GitHub
 * Cloning the project

   
_Mathieu Desponds_
 * this README
 * The structure of the project including the Gradle part 
 * The assessment branch 
    
## Troubleshooting
### IntelliJ
If the source and test folders are not showing up in the project view, try following [these instructions](https://stackoverflow.com/questions/5816419/intellij-does-not-show-project-folders).
