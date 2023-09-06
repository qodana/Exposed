pipeline {
    environment {
        QODANA_TOKEN=credentials('qodana-token')
    }
    agent {
        docker {
            args '''
              -v "${WORKSPACE}":/data/project
              --entrypoint=""
              '''
            image 'jetbrains/qodana-jvm'
        }
    }
    stages {
        stage('Qodana') {
            steps {
                sh '''
                QODANA_BRANCH=${GIT_BRANCH} qodana
                '''
            }
        }
    }
}
