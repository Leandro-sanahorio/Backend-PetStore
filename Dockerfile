FROM openjdk:17
EXPOSE 8080
ADD target/Backend-PetStore.jar Backend-PetStore.jar

ENTRYPOINT ["java", "-jar","/Backend-PetStore.jar"]