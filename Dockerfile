FROM gradle:alpine
ENV GRADLE_USER_HOME /cache
COPY tic-tac-toe /home/gradle/src/
WORKDIR /home/gradle/src/
ENTRYPOINT gradle build && java -jar app/build/libs/tic-tac-toe-app-1.0-SNAPSHOT.jar
