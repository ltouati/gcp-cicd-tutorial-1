def default_zone = 'europe-west1-b'
def project_id='gcp-ltouati-cloud-summit'
node('ubuntu-1604'){
  stage ('Unit Tests') {
    checkout scm
    withMaven(
        // Maven installation declared in the Jenkins "Global Tool Configuration"
        maven: 'Maven 3.5.3',
        mavenLocalRepo: '.repository') {
            sh "mvn clean test"
            step( [ $class: 'JacocoPublisher' ] )
    }
  }
}
