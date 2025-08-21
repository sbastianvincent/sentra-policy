pipeline {
    agent any

    environment {
        IMAGE_NAME = 'sentra-policy'

        VERSION_RELEASE = 'latest'
        VERSION_STAGING = 'staging-latest'
        DOCKER_REGISTRY = 'docker.lab.svincent7.com'
    }

    tools {
        maven "M3"
    }

    options {
        disableConcurrentBuilds(abortPrevious: true)
    }

    stages {
        stage('Checkout') {
            steps {
                git credentialsId: 'github-password', url: "${GIT_URL}"
            }
        }
        stage('Build and Test Package with Maven') {
            steps {
                configFileProvider([configFile(fileId: 'maven-settings-xml-id', variable: 'MAVEN_SETTINGS')]) {
                    sh 'mvn -s $MAVEN_SETTINGS clean verify package'
                }
            }
        }
        stage('Build Docker Image for Staging and Push to Registry') {
            steps {
                script {
                    echo "Build Docker Image and Tagging image as: ${VERSION_STAGING}"
                    docker.withRegistry("https://${DOCKER_REGISTRY}", 'docker-credential-id') {
                        sh """
                        docker build -t $DOCKER_REGISTRY/$IMAGE_NAME:$VERSION_STAGING .
                        docker push $DOCKER_REGISTRY/$IMAGE_NAME:$VERSION_STAGING
                        """
                    }
                }
            }
        }
        stage('Publish Coverage') {
            steps {
                recordCoverage(
                    tools: [[parser: 'JACOCO']],
                    id: 'jacoco',
                    name: 'JaCoCo Coverage',
                    sourceCodeRetention: 'EVERY_BUILD'
                )
            }
        }
        stage('Manual Approval for Deployment') {
            steps {
                script {
                    input message: 'Approve to proceed to Release stage?', ok: 'Proceed'
                }
            }
        }
        stage('Release: Docker Image with the Release Tag') {
            steps {
                script {
                    echo "Tagging Docker image with: ${VERSION_RELEASE}"
                    docker.withRegistry("https://${DOCKER_REGISTRY}", 'docker-credential-id') {
                        sh """
                        docker tag $DOCKER_REGISTRY/$IMAGE_NAME:$VERSION_STAGING $DOCKER_REGISTRY/$IMAGE_NAME:${VERSION_RELEASE}
                        docker push $DOCKER_REGISTRY/$IMAGE_NAME:${VERSION_RELEASE}
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            echo "Pipeline completed. Performing cleanup."
            cleanWs() // Cleanup workspace
            script {
                try {
                    echo "Cleaning up all Docker images for $IMAGE_NAME."

                    // Remove all images with all tags for this image
                    sh """
                        docker images --filter=reference=$DOCKER_REGISTRY/$IMAGE_NAME --format '{{.Repository}}:{{.Tag}}' | xargs -I {} docker rmi -f {}
                    """
                } catch (Exception e) {
                    echo "Docker cleanup failed: ${e.getMessage()}"
                }
            }
        }
        success {
            echo "Pipeline finished successfully."
        }
        failure {
            echo "Pipeline failed."
        }
    }
}