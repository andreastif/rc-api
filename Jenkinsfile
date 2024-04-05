pipeline {
    agent { docker { image 'gradle:jdk17-alpine' } }
    stages {
        stage('build') {
            steps {
                sh 'gradle --version'
            }
        }
    }
}
