package security.shiro.application;

import org.postgresql.ds.PGSimpleDataSource;

public class SecurityDataSource extends PGSimpleDataSource {
    public SecurityDataSource() {
        String username = System.getenv("OPENSHIFT_POSTGRESQL_DB_USERNAME");
        String password = System.getenv("OPENSHIFT_POSTGRESQL_DB_PASSWORD");
        String host = System.getenv("OPENSHIFT_POSTGRESQL_DB_HOST");
        int port = Integer.parseInt(System.getenv("OPENSHIFT_POSTGRESQL_DB_PORT"));

        super.setUser(username);
        super.setPassword(password);
        super.setServerName(host);
        super.setPortNumber(port);
    }
}
