# -------------------------------------------------------------------
# (Earlier builder stage remains exactly as before)
# -------------------------------------------------------------------
FROM eclipse-temurin:24-jdk AS build-stage

WORKDIR /app

COPY src/main/java ./src/main/java
COPY src/main/resources ./resources
COPY lib ./lib

RUN mkdir -p build/classes
RUN find src/main/java -name "*.java" > sources.txt
RUN javac -d build/classes -classpath "lib/*" @sources.txt
RUN cp -r resources/* build/classes/

RUN mkdir -p build/classes/META-INF
RUN cp resources/META-INF/MANIFEST.MF build/classes/META-INF/

RUN cd build/classes && jar cfm ../app.jar META-INF/MANIFEST.MF *
  
# -------------------------------------------------------------------
# Stage 2: Runtime image (Java 24 JRE) with explicit -cp
# -------------------------------------------------------------------
FROM eclipse-temurin:24-jre AS run-stage

WORKDIR /app

# Copy the runnable JAR and the lib folder from builder stage
COPY --from=build-stage /app/build/app.jar ./app.jar
COPY --from=build-stage /app/lib    ./lib

EXPOSE 9090

ENTRYPOINT ["java", "-cp", "app.jar:lib/hsqldb-2.7.1.jar", "com.k218b.vehicleregistration.Main"]
