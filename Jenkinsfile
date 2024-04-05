pipeline {
    agent { label 'docker-java-gradle-agent' }
    environment {
        REGISTRY_CREDENTIALS_ID = 'andtif-registry-credentials'
        REGISTRY_URL = 'registry.andtif.codes'
        IMAGE = 'RC-API'
        TAG = 'latest'
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm // Checks out source code to workspace
            }
        }
        stage('Build') {
            steps {
                sh './gradlew build' // Builds the .JAR
            }
        }
        stage('Create Docker Image') {
            steps {
                script {
                     // Assumes that the JAR file is built with 'gradlew build'
                    def app = docker.build("${REGISTRY_URL}/${IMAGE}:${TAG}")

                }
            }
        }
        stage('Push Image') {
            steps {
                script {
                    docker.withRegistry("https://${REGISTRY_URL}", REGISTRY_CREDENTIALS_ID) {
                        docker.image("${REGISTRY_URL}/${IMAGE}:${TAG}").push()
                    }
                }
            }
        }
    }
}
