pipeline {
    environment {
        QODANA_TOKEN = credentials('qodana-token')
    }
    agent any
    stages {
        stage('Preparation') {
            steps {
                script {
                    // Check if the archived artifacts are available and extract them
                    if (currentBuild.previousBuild != null) {
                        def lastSuccessfulBuild = currentBuild.previousBuild.rawBuild
                        copyArtifacts(projectName: "${JOB_NAME}", selector: specific("${lastSuccessfulBuild.number}"), filter: "qodana-*.tgz", target: "${env.WORKSPACE}", fingerprintArtifacts: true)
                        sh "tar -xzf ${env.WORKSPACE}/qodana-cache.tgz -C /"
                        sh "tar -xzf ${env.WORKSPACE}/qodana-results.tgz -C /"
                    }
                }
            }
        }
        stage('Qodana') {
            steps {
                script {
                    if (fileExists('/data/results/qodana.sarif.json')) {
                        sh '''
                        qodana \
                        --fail-threshold <number> \
                        --baseline /data/results/qodana.sarif.json
                        '''
                    } else {
                        sh '''
                        qodana \
                        --fail-threshold <number>
                        '''
                    }
                }
            }
        }
        stage('Post-Processing') {
            steps {
                script {
                    // Pack the artifacts
                    sh "tar -czf ${env.WORKSPACE}/qodana-cache.tgz /data/cache"
                    sh "tar -czf ${env.WORKSPACE}/qodana-results.tgz /data/results/qodana.sarif.json"
                    
                    // Archive them
                    archiveArtifacts artifacts: 'qodana-*.tgz', onlyIfSuccessful: true
                }
            }
        }
    }
}
