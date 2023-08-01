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
# Alternatively, set the params when starting minikube
minikube start --cpus 2 --memory 4g --driver docker --profile gamecloud
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

Alternatively, instead of using the default cluster (_minikube_) using a _gamecloud_ namespace,
you could also create a _gamecloud_ cluster using `minikube`.

```bash
minikube addons enable ingress --profile gamecloud
minikube start --profile gamecloud
```

This allows you to omit the `--namespace gamecloud` the the following commands below.

### Load a local image into minikube

```bash
./gradlew bootBuildImage
minikube image load gamecloud-catalog-service --profile gamecloud
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

And then visit http://localhost:8000/games.

### Get all related resources of this app

```bash
kubectl get all -l app=catalog-service
```

### Delete Deployment for application container

```bash
kubectl delete deployment --namespace gamecloud catalog-service
```

### Delete Service for application container

```bash
kubectl delete service --namespace gamecloud catalog-service
```

### Stop the minikube cluster

```bash
minikube stop
# Alternatively, when a separate profile (cluster) is used
minikube stop --profile gamecloud
```

### Manually trigger config update via Spring Actuator

The following requires a running instance of [config-service}(https://github.com/b3nk4n/gamecloud-config-service),
which is where the configuration is pulled from.

```bash
http POST :9001/actuator/refresh
```

## Manually running a backing data service

If catalog-service is running locally with `feature-flag.in-memory-storage=false` as standalone,
then you can spin up a Postgres database using the following command:

```bash
docker run -d --name gamecloud-postgres \
  -e POSTGRES_USER=user \
  -e POSTGRES_PASSWORD=password \
  -e POSTGRES_DB=gamecloud_catalog \
  -p 5432:5432 \
  postgres:15.3
```

## Local development and tools

### Hot deployment using Tilt

To simplify local development, a `Tiltfile` is provided. [Tilt](https://tilt.dev/) can be installed e.g. using the following command on macOS.

```bash
brew install tilt-dev/tap/tilt
```

Then use the following command to deploy and start the application:

```bash
tilt up
```

To undeploy the application and delete the respective Kubernetes resources:

```bash
tilt down
```

### Visualizing k8s workloads using Octant

[Octant](https://octant.dev/) is an open source developer-centric web interface for Kubernetes that lets you inspect
a Kubernetes cluster and its applications.

```bash
brew install octant
```

To visualize the workloads of the k8s cluster in context (`kubectx` or `kubectl config current-context`) in the browser:

```bash
octant
```

### Vulnerability scanning using Grype

[Grype](https://github.com/anchore/grype) enables scanning of static code and container images for vulnerabilities.

To install on macOS, use the following:

```bash
brew tap anchore/grype
brew install grype
```

To scan all the resources (jar, images) contained in the current work directory can be scanned as follows.

```bash
grype .
```

Finally, a specific container that was built using `./gradlew bootBuildImage` can be scanned using the following command.

```bash
grype gamecloud-catalog-service
```

### Validate k8s YAML using Kubeval

[Kubeval](http://kubeval.com) is a tool that can be used to validate the YAML of your Kubernetes declarations.
Install it on macOS using the following command.

```bash
brew tap instrumenta/instrumenta
$ brew install kubeval
```

And the validation can be performed as follows.

```bash
kubeval --strict -d dev/k8s
```
