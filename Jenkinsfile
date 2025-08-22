pipeline {
  agent any

  tools {
    jdk 'temurin-21'     // must match the name you set under JDK installations
    maven 'maven-3.9'    // must match the name you set under Maven installations
  }
  
  options { skipDefaultCheckout(true) } 
  
  parameters {
    choice(name: 'GROUP', choices: ['smoke','regression'], description: 'TestNG group to run2')
    
  }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build & Test') {
      steps {
        sh 'mvn -B clean test -Dgroups=${GROUP}'
      }
    }
  }

  post {
    always {
      junit '**/target/surefire-reports/*.xml'
      allure([
        includeProperties: false,
        jdk: '',
        results: [[path: 'target/allure-results']]
      ])
      emailext(
        to: 'harshsachan10@gmail.com',
        subject: "Allure Report - ${env.JOB_NAME} #${env.BUILD_NUMBER}",
        body: """
          Build: ${env.BUILD_URL}
          Allure Report: ${env.BUILD_URL}allure/
          Console Log: ${env.BUILD_URL}console
        """.stripIndent()
     )
    }
  }
}