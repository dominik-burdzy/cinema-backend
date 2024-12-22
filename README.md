## Local configuration

### Configuration file

#### Mandatory

Create a copy of [application-local.template.yml](./src/main/resources/application-local.yml)
in `/resources` and rename it to
`application-local.yml`. This configuration file is loaded while bootstrapping and contains required liquibase config.

> :warning: [**IMPORTANT!**] In order to use it locally you need to provide AWS Cognito variables (`cognito.userPoolId` and `cognito.clientId`).
> Ask other team members (preferably j.gruda@dental21.de) or check Vault `/backend-services/medlog`

#### Optional

Additionally, you can create a copy
of [application-local.template.yml](./src/main/resources/application-local.yml) and move it
to `~/.patient21/{application-name}.yml`.
This information will persist, even if you delete the project.
See [PropertyConfig](./src/main/kotlin/de/dental21/_BACKEND_TEMPLATE_/config/PropertyConfig.kt) for details.

### Database

Set up your PostgreSQL database with `docker-compose up`.
You can manually load a data dump from Staging or sync you data from Staging.
Definitely check out the sync as it will be useful in most cases.

#### Technical Info

* The project is set up to use the basic Spring MVC and corresponding security with AWS Cognito.
* The project uses `liquibase` for database schema management

### Prerequisites

What things you need to run the project locally and how to install them

* [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
* [Maven 3.6.x](http://maven.apache.org/download.cgi)

* Add Gitlab package registry to maven remote.

Add the following section in `settings.xml` which should be under `~/.m2/`.

```xml

<settings>
  <servers>
    <server>
      <id>gitlab-maven</id>
      <configuration>
        <httpHeaders>
          <property>
            <name>Private-Token</name>
            <value>REPLACE_WITH_YOUR_PERSONAL_ACCESS_TOKEN</value>
          </property>
        </httpHeaders>
      </configuration>
    </server>
  </servers>
</settings>
```

Replace the access token with one generated from https://gitlab.com/profile/personal_access_tokens with the following
roles `api, read_user, read_api, read_repository, read_registry, write_registry`.
More information can be
found [here](https://docs.gitlab.com/ee/user/packages/maven_repository/#adding-the-gitlab-package-registry-as-a-maven-remote).
