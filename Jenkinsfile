pipeline {
    agent any
	tools{
		maven 'Maven'
		jdk 'jdk'
	}
    stages {
        stage('Build') { 
            steps {
                bat 'gradle clean' 
            }
	}
	 stage('Install') {
            steps {
                bat 'gradle install'
            }
	 }
	    stage('Sonar'){
		    steps{
		    bat 'gradle sonarqube -Dsonar.host.url=http://localhost:9000 -Dsonar.login=d9b8082d2e909663d06cf394f1b38e51e07e292a -Dsonar.analysis.mode=publish -Dsonar.projectKey=vendingMachine' 
		    }
	    }
     }
}
