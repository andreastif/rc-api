pipeline {
    agent { label 'docker-java-gradle-agent' }
    stages {
        stage('build') {
            steps {
                sh './gradlew --version'
            }
        }
    }
}
