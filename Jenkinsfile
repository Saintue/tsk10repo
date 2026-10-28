pipeline {
    agent {
        kubernetes {
            defaultContainer 'jnlp'
        }
    }
    stages {
        stage('Hello') {
            steps {
                echo 'Hello from GitHub!'
                sh 'hostname'
            }
        }
    }
}
