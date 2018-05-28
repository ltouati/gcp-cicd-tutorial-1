pipeline {
    agent any
    tools {
        maven 'Maven 3.3.9'
        jdk 'jdk8'
    }
    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
            }
        }
        stage ('Unit Tests') {
            steps {
                sh 'mvn clean test'
            }
            post {
               success {
                   junit 'target/failsafe-reports/**/*.xml'
               }
            }
        }
        stage ('Functional Tests') {
            steps {
                sh 'mvn clean verify -P integration-test'
            }
            post {
               success {
                   junit 'target/failsafe-reports/**/*.xml'
               }
            }
        }
    }
}