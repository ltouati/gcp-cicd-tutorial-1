library "gcloud-pipeline-library"
def default_zone = 'europe-west1-b'
def project_id='gcp-ltouati-cloud-summit'
/*
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
node('chrome-ubuntu-1604'){
  stage ('Functional Tests') {
    checkout scm
    withMaven(
        // Maven installation declared in the Jenkins "Global Tool Configuration"
        maven: 'Maven 3.5.3',
        mavenLocalRepo: '.repository') {
            sh "mvn clean verify -P integration-test"
    }
  }
}
*/
node('ubuntu-1604'){
    stage('Create Docker Image') {
        checkout scm
        withMaven(
            // Maven installation declared in the Jenkins "Global Tool Configuration"
            maven: 'Maven 3.5.3',
            mavenLocalRepo: '.repository') {
                sh "git rev-parse --short HEAD > .git/commit-id"
                def commit_id = readFile('.git/commit-id').trim()
                gcloud(serviceAccountCredential: 'gcp-instance-creator') {
                    sh "mvn clean -Ddocker.image.version=${env.BUILD_ID}-${commit_id} -Dgcp-project-id=${project_id} install -P docker"
                    sh "gcloud docker -- push eu.gcr.io/${project_id}/todo-app:${env.BUILD_ID}-${commit_id}"
                    sh "gcloud --quiet compute instances delete --zone ${default_zone} test-${env.BUILD_ID}-${commit_id} || true "
                    sh "gcloud beta compute instances create-with-container test-${env.BUILD_ID}-${commit_id} --zone=${default_zone} --machine-type=n1-standard-1  --image-project=cos-cloud --boot-disk-size=10GB --boot-disk-type=pd-standard --container-image=eu.gcr.io/${project_id}/todo-app:${env.BUILD_ID}-${commit_id} --container-restart-policy=always --preemptible"
                }
        }
    }
}