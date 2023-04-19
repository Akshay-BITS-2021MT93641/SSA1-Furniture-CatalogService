FROM amazoncorretto:17.0.6-al2023
RUN mkdir -p /opt/ssa1
COPY ${project.basedir}/target/${project.artifactId}-${project.version}.jar /opt/ssa1
ARG SPRING_PROFILE
ENV SPRING_PROFILE="${SPRING_PROFILE}"
EXPOSE 8081
ENTRYPOINT java -Dspring.profiles.active=$SPRING_PROFILE -jar /opt/ssa1/${project.artifactId}-${project.version}.jar