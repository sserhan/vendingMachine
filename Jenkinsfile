pipeline {
    agent none
    stages {
        stage('Build'){
            agent { label 'gradle' }
            steps {
                    sh "gradle clean"
                      }
    }

}