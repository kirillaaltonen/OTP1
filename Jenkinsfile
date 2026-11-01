pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                git 'https://github.com/kirillaaltonen/OTP1'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Code Coverage') {
            steps {
                bat 'mvn jacoco:report'
            }
        }

        stage('Publish Test Results') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        }

        stage('Publish Coverage Report') {
            steps {
                recordCoverage(
                    tools: [[
                            parser: 'JACOCO',
                            pattern: 'target/site/jacoco/jacoco.xml'
                        ]]
                )
            }
        }

        stage('Build Docker Image') {
            steps {
                bat 'docker build -t kirillaaltonen/temperature-converter:latest .'
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([
                        usernamePassword(
                            credentialsId: 'docker',
                            usernameVariable: 'DOCKER_USER',
                            passwordVariable: 'DOCKER_PASS'
                        )
                    ]) {
                    bat 'echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin'
                    bat 'docker push kirillaaltonen/temperature-converter:latest'
                }
            }
        }
    }
}