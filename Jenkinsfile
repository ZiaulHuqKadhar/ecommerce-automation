pipeline {
    agent any

    tools {
        maven 'Maven3'   // Manage Jenkins > Tools > Maven installations (name must match)
        jdk   'JDK17'    // Manage Jenkins > Tools > JDK installations (name must match)
    }

    triggers {
        cron('H 2 * * *')          // every day around 2 AM
        pollSCM('H/5 * * * *')     // check Git every 5 min, run only when code changed
    }

    options {
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean compile -DskipTests'
            }
        }

        stage('Run Tests') {
            steps {
                // Do not fail the pipeline here; we still want the report published.
                // headless=true because Jenkins has no desktop.
                catchError(buildResult: 'UNSTABLE', stageResult: 'FAILURE') {
                    bat 'mvn test -Dheadless=true'
                }
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/TEST-*.xml'
            allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
            archiveArtifacts artifacts: 'target/screenshots/**, logs/**', allowEmptyArchive: true
        }
        failure {
            echo 'Pipeline failed - open the Allure Report link on the build page.'
            // mail to: 'you@example.com', subject: "FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}", body: "${env.BUILD_URL}"
        }
    }
}
