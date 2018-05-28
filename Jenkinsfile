pipeline {
  stages {
      stage ('Unit Tests') {
        agent { node { label 'ubuntu-1604' } }
        git url: 'https://github.com/ltouati/gcp-cicd-tutorial-1.git'
        withMaven(
            // Maven installation declared in the Jenkins "Global Tool Configuration"
            maven: 'Maven 3.5.3',
            mavenLocalRepo: '.repository') {
                sh "mvn clean test"
                junit '*/build/test-results/*.xml'
                step( [ $class: 'JacocoPublisher' ] )
        }
      }
      stage ('Functional Tests') {
        agent { node { label 'chrome-ubuntu-1604' } }
        git url: 'https://github.com/ltouati/gcp-cicd-tutorial-1.git'
        withMaven(
            // Maven installation declared in the Jenkins "Global Tool Configuration"
            maven: 'Maven 3.5.3',
            mavenLocalRepo: '.repository') {
                sh "mvn clean verify -P integration-test"
                junit '*/build/test-results/*.xml'
                step( [ $class: 'JacocoPublisher' ] )
        }
      }
  }
}