# Build
custom_build(
    # Name of the container image
    ref = 'gamecloud-catalog-service',
    # Command to build the container image
    command = './gradlew bootBuildImage --imageName $EXPECTED_REF',
    # Files to watch that trigger a new build
    deps = ['build.gradle', 'src']
)

# Deploy
k8s_yaml([
	'dev/k8s/deployment.yaml',
	'dev/k8s/service.yaml'
])

# Manage
k8s_resource('catalog-service', port_forwards=['9001'])
