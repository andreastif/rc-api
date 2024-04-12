pipeline {
    agent { label 'docker-java-gradle-agent' }
    environment {
        REGISTRY_CREDENTIALS_ID = 'andtif-registry-credentials'
        REGISTRY_URL = 'registry.andtif.codes'
        IMAGE = 'rc-api'
        TAG = 'latest'
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm // Checks out source code to workspace
            }
        }
    stage('Test SSH') {
            steps {
                sshagent(credentials: ['SSH-agent-to-ubuntu']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no andtif@192.168.68.134 "echo Hello from Jenkins!"
                    '''
                }
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
        // Optionally, you can add a deployment stage here with a similar 'when' condition for 'master' branch
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
