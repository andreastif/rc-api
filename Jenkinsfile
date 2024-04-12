pipeline {
    agent { label 'docker-java-gradle-agent' }
    environment {
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
        stage('Checkout') {
            steps {
                checkout scm // Checks out source code to workspace
            }
        }
        stage('Test') {
            steps {
                // Add your testing steps here
                sh './gradlew test' // For example, if you have a Gradle task for testing
            }
        }
        stage('Build') {
            steps {
                sh './gradlew build' // Builds the .JAR
            }
        }
        stage('Create Docker Image') {
            when {
                branch 'master' // Only run this stage when on 'master' branch
            }
            steps {
                script {
                    // Assumes that the JAR file is built with 'gradlew build'
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
                sshagent(credentials: ['SSH-agent-to-ubuntu']) {
                    sh """
                    # Pull the latest image on the remote host
                    ssh andtif@192.168.68.134 'docker pull ${REGISTRY_URL}/${IMAGE}:${TAG}'
        
                    # Stop and remove the previous container if it's running
                    ssh andtif@192.168.68.134 'docker stop rc-api || true'
                    ssh andtif@192.168.68.134 'docker rm rc-api || true'
        
                    # Start the new container with environment variables
                    ssh andtif@192.168.68.134 '
                        docker run -d --name rc-api \\
                        --network recipe-companion-network \\
                        -e OPEN_AI_API_KEY="\${OPEN_AI_API_KEY}" \\
                        -e API_USER="\${API_USER}" \\
                        -e API_PW="\${API_PW}" \\
                        -e TOKEN_ISSUER_URI="\${TOKEN_ISSUER_URI}" \\
                        -e TOKEN_AUDIENCE="\${TOKEN_AUDIENCE}" \\
                        -p 9000:9000 \\
                        ${REGISTRY_URL}/${IMAGE}:${TAG}
                    '
                    """
                }
            }
        }
    }
    post {
            always {
                archiveArtifacts artifacts: '**/build/logs/*.log', allowEmptyArchive: true
                // The above assumes that your logs are in 'build/logs' directory and have a '.log' extension
                // 'allowEmptyArchive: true' means don't fail the build if no logs are found
            }
            failure {
                echo 'The build failed, see archived logs for details.'
                // Additional failure handling...
            }
            success {
                echo 'Build and deployment were successful.'
                // Additional success handling...
            }
            // You can also add 'unstable', 'aborted' conditions as needed
        }
}
