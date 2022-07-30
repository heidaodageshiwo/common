FROM java:8
VOLUME /tmp
COPY target/common-0.0.1-SNAPSHOT.jar demo.jar
RUN bash -c "touch /demo.jar"
EXPOSE 8081
ENTRYPOINT ["java","-jar","demo.jar"]
