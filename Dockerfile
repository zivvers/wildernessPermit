FROM zenika/alpine-maven
RUN apk update \
	&& apk add ca-certificates wget \
	&& update-ca-certificates

RUN apk add vim

ENV mongopath /usr/src/app/MongoScala/src/main/scala/com/

RUN mvn archetype:generate -B \
    -DarchetypeGroupId=net.alchim31.maven -DarchetypeArtifactId=scala-archetype-simple -DarchetypeVersion=1.7 \
    -DgroupId=com.mongoscala -DartifactId=MongoScala -Dversion=0.1-SNAPSHOT -Dpackage=com.mongoscala
WORKDIR /usr/src/app/MongoScala 
RUN rm pom.xml
RUN rm src/test/scala/samples/*

##make addtl changes down here to avoid re-running mvn layer :)