pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/mohamadelagamal/Review-Android-App-With-Firebase.git'
            }
        }

        stage('Setup Fastlane') {
            steps {
                sh 'bundle install'
            }
        }

        stage('Build and Deploy') {
            steps {
                sh 'fastlane android beta_prod'
            }
        }
    }
}
