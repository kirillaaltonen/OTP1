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
                bat '"C:\\Users\\alokas\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker.exe" build -t holuses/temperature-converter:latest .'
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([
                        string(
                            credentialsId: 'dockerhub_token',
                            variable: 'DOCKER_TOKEN'
                        )
                    ]) {
                    powershell '''
                $env:DOCKER_TOKEN | & "C:\\Users\\alokas\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker.exe" login -u holuses --password-stdin
            '''

                    bat '"C:\\Users\\alokas\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker.exe" push holuses/temperature-converter:latest'
                }
            }
        }
    }
}