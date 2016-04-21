[![Build Status](https://travis-ci.com/Tony-Mc/asdasd.svg?token=fdKC47jJTuUKGzpgQy8t&branch=master)](https://travis-ci.com/Tony-Mc/asdasd)
[![Dependency Status](https://david-dm.org/Tony-Mc/asdasd.svg?path=src/main/webapp)](https://david-dm.org/Tony-Mc/asdasd?path=src/main/webapp)
[![devDependency Status](https://david-dm.org/Tony-Mc/asdasd/dev-status.svg?path=src/main/webapp)](https://david-dm.org/Tony-Mc/asdasd?path=src/main/webapp#info=devDependencies)

![awd](/docs/asdasd.png)

# PSK

## Links

- [Application](http://clubby-teamasdasd.rhcloud.com/)
- [Trello Board](https://trello.com/b/nIlxlaQh/psk)

## Requirements

- [Functional (LT)](/docs/SGP_uzduotis_2016.pdf)
- [Non functional (LT)](/docs/KokybiniaiReikalavimai.pdf)

# Setup

## IntelliJ

### Application Server
1. Get [Wildfly 10](http://wildfly.org/downloads/) here.
2. (Optional) You may need to configure admin user for it.

### Backend Project

1. Checkout
2. Import `pom.xml` to IntelliJ. Hit next.
3. Select `openshift` profile. Hit next 4x times.

From here you can go two ways:

1. Maven way (may require IntelliJ Maven tools). This is the way app is built in the cloud.
    1. Edit configurations -> Add -> JBoss -> Local.
    2. Clear `before launch` configuration (list of steps at the bottom).
    3. Add -> Run Maven Goal -> Command line -> `clean`
    4. Add -> Run Maven Goal -> Command line -> `package -P openshift`
    5. Build project.
    6. Deployment -> Add -> External Source -> Select `/deployments/ROOT.war`
2. Default IntelliJ way
    1. File -> Project Structure -> Artifacts
    2. Add -> Web Application: Exploded -> From modules -> OK
    3. Add -> Web Application: Archive -> For `clubby`
    4. Rename Web Application: Archive file you just created from `clubby.war` (or sth) to `ROOT.war` -> OK
    5. Edit configurations -> Add -> JBoss -> Local
    6. Deployment -> Add -> Artifact -> clubby:war

####DataSource configuration
1. Download [postgresql driver](https://www.dropbox.com/s/rjxxa6cynt7ped2/postgresql.zip?dl=0)
2. Extract to `%WILDFLY_HOME%\modules\system\layers\base\org`
3. Edit `%WILDFLY_HOME%\standalone\configuration\standalone.xml`
    - Find `<datasources>` tag and replace [with] (https://gist.github.com/elpaulas/214bc090b742abbbad95b389e47214d5)
    - Find and remove `default-bindings` tag.

## UI Project

Based on `https://github.com/preboot/angular2-webpack`

### Installing

* `clone` the repo
* `npm install` to install all dependencies

### Running the app

After you have installed all dependencies you can now run the app with:

```bash
npm start
```

It will start a local server using `webpack-dev-server` which will watch, build (in-memory), and reload for you. The port will be displayed to you as `http://localhost:3000`.

### Developing UI

#### Add new components, services, pipes with Angular 2 cli

* `npm install -g angular-cli` to install Angular 2 cli
* Add a new component with: `ng generate components/component my-new-component`
* Add a new service with: `ng generate service services/my-new-service`
* Add a new pipe with: `ng generate pipe pipes/my-new-pipe`

Read more at [angular-cli github](https://github.com/angular/angular-cli).

#### Build files

* single run: `npm run build`
* build files and watch: `npm run watch`

## Database

For this you will need an account on [Openshift](https://www.openshift.com/).
To access database you will need to use RedHat Client a.k.a. OpenShift client tools - `rhc`

1. [Install OpenShift tools (Windows)](https://developers.openshift.com/en/getting-started-windows.html)
  * **Note:** Install Ruby 1.9.3, because 2+ is not compatible!
  * **Note2:** Make sure `rhc` does not mess your `ssh` keys if you have it configured! Better skip that step and add a new key for Openshift in the website.

2. Run `rhc port-forward -a clubby` - this will tunnel traffic from your localhost ports through `rhc` client straight to endpoints on the server using ssh. See the printed table in console for `ip:port` mappings (some local ports may be already in use, so it will iterate the port).

3. Set environment variables for DB (ask team for values):
  * `OPENSHIFT_POSTGRESQL_DB_USERNAME`
  * `OPENSHIFT_POSTGRESQL_DB_PASSWORD`
  * `OPENSHIFT_POSTGRESQL_DB_HOST`
  * `OPENSHIFT_POSTGRESQL_DB_PORT`

4. Set environment variables for Facebook (ask team for values):
  * `FACEBOOK_CLUBBY_SECRET`
  * `FACEBOOK_CLUBBY_REDIRECT_URL`
  * `FACEBOOK_CLUBBY_APP_ID`

## More notes

Do not commit any passwords to git ;)
