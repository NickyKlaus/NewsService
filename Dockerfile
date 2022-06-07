FROM amazoncorretto:11-alpine-jdk AS JDK

MAINTAINER Nikolay Beloglazov "nickolai.beloglazov@gmail.com"

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY ./target/newsservice.jar newsservice.jar
ENTRYPOINT ["java", "-jar", "/newsservice.jar", "com.home.newsservice.invoker.Swagger2SpringBoot -Dlog4j2.formatMsgNoLookups=true"]