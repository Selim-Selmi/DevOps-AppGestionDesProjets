pipeline {
    agent any

    environment {
        DOCKER_USER = 'selimselmi'
        BACK_IMAGE  = "${DOCKER_USER}/projets-backend"
        FRONT_IMAGE = "${DOCKER_USER}/projets-frontend"
        TAG         = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps { checkout scm }
        }

        stage('Build Backend') {
            steps {
                dir('backend') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Unit Tests') {
            steps {
                dir('backend') {
                    sh 'mvn test'
                }
            }
            post {
                always {
                    junit 'backend/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'backend/target/*.jar', fingerprint: true
            }
        }

        stage('Docker Login') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds',
                        usernameVariable: 'U', passwordVariable: 'P')]) {
                    sh 'echo $P | docker login -u $U --password-stdin'
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                sh "docker build -t ${BACK_IMAGE}:${TAG} -t ${BACK_IMAGE}:latest ./backend"
                sh "docker build -t ${FRONT_IMAGE}:${TAG} -t ${FRONT_IMAGE}:latest ./frontend"
            }
        }

        stage('Push Images') {
            steps {
                sh "docker push ${BACK_IMAGE}:${TAG}"
                sh "docker push ${BACK_IMAGE}:latest"
                sh "docker push ${FRONT_IMAGE}:${TAG}"
                sh "docker push ${FRONT_IMAGE}:latest"
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker compose down || true'
                sh 'docker compose up -d --build'
            }
        }
    }

    post {
        always { sh 'docker logout || true' }
        success { echo 'Pipeline OK: app is on http://localhost:4200' }
        failure { echo 'Pipeline FAILED: check the console output' }
    }
}