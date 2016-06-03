# Setup Guide
If you want to try this project locally, checkout this repository and do the following steps (yes, there's quite many of them).

## Prerequisites
### Java 8 SDK
This project uses Java SDK version 1.8

### Application Server
1. Get [Wildfly 10](http://wildfly.org/downloads/) here.
2. (Optional) You may need to configure admin user, ports etc. Read the latest docs for info about that.

## IntelliJ
### Backend Project (JavaEE)
1. Import `pom.xml` to IntelliJ. Hit next.
2. Select `openshift` profile. Hit next 4x times.
3. `File` -> `Project Structure` -> `Artifacts`.
4. `Add` -> `Web Application: Exploded` -> `From modules` -> `OK`
5. Change the destination location from `/target/clubby` to `/target/ROOT` (So it would register the project as `ROOT.war`).
6. `Edit configurations` -> `Add` -> `JBoss` -> `Local`.
7. `Deployment` -> `Add` -> `Artifact` -> `clubby:war exploded`.

#### DataSource configuration
1. Download [postgresql driver](https://www.dropbox.com/s/rjxxa6cynt7ped2/postgresql.zip?dl=0).
2. Extract to `%WILDFLY_HOME%\modules\system\layers\base\org`.
3. Edit `%WILDFLY_HOME%\standalone\configuration\standalone.xml`.
  - Find `<datasources>` tag and replace it [with this tag](https://gist.github.com/elpaulas/214bc090b742abbbad95b389e47214d5).
  - Find and remove `default-bindings` tag.

#### Email service configuration
1. Edit `%WILDFLY_HOME%\standalone\configuration\standalone.xml`
  - Find `<remote-destination` tag and replace it with `<remote-destination host="smtp.gmail.com" port="465"/>`
  - Find `<mail-session` tag and replace it with

```
<mail-session name="Gmail" jndi-name="java:jboss/mail/Gmail">
  <smtp-server password="${env.OPENSHIFT_GMAIL_PASSWORD}" username="${env.OPENSHIFT_GMAIL_USERNAME}" ssl="true" outbound-socket-binding-ref="mail-smtp"/>
</mail-session>
```

## UI Project (Angular2)
### Installing
- `npm install` to install all dependencies.
- 'npm run build' to build the app.
- (Optional) `npm run watch` builds the app on file change.

### Running the app
Build and deploy Java Web application, it will host the app you just built on `localhost:8080`.

#### Automatic file updates
This guide does not include auto file updates by IntelliJ, look that up [here](https://blog.jetbrains.com/idea/2009/10/update-a-running-javaee-application/).

## Database
### Openshift platform
For this you will need an account on [Openshift](https://www.openshift.com/). To access database you will need to use RedHat Client a.k.a. OpenShift client tools - `rhc`
- [Install OpenShift tools (Windows)](https://developers.openshift.com/en/getting-started-windows.html)
  - **Note:** Install Ruby 1.9.3, because 2+ is not compatible!
  - **Note2:** Make sure `rhc` does not mess your `ssh` keys if you have any configured! Better skip that step and add a new key for Openshift in the website.

- Run `rhc port-forward -a clubby` - this will tunnel traffic from your `localhost` ports through `rhc` client straight to endpoints on the server using ssh. See the printed table in console for `ip:port` mappings (some local ports may be already in use, so it will iterate the port).

### Local database
This guide does not cover the setup of local database because it's fairly straightforward, just remember it's `PostgreSQL 9.2`.

## Environment variables
Set these values depending on you approach (Openshift platform or local DB).
- Set environment variables for DB (ask team for values):
  - `OPENSHIFT_POSTGRESQL_DB_USERNAME`
  - `OPENSHIFT_POSTGRESQL_DB_PASSWORD`
  - `OPENSHIFT_POSTGRESQL_DB_HOST`
  - `OPENSHIFT_POSTGRESQL_DB_PORT`

- Set environment variables for Facebook (ask team for values):
  - `FACEBOOK_CLUBBY_SECRET`
  - `FACEBOOK_CLUBBY_REDIRECT_URL`
  - `FACEBOOK_CLUBBY_APP_ID`

- Set environment variables for mail service (ask team for values):
  - `OPENSHIFT_GMAIL_PASSWORD`
  - `OPENSHIFT_GMAIL_USERNAME`
