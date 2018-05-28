FROM anapsix/alpine-java:8_server-jre_unlimited
MAINTAINER Lionel Touati <ltouati@google.com>
ENTRYPOINT ["/opt/jdk/jre/bin/java", "-jar", "/usr/share/todo/service.jar"]
ARG JAR_FILE

ADD target/lib /usr/share/todo/lib
ADD target/${JAR_FILE} /usr/share/todo/service.jar