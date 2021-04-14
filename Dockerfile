FROM maven:3.5-jdk-8
COPY starter/target/starter-0.0.1-SNAPSHOT.jar /
EXPOSE 9494
WORKDIR /root
ENTRYPOINT ["/bin/bash","-c","java -jar /starter-0.0.1-SNAPSHOT.jar"]
