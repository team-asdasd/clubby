![awd](/docs/asdasd.png)

# PSK

## Links

- [Application](http://clubby-teamasdasd.rhcloud.com/)
- [Trello Board](https://trello.com/b/nIlxlaQh/psk)

## Requirements

- [Funkctional (LT)](/docs/SGP_uzduotis_2016.pdf)
- [Non functional (LT)](/docs/KokybiniaiReikalavimai.pdf)


# Setup

## IntelliJ

### Project

1. Checkout
2. Import `pom.xml` to IntelliJ
3. Next -> select openshift -> next x4
4. File->Project structure->artifacts
5. add-> web application exploded ->from modules-> OK
6. add -> web application archive -> for 'clubby'
7. Rename to 'ROOT.war' -> OK
8. Edit configurations -> add -> jboss local
9. Deployment -> add -> artifact -> clubby:war
10. Run and have fun

### Database

For this you will need an account on [Openshift](https://www.openshift.com/).
To access database you will need to use RedHat Client a.k.a. OpenShift client tools - `rhc`

1. [Install OpenShift tools (Windows)](https://developers.openshift.com/en/getting-started-windows.html)
  * **Note:** Install Ruby 1.9.3, because 2+ is not compatible!
  * **Note2:** Make sure `rhc` does not mess your `ssh` keys if you have it configured! Better skip that step and add a new key for Openshift in the website.

2. Run `rhc port-forward -a clubby` - this will tunnel traffic from your localhost ports through `rhc` client straight to endpoints on the server using ssh. See the printed table in console for `ip:port` mappings (some local ports may be already in use, so it will iterate the port).

3. Set environment variables (ask team for values):
  * `OPENSHIFT_POSTGRESQL_DB_USERNAME`
  * `OPENSHIFT_POSTGRESQL_DB_PASSWORD`
  * `OPENSHIFT_POSTGRESQL_DB_HOST`
  * `OPENSHIFT_POSTGRESQL_DB_PORT`

## More notes

Do not commit any passwords to git ;)
