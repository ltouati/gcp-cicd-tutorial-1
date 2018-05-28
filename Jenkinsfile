node('ubuntu-1604'){
  stage ('Unit Tests') {
    git url: 'https://github.com/ltouati/gcp-cicd-tutorial-1.git'
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
    git url: 'https://github.com/ltouati/gcp-cicd-tutorial-1.git'
    withMaven(
        // Maven installation declared in the Jenkins "Global Tool Configuration"
        maven: 'Maven 3.5.3',
        mavenLocalRepo: '.repository') {
            sh "mvn clean verify -P integration-test"
    }
  }
}
node() {
    stage('Create Docker Image') {
        tool name: 'docker', type: 'org.jenkinsci.plugins.docker.commons.tools.DockerTool'
        git url: 'https://github.com/ltouati/gcp-cicd-tutorial-1.git'
        withMaven(
            // Maven installation declared in the Jenkins "Global Tool Configuration"
            maven: 'Maven 3.5.3',
            mavenLocalRepo: '.repository') {
                sh "git rev-parse --short HEAD > .git/commit-id"
                def commit_id = readFile('.git/commit-id').trim()
                sh "mvn clean -Ddocker.image.version=${env.BUILD_ID}-${commit_id} -Dgcp-project-id=gcp-ltouati-cloud-summit install -P docker"
                sh "gcloud beta compute instances create-with-container TEST-${env.BUILD_ID}-${commit_id} --zone=europe-west1-b --machine-type=n1-standard-1  --image-project=cos-cloud --boot-disk-size=10GB --boot-disk-type=pd-standard --container-image=eu.gcr.io/gcp-ltouati-cloud-summit/todo-app:${env.BUILD_ID}-${commit_id} --container-restart-policy=always --preemptible"
        }
    }
}