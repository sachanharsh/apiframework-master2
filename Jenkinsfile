pipeline {
  agent any

  tools { jdk 'temurin-21'; maven 'maven-3.9' }

  parameters {
    choice(name: 'GROUP', choices: ['smoke','regression'], description: 'TestNG group to run')
  }

  options {
    disableConcurrentBuilds()
    timestamps()
    timeout(time: 45, unit: 'MINUTES')
    buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '5'))
  }

  stages {
    stage('Checkout') {
      steps {
        // Simple shorthand for Git checkout
        git branch: 'main', url: 'https://github.com/sachanharsh/apiframework-master2'
      }
    }

    stage('Tool sanity') {
      steps {
        sh 'java -version'
        sh 'mvn -version'
      }
    }

    stage('Build & Test') {
      steps {
        // If your pom has surefire pointing to testng.xml, this is enough:
        sh 'mvn -B clean test -Dgroups=${GROUP}'
        // If not, use the explicit suite flag:
        // sh 'mvn -B clean test -Dsurefire.suiteXmlFiles=testng.xml -Dgroups=${GROUP}'
      }
    }
  }

  post {
    always {
      junit '**/target/surefire-reports/*.xml'
      archiveArtifacts artifacts: 'target/**/*', allowEmptyArchive: true
    }
  }
}