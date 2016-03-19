package utilities;

import java.util.Properties;

public class DatasourceConfiguration extends Properties{
    public DatasourceConfiguration(){
        super.put("user", System.getenv("OPENSHIFT_POSTGRESQL_DB_USERNAME"));
        super.put("password", System.getenv("OPENSHIFT_POSTGRESQL_DB_PASSWORD"));
        super.put("serverName", System.getenv("OPENSHIFT_POSTGRESQL_DB_HOST"));
        super.put("portNumber", System.getenv("OPENSHIFT_POSTGRESQL_DB_PORT"));
    }
}
