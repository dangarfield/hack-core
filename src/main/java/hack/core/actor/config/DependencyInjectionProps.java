package hack.core.actor.config;
 
import org.springframework.context.ApplicationContext;

import akka.actor.Props;
import akka.actor.UntypedActorFactory;

@SuppressWarnings("serial")
public class DependencyInjectionProps extends Props {  
    /**
     * No-args constructor that sets all the default values.
     */
    public DependencyInjectionProps(ApplicationContext applicationContext, Class<?> actorClass) {
        super(new SpringUntypedActorFactory(actorClass, applicationContext));
    }

    /**
     * Java API.
     */
    public DependencyInjectionProps(ApplicationContext applicationContext, UntypedActorFactory factory) {
        super(new SpringUntypedActorFactory(factory, applicationContext));
    }
}
