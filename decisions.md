## Tue 15 Oct, 2015
- Introduced pool for the bots to reduce GC overhead

## Sun 25 Oct, 2015
- Decided to make bots behaviour configurable and use DropWizard EnvironmentVariableSubstitutor.
    The idea is to set more bots (about 100 via poolSize) and shorter scheduled period
    (about 10 sec via scheduledPeriodInSeconds) during initial launch, and then switch to smaller pool size and
    longer scheduled period.

## Sat 31 Oct, 2015

All the decisions below were done in the beginning of the project development, although at that moment of time, the
concept of "decisions.md" file was just finalizing, so here I describe the decisions were taken, but ideally they
should be listed as soon as the actual decision is made.

### Application architecture
Being an online real-time interactive game, there were only a few feasible options to go: WebSockets or library which provides
an abstraction on top of the actual connection being used, like socket.io. WebSockets technology was chosen to make
everything simple, and explicit. Although, the risk is clear, anybody with the old browser without WebSocket support,
can't join a game, which was a deliberate decision.

Application is 100% stateless, no state is kept on the backend. Again an explicit choice, considering the scope and the
purpose of the application.

Architecture style: micro services, or more precise wanted something which can be run, without necessity of a separate
web application container, like Tomcat. So, an embedded option or own HTTP server implementation. Went with an embedded
Jetty provided by Dropwizard.

Key characteristic of any project (and this one is not an exception) is to have tools enforcing and checking code style,
code coverage, static analysis. That is why Checkstyle, JaCoCo, PMD/FindBugs were used. There is even a dedicated
project of mine to help getting started with a such setup: [Gradle Getting Started Template](https://github.com/zshamrock/gradle-getting-started-template).

### Hosting
- *Heroku*

    At the time when I have to deploy the app, the one of the main criteria was the ease and fast of deployment.
    And it came out that Heroku was the easiest one to setup and run the application online.
    Another alternative tried was Google App Engine, but the documentation and getting started guide was not that
    clean and straight forward in order to get started in minutes, not hours. So, it is still might be simple, but
    Heroku is just simply simpler. And there was also a personal reason - wanted to try Heroku in action.
    Also Heroku nicely implements http://12factor.net/ (and it is not a surprise, as it was written by Heroku developers),
    which I agree and aim to implement for every project I develop as close as possible.
- *Alternatives*: Google App Engine, Digital Ocean (deploying the app inside the Docker container).
- *Risks*: Being a hobby project, no risks, except price increase, as the current price 7$ per month not being expensive,
    still not for free. For the new project I will go for Docker, most likely hosting on Digital Ocean.

### RESTful framework
- *Dropwizard*

    I wanted something simple in the sense "Simple made easy" talk by Rich Hickey, and I wanted to avoid Spring.
    Dropwizard was a natural choice. One of the concern I had, that how easy/possible it would be to add WebSocket
    support into Dropwizard, doing some spike before writing the main app approved that it is more than possible.
    Although I have to go with native Jersey implementation instead of official JSR 356, Java API for WebSocket.
- *Alternatives*: Spring Boot, but again as I wanted to avoid to use Spring, it was not an option.
- *Risks*: I would say none, considering the features I required. Even with the bigger project I would still go with
    Dropwizard vs Spring Boot.

### Building tool
- *Gradle*

    It is just a personal choice versus maven, to give it a try, and I used it for another projects as well,
    and found it superior comparing to Maven, and leverage Groovy skills.
- *Alternatives*: Maven. Again both would do the job well, it is just simply Gradle of being the build tool of mine nowadays.

### Testing
- *Spock*

    Tried it once, and no longer want to come back to JUnit. More productive, less verbose, more powerful, and simple
    comparing to JUnit.
- *Alternatives*: JUnit. Again as in case with Gradle, Spock nowadays the testing tool I prefer to use for everyday testing.

## Sun 1 Nov, 2015

Integrated with Travis CI, and in addition to VersionEye, Coveralls and Coverity Scan (except Travis CI, just for
curiosity and for fun, but of course, all of them deliver it is own value).

### CI
- *Travis CI*
- *Alternatives*: many. Have not considered them at all, as Travis being the number one (and personal) choice for open
    source projects. More over having a good documentation and ease of setup.
