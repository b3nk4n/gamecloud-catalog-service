# GameCloud Catalog Service

This application is part of the GameCloud system and provides the functionality for managing
the available games in the catalog. The project was created based on a similar project of the
[Cloud Native Spring in Action](https://www.manning.com/books/cloud-native-spring-in-action) book
by [Thomas Vitale](https://www.thomasvitale.com).

## Prerequisites

```bash
# Kubernetes CLI
brew install kubectl

# Minikube to run a Kubernetes cluster locally 
brew install minikube
minikube config set driver docker
minikube start

# Grype for code vulnerability scanning
brew tap anchore/grype
brew install grype
```

## Useful Commands

| Gradle Command	         | Description                                                    |
|:---------------------------|:---------------------------------------------------------------|
| `./gradlew bootRun`        | Run the application.                                           |
| `./gradlew build`          | Build the application.                                         |
| `./gradlew test`           | Run all tests.                                                 |
| `./gradlew bootJar`        | Package the application as a JAR.                              |
| `./gradlew bootBuildImage` | Package the application as a container image using Buildpacks. |

After building the application, you can also run it from the Java CLI:

```bash
java -jar build/libs/catalog-service-0.0.1-SNAPSHOT.jar
```

## Container tasks

Run Catalog Service as a container

```bash
docker run --rm --name catalog-service -p 8080:8080 catalog-service:0.0.1-SNAPSHOT
```

### Container Commands

| Docker Command	              | Description       |
|:-------------------------------:|:-----------------:|
| `docker stop catalog-service`   | Stop container.   |
| `docker start catalog-service`  | Start container.  |
| `docker remove catalog-service` | Remove container. |

## Kubernetes tasks

As a prerequisite, if not yet present, create a namespace for GameCloud using `kubectl`:
```bash
kubectl create namespace gamecloud
```

### Create Deployment for application container

```bash
kubectl create deployment catalog-service --namespace gamecloud --image=catalog-service:0.0.1-SNAPSHOT
```

### Create Service for application Deployment

```bash
kubectl expose deployment catalog-service --namespace gamecloud --name=catalog-service --port=8080
```

### Port forwarding from localhost to Kubernetes cluster

```bash
kubectl port-forward --namespace gamecloud service/catalog-service 8000:8080
```

### Delete Deployment for application container

```bash
kubectl delete deployment --namespace gamecloud catalog-service
```

### Delete Service for application container

```bash
kubectl delete service --namespace gamecloud catalog-service
```
