node('ubuntu-1604'){
  stage ('Unit Tests') {

    withMaven(
        // Maven installation declared in the Jenkins "Global Tool Configuration"
        maven: 'Maven 3.5.3',
        mavenLocalRepo: '.repository') {
            sh "mvn clean test"

    }
  }
}
node('ubuntu-1604'){
  stage ('Functional Tests') {

    withMaven(
        // Maven installation declared in the Jenkins "Global Tool Configuration"
        maven: 'Maven 3.5.3',
        mavenLocalRepo: '.repository') {
            sh "mvn clean verify -P integration-test"

    }
  }
}
