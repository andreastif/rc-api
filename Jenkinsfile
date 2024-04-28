pipeline {
    agent { label 'docker-java-gradle-agent' }
    environment {
        SSH_ADDRESS = credentials('SSH-HOST-ADDRESS')
        HOST_IP = credentials('HOST_IP')
        
        REGISTRY_CREDENTIALS_ID = 'andtif-registry-credentials'
        REGISTRY_URL = 'registry.andtif.codes'
        IMAGE = 'rc-api'
        TAG = 'latest'
        
        OPEN_AI_API_KEY = credentials('ENV_OPEN_AI_API_KEY')
        API_USER = credentials('ENV_API_USER')
        API_PW = credentials('ENV_API_PW')
        TOKEN_ISSUER_URI = credentials('ENV_TOKEN_ISSUER_URI')
        TOKEN_AUDIENCE = credentials('ENV_TOKEN_AUDIENCE')
    }
    stages {
        stage('Setup') {
            steps {
                echo 'Setting upp SSH for known_hosts'
                sh 'mkdir -p ~/.ssh'
                sh 'chmod 700 ~/.ssh'
                sh 'ssh-keyscan -H ${HOST_IP} >> ~/.ssh/known_hosts'
                sh 'chmod 644 ~/.ssh/known_hosts'
            }
        }
        stage('Checkout') {
            steps {
                echo 'Checking out source code'
                checkout scm // Checks out source code to workspace
            }
        }
        stage('Test') {
            steps {
                echo 'Running tests'
                sh './gradlew test' // Run tests
            }
        }
        stage('Build') {
            steps {
                echo 'Building JAR.'
                sh './gradlew build' // Build .JAR
            }
        }
        stage('Create Docker Image') {
            when {
                branch 'master' 
            }
            steps {
                script {
                    echo 'Creating Docker Image.'
                    // Assumes that the JAR file is built with 'gradlew build'
                    def app = docker.build("${REGISTRY_URL}/${IMAGE}:${TAG}")
                }
            }
        }
        stage('Push Image') {
            when {
                branch 'master'
            }
            steps {
                script {
                    echo 'Pushing Docker Image.'
                    docker.withRegistry("https://${REGISTRY_URL}", REGISTRY_CREDENTIALS_ID) {
                        docker.image("${REGISTRY_URL}/${IMAGE}:${TAG}").push()
                    }
                }
            }
        }
        stage('Deploy') {
            when {
                branch 'master' 
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'andtif-registry-credentials', usernameVariable: 'REGISTRY_USER', passwordVariable: 'REGISTRY_PASS')]) {
                    sshagent(credentials: ['SSH-agent-to-ubuntu']) {
                        sh '''
                            ssh -o StrictHostKeyChecking=no ${SSH_ADDRESS} '
                                echo "Logging into Docker registry..."
                                echo \$REGISTRY_PASS | docker login ${REGISTRY_URL} -u "\$REGISTRY_USER" --password-stdin
            
                                echo "Pulling the latest image..."
                                docker pull ${REGISTRY_URL}/${IMAGE}:${TAG}
            
                                echo "Stopping existing container if it exists..."
                                docker stop rc-api || true
                                docker rm rc-api || true
            
                                echo "Running new container..."
                                docker run -d --name rc-api \\
                                --network recipe-companion-network \\
                                -e OPEN_AI_API_KEY="${OPEN_AI_API_KEY}" \\
                                -e API_USER="${API_USER}" \\
                                -e API_PW="${API_PW}" \\
                                -e TOKEN_ISSUER_URI="${TOKEN_ISSUER_URI}" \\
                                -e TOKEN_AUDIENCE="${TOKEN_AUDIENCE}" \\
                                -p 9000:9000 \\
                                ${REGISTRY_URL}/${IMAGE}:${TAG}
                            '
                        '''
                    }
                }
            }
        }
    }
    post {
            failure {
                echo 'The build failed.'
            }
            success {
                echo 'Build and deployment were successful.'
                
            }
            
        }
}
