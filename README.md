![awd](/docs/asdasd.png)

# PSK

## Linkai

- [Aplikacija](http://clubby-teamasdasd.rhcloud.com/)
- [Trello Boardas](https://trello.com/b/nIlxlaQh/psk)

## Reikalavimai

- [Funkciniai](/docs/SGP_uzduotis_2016.pdf)
- [Nefunkciniai](/docs/KokybiniaiReikalavimai.pdf)


# Setup

## IntelliJ

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
