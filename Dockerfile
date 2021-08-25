ARG SPARK_VERSION=3.1.0

ARG ARG_JAR_NAME=bigdata-spark-scoring-0.0.1-SNAPSHOT.jar
ARG ARG_MAIN_CLASS=sy.bigdata.spark.SparkPractice
ARG spark_uid=185


FROM maven:3-openjdk-11 as build



RUN mkdir /local

COPY pom.xml /local/pom.xml

COPY src /local/src

WORKDIR /local

RUN mvn clean package




FROM gcr.io/spark-operator/spark-operator:latest


ARG ARG_JAR_NAME
ARG ARG_MAIN_CLASS

COPY --from=build /local/target/${ARG_JAR_NAME} /${ARG_JAR_NAME}

ENV MAIN_CLASS=${ARG_MAIN_CLASS}

ENV JAR="/${ARG_JAR_NAME}"


USER ${spark_uid}


ENTRYPOINT ["/opt/entrypoint.sh"]


#ADD target/bigdata-spark-practice-0.0.1-SNAPSHOT-jar-with-dependencies.jar bigdata-spark-practice.jar
#ENTRYPOINT /opt/java/openjdk/bin/java -cp bigdata-spark-practice.jar sy.bigdata.spark.SparkPractice
