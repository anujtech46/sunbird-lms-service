FROM openjdk:8-jre-alpine
MAINTAINER "Manojv" "manojv@ilimi.in"
RUN apk update \
    && apk add  unzip \
    && apk add curl \
    && adduser -u 1001 -h /home/sunbird/ -D sunbird \
    && mkdir -p /home/sunbird/learner

#ENV sunbird_learnerstate_actor_host 52.172.24.203
#ENV sunbird_learnerstate_actor_port 8088 
#ADD https://github.com/anujtech46/jarrepo/tarball/master /home/sunbird/learner/
#RUN ls
WORKDIR /home/sunbird/learner/
ADD https://github.com/anujtech46/jarrepo/tarball/master .
RUN chown -R sunbird:sunbird /home/sunbird
USER sunbird
WORKDIR /home/sunbird/learner/
RUN mkdir -p /home/sunbird/learner/logs/
RUN touch /home/sunbird/learner/logs/learningServiceProject.log
RUN ln -sf /dev/stdout /home/sunbird/learner/logs/learningServiceProject.log
CMD java  -cp '/home/sunbird/learner/learning-service-1.0-SNAPSHOT/lib/*' play.core.server.ProdServerStart  /home/sunbird/learner/learning-service-1.0-SNAPSHOT
