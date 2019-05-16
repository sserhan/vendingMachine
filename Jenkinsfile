pipeline {
    agent none
    stages {
        stage('Build'){
            agent { label 'gradle' }
            steps {
                    sh "gradle clean"
                  }
        }
        stage('sonar'){
            steps{
                sh "gradle sonarqube"
            }
        }
    }

}
