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
        stage('Check Docker') {
            steps {
                bat '"C:\\Users\\alokas\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker.exe" --version'
            }
        }
        stage('Build Docker Image') {
            steps {
                bat '"C:\\Users\\alokas\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker.exe" build -t kirillaaltonen/temperature-converter:latest .'
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([
                        usernamePassword(
                            credentialsId: 'holuses_docker',
                            usernameVariable: 'DOCKER_USER',
                            passwordVariable: 'DOCKER_PASS'
                        )
                    ]) {
                    bat 'echo %DOCKER_PASS% | "C:\\Users\\alokas\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker.exe" login -u %DOCKER_USER% --password-stdin'
                    bat '"C:\\Users\\alokas\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker.exe" push kirillaaltonen/temperature-converter:latest'
                }
            }
        }
    }
}