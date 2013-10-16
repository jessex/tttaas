package ch.jessex.tttaas;

import ch.jessex.tttaas.db.GameDAO;
import ch.jessex.tttaas.db.jdbi.GameJdbiDAO;
import ch.jessex.tttaas.resources.v1.GameResource;
import ch.jessex.tttaas.resources.v1.MoveResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.jdbi.DBIFactory;
import com.yammer.dropwizard.json.ObjectMapperFactory;
import org.skife.jdbi.v2.DBI;

/**
 * The main class for the service, whence comes all bootstrapping.
 *
 * @author jessex
 * @since 0.0.1
 */
public class TttaasService extends Service<TttaasConfiguration> {

    public static void main(String[] args) throws Exception {
        new TttaasService().run(args);
    }

    @Override
    public void initialize(Bootstrap<TttaasConfiguration> tttaasConfigurationBootstrap) {
        tttaasConfigurationBootstrap.setName("tttaas");
    }

    @Override
    public void run(TttaasConfiguration tttaasConfiguration, Environment environment) throws Exception {
        DBIFactory factory = new DBIFactory();
        DBI jdbi = factory.build(environment, tttaasConfiguration.getDatabaseConfiguration(), "mysql");
        GameDAO gameDAO = new GameJdbiDAO(jdbi, new ObjectMapperFactory().build());

        environment.addResource(new GameResource(gameDAO));
        environment.addResource(new MoveResource(gameDAO));
    }
}
