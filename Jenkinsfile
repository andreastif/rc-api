pipeline {
    agent { label 'docker-java-gradle-agent' }
    environment {
        
        SSH_ADDRESS = credentials('SSH-HOST-ADDRESS')
        HOST_IP = credentials('HOST_IP')
        
        REGISTRY_CREDENTIALS_ID = 'andtif-registry-credentials'
        REGISTRY_URL = 'registry.andtif.codes'
        IMAGE = 'rc-api'
        TAG = 'latest'
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
                echo 'Checking out repository'
                checkout scm // Checks out source code to workspace
            }
        }
        stage('Test') {
            steps {
                echo 'Running gradle tests'
                sh './gradlew test' 
            }
        }
        stage('Build') {
            steps {
                echo 'Building JAR'
                sh './gradlew build' 
            }
        }
        stage('Create Docker Image') {
            when {
                branch 'master' 
            }
            steps {
                script {
                    echo 'Building docker image'
                    def app = docker.build("${REGISTRY_URL}/${IMAGE}:${TAG}")
                }
            }
        }
        stage('Push Image') {
            when {
                branch 'master' // Only run this stage when on 'master' branch
            }
            steps {
                script {
                    echo 'Pushing docker image'
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
                withCredentials([
                    usernamePassword(credentialsId: 'andtif-registry-credentials', usernameVariable: 'REGISTRY_USER', passwordVariable: 'REGISTRY_PASS'),
                    string(credentialsId: 'ENV_OPEN_AI_API_KEY', variable: 'OPEN_AI_API_KEY'),
                    string(credentialsId: 'ENV_API_USER', variable: 'API_USER'),
                    string(credentialsId: 'ENV_API_PW', variable: 'API_PW'),
                    string(credentialsId: 'ENV_TOKEN_ISSUER_URI', variable: 'TOKEN_ISSUER_URI'),
                    string(credentialsId: 'ENV_TOKEN_AUDIENCE', variable: 'TOKEN_AUDIENCE')
                ]) {
                    sshagent(credentials: ['SSH-agent-to-ubuntu']) {
                        sh """
                            ssh ${SSH_ADDRESS} '
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
                                -e OPEN_AI_API_KEY="\$OPEN_AI_API_KEY" \\
                                -e API_USER="\$API_USER" \\
                                -e API_PW="\$API_PW" \\
                                -e TOKEN_ISSUER_URI="\$TOKEN_ISSUER_URI" \\
                                -e TOKEN_AUDIENCE="\$TOKEN_AUDIENCE" \\
                                -p 9000:9000 \\
                                ${REGISTRY_URL}/${IMAGE}:${TAG}
                            '
                        """
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
