package controllers.actorutility;

import controllers.actorutility.impl.LocalActorSystem;
import controllers.actorutility.impl.RemoteActorSystem;
import org.sunbird.common.models.util.ConfigUtil;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.common.models.util.ProjectLogger;

/**
 * 
 * @author Amit Kumar
 *
 */
public class ActorSystemFactory {

  private static ActorSystem actorSystem = null;

  private ActorSystemFactory() {}

  static {
    ProjectLogger.log("Inside ActorSystemFactory static block");
    try{
    ProjectLogger.log("api_actor_provider value "+ ConfigUtil.getString(JsonKey.API_ACTOR_PROVIDER));
    if ("local".equalsIgnoreCase(ConfigUtil.getString(JsonKey.API_ACTOR_PROVIDER))) {
      ProjectLogger.log("Initializing Normal Local Actor System  called from controller");
      if (null == actorSystem) {
        actorSystem = LocalActorSystem.getInstance();
      }
    } else {
      ProjectLogger.log("Initializing Normal Remote Actor System called from controller");
      if (null == actorSystem) {
        actorSystem = RemoteActorSystem.getInstance();
      }
    }
    }catch(Exception ex ){
      ProjectLogger.log("Exception In controllers.actorutility.ActorSystemFactory "+ex);
      }
  }

  public static ActorSystem getActorSystem() {
    return actorSystem;
  }
}
