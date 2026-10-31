def utils

pipeline {
    agent none

    stages {
        stage('Load Utils') {
            steps {
                script {
                    utils = load 'ci/ci-utils.groovy'
                }
            }
        }

        stage('Build Applications') {
            parallel {
                stage('Back-end') {
                    agent {
                        kubernetes {
                            yaml readFile('ci/kaniko-pod.yaml')
                            defaultContainer 'jnlp'
                        }
                    }
                    stages {
                        stage('Checkout Back-end') {
                            steps {
                                checkout([$class: 'GitSCM',
                                    branches: [[name: '*/main']],
                                    extensions: [[$class: 'SparseCheckoutPaths',
                                                  sparseCheckoutPaths: [
                                                      [$class: 'SparseCheckoutPath', path: 'back-end/'],
                                                      [$class: 'SparseCheckoutPath', path: 'ci/']
                                                  ]]],
                                    userRemoteConfigs: [[url: 'https://github.com/Saintue/tsk10repo.git']]
                                ])
                            }
                        }
                        stage('Build & Push Back-end') {
                            steps {
                                container('kaniko') {
                                    script {
                                        utils.buildAndPush('back-end', 'back-end')
                                    }
                                }
                            }
                        }
                    }
                }

                stage('Front-end') {
                    agent {
                        kubernetes {
                            yaml readFile('ci/kaniko-pod.yaml')
                            defaultContainer 'jnlp'
                        }
                    }
                    stages {
                        stage('Checkout Front-end') {
                            steps {
                                checkout([$class: 'GitSCM',
                                    branches: [[name: '*/main']],
                                    extensions: [[$class: 'SparseCheckoutPaths',
                                                  sparseCheckoutPaths: [
                                                      [$class: 'SparseCheckoutPath', path: 'front-end/'],
                                                      [$class: 'SparseCheckoutPath', path: 'ci/']
                                                  ]]],
                                    userRemoteConfigs: [[url: 'https://github.com/Saintue/tsk10repo.git']]
                                ])
                            }
                        }
                        stage('Build & Push Front-end') {
                            steps {
                                container('kaniko') {
                                    script {
                                        utils.buildAndPush('front-end', 'front-end')
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
