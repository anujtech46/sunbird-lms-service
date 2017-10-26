package controllers.actorutility.impl;

import org.sunbird.common.Application;

import controllers.actorutility.ActorSystem;

/**
 * 
 * @author Amit Kumar
 *
 */
public class LocalActorSystem implements ActorSystem{

  @Override
  public Object initializeActorSystem() {
    return Application.startLocalActorSystem();
  }

  
 
}
