package controllers.actorutility.impl;

import org.sunbird.common.util.actorutility.ActorUtility;

import controllers.actorutility.ActorSystem;

/**
 * 
 * @author Amit Kumar
 *
 */
public class RemoteActorSystem implements ActorSystem{

  @Override
  public Object initializeActorSystem() {
    return ActorUtility.getActorSelection();
  }

}