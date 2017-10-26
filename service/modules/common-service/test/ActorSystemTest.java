import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sunbird.common.models.util.PropertiesCache;
import org.sunbird.common.util.actorutility.ActorSystemFactory;
import org.sunbird.common.util.actorutility.impl.LocalActorSystem;
import org.sunbird.common.util.actorutility.impl.RemoteActorSystem;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;

public class ActorSystemTest {
  
  static String provider = null;
      
  @BeforeClass
  public static void setUp() {
    
    provider = PropertiesCache.getInstance().getProperty("api_actor_provider");
  }
  
  @Test
  public void testActorSystem(){
    Object obj = ActorSystemFactory.getActorSystem();
     if(provider.equalsIgnoreCase("local")){
       Assert.assertTrue(obj instanceof LocalActorSystem);
     } else {
       Assert.assertTrue(obj instanceof RemoteActorSystem);
     }
  }
  
  @Test
  public void testActorRef(){
    Object obj = ActorSystemFactory.getActorSystem().initializeActorSystem(null);
     if(provider.equalsIgnoreCase("local")){
       Assert.assertTrue(obj instanceof ActorRef);
     } else {
       Assert.assertTrue(obj instanceof ActorSelection);
     }
  }

}
