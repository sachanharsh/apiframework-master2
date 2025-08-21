pipeline {
  agent any

  tools {
    jdk 'temurin-21'     // must match the name you set under JDK installations
    maven 'maven-3.9'    // must match the name you set under Maven installations
  }

  parameters {
    choice(name: 'GROUP', choices: ['smoke','regression'], description: 'TestNG group to run')
    
  }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build & Test') {
      steps {
        sh 'mvn -B -q clean test -Dgroups=${GROUP}'
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