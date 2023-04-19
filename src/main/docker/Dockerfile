FROM amazoncorretto:17.0.6-al2023
WORKDIR /opt/ssa1
COPY ${project.basedir}/target/${project.artifactId}-${project.version}.jar .
RUN mv ${project.artifactId}-${project.version}.jar ${project.artifactId}.jar
ARG SPRING_PROFILE=local
ENV SPRING_PROFILE="${SPRING_PROFILE}"
VOLUME /tmp/ssa1/logs
EXPOSE 8081
ENTRYPOINT java -Dspring.profiles.active=$SPRING_PROFILE -jar ${project.artifactId}.jar