package ch.jessex.tttaas;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

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

    }
}
