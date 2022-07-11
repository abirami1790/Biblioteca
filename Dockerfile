FROM openjdk:8-jdk
WORKDIR ./

COPY build/libs/biblioteca-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-Xmx1024m","-jar","app.jar"]